/* ===================================================================

 * スマホ向けサブメニュー

=================================================================== */
$(function($){
	var windowWidth = window.innerWidth || document.documentElement.clientWidth || 0;
	function slideMenu() {
		if ($('nav h3').css('display') == 'block') {
			$('nav ul').hide();
		}else {
			$('nav ul').show();
		}
	}

	$(window).on('load', function(){
		slideMenu();
	});

	$(window).on('resize', function(){
		var nowWidth = window.innerWidth || document.documentElement.clientWidth || 0;
		if(windowWidth != nowWidth) {
			slideMenu();
			if($('#navBtnIcon').hasClass('close')) {
				$('#navBtnIcon').removeClass('close')
			}
		}
		windowWidth = window.innerWidth || document.documentElement.clientWidth || 0;
	});

	$('nav h3').on('click', function(){
		$(this).next().slideToggle();
		$('#navBtnIcon').toggleClass('close');
	});
});
