<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.RoomDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="jp.swell.constant.RoomState"%>
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
<title>部屋情報一覧</title>
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

.list_tr:nth-child(odd) {
  background: #efefef;
}

footer {
width: 100%;
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
<%--テーブルを一行ごとにいろを変える--%>
  $(document).ready(function(){
        $('table.list_table tr:even').addClass('even');
        $('table.list_table tr:odd').addClass('odd');
  });
  function go_submit(action_cmd)
  {
    document.getElementById('main_form').action='RoomList.do';
    document.getElementById('action_cmd').value=action_cmd;
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
  function go_reserve_confirm(room_id)
  {
    document.getElementById('main_form').action = 'ReserveList.do';
    document.getElementById('form_name').value = 'ReserveList';
    document.getElementById('action_cmd').value = 'search';
    document.getElementById('room_id').value = room_id;
    document.getElementById('main_form').submit();
  }
  function go_reserve_new(room_id)
  {
    document.getElementById('main_form').action = 'UserYoyakuDetail.do';
    document.getElementById('form_name').value = 'UserYoyakuDetail';
    document.getElementById('action_cmd').value = 'reserveFromRoom';
    document.getElementById('room_id').value = room_id;
    document.getElementById('main_form').submit();
  }
  function toggleRoomCheckAll(source)
  {
    var checks = document.getElementsByName('select_room_id');
    for (var i = 0; i < checks.length; i++)
    {
      checks[i].checked = source.checked;
    }
  }
  function syncSelectedRoomIds()
  {
    var hidden = document.getElementById('select_room_ids');
    var checks = document.getElementsByName('select_room_id');
    var ids = [];
    for (var i = 0; i < checks.length; i++)
    {
      if (checks[i].checked)
      {
        ids.push(checks[i].value);
      }
    }
    hidden.value = ids.join(',');
  }
  function go_bulk_maintenance()
  {
    syncSelectedRoomIds();
    if (document.getElementById('select_room_ids').value === '')
    {
      alert('対象の部屋を選択してください。');
      return;
    }
    var statusSelect = document.getElementById('bulk_status_value');
    var statusLabel = statusSelect.options[statusSelect.selectedIndex].text;
    if (!confirm('選択した部屋を「' + statusLabel + '」に一括変更します。よろしいですか？'))
    {
      return;
    }
    document.getElementById('main_form').action = 'RoomList.do';
    document.getElementById('action_cmd').value = 'bulk_maintenance';
    document.getElementById('main_form').submit();
  }
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
      <input type="hidden" name="select_room_ids" id="select_room_ids" value=""/>
      <div class="left">
        <div class="messages">
          <%=webBean.dispMessages()%>
        </div>
        <div class="errors">
          <%=webBean.dispErrorMessages()%>
        </div>
        <table class="select_table">
          <tr>
            <td class="search_label center" style="width: 50%">部屋名</td>
            <td class="search_label center" style="width: 25%">表示件数</td>
            <td class="search_label center" style="width: 25%"></td>
          </tr>
          <tr>
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
            <td class="list_label" style="width: 5%"><input type="checkbox" id="select_room_id_all" onclick="toggleRoomCheckAll(this);" /></td>
            <td class="list_label" style="width: 45%"><a href="javaScript:go_sort_request('full_name')">部屋名</a></td>
            <td class="list_label" style="width: 20%">ステータス</td>
            <td class="list_label" style="width: 30%"></td>
          </tr>
          <%
          RoomState roomState = new RoomState();
          for(Object item : webBean.arrayList("list"))
          {
              RoomDao dao = (RoomDao)item;
          %>
          <tr class="list_tr">
            <td class="list_check">
              <input type="checkbox" name="select_room_id" value="<%=WebUtil.txtEscape(dao.getRoomId())%>" />
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(dao.getRoomName())%>
            </td>
            <td class="list_text">
              <%=WebUtil.htmlEscape(roomState.getStateName(dao.getStatus()))%>
            </td>
            <td class="list_btn">
              <input type="button" value="編集" onclick="go_detail_1('go_next','update','<%=WebUtil.txtEscape(dao.getRoomId())%>','<%=WebUtil.txtEscape(dao.getRoomName())%>');" />
              <input type="button" value="削除" onclick="go_detail_2('go_next','deletef','<%=WebUtil.txtEscape(dao.getRoomId())%>','<%=WebUtil.txtEscape(dao.getRoomName())%>');" />
              <input type="button" value="予約確認" onclick="go_reserve_confirm('<%=WebUtil.txtEscape(dao.getRoomId())%>');" />
              <input type="button" value="予約登録" onclick="go_reserve_new('<%=WebUtil.txtEscape(dao.getRoomId())%>');" />
            </td>
          </tr>
          <%}%>
        </table>
        <div class="pagenation">
          <select name="bulk_status_value" id="bulk_status_value">
            <option value="1">利用可</option>
            <option value="2">使用中</option>
            <option value="3">メンテナンス中</option>
          </select>
          <input type="button" value="選択した部屋のステータスを一括変更する" onclick="go_bulk_maintenance();" />
        </div>
        <%}%>
      </div>
    </form>
  </div>
</body>
</html>
