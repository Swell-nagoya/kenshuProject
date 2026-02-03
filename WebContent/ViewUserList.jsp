<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
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
<title>ユーザー情報一覧</title>
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

.edit-reservation {
	background-color: #f0f0f0;
	border-radius: 5px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
	max-width: 800px;
	margin: 0 auto;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th {
	background-color: #f2f2f2;
	padding: 10px;
}

td {
	padding: 10px;
	border-bottom: 1px solid #ddd;
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

footer {
	width: 100%;
}
</style>
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
	function go_submit(action_cmd) {
		document.getElementById('main_form').action = 'ViewUserList.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('main_form').submit();
	}
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
		document.getElementById('main_form').action = 'UserInfoDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		document.getElementById('main_key').value = main_key;
		document.getElementById('main_form').submit();
	}
	function go_detail(action_cmd, request_cmd) {
		document.getElementById('main_form').action = 'UserInfoDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		document.getElementById('main_form').submit();
	}
	function copyToClipboard(str) {

		navigator.clipboard.writeText(str)
	}
</script>
</head>
<body>
	<div class="container">
		<div class="new-btn">
			<input type="button" value="新規登録"
				onclick="go_detail('go_next','ins')" /> <input type="button"
				value="　戻る　" onclick="history.back()" />
		</div>
		<header>
		<h1>
			<a href="javascript:void(0)" value="" onclick="go_menu('top')">ユーザー情報一覧</a>
		</h1>
		</header>
		<form id="main_form" method="post" action="">

			<input type="hidden" name="form_name" id="form_name"
				value="ViewUserList" />
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
			<div class="left">
				<div class="messages">
					<%=webBean.dispMessages()%>
				</div>
				<div class="errors">
					<%=webBean.dispErrorMessages()%>
				</div>
				<table class="select_table">
					<tr>
						<td class="search_label center" style="width: 50%">氏名</td>
						<td class="search_label center" style="width: 20%">表示件数</td>
						<td class="search_label center" style="width: 30%"></td>
					</tr>
					<tr>
						<td class="search_text center"><input type="text"
							name="list_search_full_name" id="list_search_full_name" size="30"
							maxlength="100" value="<%=webBean.txt("list_search_full_name")%>"
							class="ime_active <%=webBean.dispErrorCSS("list_search_full_name")%>"
							placeholder="検索" /> <%=webBean.dispError("list_search_full_name")%>
						</td>
						<td class="search_line center"><input type="text"
							name="lineCount" id="lineCount" size="2" maxlength="5"
							value="<%=webBean.txt("lineCount")%>" class="right ime_disabled" />件
						</td>
						<td class="search_text center"><input type="button"
							value="検索" onclick="go_submit('search')" /> <input type="button"
							value="クリア" onclick="go_submit('clear')" /></td>
					</tr>
				</table>
				<%
				if (webBean.arrayList("list").size() > 0) {
				%>
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
				<table class="list_table">
					<tr class="list_title">
						<td class="list_label" style="width: 25%"><a
							href="javaScript:go_sort_request('last_name_kana')">氏名</a></td>
						<td class="list_label" style="width: 25%"><a
							href="javaScript:go_sort_request('last_name_kana')">氏名よみ（かな）</a></td>
						<td class="list_label" style="width: 25%"><a
							href="javaScript:go_sort_request('memail')">メールアドレス</a></td>
						<td class="list_label" style="width: 25%"></td>
					</tr>
					<%
					for (Object item : webBean.arrayList("list")) {
						UserInfoDao dao = (UserInfoDao) item;
					
						//氏名をフルネームで表示する
						String fullName = WebUtil.htmlEscape(dao.getLastName()) +
						" " +
						WebUtil.htmlEscape(dao.getMiddleName()) +
						" " +
						WebUtil.htmlEscape(dao.getFirstName());
						
						//氏名（かな）をフルネームで表示する
						String fullNameKana = WebUtil.htmlEscape(dao.getLastNameKana()) +
						" " +
						WebUtil.htmlEscape(dao.getMiddleNameKana()) +
						" " +
						WebUtil.htmlEscape(dao.getFirstNameKana());
					%>
					<tr class="list_tr">
						<td class="list_text"><%=fullName%></td>
						<td class="list_text"><%=fullNameKana%></td>
						<td class="list_text"><%=WebUtil.htmlEscape(dao.getMemail())%></td>
						<td class="list_btn"><input type="button" value="編集"
							onclick="go_detail_1('go_next','update','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
							<input type="button" value="削除"
							onclick="go_detail_1('go_next','delete','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
							<input type="button" value="確認"
							onclick="go_detail_1('go_next','check','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
							<input type="button" value="閲覧管理"
							onclick="go_detail_1('go_next','access','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
						</td>
					</tr>
					<%
					}
					%>
				</table>
				<%
				}
				%>
			</div>
		</form>
	</div>
</body>
</html>
