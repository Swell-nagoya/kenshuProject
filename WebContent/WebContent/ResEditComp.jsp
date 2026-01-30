<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<jsp:useBean id="reserveDao" class="jp.swell.dao.ReserveDao" scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8">
<title>編集完了確認</title>
<style>
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
    background-color: #f0f0f0;
}

.container {
    background-color: white;
    width: 800px;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    margin: 0 auto;
}

h1 {
    color: #333;
    border-bottom: 4px dotted #800080;
    padding-bottom: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

th, td {
    padding: 10px;
    border: 1px solid #ddd;
}

th {
    background-color: #f0e6f7;
    width: 50px;
}

td {
    width: 100px;
}

.buttons {
    margin-top: 20px;
    text-align: center;
}

.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    margin: 0 10px;
    text-decoration: none;
    display: inline-block;
}

.btn-primary {
    background-color: #4CAF50;
    color: white;
}
</style>
<script type="text/javascript">
    function go_menu(action_cmd) {
        // クリック時の更新対応
        document.getElementById('main_form').action = 'UserMenu.do';
        document.getElementById('action_cmd').value = action_cmd;
        document.getElementById('main_form').submit();
    }
</script>
</head>
<body>
<form id="main_form" method="post" action="">
    <div class="container">
        <h1>編集が完了しました</h1>
        <input type="hidden" name="form_name" id="form_name" value="UserYoyakuDetail" />
        <input type="hidden" name="action_cmd" id="action_cmd" value="" />
            <table>
                <tr>
                    <th>予約者</th>
                    <td><%= reserveDao.getUserName() %> 様</td>
                </tr>
                <tr>
                    <th>会議室</th>
                    <td><%= reserveDao.getRoomName() %></td>
                </tr>
                <tr>
                    <th>日付</th>
                    <td><%= reserveDao.getReservationDate() %></td>
                </tr>
                <tr>
                    <th>時刻</th>
                    <td><%= reserveDao.getCheckinTime() %> ～ <%= reserveDao.getCheckoutTime() %></td>
                </tr>
                <tr>
                    <th>色</th>
                    <td><input type="color" value="<%= reserveDao.getColor() %>" disabled></td>
                </tr>
                <tr>
                    <th>テキスト</th>
                    <td><%= reserveDao.getInputText() %></td>
                </tr>
                <tr>
                    <th>備考</th>
                    <td><%= reserveDao.getInputRemark() %></td>
                </tr>
                <tr>
                    <th>最終変更者</th>
                    <td><%= reserveDao.getUpdateUserName() %> 様</td>
                </tr>
            </table>
        <div class="buttons">
            <a href="javascript:void(0)" onclick="go_menu('top')" class="btn btn-primary">一覧に戻る</a>
        </div>
    </div>
</form>
</body>
</html>