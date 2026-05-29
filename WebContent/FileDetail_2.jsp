<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<html xmlns="http://www.w3.org/1999/xhtml">
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
<title>部屋情報修正</title>
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

.file__form--name{
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

input[type="text"]{
  border: 1px solid #696969;
  border-radius: 4px;
  padding: 5px;
  background: #FFF;
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
  color:#00f;
}

.style_head_size {
  height: 30px;
  vertical-align: middle;
  display: table-cell;
  background: #00bcd4;
  color: #fff;
}

td.table-date {
  text-align: left;
  background: #fff;
  text-align: center;
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

</style>
<script type="text/javascript">
function go_submit(action_cmd, request_cmd) {
    document.getElementById('main_form').action='';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
}
function go_list(action_cmd , request_cmd) {
    document.getElementById('main_form').action = '';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
}
</script>
</head>
<body>
 <%
     String val = webBean.txt("request_name");
     String actionType =  val.equals("削除する") ? "deleteEnter" : val.equals("登録する") ? "insEnter" : "unknown";
     String requestType =  val.equals("削除する") ? "delete" : val.equals("登録する") ? "ins" : "unknown";
     String header =  val.equals("削除する") ? "ファイル削除" : val.equals("登録する") ? "ファイル登録確認" : "unknown";
   %>
  <div class="container">
    <div class="new-btn">
      <input type="button" onclick="go_list('return','<%=requestType%>')" class="submit-btn" value="　戻る　" />
    </div>
<header>
    <h1><%= header %>ページ</h1>
</header>

     <form method="post" id="main_form" action="" class="main__form">
       
       <input type="hidden" name="form_name" id="form_name" value="FileDetail_2" />
       <input type="hidden" name="action_cmd" id="action_cmd" value="" />
       <input type="hidden" name="input_name" id="input_name" value="<%=webBean.txt("input_name")%>" />
       <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
       <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
       <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
       <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
       
      
       <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
       <div class="errors"><%=webBean.dispErrorMessages()%></div>
      
       <div class="left">
         <% if ("登録する".equals(val)) { %>
         <table class="file__form--name">
           <tr>
             <td class="style_head3 style_head_size">登録ファイル名</td>
           </tr>
           <tr >
             <td class="table-date"><%=webBean.txt("input_name")%></td>
           </tr>
         </table>
         <% } else {%>
         <table class="file__form--name">
           <tr>
             <td class="style_head3 style_head_size">削除ファイル名</td>
           </tr>
           <tr >
             <td class="table-date"><%=webBean.txt("file_name")%></td>
           </tr>
         </table>
         <% } %>
       </div>
       <div class="button">
         <input type="button" id="bt" name="reg-btn"  onclick="go_submit('go_next','<%=actionType%>')" value="<%=val%>"/>
       </div>
     </form>
  </div>
</body>
</html>
