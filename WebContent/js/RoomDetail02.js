"use strict";

function go_submit(action_cmd, request_cmd) {
    document.getElementById('main_form').action = '';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('request_cmd').value = request_cmd;
    document.getElementById('main_form').submit();
}

function go_list(action_cmd) {
    document.getElementById('main_form').action = '';
    document.getElementById('action_cmd').value = action_cmd;
    document.getElementById('main_form').submit();
}