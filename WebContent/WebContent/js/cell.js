// セルに予約情報を反映する

// "YYYYMMDD" 形式で日付をフォーマットする関数を定義
function formatDate(date) {
    var year = date.getFullYear(); // 年を取得
    var month = ("0" + (date.getMonth() + 1)).slice(-2); // 月を2桁でフォーマット
    var day = ("0" + date.getDate()).slice(-2); // 日を2桁でフォーマット
    return `${year}${month}${day}`; // "YYYYMMDD" 形式で返す
}

// "YYYY年MM月DD日"形式の文字列を"YYYYMMDD"形式に変換する関数
function format(dateStr) {
    var parts = dateStr.match(/(\d{4})年(\d{2})月(\d{2})日/);
    if (parts) {
        return parts[1] + parts[2] + parts[3]; // "YYYYMMDD" 形式で返す
    }
    return ''; // フォーマットできなかった場合は空文字を返す
}


$(document).ready(function() {
    // 今日の日付を取得して、予約情報を初期状態でフィルタリング
    var today = formatDate(new Date()); // 今日の日付を"YYYYMMDD"形式に変換
    update1(today); // 初期状態でのフィルタリング

    // flatpickrの初期化
    flatpickr("#myDatePicker", {
        inline: true,
        dateFormat: "Y年m月d日", // flatpickrの日付フォーマットを設定
        defaultDate: new Date(), // 今日の日付をデフォルトとして設定

         // カレンダーが初期化されたときに呼び出される関数
        onReady: function(selectedDates, dateStr, instance) {
            reservedDateColor(instance);
        },
        // 日付が変更されたときに呼び出される関数
        onChange: function(selectedDates, dateStr, instance) {
            var selectedDate = selectedDates.length > 0 ? selectedDates[0] : null; // 選択された日付かnull(空)なのか
            if (selectedDate) {
                var formatDate1 = formatDate(selectedDate); // 選択された日付を"YYYYMMDD"形式に変換
                update1(formatDate1); // 予約セルをフィルタリング
                reservedDateColor(instance); // カレンダーの日付に色を付ける
            }
        },
        // 表示月が変更されたときに呼び出される関数
        onMonthChange: function(selectedDates, dateStr, instance) {
            reservedDateColor(instance); // カレンダーの日付に色を付ける
        },
        // 表示年が変更されたときに呼び出される関数
        onYearChange: function(selectedDates, dateStr, instance) {
            reservedDateColor(instance); // カレンダーの日付に色を付ける
        }
    });

    // 選択された日付に基づいて予約セルを更新する関数
    function update1(selectedDate) {
        // 予約セルの更新
        $('#scheduleTable td').removeClass('reserved').css({'background-color': '','opacity': ''}); //予約情報のリセット
        reservationsData.forEach(function(reservation) {
            if (reservation.reservationDate === selectedDate) {
                var startHour = parseInt(reservation.checkinTime.substring(0, 2)); // チェックイン時間の時刻を取得
                var endHour = parseInt(reservation.checkoutTime.substring(0, 2)); // チェックアウト時間の時刻を取得
                var roomId = reservation.roomId; // 部屋IDを取得
                var color = reservation.color; // 色情報を取得

                // 予約時間内の各時間帯に対してセルを更新
                for (var hour = startHour; hour < endHour; hour++) {
                    var cellSelector = `td[data-room-id="${roomId}"][data-hour="${hour}"]`;
                    $(cellSelector).addClass('reserved').css({'background-color': color,'opacity': 0.4});
                }
            }
        });
    }

    // カレンダーの日付に色を付ける関数
    function reservedDateColor(instance) {
        reservationsData.forEach(function(reservation) {
            var dateStr = reservation.reservationDate;
            var date = new Date(
                parseInt(dateStr.substring(0, 4)), // 年
                parseInt(dateStr.substring(4, 6)) - 1, // 月 (0から始まるため1を引く)
                parseInt(dateStr.substring(6, 8)) // 日
            );

            // 予約がある日付にカスタムクラスを追加
            instance.calendarContainer.querySelectorAll('.flatpickr-day').forEach(function(dayElem) {
                if (dayElem.dateObj.getTime() === date.getTime()) {
                    dayElem.classList.add('highlight'); // カスタムクラスを追加
                }
            });
        });
    }

    // 指定された条件で予約を検索する関数
    function findReservation(roomId, date, hour) {
        var dateStr = $('#myDatePicker').val(); // 選択された日付を取得
        var date = format(dateStr); // "YYYYMMDD" 形式に変換
        return reservationsData.find(function(reservation) {
            var startHour = parseInt(reservation.checkinTime.substring(0, 2)); // チェックイン時間の時刻を取得
            var endHour = parseInt(reservation.checkoutTime.substring(0, 2)); // チェックアウト時間の時刻を取得

            // 予約が条件に一致するかを確認
            return reservation.roomId.toString() === roomId.toString() &&
                reservation.reservationDate === date &&
                hour >= startHour && hour < endHour
        });
    }
    
    // 予約日をYYYYMMDDからYYYY年MM月DD日に変換する関数
    function formatReservationDate(value) {
      if (value && value.length === 8) { // YYYYMMDD形式であることを確認
        return value.substring(0, 4) + '年' + value.substring(4, 6) + '月' + value.substring(6, 8) + '日';
      }
      return value; // フォーマットに失敗した場合はそのまま返す
      }

    // 時間をHHMMからHH:MMに変換する関数
    function formatTime(value) {
      if (value && value.length === 4) { // HHMM形式であることを確認
        return value.substring(0, 2) + ':' + value.substring(2, 4);
      }
      return value; // フォーマットに失敗した場合はそのまま返す
    }

    // ツールチップを表示する関数
    function showMessage(element) {
        var roomId = $(element).data('room-id');
        var hour = $(element).data('hour');
        var dateStr = $('#myDatePicker').val();
        var date = format(dateStr);
        var reservation = findReservation(roomId, date, hour);

        if (reservation) {
            // 現在選択されているユーザーIDを取得
            var selectedUserId = $('#user_info_id_input').val();
            
            // 既存のツールチップを削除
            $('.reservation-tooltip').remove();
            
             // ファイル情報を含むリンクを作成する
          var fileLinks = '';
          if (reservation.fileLinks) {
            var fileNamesArray = reservation.fileLinks.split(','); // 複数のファイル名を配列に分割
            var fileIdsArray = reservation.fileIds.split(',');
            fileNamesArray.forEach(function(fileName, index) {
                var fileId = fileIdsArray[index];  // 対応するファイルIDを取得
        
            // データベースのファイルパスをWebアクセス可能なURLに変換
            var filePath = fileName.replace('rainingkenshuProjectWebContentupload/', '/uploads/');
        
            // ファイルのリンクを表示
            fileLinks += `<br><a href="#" onclick="go_download('go_next', 'download', '${fileId}', '${fileName}'); return false;">ファイル: ${fileName}</a>`;
            });
          }

          // 日付と時間のフォーマットを変換
          var formattedDate = formatReservationDate(reservation.reservationDate); // YYYYMMDD -> YYYY年MM月DD日
          var formattedCheckinTime = formatTime(reservation.checkinTime); // HHMM -> HH:MM
          var formattedCheckoutTime = formatTime(reservation.checkoutTime); // HHMM -> HH:MM

          // ツールチップに表示するメッセージを作成
         var message = `予約日: ${formattedDate}<br>
                        開始時間: ${formattedCheckinTime}<br>
                        終了時間: ${formattedCheckoutTime}<br>
                        部屋名: ${reservation.roomName}<br>
                        予約者: ${reservation.userName}`;

                      
                      // fileLinksが "none" でない場合のみリンクを追加
                      if (reservation.fileLinks !== "none") {
                      message += fileLinks;
                      }
                      
          // ユーザーIDが一致する場合のみ編集ボタンを追加
          if (reservation.userId === selectedUserId) {
            message += `<br><input type="button" class="detail1" value="編集" onclick="go_detail('edit', '${reservation.reserveId}');" />`;
          }

          var tooltip = $('<div class="reservation-tooltip"></div>'); // ツールチップの要素を作成
          tooltip.html(message); // メッセージを設定
          $('body').append(tooltip); // ツールチップをページに追加

          // ツールチップの位置を調整
          var pos = $(element).offset();
          tooltip.css({
            left: pos.left + $(element).outerWidth() / 2 - tooltip.outerWidth() / 2,
            top: pos.top - tooltip.outerHeight() + 15,
          }).fadeIn("fast");


          // ツールチップと関連要素を紐付け
          $(element).data('tooltip', tooltip);
          tooltip.data('relatedElement', $(element));

          // ツールチップの表示を維持するためのフラグ
          var keepTooltip = false;

          // ツールチップ上にマウスが入ったときにツールチップを消さないようにする
          tooltip.on('mouseover', function() {
              keepTooltip = true; // ツールチップの表示を維持するフラグ
          }).on('mouseout', function() {
            keepTooltip = false; // ツールチップの表示を解除するフラグ
          });

          // ツールチップの表示を解除する条件を設定
          setTimeout(function() {
            if (!keepTooltip) {
                tooltip.fadeOut("slow", function() {
                    $(this).remove(); // ツールチップを完全に削除
                });
            }
          }, 300); // 遅延を設定してツールチップの表示を確認
        }
    }

// 固定メッセージを非表示にする関数
function hideMessage() {
    $('.reservation-tooltip').each(function() {
        if (!$(this).data('')) {
            $(this).fadeOut("slow", function() {
                $(this).remove(); // ツールチップを削除
            });
        }
    });
}

// 予約セルにマウスが重なったときの処理
$(document).on('mouseover', '#scheduleTable td.reserved', function() {
    showMessage($(this));
});

// 予約セルからマウスが離れたときの処理
$(document).on('mouseout', '#scheduleTable td.reserved', function() {
    // ツールチップが表示中の場合は、マウスがツールチップの上にない場合のみツールチップを非表示にする
    var tooltip = $(this).data('tooltip');
    if (tooltip && !tooltip.is(':visible')) {
        hideMessage();
    }
});


});

