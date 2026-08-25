<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.patasys.common.http.WebUtil"%>
<%@ page import="jp.patasys.common.http.HtmlParts"%>
<%@ page import="jp.swell.constant.UserInfoState"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="webBean" class="jp.patasys.common.http.WebBean"
  scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<link type="text/css" href="jquery-ui/jquery-ui.css" rel="stylesheet"/>
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<script type="text/javascript" src="js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<title>【API連携】部屋情報一覧（非同期）</title>
<style type="text/css">
body { font-family: 'Arial', sans-serif; background-color: #f9f9f9; margin: 0; padding: 10px; }
header { background: #00bcd4; width: 100%; margin-bottom: 5px; text-align: center; }
h1 a { font-size: 1.5em; color: white; text-decoration: none; font-weight: normal; }
.container { background-color: #f0f0f0; border: 1px solid #ddd; border-radius: 5px; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); width: 90%; margin: 20px auto; }
input[type="button"] { border-radius: 10px; color: #fff; cursor: pointer; background: #90a0b0; padding: 5px 15px; }
input[type="button"]:hover { background-color: #4baea8; }
.api-controls { margin-bottom: 20px; padding: 15px; background: #e0f7fa; border-radius: 5px; }
table.list_table { width: 100%; border-collapse: collapse; margin-top: 10px; }
.list_label { background: #00bcd4; color: #fff; text-align: center; padding: 5px; }
.list_table td { border: 1px #a0a0a0 solid; padding: 5px; }
.list_tr:nth-child(odd) { background: #efefef; }
.list_tr:nth-child(even) { background: #ffffff; }
</style>
<script type="text/javascript">
  // 部屋一覧を取得してテーブルを再描画する
  function reloadRooms() {
    fetch('ApiRoomListController.do')
      .then(response => {
        if (!response.ok) throw new Error("通信エラー");
        return response.json();
      })
      .then(data => {
        const tbody = document.getElementById('roomListBody');
        tbody.innerHTML = ''; // 既存の内容をクリア
        
        data.forEach(room => {
          const tr = document.createElement('tr');
          tr.className = 'list_tr';
          
          const tdName = document.createElement('td');
          tdName.className = 'list_text';
          tdName.textContent = room.roomName;
          
          const tdBtn = document.createElement('td');
          tdBtn.className = 'list_btn center';
          tdBtn.style.textAlign = 'center';
          
          const deleteBtn = document.createElement('input');
          deleteBtn.type = 'button';
          deleteBtn.value = '削除 (非同期)';
          deleteBtn.onclick = function() { deleteRoom(room.roomId); };
          
          tdBtn.appendChild(deleteBtn);
          
          tr.appendChild(tdName);
          tr.appendChild(tdBtn);
          tbody.appendChild(tr);
        });
      })
      .catch(error => {
        console.error('Error:', error);
        alert('一覧の取得に失敗しました。');
      });
  }
  
  // 部屋を削除する
  function deleteRoom(roomId) {
    if(!confirm("本当に削除しますか？")) return;
    
    fetch('ApiRoomDetailController.do?room_id=' + roomId, {
      method: 'DELETE'
    })
    .then(response => {
      if(response.ok) {
        alert("削除しました");
        reloadRooms(); // 画面リロードせずに再取得
      } else {
        alert("削除に失敗しました");
      }
    })
    .catch(error => console.error('Error:', error));
  }

  // 部屋を新規追加する
  function addRoom() {
    const roomName = document.getElementById('newRoomName').value;
    if(!roomName) {
      alert("部屋名を入力してください");
      return;
    }
    
    fetch('ApiRoomDetailController.do', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      // GsonがマッピングできるようにJSONのキーを合わせる
      body: JSON.stringify({ roomName: roomName })
    })
    .then(response => {
      if(response.ok || response.status === 201) {
        alert("登録しました");
        document.getElementById('newRoomName').value = '';
        reloadRooms(); // 画面リロードせずに再取得
      } else {
        response.json().then(data => alert("エラー: " + data.message)).catch(() => alert("登録に失敗しました"));
      }
    })
    .catch(error => console.error('Error:', error));
  }

  // 画面ロード時に自動で一覧取得
  $(document).ready(function(){
      reloadRooms();
  });
</script>
</head>
<body>
  <div class="container">
    <header>
      <h1><a href="MenuAdmin.do">【API連携】部屋情報一覧（SPA風）</a></h1>
    </header>
    
    <!-- API連携用コントロールパネル -->
    <div class="api-controls">
      <h3>APIテスト操作パネル</h3>
      <p>
        <input type="button" value="リアルタイム再読み込み" onclick="reloadRooms()" style="background:#00bcd4;"/>
      </p>
      <p>
        新規部屋名: <input type="text" id="newRoomName" size="30" />
        <input type="button" value="追加 (非同期POST)" onclick="addRoom()" />
      </p>
    </div>

    <table class="list_table">
      <thead>
        <tr class="list_title">
          <td class="list_label" style="width: 70%">部屋名</td>
          <td class="list_label" style="width: 30%">操作</td>
        </tr>
      </thead>
      <tbody id="roomListBody">
        <!-- JavaScript (Fetch API) で動的に行が追加されます -->
      </tbody>
    </table>
  </div>
</body>
</html>
