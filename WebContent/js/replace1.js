// 日付情報のハイフンを消す式

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('reservation_date_input').addEventListener('change', function() {
        // 元のデータを変更しないように初期化
        var value = this.value;
        // ハイフンを削除
        var value1 = value.replaceAll("-", "");
        // 入力フィールドの値を更新
        document.getElementById('reservation_date').value = value1;
    });
});