<?xml version="1.0" encoding="UTF8" ?>

<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao" %>
<%@ page import="jp.patasys.common.http.WebUtil" %>
<%@ page import="jp.patasys.common.http.HtmlParts" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<head>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta name="viewport" content="width=device-width" , initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/flatpickr.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.smoothscroll.js"></script>
<script type="text/javascript" src="js/jquery.scrollshow.js"></script>
<script type="text/javascript" src="js/jquery.rollover.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript"
    src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/flatpickr.min.js"></script>
<title>部屋予約サイト</title>
<style type="text/css">
#scheduleTable th, #scheduleTable td:nth-child(n+2):nth-child(-n+25) {
    width: 20px; /* 統一したい横幅に調整してください */
    height: 21px;
}

#scheduleTable th:first-child, #scheduleTable td:first-child {
    width: 90px; /* 一番左の列の横幅は自動調整 */
    white-space: nowrap; /* テキストが折り返されないように設定 */
}

#myDatePicker {
    width: 115px; /* 任意の幅に調整 */
    /* 他のスタイルを追加できます */
}

#scheduleTable {
    table-layout: fixed;
    user-select: none;
}

.flatpickr-calendar {
    width: 307px; /* 適切な幅に調整してください */
    max-width: 100%; /* 幅が親要素を超えないようにする */
    font-size: 13px;
    transform: scale(0.95); /* 適切なスケールに調整してください */
    transform-origin: top left; /* 縮小の基準点を左上に設定 */
    float: left;
}

.container {
    display: flex;
    justify-content: space-between;
}

.container div {
    width: 48%;
}

table {
    border-collapse: collapse;
    width: 15%;
    margin: 0;
    border: 1px solid #333;
}

th, td {
    border: 1px solid #333;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f2f2f2;
    white-space: nowrap;
    width: 70px;
}

td[data-room] {
    text-align: center;
}

th.hourTable {
    border: none;
    border-collapse: separate;
    border-spacing: 8px 0px;
}


body {
    background-color: #f0f0f0; position : relative; /* bodyを相対位置指定 */
    margin-bottom: 100px;
    position: relative; /* ページの一番下にfooterを配置するための余白を設定 */
}

footer {
    position: fixed; /* または position: absolute; */
    bottom: 0;
    width: 100%;
    background-color: #f2f2f2;
    padding: 10px;
    text-align: center;
}
</style>
<script type="text/javascript">
//<![CDATA[
<%--検索条件入力でenterキーが押された場合の処理--%>
$(function($){


    $('html').smoothscroll({easing : 'swing', speed : 1000, margintop : 10});
    $('.totop').scrollshow({position : 500});
    $('.slide').slideshow({
        touch        : true,
        touchDistance : '80',
        bgImage      : false,
        autoSlide    : true,
        effect       : 'slide',
        repeat       : true,
        easing       : 'swing',
        interval     : 3000,
        duration     : 500,
        imgHoverStop : true,
        navHoverStop : true,
        navImg       : false,
        navImgCustom : false,
        navImgSuffix : ''
    });
    $('.slidePrev img').rollover();
    $('.slideNext img').rollover();
});

function displayData(startTime, endTime, roomName) {
    document.getElementById('startTimeInput').value = startTime;
    document.getElementById('endTimeInput').value = endTime;
    document.getElementById('roomNameInput').value = roomName;
}

function go_submit(action_cmd)
{
  document.getElementById('main_form').action='';
  document.getElementById('menu_cmd').value=action_cmd;
  document.getElementById('main_form').submit();
}
function go_sort_request(key)
{
  document.getElementById('sort_key').value=key;
  document.getElementById('menu_cmd').value='sort';
  document.getElementById('main_form').submit();
}
function go_menu(action_cmd)
{
    document.getElementById('main_form').action='MenuAdmin.do';
    document.getElementById('menu_cmd').value=action_cmd;
    document.getElementById('main_form').submit();
}
function go_detail(action_cmd,main_key)
{
  document.getElementById('main_form').action='MitsumoriDetail.do';
  document.getElementById('menu_cmd').value=action_cmd;
  document.getElementById('main_key').value=main_key;
  document.getElementById('main_form').submit();
}

// セルのクリックした時の処理
$(document).on('click', '#scheduleTable td', function() {
    var cellId = this.id;
    document.getElementById('result').textContent = "セルID: " + cellId + "時";
});

// ロード時日付表示
document.addEventListener("DOMContentLoaded", function () {
    flatpickr("#date-picker", {
        locale: "ja"
    });
});

//]]>
  function copyToClipboard(str) {

      navigator.clipboard.writeText(str)
  }
</script>
</head>
<body onload="createMenu();">
  <form id="main_form" method="post" action="">

      <input type="hidden" name="form_name" id="form_name" value="UserMenuHome" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" />
      <input type="hidden" name="main_key" id="main_key" value="" />
      <input type="hidden" name="sort_key_old" id="sort_key_old" value="<%=webBean.txt(" sort_key_old ")%>" />
      <input type="hidden" name="sort_key" id="sort_key" value="" />
      <input type="hidden" name="sort_order" id="sort_order" value="<%=webBean.txt(" sort_order ")%>" />
      <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt(" search_info ")%>" />
      <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt(" user_info_id ")%>" />
     <div id="container">

        <div id="sub">
            <header>
                <h1>
                    <a href="UserMenuHome.jsp">フレームワーク課題</a>
                </h1>
            </header>
            <nav id="navigation"></nav>
        </div>
        <div id="main">
            <h3>会議室予約</h3>
            <dl class="info">
                <p id="selectedDate"></p>
        </div>
        <script src="${pageContext.request.contextPath}/js/flatpickr.min.js"></script>
        <div>
            <p>選択日時 : <input type="text" id="myDatePicker"></p>
    <script>
        flatpickr("#myDatePicker", {
          inline: true, // カレンダーを常に表示
          dateFormat: "Y年 m月 d日", // 表示する日付のフォーマット
          onChange: function(selectedDates, dateStr, instance) {
              // 日付が変更されたときの処理
              var selectedDateElement = document.getElementById('selectedDate');
              var year = selectedDates[0].getFullYear();
              var month = selectedDates[0].getMonth() + 1; // getMonth()は0から始まるため
              var day = selectedDates[0].getDate();
              // ここで必要な処理を追加（例: サーバーサイドに送信など）
            }
        });
    </script>
        </div>
        <div class="roomTable">
            <%
    // サーバーサイドでのデータ生成（例としてリストを使用）
    List<String> roomNames = new ArrayList<>();
    roomNames.add("MTGルーム");
    roomNames.add("応接室");
    roomNames.add("個室1");
    roomNames.add("個室2");
    %>
            <table id="scheduleTable">
                <thead>
                    <tr>
                        <th></th>
                        <!-- 空のセル -->
                        <% for (int hour = 0; hour < 24; hour++) { %>
                        <th class="roomTable" data-time="<%= hour + 1 %>"><%= String.format("%2d", hour) %></th>
                        <!-- 1から24の数字を表示 -->
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                    <% for (int row = 0; row < roomNames.size(); row++) { %>
                    <tr>
                        <th><%= roomNames.get(row) %></th>
                        <% for (int col = 0; col <= 23; col++) { %>
                        <td data-room="<%= roomNames.get(row) %>" data-hour="<%= col %>" id="<%= roomNames.get(row) + "-" + col%>"></td>
                        <!-- セルの内容は空白 -->
                        <% } %>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <p id="result"></p>
        </div>
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
        <div class="copyright">Copyright &#169; 2017 RayD Developer All
            Rights Reserved.</div>
        <!-- /.copyright -->
    </footer>
    <div class="totop">
        <a href="#"><img src="images/totop.png" alt="ページのトップへ戻る"></a>
    </div>
    <!-- /.totop -->
    <script>
</script>
</form>
</body>
</html>
        </div>
    </div>
  </form>
</body>
</html>

