<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.swell.dao.ScheduleDao"%>
<%@ page import="jp.patasys.common.http.WebBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Map, java.util.ArrayList, java.util.AbstractMap, java.util.List" %>

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
<link rel="stylesheet" href="css/common.css" type="text/css" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>スケジュール管理</title>
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
  margin: 15px auto; /* 不要な余白を排除 */
  display: flex; /* Flexboxを有効にする */
  justify-content: center; /* 水平方向に中央揃え */
  align-items: center; /* 垂直方向に中央揃え */
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

.messages {
  font-size: 30px;
  margin-bottom: 20px;
}

.list_table, .text {
  margin: 10 auto;
  text-align: center;
}

.style_head3 {
  padding-left: 10px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  
}

.style_head_size {
  height: 30px;
  vertical-align: middle;
  display: table-cell;
  background: #00bcd4;
  color: #fff;
}

table {
    width: 100%;
    border: 1px solid #ddd;
  }

  td, th {
    border: 1px solid #ddd; 
    padding: 5px;
  }

.input-text {
  font-size: 20px;
  font-weight: bold;
  background: #fff;
}

}
</style>
<script type="text/javascript">
  
  function go_submit(action_cmd, request_cmd,main_key) {
      document.getElementById('main_form').action = 'Schedule.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('request_cmd').value = request_cmd;
      document.getElementById('main_key').value=main_key;
      document.getElementById('main_form').submit();
  }


  function go_return(action_cmd,main_key) {
      document.getElementById('main_form').action = 'Schedule.do';
      document.getElementById('action_cmd').value = action_cmd;
      document.getElementById('main_key').value=main_key;
      document.getElementById('main_form').submit();
  }
  
</script>
  
</script>
</head>
<body>
  <div class="container">
    <div class="new-btn">
      <input type="button" value="　戻る　" onclick="go_return('return','<%=webBean.txt("user_info_id")%>')"  />
    </div>
<header>
    <h1>ユーザー閲覧管理登録ページ </h1>
</header>

    <form id="main_form" method="post" action="">

        <input type="hidden" name="form_name" id="form_name" value="ScheduleCheck" /> 
        <input type="hidden" name="action_cmd" id="action_cmd" value="" /> 
        <input type="hidden" name="request_cmd" id="request_cmd" value="<%=webBean.txt("request_cmd")%>" /> 
        <input type="hidden" name="request_name" id="request_name" value="<%=webBean.txt("request_name")%>" /> 
        <input type="hidden" name="main_key" id="main_key" value="" />
        <input type="hidden" name="input_info" id="input_info" value="<%=webBean.txt("input_info")%>" />
        <input type="hidden" name="selected_user_ids" id="selected_user_ids" value="<%= webBean.txt("selected_user_ids") %>"/>
        <input type="hidden" name="selected_user_names" id="selected_user_names" value=""/>
        <input type="hidden" name="user_info_id" id="user_info_id" value="<%=webBean.txt("user_info_id")%>">
        

        <div class="style_head3 messages"><%=webBean.dispMessages()%></div>

        <%
            String val = webBean.txt("request_name");
            String actionType =  val.equals("登録") ? "ins" : "ok" ;
        %>
        <div class="left">
        <table class="list_table">
        
          <tr>
            <td class="style_head3 style_head_size"> メインユーザー </td>
          </tr>
          <tr>
            <td class="input-text"> <%=webBean.txt("main_user_name")%> </td>
          </tr>
          
          <table class="list_table">
            <td class="style_head3 style_head_size" colspan="2"> 閲覧可能なユーザー </td>
             <%
             // 名前の配列またはリストの取得
             String userNames = webBean.txt("selected_user_names");
             List<String> userNamesList = Arrays.asList(userNames.split(","));
             String priorities = webBean.txt("selected_user_priorities");
             List<String> prioritiesList = Arrays.asList(priorities.split(","));

             // 名前と優先度をペアにしてリストを作成
             List<Map.Entry<String, String>> userPriorityList = new ArrayList<>();
             for (int i = 0; i < userNamesList.size(); i++) {
               userPriorityList.add(new AbstractMap.SimpleEntry<>(userNamesList.get(i), prioritiesList.get(i)));
             }

             // 優先度の数字が大きい順にソート
             userPriorityList.sort((entry1, entry2) -> Integer.parseInt(entry2.getValue()) - Integer.parseInt(entry1.getValue()));
             %>
             <%
             for (Map.Entry<String, String> entry : userPriorityList) {
             String userName = entry.getKey();
             String priorityValue = entry.getValue();
             String priority = priorityValue.equals("3") ? "高い" : priorityValue.equals("2") ? "やや高い" : "普通";
             %>
              <tr class="input-text">
                <td style="padding-right: 50px;" style="width: 30%"><%= userName %></td>
                <td style="width: 50%">優先度：<%= priority %></td>
              </tr>
            <%
            }
            %>
                </table>
        </table>
        </div>
        <div class="button">
          <input type="button" id="submitButton" value="<%=val%>" onclick="go_submit('go_next','<%=actionType%>','<%=webBean.txt("_user_info_id")%>')" />
        </div>
      </div>
  </form>
</body>
</html>

