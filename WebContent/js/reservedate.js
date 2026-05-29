// 日付情報を予約画面に情報を引っ張ってくる
$(document).ready(function() {
    // セッションストレージから予約情報を取得
    var room = sessionStorage.getItem('reservationRoom');
    var date = sessionStorage.getItem('reservationDate');
    var startTime = sessionStorage.getItem('reservationStartTime');
    var endTime = sessionStorage.getItem('reservationEndTime');
    // 日付の修正
    if (date) {
        // 日付が "yyyy年MM月dd日" 形式で保存されている場合
        var formattedDate = date.replace(/年|月|日/g, ''); // "yyyy年mm月dd日" を "yyyy-mm-dd" に変換
        $("#reservation_date_input").val(formattedDate);
        var formattedDate1 = formattedDate.replace(/-/g, ''); // ハイフンを削除
        $("#reservation_date").val(formattedDate1);
    }
    // 時間の修正
    function formatTime(time) {
        // コロンを削除して、時間を4桁の形式に整える
        var cleanedTime = time.replace(/:/g, ''); // コロンを削除
         // 先頭にゼロを追加する（必要に応じて）
    if (cleanedTime.length < 4) {
        cleanedTime = '0'.repeat(4 - cleanedTime.length) + cleanedTime;
    }

    return cleanedTime;
    }
    // フォームに値を設定
    if (room) {
        // 部屋名を表示する要素に設定
        $("#room_name").val(room);

        // セレクトボックスの値を設定
        $("#room_id_input option").each(function() {
            if ($(this).data('room-name') === room) {
                $(this).prop('selected', true);
                $("#room_id").val($(this).val()); // hidden フィールドにも設定
            }
        });
    }
    if (startTime) {
        var formattedStartTime = formatTime(startTime);
        $("#checkin_time_input").val(formatTime(startTime));
        $("#checkin_time").val(formattedStartTime); // hidden フィールドにも設定
    }
    if (endTime) {
        var formattedEndTime = formatTime(endTime);
        $("#checkout_time_input").val(formattedEndTime);
        $("#checkout_time").val(formattedEndTime); // hidden フィールドにも設定
    }

    // セッションストレージをクリア
    sessionStorage.removeItem('reservationRoom');
    sessionStorage.removeItem('reservationDate');
    sessionStorage.removeItem('reservationStartTime');
    sessionStorage.removeItem('reservationEndTime');
});