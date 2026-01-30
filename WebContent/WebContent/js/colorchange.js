// カラーパレットの色をクリックしたときに現在の色を変更する
$(document).ready(function() {
    // カラーパレットにあるカラーボックスをクリックすると動く
  $('#color_palette').on('click', '.color-box', function() {
    var color = $(this).data('color');
    // hiddenの値変更
    $('#rgb_color').val(color);
    // 選択画面の値変更
    $('#rgb_color1').val(color);
  });
});