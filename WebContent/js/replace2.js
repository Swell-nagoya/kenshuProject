// 時間を15分ごとの設定する

$(document).ready(function() {
    $('#checkin_time_input, #checkout_time_input').timepicker({
        timeFormat: 'HHmm', // 例:1200と表示
        interval: 15, // 15分毎に切り替える
        minTime: '00:00', // 0000
        maxTime: '23:45', // 2345まで表示
        dynamic: true, // 予約情報の時間情報を反映する
        dropdown: true, // テキストを押したらドロップダウンリスト表示
        scrollbar: true,// スクロールバー利用

            // 時間が変更されたらhiddenの値も変更される
        change: function(time) {
            var id1 = $(this).attr('id');
            var timeValue = $(this).val();
            if (id1 === 'checkin_time_input') {
                $('#checkin_time').val(timeValue);
            } else if (id1 === 'checkout_time_input') {
                $('#checkout_time').val(timeValue);
            }
        }
    });
});