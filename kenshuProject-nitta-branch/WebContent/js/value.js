// 入力データをhiddenに代入する式

function updateHiddenField(inputId, hiddenId) {
  document.getElementById(inputId).addEventListener('change', function() {
    var value = this.value;
    document.getElementById(hiddenId).value = value;
  });
}

// ルーム名、ユーザー名をhiddenに反映させる処理
function updateHiddenNameld(inputId,name,hiddenId) {
  document.getElementById(inputId).addEventListener('change', function() {
      var selectedOption = this.options[this.selectedIndex];
      var name1 = selectedOption.getAttribute(name);
      var hiddenInput = document.getElementById(hiddenId);

       hiddenInput.value = name1;
  });
}
// 各入力フィールドに対してイベントリスナーを設定
window.onload = function() {
updateHiddenField('user_info_id_input', 'user_info_id');
updateHiddenField('room_id_input', 'room_id');
updateHiddenField('input_text_input', 'input_text');
updateHiddenField('input_remark_input', 'input_remark');
// ユーザー名をhiddenに反映させるイベントリスナーを設定
updateHiddenNameld('room_id_input','data-room-name','room_name');
updateHiddenNameld('user_info_id_input','data-user-name','user_name');
};