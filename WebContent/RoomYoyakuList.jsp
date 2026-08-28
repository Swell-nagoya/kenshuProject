<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<% // データベースの reserve が空でないかの確認
 String htmlTableString = "";
 if (webBean.arrayList("list") != null && !webBean.arrayList("list").isEmpty()) {
  // ユーザー情報を取るためのループ処理
  for (Object item : webBean.arrayList("list")) {ReserveDao roomYoyaku = (ReserveDao) item;

  String roomYoyakuDate = roomYoyaku.getReservationDate();

  //予約　年月日の取得.
  String roomYoyakuYear = roomYoyakuDate.substring(0, 4);
  String roomYoyakuMonth = roomYoyakuDate.substring(4, 6);
  String roomYoyakuDay = roomYoyakuDate.substring(6, 8);
  String formatDate = roomYoyakuYear + "/" + roomYoyakuMonth + "/" + roomYoyakuDay;
  
  // チェックイン、チェックアウト
  String checkinTime = roomYoyaku.getCheckinTime();
  String checkoutTime = roomYoyaku.getCheckoutTime();
  
  String formatCheckinTimeHour = "";
  String formatCheckinTimeMin = "";
  String formatCheckinTime = "";
  
  String formatCheckoutTimeHour = "";
  String formatCheckoutTimeMin = "";
  String formatCheckoutTime = "";
  
  // 予約時刻を代入（チェックイン）.
  if (checkinTime != null && checkinTime.length() >= 4){
   formatCheckinTimeHour  = checkinTime.substring(0, 2);
   formatCheckinTimeMin   = checkinTime.substring(2, 4);
   //チェックインの時間「XX:XX」
   formatCheckinTime = formatCheckinTimeHour + ":" + formatCheckinTimeMin;
  }

  // 予約時刻を代入（チェックアウト）.
  if (checkoutTime != null && checkoutTime.length() >= 4){
   formatCheckoutTimeHour  = checkoutTime.substring(0, 2);
   formatCheckoutTimeMin   = checkoutTime.substring(2, 4);
   //チェックアウトの時間「XX:XX」
   formatCheckoutTime = formatCheckoutTimeHour + ":" + formatCheckoutTimeMin;
  }

  // 外側で変数を宣言（Object型にすることで、日時と日付の両方に対応）
  Object checkinDateTime = null;
  Object checkoutDateTime = null;

  // --- チェックイン日時の設定 ---
  if (checkinTime != null && checkinTime.length() >= 4){
   // 値がある場合は「LocalDateTime」
   checkinDateTime = java.time.LocalDateTime.of(
     Integer.parseInt(roomYoyakuYear),
     Integer.parseInt(roomYoyakuMonth),
     Integer.parseInt(roomYoyakuDay),
     Integer.parseInt(formatCheckinTimeHour),
     Integer.parseInt(formatCheckinTimeMin)
   );
  } else {
   // 値がない場合は時間・分なしの「LocalDate」
   checkinDateTime = java.time.LocalDate.of(
     Integer.parseInt(roomYoyakuYear),
     Integer.parseInt(roomYoyakuMonth),
     Integer.parseInt(roomYoyakuDay)
   );
  }

  // --- チェックアウト日時の設定 ---
  if (checkoutTime != null && checkoutTime.length() >= 4){
   // 値がある場合は「LocalDateTime」
   checkoutDateTime = java.time.LocalDateTime.of(
     Integer.parseInt(roomYoyakuYear),
     Integer.parseInt(roomYoyakuMonth),
     Integer.parseInt(roomYoyakuDay),
     Integer.parseInt(formatCheckoutTimeHour),
     Integer.parseInt(formatCheckoutTimeMin)
   );
  } else {
   // 値がない場合は時間・分なしの「LocalDate」
   checkoutDateTime = java.time.LocalDate.of(
     Integer.parseInt(roomYoyakuYear),
     Integer.parseInt(roomYoyakuMonth),
     Integer.parseInt(roomYoyakuDay)
   );
  }


  // --- 現在日時との比較判定処理 ---
  boolean isBefore = false;
  if (checkinDateTime instanceof java.time.LocalDateTime) {
      // 時間がある場合：現在の「日時」と比較
      isBefore = java.time.LocalDateTime.now().isBefore((java.time.LocalDateTime) checkinDateTime);
  } else if (checkinDateTime instanceof java.time.LocalDate) {
      // 時間がない場合：現在の「日付」と比較（時間・分は一切なし）
      isBefore = java.time.LocalDate.now().isBefore((java.time.LocalDate) checkinDateTime);
  }


  // 判定結果（isBefore）を使ってクラス名を切り替え
  htmlTableString += "<tr class=\"" + (isBefore ? "before" : " now") + "\">"
  + "<td style='text-align: left;'>"
  + "【日付】" + formatDate + "<br>";
  // 時間はDBに登録があれば、表示する
  if ((checkinTime != null && !checkinTime.isEmpty()) || (checkoutTime != null && !checkoutTime.isEmpty())) {
    htmlTableString += "【時間】";
    if (checkinTime != null && !checkinTime.isEmpty()) {
       htmlTableString += formatCheckinTime;
    }
    if ((checkinTime != null && !checkinTime.isEmpty()) && (checkoutTime != null && !checkoutTime.isEmpty())) {
       htmlTableString += " - ";
    }
    if (checkoutTime != null && !checkoutTime.isEmpty()) {
       htmlTableString += formatCheckoutTime;
    }
       htmlTableString += "<br>";
  }


  htmlTableString += "【ユーザー名】" + WebUtil.htmlEscape(roomYoyaku.getFullName()) + "<br>"
+ "【部屋名】" + WebUtil.htmlEscape(roomYoyaku.getRoomName()) + "<br>"
+ "【テキスト】" + WebUtil.htmlEscape(roomYoyaku.getInputText()) + "<br>"
+ "</td>"
+ "</tr>";
      
  }
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>部屋予約状況</title>
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
.pagenation, .select_table {
    margin-bottom: 10px;
}
.pagenation {
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
function submitSelection() {
    const selectedUsers = [];
    // チェックされたチェックボックスを全て取得
    document.querySelectorAll('input[name="user_info_id"]:checked').forEach((checkbox) => {
        let name = checkbox.getAttribute('data-user-name');
        // ミドルネームがない場合、不要な&nbsp;を除去
        name = name.replace(/&nbsp;/g, ' ').replace(/\s+/g, ' ').trim();
        // ユーザーIDと名前を追加
        selectedUsers.push({
            id: checkbox.value,
            name: name
        });
    });
    // 親ウィンドウに選択したユーザー情報を送信
    window.opener.receiveSelectedUsers(selectedUsers);
    window.close();
}

//ウィンドウが読み込まれた時に実行される処理
window.onload = function() {
    // サーバーから受け取った選択されたユーザーIDの生データを取得
    const rawSelectedUserIds = '<%=webBean.txt("selected_user_ids")%>';
    // 受け取ったユーザーIDをカンマ区切りで分割して配列に変換
    const selectedUserIds = rawSelectedUserIds ? rawSelectedUserIds.split(',') : [];

    // チェックボックスを選択するロジック
    const checkboxes = document.querySelectorAll('input[name="user_info_id"]');
    // 選択されたユーザーIDがチェックボックスの値と一致する場合、チェックを入れる
    checkboxes.forEach(checkbox => {
        if (selectedUserIds.includes(checkbox.value)) {
            checkbox.checked = true; // 値が一致する場合、チェックを入れる
        }
    });
};



//カレンダーのセルにクリックイベントを追加
$(document).on('click', '.btn-primary', function(e) {
	const today = new Date();

	const year = today.getFullYear();

	// 月を2桁にする (0から始まるので+1)
	const month = String(today.getMonth() + 1).padStart(2, '0');

	// 日を2桁にする
	const day = String(today.getDate()).padStart(2, '0');

    const formattedDate = year + "年" + month + "月" + day + "日";
     // hidden フィールドに値をセット
    $('#reservation_date').val(formattedDate);
     //予約画面に移行
    go_detail('reserve');
});

function go_detail(action_cmd) {
	// フォームの送信先を親ウィンドウの名前に設定
	if (window.opener && !window.opener.closed) {
	 if (!window.opener.name) {
	    window.opener.name = "parentWindow_" + Date.now();
	  }
	    
	  document.getElementById('main_form').target = window.opener.name;
	}

	document.getElementById('main_form').action = 'UserMenu.do';
	document.getElementById('action_cmd').value = action_cmd;
	document.getElementById('main_form').submit();
	window.close();
}
function go_submit(action_cmd)
{
  document.getElementById('main_form').action='RoomYoyakuList.do';
  document.getElementById('action_cmd').value=action_cmd;
  document.getElementById('main_form').submit();
}
</script>
</head>
<body>
<div class="container">
  <h1>部屋予約状況</h1>
  <form id="main_form" method="post" action="">
     <input type="hidden" id="form_name" name="form_name" value="RoomYoyaku"/>
     <input type="hidden" id="action_cmd" name="action_cmd" value=""/>
     <input type="hidden" id="reservation_date" name="reservation_date" value=""/>
     <input type="hidden" id="main_key" name="main_key" value="<%=webBean.txt("main_key")%>" />
     <input type="hidden" id="previous_page" name="previous_page" value="RoomYoyakuList" />
    <%
      if (webBean.arrayList("list").size() > 0) {
    %>
    <div class="pagenation">
      <input type="text" name="pageNo" id="pageNo" maxlength="3" size='1' value="<%=webBean.txt("pageNo")%>" class="right ime_disabled" />  /
      <%=webBean.html("maxPageNo")%> ページ〚全
      <%=webBean.html("recordCount")%>件〛<br/>
      <%if(!"1".equals(webBean.value("pageNo"))){%> <input type="button" value="<--前の<%=webBean.html("lineCount")%>件" onclick="go_submit('prior')" />
      <%}else{%>
      <%}%>
      <input type="button" value="ページ表示" onclick="go_submit('jump')" />
      <%if(!webBean.value("pageNo").equals(webBean.value("maxPageNo"))){%> <input type="button" value="次の<%=webBean.html("lineCount")%>件-->" onclick="go_submit('next')" />
      <%}else{%>
      <%}%>
     </div>
      <%}%>
     <%
      if (htmlTableString != null && !htmlTableString.trim().isEmpty()) {
     %>
     <table>
       <%= htmlTableString %>
     </table>
    <%
     // 予約がない.または、過去の予約のみの時
     } else {
    %>
      <p>予約はありません</p>
    <%
     }
    %>   
 <div class="buttons">
   <input type="button" value="新規予約" class="btn btn-primary js-btn-primary">
   <input type="button" value="閉じる" onclick="window.close()" class="btn btn-close">
 </div>
  </form>
</div>
</body>
</html>