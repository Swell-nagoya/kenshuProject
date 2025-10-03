<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.ShiftDAO"%>
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
<html>
<head>
  <title>シフト情報 確認</title>
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
  color: blue; 
  text-decoration: none; /* 下線を削除 */
  font-weight: normal;
}
td {
	padding: 10px;
	border-bottom: 1px solid #ddd;
}
.container {
  position: relative; /* ボタンを基準に配置するため */
  background-color: #e0e0e0;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.78);
  width: 90%; /* コンテナの幅を画面幅に揃える */
  margin: 20px auto; /* 中央寄せ */
  
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

</style>
<script type="text/javascript">

  function go_back(action_cmd, request_cmd) {
      document.getElementById("main_form").action = "Shift.do";
      document.getElementById("action_cmd").value = "return";
      document.getElementById("main_form").submit();
    }
  </script>
</head>
<body>
  <div class="container">
    <h1><%= webBean.txt("request_name") %></h1>

    <form method="post" id="main_form">
      <input type="hidden" name="form_name" id="form_name" value="ShiftDetail_3" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="go_next" />
      <input type="hidden" name="request_cmd" id="request_cmd" value="<%= webBean.txt("request_cmd") %>" />
      <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
      <input type="hidden" name="input_info" id="input_info" value="<%= webBean.txt("input_info") %>" />
      <input type="hidden" name="main_key" id="main_key" value="<%= webBean.txt("main_key") %>" />
    


        <input type="button" value="戻る" onclick="go_back('go_back','return')" />
      </div>
    </form>
  </div> 
</body>
</html>