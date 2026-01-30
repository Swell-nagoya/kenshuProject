const reservations = [
    { date: "2024-11-15", info: "予約: 会議室A - 14時" },
    { date: "2024-11-18", info: "予約: 会議室B - 10時" }
];

reservations.forEach(reservation => {
    // ID の形式に一致するセルを探す
    const cell = document.querySelector(`td:contains('${parseInt(reservation.date.split("-")[2], 10)}')`);
    if (cell) {
        cell.innerHTML += `<br><span style="color: red;">${reservation.info}</span>`;
        cell.style.backgroundColor = "lightyellow"; // 背景色を変更
    }
});
