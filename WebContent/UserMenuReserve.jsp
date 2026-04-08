<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="jp.patasys.common.http.WebBean"%>

<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/jquery.timepicker.min.css">
<link rel="stylesheet" href="jquery-ui/jquery-ui.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.timepicker.min.js"></script>
<script type="text/javascript" src="js/colorchange.js"></script>
<script type="text/javascript" src="js/replace1.js"></script>
<script type="text/javascript" src="js/replace2.js"></script>
<script type="text/javascript" src="js/reservedate.js"></script>
<script type="text/javascript" src="js/datePicker.js"></script>

<title>新規予約</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 20px;
	background-color: #f0f0f0;
	display: flex;
	justify-content: center;
}

.container {
	background-color: white;
	width: 800px;
	padding: 20px;
	border-radius: 5px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #333;
	border-bottom: 4px dotted #800080;
	padding-bottom: 10px;
}

.errors {
	color: #f00;
}

.messages {
	color: #00f;
}

.style_head3 {
	margin-bottom: 20px;
	font-size: 18px;
	font-weight: bold;
	text-align: center;
}

th, td {
	padding: 10px;
	border: 1px solid #ddd;
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

input.reserve_id {
	width: 100px;
}

input.error {
	color: #FF0000;
	background-color: #FFCCCC;
	border: 1px solid #FF0000;
}

select.error {
	color: #FF0000;
	background-color: #FFCCCC;
	border: 1px solid #FF0000;
}

span.error, div.error {
	color: #FF0000;
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

.buttons {
	margin-top: 20px;
	text-align: center;
}
/* ボタンの設定 */
.btn {
	padding: 10px 18px;
	border: none;
	border-radius: 3px;
	cursor: pointer;
	margin: 0 10px;
	height: 40px
}
/* OKボタンの設定 */
.btn-primary {
	background-color: #4CAF50;
	color: white;
	font-size: 15px;
}
/* キャンセルボタンの設定 */
.btn-secondary {
	background-color: #f44336;
	color: white;
}

.vertical {
	padding: 10px;
	-ms-writing-mode: tb-rl;
	writing-mode: vertical-rl;
}
/* アイコン設定 */
.material-icons {
	font-size: 13px;
	margin-right: 10px;
	font-weight: bold;
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
    //  document.getElementById('main_form').submit(); クリック時、ボタンを押すと更新かかるため、コメントアウト対応
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

    document.addEventListener('DOMContentLoaded', function() {
        const colorInput = document.getElementById('rgb_color_input');
        const colorBoxes = document.querySelectorAll('.color-box');
        const errorElement = document.getElementById('error_rgb_color');


        // カラーパレットの色がクリックされたときの処理
        colorBoxes.forEach(box => {
            box.addEventListener('click', function() {
                // クリックされたボックスの data-color 属性から選択された色の値を取得
                const selectedColor = box.getAttribute('data-color');
                // colorInput に選択された色を反映させ、選択内容を表示
                colorInput.value = selectedColor;

                // 選択された色が存在し、かつ #87ceeb ではない時、エラーメッセージを消し、error クラスを削除
                if (selectedColor && selectedColor !== '#87ceeb') {
                    colorInput.classList.remove('error'); // エラークラス削除
                    errorElement.textContent = ''; // エラーメッセージ非表示
                }
            });
        });

        // 色入力フィールドが変更されたときの処理
        colorInput.addEventListener('input', function() {
            // ユーザーが選んだ色 (colorInput.value) を hiddenColorInput に反映
            hiddenColorInput.value = colorInput.value;
            // 入力された色が存在し、かつ #87ceeb というデフォルト色ではない時、エラーメッセージを消し、error クラスを削除
            if (colorInput.value && colorInput.value !== '#87ceeb') {
                colorInput.classList.remove('error'); // エラークラス削除
                errorElement.textContent = ''; // エラーメッセージ非表示
            }
        });
    });

    $(document).ready(function() {
        // 予約日、チェックイン時間、チェックアウト時間の入力フィールドで入力が行われた時に関数を実行
        $('#reservation_date_input, #checkin_time_input, #checkout_time_input').on('input', function() {
            // 現在の入力フィールドの name 属性を fieldName 変数に格納し、値を value 変数に格納
            var fieldName = $(this).attr('name');
            var value = $(this).val();

            // 現在のフィールドが reservation_date、checkin_time、または checkout_time のいずれかである場合に、以下の処理を実行する条件を指定
            if (fieldName === 'reservation_date' || fieldName === 'checkin_time' || fieldName === 'checkout_time') {
                if (isNumeric(value)) { // 数字であるかどうかを判断
                    $(this).removeClass('error'); // クラス削除
                    $('#error_' + fieldName).text(''); // エラーメッセージ非表示
                }
            }
        });

        // 会議室選択と予約者選択の選択フィールドで選択が変更されるたびに関数を実行
        $('#room_id_input, #user_info_id_input').on('change', function() {
            // 現在の入力フィールドの name 属性を fieldName 変数に格納し、値を value 変数に格納
            var fieldName = $(this).attr('name');
            var value = $(this).val();

            // 選択された値が空でなく、会議室選択 または 選択してください でない場合に以下の処理を実行する条件を指定
            if (value && value !== '会議室選択' && value !== '選択してください') {
                $(this).removeClass('error'); // クラス削除
                $('#error_' + fieldName).text(''); // エラーメッセージ非表示
            }
        });
    });
    
    function isNumeric(value) {
        // 正規表現を使用して値が数字だけで構成されているかどうかをチェックし、その結果を返す
        return /^\d+$/.test(value); //^\d+$ は数字のみを許可する正規表現
    }
    

    // サブ画面処理
    function openUserWindow(action_cmd) {
        const selectedUserIds = document.getElementById('user_info_id').value;
        // コントローラー設定
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'UserYoyakuDetail.do';
        form.target = 'Reserve_User';
        // form_name設定
        const formNameInput = document.createElement('input');
        formNameInput.type = 'hidden';
        formNameInput.name = 'form_name';
        formNameInput.value = 'UserYoyakuDetail';
        form.appendChild(formNameInput);
        // アクションコマンド設定
        const actionCmdInput = document.createElement('input');
        actionCmdInput.type = 'hidden';
        actionCmdInput.name = 'action_cmd';
        actionCmdInput.value = action_cmd;
        form.appendChild(actionCmdInput);
        // ユーザー情報
        const selectedUsersInput = document.createElement('input');
        selectedUsersInput.type = 'hidden';
        selectedUsersInput.name = 'selected_user_ids';
        selectedUsersInput.value = selectedUserIds;
        form.appendChild(selectedUsersInput);

        document.body.appendChild(form);
        // サブ画面表示処理
        window.open('', 'Reserve_User', 'width=400,height=500');
        form.submit();

        document.body.removeChild(form);
    }

    function receiveSelectedUsers(users) {
        const selectedUsersDiv = document.getElementById('selected_users');
        selectedUsersDiv.innerHTML = '';
        const userIds = [];
        const userNames = [];
        users.forEach((user) => {
            const userDiv = document.createElement('div');
            userDiv.textContent = user.name;
            selectedUsersDiv.appendChild(userDiv);
            userIds.push(user.id);
            userNames.push(user.name);
        });
        // 隠しフィールドの値を更新
        document.getElementById('user_info_id').value = userIds.join(',');
        document.getElementById('user_name').value = userNames.join(',');
    }
    
    // エラー時hiddenフィールドからユーザー名を取得
    document.addEventListener("DOMContentLoaded", function() {
        var userNames = document.getElementById("user_name").value;
        // カンマで分割してリストに変換
        var userNamesList = userNames.split(",");
        // ユーザー名を表示する要素を取得
        var selectedUsersDiv = document.getElementById("selected_users");
        // ユーザー名をリストとして表示
        selectedUsersDiv.innerHTML = userNamesList.map(function(name) {
            return "<div>" + name + "</div>";
        }).join("");
    });
    </script>
</head>
<body>
	<%
     String val = webBean.txt("request_name");
     String actionType =  val.equals("戻る") ? "return" : "unknown";
   %>
	<form id="main_form" method="post" action=""
		enctype="multipart/form-data">
		<div class="container">
			<h1>新規予約</h1>
			<input type="hidden" name="form_name" id="form_name"
				value="UserYoyakuDetail" /> <input type="hidden" name="action_cmd"
				id="action_cmd" value="" /> <input type="hidden" name="main_key"
				id="main_key" value="" /> <input type="hidden" name="main_cmd"
				id="main_cmd" value="" /> <input type="hidden" name="request_cmd"
				id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> <input
				type="hidden" name="request_name" id="request_name"
				value="<%=webBean.txt("request_name")%>" /> <input type="hidden"
				name="reserveId" id="reserveId"
				value="<%=webBean.txt("reserve_id")%>" /> <input type="hidden"
				name="user_info_id" id="user_info_id"
				value="<%= String.join(",", webBean.txt("user_info_ids")) %>">
			<input type="hidden" name="user_name" id="user_name"
				value="<%= String.join(",", webBean.txt("user_names")) %>">

			<div class="style_head3">
				<div class="messages"><%=webBean.dispMessages()%></div>
				<div class="errors"><%=webBean.dispErrorMessages()%></div>
				<%
          // actionTypeが"ins"の場合のみ表示
          if ("return".equals(actionType)) { 
          %>
				<div class="errors">日時か部屋を変更してください。</div>
				<%
          }
          %>
			</div>
			<table>
				<tr>
					<th>日付</th>
					<td><input type="text" name="reservation_date"
						id="reservation_date_input"
						value="<%=webBean.txt("reservation_date")%>"
						class="reserve_id <%=webBean.dispErrorCSS("reservation_date")%>"
						readonly />
						<div id="error_reservation_date" class="error"><%=webBean.dispError("reservation_date")%></div>
					</td>
				</tr>
				<tr>
					<th>時刻</th>
					<td><input type="text" name="checkin_time"
						id="checkin_time_input" value="<%=webBean.txt("checkin_time")%>"
						class="reserve_id <%=webBean.dispErrorCSS("checkin_time")%>"
						data-time-format="H:i" readonly /> ～ <input type="text"
						name="checkout_time" id="checkout_time_input"
						value="<%=webBean.txt("checkout_time")%>"
						class="reserve_id <%=webBean.dispErrorCSS("checkout_time")%>"
						data-time-format="H:i" readonly />
						<div id="error_checkin_time" class="error"><%=webBean.dispError("checkin_time")%></div>
						<div id="error_checkout_time" class="error"><%=webBean.dispError("checkout_time")%></div>
					</td>
				</tr>
				<tr>
					<th>会議室</th>
					<td><select name="room_id" id="room_id_input"
						class="<%=webBean.dispErrorCSS("room_id")%>">
							<option>会議室選択</option>
							<% // データベースのroomが空でないかの確認
                            List roomsList = webBean.arrayList("rooms");
                            if (webBean.arrayList("rooms") != null && !webBean.arrayList("rooms").isEmpty()) {
                              // 部屋情報を取るためのループ処理
                              for (Object item : webBean.arrayList("rooms")) {
                                RoomDao room = (RoomDao) item;
                            %>

							<option value="<%= WebUtil.htmlEscape(room.getRoomId()) %>"
								<%= WebUtil.dispSelected(webBean.value("room_id"), room.getRoomId()) %>
								data-room-name="<%= WebUtil.htmlEscape(room.getRoomName()) %>">
								<%= WebUtil.htmlEscape(room.getRoomName()) %></option>
							<%
                            }
                                  } else { // 部屋情報がない場合
                            %>
							<option value="">部屋情報がありません</option>
							<%
                            }
                            %>
					</select>
						<div id="error_room_id" class="error"><%= webBean.dispError("room_id") %></div>

					</td>
				</tr>
				<tr>
					<th>テキスト</th>
					<td><input type="text" name="input_text" id="input_text_input"
						value="<%=webBean.txt("input_text")%>" /></td>
				</tr>
				<tr>
					<th>予約者（登録者）</th>
					<td>
						<div id="selected_users"></div> <input type="button" value="選択"
						name="user_info_id" id="user_info_id"
						value="<%=webBean.txt("user_info_ids")%>"
						onclick="openUserWindow('sub')" />
						<div id="error_user_info_id" class="error"><%= webBean.dispError("user_info_id") %></div>

					</td>
				</tr>
				<tr>
					<th>色</th>
					<td>現在の色：<input type="color"
						value="<%= webBean.txt("rgb_color").isEmpty() ? "#87ceeb" : webBean.txt("rgb_color") %>"
						name="rgb_color" id="rgb_color_input"
						class="<%=webBean.dispErrorCSS("rgb_color")%>"><br> <span
						id="error_rgb_color" class="error"><%= webBean.dispError("rgb_color") %></span>
						▼パレットから選択
						<div class="color-palette" id="color_palette">
							<div class="color-box" style="background-color: red;"
								data-color="#FF0000"></div>
							<div class="color-box" style="background-color: yellow;"
								data-color="#FFFF00"></div>
							<div class="color-box" style="background-color: lime;"
								data-color="#00FF00"></div>
							<div class="color-box" style="background-color: aqua;"
								data-color="#00FFFF"></div>
							<div class="color-box" style="background-color: blue;"
								data-color="#0000FF"></div>
							<div class="color-box" style="background-color: fuchsia;"
								data-color="#FF00FF"></div>
							<div class="color-box" style="background-color: maroon;"
								data-color="#800000"></div>
							<div class="color-box" style="background-color: olive;"
								data-color="#808000"></div>
							<div class="color-box" style="background-color: green;"
								data-color="#008000"></div>
							<div class="color-box" style="background-color: teal;"
								data-color="#008080"></div>
							<div class="color-box" style="background-color: navy;"
								data-color="#000080"></div>
							<div class="color-box" style="background-color: purple;"
								data-color="#800080"></div>
							<div class="color-box" style="background-color: black;"
								data-color="#000000"></div>
							<div class="color-box" style="background-color: gray;"
								data-color="#808080"></div>
							<div class="color-box" style="background-color: lightgray;"
								data-color="#d3d3d3"></div>
							<div class="color-box" style="background-color: white;"
								data-color="#FFFFFF"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th>備考</th>
					<td><textarea rows="4" name="input_remark"
							id="input_remark_input" value=""><%= webBean.txt("input_remark") %></textarea>
					</td>
				</tr>
				<tr>
					<th>アップロード</th>
					<td><input type="text" name="input_name" id="input_name"
						value="<%=webBean.txt("file_name")%>" class="ime_disabled"
						placeholder="ファイル名を入力" /> <input type="file" name="file" id="file"
						class="ime_disabled" /></td>
				</tr>
			</table>
			<div class="buttons">
				<button type="button" class="btn btn-primary"
					onclick="go_detail('reserve', '');">
					<i class="material-icons">trip_origin</i>OK
				</button>
				<button type="button" class="btn btn-secondary"
					onclick="window.location.href='UserMenu.do'">
					<i class="material-icons">close</i>キャンセル
				</button>
			</div>
		</div>
	</form>
</body>
</html>