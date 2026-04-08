<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.FileDao"%>
<%@ page import="jp.swell.dao.UserFileDao"%>
<%@ page import="jp.swell.dao.AnnouncementDao"%>
<%@ page import="jp.swell.user.UserLoginInfo"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.text.SimpleDateFormat"%>
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
<link rel="shortcut icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript"
	src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>おしらせ</title>
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
	margin-bottom: 5px; /* 不要な余白を排除 */
	text-align: center; /* テキスト中央寄せ */
}

h1 a {
	font-size: 1.5em;
	color: white; /* リンクの文字色を白に */
	text-decoration: none; /* 下線を削除 */
	font-weight: normal;
}

h1 a:hover {
	color: #4baea8; /* ホバー時に下線を表示する場合 */
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
}

/* ボタンの共通スタイル */
input[type="button"] {
	border-radius: 10px; /* 角を丸くする */
	color: #fff; /* 文字色 */
	cursor: pointer; /* カーソルをポインタにする */
	background: #90a0b0; /* デフォルトの背景色 */
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

/* ホバー時のスタイル */
input[type="button"]:hover {
	background-color: #4baea8; /* ホバー時の背景色 */
}

div {
	display: block;
	unicode-bidi: isolate;
}

body {
	font-family: 'Arial', sans-serif;
	background-color: #f9f9f9;
	margin: 0;
	padding: 20px;
}

table {
	width: 100%;
	margin: 0px;
	padding: 0px;
	border-collapse: collapse;
	border-spacing: 0px;
	border: 0px #808080 solid;
}

td {
	height: 1.8em;
	border-color: #404040;
	border-collapse: collapse;
}

.search_label {
	background: #00bcd4;
	color: #fff;
	text-align: center;
}

.search_text, .search_line, .list_btn {
	text-align: center;
}

.search_text input {
	text-align: left;
	border-radius: 5px;
}

.search_line input {
	text-align: right;
	border-radius: 5px;
}

.pagenation, .select_table {
	margin-bottom: 10px;
}

.select_table td {
	border-collapse: collapse;
	border: 1px #a0a0a0 solid;
	padding: 2px;
}

.list_label {
	background: #00bcd4;
	color: #fff;
	text-align: center;
}

.list_label a {
	color: #fff;
	text-decoration: none;
}

.list_table td {
	border-collapse: collapse;
	border: 1px #a0a0a0 solid;
	padding: 2px;
}

#pageNo {
	text-align: center;
	border-radius: 5px;
}

.list_tr:nth-child(odd) {
	background: #efefef;
}

.select-file {
	height: 95%;
	float: left;
	margin-right: 10px;
}

footer {
	width: 100%;
	background-color: #f1f1f1;
}

th {
	background-color: #00bcd4;
	padding: 10px;
	color: #fff;
	text-align: center;
}

ander-btn {
	margin: auto;
}

div {
	
}

.title {
	position: relative;
	background: #FFFFFF; /* ヘッダーの背景色 */
	width: 100%; /* 幅を画面いっぱいに */
	margin-bottom: 5px; /* 不要な余白を排除 */
	text-align: center; /* テキスト中央寄せ */
	font-size: 32px;
	font-weight: bold;
}

.info {
	display: flex;
}

.day {
	
}

.name {
	margin-left: 8px;
}

.news {
	background: #FFFFFF;
	text-align: center; /* テキスト中央寄せ */
	font-size: 20px;
	width: 100%; /* 幅を300pxに */
	height: 800px; /* 高さを200pxに */
	margin: auto;
}
</style>
<script type="text/javascript">
	function go_submit(action_cmd)){
		document.getElementById('main_form').action = 'FileList.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_form').submit();
	}
	function go_menu(action_cmd) {
		document.getElementById('main_form').action = 'UserMenu.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_form').submit();
	}
</script>
</head>
<body>
	<%
	WebBean bean = (WebBean) request.getAttribute("webBean");
	%>
	<div class="container">
		<div class="new-btn">
			<input type="button" value="　戻る　" onclick="history.back()" />
		</div>
		<header>
		<h1>
			<a href="javascript:void(0)" value="" onclick="go_menu('top')">おしらせ</a>
		</h1>
		</header>

		<form id="main_form" method="post" action="">

			<input type="hidden" name="form_name" id="form_name" value="FileList" />
			<input type="hidden" name="action_cmd" id="action_cmd" value="" />
			<input type="hidden" name="request_cmd" id="request_cmd" value="" />
			<input type="hidden" name="main_key" id="main_key" value="" />
			<input type="hidden" name="sort_key_old" id="sort_key_old"
				value="<%=webBean.txt("sort_key_old")%>" />
			<input type="hidden" name="sort_key" id="sort_key" value="" />
			<input type="hidden" name="sort_order" id="sort_order"
				value="<%=webBean.txt("sort_order")%>" />
			<input type="hidden" name="search_info" id="search_info"
				value="<%=webBean.txt("search_info")%>" />
			<input type="hidden" name="user_info_id" id="user_info_id"
				value="<%=webBean.txt("user_info_id")%>" />
			<input type="hidden" name="title" id="title"
				value="<%=webBean.txt("title")%>" />
			<input type="hidden" name="anno_id" id="anno_id"
				value="<%=webBean.txt("anno_id")%>" />

			<%
			AnnouncementDao anno = new AnnouncementDao();
			List<AnnouncementDao> List = anno.getAllNews();
			AnnouncementDao select = null;
			for (AnnouncementDao Info : List) {
				if (Info.getAnnoId().equals(webBean.txt("anno_id"))) {
					select = Info;
					break;
				}
			}
			// 表示形式を指定（例：2023/10/25 15:30）
			String Date = select.getUpdateDate();
			String formatDate = (Date != null && !Date.isEmpty()) ? Date : "データを取得していない";
			%>

			<table border="1">
				<div class="title"><%=WebUtil.htmlEscape(select.getTitle())%></div>
				<div class="info">
					<div class="day"><%=WebUtil.htmlEscape(formatDate)%></div>
					<div class="name"><%=WebUtil.htmlEscape(select.getLastName())%></div>
				</div>
				<div class="news"><%=WebUtil.htmlEscape(select.getText())%><div>
			</table>
			<div class="ander-btn">
				<input type="button" value="　戻る　" onclick="history.back()" />
			</div>
	</div>
	</form>
	</div>
</body>