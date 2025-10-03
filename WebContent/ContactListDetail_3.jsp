<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<title>連絡先情報確認</title>

<style type="text/css">
/* UserInfoDetail_3 と同一トーン */
body {
	font-family: 'Arial', sans-serif;
	background: #f9f9f9;
	margin: 0;
	padding: 10px;
}

.container {
	position: relative;
	background: #f0f0f0;
	border: 1px solid #ddd;
	border-radius: 5px;
	padding: 20px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 90%;
	margin: 20px auto;
}

header {
	position: relative;
	background: #00bcd4;
	height: 70px;
	margin: 15px auto;
	display: flex;
	justify-content: center;
	align-items: center;
}

h1 {
	font-size: 50px;
	color: #fff;
	font-weight: normal;
	margin: 0;
}
/* 戻る */
.new-btn {
	position: absolute;
	right: 10px;
	top: 5px;
	z-index: 2;
}

.new-btn input {
	background: #fff;
	color: #000;
	border-radius: 10px;
	cursor: pointer;
	padding: 2px 14px;
}
/* 本文 */
.center-msg {
	margin: 16px 0 6px 0;
	text-align: center;
	color: #2b35ff;
	font-size: 22px;
	font-weight: 600;
}

table {
	width: 80%;
	margin: 0 auto;
	border-collapse: collapse;
	border: 1px solid #ddd;
	background: #fff;
}

td, th {
	border: 1px solid #ddd;
	padding: 10px;
}

th {
	width: 25%;
	background: #00bcd4;
	color: #fff;
	text-align: center;
}

.btn-wrap {
	display: flex;
	justify-content: center;
	align-items: center;
	margin-top: 16px;
}

.btn-wrap input[type="button"] {
	padding: 0 50px;
	font-size: 24px;
	border: 2px solid #fff;
	background: #00bcd4;
	color: #fff;
	border-radius: 10px;
	cursor: pointer;
}

.btn-wrap input[type="button"]:hover {
	background: #4baea8;
}
</style>

<script type="text/javascript">
	function go_submit(action_cmd, request_cmd) {
		const f = document.getElementById('main_form');
		f.action = 'ContactListDetail.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('request_cmd').value = request_cmd;
		f.submit();
	}
	function go_back() {
		const f = document.getElementById('main_form');
		f.action = 'ContactListDetail.do';
		document.getElementById('action_cmd').value = 'return';
		f.submit();
	}
</script>
</head>
<body>
	<div class="container">

		<div class="new-btn">
			<input type="button" value="　戻る　" onclick="go_back()" />
		</div>

		<header>
		<h1>
			<%= "delete".equals(webBean.txt("request_cmd")) ? "連絡先削除ページ" : "連絡先情報" + webBean.txt("request_name") + "確認" %>
		</h1>
		</header>

		<!-- 案内文は1行だけ -->
		<div class="center-msg">
			<%= "delete".equals(webBean.txt("request_cmd")) ? "この連絡先を削除します。よろしいですか？" : "この内容で" + webBean.txt("request_name") + "します。よろしいですか？" %>
		</div>

		<form id="main_form" method="post" action="">
			<input type="hidden" name="form_name" value="ContactListDetail_3" />
			<input type="hidden" name="action_cmd" id="action_cmd" value="" />
			<input type="hidden" name="request_cmd" id="request_cmd"
				value="<%=webBean.txt("request_cmd")%>" />
			<input type="hidden" name="request_name"
				value="<%=webBean.txt("request_name")%>" />
			<input type="hidden" name="main_key"
				value="<%=webBean.txt("main_key")%>" />
			<input type="hidden" name="id" value="<%=webBean.txt("id")%>" />
			<input type="hidden" name="input_info"
				value="<%=webBean.txt("input_info")%>" />
			<input type="hidden" name="select_info"
				value="<%=webBean.txt("select_info")%>" />

			<div class="errors"><%=webBean.dispErrorMessages()%></div>

			<table>
				<tr>
					<th>氏名</th>
					<td><%=webBean.txt("last_name")%> <%=webBean.txt("first_name")%></td>
				</tr>
				<tr>
					<th>氏名よみ</th>
					<td><%=webBean.txt("last_name_kana")%> <%=webBean.txt("first_name_kana")%></td>
				</tr>
				<tr>
					<th>ミドルネーム</th>
					<td><%=webBean.txt("middle_name")%></td>
				</tr>
				<tr>
					<th>ミドルネームよみ</th>
					<td><%=webBean.txt("middle_name_kana")%></td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td><%=webBean.txt("phone_number")%></td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td><%=webBean.txt("email")%></td>
				</tr>
			</table>

			<div class="btn-wrap">
				<input type="button"
					value="<%= "delete".equals(webBean.txt("request_cmd")) ? "削除" : webBean.txt("request_name") %>"
					onclick="go_submit('go_next','<%=webBean.txt("request_cmd")%>')" />
			</div>
		</form>
	</div>
</body>
</html>
