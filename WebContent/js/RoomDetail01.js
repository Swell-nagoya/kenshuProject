"use strict";

function go_submit(action_cmd, request_cmd) {
  document.getElementById('main_form').action = 'RoomDetail.do';
  document.getElementById('action_cmd').value = action_cmd;
  document.getElementById('request_cmd').value = request_cmd;
  document.getElementById('main_form').submit();
}

function go_list(action_cmd, request_cmd,main_key) {
    document.getElementById('main_form').action = 'RoomDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
}