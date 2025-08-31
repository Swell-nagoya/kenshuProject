<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao" %>
<%@ page import="jp.patasys.common.http.WebUtil" %>
<%@ page import="jp.patasys.common.http.HtmlParts" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/dialog_select_organization.js" charset="utf-8"></script>
<title>従業員一覧</title>
<style type="text/css">
.provisioonal
{
  background-color : #ccccff;
}
.normally
{
  background-color : #ccffcc;
}
.suspended
{
  background-color : #ffffcc;
}
.deleted
{
  background-color : #cccccc;
}
.unknown
{
  background-color : #ff0000;
}
a
{
  color : #0000ff;
  text-decoration:underline;
}
</style>
<script type="text/javascript">
//<![CDATA[
<%--検索条件入力でenterキーが押された場合の処理--%>
jQuery(function($)
{
  $(".select_table input").keydown(function (e)
  {
    if(e.which == 13)
    {
        go_submit('search');
    }
  });
  $(".page_table input").keydown(function (e)
  {
    if(e.which == 13)
    {
        go_submit('jump');
    }
  });
});
<%--テーブルを一行ごとにいろを変える--%>
  $(document).ready(function(){
        $('table.list_table tr:even').addClass('even');
        $('table.list_table tr:odd').addClass('odd');
  });
  function go_submit(action_cmd)
  {
    document.getElementById('main_form').action='';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('main_form').submit();
  }
  function go_sort_request(key)
  {
    document.getElementById('sort_key').value=key;
    document.getElementById('action_cmd').value='sort';
    document.getElementById('main_form').submit();
  }
  function go_menu(action_cmd)
  {
      document.getElementById('main_form').action='UserMenu.do';
      document.getElementById('action_cmd').value=action_cmd;
      document.getElementById('main_form').submit();
  }
  function go_detail(action_cmd,main_key)
  {
    document.getElementById('main_form').action='MitsumoriDetail.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
  }
//]]>
  function copyToClipboard(str) {

	  navigator.clipboard.writeText(str)
  }
</script>
</head>
<body onload="createMenu();">
  <form id="main_form" method="post" action="">
    <div class="container">
      <input type="hidden" name="form_name" id="form_name" value="ViewUserList" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" />
      <input type="hidden" name="main_key" id="main_key" value="" />
      <input type="hidden" name="sort_key_old" id="sort_key_old" value="<%=webBean.txt(" sort_key_old ")%>" />
      <input type="hidden" name="sort_key" id="sort_key" value="" />
      <input type="hidden" name="sort_order" id="sort_order" value="<%=webBean.txt(" sort_order ")%>" />
      <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt(" search_info ")%>" />
      <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt(" user_info_id ")%>" />
      <div class="left">
        <table class="title">
          <tr>
            <td class="title_left"><input type="button" value="　戻る　" onclick="go_menu('menu')" /><input type="button" value="新規登録" onclick="go_menu('menu')" /></td>
            <td class="title_center">ユーザ情報画面</td>
            <td class="title_right">
              <%=webBean.dispNowDate() %>
            </td>
          </tr>
        </table>
        <div> 　 </div>
        <div class="messages">
          <%=webBean.dispMessages()%>
        </div>
        <div class="errors">
          <%=webBean.dispErrorMessages()%>
        </div>
        <table class="select_table">
          <tr>
            <td class="search_label center">氏名</td>
            <td class="search_label center">氏名よみ</td>
            <td class="search_label center">表示件数</td>
            <td class="search_label center"><input type="button" value="検索条件クリア" onclick="go_submit('clear')" /></td>
          </tr>
          <tr>
            <td class="search_text center"><input type="text" name="list_search_full_name" id="flist_search_ull_name" size="10" maxlength="100" value="<%=webBean.txt(" list_search_full_name ") %>" class="ime_active <%=webBean.dispErrorCSS(" list_search_full_name ")%>" />
              <%=webBean.dispError("list_search_full_name")%>
            </td>
            <td class="search_text center"><input type="text" name="list_search_full_name_kana" id="list_search_full_name_kana" size="10" maxlength="100" value="<%=webBean.txt(" list_search_full_name_kana ") %>" class="ime_active <%=webBean.dispErrorCSS(" list_search_full_name_kana ")%>" />
              <%=webBean.dispError("list_search_full_name_kana")%>
            </td>
            <td class="search_text center"><input type="text" name="lineCount" id="lineCount" size="2" maxlength="5" value="<%=webBean.txt(" lineCount ")%>" class="right ime_disabled" />件</td>
            <td class="search_label center"><input type="button" value="検索" onclick="go_submit('search')" /></td>
          </tr>
        </table>
        <%if(webBean.arrayList("list").size()>0){%>
          <table class="page_table">
            <tr>
              <td class="left" style="width:33%;">
                <%if(!"1".equals(webBean.value("pageNo"))){%> <input type="button" value="<--前の<%=webBean.html(" lineCount ")%>件" onclick="go_submit('prior')" />
                  <%}else{%>
                    <%}%>
              </td>
              <td class="center" style="width:34%;"><input type="text" name="pageNo" id="pageNo" maxlength="3" size='1' value="<%=webBean.txt(" pageNo ")%>" class="right ime_disabled" /> /
                <%=webBean.html("maxPageNo")%> ページを表示しています〚全
                  <%=webBean.html("recordCount")%>件〛 <input type="button" value="ページ表示" onclick="go_submit('jump')" /></td>
              <td class="right" style="width:33%;">
                <%if(!webBean.value("pageNo").equals(webBean.value("maxPageNo"))){%> <input type="button" value="次の<%=webBean.html(" lineCount ")%>件-->" onclick="go_submit('next')" />
                  <%}else{%>
                    <%}%>
              </td>
            </tr>
          </table>
          <script type="text/javascript">
            //<![CDATA[
              function window_open(ref)
              {
                window.open(ref);
                return false;
              }
            //]]>
          </script>
          <table class="list_table">
            <tr class="list_title">
              <td class="list_label"><a href="javaScript:go_sort_request('full_name')">氏名</a></td>
              <td class="list_label"><a href="javaScript:go_sort_request('email')">階級</a></td>
              <td class="list_label"><a href="javaScript:go_sort_request('mtel')">　</a></td>
            </tr>
            <%
        for(Object item : webBean.arrayList("list"))
        {
            UserInfoDao dao = (UserInfoDao)item;
        %>
              <tr class="list_tr">
                <td class="list_text">
                  <%=WebUtil.htmlEscape(dao.getLastName())%>・<%=WebUtil.htmlEscape(dao.getMiddleName())%>・<%=WebUtil.htmlEscape(dao.getFirstName())%>
                </td>
                <td class="list_text"><%=WebUtil.htmlEscape(dao.getLastName())%></td>
                <td class="list_text">
                  <input type="button" value="編集" onclick="go_detail('edit','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                  <input type="button" value="削除" onclick="go_detail('deletef','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                </td>
              </tr>
              <%}%>
          </table>
          <%}%>
      </div>
    </div>
  </form>
</body>
</html>

