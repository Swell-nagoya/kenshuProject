<?xml version="1.0" encoding="UTF8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean" scope="request" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="jquery.watermark/jquery.watermark.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>ユーザー情報一覧</title>
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

.edit-reservation {
  background-color: #f0f0f0;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

table,
.table-wrap {
  margin-top: 20px;
}
table {
  width: 100%;
  border-collapse: collapse;
}
table:first-child {
  margin-top: 0;
}

th {
  background-color: #f2f2f2;
  padding: 10px;
}
td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
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

.select_table th,
.select_table td,
.list_table th,
.list_table td {
  border: 1px #a0a0a0 solid;
  border-collapse: collapse;
  border-spacing: 0;
}
.select_table tr,
.list_table tr {
  padding: 0;
}
.select_table th,
.select_table td {
  padding: 2px;
}

.search_label {
  padding: 2px 4px;
  background: #00bcd4;
  color: #fff;
  text-align: center;
  font-weight: normal;
}

.search_text, .search_line, .list_btn {
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

.pagenation, .select_table {
  margin-bottom: 10px;
}

.list_table th,
.list_table td {
  padding: 0;
}
.table-wrap .list_table > thead > tr > th {
  border-top: none;
}
.table-wrap .list_table > tbody > tr:last-child td {
  border-bottom: none;
}

#pageNo {
  text-align: center;
  border-radius: 5px;
}


/* ボタンの共通スタイル */
input[type="button"] {
  margin: 2px;
  border-radius: 10px; /* 角を丸くする */
  color: #fff; /* 文字色 */
  cursor: pointer; /* カーソルをポインタにする */
  background: #90a0b0; /* デフォルトの背景色 */
}

.button_send_all,
input[type="button"].button_send_all {

    
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

.button_send_all:hover,
input[type="button"].button_send_all:hover {
  background-color: #ff7f50;
  color: #fff;
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

footer {
  width: 100%;
}


/*./list_table*/
.list_table .fixed {
  position: sticky;
  top: 0;
  z-index: 1;
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
/* Table ソート用のスタイル */
.js-table_sort_label > a {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3px 25px 3px 25px;
  background-position: right;
  background-repeat: no-repeat;
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkElEQVQ4T73UMQ6AIAyF4Z/E0dHRIzh4/1M4eARHR0cH0wQMMdBC0sgMH4VXCDiP4OzRAg7AEjfegVsrwgIFW4ExIhewaagGfrFUmIrWwBpmoiXQwlS0BEoAU2P6JyBBvaMEzp3gYYGNxZWnWW3Tjf9yZPdQ3NtG7s1Cq6/lt6eX0nX9HHLU7fty6cNuJF/wAAIWJBX1VHH6AAAAAElFTkSuQmCC);
}
.js-table_sort_label > a:hover {
  background-color: #6acfc9;
}
.js-table_sort_label.is-asc > a {
  background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAgUlEQVQ4T+3SsQ2AIBCF4Z/eMdxCx3ARB7ByABdxDN3CMRzAkKCFHneQUFhwNXxc3sNReFxhjxSwAebw8ASc2hIW6LEFaANyAKOGauAbuxdT0RgYw0xUAi1MRSXQF9Antr8BvqhnJHAAukRwB1YLTLTkY9a3ycYrmB3Z50LN8IcZXsnkEhVNjPJpAAAAAElFTkSuQmCC);
}
.js-table_sort_label.is-desc > a {
  background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAfUlEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4RMIwhIGBwZpItx5lYGBYg6wWWyw3MzAw2BBp4BEGBoZaQgbyMDAw9DMwMKgQMPQOAwNDIQMDwxdCBoLkCRmK1TCQRnwJG5ehOA0jZCA2l+I1jBgDYYaCIgoEQBGAEmbo4Tz48zIAQNgSFR9+d5MAAAAASUVORK5CYII=);
}
</style>
<script type="text/javascript">
  jQuery(function($) {
      <%--検索条件入力でenterキーが押された場合の処理--%>
      $(".select_table input").keydown(function(e) {
        if (e.which == 13) {
          go_submit('search');
        }
      });
      $(".page_table input").keydown(function(e) {
        if (e.which == 13) {
          go_submit('jump');
        }
      });


      // 検索チェック時：value値の操作
      const list_search_cheack = new ListSearchCheack();
      list_search_cheack.event(); 
      list_search_cheack.chackFun(list_search_cheack.getListSearchState());

      // テーブルのheadタグ内 ソート用のリンク表示設定
      new TableSort();


    });
    <%--利用停止チェックボックスの状態判定、value適応--%>
    class ListSearchCheack {
    	  constructor(x, y) {
    	    // ソート順番（昇順、降順）
    	    this.list_search_state = "#list_search_state";
    	    this.$list_search_state = $(this.list_search_state);
    	  }
    	  event() {
    		if (this.$list_search_state.length === 0) return;
    	    // 変更イベントを監視
    	    this.$list_search_state.on('change', (e) => {
    	    	this.chackFun(e.currentTarget);
    	    });
    	 }
         getListSearchState(){
             return this.list_search_state;
         }
     	 chackFun(target){
   	      let check = $(target).prop("checked");
   	      if (check === true) {
	        this.$list_search_state.val("8");
	      } else {
	        this.$list_search_state.val("1");
	      }
         }
    }
    <%--テーブルの順番入れ替え時のクラス付け替え--%>
    class TableSort {

      constructor(x, y) {
        // ソート順番（昇順、降順）
        this.sort_order = $("#sort_order").val();
        // ソート時のkey取得
        this.sort_key_old = $("#sort_key_old").val();
        // ソートの対象となるクラス
        this.table_sort_labelClassName = "js-table_sort_label";
        // ソートの対象となる個別のID名
        this.table_sortIdName = "js-table_sort-";
        this.init();
      }

      init(){
        this.sort_label_height();
        this.event();
      }

      event(){
        this.sort();
        $(window).resize(() => {
          this.sort_label_height();
        });
      }
      
      // 並び替えリンクの高さ調整
      sort_label_height(){

       if( $("." + this.table_sort_labelClassName + " > a").length > 0 ){
         $("." + this.table_sort_labelClassName + " > a").css({
               'height': ''
         });
         Promise.resolve().then(() => {
           let sort_label_h = $("." + this.table_sort_labelClassName).height();

           $("." + this.table_sort_labelClassName + " > a").css({
              'height': sort_label_h
           });
         });

        }
      }

      // 並び替えのアクティブ状態を付与
      sort(){
        let $table_sort = $("#" + this.table_sortIdName + this.sort_key_old);
        if($table_sort.length > 0){
          // 昇順、降順の状態判定用のクラスを付与
          $table_sort.addClass("is-" + this.sort_order);
        }
      }
    }
    
  <%--テーブルを一行ごとにいろを変える--%>
    $(document).ready(function() {
      $('table.list_table tr:even').addClass('even');
      $('table.list_table tr:odd').addClass('odd');
    });
    function go_submit(action_cmd) {
      document.getElementById('main_form').action = 'ViewUserList.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('main_form').submit();
    }
    function go_sort_request(key) {
      document.getElementById('sort_key').value = key;
      document.getElementById('action_cmd').value = 'sort';
      document.getElementById('main_form').submit();
    }
    function go_menu(action_cmd) {
      document.getElementById('main_form').action = 'UserMenu.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('main_form').submit();
    }
    function go_detail_1(action_cmd, request_cmd, main_key) {
      document.getElementById('main_form').action = 'UserInfoDetail.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_key').value = main_key;
      document.getElementById('state_flg').value = main_key;
      if( document.getElementById('state_flg_' + main_key) ){
        if(document.getElementById('state_flg_' + main_key).checked){
          document.getElementById('state_flg').value = '8';
        } else {
          document.getElementById('state_flg').value = '1';
        }
        document.getElementById('main_form').submit();
      }
    }
    function go_detail(action_cmd, request_cmd) {
      document.getElementById('main_form').action = 'UserInfoDetail.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_form').submit();
    }
    function copyToClipboard(str) {
      navigator.clipboard.writeText(str)
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
            <a href="javascript:void(0)" value="" onclick="go_menu('top')">ユーザー情報一覧</a>
        </h1>
    </header>
    <form id="main_form" method="post" action="">
      <input type="hidden" name="form_name" id="form_name" value="ViewUserList" />
      <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
      <input type="hidden" name="request_cmd" id="request_cmd" value="" />
      <input type="hidden" name="main_key" id="main_key" value="" /> 
      <input type="hidden" name="sort_key_old" id="sort_key_old" value="<%=webBean.txt("sort_key_old")%>" /> 
      <input type="hidden" name="sort_key" id="sort_key" value="" /> 
      <input type="hidden" name="sort_order" id="sort_order"value="<%=webBean.txt("sort_order")%>" />
      <input type="hidden" name="search_info" id="search_info" value="<%=webBean.txt("search_info")%>" /> 
      <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt("user_info_id")%>" />
      <input type="hidden" name="state_flg" id="state_flg" value="">
      <input type="hidden" name="state_flg_all" id="state_flg_all" value="<%=webBean.txt("state_flg_all")%>">
     
      <div class="left">
        <div class="messages">
          <%=webBean.dispMessages()%>
        </div>
        <div class="errors">
          <%=webBean.dispErrorMessages()%>
        </div>
        <table class="select_table">
          <tr>
            <th class="search_label center statas" style="width: 6%">利用<br>停止</th>
            <th class="search_label center full_name" style="width: 31%">氏名</th>
            <th class="search_label center memail" style="width: 31%">メールアドレス</th>
            <th class="search_label center items_displayed" style="width: 10%">表示件数</th>
            <th class="search_label center" style="width: 18%"></th>
          </tr>
          <tr>
          <td class="search_text center statas">
          <input type="checkbox"  name="list_search_state"  id="list_search_state" value="8" class="search_active <%=webBean.dispErrorCSS("list_search_state")%>" 
         <% if("8".equals(webBean.txt("list_search_state"))) { %> checked<% } %> /> 
           <%=webBean.dispError("list_search_state")%>
           </td>
            <td class="search_text center full_name">
              <input type="text" name="list_search_full_name" id="list_search_full_name" size="30" maxlength="100" value="<%=webBean.txt("list_search_full_name")%>" class="full_name_active <%=webBean.dispErrorCSS("list_search_full_name")%>" placeholder="検索"/> <%=webBean.dispError("list_search_full_name")%>
            </td>
            <td class="search_text center memail">
              <input type="email" name="list_search_memail" id="list_search_memail" size="30" maxlength="100" value="<%=webBean.txt("list_search_memail")%>" class="memail_active <%=webBean.dispErrorCSS("list_search_memail")%>"/> <%=webBean.dispError("list_search_memail")%>
            </td>
            <td class="search_line center items_displayed">
              <input type="text" name="lineCount" id="lineCount" size="2" maxlength="5" value="<%=webBean.txt("lineCount")%>" class="right ime_disabled" />件
            </td>
            <td class="search_text center search_button">
              <input type="button" value="検索" onclick="go_submit('search')" /> 
              <input type="button" value="クリア" onclick="go_submit('clear')" /></td>
          </tr>
        </table>
        <%
        if (webBean.arrayList("list").size() > 0) {
        %>
        <div class="pagenation">
          <input type="text" name="pageNo" id="pageNo" maxlength="3" size='1' value="<%=webBean.txt("pageNo")%>" class="right ime_disabled" />
          /
          <%=webBean.html("maxPageNo")%>
          ページ〚全
          <%=webBean.html("recordCount")%>件〛<br />
          <%
          if (!"1".equals(webBean.value("pageNo"))) {
          %>
          <input type="button" value="<--前の<%=webBean.html("lineCount")%>件"
            onclick="go_submit('prior')" />
          <%
          } else {
          %>
          <%
          }
          %>
          <input type="button" value="ページ表示" onclick="go_submit('jump')" />
          <%
          if (!webBean.value("pageNo").equals(webBean.value("maxPageNo"))) {
          %>
          <input type="button" value="次の<%=webBean.html("lineCount")%>件-->"
            onclick="go_submit('next')" />
          <%
          } else {
          %>
          <%
          }
          %>
        </div>
        <div class="table-wrap">
          <table class="list_table">
            <thead>
              <tr class="list_title">
                <th id="js-table_sort-state_flg" class="list_label fixed statas js-table_sort_label">
                  <a href="javaScript:go_sort_request('state_flg')">利用<br>停止</a>
                </th>
                <th id="js-table_sort-last_name" class="list_label fixed full_name js-table_sort_label">
                  <a href="javaScript:go_sort_request('last_name')">氏名</a>
                </th>
                <th id="js-table_sort-last_name_kana" class="list_label fixed full_name_kana js-table_sort_label" >
                  <a href="javaScript:go_sort_request('last_name_kana')">氏名よみ（かな）</a>
                </th>
                <th id="js-table_sort-memail" class="list_label fixed memail js-table_sort_label">
                  <a href="javaScript:go_sort_request('memail')">メールアドレス</a>
                </th>
                <th class="list_label search_button fixed"></th>
              </tr>
            </thead>
            <tbody>
            <%
              // 【追記】サーブレットから送信されたチェック済みのID配列を取得
              String[] listStateFlgs = (String[]) request.getAttribute("checkedFlgs");
              java.util.List<String> checkedList = listStateFlgs != null ? java.util.Arrays.asList(listStateFlgs) : new java.util.ArrayList<>();
            %>
            <%
            for (Object item : webBean.arrayList("list")) {
                UserInfoDao dao = (UserInfoDao) item;
            %>
            <tr class="list_tr">
              <td class="list_input statas">
              <input type="checkbox" name="list_state_flg" id="state_flg_<%=WebUtil.txtEscape(dao.getUserInfoId())%>" value="<%=WebUtil.txtEscape(dao.getUserInfoId())%>" <% if(dao.getStateFlg() == 8){ %> checked<%}%>>
              <td class="list_text full_name">
            <%=WebUtil.htmlEscape(dao.getLastName())%>・<%=WebUtil.htmlEscape(dao.getMiddleName())%>・<%=WebUtil.htmlEscape(dao.getFirstName())%>
              </td>
              <td class="list_text full_name_kana"><%=WebUtil.htmlEscape(dao.getLastNameKana())%>・<%=WebUtil.htmlEscape(dao.getMiddleNameKana())%>・<%=WebUtil.htmlEscape(dao.getFirstNameKana())%>
              </td>
              <td class="list_text memail"><%=WebUtil.htmlEscape(dao.getMemail())%></td>
              <td class="list_btn search_button">
               
                <input type="button" value="編集" onclick="go_detail_1('go_next','update','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                <% //管理者
                if( "1".equals(webBean.txt("admin")) ) { %>
                <input type="button" value="削除" onclick="go_detail_1('go_next','delete','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                <% } %>
                <input type="button" value="確認" onclick="go_detail_1('go_next','check','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                <input type="button" value="閲覧管理" onclick="go_detail_1('go_next','access','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                <% //管理者
                 if( "1".equals(webBean.txt("admin")) ) { %>
                 <input type="button" value="利用停止登録" onclick="go_detail_1('go_next','stateUpdate','<%=WebUtil.txtEscape(dao.getUserInfoId())%>');" />
                <% } %>
              </td>
            </tr>
            <%
            }
            %>
            </tbody>
          </table>
          
        </div>
        <!-- ./table-wrap -->
        <input type="button" value="一括登録" class="button_send_all" onclick="go_detail('go_next','stateUpdateAll');" />
        <%
        } else {
        %>
          <p>ユーザー情報がありません</p>
        <%
        }
        %>
      </div>
    </form>
  </div>
</body>
</html>