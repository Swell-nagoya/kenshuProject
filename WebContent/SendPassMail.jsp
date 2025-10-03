<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
<title>パスワード再発行</title>
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
.main__text {
  text-align: center;
  margin: 0;
  width: 60%;
}

.style_head3 {
  padding-left: 10px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.messages {
  font-size: 16px;
  margin-bottom: 20px;
  color:#0000ff;
}

</style>
<script type="text/javascript">
function go_submit(action_cmd){
  document.getElementById('main_form').action='SendPassMail.do';
  document.getElementById('action_cmd').value=action_cmd;
  document.getElementById('main_form').submit();
}
</script>
</head>
<body>
 <div class="container">
    <div class="new-btn">
      <input type="button" onclick="go_submit('return')" value="　戻る　" />
    </div>
<header>
    <h1>パスワード再発行ページ</h1>
</header>

      <form method="post" id="main_form" action="" >
      
      <input type="hidden" name="form_name" id="form_name" value="SendPassMail" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" />
  
      <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
      
      <div class="left">
        <div class="main__text">
          <input type="email" id="memail" name="memail" size="35" maxlength="100" value="<%=webBean.txt("memail")%>" placeholder="****@email" class="ime_disabled" required>
        </div>
      </div>
        <div class="button">
          <input type="button" value="送信"  onclick="go_submit('go_next')"/>
        </div>
      </form>
    </div>
  </div>
</body>
</html>