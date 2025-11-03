<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.ScheduleDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.FileDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="jp.swell.dao.UserReserveDao"%>
<%@ page import="jp.swell.dao.ReserveFileDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width" , initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" href="css/flatpickr.min.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/jquery.timepicker.min.js"></script>
<script type="text/javascript" src="js/flatpickr.min.js"></script>
<script type="text/javascript" src="js/cell.js"></script>
<jsp:include page="/calendar-core.jsp" />

<title>部屋予約サイト</title>
<style type="text/css">
body {
    background-color: #f0f0f0;
    position: relative; /* bodyを相対位置指定 */
    margin-bottom: 100px;
    width: 100%; /* コンテンツの最小幅 */
    overflow-x: auto;
}

header {
    background-color: #f0f0f0;
    margin: 0 5px;
    display: flex;
}

div #title {
    flex: 1;
}
/* h1の文字色 */
h1 a{
     color:#000000;
     font-weight: bold;
}
/* h1の背景色 */
h1 {
    background-color: #f0e6f7;
    border-radius: 15px;
}

div #subtitle {
    flex: 5;
    display: flex;
    justify-content: space-between;
    border-bottom: 3px dotted #800080;
    align-items: center;
}
#month_button {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 5px;
}
#current_month {
    font-size: 25px;
}
#month_button button {
    display: flex;
    justify-content: center;
    align-items: center; 
    font-size: 25px;
    width: 30px;
    height: 30px;
    border: none;
}
button:hover {
    background-color: #f0e6f7;
}
#user_menu {
    display: flex;
    justify-content: center;
    align-items: center;
}
/* Menuボタン全体のスタイル */
.menu {
    display: flex;
    justify-content: flex-start;
    list-style-type: none;
    padding: 0;
    margin: 3px;
}
/* li要素のスタイル設定 */
.menu li {
    position: relative;
    width: 100px;
    margin-left: 1px;
    padding: 5px;
    background: #f0e6f7;
    list-style-type: none;
    font-weight: bold;
}
/* 隠れているメニューのスタイル設定 */
.menuSub {
  position: absolute;
  margin-left: -6px;
  padding: 0;
  display: none;
  list-style-type: none;
}
/* 隠れているメニュー内のリンクにホバーしたときのスタイル設定 */
.menuSub li:hover {
  cursor: pointer;
  background-color: #FFCA7B;
}
/* 管理画面のスタイル設定 */
.control {
  color: #382400;
  font-weight: bold;
}
/* アイコン設定 */
.menu li i.material-icons {
  font-size: 15px;
  margin-right: 6px;
  color:#800080;
  position: relative;
  top: 3px
}
.user-block {
    padding: 0px 5px;        
    border: 1px solid #800080;
    margin: 5px;              
    background-color: #f0e6f7; 
    border-radius: 5px;        
    color: #000;               
    vertical-align: middle;
}

div #middle {
    margin: 5px;
    display: flex;
}

nav {
    flex: 1;
}
#month_button_sub {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 90%;
    margin-left: auto;
    margin-right: auto;
}
#prev_sub,
#next_sub {
    padding: 0 4px;
    border: none;
}
#subCalendar{
    width: 90%;
    margin-left: auto;
    margin-right: auto;
    table-layout: fixed;
}
#subCalendar td,
#subCalendar th {
    text-align: center;
    aspect-ratio: 1;
    padding: 3px;
    font-size: 12px;
}
#subCalendar td {
    cursor: default;
}
#subCalendar td :first-child {
    aspect-ratio: 1;
    border-radius: 50%;
    padding: 3px;
}
#subCalendar td :first-child:hover {
    background-color: lightgray;
}
.selectedCellSub {
    background-color: lightcyan;
}
#myCalendar {
    width: 90%;
    margin-left: auto;
    margin-right: auto;
}

main {
    flex: 5;
    margin: 0;
}
main table {
    width: 100%;
    margin-top: 5px;
    table-layout: fixed;
}
.monthCalendar th {
    padding: 3px;
    text-align: center;
}
.monthCalendar td {
    padding: 3px;
    text-align: center;
    font-size: 12px;
    height: 100px;
}
.monthCalendar td :first-child {
    height: 22px;
    width: 22px;
    line-height: 22px;
    border-radius: 11px;
    margin-left: auto;
    margin-right: auto;
}
div.today {
    background-color: blue;
    color: white;
}
.mute {
    color: gray;
}
.myreserve {
    background-color: skyblue;
    margin: 1px 0;
    padding: 0 3px;
    
}
.reserved {
    background-color: violet;
    margin: 1px 0;
    padding: 0 3px;
}

.dayRow th {
    border-bottom: hidden;
    height: 15px;
    padding: 2px;
}
th.dateOfWeek {
    height: 40px;
    padding: 4px;
}
.dateOfWeek div {
    font-size: 24px;
    aspect-ratio: 1;
    width: 40px;
    line-height:40px;
    border-radius: 50%;
    margin: auto;
    padding: 0;
}
#weekCalendar th:first-child {
    vertical-align: bottom;
    text-align: right;
    padding: 0 3px;
    width: 5%;
}
.timeCell {
    line-height: 1;
    font-size: 12px;
}
#weekCalendar td {
    height: 10px;
    padding: 0;
}
.divwrapper {
    display: flex;
    height: 100%;
    width: 100%;
    padding: 0;
    margin: 0;
    pointer-events: none;
}
.divwrapper>div {
    justify-content: center;
    height: 100%;
    width: 10%;
    padding: 0;
    margin: 0 auto;
    pointer-events: none;
}
.myreserveWeek {
    background-color: skyblue;
}
.reservedWeek {
    background-color: violet;
}
.hiddenTime {
    border-bottom: hidden;
}
.drug {
    background-color: tomato;
}

footer {
    bottom: 0;
    width: 100%;
    background-color: #f2f2f2;
    padding: 10px;
    text-align: center;
}
</style>

<script type="text/javascript">

    function displayData(startTime, endTime, roomName) {
        document.getElementById('startTimeInput').value = startTime;
        document.getElementById('endTimeInput').value = endTime;
        document.getElementById('roomNameInput').value = roomName;
    }

    function go_submit(action_cmd) {
        document.getElementById('main_form').action = '';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
    function go_sort_request(key) {
        document.getElementById('sort_key').value = key;
        document.getElementById('menu_cmd').value = 'sort';
        document.getElementById('main_form').submit();
    }
    function go_menu(action_cmd) {
        document.getElementById('main_form').action = 'UserMenu.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
    function go_detail(action_cmd, main_key) {
        document.getElementById('main_form').action = 'UserMenu.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_key').value = main_key;
        document.getElementById('main_form').submit();
    }
    function go_file(action_cmd) {
        document.getElementById('main_form').action = 'FileList.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
    function go_logout(action_cmd) {
        document.getElementById('main_form').action = 'UserLogin.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
    function go_download(action_cmd,request_cmd,main_key,file_name){
        document.getElementById('main_form').action='FileDetail.do';
        document.getElementById('action_cmd').value=action_cmd;
        document.getElementById('request_cmd').value=request_cmd;
        document.getElementById('main_key').value=main_key;
        document.getElementById('file_name').value=file_name;
        document.getElementById('main_form').submit();
    }


    const date = new Date();
    const today = date.getDate();
    const monthDays = ["日", "月", "火", "水", "木", "金", "土"];
    let selectedCell = new Date();
    let calendarDateSub = new Date();
    calendarDateSub.setDate(1);

    //月カレンダーの作成
    function createCalendar(className, dateObject, idHead) {
        let calendarHTML = '<table class="' + className + '"><thead><tr>';
    
        for (let i = 0; i < 7; i ++) {//曜日を作成
            calendarHTML += '<th>' + monthDays[i] + '</th>';
        }
        
        calendarHTML += '</tr></thead><tbody>';
        
        const firstDay = new Date(dateObject.getFullYear(), dateObject.getMonth(), 1).getDay(); //今月の最初の曜日
        let dateInMonth = new Date(dateObject);
        dateInMonth.setDate(1 - firstDay);
    
        for (let i = 0; i < 6; i ++) {
            calendarHTML += '<tr>';
    
            for (let j = 0; j < 7; j ++) { //カレンダーを一列ずつ作成(一日ずつIDを付与)
                if (dateInMonth.getMonth() !== dateObject.getMonth()) { //先月来月分の日付
                    calendarHTML += 
                        '<td id = "' + idHead + dateInMonth.getFullYear() + ("0" + (dateInMonth.getMonth() + 1)).slice(-2) + ("0" + dateInMonth.getDate()).slice(-2) + '"><div class = "date mute">' + dateInMonth.getDate() + '</div></td>';
                } else { //今月分の日付
                    calendarHTML += 
                        '<td id = "' + idHead + dateInMonth.getFullYear() + ("0" + (dateInMonth.getMonth() + 1)).slice(-2) + ("0" + dateInMonth.getDate()).slice(-2) + '"><div class = "date">' + dateInMonth.getDate() + '</div></td>';
                }
                dateInMonth.setDate(dateInMonth.getDate() + 1);
            }
            calendarHTML += '</tr>';
    
            if (className === 'monthCalendar' && dateInMonth.getMonth() > dateObject.getMonth()) { //メインカレンダーは５週目で今月が終われば終了
                break;
            }
        }
        calendarHTML += '</tbody></table>';
        
        return calendarHTML;
    }

    //月カレンダーに予約を挿入する関数
    function insertReserve() {
        let dateCell;
        <%
        // データベースの予約情報が空でないかの確認
        if (webBean.arrayList("reserves") != null && !webBean.arrayList("reserves").isEmpty()) {
          // 予約情報を取るためのループ処理
            for (Object reserveItem : webBean.arrayList("reserves")) {
                ReserveDao reserve = (ReserveDao) reserveItem;
                
                String checkinTime = WebUtil.htmlEscape(reserve.getCheckinTime());
                String checkoutTime = WebUtil.htmlEscape(reserve.getCheckoutTime());
                String formatCheckinTime = checkinTime.substring(0, 2) + ":" + checkinTime.substring(2, 4);
                String formatCheckoutTime = checkoutTime.substring(0, 2) + ":" + checkoutTime.substring(2, 4);
                %>
                dateCell = document.getElementById("date<%=WebUtil.htmlEscape(reserve.getReservationDate())%>");
                if (dateCell) {
                    <%
                    if (WebUtil.htmlEscape(reserve.getUserInfoId()).equals(webBean.txt("user_info_id"))) {
                    %>
                        dateCell.innerHTML += 
                            '<div class="myreserve room<%=WebUtil.htmlEscape(reserve.getRoomId())%> <%=WebUtil.htmlEscape(reserve.getUserInfoId())%>"><%=WebUtil.htmlEscape(formatCheckinTime)%>-<%=WebUtil.htmlEscape(formatCheckoutTime)%> <%=WebUtil.htmlEscape(reserve.getRoomName())%></div>';
                    <%
                    } else if (webBean.txt("admin").equals("admin")) {
                    %>
                        dateCell.innerHTML += 
                            '<div class="reserved room<%=WebUtil.htmlEscape(reserve.getRoomId())%> <%=WebUtil.htmlEscape(reserve.getUserInfoId())%>"><%=WebUtil.htmlEscape(formatCheckinTime)%>-<%=WebUtil.htmlEscape(formatCheckoutTime)%> <%=WebUtil.htmlEscape(reserve.getRoomName())%><br><%=WebUtil.htmlEscape(reserve.getUserName())%></div>';
                    <%
                    }
                    %>
                }
            <%
            }
        }
        %>
    }

    let firstDateInWeek;
    
    //週カレンダーの作成
    function createWeekCalendar() {
        <%
        // データベースの部屋情報が空でないかの確認
        if (webBean.arrayList("rooms") != null && !webBean.arrayList("rooms").isEmpty()) {
        %>
            const roomLength = <%=webBean.arrayList("rooms").size()%>;
            let weekCalendarHTML = '<table id = "weekCalendar"><thead><tr class = "dayRow"><th></th>';

            // ✅ ここで firstDateInWeek を最初に定義！
            firstDateInWeek = new Date(selectedCell);
            for (let j = firstDateInWeek.getDay(); j > 0; j--) {
              firstDateInWeek.setDate(firstDateInWeek.getDate() - 1);
            }

            for (let i = 0; i < 7; i ++) { //曜日のセルを作成
            	  const dateInWeek = new Date(firstDateInWeek);
            	  dateInWeek.setDate(firstDateInWeek.getDate() + i);

            	  const className = getDayClass(dateInWeek); // ← ここで祝日・曜日判定
            	weekCalendarHTML += '<th class = "weekDay ' + className + '">' + monthDays[i] + '</th>';
            }
            
            weekCalendarHTML += '</tr><tr><th class = "timeCell">0時</th>';
    
            firstDateInWeek = new Date(selectedCell);
            for (let j = firstDateInWeek.getDay(); j > 0; j --) { //週の初日を特定
                firstDateInWeek.setDate(firstDateInWeek.getDate() - 1);
            }
            let dateInWeek = new Date(firstDateInWeek);
            for (let k = 0; k < 7; k ++) { //日付のセルを作成
            	 const className = getDayClass(dateInWeek); // ← 曜日・祝日クラス付与
            	  const ymd = dateInWeek.getFullYear() + 
            	              ("0" + (dateInWeek.getMonth() + 1)).slice(-2) + 
            	              ("0" + dateInWeek.getDate()).slice(-2);

            	  weekCalendarHTML += 
            	    '<th class="dateOfWeek"><div id="weekDate' + ymd + '" class="' + className + '">' + dateInWeek.getDate() + '</div></th>';
            	  
            	  dateInWeek.setDate(dateInWeek.getDate() + 1);
            	}
        	
            
            weekCalendarHTML += '</tr></thead><tbody class = "weekBody">';
    
            for (let m = 1; m < 25; m ++) { //時間のセルを作成
                for (let t = 0; t < 3; t ++) {
                    dateInWeek = new Date(firstDateInWeek);
                    weekCalendarHTML += '<tr><th class = "hiddenTime"></th>';
                    for (let u = 0; u < 7; u ++) {
                        weekCalendarHTML += 
                            '<td class = "hiddenTime" data-date = "' + dateInWeek.getFullYear() + ("0" + (dateInWeek.getMonth() + 1)).slice(-2) + ("0" + dateInWeek.getDate()).slice(-2) + '" data-time = "' + ("00" + (m - 1)).slice(-2) + ("00" + (15 * t)).slice(-2) + '"><div class ="divwrapper">';
                        for (let v = 0; v < roomLength; v ++) {
                        	weekCalendarHTML += '<div></div>';
                        }
                        weekCalendarHTML += '</div></td>';
                        dateInWeek.setDate(dateInWeek.getDate() + 1);
                    }
                    weekCalendarHTML += '</tr>';
                }
                dateInWeek = new Date(firstDateInWeek);
            	weekCalendarHTML += '<tr><th class = "timeCell">' + m + '時</th>';
                for (let s = 0; s < 7; s ++) {
                    weekCalendarHTML += '<td data-date = "' + dateInWeek.getFullYear() + ("0" + (dateInWeek.getMonth() + 1)).slice(-2) + ("0" + dateInWeek.getDate()).slice(-2) + '" data-time = "' + ("00" +(m - 1)).slice(-2) + '45"><div class ="divwrapper">';
                    for (let w = 0; w < roomLength; w ++) {
                        weekCalendarHTML += '<div></div>';
                    }
                    weekCalendarHTML += '</div></td>';
                    dateInWeek.setDate(dateInWeek.getDate() + 1);
                }
                weekCalendarHTML += '</tr>';
            }
            
            weekCalendarHTML += '</tbody></table>';
            
            return weekCalendarHTML;
        <%
        }
        %>
    }

    //日カレンダーの作成
	function createDayCalendar(dateObject) {
	  const year = dateObject.getFullYear();
	  const month = String(dateObject.getMonth() + 1).padStart(2, '0');
	  const day = String(dateObject.getDate()).padStart(2, '0');
	  const dayOfWeekJP = ['日', '月', '火', '水', '木', '金', '土'][dateObject.getDay()];
	  const dateStr = year + month + day; // data-date に使う形式
	
	  let calendarHTML = '<table class="day-calendar">';
	  calendarHTML += '<thead>';
	  calendarHTML += '<tr><th colspan="2">' + year + '年' + month + '月' + day + '日（' + dayOfWeekJP + '）</th></tr>';
	  calendarHTML += '</thead>';
	  calendarHTML += '<tbody>';
	
	  for (let hour = 9; hour <= 20; hour++) {
	    const hourStr = String(hour).padStart(2, '0');
	    for (let q = 0; q < 4; q++) {
	      const minuteStr = String(q * 15).padStart(2, '0');
	      const time = hourStr + minuteStr;
	      const label = hourStr + ':' + minuteStr;
	
	      calendarHTML += '<tr>';
	      calendarHTML += '<td class="time-cell">' + label + '</td>';
	      calendarHTML += '<td class="schedule-cell" data-date="' + dateStr + '" data-time="' + time + '">';
	      calendarHTML += '<div class="divwrapper"><div></div></div>';
	      calendarHTML += '</td>';
	      calendarHTML += '</tr>';
	    }
	    
	  }
	
	  calendarHTML += '</tbody>';
	  calendarHTML += '</table>';
	
	  return calendarHTML;
	}

    //週カレンダーに予約を挿入する関数
<%--     function insertReserveWeek() {
        let dateCell;
        let checkinTime;
        let checkoutTime;
        let checkinTimeMinutes;
        let checkoutTimeMinutes;
        let cellNumbers;
        <%
        // データベースの予約情報が空でないかの確認
        if (webBean.arrayList("reserves") != null && !webBean.arrayList("reserves").isEmpty() && webBean.arrayList("rooms") != null && !webBean.arrayList("rooms").isEmpty()) {
            // 予約情報を取るためのループ処理
            int roomLength = webBean.arrayList("rooms").size();
            
            for (Object reserveItem : webBean.arrayList("reserves")) {
                ReserveDao reserve = (ReserveDao) reserveItem;
                String roomName = reserve.getRoomName();
                String reservationDate = reserve.getReservationDate();
                String checkinTime = reserve.getCheckinTime();
                String checkoutTime = reserve.getCheckoutTime();
                String formatCheckinTime = checkinTime.substring(0, 2) + ":" + checkinTime.substring(2, 4);
                String formatCheckoutTime = checkoutTime.substring(0, 2) + ":" + checkoutTime.substring(2, 4);
                
                List<Object> rooms = webBean.arrayList("rooms");
                int indexOfRoom = -1;
                for (int i = 0; i < rooms.size(); i++) {
                    RoomDao room = (RoomDao) rooms.get(i);
                    if (room.getRoomName().equals(roomName)) {
                        indexOfRoom = i;
                        break;
                    }
                }
                %>
                dateCell = document.getElementById('weekDate<%=WebUtil.htmlEscape(reservationDate)%>');
                if (dateCell) {
                    checkinTime = '<%=WebUtil.htmlEscape(checkinTime)%>';
                    checkoutTime = '<%=WebUtil.htmlEscape(checkoutTime)%>';
                    checkinTimeMinutes = parseInt(checkinTime.slice(0, 2)) * 60 + parseInt(checkinTime.slice(2, 4));
                    checkoutTimeMinutes = parseInt(checkoutTime.slice(0, 2)) * 60 + parseInt(checkoutTime.slice(2, 4));
                    cellNumbers = (checkoutTimeMinutes - checkinTimeMinutes) / 15;
                    <%
                    if (WebUtil.htmlEscape(reserve.getUserInfoId()).equals(webBean.txt("user_info_id"))) {
                    %>
                        for (let i = 0; i < cellNumbers; i ++) {
                            targetCell = document.querySelector('[data-date = "<%=reservationDate%>"][data-time = "${checkinTime}"]');
                            targetWrapper = targetCell.firstElementChild;
                            targetBlock = targetWrapper.children[<%=indexOfRoom%>];
                            targetBlock.classList.add('myreserveWeek');
                            checkinTime = checkinTime.slice(0, 2) + (parseInt(checkinTime.slice(2, 4)) + 15);
                            if (checkinMinute.slice(2, 4) === '60') {
                            	checkinTime = ('0' + (parseInt(checkinTime) + 40)).slice(-4);
                            }
                        }
                    <%
                    } else if (webBean.txt("admin").equals("admin")) {
                    %>
                        for (let i = 0; i < cellNumbers; i ++) {
                            targetCell = document.querySelector('[data-date = "<%=reservationDate%>"][data-time = "${checkinTime}"]');;
                            targetWrapper = targetCell.firstElementChild;
                            targetBlock = targetWrapper.children[<%=indexOfRoom%>];
                            targetBlock.classList.add('reservedWeek');
                            checkinTime = checkinTime.slice(0, 2) + (parseInt(checkinTime.slice(2, 4)) + 15);
                            if (checkinMinute.slice(2, 4) === '60') {
                                checkinTime = ('0' + (parseInt(checkinTime) + 40)).slice(-4);
                            }
                    <%
                    }
                    %>
                }
            <%
            }
        }
        %>
    } --%>

    //チェックボックスの作成
    function createMyCalendar() {
        let myCalendarHTML = '';
        <%
        // データベースの予約情報が空でないかの確認
        if (webBean.arrayList("users") != null && !webBean.arrayList("users").isEmpty()) {
          // 予約情報を取るためのループ処理
            for (Object allUsers : webBean.arrayList("users")) {
                UserInfoDao user = (UserInfoDao) allUsers;
                %>
                myCalendarHTML += 
                    '<div id="<%=WebUtil.htmlEscape(user.getUserInfoId())%>"><label><input type="checkbox" id="<%=WebUtil.htmlEscape(user.getUserInfoId())%>check" name="<%=WebUtil.htmlEscape(user.getUserInfoId())%>check" value="" checked/><%=WebUtil.htmlEscape(user.getFullName())%></label></div>';
            <%
            }
        }
        %>
        return myCalendarHTML;
    }

    //チェックボックスで予約の表示・非表示を操作する関数
    function reserveDisplay() {
        let roomElements;
        <%
        if (webBean.arrayList("users") != null && !webBean.arrayList("users").isEmpty()) {
            for (Object allUsers : webBean.arrayList("users")) {
                UserInfoDao user = (UserInfoDao) allUsers;
                String userId = WebUtil.htmlEscape(user.getUserInfoId());
        %>
        const checkbox<%=userId%> = document.getElementById("<%=userId%>check");
        if (checkbox<%=userId%>) {
            roomElements = document.getElementsByClassName("<%=userId%>");
            if (checkbox<%=userId%>.checked) {
                for (let i = 0; i < roomElements.length; i++) {
                    roomElements[i].style.display = "block";
                }
            } else {
                for (let i = 0; i < roomElements.length; i++) {
                    roomElements[i].style.display = "none";
                }
            }
        }
        <%
            }
        }
        %>
    }

    //チェックボックスを押したときにイベントが起きるように設定する関数
    function changeReserveDisplay() {
        let roomElements;
        <%
        if (webBean.arrayList("users") != null && !webBean.arrayList("users").isEmpty()) {
            for (Object allUsers : webBean.arrayList("users")) {
                UserInfoDao user = (UserInfoDao) allUsers;
                String userId = WebUtil.htmlEscape(user.getUserInfoId());
        %>
        const checkbox<%=userId%> = document.getElementById("<%=userId%>check");
        if (checkbox<%=userId%>) {
            checkbox<%=userId%>.addEventListener('change', () => {
                roomElements = document.getElementsByClassName("<%=userId%>");
                if (checkbox<%=userId%>.checked) {
                    for (let i = 0; i < roomElements.length; i++) {
                        roomElements[i].style.display = "block";
                    }
                } else {
                    for (let i = 0; i < roomElements.length; i++) {
                        roomElements[i].style.display = "none";
                    }
                }
            });
        }
        <%
            }
        }
        %>
    }
    
    function showSubCalendar() {
        document.getElementById('current_month_sub').innerHTML = calendarDateSub.getFullYear() + '年' + (calendarDateSub.getMonth() + 1) + '月'; //年月の表示
        document.getElementById('subCalendar').innerHTML = createCalendar('subCalendar', calendarDateSub, 'subDate'); //カレンダーの表示
        const todayId = 'subDate' + date.getFullYear() + ("0" + (date.getMonth() + 1)).slice(-2) + ("0" + date.getDate()).slice(-2);
        if (document.getElementById(todayId)) { //今日の日付があればクラス付与
        	document.getElementById(todayId).firstElementChild.classList.add('today');
        }
        const selectedCellId = 'subDate' + selectedCell.getFullYear() + ("0" + (selectedCell.getMonth() + 1)).slice(-2) + ("0" + selectedCell.getDate()).slice(-2);
        if (document.getElementById(selectedCellId)) { //選択セルがあればクラス付与
            document.getElementById(selectedCellId).firstElementChild.classList.add('selectedCellSub');
        }
    }

    function showCalendar() {
        document.getElementById('current_month').innerHTML = selectedCell.getFullYear() + '年' + (selectedCell.getMonth() + 1) + '月'; //年月の表示
        document.getElementById('calendar').innerHTML = createCalendar('monthCalendar', selectedCell, 'date'); //カレンダーの表示
        const todayId = 'date' + date.getFullYear() + ("0" + (date.getMonth() + 1)).slice(-2) + ("0" + date.getDate()).slice(-2);
        if (document.getElementById(todayId)) { //今日の日付があればクラス付与
            document.getElementById(todayId).firstElementChild.classList.add('today');
        }
        insertReserve(); //カレンダーに予約を挿入
        reserveDisplay(); //チェックボックスの値を反映
    }

    function showWeekCalendar() {
        document.getElementById('calendar').innerHTML = createWeekCalendar(); //カレンダーの表示
        const todayId = 'weekDate' + date.getFullYear() + ("0" + (date.getMonth() + 1)).slice(-2) + ("0" + date.getDate()).slice(-2);
        if (document.getElementById(todayId)) { //今日の日付があればクラス付与
            document.getElementById(todayId).classList.add('today');
        }
        let lastDateInWeek = new Date(firstDateInWeek);
        lastDateInWeek.setDate(firstDateInWeek.getDate() + 6);
        if (firstDateInWeek.getFullYear() === lastDateInWeek.getFullYear()) { //年月の表示
            if (firstDateInWeek.getMonth() === lastDateInWeek.getMonth()) {
                document.getElementById('current_month').innerHTML = 
                    firstDateInWeek.getFullYear() + '年' + (firstDateInWeek.getMonth() + 1) + '月';
            } else {
                document.getElementById('current_month').innerHTML = 
                    firstDateInWeek.getFullYear() + '年' + (firstDateInWeek.getMonth() + 1) + '月～' + (lastDateInWeek.getMonth() + 1) + '月';
            }
        } else {
            document.getElementById('current_month').innerHTML = 
                firstDateInWeek.getFullYear() + '年12月～' + lastDateInWeek.getFullYear() + '年1月';
        }
        /* insertReserveWeek(); */
    }
    
    function showDayCalendar() {
        const selectedDate = selectedCell;
        const dayHTML = createDayCalendar(selectedDate);
        document.getElementById('calendar').innerHTML = dayHTML;

        document.getElementById('current_month').innerHTML =
            selectedDate.getFullYear() + '年' +
            (selectedDate.getMonth() + 1) + '月' +
            selectedDate.getDate() + '日';
        
        insertReserveDay();
    }
    
    //メインカレンダーを動かす関数
    function moveCalendar(e) {
    	const calendarType = document.getElementById('calendar_type').value;
    	
        if (calendarType === 'calendar') {
            if (e.target.id === 'prev') {
                selectedCell.setDate(1);
                selectedCell.setMonth(selectedCell.getMonth() - 1);
            } else if (e.target.id === 'next') {
                selectedCell.setDate(1);
                selectedCell.setMonth(selectedCell.getMonth() + 1);
            }
            calendarDateSub.setFullYear(selectedCell.getFullYear());
            calendarDateSub.setMonth(selectedCell.getMonth());
            showSubCalendar();
            showCalendar();
        } else if (calendarType === 'weekCalendar') {
            if (e.target.id === 'prev') {
                selectedCell.setDate(selectedCell.getDate() - 7);
            } else if (e.target.id === 'next') {
                selectedCell.setDate(selectedCell.getDate() + 7);
            }
            calendarDateSub.setFullYear(selectedCell.getFullYear());
            calendarDateSub.setMonth(selectedCell.getMonth());
            showSubCalendar();
            showWeekCalendar();
        } else if (calendarType === 'dayCalendar') {
            if (e.target.id === 'prev') {
                selectedCell.setDate(selectedCell.getDate() - 1);
            } else if (e.target.id === 'next') {
                selectedCell.setDate(selectedCell.getDate() + 1);
            }
            calendarDateSub.setFullYear(selectedCell.getFullYear());
            calendarDateSub.setMonth(selectedCell.getMonth());
            showSubCalendar();
            showDayCalendar();
        }
    }
    
    //サブカレンダーを動かす関数
    function moveSubCalendar(e) {
        if (e.target.id === 'prev_sub') { //前の月に戻る処理
        	calendarDateSub.setMonth(calendarDateSub.getMonth() - 1);
        }
        else if (e.target.id === 'next_sub') { //次の月に進む処理
            calendarDateSub.setMonth(calendarDateSub.getMonth() + 1);
        }
        showSubCalendar();
    }

    //カレンダーを変更する関数
    function changeCalendar(calendarType) {
    	const currentType = document.getElementById('calendar_type').value;

        if (calendarType != document.getElementById('calendar_type').value) {
        	// 🔁 値の更新は比較のあと
        	document.getElementById('calendar_type').value = calendarType;
        	calendarDateSub.setFullYear(selectedCell.getFullYear());//サブカレンダー連動のオブジェクト
            calendarDateSub.setMonth(selectedCell.getMonth());
            showSubCalendar();
        }
            if (calendarType === 'calendar') {
                showCalendar();
            } else if (calendarType === 'weekCalendar') {
            	showWeekCalendar();
            }else if (calendarType === 'dayCalendar') {
                showDayCalendar();
            }
        }

    $(function() {
        const admin = '<%=(webBean.txt("admin"))%>';
        if (admin === 'admin') {
            document.getElementById('myCalendar').innerHTML = createMyCalendar();
        }

        showSubCalendar();
        showCalendar();
        document.getElementById('prev').addEventListener('click', moveCalendar);
        document.getElementById('next').addEventListener('click', moveCalendar);
        document.getElementById('prev_sub').addEventListener('click', moveSubCalendar);
        document.getElementById('next_sub').addEventListener('click', moveSubCalendar);
        changeReserveDisplay();
    });

    //カレンダーのセルにクリックイベントを追加
    $(document).on('click', '.monthCalendar td', function(e) {
        const selectedDate = this.id.substr(4);
        const formattedDate = selectedDate.substr(0, 4) + "年" + selectedDate.substr(4, 2) + "月" + selectedDate.substr(6, 2) + "日";
         // hidden フィールドに値をセット
        $('#reservation_date').val(formattedDate);
        //予約画面に移行
        go_detail('reserve', '');
    });
    
    //サブカレンダーのセルにクリックイベントを追加
    $(document).on('click', '.subCalendar td', function(e) {
        selectedCell = new Date(this.id.substr(7, 4), parseInt(this.id.substr(11,2)) - 1, this.id.substr(13, 2));
        calendarDateSub.setFullYear(selectedCell.getFullYear());
        calendarDateSub.setMonth(selectedCell.getMonth());
        showSubCalendar();
        if (document.getElementById('calendar_type').value === 'calendar') {
            showCalendar();
        } else {
            showWeekCalendar();
        }
    });

    //週カレンダーのドラッグに使う変数
    let isDragging = false;
    let startColIndex = null;
    let startRowIndex = null;

    function getColumnIndex(cell) {
        const table = document.getElementById("weekCalendar");
        const row = cell.parentElement;
        const cells = Array.from(row.children);
        return cells.indexOf(cell);
    }

    function selectCells(rowIndex) {
        const rows = document.getElementById("weekCalendar").rows;
        if (startRowIndex <= rowIndex) {
            for (i = startRowIndex; i <= rowIndex; i ++) {
                const cell = rows[i].children[startColIndex];
                cell.classList.add("drug");
            }
        } else if (startRowIndex > rowIndex) {
            for (i = rowIndex; i <= startRowIndex; i ++) {
                const cell = rows[i].children[startColIndex];
                cell.classList.add("drug");
            }
        }
    }

    function clearSelection() {
        const drugCells = document.querySelectorAll("td.drug");
        drugCells.forEach((cell) => cell.classList.remove("drug"));
    }

    function getRowFromYPosition(y) {
        const table = document.getElementById("weekCalendar");
        const rows = Array.from(table.rows);
        const firstRect = rows[2].getBoundingClientRect();
        const lastRect = rows[97].getBoundingClientRect();
        if (y <= firstRect.top) { //テーブルより上
            return 2;
        }
        for (let i = 2; i < rows.length; i ++) {
            const rect = rows[i].getBoundingClientRect();
            if (y > rect.top && y <= rect.bottom) {
                return i; // 行インデックスを返す
            }
        }
        if (y > lastRect.bottom) { //テーブルより下
            return 97;
        }
    }

    // マウスダウン時にドラッグを開始
    document.addEventListener("mousedown", (e) => {
        if (e.target.matches('#weekCalendar td')) {
            isDragging = true;
            clearSelection();
            startColIndex = getColumnIndex(e.target);
            startRowIndex = getRowFromYPosition(e.clientY);
            e.target.classList.add('drug');
        }
    });

    // マウス移動中も選択を更新
    document.addEventListener("mousemove", (e) => {
        if (isDragging) {
            clearSelection();
            const rowIndex = getRowFromYPosition(e.clientY);
            selectCells(rowIndex);
        }
    });

    // マウスアップ時にドラッグ終了
    document.addEventListener("mouseup", () => {
        if (isDragging) {
            isDragging = false;
            const drugCells = document.getElementsByClassName("drug");
            const firstDrugCell = drugCells[0];
            const lastDrugCell = drugCells[drugCells.length - 1];
            const reservationDate = firstDrugCell.dataset.date.slice(0, 4) + '年' + firstDrugCell.dataset.date.slice(4, 6) + '月' + firstDrugCell.dataset.date.slice(6, 8) + '日';
            document.getElementById("reservation_date").value = reservationDate;
            document.getElementById("checkin_time").value = firstDrugCell.dataset.time;
            if (lastDrugCell.dataset.time.slice(-2) !== '45') {
            	document.getElementById("checkout_time").value = lastDrugCell.dataset.time.slice(0, 2) + (parseInt(lastDrugCell.dataset.time.slice(-2)) + 15);
            } else {
            	document.getElementById("checkout_time").value = ("0" + (parseInt(lastDrugCell.dataset.time.slice(0, 2)) + 1)).slice(-2) + "00";
            }
            go_detail('reserve', '');
        }
    });

    //メニューの表示設定
    $(function() {
        $("ul.menu li").hover(
            function() {
                $(".menuSub:not(:animated)", this).slideDown();
            },
            function() {
                $(".menuSub", this).slideUp();
            }
        );
    });
</script>
</head>
<%
String actionCmd = (String) request.getParameter("action_cmd");
%>
<body>
    <form id="main_form" method="post" action="">
        <div id="container">

            <input type="hidden" name="form_name" id="form_name"value= "UserMenuHome" />
            <input type="hidden" name="action_cmd"id="action_cmd" value="" />
            <input type="hidden" name="main_key"id="main_key" value="" />
            <input type="hidden" name="main_cmd"id="main_cmd" value="" />
            <input type="hidden" name="sort_key_old"id="sort_key_old" value="<%=webBean.txt(" sort_key_old ")%>" />
            <input type="hidden" name="sort_key" id="sort_key" value="" />
            <input type="hidden" name="sort_order" id="sort_order"value="<%=webBean.txt(" sort_order ")%>" />
            <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt(" search_info ")%>" />
            <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt(" user_info_id ")%>" />
            <input type="hidden" name="file_name" id="file_name" value="<%=webBean.txt("file_name")%>" />
            <input type="hidden" name="file_id" id="file_id" value="<%=webBean.txt("file_id")%>"/>
            <input type="hidden" name="request_cmd" id="request_cmd" value=""/>
            <input type="hidden" name="date" id="date" value="<%= new java.util.Date() %>" />
            <input type="hidden" name="user_info_id" id="user_info_id" value="<%= String.join(",", webBean.txt("user_info_ids")) %>">
            <input type="hidden" name="user_name" id="user_name" value="<%= String.join(",", webBean.txt("user_names")) %>">
            <input type="hidden" name="selected_user_ids" id="selected_user_ids" value="<%= webBean.txt("selected_user_ids") %>">
            <input type="hidden" id="room_id" name="room_id" value="">
            <input type="hidden" id="reservation_date" name="reservation_date" value="">
            <input type="hidden" id="checkin_time" name="checkin_time" value="">
            <input type="hidden" id="checkout_time" name="checkout_time" value="">
            <input type="hidden" id="calendar_type" name="calendar_type" value="calendar">

            <header>
                <div id="title">
                    <h1>
                        <a href="javascript:void(0)" value="サイトトップ" onclick="go_menu('top')">会議室予約</a>
                    </h1>
                </div>
                <div id="subtitle">
                    <div id="month_button">
                        <button id="prev" type="button">＜</button><button id="next" type="button">＞</button><div id="current_month"></div>
                    </div>
                    <div id="user_menu">
                        <ul class="menu">
                            <li><i class="material-icons">today</i> カレンダー
                                <ul class="menuSub">
                                    <li onclick="changeCalendar('calendar')">月</li>
                                    <li onclick="changeCalendar('weekCalendar')">週</li>
                                    <li onclick="changeCalendar('dayCalendar')">日</li>
                                </ul>
                            </li>
                            <li><i class="material-icons">arrow_drop_down_circle</i> MENU
                                <ul class="menuSub">
                                    <li onclick="go_detail('reserve', '')">新規予約</li>
                                    <li onclick="go_file('file')">ファイル一覧</li>
                                    <li onclick="go_logout('logout')">ログアウト</li>
                                </ul>
                            </li>
                            <%
                                if (webBean.txt("admin").equals("admin")){
                            %>
                            <li><i class="material-icons">settings</i> <span class="control">管理画面</span>
                                <ul class="menuSub">
                                    <li onclick="go_detail('admin1', '');">管理者メニュー</li>
                                    <li onclick="window.location.href='UserMenu.do'">ホーム画面</li>
                                    <li onclick="window.location.href='UserLogin.do'">ログイン画面（仮）</li>
                                    <li onclick="window.location.href='Calendar.do'">カレンダー</li>
                                </ul>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                    <div class="user-block">ログインユーザー : <%=webBean.txt("login_user_name")%></div>
                    </div>
                </div>
            </header>

            <div id="middle">
                <nav>
                    <div id="month_button_sub">
                        <div id="current_month_sub"></div>
                        <div id="month_buttons_sub">
                            <button id="prev_sub" type="button">＜</button><button id="next_sub" type="button">＞</button>
                        </div>
                    </div>
                    <div id="subCalendar"></div>
                    <div id="myCalendar"></div>
                </nav>
                
                <main id="calendar"></main>
            </div>
        </div>
        <footer>
            <div class="footmenu">
                <ul>
                    <li><a href="javascript:void(0)" onclick="go_detail('admin1', '');">HOME</a></li> <%--"go_submit('home')"→ "go_detail('admin1', '');"管理者メニューに遷移するように変更--%>
                </ul>
            </div>
            <!-- /.footmenu -->
            <div class="copyright">Copyright &#169; 2017 RayD Developer All
                Rights Reserved.</div>
            <!-- /.copyright -->
        </footer>
    </form>
</body>
</html>

