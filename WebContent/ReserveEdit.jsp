<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="jp.patasys.common.http.WebBean" %>

<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
    scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/jquery.timepicker.min.css">
<link rel="stylesheet" href="jquery-ui/jquery-ui.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.timepicker.min.js"></script>
<script type="text/javascript" src="js/colorchange.js"></script>
<script type="text/javascript" src="js/replace2.js"></script>
<script type="text/javascript" src="js/datePicker.js"></script>

<title>予約編集</title>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: #f0f0f0;
    margin: 0;
    padding: 20px;
    display: flex;
    justify-content: center;
}

.edit-reservation {
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
}

h1 {
    color: #333;
    border-bottom: 4px dotted #800080;
    padding-bottom: 10px;
}

th, td {
    padding: 10px;
    border-bottom: 1px solid #ddd;
}

th {
    background-color: #f0e6f7;
    width: 150px;
}

input[type="text"], textarea {
    width: 90%;
    padding: 5px;
    border: 1px solid #ddd;
    border-radius: 3px;
}

select {
    width: 23%;
    padding: 5px;
    border: 1px solid #000;
    border-radius: 5px;
}

input.reserve_id {
    width: 100px;
}

input.error {
    color: #FF0000;
    background-color: #FFCCCC;
}

span.error {
    color: #FF0000;
}

.timestamp {
    font-size: 0.8em;
    color: #666;
}

.buttons {
    margin-top: 20px;
    text-align: center;
}

.button{
    width: 100px;
    height: 40px;
    padding: 0px 10px;
    margin: 0 10px;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    font-weight: bold;
}
/* 閉じるボタン設定 */
.close_button {
    background-color: #f0f0f0;
    color: #333;
}
/* 削除ボタン設定 */
.delete_button {
    background-color: #ff4136;
    color: white;
}
/* 編集ボタン設定 */
.update_button {
    background-color: #2ecc40;
    color: white;
}

.color-palette {
    display: flex;
    border: 1px solid #a9a9a9;
    width: fit-content;
}

.color-box {
    width: 20px;
    height: 20px;
    cursor: pointer;
}

.color-option {
    width: 20px;
    height: 20px;
    border: 1px solid #ddd;
    cursor: pointer;
}

.vertical {
    padding: 10px;
    -ms-writing-mode: tb-rl;
    writing-mode: vertical-rl;
}

select:disabled {
    opacity: 1; /* 通常の不透明度に設定 */
    color: #000; /* 文字色を黒に設定 */
    background-color: #fff; /* 背景色を白に設定 */
}
/* アイコン設定 */
.material-icons {
    margin-right: 5px; /* アイコンとテキストの間のスペース */
    vertical-align: middle;
}
</style>
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
//      document.getElementById('main_form').submit(); クリック時、ボタンを押すと更新かかるため、コメントアウト対応
    }
    function go_detail(action_cmd,main_key) {
        document.getElementById('main_form').action = 'UserYoyakuDetail.do';
        document.getElementById('action_cmd').value=action_cmd;
        document.getElementById('main_key').value=main_key;
        document.getElementById('main_form').submit();
    }
    $(function() {
        $("#reservation_date_input").datepicker();
        $("#reservation_date_input").on("change",function() {
            var value = $(this).val();
            var value1 = value.replaceAll("-","");
            $("#reservation_date").val(value1);
        });
    });
    // 予約者の名前を固定
    $(document).ready(function() {
        var $select = $('#user_info_id_input');
        var $input = $('#user_info_name_input');
        // 選択されたoption情報の取得
        var $option = $select.find('option:selected');
        // data-user-nameの値を取得
        $input.val($option.data('user-name'));
    });
    // 削除ボタンの確認処理
    function delete1(reserveId) {
        // 削除確認のアラートを表示
        if (confirm("本当に削除してもよろしいですか？")) {
            // ユーザーが「OK」をクリックした場合に処理を進める
            go_detail('delete', reserveId);
        } else {
            // ユーザーが「キャンセル」をクリックした場合に処理を中断する
            alert("削除がキャンセルされました");
        }
    }
    </script>
</head>
<body>
<form id="main_form" method="post" action="">
    <div class="edit-reservation">
            <input type="hidden" name="form_name" id="form_name"value=UserYoyakuDetail />
            <input type="hidden" name="action_cmd"id="action_cmd" value="" />
            <input type="hidden" name="main_key"id="main_key" value="" />
            <input type="hidden" name="main_cmd"id="main_cmd" value="" />
            <input type="hidden" name="reserveId" id="reserveId"value="<%=webBean.txt("reserve_id")%>" />
            <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
        <h1>予約編集</h1>
            <div class="errors"><%=webBean.dispErrorMessages()%></div>
        <table>
            <tr>
                <th>日時</th>
                <td>
                    <input type="" name="reservation_date"id="reservation_date_input" value="<%=webBean.txt("reservation_date")%>" class="reserve_id ime_active <%=webBean.dispErrorCSS("reservation_date")%>">
                     <br />
                         <span class="error"><%=webBean.dispError("reservation_date")%></span>
                </td>
            </tr>
            <tr>
                <th>時刻</th>
                <td>
                    <input type="text" name="checkin_time" id="checkin_time_input" value="<%=webBean.txt("checkin_time")%>" class="reserve_id ime_active <%=webBean.dispErrorCSS("checkin_time")%>"> 例：0900～1330 ※半角数字24時制表記 直接入力可
                    <br/>
                      <span class="error"><%=webBean.dispError("checkin_time")%> </span>
                    <div class="vertical">～</div>
                    <input type="text" name="checkout_time" id="checkout_time_input" value="<%=webBean.txt("checkout_time")%>" class="reserve_id ime_active <%=webBean.dispErrorCSS("checkout_time")%>">
                     <br />
                     <span class="error"><%=webBean.dispError("checkout_time")%> </span>
                </td>
            </tr>
            <tr>
                <th>会議室</th>
                <td>
                    <select name="room_id" id="room_id_input">
                        <% // データベースのroomが空でないかの確認
                        List roomsList = webBean.arrayList("rooms");
                        if (webBean.arrayList("rooms") != null && !webBean.arrayList("rooms").isEmpty()) {
                          // 部屋情報を取るためのループ処理
                          for (Object item : webBean.arrayList("rooms")) {
                            RoomDao room = (RoomDao) item;
                        %>
                            <option value="<%= WebUtil.htmlEscape(room.getRoomId()) %>" <%= WebUtil.dispSelected(webBean.value("room_id"), room.getRoomId()) %> data-room-name="<%= WebUtil.htmlEscape(room.getRoomName()) %>"> <%= WebUtil.htmlEscape(room.getRoomName()) %></option>
                        <%
                        }
                              } else { // 部屋情報がない場合
                        %>
                        <option value="">部屋情報がありません</option>
                        <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <th>予約者（登録者）</th>
                <td>
                    <select name="user_info_id" id="user_info_id_input" style="display:none;" >
                     <% // データベースのroomが空でないかの確認
                        if (webBean.arrayList("list") != null && !webBean.arrayList("list").isEmpty()) { %>
                        <% // 部屋情報を取るためのループ処理
                        for (Object item : webBean.arrayList("list")) {
                          UserInfoDao user = (UserInfoDao) item;
                        %>
                        <option value="<%= WebUtil.htmlEscape(user.getUserInfoId()) %>" <%= WebUtil.dispSelected(webBean.value("user_info_id"), user.getUserInfoId()) %> data-user-name="<%= WebUtil.htmlEscape(user.getLastName()) %> <%= WebUtil.htmlEscape(user.getMiddleName()) %> <%= WebUtil.htmlEscape(user.getFirstName()) %>"> <%= WebUtil.htmlEscape(user.getLastName()) %> <%= WebUtil.htmlEscape(user.getMiddleName()) %> <%= WebUtil.htmlEscape(user.getFirstName()) %></option>
                        <%
                        }
                              } else { // ユーザー情報がない場合
                        %>
                        <option value="">予約者情報がありません</option>
                        <%
                        }
                        %>
                    </select>
                    <input type="text" id="user_info_name_input" class="reserve_id" readonly>
                </td>
            </tr>
            <tr>
                <th>最終変更者</th>
                <td>
                    <select name="update_user_id" id="update_user">
                     <% // データベースのroomが空でないかの確認
                        if (webBean.arrayList("list") != null && !webBean.arrayList("list").isEmpty()) { %>
                        <% // 部屋情報を取るためのループ処理
                        for (Object item : webBean.arrayList("list")) {
                          UserInfoDao user = (UserInfoDao) item;
                        %>
                        <option value="<%= WebUtil.htmlEscape(user.getUserInfoId()) %>" <%= WebUtil.dispSelected(webBean.value("update_user_id"), user.getUserInfoId()) %> data-user-name="<%= WebUtil.htmlEscape(user.getLastName()) %> <%= WebUtil.htmlEscape(user.getMiddleName()) %> <%= WebUtil.htmlEscape(user.getFirstName()) %>"> <%= WebUtil.htmlEscape(user.getLastName()) %> <%= WebUtil.htmlEscape(user.getMiddleName()) %> <%= WebUtil.htmlEscape(user.getFirstName()) %></option>
                        <%
                        }
                              } else { // ユーザー情報がない場合
                        %>
                        <option value="">予約者情報がありません</option>
                        <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <th>色</th>
                <td>
                    現在の色：<input type="color" value="<%=webBean.txt("rgb_color")%>" name="rgb_color" id="rgb_color1"><br>
                    ▼パレットから選択
                    <div class="color-palette" id="color_palette">
                        <div class="color-box" style="background-color: red;" data-color="#FF0000"></div>
                        <div class="color-box" style="background-color: yellow;" data-color="#FFFF00"></div>
                        <div class="color-box" style="background-color: lime;" data-color="#00FF00"></div>
                        <div class="color-box" style="background-color: aqua;" data-color="#00FFFF"></div>
                        <div class="color-box" style="background-color: blue;" data-color="#0000FF"></div>
                        <div class="color-box" style="background-color: fuchsia;" data-color="#FF00FF"></div>
                        <div class="color-box" style="background-color: maroon;" data-color="#800000"></div>
                        <div class="color-box" style="background-color: olive;" data-color="#808000"></div>
                        <div class="color-box" style="background-color: green;" data-color="#008000"></div>
                        <div class="color-box" style="background-color: teal;" data-color="#008080"></div>
                        <div class="color-box" style="background-color: navy;" data-color="#000080"></div>
                        <div class="color-box" style="background-color: purple;" data-color="#800080"></div>
                        <div class="color-box" style="background-color: black;" data-color="#000000"></div>
                        <div class="color-box" style="background-color: gray;" data-color="#808080"></div>
                        <div class="color-box" style="background-color: lightgray;" data-color="#d3d3d3"></div>
                        <div class="color-box" style="background-color: white;" data-color="#FFFFFF"></div>
                    </div>
                </td>
            </tr>
            <tr>
                <th>テキスト</th>
                <td>
                    <input type="text" name="input_text" id="input_text_input" value="<%=webBean.txt("input_text")%>">
                </td>
            </tr>
            <tr>
                <th>備考</th>
                <td>
                    <textarea rows="4" name="input_remark" id="input_remark_input" value=""><%=webBean.txt("input_remark")%></textarea>
                </td>
            </tr>
            <tr>
                <th>アップロード</th>
                <td>
                    <input type="text" name="input_name" id="input_name" value="<%=webBean.txt("file_name")%>" class="ime_disabled" placeholder="ファイル名を入力"/>
                    <input type="file" name="file" id="file" class="ime_disabled">
                </td>
            </tr>
        </table>
        <div class="buttons">
            <button type="button" class="button close_button" onclick="window.location.href='UserMenu.do'"><i class="material-icons">cancel</i>閉じる</button>
            <button type="button" class="button delete_button" onclick="delete1 ('<%=webBean.txt("reserve_id")%>');"><i class="material-icons">delete</i>削除</button>
            <button type="button" class="button update_button" onclick="go_detail('edit', '<%=webBean.txt("reserve_id")%>');"><i class="material-icons">sync_alt</i>編集</button>
        </div>
    </div>
</body>
</html>