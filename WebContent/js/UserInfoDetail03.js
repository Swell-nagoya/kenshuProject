"use strict";

function go_submit(action_cmd, request_cmd) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_form').submit();
}

function go_mail(action_cmd, request_cmd,main_key) {
    document.getElementById('main_form').action = 'SendPassMail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
}

function go_list(action_cmd, request_cmd,main_key) {
    document.getElementById('main_form').action = 'UserInfoDetail.do';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_key').value=main_key;
    document.getElementById('main_form').submit();
}

function togglePassword(id) {
  var input = document.getElementById(id);
  var type = input.getAttribute('type');
  input.setAttribute('type', type === 'password' ? 'text' : 'password');
}