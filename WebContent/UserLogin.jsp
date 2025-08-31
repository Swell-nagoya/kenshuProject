<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<%@ page import="jp.patasys.common.http.WebUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="stylesheet" href="css/cssreset.css" type="text/css" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.6/semantic.min.css" integrity="sha256-iK7AZM7Xa42iuURdMdo1sp38ld/JJ3fDtS0523GKqdk=" crossorigin="anonymous" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/private-browsing.js"></script>
<title></title>
    <style>
      .pusher {
          padding-top:50px;
      }
     body {
       background-color: whitesmoke;
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
	function go_submit(action) {
		trim_all_values();
		document.getElementById("action_cmd").value = action;
		document.getElementById("main_form").action = "UserLogin.do";
		document.getElementById("main_form").submit();
		return false;
	}
	//
</script>
</head>
<body onload="document.getElementById('ac').focus()">
<div class="container">
  <form method="post" id="main_form" action="UserLogin.do">
    <input type="hidden" name="form_name" id="form_name" value="E" />
    <input type="hidden" name="action_cmd" id="action_cmd" value="" />
    <input type="hidden" name="before_doc" id="before_doc" value="<%=webBean.txt("before_doc")%>" />

    <div class="center">
    <div class="ui link cards">
    <div class="ui card">
      <div class="content">
        <div class="header"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">ようこそ</font></font></div>
      </div>
      <div class="content">
        <div class="ui small feed">
          <div class="event">
            <div class="content">
              <div class="summary">
                <input name="ac" type="text" id="ac" value="<%=webBean.txt("ac")%>" size="25" class="ime_disabled" maxlength="255" />
                <%=webBean.dispError("ac")%><br/>
              </div>
              <div class="summary">
              　
              </div>
              <div class="summary">
                <input name="ko" type="password" id="ko" value="" size="25" maxlength="60" class="ime_disabled" />
                <%=webBean.dispError("ko")%>
              </div>
              <div class="summary">
              　
              </div>
                <input type="button" value="　　ボタン　　" onclick="go_submit('123456')" /><br/>
              　　<a href="#" onclick="go_retry()">再発行－＞</a>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
      </div>    
  </form>
</div>
</body>
</html>
