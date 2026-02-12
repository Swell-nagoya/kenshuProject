<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<link rel="shortcut icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery-3.6.4.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript"
	src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>ユーザー選択</title>
<style>
body {
	margin: 0px;
	padding: 0px;
	border: 0px;
	font-size: 80%;
	line-height: 1.2;
	letter-spacing: 0;
	font-family: "Lucida Grande", "Lucida Sans Unicode",
		"Hiragino Kaku Gothic Pro", "ヒラギノ角ゴ Pro W3", "メイリオ", Meiryo,
		"ＭＳ Ｐゴシック", Helvetica, Arial, Verdana, sans-serif;
	height: 100%;
	width: 100%;
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

table {
	width: 100%;
	margin: 0px;
	padding: 0px;
	border-collapse: collapse;
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

.btn-primary {
	background-color: #4CAF50;
	color: white;
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
</script>
</head>
<body>
	<div class="container">
		<form id="user_select_form">
			<h1>予約者選択</h1>
			<table>
				<%
				// データベースの users が空でないかの確認
				if (webBean.arrayList("list") != null && !webBean.arrayList("list").isEmpty()) {
				%>
				<%
				// ユーザー情報を取るためのループ処理
				for (Object item : webBean.arrayList("list")) {
					UserInfoDao user = (UserInfoDao) item;

					String fullName = WebUtil.htmlEscape(user.getLastName()) +
					" " +
					WebUtil.htmlEscape(user.getMiddleName()) +
					" " +
					WebUtil.htmlEscape(user.getFirstName());
				%>
				<tr>
					<td style="text-align: left;"><input type="checkbox"
						name="user_info_id"
						value="<%=WebUtil.htmlEscape(user.getUserInfoId())%>"
						data-user-name="<%=fullName%>" /> <%=fullName%></td>
				</tr>
				<%
				}
				} else { // ユーザー情報がない場合
				%>
				<tr>
					<td>ユーザー情報がありません</td>
				</tr>
				<%
				}
				%>
			</table>
			<div class="buttons">
				<input type="button" value="選択" onclick="submitSelection()"
					class="btn btn-primary">
			</div>
		</form>
	</div>
</body>
</html>