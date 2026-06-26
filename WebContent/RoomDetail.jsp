<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
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
<%


  String val = webBean.txt("request_name");
  String pageNum1 = "0";
  String pageNum2 = "0";
  //　ページ分岐（登録・修正 入力）
  if(
    (val.equals("登録")) || 
    (val.equals("修正"))) {
 	  pageNum1 = "1";
  //　ページ分岐（削除・修正・登録 確定画面）
  } else if(
    (val.equals("削除")) || 
    (val.equals("修正確定")) ||
    (val.equals("登録確定"))) {
  	  pageNum1 = "2";
  } else {}
  
  
  if (
    webBean.txt("request_name").equals("登録確定") ||
    webBean.txt("request_name").equals("修正確定") ) {
  	pageNum2 = "1";
 } else {
 	pageNum2 = "2";
 }
  
  if (pageNum1.equals("2")){
   String roomName = webBean.txt("room_name");
  }


  String actionType = val.equals("登録") ?  "ins": val.equals("登録確定") ? "insConfirm"
                                                 : val.equals("修正") ? "update" : val.equals("修正確定") ? "updateConfirm" 
                                                 : val.equals("削除") ? "deleteConfirm" : val.equals("確定") ? "deleteConfirm"
                                                 : "unknown";
  String header = val.equals("登録") ? "新規部屋登録" : val.equals("登録確定") ? "新規部屋登録確認"
                : val.equals("修正") ? "部屋名修正" : val.equals("修正確定") ? "部屋名修正確認"
                : val.equals("削除") ? "部屋削除" : val.equals("確定") ? "部屋削除確定" : "unknown";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width" , initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta charset="UTF-8">
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link type="text/css" href="css/RoomDetail.css" rel="stylesheet"/>
<% if (pageNum1.equals("1")){ %>
<link type="text/css" href="css/RoomDetail01.css" rel="stylesheet"/>
<% } else if (pageNum1.equals("2")){ %>
<link type="text/css" href="css/RoomDetail02.css" rel="stylesheet"/>
<% } else {} %>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/flatpickr.min.js"></script>
<title>部屋登録画面</title>

<% if (pageNum2.equals("1")){ %>
<script type="text/javascript" src="js/RoomDetail01.js"></script>
<% } else if (pageNum2.equals("2")){ %>
<script type="text/javascript" src="js/RoomDetail02.js"></script>
<% } else {} %>
</head>
<body>
  <div class="container">
    <div class="new-btn">
<%
    if (pageNum2 == "1") {
%>
      <input type="button" value=" 戻る " onclick="go_list('return','<%=actionType%>','<%=webBean.txt("room_id")%>')" />
<%
    } else { 
%>
      <input type="button" value="　戻る　" onclick="go_list('return')" />
<%  
    }
%>
    </div>
    <header>
      <h1><%= header %>ページ</h1>
    </header>
    <form method="post" id="main_form" action=""<% if (pageNum1.equals("2")){ %> class="main__form"<% } %>>
            
            <input type="hidden" name="form_name" id="form_name" value="RoomDetail" />
            <input type="hidden" name="action_cmd" id="action_cmd" value="" />
            <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
            <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" />
            <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
            <input type="hidden" name="before_name" id="before_name" value="<%=webBean.txt("before_name")%>" /
            <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
            
             
<% if (pageNum1.equals("2")){ %>
            <input type="hidden" name="room_name" id="room_name" value="<%=webBean.txt("room_name")%>" />
<% } %>
            <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
            <div class="errors text-center"><%=webBean.dispErrorMessages()%></div>
<% if (pageNum1.equals("1")){ %>
            <%
              Map<String, String> itemErrors = webBean.getItemErrors();
            %>
            <div class="left">
              <div class="room__form--name">
                <input type="text" id="room_name" name="room_name" class="ime_disabled" value="<%=webBean.txt("room_name")%>" placeholder="RoomName" size="25" maxlength="255" />
            <%
              if (itemErrors.containsKey("room_name_empty")) { 
            %>
             <div class="field-error errors text-center"><%=itemErrors.get("room_name_empty")%></div>
            <%
              }
            %>
            
            <%
              if (itemErrors.containsKey("room_name_duplicate")) {
            %>
              <div class="field-error errors text-center"><%=itemErrors.get("room_name_duplicate")%></div>
            <%
              }
            %>
              </div>
            </div>
            <!-- ./left -->
<% } else if(pageNum1.equals("2")) { %>
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
            <% } else if ("登録確定".equals(val)) { %>
              <table class="room__form--name">
                <tr class="table-header">
                  <td>部屋名</td>
                </tr>
                <tr class="table-date">
                  <td><%=webBean.txt("room_name")%></td>
                </tr>
               </table>
          <% } else if ("削除".equals(val)) { %>
               <table class="room__form--name">
                 <tr class="table-header">
                   <td>削除</td> 
                 </tr>
                 <tr class="table-date">
                   <td><%=webBean.txt("room_name")%></td>
                 </tr>
               </table>
         <% } %>
             </div>
             <!-- ./left -->
<% } else {} %>
            <div class="button">
                <input type="button" id="bt" name="reg-btn"  onclick="go_submit('go_next', '<%=actionType%>')" value="<%=val%>"/>
            </div>
             <!-- ./button -->
          </form>
    </div>
    <!-- ./container -->
</body>
</html>
