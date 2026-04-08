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
</style>
</head>
<script type="text/javascript">
	
<%--検索条件入力でenterキーが押された場合の処理--%>
	jQuery(function($) {
		$(".select_table input").keydown(function(e) {
			if (e.which == 13) {
				go_submit('search');
			}
		});
		$(".page_table input").keydown(function(e) {
			if (e.which == 13) {
				go_submit('jump');
			}
		});
	});
<%--テーブルを一行ごとにいろを変える--%>
	$(document).ready(function() {
		$('table.list_table tr:even').addClass('even');
		$('table.list_table tr:odd').addClass('odd');
	});

	function go_sort_request(key) {
		document.getElementById('sort_key').value = key;
		document.getElementById('action_cmd').value = 'sort';
		document.getElementById('main_form').submit();
	}

	function go_menu(action_cmd) {
		document.getElementById('main_form').action = 'UserMenu.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_form').submit();
	}

	function go_detail_1(action_cmd, request_cmd, main_key) {
		document.getElementById('main_form').action = '';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('main_form').submit();
	}

	function go_delete(action_cmd, request_cmd, main_key) {
		document.getElementById('main_form').action = 'AnnouncementDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('main_form').submit();
	}

	function go_detail(action_cmd, request_cmd) {
		document.getElementById('main_form').action = 'AnnouncementDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		document.getElementById('main_form').submit();
	}
	function go_show(action_cmd, main_key) {
		document.getElementById('main_form').action = 'AnnouncementDetail.do';
		dodocument.getElementById('main_form').action = 'AnnouncementDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('main_form').submit();
		cument.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('main_form').submit();
	}
	function go_sort(action_cmd,main_key,sort_key) {
		document.getElementById('main_form').action = 'AnnouncementList.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('sort_key').value = sort_key;
		document.getElementById('main_form').submit();
	}
</script>
<body>
	<%
	String userAdmin = webBean.txt("main_key");
	String flg = webBean.txt("sort_key");
	%>
	<div class="container">
		<div class="new-btn">
			<input type="button" value="アナウンス登録"
				onclick="go_detail('go_next','ins')" /> <input type="button"
				value="　戻る　" onclick="history.back()" />
		</div>
		<header>
		<h1>おしらせ</h1>
		</header>
		<form id="main_form" method="post" action="">

			<input type="hidden" name="form_name" id="form_name"
				value="Announcement" />
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
			<input type="hidden" name="file_name" id="file_name"
				value="<%=webBean.txt("title")%>" />
			<input type="hidden" name="title" id="title"
				value="<%=webBean.txt("anno_id")%>" />
			<input type="hidden" name="flg" id="flg"
				value="<%=webBean.txt("flg")%>" />
			<div class="left">
				<div class="messages">
					<%=webBean.dispMessages()%>
				</div>
				<div class="errors">
					<%=webBean.dispErrorMessages()%>
				</div>
				<table class="select_table">
					<tr>
						<td class="search_label center" style="width: 50%">タイトル名</td>
						<td class="search_label center" style="width: 25%">表示件数</td>
						<td class="search_label center" style="width: 25%"></td>
					</tr>
					<tr>
						<td class="search_text center"><input type="text"
							name="list_search_file_name" id="list_search_file_name" size="30"
							maxlength="100"
							value="<%=webBean.txt("list_search_announce_title")%>"
							class="ime_active <%=webBean.dispErrorCSS("list_search_announce_title")%>"
							placeholder="検索" /> <%=webBean.dispError("list_search_announce_title")%>
						</td>
						<td class="search_line center"><input type="text"
							name="lineCount" id="lineCount" size="2" maxlength="5"
							value="<%=webBean.txt("lineCount")%>" class="righ t ime_disabled" />件
						</td>
						<td class="search_text center"><input type="button"
							value="検索" onclick="go_submit('search')" /> <input type="button"
							value="クリア" onclick="go_submit('clear')" /></td>
					</tr>
				</table>
				<div class="pagenation">
					<input type="text" name="pageNo" id="pageNo" maxlength="3" size='1'
						value="<%=webBean.txt("pageNo")%>" class="right ime_disabled" />
					/
					<%=webBean.html("maxPageNo")%>
					ページ〚全
					<%=webBean.html("recordCount")%>件〛<br />
					<%
					if (!"1".equals(webBean.value("pageNo"))) {
					%>
					<input type="button" value="<--前の<%=webBean.html("lineCount")%>件"
						onclick="go_submit('prior')" />
					<%
					} else {
					%>
					<%
					}
					%>
					<input type="button" value="ページ表示" onclick="go_submit('jump')" />
					<%
					if (!webBean.value("pageNo").equals(webBean.value("maxPageNo"))) {
					%>
					<input type="button" value="次の<%=webBean.html("lineCount")%>件-->"
						onclick="go_submit('next')" />
					<%
					} else {
					%>
					<%
					}
					%>
				</div>
				<table border="1">
					<tr>
						<th><a href="javascript:void(0);" onclick="go_sort('news','<%=webBean.txt(" user_info_id ")%>','<%=webBean.txt("sort_key")%>')">日付</a>
						</th>
						<th>対象者</th>
						<th>タイトル</th>
						<th>発信者</th>
						<th>削除</th>
					</tr>
					<!--表示形式を指定（例：2023/10/25 15:30）-->
					<%
					AnnouncementDao anno = new AnnouncementDao();
					List<AnnouncementDao> List = anno.getAllNews(flg);
					AnnouncementDao select = null;
					if (userAdmin.equals("0")) {
						for (AnnouncementDao Info : List) {
							if (Info.getAdmin().equals("0")) {
						select = Info;
						String Date = select.getUpdateDate();
						String formatDate = (Date != null && !Date.isEmpty()) ? Date : "データを取得していない";
						String admin = select.getAdmin();
						String anno_id = select.getAnnoId();
					%>
					<tr>
						<td><%=WebUtil.htmlEscape(formatDate)%></td>
						<%
						if (admin.equals("1") || admin.equals("admin")) {
						%>
						<td>管理者</td>
						<%
						} else {
						%>
						<td>全体</td>
						<%
						}
						%>
						<td><a href="javascript:void(0);"
							onclick="go_show('go_show','<%=WebUtil.txtEscape(select.getAnnoId())%>');"><%=WebUtil.htmlEscape(select.getTitle())%></a></td>
						<td><%=WebUtil.htmlEscape(select.getLastName())%></td>
						<td><input type="button" value="削除 "
							onclick="go_delete('go_next','delete','<%=WebUtil.txtEscape(select.getAnnoId())%>');" />
						</td>
					</tr>
					<%
					}
					}
					} else {
					for (AnnouncementDao Info : List) {
					String Date = Info.getUpdateDate();
					String formatDate = (Date != null && !Date.isEmpty()) ? Date : "データを取得していない";
					String admin = Info.getAdmin();
					String anno_id = Info.getAnnoId();
					%>
					<tr>
						<td><%=WebUtil.htmlEscape(formatDate)%></td>
						<%
						if (admin.equals("1") || admin.equals("admin")) {
						%>
						<td>管理者</td>
						<%
						} else {
						%>
						<td>全体</td>
						<%
						}
						%>
						<td><a href="javascript:void(0);"
							onclick="go_show('go_show','<%=WebUtil.txtEscape(Info.getAnnoId())%>');"><%=WebUtil.htmlEscape(Info.getTitle())%></a></td>
						<td><%=WebUtil.htmlEscape(Info.getLastName())%></td>
						<td><input type="button" value="削除 "
							onclick="go_delete('go_next','delete','<%=WebUtil.txtEscape(Info.getAnnoId())%>');" />
						</td>
					</tr>
					<%
					}
					}
					%>
				</table>
		</form>
</body>
</html>