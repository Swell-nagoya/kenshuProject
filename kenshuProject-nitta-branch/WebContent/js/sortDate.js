// DOMが完全に読み込まれた後に実行される関数を登録
document.addEventListener('DOMContentLoaded', function() {
    // 各列のソート順を管理するオブジェクト
    let sortOrder = {
        date: true, // 日付列のソート順
        time: true, // 時間列のソート順
        customer: true, // 利用者名列のソート順
        room: true // 部屋列のソート順
    };

    // 日付列のヘッダーにクリックイベントリスナーを追加
    document.getElementById('dateHeader').addEventListener('click', function() {
        sortTable('date', sortOrder.date); // テーブルを日付でソート
        sortOrder.date = !sortOrder.date;
        updateSortIcon('dateHeader', sortOrder.date);
    });

    // 時間列のヘッダーにクリックイベントリスナーを追加
    document.getElementById('timeHeader').addEventListener('click', function() {
        sortTable('time', sortOrder.time); // テーブルを時間でソート
        sortOrder.time = !sortOrder.time;
        updateSortIcon('timeHeader', sortOrder.time);
    });

    // 利用者名列のヘッダーにクリックイベントリスナーを追加
    document.getElementById('memberHeader').addEventListener('click', function() {
        sortTable('member', sortOrder.member); // テーブルを利用者名でソート
        sortOrder.member = !sortOrder.member;
        updateSortIcon('memberHeader', sortOrder.member);
    });

    // 部屋列のヘッダーにクリックイベントリスナーを追加
    document.getElementById('roomHeader').addEventListener('click', function() {
        sortTable('room', sortOrder.room); // テーブルを部屋名でソート
        sortOrder.room = !sortOrder.room;
        updateSortIcon('roomHeader', sortOrder.room);
    });
});

// テーブルをソートする関数
function sortTable(column, ascending) {
    const table = document.getElementById('reservationTable');
    const tbody = table.getElementsByTagName('tbody')[0];
    const rows = Array.from(tbody.getElementsByTagName('tr'));

    // 各行を指定された列の値に基づいてソート
    rows.sort((a, b) => {
        let valA, valB;
          if (column === 'date') {
            // 日付列の値を取得してDateオブジェクトに変換
            valA = a.cells[0].textContent.trim();
            valB = b.cells[0].textContent.trim();
        } else if (column === 'time') {
            // 時間列の値を取得
            valA = a.cells[1].textContent.trim();
            valB = b.cells[1].textContent.trim();
        } else if (column === 'member') {
            // 利用者名列の値を取得
            valA = a.cells[2].textContent.trim();
            valB = b.cells[2].textContent.trim();
        } else if (column === 'room') {
            // 部屋名列の値を取得
            valA = a.cells[3].textContent.trim();
            valB = b.cells[3].textContent.trim();
        }

        // 数値の比較と文字列の比較を区別してソート
        if (typeof valA === 'number' && typeof valB === 'number') {
            return ascending ? valA - valB : valB - valA; // 数値の場合、昇順/降順に応じて比較
        } else {
            return ascending ? valA.localeCompare(valB) : valB.localeCompare(valA); // 文字列の場合、昇順/降順に応じて比較
        }
    });

    // ソートされた行をテーブルに再追加
    rows.forEach(row => tbody.appendChild(row));
}

// ソートアイコンを更新する関数
function updateSortIcon(headerId, ascending) {
    // すべてのヘッダーのソートアイコンをリセット
    const reset = document.querySelectorAll('.sort-icon');
    reset.forEach(icon => {
        icon.classList.remove('asc', 'desc'); // 昇順、降順のクラスをすべて削除
        });
    const header = document.getElementById(headerId); // ヘッダー要素を取得
    const sortIcon = header.querySelector('.sort-icon'); // ソートアイコン要素を取得
    if (ascending) {
        sortIcon.classList.remove('desc'); // 降順アイコンを削除
        sortIcon.classList.add('asc'); // 昇順アイコンを追加
    } else {
        sortIcon.classList.remove('asc'); // 昇順アイコンを削除
        sortIcon.classList.add('desc'); // 降順アイコンを追加
    }
}