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
    // ▼▼▼ モード判定ロジック ▼▼▼
    String reqName = webBean.txt("request_name");
    
    // "削除" の時だけ true になるフラグ
    boolean isDelete = "削除".equals(reqName);

    // タイトル文字
    String pageTitle = "ユーザー情報" + reqName + "確認";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title><%= pageTitle %></title>
<style type="text/css">
body { font-family: 'Arial', sans-serif; background-color: #f9f9f9; margin: 0; padding: 10px; }
header { position: relative; background: #00bcd4; width: 100%; height: 70px; margin: 15px auto; display: flex; justify-content: center; align-items: center; }
h1 { font-size: 50px; color: white; text-decoration: none; font-weight: normal; }
.container { position: relative; background-color: #f0f0f0; border: 1px solid #ddd; border-radius: 5px; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); width: 90%; margin: 20px auto; }
.left { margin-bottom: 20px; text-align: center; display: flex; justify-content: center; align-items: center; }
.input-table{ width: 60%; }
.button { display: flex; justify-content: center; align-items: center; }
.button input[type="button"] { padding: 0px 50px; font-size: 24px; border: 2px solid #fff; background-color: #00bcd4; color: #fff; cursor: pointer; border-radius: 10px; }
.button input[type="button"]:hover { background-color: #4baea8; }
.new-btn { position: absolute; right: 10px; top: 5px; }
.new-btn input { background: #fff; color: #000; border-radius: 10px; cursor: pointer; }
.style_head3 { padding-left: 10px; font-size: 18px; font-weight: bold; text-align: center; }
.style_head_size { height: 30px; vertical-align: middle; display: table-cell; background: #00bcd4; color: #fff; }
td.input-text { text-align: left; background: #fff; padding: 10px; }
table { border-collapse: collapse; width: 100%; border: 1px solid #ddd; }
td, th { border: 1px solid #ddd; padding: 5px; }
</style>
<script type="text/javascript">
  // 戻るボタン用
  function go_list(action_cmd) {
    history.back();
  }

  // 確定ボタン用
  function go_submit(action_cmd, request_cmd) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_form').submit();
  }
</script>
</head>
<body>
  <div class="container">
    <div class="new-btn">
      <input type="button" value="　戻る　" onclick="go_list('return')" />
    </div>
    <header>
        <h1> <%= pageTitle %>ページ </h1>
    </header>

    <form method="post" id="main_form" action="">
        <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_Confirm" />
        <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
        <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
        <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
        <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
        <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
        <input type="hidden" name="select_info" id="select_info" value="<%=webBean.txt("select_info")%>" />

        <input type="hidden" name="user_info_id" value="<%=webBean.txt("user_info_id")%>">
        <input type="hidden" name="password" value="<%=webBean.txt("password")%>">
        
        <input type="hidden" name="last_name" value="<%=webBean.txt("last_name")%>">
        <input type="hidden" name="first_name" value="<%=webBean.txt("first_name")%>">
        <input type="hidden" name="last_name_kana" value="<%=webBean.txt("last_name_kana")%>">
        <input type="hidden" name="first_name_kana" value="<%=webBean.txt("first_name_kana")%>">
        <input type="hidden" name="middle_name" value="<%=webBean.txt("middle_name")%>">
        <input type="hidden" name="middle_name_kana" value="<%=webBean.txt("middle_name_kana")%>">
        <input type="hidden" name="maiden_name" value="<%=webBean.txt("maiden_name")%>">
        <input type="hidden" name="maiden_name_kana" value="<%=webBean.txt("maiden_name_kana")%>">
        <input type="hidden" name="insert_user_id" value="<%=webBean.txt("insert_user_id")%>">
        <input type="hidden" name="memail" value="<%=webBean.txt("memail")%>">
        <input type="hidden" name="memail_1" value="<%=webBean.txt("memail")%>">
        <input type="hidden" name="admin" value="<%=webBean.txt("admin")%>">
        <input type="hidden" name="leave_date" value="<%=webBean.txt("leave_date")%>">
        <div class="style_head3 messages"><%=webBean.dispMessages()%></div>
        <div class="errors"><%=webBean.dispErrorMessages()%></div>

        <div class="left">
          <table class="input-table">
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">氏名</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("last_name")%> <%=webBean.txt("first_name")%>
              </td>
            </tr>

            <% if (!isDelete) { %>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">氏名よみ</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("last_name_kana")%> <%=webBean.txt("first_name_kana")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">ミドルネーム</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("middle_name")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">ミドルネームよみ</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("middle_name_kana")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">旧姓</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("maiden_name")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">旧姓よみ</td>
              <td class="input-text" style="width: 70%">
                <%=webBean.txt("maiden_name_kana")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">任意ＩＤ</td>
              <td class=" input-text" style="width: 70%">
                <%=webBean.txt("insert_user_id")%>
              </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">メールアドレス</td>
                <td class="email-container input-text" style="width: 70%">
                  <%=webBean.txt("memail")%>
                </td>
            </tr>
            <tr>
              <td class="style_head3 style_head_size" style="width: 30%">ユーザー区分</td>
              <td class="input-text" style="width: 70%">
                <input type="radio" value="1" disabled="disabled" <%= webBean.txt("admin").equals("1") ? "checked" : "" %> />
                <label>管理者</label>
                &nbsp;&nbsp;
                <input type="radio" value="0" disabled="disabled" <%= webBean.txt("admin").equals("0") ? "checked" : "" %> />
                <label>一般</label>
              </td>
            </tr>
            <% } %>
            </table>
        </div>
        
      <div class="button">
        <input type="button" id="submit_btn" 
               value="<%=webBean.txt("request_name")%>する" 
               onclick="go_submit('go_next','<%=webBean.txt("request_cmd")%>')" /> 
      </div>
    </form>
  </div>
</body>
</html>