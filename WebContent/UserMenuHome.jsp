<!DOCTYPE html>
<html lang="ja">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao" %>
<%@ page import="jp.patasys.common.http.WebUtil" %>
<%@ page import="jp.patasys.common.http.HtmlParts" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList" %>
<title>RayD開発研修サイト</title>
<meta name="viewport" content="width=device-width">
<meta name="keywords" content="">
<meta name="description" content="">
<link rel="stylesheet" type="text/css" href="css/style.css">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.smoothscroll.js"></script>
<script type="text/javascript" src="js/jquery.scrollshow.js"></script>
<script type="text/javascript" src="js/jquery.rollover.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script>
$(function($){
	$('html').smoothscroll({easing : 'swing', speed : 1000, margintop : 10});
	$('.totop').scrollshow({position : 500});
	$('.slide').slideshow({
		touch		: true,
		touchDistance : '80',
		bgImage	  : false,
		autoSlide	: true,
		effect	   : 'slide',
		repeat	   : true,
		easing	   : 'swing',
		interval	 : 3000,
		duration	 : 500,
		imgHoverStop : true,
		navHoverStop : true,
		navImg	   : false,
		navImgCustom : false,
		navImgSuffix : ''
	});
	$('.slidePrev img').rollover();
	$('.slideNext img').rollover();
	});
</script>
<!--[if lt IE 9]>
	<script src="js/html5shiv.js"></script>
	<script src="js/css3-mediaqueries.js"></script>
<![endif]-->
</head>
<body onload="createMenu();">
  <div id="contents">
    <div id="sub">
      <header>
        <h1><a href="../index.html">RayD</a></h1>
        <p class="summary"> 開発研修サイト </p>
      </header>
      <nav id="navigation"> </nav>
    </div>
    <div id="main">
      <h2>ホーム</h2>
      <dl class="info">
        <li><a href="ViewUserList.do">ユーザ情報</a></li>
        <div style="text-align:center"> <img src="images/shigoto_white_man_suit.png" alt=""> </Div>
    </div>
    <!-- /#main -->
  </div>
  <!-- /#contents -->
  <footer>
    <div class="footmenu">
      <ul>
        <li><a href="index.html">HOME</a></li>
      </ul>
    </div>
    <!-- /.footmenu -->
    <div class="copyright">Copyright &#169; 2017 RayD Developer All Rights Reserved.</div>
    <!-- /.copyright -->
  </footer>
  <div class="totop"><a href="#"><img src="images/totop.png" alt="ページのトップへ戻る"></a></div>
  <!-- /.totop -->
</body>
</html>