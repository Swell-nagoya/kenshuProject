<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.FileDownloadsDao"%>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>ファイルダウンロード履歴一覧</title>
<style>
body
{
  margin:0px;
  padding : 0px;
  border: 0px;
  font-size: 80%;
  line-height: 1.2;
  letter-spacing: 0;
  font-family: "Lucida Grande", "Lucida Sans Unicode", "Hiragino Kaku Gothic Pro", "ヒラギノ角ゴ Pro W3", "メイリオ", Meiryo, "ＭＳ Ｐゴシック", Helvetica, Arial, Verdana, sans-serif;
  height: 100%;
  width : 100%;
  background: #f0f0f0;
}

.container {
  background-color: white;
  width: 300px;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin: 0 auto;
}

table
{
  width : 100%;
  margin : 0px;
  padding : 0px;
  border-collapse : collapse;
  border-spacing: 0px;
  border: 0px #808080 solid;
}

h1 {
    color: #333;
    border-bottom: 4px dotted #800080;
    padding-bottom: 10px;
}

td {
    padding: 10px;
    border-bottom: 1px solid #ddd;
    text-align: center;
}
.pagenation, .select_table {
    margin-bottom: 10px;
}
.pagenation {
    text-align: center;
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
    margin: 10px 10px;
    text-decoration: none;
    display: inline-block;
}

/*.file_preview-area*/

.file_preview-area img {
  width: 100%;
  height: auto;

}

</style>
<script>
function submitSelection() {
    const selectedUsers = [];
    // チェックされたチェックボックスを全て取得
    document.querySelectorAll('input[name="user_info_id"]:checked').forEach((checkbox) => {
        let name = checkbox.getAttribute('data-user-name');
        // ミドルネームがない場合、不要な&nbsp;を除去
        name = name.replace(/&nbsp;/g, ' ').replace(/\s+/g, ' ').trim();
        // ユーザーIDと名前を追加
        selectedUsers.push({
            id: checkbox.value,
            name: name
        });
    });
    // 親ウィンドウに選択したユーザー情報を送信
    window.opener.receiveSelectedUsers(selectedUsers);
    window.close();
}

//ウィンドウが読み込まれた時に実行される処理
window.onload = function() {
    // サーバーから受け取った選択されたユーザーIDの生データを取得
    const rawSelectedUserIds = '<%=webBean.txt("selected_user_ids")%>';
    // 受け取ったユーザーIDをカンマ区切りで分割して配列に変換
    const selectedUserIds = rawSelectedUserIds ? rawSelectedUserIds.split(',') : [];

    // チェックボックスを選択するロジック
    const checkboxes = document.querySelectorAll('input[name="user_info_id"]');
    // 選択されたユーザーIDがチェックボックスの値と一致する場合、チェックを入れる
    checkboxes.forEach(checkbox => {
        if (selectedUserIds.includes(checkbox.value)) {
            checkbox.checked = true; // 値が一致する場合、チェックを入れる
        }
    });
};



function go_detail(action_cmd) {
	// フォームの送信先を親ウィンドウの名前に設定
	if (window.opener && !window.opener.closed) {
	 if (!window.opener.name) {
	    window.opener.name = "parentWindow_" + Date.now();
	  }
	    
	  document.getElementById('main_form').target = window.opener.name;
	}

	document.getElementById('main_form').action = 'UserMenu.do';
	document.getElementById('action_cmd').value = action_cmd;
	document.getElementById('main_form').submit();
	window.close();
}
function go_submit(action_cmd)
{
  document.getElementById('main_form').action='FileSeparateWindow.do';
  document.getElementById('action_cmd').value=action_cmd;
  document.getElementById('main_form').submit();
}
</script>
</head>
<body>
<div class="container">
  <h1>ファイルダウンロード一覧</h1>   
  <form id="main_form" method="post" action="">
     <%
      if (webBean.arrayList("list").size() > 0) {
     %>
    <div class="pagenation">
      <input type="text" name="pageNo" id="pageNo" maxlength="3" size='1' value="<%=webBean.txt("pageNo")%>" class="right ime_disabled" />  /
      <%=webBean.html("maxPageNo")%> ページ〚全
      <%=webBean.html("recordCount")%>件〛<br/>
      <%if(!"1".equals(webBean.value("pageNo"))){%> <input type="button" value="<--前の<%=webBean.html("lineCount")%>件" onclick="go_submit('prior')" />
      <%}else{%>
      <%}%>
      <input type="button" value="ページ表示" onclick="go_submit('jump')" />
      <%if(!webBean.value("pageNo").equals(webBean.value("maxPageNo"))){%> <input type="button" value="次の<%=webBean.html("lineCount")%>件-->" onclick="go_submit('next')" />
      <%}else{%>
      <%}%>
     </div>
      <%}%>
      
      
     <input type="hidden" id="form_name" name="form_name" value="FileDownloads"/>
     <input type="hidden" id="action_cmd" name="action_cmd" value=""/>
     <input type="hidden" id="reservation_date" name="reservation_date" value=""/>
     <input type="hidden" id="main_key" name="main_key" value="<%=webBean.txt("main_key")%>" />
     <input type="hidden" id="file_downloads_id_show_all" name="file_downloads_id_show_all" value="<%=webBean.txt("file_downloads_id_show_all")%>" />
     <input type="hidden" id="previous_page" name="previous_page" value="RoomYoyakuList" />
     
     <table border="1">
       <tr>
          <th>ID</th>
          <th>ユーザーID</th>
          <th>ファイルID</th>
          <th>ダウンロード日時</th>
       </tr>
       <%
        for(Object item : webBean.arrayList("list"))
        {
          FileDownloadsDao dao = (FileDownloadsDao)item;
       %>
       <tr class="list_tr">
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getFileDownloadsId())%>
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getUserInfoId())%>
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getFileId())%>
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getDownloadsDate())%>
            </td>
       </tr>
       <%}%>
    </table>
    <div class="buttons">
      <input type="button" value="閉じる" onclick="window.close()" class="btn btn-close">
    </div>
  </form>
</div>
</body>
</html>