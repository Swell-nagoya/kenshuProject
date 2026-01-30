<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="shortcut icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<title></title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript"
	src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>

<style>
#content {
	width: 100%;
}

h1 {
	background-color: slategray;
	color: aliceblue;
	text-align: center;
	margin: 0;
}

#main {
	border: 1px solid black;
	border-radius: 5px;
	width: 500px;
	height: 500px;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.main__text {
	text-align: center;
	margin: 0;
}

.main__form {
	border: 1px solid black;
	border-radius: 10px;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 400px;
}

.ime_disabled {
	width: 350px;
	height: 50px;
	margin-top: 10px;
}

.main__form--name, .main__form--password, .main__form--btn,
	.main__form--link {
	text-align: center;
}

.main__form--btn {
	margin-top: 10px;
}

.main__form--link {
	margin-top: 30px;
}

#reissue {
	text-decoration: none;
}

.submit-btn {
	width: 200px;
	height: 40px;
	font-size: 18px;
	background-color: slategray;
	color: aliceblue;
	border: none;
	border-radius: 5px;
}

.copyright {
	width: 100%;
	background-color: slategray;
	text-align: center;
	color: aliceblue;
	position: absolute;
	bottom: 0%;
}
</style>
<script type="text/javascript">
	//
	jQuery(function($) {
		$("input").keydown(function(e) {
			if (e.which == 13) {
				go_submit('123456');
			}
		});
	});
	function go_submit(action_cmd) {
		document.getElementById("main_form").action = 'UserLogin.do';
		document.getElementById("action_cmd").value = action_cmd;
		document.getElementById("main_form").submit();
	}
	function go_submit_1(action_cmd) {
		document.getElementById("main_form").action = 'UserLogin.do';
		document.getElementById("action_cmd").value = action_cmd;
		document.getElementById("main_form").submit();
	}
	//
</script>
</head>
<body onload="document.getElementById('ac').focus()">
	<div class="container">
		<div id="contents">
			<div id="main">
				<h1>LOGIN</h1>
				<div class="main__text">
					<p>usernameとpasswordを入力してください</p>
				</div>
				<form method="post" id="main_form" action="" class="main__form">
					<input type="hidden" name="form_name" id="form_name"
						value="UserLogin" /> <input type="hidden" name="action_cmd"
						id="action_cmd" value="" /> <input type="hidden"
						name="before_doc" id="before_doc"
						value="<%=webBean.txt("before_doc")%>" />
					<div class="main__form--name">
						<input type="text" id="ac" name="ac" class="ime_disabled"
							value="<%=webBean.txt("ac")%>" placeholder="Username" size="25"
							maxlength="255" />
					</div>
					<div class="main__form--password">
						<input type="password" id="ko" name="ko" class="ime_disabled"
							value="" placeholder="Password" size="25" maxlength="60" />
					</div>
					<div class="main__form--btn">
						<input type="submit" id="bt" name="login-btn"
							onclick="go_submit('')" class="submit-btn" value="ログイン" />
					</div>
					<div class="main__form--link">
						<a href="#" id="reissue" onclick="go_submit_1('')"
							class="link-btn">パスワード再発行</a>
					</div>
				</form>
				<%=webBean.dispError("ac")%><br />
				<%=webBean.dispError("ko")%>
				<div class="copyright">Copyright &#169; 2017 RayD Developer
					All Rights Reserved.</div>
				<!-- /.copyright -->
			</div>
		</div>
	</div>
</body>
</html>
