<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<html>
<head>
<meta name="viewport" content="width=device-width" , initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta charset="UTF-8">
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/flatpickr.min.js"></script>
<title>パスワード再設定</title>
<style>
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
  width: 40%;
}

 .button {
  display: flex;
  justify-content: center;
  align-items: center;
}

.button input[type="submit"] {
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  padding: 0px 50px; /* ボタンの内側余白 */
  font-size: 24px; /* 文字サイズ */
  border: 2px solid #fff; /* ボタンの枠線 */
  cursor: pointer; /* カーソルをポインタにする */
  background-color: #00bcd4; /* 上書きの背景色 */
}

.button input[type="submit"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
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

p {
  text-align: center;
  margin: 20px 0;
}
</style>

<script type="text/javascript">
function go_submit(action_cmd) {
  document.getElementById('main_form').action = '';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
}

</script>
</head>
<body>
  <div class="container">
<header>
    <h1>パスワード再設定ページ</h1>
</header>

      <p><%= webBean.txt("result") %></p>
      
      <form method="get" id="main_form" action="">
      
      <input type="hidden" name="form_name" id="form_name" value="UserPassReset"/>
      <input type="hidden" name="action_cmd" id="action_cmd" value=""/>
      <input type="hidden" name="key" value="<%= request.getParameter("key") %>" />
      <input type="hidden" name="main_key" value="<%= webBean.txt("main_key") %>"/>

      <div class="left">
        <table class="input-table">
          <tr>
            <td class="style_head3 style_head_size" style="width: 50%">新しいパスワード</td>
            <td class="input-text" style="width: 50%">
              <input type="password" id="new_password" name="new_password" size="30" maxlength="100" value="<%= webBean.txt("new_password") %>" required/>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 50%">確認用パスワード</td>
            <td class="input-text" style="width: 50%">
              <input type="password" id="check_password" name="check_password" size="30" maxlength="100" value="<%= webBean.txt("check_password") %>" required/>
            </td>
          </tr>
        </table>
      </div>
      <div class="button">
        <input type="submit" value="パスワードを再設定"/>
      </div>
      </form>
    </div>
</body>
</html>