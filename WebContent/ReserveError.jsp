<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>予約エラー</title>
<style>
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
    background-color: #f0f0f0;
}

.container {
    background-color: white;
    width: 800px;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    margin: 0 auto;
}

h1 {
    color: #333;
    border-bottom: 4px dotted #800080;
    padding-bottom: 10px;
}

.buttons {
    margin-top: 20px;
    text-align: center;
}

.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    margin: 0 10px;
    text-decoration: none;
    display: inline-block;
}

.btn-primary {
    background-color: #4CAF50;
    color: white;
}
.error-message {
    color: red;
    font-weight: bold;
    margin-bottom: 20px;
    margin-bottom: 20px;
    font-size: 18px;
    text-align: center;
  }
    </style>
<script type="text/javascript">
    function go_menu(action_cmd) {
        document.getElementById('main_form').action = 'UserYoyakuDetail.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
</script>    
</head>
<body>
 <div class="container">
    <h1>予約エラー</h1>
    
      <form method="post" id="main_form" action="">

        <input type="hidden" name="form_name" id="form_name" value="ReserveError" /> 
        <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
        <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
        <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
        <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
    <div class="error-message">
        予約時間が既存の予約と重複しています
    </div>
    <div class="buttons">
        <button onclick="go_menu('return')" class="btn btn-primary">戻る</button>
    </div>
</body>
</html>
