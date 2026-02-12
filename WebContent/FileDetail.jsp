<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="keywords" content="">
		<meta name="description" content="">
			<meta charset="UTF-8">
				<link type="text/css" href="jquery-ui/jquery-ui.css"
					rel="stylesheet" />
				<link rel="shortcut icon" href="images/favicon.ico"
					type="image/vnd.microsoft.icon" />
				<link rel="icon" href="images/favicon.ico"
					type="image/vnd.microsoft.icon" />
				<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
				<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
				<script type="text/javascript"
					src="jquery.watermark/jquery.watermark.js"></script>
				<script type="text/javascript" src="js/common.js"></script>
				<script type="text/javascript" src="js/flatpickr.min.js"></script>
				<title>アップロード画面</title>
				<style>
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
	justify-content: center; /* 横方向で中央に配置 */
	align-items: center; /* 縦方向で中央に配置 */
}

.file__form--name {
	width: 60%;
}

/* ボタンの共通スタイル */
input[type="button"] {
	border-radius: 10px; /* 角を丸くする */
	color: #fff; /* 文字色 */
	cursor: pointer; /* カーソルをポインタにする */
	background: #90a0b0; /* デフォルトの背景色 */
}

/* ホバー時のスタイル */
input[type="button"]:hover {
	background-color: #4baea8; /* ホバー時の背景色 */
}

.new-btn {
	position: absolute;
	right: 10px; /* 右端に10pxの余白を取る */
	top: 5px;
}

/* .new-btnのスタイル */
.new-btn input {
	background: #fff; /* 背景色を白に */
	color: #000; /* 文字色を黒に */
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
}

.button input[type="button"]:hover {
	background-color: #4baea8; /* ホバー時の背景色 */
}

input[type="text"] {
	border: 1px solid #696969;
	border-radius: 4px;
	padding: 5px;
	background: #FFF;
}

.errors, .messages {
	margin-bottom: 20px;
	font-size: 16px
}

input::placeholder {
	color: rgba(0, 0, 0, 0.3);
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

td.input-text {
	text-align: left;
	background: #fff;
}

table {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid #ddd;
}

td, th {
	border: 1px solid #ddd;
	padding: 5px;
}

.label {
	font-weight: 600;
}

span {
	color: #f00;
	font-size: 16px;
}

span.error {
	color: #f00;
	background-color: transparent;
}

input.error {
	color: #FF0000;
	background-color: #FFCCCC;
	border: 1px solid #FF0000;
}

label.error {
	color: #FF0000;
}
</style>
				<script type="text/javascript">
const ctx = '<%=request.getContextPath()%>';

function go_submit(action_cmd) {
  document.getElementById('main_form').action = 'FileDetail.do';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
}

function go_upload(action_cmd) {
  document.getElementById('main_form').action = '';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
}

// サブ画面処理
function openUserWindow(action_cmd) {
  // コントローラー設定
   const form = document.createElement('form');
  form.method = 'POST';
  form.action = ctx + '/FileDetail.do';
  form.target = 'FileUserList';

  // form_name設定
  const formNameInput = document.createElement('input');
  formNameInput.type = 'hidden';
  formNameInput.name = 'form_name';
  formNameInput.value = 'FileDetail';
  form.appendChild(formNameInput);

  // アクションコマンド設定
  const actionCmdInput = document.createElement('input');
  actionCmdInput.type = 'hidden';
  actionCmdInput.name = 'action_cmd';
  actionCmdInput.value = action_cmd;
  form.appendChild(actionCmdInput);

  document.body.appendChild(form);

  // サブ画面表示処理
  window.open('', 'FileUserList', 'width=600,height=400');
  form.submit();

  document.body.removeChild(form);
}

function receiveSelectedUsers(users, type) {
  let selectedUsersDiv;
  let userIds = [];

  if (type === 'source') {
    selectedUsersDiv = document.getElementById('selected_source_users');
    selectedUsersDiv.innerHTML = ''; // 既存のユーザーをクリア
}

  if (type === 'sub') {
    selectedUsersDiv = document.getElementById('selected_destination_users');
    selectedUsersDiv.innerHTML = ''; // 既存のユーザーをクリア
}

  users.forEach((user) => {
    const userDiv = document.createElement('div');
    userDiv.textContent = user.name;
    userDiv.classList.add('user-item'); // クラスを追加
    selectedUsersDiv.appendChild(userDiv);
    userIds.push(user.id);
});

// ユーザーIDを隠しフィールドに設定
  if (type === 'source') {
    document.getElementById('user_info_id').value = userIds.join(',');
} else {
    document.getElementById('destination_user_info_id').value = userIds.join(',');
  }
}
</script>
</head>
<body>
	<div class="container">
		<div class="new-btn">
			<input type="button" value="　戻る　" onclick="history.back()" />
		</div>
		<header>
		<h1>ファイル登録ページ</h1>
		</header>
		<form method="post" id="main_form"
			action="/kenshuProject/WebContent/upload"
			enctype="multipart/form-data">

			<input type="hidden" name="form_name" id="form_name"
				value="FileDetail" />
			<input type="hidden" name="action_cmd" id="action_cmd" value="" />
			<input type="hidden" name="list" id="list"
				value="<%=webBean.txt("list")%>" />
			<input type="hidden" name="name" id="name"
				value="<%=webBean.txt("name")%>" />
			<input type="hidden" name="destination_user_info_id"
				id="destination_user_info_id">

				<div class="style_head3 messages"><%=webBean.dispMessages()%></div>
				<div class="errors"><%=webBean.dispErrorMessages()%></div> <!-- ファイルアップロードフォーム -->
				<div class="left">
					<table class="file__form--name">
						<tr>
							<td class="style_head3 style_head_size" style="width: 40%">ファイル名</td>
							<td class="input-text" style="width: 60%"><input type="text"
								name="input_name" id="input_name"
								value="<%=webBean.txt("file_name")%>" class="ime_disabled"
								placeholder="入力" /></td>
						</tr>
						<tr>
							<td class="style_head3 style_head_size" style="width: 40%">ファイルリンク</td>
							<td class="input-text" style="width: 60%"><input type="file"
								name="file" id="file" class="ime_disabled" /></td>
						</tr>
						<!-- 送信元ユーザー選択 -->
						<tr>
							<td class="style_head3 style_head_size" style="width: 40%">送信元ユーザー
							</td>
							<td class="input-text" style="width: 60%">
								<!-- ログインユーザー名を直接表示 -->
								<div class="user-item">
									<%=WebUtil.htmlEscape(webBean.value("loginUserName"))%>
								</div> <!-- 隠しフィールドでログインユーザーのIDを送信 --> <input type="hidden"
								name="user_info_id"
								value="<%=WebUtil.htmlEscape(webBean.value("loginUserId"))%>" />
								<span id="error_user_info_id" class="error"><%=webBean.dispError("user_info_id")%></span>
							</td>
						</tr>
						<!-- 送信先ユーザー選択 -->
						<tr>
							<td class="style_head3 style_head_size" style="width: 40%">送り先ユーザー
								<input type="button" value="選択" name="destination_user_info_id"
								value="<%=webBean.txt("destination_user_info_id")%>"
								onclick="openUserWindow('sub')" />
							</td>
							<td class="input-text" style="width: 60%">
								<div id="selected_destination_users" class="user_list"></div> <span
								id="error_destination_user_info_id" class="error"><%=webBean.dispError("destination_user_info_id")%></span>
							</td>
						</tr>
					</table>
				</div>
				<div class="button">
					<input type="button" onclick="go_upload('upload')" value="登録" />
				</div>
		</form>
	</div>
</body>h
</html>
