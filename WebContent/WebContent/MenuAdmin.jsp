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
<title>管理者メニュー</title>
<style>
#main-menu {
	background-color: #ffffff;
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 20px;
}

.admin-menu {
	display: flex;
	flex-direction: column;
	align-items: center;
}

header {
	margin-bottom: 20px;
}

.main_link {
	display: flex;
	gap: 10px;
}

input[type="button"] {
	background-color: #00bcd4;
	color: white;
	border: none;
	padding: 10px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 15px;
	border-radius: 5px;
}

input[type="button"]:hover {
	background-color: #45a049;
}
</style>
<script type="text/javascript">
	function go_submit(action_cmd) {
		document.getElementById('admin_form').action = 'MenuAdmin.do';
		document.getElementById('action_cmd').value = action_cmd;
		document.getElementById('admin_form').submit();
	}
</script>
</head>
<body>
	<%
	jp.swell.user.UserLoginInfo loginInfo = (jp.swell.user.UserLoginInfo) session.getAttribute("LoginInfo");
	if (loginInfo == null || !"1".equals(loginInfo.getAdmin())) {
	    response.sendRedirect("UserMenuHome.do");
	    return;
	}
	%>
	<form method="post" id="admin_form" action="" class="admin__form">
		<input type="hidden" name="form_name" id="form_name" value="admin" />
		<input type="hidden" name="action_cmd" id="action_cmd" value="" />
		<div class="container">
			<div id="main-menu">
				<div class="admin-menu">
					<div id="sub">
						<header>
						<h1>管理者メニュー</h1>
						</header>
					</div>
					<div class="main_link">
						<input type="button" id="home-btn" name="home-btn"
							onclick="go_submit('home')" value="ホーム画面" /> <input
							type="button" id="user-btn" name="user-btn"
							onclick="go_submit('user')" value="ユーザー情報一覧" /> <input
							type="button" id="room-btn" name="room-btn"
							onclick="go_submit('room')" value="部屋情報一覧" /> <input
							type="button" id="reserve-btn" name="reserve-btn"
							onclick="go_submit('reserve')" value="予約情報一覧" /> <input
							type="button" id="file-btn" name="file-btn"
							onclick="go_submit('file')" value="ファイル情報一覧" /> <input
							type="button" id="calendar-btn" name="calendar-btn"
							onclick="go_submit('calendar')" value="カレンダー" /> <input
							type="button" id="Shift-btn" name="Shift-btn"
							onclick="go_submit('Shift')" value="シフト管理" /> <input
							type="button" id="Contact-btn" name="Contact-btn"
							onclick="go_submit('Contact')" value="連絡先一覧" />

					</div>
				</div>
			</div>
		</div>
		</div>
	</form>
</body>
</html>