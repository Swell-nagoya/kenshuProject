<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.ScheduleDao"%>
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
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>

<title>ユーザー閲覧管理ページ</title>
<style type="text/css">

body {
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
  margin: 0;
  padding: 10px;
}

header {
  position: relative;
  background: #00bcd4; /* ヘッダーの背景色 */
  width: 100%; /* 幅を画面いっぱいに */
  height: 70px;
  margin-bottom: 5px; /* 不要な余白を排除 */
  text-align: center; /* テキスト中央寄せ */
}

h1 {
  font-size: 50px;
  color: white; /* リンクの文字色を白に */
  text-decoration: none; /* 下線を削除 */
  font-weight: normal;
}

.container {
  position: relative; /* ボタンを基準に配置するため */
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 90%; /* コンテナの幅を画面幅に揃える */
  margin: 20px auto; /* 中央寄せ */
}

.left {
  margin-bottom: 20px;
  text-align: center;
}

.list_table td {
  border-collapse: collapse;
  border: 1px #a0a0a0 solid;
  padding: 2px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  
}

th {
  background-color: #f2f2f2;
  padding: 10px;
}

td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  border: 1px #a0a0a0 solid;
}

.list_title {
  background-color: #f7f7f7;
}

.search_label {
  background-color: #00bcd4;
  color: white;
}

.list_label {
  font-weight: bold;
  font-size: 1.2em;
  background-color: #00bcd4;
  color: white;
  border: 1px #a0a0a0 solid;
  text-align: left;
}

.main_label {
  font-weight: bold;
  font-size: 1.2em;
  background-color: #00bcd4;
  color: white;
  border: 1px #a0a0a0 solid;
  text-align: center;
  vertical-align: middle;
}

.main_user{
  font-weight: bold;
  font-size: 1.2em;
  border: 1px #a0a0a0 solid;
  text-align: center;
  vertical-align: middle;
}

.list_text {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 左右に要素を配置 */
}

a {
  color: white; /* リンクの色を白に */
  text-decoration: none; /* 下線を消す */
  text-align: left;
}

a:hover {
  color: white; /* マウスが乗ったときの色変更を防ぐ */
  text-decoration: none; /* マウスホバー時の下線を消す */
}

.search_text input {
  text-align: left;
  border-radius: 5px;
}

/* ボタンの共通スタイル */
input[type="button"] {
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  cursor: pointer; /* カーソルをポインタにする */
  background: #90a0b0; /* デフォルトの背景色 */
}

/* ホバー時のスタイル */
input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.new-btn {
  position: absolute;
  right: 10px; /* 右端に10pxの余白を取る */
  top: 5px;   
}

/* .new-btnのスタイル */
.new-btn input {
  background: #fff; /* 背景色を白に */
  color: #000; /* 文字色を黒に */
}

 .button {
  display: flex;
  justify-content: center;
  align-items: center;
}

.button input[type="button"] {
  padding: 0px 50px; /* ボタンの内側余白 */
  font-size: 24px; /* 文字サイズ */
  border: 2px solid #fff; /* ボタンの枠線 */
  background-color: #00bcd4; /* 上書きの背景色 */
}

.button input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

.pagenation {
  text-align: center;
  margin-top: 20px;
}

.list_text input[type="checkbox"] {
  margin-right: 10px; 
}

.list_tr {
  background-color: #fff; 
}



</style>
  <script type="text/javascript">
<%--検索条件入力でenterキーが押された場合の処理--%>
jQuery(function($)
{
  $(".select_table input").keydown(function (e)
  {
    if(e.which == 13)
    {
        go_submit('search');
    }
  });
  $(".page_table input").keydown(function (e)
  {
    if(e.which == 13)
    {
        go_submit('jump');
    }
  });
});

let checkedUsers = [];

//ページロード時の初期化処理
window.onload = function () {
    // サーバーから受け取ったメインユーザーIDを取得（主にロックされるチェックボックス用）
    const selectedMainUserId = document.getElementById("user_info_id_input").getAttribute("value"); 
    
    // サーバーから受け取った選択されたユーザーIDの生データを取得
    const rawSelectedUserIds = '<%=webBean.txt("selected_user_ids")%>'; // カンマ区切りのユーザーID
    const selectedUserIds = rawSelectedUserIds ? rawSelectedUserIds.split(',').map(id => id.trim()) : []; // 配列に変換

    // サーバーから受け取った選択されたユーザーの優先度データを取得
    const rawPriorities = '<%=webBean.txt("selected_user_priorities")%>'; // カンマ区切りの優先度
    const priorities = rawPriorities ? rawPriorities.split(',').map(priority => priority.trim()) : []; // 配列に変換

    // `priorities` 配列のインデックスを追跡するための変数
    let priorityIndex = 0;

    // ユーザーのチェックボックスを操作するロジック
    const checkboxes = document.querySelectorAll('input[name="selected_user_ids"]'); // 全ての対象チェックボックスを取得
    checkboxes.forEach(checkbox => {
        // メインユーザーのチェックボックスを自動的にチェックし、無効化
        if (checkbox.value === selectedMainUserId) {
            checkbox.checked = true;
            checkbox.disabled = true; // メインユーザーは変更不可
        }

        // サーバーから受け取った選択済みユーザーIDに対応するチェックボックスを操作
        for (let i = 0; i < selectedUserIds.length; i++) {
            if (selectedUserIds[i] === checkbox.value) {
                // 選択済みのチェックボックスをチェック状態にする
                checkbox.checked = true;

                // チェックボックスに関連するセレクトボックスを表示
                const prioritySelect = checkbox.closest('td').querySelector('.priority-select');
                if (prioritySelect) {
                    prioritySelect.style.display = 'block'; // セレクトボックスを表示

                    // 優先度の値をセレクトボックスに反映
                    const priority = checkbox.closest('td').querySelector('.priority');
                    priority.value = priorities[i]; // 優先度を設定

                    // セレクトボックスの変更イベントを設定（優先度を変更した際に配列を更新）
                    addPriorityChangeListener(checkbox, priority);
                }
            }
        }
    });

    // サーバーから受け取った隠しフィールドの要素を取得
    const hiddenUserIds = document.getElementById('selected_user_ids');
    const hiddenUserNames = document.getElementById('selected_user_names');
    const hiddenUserPriorities = document.getElementById('selected_user_priorities');

    // 隠しフィールドに値があれば、選択済みユーザーの情報を`checkedUsers`に格納
    if (hiddenUserIds && hiddenUserIds.value) {
        const hiddenUserIdsValue = hiddenUserIds.value; // 隠しフィールドからユーザーIDを取得
        const hiddenUserNamesValue = hiddenUserNames ? hiddenUserNames.value : ''; // 名前フィールド
        const hiddenUserPrioritiesValue = hiddenUserPriorities ? hiddenUserPriorities.value : ''; // 優先度フィールド

        // それぞれの値を配列に変換
        const ids = hiddenUserIdsValue.split(',');
        const names = hiddenUserNamesValue.split(',');
        const priorities = hiddenUserPrioritiesValue.split(',');

        // `checkedUsers`配列に選択済みユーザー情報を構築
        checkedUsers = ids.map((id, index) => ({
            id: id,
            name: names[index] || '', // 名前が無い場合は空文字
            priority: priorities[index] || '', // 優先度が無い場合は空文字
        }));
    }

    // チェックボックスの状態が変化したときのイベントリスナーを追加
    document.querySelectorAll('input[name="selected_user_ids"]').forEach((checkbox) => {
        checkbox.addEventListener('change', (event) => {
            if (event.target.checked) {
                addUserToChecked(event.target); // チェックが入った場合、配列に追加
            } else {
                removeUserFromChecked(event.target); // チェックが外れた場合、配列から削除
            }
        });
    });
};

//セレクトボックス変更時のリスナーを追加する関数
function addPriorityChangeListener(checkbox, prioritySelect) {
    prioritySelect.addEventListener('change', function () {
        let updatedPriority = prioritySelect.value; // 新しい優先度の値を取得

        // 配列の該当するユーザーの優先度を更新
        const userIndex = checkedUsers.findIndex((user) => user.id === checkbox.value);
        if (userIndex !== -1) {
            checkedUsers[userIndex].priority = updatedPriority;
        }
    });
}


//チェックされたユーザーを配列に追加または更新
function addUserToChecked(checkbox) {
  const selectedMainUserId = document.getElementById("user_info_id_input").getAttribute("value");

  if (checkbox.value === selectedMainUserId) return; // メインユーザーはスキップ

  // ユーザー名の取得
  let name = checkbox.getAttribute('data-user-name').replace(/&nbsp;/g, ' ').replace(/\s+/g, ' ').trim();
  
  // 優先度セレクトボックスの取得
  let prioritySelect = checkbox.closest('td')?.querySelector('.priority');
  
  // 優先度の値を取得
  let priority = prioritySelect ? prioritySelect.value : ''; 
  
  // ユーザー配列を更新または追加
  const userIndex = checkedUsers.findIndex((user) => user.id === checkbox.value);
  if (userIndex === -1) {
     checkedUsers.push({ id: checkbox.value, name: name, priority: priority });
  } else {
     checkedUsers[userIndex] = { id: checkbox.value, name: name, priority: priority }; // 更新
  }

  // セレクトボックスの変更を監視
  if (prioritySelect) {
    prioritySelect.addEventListener('change', function() {
      let updatedPriority = prioritySelect.value; // 新しい優先度の値を取得
      
      // 配列の該当するユーザーの優先度を更新
      const userIndex = checkedUsers.findIndex((user) => user.id === checkbox.value);
      if (userIndex !== -1) {
        checkedUsers[userIndex].priority = updatedPriority;
      }
    });
  }
}

//チェックが外れたユーザーを配列から削除
function removeUserFromChecked(checkbox) {
 checkedUsers = checkedUsers.filter((user) => user.id !== checkbox.value);
}


function submitSelection(actionCmd, main_key) {
 const selectedMainUserId = document.getElementById("user_info_id_input").getAttribute("value"); 
 const selectedMainUserName = document.getElementById("user_info_id_input").innerText.trim();
 const selectedUsers = [];

 // hiddenフィールドの値が空でない場合、checkedUsers配列から選択されたユーザーを追加
 const hiddenUserIds = document.getElementById('selected_user_ids').value;
 if (hiddenUserIds) {
     // 既存の選択されたユーザー情報があれば、それを使ってselectedUsersに追加
     checkedUsers.forEach((user) => {
         selectedUsers.push({
             id: user.id,
             name: user.name,
             priority: user.priority
         });
     });
 } else {
     // hiddenフィールドが空であれば、チェックされたチェックボックスの値をそのまま使用
     document.querySelectorAll('input[name="selected_user_ids"]:checked').forEach((checkbox) => {
         if (checkbox.value === selectedMainUserId) {
             return; // メインユーザーIDと一致する場合はスキップ
         }

         let name = checkbox.getAttribute('data-user-name').replace(/&nbsp;/g, ' ').replace(/\s+/g, ' ').trim();
         let prioritySelect = checkbox.closest('td').querySelector('.priority-select select');
         let priority = prioritySelect ? prioritySelect.value : ''; // 優先度がなければ空文字

         selectedUsers.push({
             id: checkbox.value,
             name: name,
             priority: priority
         });
     });
 }

 // hiddenフィールドに選択したユーザーのIDを追加
 document.getElementById('selected_user_ids').value = selectedUsers.map(user => user.id).join(',');
 document.getElementById('selected_user_names').value = selectedUsers.map(user => user.name).join(',');
 document.getElementById('selected_user_priorities').value = selectedUsers.map(user => user.priority).join(',');

 // メインユーザーのIDを設定
 document.getElementById('user_info_id').value = selectedMainUserId;
 document.getElementById('main_user_name').value = selectedMainUserName;

 document.getElementById('main_form').action = 'Schedule.do';
 document.getElementById('action_cmd').value = actionCmd;
 document.getElementById('main_key').value = main_key;

 // フォームを送信
 document.getElementById('main_form').submit();
}

  function go_submit(action_cmd)
  {
    document.getElementById('main_form').action='Schedule.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('main_form').submit();
  }
  function go_submit(action_cmd,main_key)
  {
    document.getElementById('main_form').action='Schedule.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
  }
  function go_sort_request(key)
  {
    document.getElementById('sort_key').value=key;
    document.getElementById('action_cmd').value='sort';
    document.getElementById('main_form').submit();
  }
  function go_menu(action_cmd)
  {
      document.getElementById('main_form').action='Schedule.do';
      document.getElementById('action_cmd').value=action_cmd;
      document.getElementById('main_form').submit();
  }
  function go_detail_1(action_cmd,request_cmd,main_key)
  {
    document.getElementById('main_form').action='Schedule.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
  }
  function go_detail(action_cmd,request_cmd)
  {
    document.getElementById('main_form').action='Schedule.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
  }
  function copyToClipboard(str) {

      navigator.clipboard.writeText(str)
  }


  const scheduleData = [
      <% for (Object item : webBean.arrayList("scheduleDaos")) {
          ScheduleDao schedule = (ScheduleDao) item;
      %>
      {
          mainUserId: "<%= WebUtil.htmlEscape(schedule.getMainUserId()) %>",
          linkUserId: "<%= WebUtil.htmlEscape(schedule.getLinkUserId()) %>",
          priority: "<%= WebUtil.htmlEscape(schedule.getPriority()) %>"
      },
      <% } %>
  ];

  //ページのロード時とセレクトボックス変更時に実行される処理をまとめた関数
  function handleUserSelection() {
    // 現在選択されているユーザーIDを取得
    const selectedMainUserId = document.getElementById("user_info_id_input").getAttribute("value"); 

    // すべてのチェックボックスを取得
    const checkboxes = document.querySelectorAll('input[name="selected_user_ids"]');

    // すべてのチェックボックスの無効化を解除し、チェックを外す
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = false;
        checkbox.disabled = false; // 無効化解除
        // 関連するセレクトタグを初期化
        const prioritySelect = checkbox.closest('td')?.querySelector('.priority-select');
        if (prioritySelect) {
            prioritySelect.style.display = 'none'; // 非表示に戻す
            const priorityDropdown = prioritySelect.querySelector('.priority');
            if (priorityDropdown) {
                priorityDropdown.value = "1"; // オプションをリセット（初期化）
            }
        }
    });

    // 選択されたユーザーIDに基づいて、チェックボックスと優先度を設定
    scheduleData.forEach(schedule => {
        if (schedule.mainUserId === selectedMainUserId) {
            // メインユーザーIDに一致するチェックボックスを無効化
            checkboxes.forEach(checkbox => {
                if (checkbox.value === selectedMainUserId) {
                    checkbox.disabled = true;
                }
                // リンクユーザーIDに一致するチェックボックスを有効化し、優先度を設定
                if (checkbox.value === schedule.linkUserId) {
                    checkbox.checked = true;
                    const prioritySelect = checkbox.closest('td')?.querySelector('.priority-select');

                    if (checkbox.value === selectedMainUserId) {
                        prioritySelect.style.display = 'none';
                    } else if (prioritySelect) {
                        prioritySelect.style.display = 'block';
                        const priority = checkbox.closest('td').querySelector('.priority');
                        priority.value = schedule.priority;
                        
                    } else {
                        // チェックボックスがチェックされていない場合、セレクトボックスを非表示
                        prioritySelect.style.display = 'none';
                    }
                }
            });
        }
    });
}

//ページロード時に実行
  window.addEventListener('DOMContentLoaded', (event) => {
    const hiddenUserIds = document.getElementById('selected_user_ids');
    
    // hiddenUserIds が存在し、その値が空のときだけ handleUserSelection を実行
    if (hiddenUserIds && hiddenUserIds.value === '') {
      handleUserSelection(); // ページが読み込まれた時に初期設定を実行
    }
  });

  
//一括選択チェックボックスの切り替え
  function toggleAllUsers(masterCheckbox) {
      const isChecked = masterCheckbox.checked; // マスターチェックボックスの状態を取得
      const userCheckboxes = document.querySelectorAll('input[name="selected_user_ids"]');

      // 各ユーザーチェックボックスを一括で操作
      userCheckboxes.forEach(checkbox => {
          checkbox.checked = isChecked;
          togglePrioritySelect(checkbox); // プライオリティセレクトも更新
      });
  }

  // 各ユーザーチェックボックスの変更処理
  function togglePrioritySelect(checkbox) {
    const selectedMainUserId = document.getElementById("user_info_id_input").getAttribute("value"); 
      const prioritySelect = checkbox.closest('td')?.querySelector('.priority-select');
      if (checkbox.value === selectedMainUserId) {
          prioritySelect.style.display = 'none';
      } 
      else if (prioritySelect) {
          prioritySelect.style.display = checkbox.checked ? 'block' : 'none';
      }

      // 「全てのユーザーを選択する」の状態を更新
      updateSelectAllState();
  }

  // 「全てのユーザーを選択する」の状態をチェックして更新
  function updateSelectAllState() {
      const masterCheckbox = document.getElementById('select_all');
      const userCheckboxes = document.querySelectorAll('input[name="selected_user_ids"]');
      const allChecked = Array.from(userCheckboxes).every(cb => cb.checked);

      // 全てチェックされていればマスターをチェック、1つでも外れたら外す
      masterCheckbox.checked = allChecked;
  }
  
</script>
</head>
<body>
  <div class="container">
    <div class="new-btn">
      <input type="button" value="　戻る　" onclick="go_menu('menu')" />
    </div>
<header>
    <h1>スケジュール管理 </h1>
</header>
    <form id="main_form" method="post" action="">

      <input type="hidden" name="form_name" id="form_name" value="Schedule" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" />
      <input type="hidden" name="main_key" id="main_key" value="" />
      <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" />
      <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" />
      <input type="hidden" name="sort_key_old" id="sort_key_old" value="<%=webBean.txt("sort_key_old")%>" />
      <input type="hidden" name="sort_key" id="sort_key" value="" />
      <input type="hidden" name="sort_order" id="sort_order" value="<%=webBean.txt("sort_order")%>" />
      <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt("search_info")%>" />
      <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt(" user_info_id")%>"> 
      <input type="hidden" id="main_user_name" name="main_user_name" value="<%=webBean.txt("main_user_name")%>">
      <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
      <input type="hidden" name="selected_user_ids" id="selected_user_ids" value="<%=webBean.txt("selected_user_ids")%>" /> 
      <input type="hidden" name="selected_user_names" id="selected_user_names" value="<%=webBean.txt("selected_user_names")%>" />
      <input type="hidden" name="selected_user_priorities" id="selected_user_priorities" value="<%=webBean.txt("selected_user_priorities")%>" />

        <div class="left">
          <table>
            <tr>
              <td class="main_label ">メインユーザー</td>
            </tr>
            <tr class="list_tr">  
              <td class="main_user" name="user_info_id" id="user_info_id_input" value="<%=webBean.txt("user_info_id")%>"> <%=webBean.txt("main_user_name")%> </td>
            </tr>
          </table>
          <table class="select_table">
            <tr>
              <td class="search_label center" style="width: 50%">氏名</td>
              <td class="search_label center" style="width: 50%"></td>
            </tr>
            <tr class="list_tr">
              <td class="search_text center">
                <input type="text" name="list_search_full_name" id="list_search_full_name" size="35" maxlength="100" value="<%=webBean.txt("list_search_full_name")%>" class="ime_active <%=webBean.dispErrorCSS("list_search_full_name")%>" placeholder="検索"/>
                <%=webBean.dispError("list_search_full_name")%>
              </td>
              <td  style="text-align: center; vertical-align: middle;">
                <input type="button" value="検索" onclick="submitSelection('search', document.getElementById('user_info_id_input').value)" /> 
                <input type="button" value="クリア" onclick="submitSelection('clear', document.getElementById('user_info_id_input').value)" />
              </td>
            </tr>
          </table>

          <%
          if (webBean.arrayList("users").size() > 0) {
          %>
          <div class="pagenation">
            〚全<%=webBean.html("recordCount")%>件〛<br />
          </div>
          <table class="list_table">
            <tr class="list_title">
              <td class="list_label"><a href="javaScript:go_sort_request('last_name_kana')">閲覧可能にするユーザーを選択してください</a>
              </td>
            </tr>
            <tr class="list_tr">
              <td class="list_text">
                <div>
                  <!-- マスターチェックボックス -->
                  <input type="checkbox" id="select_all" onclick="toggleAllUsers(this)"> 全てのユーザーを選択する
                </div>               
              </td>
            </tr>
            <%
            // データベースの users が空でないかの確認
            if (webBean.arrayList("list") != null && !webBean.arrayList("list").isEmpty()) {
            %>
            <%
            // ユーザー情報を取るためのループ処理
            for (Object item : webBean.arrayList("list")) {
                UserInfoDao user = (UserInfoDao) item;
                String selectedUserId = webBean.txt("user_info_id"); // セレクトボックスで選択されたユーザーID
            %>
            <tr class="list_tr">
              <td class="list_text">
                <div>
                  <input type="checkbox" name="selected_user_ids" value="<%=WebUtil.htmlEscape(user.getUserInfoId())%>" data-user-name="<%=WebUtil.htmlEscape(user.getFullName())%>" onclick="togglePrioritySelect(this)"> 
                    <%=WebUtil.htmlEscape(user.getFullName())%>
                </div>
                <div class="priority-select" style="display:none;">
                  <select class="priority" name="linked_user_select" data-user-id="<%= WebUtil.htmlEscape(user.getUserInfoId()) %>">
                    <option value="1">普通</option>
                    <option value="2">やや高い</option>
                    <option value="3">高い</option>
                  </select>※表示する時の優先度を選択して下さい
                </div>
              </td>
            </tr>
            <%
            }
            } else // ユーザー情報がない場合
            {
            %>
            <tr>
              <td>ユーザー情報がありません</td>
            </tr>
            <%
            }
            %>
          </table>
          <%
          }
          %>
        </div>
        <div class="button" >
          <input type="button" value="確定" onclick="submitSelection('go_next', document.getElementById('user_info_id_input').value)" />
        </div>
    </form>
  </div>
</body>
</html>