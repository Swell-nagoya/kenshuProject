<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="UTF-8">
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>権限一括更新画面</title>
<style>
body {
	font-family: 'Arial', sans-serif;
	background-color: #f9f9f9;
	margin: 0;
	padding: 10px;
}

header {
	position: relative;
	background: #00bcd4;
	width: 100%;
	height: 70px;
	margin: 15px auto;
	display: flex;
	justify-content: center;
	align-items: center;
}

h1 {
	font-size: 50px;
	color: white;
	text-decoration: none;
	font-weight: normal;
}

.container {
	position: relative;
	background-color: #f0f0f0;
	border: 1px solid #ddd;
	border-radius: 5px;
	padding: 20px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 90%;
	margin: 20px auto;
}

/* ボタンの共通スタイル */
input[type="button"] {
	border-radius: 10px;
	color: #fff;
	cursor: pointer;
	background: #90a0b0;
}

input[type="button"]:hover {
	background-color: #4baea8;
}

.new-btn {
	position: absolute;
	right: 10px;
	top: 5px;
}

.new-btn input {
	background: #fff;
	color: #000;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
	text-align: center;
}

th {
	background: #00bcd4;
	color: #fff;
	text-align: center;
}

td {
	padding: 10px;
	border-bottom: 1px solid #ddd;
}
.errors {
	color: #f00;
	text-align:center;
}
</style>
<script type="text/javascript">
function go_submit(action_cmd) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}
function go_list(action_cmd) {
    document.getElementById('main_form').action = 'ViewUserList.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}

function renderTable() {
	const selectedUsers = document.getElementById('selected_users');
	let usersArray = selectedUsers.value ? JSON.parse(selectedUsers.value) : [];
	const tableBody = document.getElementById('selected_users_table_body');
	tableBody.innerHTML = '';

	if (usersArray.length === 0) {
		tableBody.innerHTML = '<tr><td colspan="2" style="text-align: center;">選択されたユーザーはいません</td></tr>';
		return;
	}

	usersArray.forEach(user => {
		const id = user[0];
		const name = user[1];

		const tr = document.createElement('tr');
		const tdId = document.createElement('td');
		tdId.textContent = id;
		tr.appendChild(tdId);
		const tdName = document.createElement('td');
		tdName.textContent = name;
		tr.appendChild(tdName);
		tableBody.appendChild(tr);
	});
}

function changeRole() {
	const selectedUsers = document.getElementById('selected_users');
	let usersArray = selectedUsers.value ? JSON.parse(selectedUsers.value) : [];
	
	if (usersArray.length === 0) {
		alert('対象のユーザーがいません。');
		return;
	}
	
	const roleSelect = document.getElementById('select_target_role');
	const roleText = roleSelect.options[roleSelect.selectedIndex].text;
	
	if (confirm(usersArray.length + '名のユーザーを「' + roleText + '」に変更します。\nよろしいですか？')) {
		
		let targetRole = document.getElementById('target_role');
		targetRole.value = roleSelect.value;
		
		go_submit('update'); 
	}
}

document.addEventListener('DOMContentLoaded', () => {
	renderTable();
});
</script>
</head>
<body>
	<div class="container">
		<div class="new-btn">
			<input type="button" onclick="go_list('return')" value=" 戻る " />
		</div>
		<header>
			<h1>権限一括変更ページ</h1>
		</header>

		<form method="post" id="main_form" action="">
			<input type="hidden" name="form_name" id="form_name" value="UserInfoDetail_4" />
			<input type="hidden" name="action_cmd" id="action_cmd" value="" />
			<input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
			<input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" />
			<input type="hidden" name="main_key" id="main_key" value="<%=webBean.txt("main_key")%>" />
			<input type="hidden" name="before_name" id="before_name" value="<%=webBean.txt("before_name")%>" />
			<input type="hidden" name="selected_users" id="selected_users" value="<%=webBean.txt("selected_users")%>" />
			<input type="hidden" name="target_role" id="target_role" value="" />
			<div class="style_head3 messages"><%=webBean.dispMessages()%></div>
			<div class="errors"><%=webBean.dispErrorMessages()%></div>
		</form>
		<div style="margin-bottom: 15px; text-align: center;">
			以下のユーザーのユーザー区分を
			<select id="select_target_role" name="select_target_role">
				<option value="1">管理者</option>
				<option value="0">一般</option>
			</select>
			に変更します。
			<button type="button" id="btn_change_role" onclick="changeRole()">変更</button>
		</div>
		<table id="selected_users_table">
			<thead>
				<tr>
					<th>ユーザーID</th>
					<th>ユーザー名</th>
				</tr>
			</thead>
			<tbody id="selected_users_table_body">
			</tbody>
		</table>
	</div>
</body>
</html>