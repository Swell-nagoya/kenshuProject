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
// 元の go_submit / go_upload はそのまま残す
function go_submit(action_cmd) {
  document.getElementById('main_form').action = '';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
}
function go_upload(action_cmd) {
  document.getElementById('main_form').action = '';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('main_form').submit();
}

// 子画面で「選択」ボタン押下時の処理
function submitSelection() {
  const selected = [];
  document.querySelectorAll('input[name="user_id"]:checked').forEach(cb => {
    selected.push({
      id: cb.value,
      name: cb.nextElementSibling.innerText,
      type: 'sub'
    });
  });
  if (selected.length) {
    window.opener.receiveSelectedUsers(selected, 'sub');
  }
  window.close();
}

function toggleCheckbox(div) {
  const cb = div.querySelector('input[type="checkbox"]');
  if (!cb) return;
  cb.checked = !cb.checked;
}
</script>
</head>
<body>
	<%
	int pageNo = Integer.parseInt(webBean.txt("pageNo", 1));
	int maxPageNo = Integer.parseInt(webBean.txt("maxPageNo", 1));
	%>

	<form id="pagerTop" class="pager" method="post" target="FileUserList"
		action="<%=request.getContextPath()%>/FileDetail.do"
		style="text-align: center; margin: 10px 0;">
		<input type="hidden" name="form_name" value="FileDetail" />
		<input type="hidden" name="action_cmd" value="sub" />
		<input type="hidden" name="pageNo" value="<%=pageNo%>" />
		<!-- チェック保持用 -->
		<input type="hidden" name="selectedIds" id="selectedIds"
			value="<%=webBean.txt("selectedIds")%>" />
		<button type="button" onclick="goPage(<%=pageNo - 1%>)"
			<%=pageNo <= 1 ? "disabled" : ""%>>← 戻る</button>
		<span><%=pageNo%> / <%=maxPageNo%> ページ</span>
		<button type="button" onclick="goPage(<%=pageNo + 1%>)"
			<%=pageNo >= maxPageNo ? "disabled" : ""%>>次へ →</button>
	</form>
	<script>
	function goPage(p) {
		  // 選択されたチェック状態を保持
		  const prev = (document.getElementById('selectedIds').value || "")
		    .split(',').filter(x => x);
		  const curr = Array.from(
		    document.querySelectorAll('input[name="user_id"]:checked')
		  ).map(cb => cb.value);
		  const merged = [...new Set([...prev, ...curr])];

		  // 両方の hidden に反映させる（上下両方の selectedIds）
		  const topSelected = document.querySelector('#pagerTop #selectedIds');
		  const bottomSelected = document.querySelector('#pagerBottom #selectedIds');
		  if (topSelected) topSelected.value = merged.join(',');
		  if (bottomSelected) bottomSelected.value = merged.join(',');

		  // フォームの送信
		  const f = document.getElementById("pagerTop") || document.getElementById("pagerBottom");
		  f.pageNo.value = p;
		  f.submit();
		}
  </script>


	<div class="container">
		<div id="contents">
			<div id="main">
				<form method="post" id="main_form" action="" class="main__form">
					<input type="hidden" name="form_name" id="form_name"
						value="FileDetail" />
					<input type="hidden" name="action_cmd" id="action_cmd" value="" />
					<input type="hidden" name="list" id="list"
						value="<%=webBean.txt("list")%>" />
					<input type="hidden" name="name" id="name"
						value="<%=webBean.txt("name")%>" />
					<input type="hidden" name="user_info_id" id="user_info_id">

						<div class="style_head3 messages"><%=webBean.dispMessages()%></div>
						<div class="errors"><%=webBean.dispErrorMessages()%></div> <!-- 送り先ユーザー（複数選択可） -->
						<div class="file__form--name">
							<h2>送り先ユーザー</h2>
							<div class="user-container">
								<%
								String sel = webBean.value("selectedIds");
								if (sel == null)
								    sel = "";
								List<String> stay = Arrays.asList(sel.split(","));
								for (Object o : webBean.arrayList("user_data")) {
								    UserInfoDao dao = (UserInfoDao) o;
								    String userId = WebUtil.htmlEscape(dao.getUserInfoId());
								    String fullName = WebUtil.htmlEscape(dao.getLastName())
								    + " "
								    + WebUtil.htmlEscape(dao.getFirstName());
								    boolean isChecked = stay.contains(userId);
								%>
								<div class="user-item" style="cursor: pointer;"
									onclick="toggleCheckbox(this)">
									<input type="checkbox" name="user_id" id="dest_<%=userId%>"
										value="<%=userId%>" class="user-checkbox"
										<%=isChecked ? "checked" : ""%>
										onclick="event.stopPropagation();" /> <label
										for="dest_<%=userId%>" onclick="event.stopPropagation();">
										<%=fullName%></label>
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
							<form id="pagerBottom" method="post" target="FileUserList"
								action="<%=request.getContextPath()%>/FileDetail.do"
								style="display: flex; justify-content: center; align-items: center; gap: 10px; margin: 20px auto; flex-wrap: nowrap; width: 100%; max-width: 800px;">
								<input type="hidden" name="form_name" value="FileDetail" />
								<input type="hidden" name="action_cmd" value="sub" />
								<input type="hidden" name="pageNo" value="<%=pageNo%>" />
								<input type="hidden" name="selectedIds" id="selectedIds"
									value="<%=webBean.txt("selectedIds")%>" />

								<button type="button"
									style="width: 100px; height: 40px; text-align: center; white-space: nowrap;"
									onclick="goPage(<%=pageNo - 1%>)"
									<%=pageNo <= 1 ? "disabled" : ""%>>← 戻る</button>

								<span
									style="display: inline-block; min-width: 120px; text-align: center;">
									<%=pageNo%> / <%=maxPageNo%> ページ
								</span>

								<button type="button"
									style="width: 100px; height: 40px; text-align: center; white-space: nowrap;"
									onclick="goPage(<%=pageNo + 1%>)"
									<%=pageNo >= maxPageNo ? "disabled" : ""%>>次へ →</button>
							</form>

							<!-- 選択ボタンが不要ならこの<div>ごと削除 -->
							<div>
								<input type="button" value="選択" onclick="submitSelection()"
									class="select-button" />
							</div>
						</div>
			</div>
			</form>
		</div>
	</div>
	</div>
</body>
</html>