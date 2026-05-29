<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.ScheduleDao"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>ユーザー選択</title>
<style>
body
{
  margin:0px;
  padding : 0px;
  border: 0px;
  font-size: 80%;
  line-height: 1.2;
  letter-spacing: 0;
  font-family: "Lucida Grande", "Lucida Sans Unicode", "Hiragino Kaku Gothic Pro", "ヒラギノ角ゴ Pro W3", "メイリオ", Meiryo, "ＭＳ Ｐゴシック", Helvetica, Arial, Verdana, sans-serif;
  height: 100%;
  width : 100%;
  background: #f0f0f0;
}

/* ボタンの共通スタイル */
.select-btn {
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  cursor: pointer; /* カーソルをポインタにする */
  background: #90a0b0; /* デフォルトの背景色 */
}

/* ホバー時のスタイル */
.select-btns:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.container {
  background-color: white;
  width: 300px;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin: 0 auto;
}

table
{
  width : 100%;
  margin : 0px;
  padding : 0px;
  border-collapse : collapse;
  border-spacing: 0px;
  border: 0px #808080 solid;
}

h1 {
    color: #333;
    border-bottom: 4px dotted #800080;
    padding-bottom: 10px;
}

td {
    padding: 10px;
    border-bottom: 1px solid #ddd;
    text-align: center;
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
    margin: 10px 10px;
    text-decoration: none;
    display: inline-block;
}

.btn-primary {
    background-color: #4CAF50;
    color: white;
}

</style>
<script>
function go_submit(action_cmd) {
    // 選択されたユーザーIDを取得する配列
    let selectedIds = [];

    // チェックされたチェックボックスを全て取得
    document.querySelectorAll('input[name="user_info_id"]:checked').forEach((checkbox) => {
        selectedIds.push(checkbox.value);  // ユーザーIDを追加
    });
    // 親ウィンドウに選択したユーザー情報を送信
    if (window.opener && !window.opener.closed) 
    {
         window.opener.receiveSelectedIds(selectedIds.join(',')); // 配列をカンマ区切りの文字列に変換
    }
    window.close(); // サブウィンドウを閉じる
}

//ウィンドウが読み込まれた時に実行される処理
 window.onload = function() {
    // サーバーから受け取った選択されたユーザーIDの生データを取得
    const rawSelectedUserIds = '<%=webBean.txt("selected_user_ids")%>';
    const selectedUserIds = rawSelectedUserIds ? rawSelectedUserIds.split(',') : [];
    
    // サーバーサイドからMainUserIdを取得
    const mainUserId = '<%= webBean.txt("user_info_id") %>';  // MainUserIdを取得
    
    // チェックボックスを選択するロジック
    const checkboxes = document.querySelectorAll('input[name="user_info_id"]');
    
    let anySelected = false;  // 選択されたチェックボックスがあるかどうかを判定するフラグ
    
    // 選択されたユーザーIDがチェックボックスの値と一致する場合、チェックを入れる
    checkboxes.forEach(checkbox => {
        if (selectedUserIds.includes(checkbox.value)) {
            checkbox.checked = true; // 値が一致する場合、チェックを入れる
            anySelected = true;  // 1つでも選択された場合、フラグを立てる
        }
    });
    
    // selectedUserIdsに一致するチェックボックスが1つも選択されていなかった場合
    if (!anySelected) {
        // LinkUserId（checkboxのvalue）とMainUserIdを比較してチェックを入れる
        checkboxes.forEach(checkbox => {
            const linkUserId = checkbox.value;  
            
            if (linkUserId === mainUserId) {
                checkbox.checked = true; // MainUserIdと一致すればチェックを入れる
            }
        });
    }
};

//一括選択チェックボックスの切り替え
function toggleAllUsers(masterCheckbox) {
    const isChecked = masterCheckbox.checked; // マスターチェックボックスの状態を取得
    const userCheckboxes = document.querySelectorAll('input[name="user_info_id"]');

    // 各ユーザーチェックボックスを一括で操作
    userCheckboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
    });
}

// 「全てのユーザーを選択する」の状態をチェックして更新
function updateSelectAllState(checkbox) {
    const masterCheckbox = document.getElementById('select_all');
    const userCheckboxes = document.querySelectorAll('input[name="user_info_id"]');
    const allChecked = Array.from(userCheckboxes).every(cb => cb.checked);

    // 全てチェックされていればマスターをチェック、1つでも外れたら外す
    masterCheckbox.checked = allChecked;
}

</script>
</head>
<body>
<div class="container">
  <form id="main_form" method="post">
    <h1>ユーザー選択</h1>
    
    <input type="hidden" name="form_name" id="form_name" value="UserSelect" />
    <input type="hidden" name="action_cmd" id="action_cmd" value="" />
    <input type="hidden" name="selected_user_names" id="selected_user_names" value="" />
    <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt("user_info_id")%>">
    <input type="hidden" name="selected_user_ids" id="selected_user_ids" value="<%= webBean.txt("selected_user_ids") %>">
  
        <table>
            <tr >
              <td  style="text-align: left;">
                <!-- マスターチェックボックス -->
                <input type="checkbox" id="select_all" onclick="toggleAllUsers(this)"> 全てのユーザーを選択する
              </td>
            </tr>
             <% // データベースの users が空でないかの確認
                 if (webBean.arrayList("scheduleDaos") != null && !webBean.arrayList("scheduleDaos").isEmpty()) { %>
                <% // ユーザー情報を取るためのループ処理
                for (Object item : webBean.arrayList("scheduleDaos")) {
                  ScheduleDao schedule = (ScheduleDao) item;
                  UserInfoDao user = schedule.getLinkUserInfoDaos();
                %>
            <tr>
              <td style="text-align: left;">
                <input type="checkbox" name="user_info_id" value="<%= WebUtil.htmlEscape(user.getUserInfoId()) %>" data-user-name="<%= WebUtil.htmlEscape(user.getFullName()) %>" onclick="updateSelectAllState(this)">
                <%= WebUtil.htmlEscape(user.getFullName()) %>
              </td>
            </tr>
                <%
                }
                } else { // ユーザー情報がない場合
                %>
            <tr><td>ユーザー情報がありません</td></tr>
                <%
                }
                %>
        </table>
        <div class="buttons">
          <input type="button" value="選択" onclick="go_submit('go_next')" class="btn btn-primary">
        </div>
  </form>
</div>
</body>
</html>