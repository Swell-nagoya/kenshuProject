<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
  scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/datePicker.js"></script>
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


.errors,.messages {
  font-size: 16px;
  margin-bottom: 20px;
}

.left, .text {
  margin: 10 auto;
  text-align: center;
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

table {
    width: 100%;
    border: 1px solid #ddd;
  }

  td, th {
    border: 1px solid #ddd; 
    padding: 5px;
  }

.input-text {
  font-size: 20px;
  font-weight: bold;
  background: #fff;
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

}
</style>
<script type="text/javascript">
  
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
      function isNumeric(value) {
          // 正規表現を使用して値が数字だけで構成されているかどうかをチェックし、その結果を返す
          return !isNaN(value) && isFinite(value); 
      }
</script>
</head>
<body>
  <div class="container">
    <div class="new-btn">
      <input type="button" value=" 戻る " onclick="go_list('return')" />
    </div>
<header>
    <h1>ユーザー情報削除ページ
    </h1>
</header>

      <form method="post" id="main_form" action="">

        <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_2" /> 
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
            <td class="style_head3 style_head_size" style="width: 30%"> ユーザーID </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("user_info_id")%> </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 氏名 </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("last_name")%>　<%=webBean.txt("middle_name")%>　<%=webBean.txt("first_name")%> </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 氏名よみ </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("last_name_kana")%>　<%=webBean.txt("middle_name_kana")%>　<%=webBean.txt("first_name_kana")%> </td>
          </tr>
           <% 
               String maidenName = webBean.txt("maiden_name").trim();
               if (!maidenName.isEmpty()) { 
           %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 旧姓 </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("maiden_name")%> </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 旧姓よみ </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("maiden_name_kana")%> </td>
          </tr>
          <% } %>
          <% 
               String insertUserId = webBean.txt("insert_user_id").trim();
               if (!insertUserId.isEmpty()) { 
          %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 任意ＩＤ </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("insert_user_id")%> </td>
          </tr>
          <% } %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> メールアドレス </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("memail")%> </td>
          </tr>
           <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> ユーザー区分 </td>
            <td class="input-text" style="width: 70%"> <%= webBean.txt("admin").equals("admin") ? "管理者" : "一般" %> </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">退職予定日</td>
              <td class="input-text" style="width: 70%">
                <input type="text" name="leave_date" id="leave_date_input" value="<%=webBean.txt("leave_date")%>" class="input-text ime_active <%=webBean.dispErrorCSS("leave_date")%>">
                <br /> <span id="error_leave_date" class="error"><%=webBean.dispError("leave_date")%> </span>
              </td>
          </tr>
          </table>
        </div>
        <div class="button">
          <input type="button" id="submitButton" value=" 確定する " onclick="go_submit('go_next','delete','<%=webBean.txt("user_info_id")%>')" />
        </div>
    </form> 
  </div>
  <script type="text/javascript">
  </script>
</body>
</html>
