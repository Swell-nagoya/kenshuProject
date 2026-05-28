<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebBean"%>
<%@ page import="jp.swell.dao.UserInfoDao"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Arrays"%>

<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
	scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="keywords" content="">
		<meta name="description" content="">
			<meta charset="UTF-8">
				<link type="text/css" href="jquery-ui/jquery-ui.css"
					rel="stylesheet" />
				<link rel="shortcut icon" href="images/favicon.ico"
					type="image/vnd.microsoft.icon" />
				<link rel="icon" href="images/favicon.ico"
					type="image/vnd.microsoft.icon" />
				<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
				<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
				<script type="text/javascript"
					src="jquery.watermark/jquery.watermark.js"></script>
				<script type="text/javascript" src="js/common.js"></script>
				<script type="text/javascript" src="js/flatpickr.min.js"></script>
				<script type="text/javascript" src="js/cell.js"></script>
				<title>アップロード画面</title>
				<style>
body {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 80%;
	line-height: 1.2;
	letter-spacing: 0;
	font-family: "Lucida Grande", "Lucida Sans Unicode",
		"Hiragino Kaku Gothic Pro", "ヒラギノ角ゴ Pro W3", "メイリオ", Meiryo,
		"ＭＳ Ｐゴシック", Helvetica, Arial, Verdana, sans-serif;
	height: 100%;
	width: 100%;
	background: #f0f0f0;
}

.container {
	width: 75%;
	background-color: whitesmoke;
	padding: 50px;
	margin: 0 auto;
	border-radius: 5px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
	color: #333;
	background-color: #00bcd4;
	color: white;
	padding: 10px;
	text-align: center;
	border-radius: 5px;
}

label {
	display: block;
	border-radius: 5px;
	margin: 10px 0;
}

input[type="radio"], input[type="checkbox"] {
	margin-right: 5px;
}

input[type="button"] {
	background-color: #2196f3;
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
	padding: 10px 15px;
}

input[type="button"]:hover {
	background-color: #1976d2;
}

.user-radio, .user-checkbox {
	margin-bottom: 10px;
}

.errors, .messages {
	color: #FF0000;
	margin: 10px 0;
	text-align: center;
}

.file__form--name {
	margin-bottom: 20px;
}

.button-container {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

.button {
	width: 100px;
	height: 40px;
	padding: 0 10px;
	margin: 0 10px;
}

.user-container {
	display: flex;
	flex-direction: column;
}

.user-item {
	display: flex;
	align-items: center; /* 垂直方向の中央揃え */
	margin-bottom: 10px; /* 各ユーザー間のマージン */
	border: 1px solid #00bcd4; /* 枠線 */
	padding: 10px; /* 内側のパディング */
	border-radius: 5px; /* 角を丸める */
	background-color: white; /* 背景色 */
}

.user-item input[type="checkbox"], .user-item input[type="radio"] {
	margin-right: 10px; /* チェックボックス・ラジオボタンとラベルの間隔 */
}

.select-button {
	margin-top: 10px;
	background-color: #2196f3;
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
	padding: 10px 15px;
}

.pager {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 10px;
	margin: 20px 0;
	flex-wrap: nowrap;
}

.pager button {
	width: 100px;
	height: 40px;
	text-align: center;
	white-space: nowrap;
}

.pager span {
	min-width: 120px;
	text-align: center;
	display: inline-block;
}
</style>
<script type="text/javascript">
function splitValues(value) {
  if (!value) return [];
  return value.split(',').map(function(v) { return v.trim(); }).filter(function(v) { return v.length > 0; });
}

function getSelectedMap() {
  const ids = splitValues(document.getElementById('selectedIds').value);
  const names = splitValues(document.getElementById('selectedNames').value);
  const map = {};
  ids.forEach(function(id, index) {
    map[id] = names[index] || '';
  });
  return map;
}

function setSelectedMap(map) {
  const ids = [];
  const names = [];
  Object.keys(map).forEach(function(id) {
    if (!id) return;
    ids.push(id);
    names.push(map[id] || '');
  });
  document.getElementById('selectedIds').value = ids.join(',');
  document.getElementById('selectedNames').value = names.join(',');
}

function reflectCurrentPageToHidden() {
  const map = getSelectedMap();

  document.querySelectorAll('input[name="user_id"]').forEach(function(cb) {
    if (cb.checked) {
      map[cb.value] = cb.getAttribute('data-user-name') || '';
    } else {
      delete map[cb.value];
    }
  });

  setSelectedMap(map);
}

function restoreSelectionFromOpener() {
  if (document.getElementById('selectedIds').value) return;

  try {
    if (!window.opener || window.opener.closed) return;

    const parentIds = window.opener.document.getElementById('destination_user_info_id');
    if (parentIds && parentIds.value) {
      document.getElementById('selectedIds').value = parentIds.value;
    }
  } catch (e) {
    // 親画面を参照できない場合は何もしない
  }
}

function applyHiddenToCurrentPage() {
  const map = getSelectedMap();

  document.querySelectorAll('input[name="user_id"]').forEach(function(cb) {
    if (map.hasOwnProperty(cb.value)) {
      cb.checked = true;
      if (!map[cb.value]) {
        map[cb.value] = cb.getAttribute('data-user-name') || '';
      }
    }
  });

  setSelectedMap(map);
}

function goPage(p) {
  reflectCurrentPageToHidden();
  document.getElementById('pageNo').value = p;
  document.getElementById('pagerForm').submit();
}

function submitSelection() {
  reflectCurrentPageToHidden();

  const map = getSelectedMap();
  const selected = Object.keys(map).map(function(id) {
    return {
      id: id,
      name: map[id],
      type: 'sub'
    };
  });

  if (window.opener && !window.opener.closed && typeof window.opener.receiveSelectedUsers === 'function') {
    window.opener.receiveSelectedUsers(selected, 'sub');
  }
  window.close();
}

function toggleCheckbox(div) {
  const cb = div.querySelector('input[type="checkbox"]');
  if (!cb) return;
  cb.checked = !cb.checked;
  reflectCurrentPageToHidden();
}

window.onload = function() {
  restoreSelectionFromOpener();
  applyHiddenToCurrentPage();
};
</script>
</head>
<body>
	<%
	int pageNo = Integer.parseInt(webBean.txt("pageNo", 1));
	int maxPageNo = Integer.parseInt(webBean.txt("maxPageNo", 1));
	String selectedIds = webBean.txt("selectedIds");
	String selectedNames = webBean.txt("selectedNames");
	if (selectedIds == null) selectedIds = "";
	if (selectedNames == null) selectedNames = "";
	List<String> stay = Arrays.asList(selectedIds.split(","));
	%>

	<form id="pagerForm" class="pager" method="post" target="FileUserList"
		action="<%=request.getContextPath()%>/FileDetail.do">
		<input type="hidden" name="form_name" value="FileDetail" />
		<input type="hidden" name="action_cmd" value="sub" />
		<input type="hidden" name="pageNo" id="pageNo" value="<%=pageNo%>" />
		<input type="hidden" name="selectedIds" id="selectedIds"
			value="<%=WebUtil.htmlEscape(selectedIds)%>" />
		<input type="hidden" name="selectedNames" id="selectedNames"
			value="<%=WebUtil.htmlEscape(selectedNames)%>" />

		<button type="button" onclick="goPage(<%=pageNo - 1%>)"
			<%=pageNo <= 1 ? "disabled=\"disabled\"" : ""%>>← 戻る</button>
		<span><%=pageNo%> / <%=maxPageNo%> ページ</span>
		<button type="button" onclick="goPage(<%=pageNo + 1%>)"
			<%=pageNo >= maxPageNo ? "disabled=\"disabled\"" : ""%>>次へ →</button>
	</form>

	<div class="container">
		<div id="contents">
			<div id="main">
				<div class="style_head3 messages"><%=webBean.dispMessages()%></div>
				<div class="errors"><%=webBean.dispErrorMessages()%></div>

				<div class="file__form--name">
					<h2>送り先ユーザー</h2>
					<div class="user-container">
						<%
						for (Object o : webBean.arrayList("user_data")) {
						    UserInfoDao dao = (UserInfoDao) o;
						    String userId = WebUtil.htmlEscape(dao.getUserInfoId());
						    String fullName = WebUtil.htmlEscape(dao.getLastName()) + " " + WebUtil.htmlEscape(dao.getFirstName());
						    boolean isChecked = stay.contains(dao.getUserInfoId());
						%>
						<div class="user-item" onclick="toggleCheckbox(this)">
							<input type="checkbox" name="user_id" id="dest_<%=userId%>"
								value="<%=userId%>" data-user-name="<%=fullName%>"
								<%=isChecked ? "checked=\"checked\"" : ""%>
								onclick="event.stopPropagation(); reflectCurrentPageToHidden();" />
							<label for="dest_<%=userId%>" onclick="event.stopPropagation();"><%=fullName%></label>
						</div>
						<%
						}
						%>

						<%
						if (webBean.arrayList("user_data").isEmpty()) {
						%>
						<p>送信先ユーザーがありません</p>
						<%
						}
						%>
					</div>

					<div class="pager">
						<button type="button" onclick="goPage(<%=pageNo - 1%>)"
							<%=pageNo <= 1 ? "disabled=\"disabled\"" : ""%>>← 戻る</button>
						<span><%=pageNo%> / <%=maxPageNo%> ページ</span>
						<button type="button" onclick="goPage(<%=pageNo + 1%>)"
							<%=pageNo >= maxPageNo ? "disabled=\"disabled\"" : ""%>>次へ →</button>
					</div>

					<div style="text-align: center;">
						<input type="button" value="選択" onclick="submitSelection()"
							class="select-button" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
