<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<title>連絡先情報入力</title>

<style type="text/css">
/* ===== UserInfoDetail と同じレイアウト ===== */
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

.required-note {
	position: absolute;
	top: 110px;
	right: 20px;
	font-weight: bold;
	font-size: 18px;
	color: #f00;
}

.left {
	margin-bottom: 20px;
	text-align: center;
	display: flex;
	justify-content: center;
	align-items: center;
}

.input-table {
	width: 60%;
}
/* ラベル列（シアン）/値列（白） */
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
/* 入力 */
input[type="text"], input[type="email"], input[type="tel"] {
	border: 1px solid #696969;
	border-radius: 4px;
	padding: 5px;
	background: #fff;
	width: 95%;
}

input::placeholder {
	color: rgba(0, 0, 0, 0.3);
}

span {
	color: #f00;
	font-size: 16px;
}

span.error {
	color: #f00;
	background: transparent;
}

input.error {
	color: #F00;
	background: #FFCCCC;
	border: 1px solid #F00;
}

label.error {
	color: #F00;
}
/* ボタン（白字×シアン） */
.button {
	display: flex;
	justify-content: center;
	align-items: center;
	margin-top: 12px;
}

.button input[type="button"] {
	padding: 0 50px;
	font-size: 24px;
	border: 2px solid #fff;
	background: #00bcd4;
	color: #fff;
	border-radius: 10px;
	cursor: pointer;
}

.button input[type="button"]:hover {
	background: #4baea8;
}
/* 戻るボタン（右上固定） */
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
/* メッセージ */
.errors, .messages {
	margin-bottom: 20px;
	font-size: 16px;
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
	// Enterで次フィールド
	jQuery(function($) {
		const $inputs = $('table.input-table').find(
				'input[type="text"],input[type="email"],input[type="tel"]')
				.filter(':visible:enabled');
		$inputs.on('keydown', function(e) {
			if (e.which === 13) {
				e.preventDefault();
				const i = $inputs.index(this);
				const $n = $inputs.eq(i + 1);
				if ($n.length) {
					$n.focus();
				}
			}
		});
	});
</script>
</head>
<body>
	<div class="container">

		<div class="new-btn">
			<input type="button" value="　戻る　" onclick="go_back()" />
		</div>

		<header>
		<h1>
			連絡先情報<%=webBean.txt("request_name")%>ページ
		</h1>
		</header>
		<div class="required-note">※は必須項目</div>

		<form id="main_form" method="post" action="">
			<input type="hidden" name="form_name" value="ContactListDetail_1" />
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

			<div class="style_head3 messages"><%=webBean.dispMessages()%></div>
			<div class="errors"><%=webBean.dispErrorMessages()%></div>

			<div class="left">
				<table class="input-table">
					<tr>
						<td class="style_head3 style_head_size" style="width: 30%">氏名<span>
								※</span></td>
						<td class="input-text" style="width: 70%"><input type="text"
							name="last_name" id="last_name" maxlength="100"
							value="<%=webBean.txt("last_name")%>" placeholder="姓" /> <input
							type="text" name="first_name" id="first_name" maxlength="100"
							value="<%=webBean.txt("first_name")%>" placeholder="名" /> <br />
						<span class="error"><%=webBean.dispError("last_name")%></span> <span
							class="error"><%=webBean.dispError("first_name")%></span></td>
					</tr>
					<tr>
						<td class="style_head3 style_head_size">氏名よみ<span> ※</span></td>
						<td class="input-text"><input type="text"
							name="last_name_kana" id="last_name_kana" maxlength="100"
							value="<%=webBean.txt("last_name_kana")%>" placeholder="せい" /> <input
							type="text" name="first_name_kana" id="first_name_kana"
							maxlength="100" value="<%=webBean.txt("first_name_kana")%>"
							placeholder="めい" /> <br />
						<span class="error"><%=webBean.dispError("last_name_kana")%></span>
							<span class="error"><%=webBean.dispError("first_name_kana")%></span>
						</td>
					</tr>
					<tr>
						<td class="style_head3 style_head_size">ミドルネーム</td>
						<td class="input-text"><input type="text" name="middle_name"
							id="middle_name" maxlength="100"
							value="<%=webBean.txt("middle_name")%>" placeholder="任意" /> <br />
						<span class="error"><%=webBean.dispError("middle_name")%></span></td>
					</tr>
					<tr>
						<td class="style_head3 style_head_size">ミドルネームよみ</td>
						<td class="input-text"><input type="text"
							name="middle_name_kana" id="middle_name_kana" maxlength="100"
							value="<%=webBean.txt("middle_name_kana")%>" placeholder="にんい" />
							<br />
						<span class="error"><%=webBean.dispError("middle_name_kana")%></span>
						</td>
					</tr>
					<tr>
						<td class="style_head3 style_head_size">電話番号<span> ※</span></td>
						<td class="input-text"><input type="tel" name="phone_number"
							id="phone_number" maxlength="20"
							value="<%=webBean.txt("phone_number")%>"
							placeholder="090-1234-5678" /> <br />
						<span class="error"><%=webBean.dispError("phone_number")%></span>
						</td>
					</tr>
					<tr>
						<td class="style_head3 style_head_size">メールアドレス<span>
								※</span></td>
						<td class="input-text"><input type="email" name="email"
							id="email" maxlength="255" value="<%=webBean.txt("email")%>"
							placeholder="example@example.com" /> <br />
						<span class="error"><%=webBean.dispError("email")%></span></td>
					</tr>
				</table>
			</div>

			<div class="button">
				<input type="button" value="次へ"
					onclick="go_submit('go_next','<%=webBean.txt("request_cmd")%>')" />
			</div>
		</form>
	</div>
</body>
</html>
