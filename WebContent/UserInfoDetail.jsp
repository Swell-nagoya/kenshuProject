<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
  scope="request" />
<%
    HttpSession userSession = request.getSession();
    WebBean tempBean = (WebBean) session.getAttribute("temp_user_info");
    if (tempBean != null) {
        webBean.setValue("last_name", tempBean.value("last_name"));
        webBean.setValue("middle_name", tempBean.value("middle_name"));
        webBean.setValue("first_name", tempBean.value("first_name"));
        webBean.setValue("maiden_name", tempBean.value("maiden_name"));
        webBean.setValue("last_name_kana", tempBean.value("last_name_kana"));
        webBean.setValue("middle_name_kana", tempBean.value("middle_name_kana"));
        webBean.setValue("first_name_kana", tempBean.value("first_name_kana"));
        webBean.setValue("maiden_name_kana", tempBean.value("maiden_name_kana"));
        webBean.setValue("memail", tempBean.value("memail"));
        session.removeAttribute("temp_user_info");
    }
    String maidenName = webBean.txt("maiden_name").trim();
    String insertUserId = webBean.txt("insert_user_id").trim();
    String val = webBean.txt("request_name");
     
    String actionType =  val.equals("登録") ? "ins" : val.equals("登録確定") ? "insConfirm" 
                       : val.equals("修正") ? "update" : val.equals("修正確定") ? "updateConfirm" 
                       : val.equals("削除") ? "delete" : val.equals("確定") ? "deleteConfirm"
                       : val.equals("メール送信") ? "send" : "unknown";
    String actionBtn =  val.equals("メール送信") ? "go_mail" : "go_submit";
    
    String header =  val.equals("登録") ? "登録情報" : val.equals("登録確定") ? "登録確定" 
    	           : val.equals("修正") ? "情報修正": val.equals("修正確定") ? "情報編集確定" 
    	           : val.equals("削除") ? "情報削除" : val.equals("確定") ? "情報削除確定" 
    	           : val.equals("メール送信") ? "情報確認" : "unknown";
   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="icon" href="images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<% 
   if (
      webBean.txt("request_name").equals("登録") || 
      webBean.txt("request_name").equals("修正")
    ) {
%>
<script src="https://unpkg.com/wanakana@4.0.2/umd/wanakana.min.js"></script>
<% 
    } else if (
      webBean.txt("request_name").equals("削除")) {
%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/datePicker.js"></script>
<% 
    } else if (
      webBean.txt("request_name").equals("登録確定") ||
      webBean.txt("request_name").equals("修正確定") ||
      webBean.txt("request_name").equals("確定") ||
      webBean.txt("request_name").equals("メール送信")) {
%>
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<%
    } else {}
%>

<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/common.js"></script>
<% 
    if (
      webBean.txt("request_name").equals("登録") || 
      webBean.txt("request_name").equals("修正")
    ) {
%>
<link rel="stylesheet" href="css/UserInfoDetail01.css" type="text/css" />
<script type="text/javascript" src="js/UserInfoDetail01.js"></script>
<% 
    } else if (
      webBean.txt("request_name").equals("削除")) {%>
<link rel="stylesheet" href="css/UserInfoDetail02.css" type="text/css" />
<script type="text/javascript" src="js/UserInfoDetail02.js"></script>
<% 
    } else if (
      webBean.txt("request_name").equals("登録確定") ||
      webBean.txt("request_name").equals("修正確定") ||
      webBean.txt("request_name").equals("確定") ||
      webBean.txt("request_name").equals("メール送信")) {
%>
<link rel="stylesheet" href="css/UserInfoDetail03.css" type="text/css" />
<script type="text/javascript" src="js/UserInfoDetail03.js"></script>
<% 
    } else {}
 %>
<title>ユーザー<%=header%>ページ</title>
</head>
<body>
  <div class="container">
    <div class="new-btn">
    
<%
    if (
       webBean.txt("request_name").equals("登録確定") ||
       webBean.txt("request_name").equals("修正確定") ||
       webBean.txt("request_name").equals("確定") ||
       webBean.txt("request_name").equals("メール送信")) {
%>
      <input type="button" value=" 戻る " onclick="go_list('return','<%=actionType%>','<%=webBean.txt("user_info_id")%>')" />
    
<%
    } else { 
%>
      <input type="button" value="　戻る　" onclick="go_list('return')" />
<%  
    }
%>
    </div>
    <header>
        <h1>ユーザー<%=header%>ページ</h1>
    </header>
<% 
    if (
        webBean.txt("request_name").equals("登録") || 
        webBean.txt("request_name").equals("修正")) {
%>
    <div class="required-note">※は必須項目</div>
<% 
    }
%>
    <form method="post" id="main_form" action="">

      <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
      <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
      <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
      <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
      <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
      <input type="hidden" name="select_info" id="select_info" value="<%=webBean.txt("select_info")%>" />

      
      <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
        
<% 
    if (
        webBean.txt("request_name").equals("登録") || 
        webBean.txt("request_name").equals("修正") || 
        webBean.txt("request_name").equals("削除") ) {
%>
        <div class="errors"><%=webBean.dispErrorMessages()%></div>
<% 
  }
%>
<% 
    if (
        webBean.txt("request_name").equals("登録") || 
        webBean.txt("request_name").equals("修正")) {
%>
        <div class="left">
          <table class="input-table">
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">氏名<span> ※</span></td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="last_name" id="last_name" maxlength="100" value="<%=webBean.txt("last_name")%>" class="ime_active <%=webBean.dispErrorCSS("last_name")%>" autocomplete="family_name" placeholder="渋谷" /> 
              <input type="text" name="first_name" id="first_name" maxlength="100" value="<%=webBean.txt("first_name")%>" class="ime_active <%=webBean.dispErrorCSS("first_name")%>" autocomplete="given_name" placeholder="花子" /> 
                <br /> <span id="error_last_name" class="error"><%=webBean.dispError("last_name")%></span>
                       <span id="error_first_name" class="error"><%=webBean.dispError("first_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">氏名よみ<span> ※</span></td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="last_name_kana" id="last_name_kana" maxlength="100" value="<%=webBean.txt("last_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("last_name_kana")%>" autocomplete="family_name" placeholder="しぶや" />
              <input type="text" name="first_name_kana" id="first_name_kana" maxlength="100" value="<%=webBean.txt("first_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("first_name_kana")%>" autocomplete="given_name" placeholder="はなこ" />
                <br /> <span id="error_last_name_kana" class="error"><%=webBean.dispError("last_name_kana")%></span>
                       <span id="error_first_name_kana" class="error"><%=webBean.dispError("first_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ミドルネーム</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="middle_name" id="middle_name" maxlength="100" value="<%=webBean.txt("middle_name")%>" class="ime_active <%=webBean.dispErrorCSS("middle_name")%>" placeholder="ヒカリエ" /> 
                <br /> <span id="error_middle_name" class="error"><%=webBean.dispError("middle_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ミドルネームよみ</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="middle_name_kana" id="middle_name_kana" maxlength="100" value="<%=webBean.txt("middle_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("middle_name_kana")%>" placeholder="ひかりえ" />
                <br /> <span id="error_middle_name_kana" class="error"><%=webBean.dispError("middle_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">旧姓</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="maiden_name" id="maiden_name" maxlength="100" value="<%=webBean.txt("maiden_name")%>" class="ime_active <%=webBean.dispErrorCSS("maiden_name")%>" placeholder="原宿" />
                <br /> <span id="error_maiden_name" class="error"><%=webBean.dispError("maiden_name")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">旧姓よみ</td>
            <td class="input-text" style="width: 70%">
              <input type="text" name="maiden_name_kana" id="maiden_name_kana" maxlength="100" value="<%=webBean.txt("maiden_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("maiden_name_kana")%>" placeholder="はらじゅく" /> 
                <br /> <span id="error_maiden_name_kana" class="error"><%=webBean.dispError("maiden_name_kana")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">任意ＩＤ</td>
            <td class=" input-text" style="width: 70%">
              <input type="text" name="insert_user_id" id="insert_user_id" maxlength="100" value="<%=webBean.txt("insert_user_id")%>" class="ime_active <%=webBean.dispErrorCSS("insert_user_id")%>" placeholder="半角英数で入力" /> ※６文字以上１２文字以下
                <br /> <span id="error_insert_user_id" class="error"><%=webBean.dispError("insert_user_id")%></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">メールアドレス<span> ※</span></td>
              <td class="email-container input-text" style="width: 70%">
                <input type="email" name="memail" id="memail" maxlength="255" value="<%=webBean.txt("memail")%>" class="ime_active <%=webBean.dispErrorCSS("memail")%>" placeholder="example@example.com" /> 
                <br /> <span id="error_memail" class="error"><%=webBean.dispError("memail")%></span>
              </div>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">確認用メールアドレス<span> ※</span></td>
            <td class="email-container input-text" style="width: 70%">
              <input type="email" name="memail_1" id="memail_1" size="30" maxlength="255" value="<%=webBean.txt("memail")%>" class="ime_active" placeholder="example@example.com" /> 
                <br />  <span id="error_memail_1" class="error"></span>
            </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">ユーザー区分<span> ※</span></td>
            <td class=" input-text" id="userTypeContainer" style="width: 70%">
              <input type="radio" name="admin" id="admin_admin" value="1" class="ime_active <%=webBean.dispErrorCSS("admin")%>" <%= webBean.txt("admin").equals("admin") ? "checked" : "" %> />
                <label for="admin_admin" class="<%=webBean.dispErrorCSS("admin")%>">管理者</label>
              <input type="radio" name="admin" id="admin_general" value="0" class="ime_active <%=webBean.dispErrorCSS("admin")%>" <%= webBean.txt("admin").equals("general") ? "checked" : "" %> />
                <label for="admin_general" class="<%=webBean.dispErrorCSS("admin")%>">一般</label> ※管理者以外は一般を選択して下さい
              <br /> <span id="error_admin" class="error"><%=webBean.dispError("admin")%></span>
            </td>
          </tr>
        </table>
        </div>
      <div class="button">
        <input type="button" id="submit_btn" value="<%=webBean.txt("request_name")%>する" onclick="go_submit('go_next','<%=webBean.txt("request_cmd")%>')" /> 
      </div>
<% 
    } else if (
      webBean.txt("request_name").equals("削除")) {
%>
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
<% 
    } else if (
      webBean.txt("request_name").equals("登録確定") ||
      webBean.txt("request_name").equals("修正確定") ||
      webBean.txt("request_name").equals("確定") ||
      webBean.txt("request_name").equals("メール送信")) {
%>
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
           <% if (!maidenName.isEmpty()) { %>
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
          <%
          // actionTypeが"ins"の場合のみ表示
          if ("ins".equals(actionType)) { 
          %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> パスワード </td>
            <td class="input-text" style="width: 70%"> <%= webBean.txt("password") %> </td>
          </tr>
           <%
          }
          %>
            <% 
            // actionTypeが"delete"の場合のみ表示
            if ("deleteConfirm".equals(actionType)) { 
           // 退職予定日のフォーマット変換用変数を初期化
              String formatLeaveDate = "";
              String leaveDate = webBean.txt("leave_date");
              // HTML エスケープ処理し、フォーマット変換
              leaveDate = WebUtil.htmlEscape(leaveDate);
              if (leaveDate != null && leaveDate.length() >= 8) {
                formatLeaveDate = leaveDate.substring(0, 4) + "/" + leaveDate.substring(4, 6) + "/" + leaveDate.substring(6, 8);
            } else {
                formatLeaveDate = "";
            }
            %>
          <tr>
            <td class="style_head3 style_head_size"> 退職予定日 </td>
            <td class="input-text"><%= formatLeaveDate %> </td>
          </tr>
            <% 
            }
            %>
          </table>
        </div>
        <div class="button">
          <input type="button" id="submitButton" value="<%=val%>" onclick="<%=actionBtn%>('go_next','<%=actionType%>','<%=webBean.txt("user_info_id")%>')" />
        </div>
<% 
     } else {}
%>
    </form>
  </div>
</body>
</html>

