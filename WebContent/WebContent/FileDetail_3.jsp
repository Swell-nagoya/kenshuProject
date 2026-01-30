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
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f0f0f0;
}

#main {
  width: 500px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%); 
  padding: 20px;
}

#main h2 {
  margin-top: 0;
  background-color: #00bcd4;
  color: white;
  padding: 10px;
  text-align: center;
  border-radius: 5px;
}

.main__form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.ime_disabled {
  width: 100%;
  max-width: 350px;
  height: 40px;
  margin-top: 10px;
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.room__form--btn {
  margin-top: 20px;
}

.submit-btn {
  width: 200px;
  height: 40px;
  font-size: 16px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #1976d2;
}

</style>
<script type="text/javascript">
function go_submit(action_cmd, request_cmd) {
    document.getElementById('main_form').action='';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
}
function go_list(action_cmd) {
    document.getElementById('main_form').action = '';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}
</script>
</head>
<body>
<div class="container">
  <div id="contents">
     <div id="main">
       <form method="post" id="main_form" action="" class="main__form">
         <input type="hidden" name="form_name" id="form_name" value="FileDetail_3" />
         <input type="hidden" name="action_cmd" id="action_cmd" value="" />
         <input type="hidden" name="input_name" id="input_name" value="<%=webBean.txt("input_name")%>" />
         <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
         <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
         <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
         <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
         <div class="errors"><%=webBean.dispErrorMessages()%></div>
           <input type="button" onclick="go_list('return')" class="submit-btn" value="　戻る　" />
         </div>
       </form>
     </div>
  </div>
</div>
</body>
</html>
