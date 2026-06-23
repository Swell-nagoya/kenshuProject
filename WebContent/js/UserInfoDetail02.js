"use strict";

function go_submit(action_cmd, request_cmd,main_key) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
}

function go_list(action_cmd) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}

function togglePassword(id) {
    var input = document.getElementById(id);
    var type = input.getAttribute('type');
    input.setAttribute('type', type === 'password' ? 'text' : 'password');
}
function isNumeric(value) {
    // 正規表現を使用して値が数字だけで構成されているかどうかをチェックし、その結果を返す
    return !isNaN(value) && isFinite(value); 
}

$(function() {
    $("#leave_date_input").datepicker();
    $("#leave_date_input").on("change",function() {
        var value = $(this).val();
        var value1 = value.replaceAll("-","");
        $("#leave_date").val(value1);
    });
});

$(document).ready(function() {
    // 退職予定日の入力フィールドで入力が行われた時に関数を実行
    $('#leave_date_input').on('change', function() {
        // name 属性を fieldName 変数に格納し、値を value 変数に格納
        var fieldName = $(this).attr('name');
        var value = $(this).val();

        // 現在のフィールドが leave_dateである場合に、以下の処理を実行する条件を指定
        if (fieldName === 'leave_date') {
            if (isNumeric(value)) { // 数字であるかどうかを判断
                $(this).removeClass('error'); // クラス削除
                $('#error_' + fieldName).text(''); // エラーメッセージ非表示
            }
        }
    });
});