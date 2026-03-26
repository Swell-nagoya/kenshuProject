<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
  scope="request" />
<%
    HttpSession userSession = request.getSession();
    WebBean tempBean = (WebBean) session.getAttribute("temp_user_info");
    if (tempBean != null) {
        webBean.setValue("last_name", tempBean.value("last_name"));
        webBean.setValue("middle_name", tempBean.value("middle_name"));
        webBean.setValue("first_name", tempBean.value("first_name"));
        webBean.setValue("maiden_name", tempBean.value("maiden_name"));
        webBean.setValue("last_name_kana", tempBean.value("last_name_kana"));
        webBean.setValue("middle_name_kana", tempBean.value("middle_name_kana"));
        webBean.setValue("first_name_kana", tempBean.value("first_name_kana"));
        webBean.setValue("maiden_name_kana", tempBean.value("maiden_name_kana"));
        webBean.setValue("memail", tempBean.value("memail"));
        session.removeAttribute("temp_user_info");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="icon" href="/kenshuProject/images/favicon.ico" type="image/x-icon">
<link rel="stylesheet" href="css/common.css" type="text/css" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script src="https://unpkg.com/wanakana@4.0.2/umd/wanakana.min.js"></script>
<title>プロフィール</title>
<style type="text/css">
body {
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
  margin: 0;
  padding: 10px;
}

header {
  position: relative;
  background: #00bcd4; /* ヘッダーの背景色 */
  width: 100%; /* 幅を画面いっぱいに */
  height: 70px;
  margin: 15px auto; /* 不要な余白を排除 */
  display: flex; /* Flexboxを有効にする */
  justify-content: center; /* 水平方向に中央揃え */
  align-items: center; /* 垂直方向に中央揃え */
}

h1 {
  font-size: 50px;
  color: white; /* リンクの文字色を白に */
  text-decoration: none; /* 下線を削除 */
  font-weight: normal;
}

.container {
  position: relative; /* ボタンを基準に配置するため */
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 90%; /* コンテナの幅を画面幅に揃える */
  margin: 20px auto; /* 中央寄せ */
  
}

.left {
  margin-bottom: 20px;
  text-align: center;
  display: flex;
  justify-content: center;  /* 横方向で中央に配置 */
  align-items: center;      /* 縦方向で中央に配置 */
}

.input-table{
  width: 60%;
}

/* ボタンの共通スタイル */
input[type="button"] {
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  cursor: pointer; /* カーソルをポインタにする */
  background: #90a0b0; /* デフォルトの背景色 */
}

/* ホバー時のスタイル */
input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.new-btn {
  position: absolute;
  right: 10px; /* 右端に10pxの余白を取る */
  top: 5px;   
}

/* .new-btnのスタイル */
.new-btn input {
  background: #fff; /* 背景色を白に */
  color: #000; /* 文字色を黒に */
}

 .button {
  display: flex;
  justify-content: center;
  align-items: center;
}

.button input[type="button"] {
  padding: 0px 50px; /* ボタンの内側余白 */
  font-size: 24px; /* 文字サイズ */
  border: 2px solid #fff; /* ボタンの枠線 */
  background-color: #00bcd4; /* 上書きの背景色 */
}

.button input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}


.required-note {
  position: absolute;
  top: 110px;
  right: 20px;
  font-weight: bold;
  font-size: 18px;
  color: #f00;
}

.Form-Item {
  border-top: 1px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media screen and (max-width: 480px) {
  .Form-Item {
    padding: 10px;
    flex-wrap: wrap;
  }
}

.Form-Item:nth-child(5) {
  border-bottom: 1px solid #ddd;
}

.Form-Item-Label {
  flex: 0 0 150px;
  font-weight: bold;
  font-size: 16px;
}

@media screen and (max-width: 480px) {
  .Form-Item-Label {
    font-size: 16px;
    flex: 0 0 100%;
  }
}

.Form-Item-Label-Required {
  border-radius: 4px;
  margin-right: 8px;
  padding: 2px 6px;
  background: #5bc8ac;
  color: #fff;
  font-size: 16px;
  text-align: center;
}

input[type="text"]{
  border: 1px solid #696969;
  border-radius: 4px;
  padding: 5px;
  background: #FFF;
}

input[type="email"]{
  border: 1px solid #696969;
  border-radius: 4px;
  padding: 5px;
}

.email-container input {
  width: 100%;
  max-width: 400px;
  margin-right: 0; 
}

select, .Form-Item-Input, .Form-Item-Textarea, .email-container input{
  font-size: 16px;
  width: 100%;
  max-width: 400px;
}

@media screen and (max-width: 480px) {
  select, .Form-Item-Input, .Form-Item-Textarea, .email-container input{
    font-size: 16px;
  }
}

.errors, .messages {
  margin-bottom: 20px;
  font-size: 16px
}


input::placeholder {
  color: rgba(0, 0, 0, 0.3);
}

.style_head3 {
  padding-left: 10px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.style_head_size {
  height: 30px;
  vertical-align: middle;
  display: table-cell;
  background: #00bcd4;
  color: #fff;
}

td.input-text {
  text-align: left;
  background: #fff;
}

table {
  border-collapse: collapse; 
  width: 100%;
  border: 1px solid #ddd;
}

td, th {
  border: 1px solid #ddd; 
  padding: 5px;
}

.label {
  font-weight: 600;
}

span {
  color: #f00;
  font-size: 16px;
}

span.error{
  color: #f00;
  background-color: transparent;
}

input.error {
  color: #FF0000;
  background-color: #FFCCCC;
  border: 1px solid #FF0000;
}

label.error {
  color: #FF0000; 

}
</style>
<script type="text/javascript">

  function go_list(action_cmd) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
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

  
</script>
</head>
<body>
  <div class="container">
    <div class="new-btn">
      <input type="button" value="　戻る　" onclick="go_list('return')" />
    </div>
<header>
    <h1> ユーザー情報<%=webBean.txt("request_name")%>ページ </h1>
</header>
        <div class="required-note">※は必須項目</div>
         
        <form method="post" id="main_form" action="">

        <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_1" />
        <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
        <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
        <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
        <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
        <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
        <input type="hidden" name="select_info" id="select_info" value="<%=webBean.txt("select_info")%>" />

        <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
        <div class="errors"><%=webBean.dispErrorMessages()%></div>

        <div class="left">
          <table class="input-table">
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">氏名<span> ※</span></td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="last_name" id="last_name" maxlength="100" value="<%=webBean.txt("last_name")%>" class="ime_active <%=webBean.dispErrorCSS("last_name")%>" autocomplete="family_name" placeholder="渋谷" /> 
              <input type="text" name="first_name" id="first_name" maxlength="100" value="<%=webBean.txt("first_name")%>" class="ime_active <%=webBean.dispErrorCSS("first_name")%>" autocomplete="given_name" placeholder="花子" /> 
                <br /> <span id="error_last_name" class="error"><%=webBean.dispError("last_name")%></span>
                       <span id="error_first_name" class="error"><%=webBean.dispError("first_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">氏名よみ<span> ※</span></td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="last_name_kana" id="last_name_kana" maxlength="100" value="<%=webBean.txt("last_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("last_name_kana")%>" autocomplete="family_name" placeholder="しぶや" />
              <input type="text" name="first_name_kana" id="first_name_kana" maxlength="100" value="<%=webBean.txt("first_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("first_name_kana")%>" autocomplete="given_name" placeholder="はなこ" />
                <br /> <span id="error_last_name_kana" class="error"><%=webBean.dispError("last_name_kana")%></span>
                       <span id="error_first_name_kana" class="error"><%=webBean.dispError("first_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ミドルネーム</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="middle_name" id="middle_name" maxlength="100" value="<%=webBean.txt("middle_name")%>" class="ime_active <%=webBean.dispErrorCSS("middle_name")%>" placeholder="ヒカリエ" /> 
                <br /> <span id="error_middle_name" class="error"><%=webBean.dispError("middle_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ミドルネームよみ</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="middle_name_kana" id="middle_name_kana" maxlength="100" value="<%=webBean.txt("middle_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("middle_name_kana")%>" placeholder="ひかりえ" />
                <br /> <span id="error_middle_name_kana" class="error"><%=webBean.dispError("middle_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">旧姓</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="maiden_name" id="maiden_name" maxlength="100" value="<%=webBean.txt("maiden_name")%>" class="ime_active <%=webBean.dispErrorCSS("maiden_name")%>" placeholder="原宿" />
                <br /> <span id="error_maiden_name" class="error"><%=webBean.dispError("maiden_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">旧姓よみ</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="maiden_name_kana" id="maiden_name_kana" maxlength="100" value="<%=webBean.txt("maiden_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("maiden_name_kana")%>" placeholder="はらじゅく" /> 
                <br /> <span id="error_maiden_name_kana" class="error"><%=webBean.dispError("maiden_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">任意ＩＤ</td>
            <td class=" input-text" style="width: 70%">
              <input type="text" name="insert_user_id" id="insert_user_id" maxlength="100" value="<%=webBean.txt("insert_user_id")%>" class="ime_active <%=webBean.dispErrorCSS("insert_user_id")%>" placeholder="半角英数で入力" /> ※６文字以上１２文字以下
                <br /> <span id="error_insert_user_id" class="error"><%=webBean.dispError("insert_user_id")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">メールアドレス<span> ※</span></td>
              <td class="email-container input-text" style="width: 70%">
                <input type="email" name="memail" id="memail" maxlength="255" value="<%=webBean.txt("memail")%>" class="ime_active <%=webBean.dispErrorCSS("memail")%>" placeholder="example@example.com" /> 
                <br /> <span id="error_memail" class="error"><%=webBean.dispError("memail")%></span>
              </div>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">確認用メールアドレス<span> ※</span></td>
            <td class="email-container input-text" style="width: 70%">
              <input type="email" name="memail_1" id="memail_1" size="30" maxlength="255" value="<%=webBean.txt("memail")%>" class="ime_active" placeholder="example@example.com" /> 
                <br />  <span id="error_memail_1" class="error"></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ユーザー区分<span> ※</span></td>
            <td class=" input-text" id="userTypeContainer" style="width: 70%">
              <input type="radio" name="admin" id="admin_admin" value="1" class="ime_active <%=webBean.dispErrorCSS("admin")%>" <%= webBean.txt("admin").equals("admin") ? "checked" : "" %> />
                <label for="admin_admin" class="<%=webBean.dispErrorCSS("admin")%>">管理者</label>
              <input type="radio" name="admin" id="admin_general" value="0" class="ime_active <%=webBean.dispErrorCSS("admin")%>" <%= webBean.txt("admin").equals("general") ? "checked" : "" %> />
                <label for="admin_general" class="<%=webBean.dispErrorCSS("admin")%>">一般</label> ※管理者以外は一般を選択して下さい
              <br /> <span id="error_admin" class="error"><%=webBean.dispError("admin")%></span>
            </td>
          </tr>
        </table>
        </div>
      <div class="button">
        <input type="button" id="submit_btn" value="<%=webBean.txt("request_name")%>する" onclick="go_submit('go_next','<%=webBean.txt("request_cmd")%>')" /> 
      </div>
    </form>
  </div>
</body>
</html>

