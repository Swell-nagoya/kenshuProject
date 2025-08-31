function activateModal(url) {
    // initialize modal element
    var modalEl = document.createElement('div');
    modalEl.style.width = '700px';
    modalEl.style.height = 'auto';
    modalEl.style.margin = '50px auto';
    modalEl.style.color = '#ffffff';
    modalEl.style.backgroundColor = '#000000';

    mui.overlay('on', modalEl);


    	$.get(url, function(data){
    		html = data.replace(/[\n\r]/g, "<br />");
    		$(modalEl).html(html);
    	});
    	}


$(function() {
    $(".popup").click(function(){
        // ウィンドウサイズ
        var ws = 940;
        var hs = 500;

        // 画面よりウィンドウサイズが大きい場合の縮小率
        var wr = 0.6;
        var hr = 0.6;

        if(ws > screen.availWidth) {
            ws = screen.availWidth * wr;
        }
         if(hs > screen.availHeight) {
            hs = screen.availHeight * hr;
        }

        // ウィンドウを中央寄せ
        var w = ( screen.availWidth - ws ) / 2;
        var h = ( screen.availHeight - hs ) / 2;

        window.open(this.href,"window","width= "+ ws + ",height=" + hs + ",left=" + w + ",top=" + h + ", location = no ,toolbar = no, menubar = no, status = no, resizable=yes");

        return false;
    });
});