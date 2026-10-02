<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
  scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>プロフィール</title>
<style type="text/css">
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

.input-table{
  width: 60%;
}

/* ホバー時のスタイル */
.new-btn hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.new-btn {
  position: absolute;
  right: 10px; /* 右端に10pxの余白を取る */
  top: 5px;   
}

/* .new-btnのスタイル */
.new-btn input {
  border-radius: 10px;
  background: #fff; /* 背景色を白に */
  color: #000; /* 文字色を黒に */
   cursor: pointer; 
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
  border-radius: 10px;
   cursor: pointer; 
}

.button input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.messages {
  font-size: 30px;
  margin-bottom: 20px;
}

.style_head3 {
  padding-left: 10px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  
}

.style_head_size {
  height: 30px;
  vertical-align: middle;
  display: table-cell;
  background: #00bcd4;
  color: #fff;
}

table {
    width: 100%;
    border: 1px solid #ddd;
  }

  td, th {
    border: 1px solid #ddd; 
    padding: 5px;
  }

.input-text {
  font-size: 20px;
  font-weight: bold;
  background: #fff;
}

}
</style>
<script type="text/javascript">
  
  function go_submit(action_cmd, request_cmd) {
      document.getElementById('main_form').action = 'UserInfoDetail.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_form').submit();
  }

  function go_mail(action_cmd, request_cmd,main_key) {
      document.getElementById('main_form').action = 'SendPassMail.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_key').value=main_key;
      document.getElementById('main_form').submit();
  }

  function go_list(action_cmd, request_cmd,main_key) {
      document.getElementById('main_form').action = 'UserInfoDetail.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_key').value=main_key;
      document.getElementById('main_form').submit();
  }
  
  function togglePassword(id) {
    var input = document.getElementById(id);
    var type = input.getAttribute('type');
    input.setAttribute('type', type === 'password' ? 'text' : 'password');
  }

</script>
</head>
<body>
   <%
     String maidenName = webBean.txt("maiden_name").trim();
     String insertUserId = webBean.txt("insert_user_id").trim();
     String val = webBean.txt("request_name");
     String actionType = webBean.txt("request_cmd");
     String actionBtn =  val.equals("メール送信") ? "go_mail" : "go_submit";
     String header =  val.equals("登録") ? "登録確定" : val.equals("修正") ? "情報編集確定" : val.equals("確定") ? "情報削除確定" : val.equals("一括修正") ? "一括編集確定" : val.equals("一括削除") ? "一括削除確定" : val.equals("メール送信") ? "情報確認" : "確認";
     boolean isBulkUpdate = "bulk_update".equals(actionType);
     boolean isBulkDelete = "bulk_delete".equals(actionType);
     int bulkIndex = 0;
     int bulkCount = 0;
     try { bulkIndex = Integer.parseInt(webBean.txt("bulk_index")); } catch (Exception e) { bulkIndex = 0; }
     try { bulkCount = Integer.parseInt(webBean.txt("bulk_count")); } catch (Exception e) { bulkCount = 0; }
   %>
  <div class="container">
    <div class="new-btn">
      <input type="button" value=" 戻る " onclick="go_list('return','<%=actionType%>','<%=webBean.txt("user_info_id")%>')" />
    </div>
<header>
    <h1>ユーザー<%= header %>ページ</h1>
</header>
   
 　　　 <form method="post" id="main_form" action="">

        <input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_3" /> 
        <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
        <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
        <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
        <input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
        <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
        <input type="hidden" name="select_info" id="select_info" value="<%=webBean.txt("select_info")%>" />
        <input type="hidden" name="leave_date" id="leave_date" value="<%=webBean.txt("leave_date")%>" />
        <input type="hidden" name="select_user_info_ids" id="select_user_info_ids" value="<%=webBean.txt("select_user_info_ids")%>" />
        <input type="hidden" name="bulk_index" id="bulk_index" value="<%=webBean.txt("bulk_index")%>" />
        <input type="hidden" name="bulk_count" id="bulk_count" value="<%=webBean.txt("bulk_count")%>" />
        <input type="hidden" name="bulk_input_info" id="bulk_input_info" value="<%=webBean.txt("bulk_input_info")%>" />
        <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt("search_info")%>" />

        <div class="style_head3 messages"><%=webBean.dispMessages()%></div>

        <div class="left">
          <table class="input-table">
          <%
            String formatLeaveDate = "";
            String leaveDate = WebUtil.htmlEscape(webBean.txt("leave_date"));
            if (leaveDate != null && leaveDate.length() >= 8) {
              formatLeaveDate = leaveDate.substring(0, 4) + "/" + leaveDate.substring(4, 6) + "/" + leaveDate.substring(6, 8);
            }
          %>
          <% if (isBulkUpdate || isBulkDelete) { %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">表示中</td>
            <td class="input-text" style="width: 70%"><%=bulkIndex + 1%> / <%=bulkCount%> 件</td>
          </tr>
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
          <% if (!webBean.txt("maiden_name").trim().isEmpty()) { %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 旧姓 </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("maiden_name")%> </td>
          </tr>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> 旧姓よみ </td>
            <td class="input-text" style="width: 70%"> <%=webBean.txt("maiden_name_kana")%> </td>
          </tr>
          <% } %>
          <% if (!webBean.txt("insert_user_id").trim().isEmpty()) { %>
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
            <td class="input-text" style="width: 70%"> <%= webBean.txt("admin").equals("admin") || webBean.txt("admin").equals("1") ? "管理者" : "一般" %> </td>
          </tr>
          <% if (isBulkDelete) { %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%">退職予定日</td>
            <td class="input-text" style="width: 70%"><%= formatLeaveDate %></td>
          </tr>
          <% } %>
          <% } else { %>
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
          <% if (!insertUserId.isEmpty()) { %>
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
            <td class="input-text" style="width: 70%"> <%= webBean.txt("admin").equals("admin") || webBean.txt("admin").equals("1") ? "管理者" : "一般" %> </td>
          </tr>
          <% if ("ins".equals(actionType)) { %>
          <tr>
            <td class="style_head3 style_head_size" style="width: 30%"> パスワード </td>
            <td class="input-text" style="width: 70%"> <%= webBean.txt("password") %> </td>
          </tr>
          <% } %>
          <% if ("delete".equals(actionType)) { %>
          <tr>
            <td class="style_head3 style_head_size"> 退職予定日 </td>
            <td class="input-text"><%= formatLeaveDate %> </td>
          </tr>
          <% } %>
          <% } %>
          </table>
        </div>
        <div class="button">
          <% if (isBulkUpdate || isBulkDelete) { %>
            <% if (bulkIndex > 0) { %>
              <input type="button" value="前へ" onclick="go_submit('bulk_preview_prior','<%=actionType%>')" />
            <% } %>
            <% if (bulkIndex < bulkCount - 1) { %>
              <input type="button" value="次へ" onclick="go_submit('bulk_preview_next','<%=actionType%>')" />
            <% } %>
            <input type="button" id="submitButton" value="確定する" onclick="go_submit('go_next','<%=actionType%>')" />
          <% } else { %>
            <input type="button" id="submitButton" value="<%=val%>" onclick="<%=actionBtn%>('go_next','<%=actionType%>','<%=webBean.txt("user_info_id")%>')" />
          <% } %>
        </div>
    </form>
  </div>
</body>
</html>