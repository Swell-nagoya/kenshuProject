<%-- calendar-core.jsp --%>
<script>
  const holidays = {
    "20250101": "元日",
    "20250211": "建国記念日",
    "20250429": "昭和の日",
    "20250503": "憲法記念日",
    "20250504": "みどりの日",
    "20250505": "こどもの日"
  };

  function isHoliday(yyyymmdd) {
    return holidays.hasOwnProperty(yyyymmdd);
  }

  function getDayClass(dateObj) {
    const y = dateObj.getFullYear();
    const m = String(dateObj.getMonth() + 1).padStart(2, '0');
    const d = String(dateObj.getDate()).padStart(2, '0');
    const dateStr = `${y}${m}${d}`;
    const day = dateObj.getDay();

    let classList = ['date'];
    if (day === 0) classList.push('sunday');
    if (day === 6) classList.push('saturday');
    if (isHoliday(dateStr)) classList.push('holiday');

    return classList.join(' ');
  }
</script>

<style>
	/* 曜日（日曜日） */
	.weekDay.sunday,
	.dateOfWeek .sunday {
	  color: red;
	  font-weight: bold;
	}
	
	/* 曜日（土曜日） */
	.weekDay.saturday,
	.dateOfWeek .saturday {
	  color: blue;
	  font-weight: bold;
	}
	
	/* 祝日 */
	.weekDay.holiday,
	.dateOfWeek .holiday {
	  color: red;
	  font-weight: bold;
	}
</style>
