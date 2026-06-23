"use strict";

function go_list(action_cmd) {
  document.getElementById('main_form').action = 'UserInfoDetail.do';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
  console.log('hit');
  return false;
}

function go_submit(action_cmd, request_cmd) {
  document.getElementById('main_form').action = 'UserInfoDetail.do';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('request_cmd').value = request_cmd;
  document.getElementById('main_form').submit();
}

// 入力欄でenterキーが押された場合の処理
jQuery(function($) {
  $("table.input-table input").keydown(function (e) {
    if (e.which === 13) {
      e.preventDefault();  // Enterキーでのデフォルト動作をキャンセル
      let nextInput = $('table.input-table input').eq($('table.input-table input').index(this) + 1);  // 次のinput要素を取得
      if (nextInput.length) {
        nextInput.focus();  // 次のinputにフォーカスを移動
      }
    }
  });
});


// イベントリスナーの設定はそのまま
const lastNameInput = document.getElementById("last_name");
if (lastNameInput) {
  lastNameInput.addEventListener("input", async function () {
      const lastName = this.value;
      const lastNameKana = await fetchKanaFromAPI(lastName);
      document.getElementById("last_name_kana").value = lastNameKana;
  });
}

const firstNameInput = document.getElementById("first_name");
if (firstNameInput) {
  firstNameInput.addEventListener("input", async function () {
      const firstName = this.value;
      const firstNameKana = await fetchKanaFromAPI(firstName);
      document.getElementById("first_name_kana").value = firstNameKana;
  });
}

$(document).ready(function() {
    // ひらがな変換を保持するための変数
    let lastHiraganaInput = '';  // 最後のひらがな入力を保存

    // 氏名の入力があるたびに変換
    $('#last_name, #first_name').on('input', function() {
        // 氏名の内容を取得
        let inputText = $(this).val();
        let kanaText = '';

        // ひらがな部分を取り出し、保持する
        kanaText = getHiragana(inputText);
        if (kanaText) {
            lastHiraganaInput = kanaText;  // ひらがなを保存
        }

        // ひらがなを氏名の氏名よみフィールド（kana）に反映
        if ($(this).attr('id') === 'last_name') {
            $('#last_name_kana').val(lastHiraganaInput);  // ひらがなを氏名よみフィールドに反映
        } else if ($(this).attr('id') === 'first_name') {
            $('#first_name_kana').val(lastHiraganaInput);  // ひらがなを氏名よみフィールドに反映
        }
    });

    // ひらがな部分を抽出する関数
    function getHiragana(value) {
        // ひらがなだけを抽出して返す
        var hiraganaPattern = /[\u3040-\u309Fー]+/g;
        var matches = value.match(hiraganaPattern);
        if (matches) {
            return matches.join('');  // 複数のひらがな部分を1つに結合
        }
        return '';  // ひらがな部分がない場合は空
    }
});


$(document).ready(function() {
    // 氏名の入力フィールドで入力が行われた時に関数を実行
    $('#last_name, #first_name').on('input', function() {
        $(this).removeClass('error'); // 入力が行われたらerrorクラスを削除
        $('#error_' + $(this).attr('name')).text(''); // エラーメッセージをクリア
    });

    // 氏名よみ、ミドルネームよみ、旧姓よみの入力フィールドで入力が行われた時に関数を実行
    $('#last_name_kana, #first_name_kana, #middle_name_kana, #maiden_name_kana').on('input', function() {
        var fieldName = $(this).attr('name');
        var value = $(this).val();
        if (fieldName === 'last_name_kana' || fieldName === 'first_name_kana' || fieldName === 'middle_name_kana' || fieldName === 'maiden_name_kana') {
            if (isHiragana(value)) {
                $(this).removeClass('error');
                $('#error_' + $(this).attr('name')).text('');
            }
        }
    });

    function isHiragana(value) {
        // 正規表現を使ってひらがなの範囲と伸ばし棒（ー）を含むことを確認
        var hiraganaPattern = /^[\u3040-\u309Fー]+$/;
        return hiraganaPattern.test(value);
    }

    // ＩＤの入力フィールドで入力が行われた時に関数を実行
    $('#insert_user_id').on('input', function() {
        var idPattern = /^[a-zA-Z0-9]+$/;
        var value = $(this).val();

        if (idPattern.test(value)) {
            // 半角英数で入力が行われた場合
            $(this).removeClass('error');
            $('#error_' + $(this).attr('name')).text('');
        }
    });

    // メールアドレスの入力フィールドで入力が行われた時に関数を実行
    $('#memail').on('input', function() {
        var emailPattern = /^[a-zA-Z0-9_+-]+(\.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$/;
        var value = $(this).val();

        if (emailPattern.test(value)) {
            // メールアドレス形式が正しい場合
            $(this).removeClass('error');
            $('#error_' + $(this).attr('name')).text('');
        }
    });

    // ユーザー区分のラジオボタンに変更があったときにエラーを外す処理
    $('input[name="admin"]').on('change', function() {
        // ラジオボタンが選択された時にエラークラスを外す
        $('#userTypeContainer').find('.error').each(function() {
            $(this).removeClass('error');
        });
        
        // エラーメッセージのテキストも空にする
        $('#error_admin').text('');
    });
});

$(document).ready(function() {
    // メールアドレスと確認用メールアドレスのフィールドを監視
    $('#memail, #memail_1').on('input', function() {
      // 入力フィールドの値を取得
      var email = $('#memail').val();
      var confirmEmail = $('#memail_1').val();

      // メールアドレスと確認用メールアドレスが一致するかどうかをチェック
      if (email !== confirmEmail) {
        // 一致しない場合、エラークラスを追加
        $('#memail_1').addClass('error');
        $('#error_memail_1').text('メールアドレスが一致しません');
        $('#submit_btn').prop('disabled', true);
      } else {
        // 一致する場合、エラークラスを削除
        $('#memail_1').removeClass('error');
        $('#error_memail_1').text('');
        $('#submit_btn').prop('disabled', false);
      }
    });
  });