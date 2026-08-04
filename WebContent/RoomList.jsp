<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.swell.dao.ReserveDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
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
<script type="text/javascript" src="js/common-page.js"></script>
<link rel="stylesheet" type="text/css" href="css/common-page.css">
<title>部屋情報一覧</title>
<style type="text/css">
body {
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
  margin: 0;
  padding: 10px;
}
input[type="checkbox"],
select {
 cursor: pointer;
}

header {
  position: relative;
  background: #00bcd4; /* ヘッダーの背景色 */
  width: 100%; /* 幅を画面いっぱいに */
  margin-bottom: 5px; /* 不要な余白を排除 */
  text-align: center; /* テキスト中央寄せ */
}

h1 a {
  font-size: 1.5em;
  color: white; /* リンクの文字色を白に */
  text-decoration: none; /* 下線を削除 */
  font-weight: normal;
}

h1 a:hover {
  color: #4baea8; /* ホバー時に下線を表示する場合 */
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

/* ボタンの共通スタイル */
input[type="button"] {
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  cursor: pointer; /* カーソルをポインタにする */
  background: #90a0b0; /* デフォルトの背景色 */
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

/* ホバー時のスタイル */
input[type="button"]:hover {
  background-color: #4baea8; /* ホバー時の背景色 */
}

table {

  width : 100%;
  margin : 0px;
  padding : 0px;
  border-collapse : collapse;
  border-spacing: 0px;
  border: 0px #808080 solid;
}

td {
  height:1.8em;
  border-color: #404040;
  border-collapse: collapse;
}

.table-wrap {
  overflow: auto;
  max-height: 500px;
  border-top: 1px #a0a0a0 solid;
  border-bottom: 1px #a0a0a0 solid;
}
.table-wrap > table {
  width: 100%;
  min-width: 800px;
}

.select_table tr,
.list_table tr {
  padding: 0;
}
.select_table th,
.select_table td,
.list_table th,
.list_table td {
  border: 1px #a0a0a0 solid;
  border-collapse: collapse;
  border-spacing: 0;
}

.select_table input[type="radio"] {
  margin: 3px 0 0 5px;
}
.select_table input[type="radio"] + label {
  display: inline-block;
  margin: 3px 0 0 0;
  padding-left: 5px;
}


.select_table input[type="radio"] + label:has() {
  letter-spacing: -.4em;
}
.select_table input[type="radio"] + label:has() > * {
  letter-spacing: normal;
}
.select_table input[type="checkbox"],
.select_table input[type="radio"],
.select_table input[type="radio"] + label {
  cursor: pointer;
}

.search_label {
  padding: 4px 10px;
  background: #00bcd4;
  color: #fff;
  text-align: center;
  font-size: 16px;
  font-weight: normal;
}

.search_label { /* 部屋、表示件数 */
  background: #00bcd4;
  color: #fff;
  text-align: center;
}

.search_text,.search_line,.list_btn {
  text-align: center;
}

.search_text input {
  text-align: left;
  border-radius: 5px;
}

.search_line input {
  text-align: right;
  border-radius: 5px;
}

.pagenation,.select_table {
  margin-bottom: 10px;
}

.select_table td {
  border-collapse: collapse;
  border: 1px #a0a0a0 solid;
  padding : 2px;
}

.list_label { /* 部屋、表示件数 */
  background: #00bcd4;
  color: #fff;
  text-align: center;
}

.list_label a {
  color: #fff;
  text-decoration: none;
}

.list_table td{
  border-collapse: collapse;
  border: 1px #a0a0a0 solid;
  padding : 2px;
}

#pageNo {
  text-align: center;
  border-radius: 5px;
}


.list_tr:nth-child(even) {
  background: #fff;
}

.list_tr:nth-child(odd) {
  background: #efefef;
}

footer {
  width: 100%;
}
.button_area {
  margin-top: 20px;
}
.button_area > [class^="button_"] {
  margin: 10px 10px 0 10px !important;
}
input[type="button"] {
    margin: 2px;
    border-radius: 10px;
    color: #fff;
    cursor: pointer;
    background: #90a0b0;
}
.button_send,
input[type="button"].button_send {
  display: inline-block;
  margin-top: 20px;
  padding: 5px 25px;
  border-radius: 18px;
  color: #000;
  cursor: pointer;
  background: #fff;
  font-weight: 500;
  font-size: 14px;
  border: 2px solid #ff7f50;
  transition: background 0.3s ease-in-out;
}
.button_send:hover,
input[type="button"].button_send:hover {
    background-color: #ff7f50;
    color: #fff;
}

/*.list_table*/
.list_table .fixed {
  position: sticky;
  top: 0;
  z-index: 1;
}
.list_table th {
  padding: 0;
}
.list_table td {
  padding: 5px 10px;
}
.list_table > thead > tr > th {
  font-size: 16px;
}
.table-wrap .list_table > thead > tr > th {
  border-top: none;
}
.table-wrap .list_table > tbody > tr:last-child td {
  border-bottom: none;
}
.list_table input[type="checkbox"] {
  cursor: pointer;
}
.list_table input[type="button"] {
  margin: 3px 2px;
}

.list_table thead > tr > .statas,
.list_table tbody > tr > .statas {
  width: 12%;
}
.list_table thead > tr > .full_name,
.list_table tbody > tr > .full_name {
  width: 22%;
}
.list_table thead > tr > .full_name_kana,
.list_table tbody > tr > .full_name_kana {
  width: 22%;
}
.list_table thead > tr > .memail,
.list_table tbody > tr > .memail {
  width: 22%;
}
.list_table thead > tr > .search_button,
.list_table tbody > tr > .search_button {
  width: 22%;
}

.list_title  {
  border-left: 1px #a0a0a0 solid;
  border-right: 1px #a0a0a0 solid;
}

.list_label {
  padding: 3px 7px;
  background: #00bcd4;
  color: #fff;
  text-align: center;
  font-weight: normal;
}

.list_label a {
  color: #fff;
  text-decoration: none;
}
.list_tr:nth-child(odd) {
  background: #efefef;
}

.list_tr:nth-child(even) {
  background: #fff;
}
</style>
<script type="text/javascript">
<%--検索条件入力でenterキーが押された場合の処理--%>
/*
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
*/
<%--テーブルを一行ごとにいろを変える--%>
 /*
  $(document).ready(function(){
        $('table.list_table tr:even').addClass('even');
        $('table.list_table tr:odd').addClass('odd');
  });
  */
/*
  $(function(){
	  new StatusFun();
  });
*/
  function go_submit(action_cmd)
  {
    document.getElementById('main_form').action='RoomList.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('main_form').submit();
  }
  function go_submit_statusUpdate(action_cmd,main_key)
  {
    document.getElementById('main_form').action='RoomList.do';
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
  function go_menu(action_cmd) {
      document.getElementById('main_form').action = 'UserMenu.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('main_form').submit();
  }
  function go_detail_1(action_cmd,request_cmd,main_key,before_name)
  {
    document.getElementById('main_form').action='RoomDetail.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('before_name').value=before_name;
    document.getElementById('main_form').submit();
  }
  function go_detail_2(action_cmd,request_cmd,main_key,room_name)
  {
    document.getElementById('main_form').action='RoomDetail.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('room_name').value=room_name;
    document.getElementById('main_form').submit();
  }
  function go_detail(action_cmd,request_cmd)
  {
    document.getElementById('main_form').action='RoomDetail.do';
    document.getElementById('action_cmd').value=action_cmd;
    document.getElementById('request_cmd').value=request_cmd;
    document.getElementById('main_form').submit();
  }

  // サブ画面処理
  function openRoomYoyakuWindow(action_cmd, room_id, room_name) {
      const selectedRoomId = room_id;
      const selectedRoomName = room_name;
      // コントローラー設定
      const form = document.createElement('form');
      form.method = 'POST';
      form.action = 'RoomYoyakuList.do';
      form.target = 'Reserve_Room';
      // form_name設定
      const formNameInput = document.createElement('input');
      formNameInput.type = 'hidden';
      formNameInput.name = 'form_name';
      formNameInput.value = 'RoomYoyakuList';
      form.appendChild(formNameInput);
      // アクションコマンド設定
      const actionCmdInput = document.createElement('input');
      actionCmdInput.type = 'hidden';
      actionCmdInput.name = 'action_cmd';
      actionCmdInput.value = action_cmd;
      form.appendChild(actionCmdInput);
      // 部屋のID
      const roomIdInput = document.createElement('input');
      roomIdInput.type = 'hidden';
      roomIdInput.name = 'main_key';
      roomIdInput.value = selectedRoomId;
      form.appendChild(roomIdInput);
      // 部屋の名前
      const roomNameInput = document.createElement('input');
      roomNameInput.type = 'hidden';
      roomNameInput.name = 'room_name';
      roomNameInput.value = selectedRoomName;
      form.appendChild(roomNameInput);

      document.body.appendChild(form);
      // サブ画面表示処理
      window.open('', 'Reserve_Room', 'width=600,height=600');
      form.submit();

      document.body.removeChild(form);
  }
/*
  // メンテナンス中　checkboxの変更
  class StatusFun {

    constructor() {
      // メンテナンス中　checkboxの共通クラス名
      this.statusCheckClassName = "js-status_check";
      // メンテナンス中　selectの共通クラス名
      this.statusClassName = "js-status";
      // メンテナンス中　checkboxの取得
      this.$statusCheck = $('.' + this.statusCheckClassName );
      // メンテナンス中　selectの取得
      this.$status = $('.' + this.statusClassName );
      // メンテナンス中　checkboxのid名の共通接頭値
      this.beforeNameSet = "status_";
      // メンテナンス中のcheckboxとselectがページに存在する時
      if( ( this.$status.length > 0 ) && 
    	  ( this.$statusCheck.length > 0 )){
    	  // 初期値の読み込み
          this.init();
      }
    }
    // 初期値
    init() {
        this.event();
    }
    // イベント処理
    event(){
        // メンテナンス中 checkboxの値に変化があった時
        $(this.$statusCheck).on("change",(e) =>{
            let target = e.target;
            if( target ){
              let targetValue = target.defaultValue;
              let statusTargetIdName = this.beforeNameSet + targetValue;
              let $statusTarget = $("#" + statusTargetIdName );

                // メンテナンス中 selectの値を変更する
                // checkboxのチェックを入っている時、selectは「8」を代入する
                if(target.checked){
                    $statusTarget[0].value = 8;

                // checkboxのチェックがない時、selectは「1」を代入する
                } else {
                    $statusTarget[0].value = 1;
                 }
           }
        });

        // メンテナンス中 selectの値に変化があった時
        $(this.$status).on("change",(e)=>{
          let target = e.target;
          if(target) {
            let targetValue = target.value;
            let targetId = target.id;
            let roomId = targetId.replace(this.beforeNameSet, '');
            

            // メンテナンス中 checkboxの値を変更する
            for(let i = 0; i < this.$statusCheck.length; i++){
             if(this.$statusCheck[i].value === roomId){
                // valueが「8」の時、checkboxのチェックを入れる
               if(targetValue === "8"){
                 this.$statusCheck[i].checked = true;
               // valueが「8」の時、checkboxのチェックを外す
               } else {
                 this.$statusCheck[i].checked = false;
               }
             }
            }
          }
        });
        
    }
  }
*/
</script>
</head>
<body>
   <div class="container">
    <div class="new-btn">
      <input type="button" value="新規登録" onclick="go_detail('go_next','ins')" />
      <input type="button" value="　戻る　" onclick="go_submit('return')" />
    </div>
  <header>
    <h1>
        <a href="javascript:void(0)" value="" onclick="go_menu('top')">部屋情報一覧</a>
    </h1>
  </header>
  <form id="main_form" method="post" action="">
      <input type="hidden" name="form_name" id="form_name" value="RoomList"/>
      <input type="hidden" name="action_cmd" id="action_cmd" value=""/>
      <input type="hidden" name="request_cmd" id="request_cmd" value=""/>
      <input type="hidden" name="main_key" id="main_key" value=""/>
      <input type="hidden" name="room_name" id="room_name" value="<%=webBean.txt("room_name")%>" />
      <input type="hidden" name="before_name" id="before_name" value="<%=webBean.txt("before_name")%>" />
      <input type="hidden" name="sort_key_old" id="sort_key_old" value="<%=webBean.txt("sort_key_old")%>"/>
      <input type="hidden" name="sort_key" id="sort_key" value=""/>
      <input type="hidden" name="sort_order" id="sort_order" value="<%=webBean.txt("sort_order")%>"/>
      <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt("search_info")%>"/>
      <input type="hidden" name="room_id" id="room_id" value="<%=webBean.txt("room_id")%>"/>
      <input type="hidden" name="room_id_show_all" id="room_id_show_all" value="<%=webBean.txt("room_id_show_all")%>"/>
      <div class="left">
        <div class="messages">
          <%=webBean.dispMessages()%>
        </div>
        <div class="errors">
          <%=webBean.dispErrorMessages()%>
        </div>
        <table class="select_table">
          <tr>
            <th class="search_label center" style="width: 10%">メンテ<br>ナンス中</th>
            <th class="search_label center" style="width: 50%">部屋名</th>
            <th class="search_label center" style="width: 20%">表示件数</th>
            <th class="search_label center" style="width: 20%"></th>
          </tr>
          <tr>
            <td class="search_text center">
              <input type="checkbox"  name="list_search_status"  id="list_search_status" value="8" class="search_active <%=webBean.dispErrorCSS("list_search_status")%>" 
              <% if("8".equals(webBean.txt("list_search_status"))) { %> checked<% } %> /> 
            </td>
            <td class="search_text center">
              <input type="text" name="list_search_room_name" id="list_search_room_name" size="30" maxlength="100" value="<%=webBean.txt("list_search_room_name") %>" class="ime_active <%=webBean.dispErrorCSS("list_search_room_name")%>" placeholder="検索"/>
              <%=webBean.dispError("list_search_room_name")%>
            </td>
            <td class="search_line center">
              <input type="text" name="lineCount" id="lineCount" size="2" maxlength="5" value="<%=webBean.txt("lineCount") %>" class="right ime_disabled" />件
            </td>
            <td style="text-align: center; vertical-align: middle;">
              <input type="button" value="検索" onclick="go_submit('search')" />
              <input type="button" value="クリア" onclick="go_submit('clear')" />
            </td>
          </tr>
        </table>
        <%if(webBean.arrayList("list").size()>0){%>
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
        <table class="list_table">
          <tr class="list_title">
            <th style="width: 10%" class="list_label js-table_sort_label<%= "status".equals(webBean.txt("sort_key_old")) ? ("desc".equals(webBean.txt("sort_order")) ? " is-desc" : " is-asc") : "" %>">
            <a href="javaScript:go_sort_request('status')">メンテ<br>ナンス中</a>
            </th>
            <th style="width: 54%" class="list_label js-table_sort_label<%= "room_name".equals(webBean.txt("sort_key_old")) ? ("desc".equals(webBean.txt("sort_order")) ? " is-desc" : " is-asc") : "" %>">
            <a href="javaScript:go_sort_request('room_name')">部屋名</a>
            </th>
            <th style="width: 18%" class="list_label js-table_sort_label<%= "status".equals(webBean.txt("sort_key_old")) ? ("desc".equals(webBean.txt("sort_order")) ? " is-desc" : " is-asc") : "" %>">
            <a href="javaScript:go_sort_request('status')">利用ステータス</a>
            </th>
            <th class="list_label" style="width: 18%"></th>
          </tr>
          <%
          for(Object item : webBean.arrayList("list"))
          {
              RoomDao dao = (RoomDao)item;
              
          %>
          <tr class="list_tr">
            <td class="list_text">
              <input type="checkbox" id="status_flg_<%=WebUtil.txtEscape(dao.getRoomId())%>"  class="js-status_check" name="list_status_flg" value="<%=WebUtil.txtEscape(dao.getRoomId())%>" <% if(dao.getStatus() == 8){ %> checked<%}%>>
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getRoomName())%>
            </td>
            <td class="list_text">
              <select id="status_<%=WebUtil.txtEscape(dao.getRoomId())%>" class="js-status" name="list_status_<%=WebUtil.txtEscape(dao.getRoomId())%>" >
                <option value="1" <% if(dao.getStatus() == 1){ %> selected<%}%>>利用可能</option>
                <option value="8" <% if(dao.getStatus() == 8){ %> selected<%}%>>メンテナンス中</option>
              </select>
            </td>
            <td class="list_btn">
              <input type="button" value="編集" onclick="go_detail_1('go_next','update','<%=WebUtil.txtEscape(dao.getRoomId())%>','<%=WebUtil.txtEscape(dao.getRoomName())%>');" />
              <input type="button" value="予約確認" onclick="openRoomYoyakuWindow('reservationConfirmation', '<%=WebUtil.txtEscape(dao.getRoomId())%>','<%=WebUtil.txtEscape(dao.getRoomName())%>');" />
              <input type="button" value="利用ステータス保存" onclick="go_submit_statusUpdate('statusUpdate', '<%=WebUtil.txtEscape(dao.getRoomId())%>');" />
              <input type="button" value="削除" onclick="go_detail_2('go_next','delete','<%=WebUtil.txtEscape(dao.getRoomId())%>','<%=WebUtil.txtEscape(dao.getRoomName())%>');" />
            </td>
          </tr>
          <%}%>
        </table>
        
        <div class="button_area">
          <input type="button" value="全選択" class="button_check_all js-check_all"" data-target="js-status_check" />
          <input type="button" value="一括登録" class="button_send" onclick="go_submit('statusUpdateAll');" />
        </div>
        <%} else { %>
          <p>ユーザー情報がありません</p>
        <%} %>
      </div>
    </form>
  </div>
</body>
</html>
