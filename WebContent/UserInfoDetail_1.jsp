<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts" %>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type"  content="text/css" />
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jp-calendar.js" charset="utf-8"></script>
<title>プロフィール</title>
<!-- ここにCSSを書いてね -->
<style type="text/css">
.Form-Item {
  border-top: 1px solid #ddd;
  width: 100%;
  display: flex;
  align-items: center;
}
@media screen and (max-width: 480px) {
  .Form-Item {
    padding-left: 14px;
    padding-right: 14px;
    padding-top: 16px;
    padding-bottom: 16px;
    flex-wrap: wrap;
  }
}
.Form-Item:nth-child(5) {
  border-bottom: 1px solid #ddd;
}
.Form-Item-Label {
  width: 100%;
  max-width: 248px;
  letter-spacing: 0.05em;
  font-weight: bold;
  font-size: 18px;
}
@media screen and (max-width: 480px) {
  .Form-Item-Label {
    max-width: inherit;
    display: flex;
    align-items: center;
    font-size: 15px;
  }
}
.Form-Item-Label.isMsg {
  margin-top: 8px;
  margin-bottom: auto;
}
@media screen and (max-width: 480px) {
  .Form-Item-Label.isMsg {
    margin-top: 0;
  }
}
.Form-Item-Label-Required {
  border-radius: 6px;
  margin-right: 8px;
  width: 48px;
  display: inline-block;
  text-align: center;
  background: #5bc8ac;
  color: #fff;
  font-size: 14px;
}
@media screen and (max-width: 480px) {
  .Form-Item-Label-Required {
    border-radius: 4px;
    padding-top: 4px;
    padding-bottom: 4px;
    width: 32px;
    font-size: 10px;
  }
}
select{
  border: 1px solid #ddd;
  border-radius: 6px;
  padding-left: 1em;
  padding-right: 1em;
  height: 25px;
  flex: 1;
  max-width: 410px;
  background: #F7FFFB;
  font-size: 18px;
}
@media screen and (max-width: 480px) {
  .Form-Item-Input {
    margin-left: 0;
    margin-top: 18px;
    height: 40px;
    flex: inherit;
    font-size: 15px;
  }
}
.Form-Item-Textarea {
  border: 1px solid #ddd;
  border-radius: 6px;
  padding-left: 1em;
  padding-right: 1em;
  flex: 1;
  width: 100%;
  max-width: 410px;
  background: #F7FFFB;
  font-size: 18px;
}
@media screen and (max-width: 480px) {
  .Form-Item-Textarea {
    margin-top: 18px;
    margin-left: 0;
    height: 200px;
    flex: inherit;
    font-size: 15px;
  }
}
.Form-Btn {
  border-radius: 6px;
  margin-top: 32px;
  width: 280px;
  letter-spacing: 0.05em;
  background: #5bc8ac;
  color: #fff;
  font-weight: bold;
  font-size: 20px;
}
@media screen and (max-width: 480px) {
  .Form-Btn {
    margin-top: 24px;
    padding-top: 8px;
    padding-bottom: 8px;
    width: 160px;
    font-size: 16px;
  }
}
.style_head3 {
    border-left: 8px solid #033 !important;
}
.style_head_size {
    height:30px;
    vertical-align: middle;
    display: table-cell;
}
.label
{
	font-weight: 600;
}
</style>
<script type="text/javascript">
//<![CDATA[
    var cal =new JP_Calendar();
    function go_list(action_cmd)
    {
        document.getElementById('main_form').action='UserMenu.do';
        document.getElementById('action_cmd').value=action_cmd;
        document.getElementById('main_form').submit();
    }
    function go_submit(action_cmd)
    {
        document.getElementById("main_form").action = '';
        document.getElementById('action_cmd').value=action_cmd;
        document.getElementById('main_form').submit();
    }

    //]]>
</script>
</head>
<body>
<form method="post" id="main_form" action="">
  <div class="container">
  <jsp:include page="./SideMenu.jsp" flush="true | false" />
    <div class="wrap">
    <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_1" />
    <input type="hidden" name="action_cmd" id="action_cmd" value="" />
    <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
    <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" />
    <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
      <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
      <div class="errors"><%=webBean.dispErrorMessages()%></div>
      <table class="left">
        <tr>
          <td class="text">
          <div class="style_head3 style_head_size">
            氏名
          </div>
          <div>
            <input type="text" name="last_name" id="last_name" size="10" maxlength="100" value="<%=webBean.txt("last_name")%>" class="ime_active <%=webBean.dispErrorCSS("last_name")%>" />
            <input type="text" name="first_name" id="first_name" size="10" maxlength="100" value="<%=webBean.txt("first_name")%>" class="ime_active <%=webBean.dispErrorCSS("first_name")%>" />
            <br />
            <%=webBean.dispError("last_name")%>
            <%=webBean.dispError("first_name")%>
            </div>
          </td>
        </tr>
        <tr>
          <td class="text">
          <div class="style_head3 style_head_size">
            氏名よみ
          </div>
          <div>
            <input type="text" name="last_name_kana" id="last_name_kana" size="10" maxlength="100" value="<%=webBean.txt("last_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("last_name_kana")%>" />
            <input type="text" name="first_name_kana" id="first_name_kana" size="10" maxlength="100" value="<%=webBean.txt("first_name_kana")%>" class="ime_active <%=webBean.dispErrorCSS("first_name_kana")%>" />
            <br />
            <%=webBean.dispError("last_name_kana")%>
            <%=webBean.dispError("first_name_kana")%>
            </div>
          </td>
        </tr>
      </table>
    <div>
        <input type="button" class="Form-Btn" value="　戻る　" onclick="go_list('return')" />
        <input type="button"  class="Form-Btn" value="<%=webBean.txt("request_name")%>" onclick="go_submit('go_next')" />
    </div>
   </div>
</form>
</body>
</html>

