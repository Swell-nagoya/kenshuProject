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
<link type="text/css" href="css/RoomDetail02.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/flatpickr.min.js"></script>
<title>部屋情報修正</title>
<script type="text/javascript">
function go_submit(action_cmd, request_cmd)
{
    document.getElementById('main_form').action='';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
}
function go_list(action_cmd)
{
    document.getElementById('main_form').action = '';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}
</script>
</head>
<body>
 <%
     String roomName = webBean.txt("room_name");
     String val = webBean.txt("request_name");
     String actionType =  val.equals("削除する") ? "deleteEnter" : val.equals("修正確定") ? "updateEnter" : val.equals("登録確定") ? "insEnter" : "unknown";
     String header =  val.equals("削除する") ? "部屋削除" : val.equals("修正確定") ? "部屋名修正確認" : val.equals("登録確定") ? "新規部屋登録確認" : "unknown";
   %>
  <div class="container">
    <div class="new-btn">
       <input type="button" onclick="go_list('return')" value="　戻る　" />
    </div>
<header>
    <h1><%= header %>ページ</h1>
</header>
      
       <form method="post" id="main_form" action="" class="main__form">
             
       <input type="hidden" name="form_name" id="form_name" value="RoomDetail_2" />
       <input type="hidden" name="action_cmd" id="action_cmd" value="" />
       <input type="hidden" name="room_name" id="room_name" value="<%=webBean.txt("room_name")%>" />
       <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
       <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
       <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
       <input type="hidden" name="before_name" id="before_name" value="<%=webBean.txt("before_name") %>" />
       
       <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
       <div class="errors"><%=webBean.dispErrorMessages()%></div>
       
       <div class="left">
         <% if ("修正確定".equals(val)) { %>
         <table class="room__form--name">
           <tr class="table-header">
             <td>修正前</td>
             <td>修正後</td>
           </tr>
           <tr class="table-date">
             <td><%=webBean.txt("before_name")%></td>
             <td><%=webBean.txt("room_name")%></td>
           </tr>
         </table>
         <% } else if ("登録確定".equals(val)) {%>
         <table class="room__form--name">
           <tr class="table-header">
             <td>部屋名</td>
           </tr>
           <tr class="table-date">
             <td><%=webBean.txt("room_name")%></td>
           </tr>
          </table>
          <%} else if ("削除する".equals(val)) {%> <%--削除する追加--%>
         <table class="room__form--name">
           <tr class="table-header">
             <td>削除</td> 
           </tr>
           <tr class="table-date">
             <td><%=webBean.txt("room_name")%></td>
           </tr>
         </table>
         <%} %>
        </div>
          <div class="button">
            <input type="button" id="bt" name="reg-btn"  onclick="go_submit('go_next','<%=actionType%>')" value="<%=val%>"/>
          </div>
      </form>
    </div>
</body>
</html>
