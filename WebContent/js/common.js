	String.prototype.trim = function() {
		var str = this.replace(/^[\s　]+|[\s　]+$/g, '');
    	return str;
	};
	function trim_all_values()
	{
	  var len = document.forms.length;
	  for(var i=0;i<len;i++)
	  {
	    trim_text_node_values(document.forms[i]);
	  }
	}
	function trim_text_node_values(pnode)
	{
		if(!pnode) return;
		var len=pnode.childNodes.length;
		if(!len) return;
		var i;
		for(i=0;i<len;i++)
		{
			node = pnode.childNodes[i];
			switch(node.nodeName)
			{
			  	case 'INPUT':
			  	case 'input':
					if(node.type!='button')
					{
						node.value 	= node.value.trim();
					}
					break;
				default:
			}
		}
	}
	$(document).ready(function(){
		$("textarea").each( function (e)
		 {
		    var value = $(this).val();
		    var orow = $(this).attr("rows");
		    $(this).attr("orows",orow);
		    var nrow = value.split("\n").length;
		    if(orow < nrow) $(this).attr("rows",nrow);
		 } );

		$("textarea").keydown( function (e)
		 {
		    var value = $(this).val();
		    var nrow = value.split("\n").length;
		    var orow = $(this).attr("orows");
		    if(orow < nrow) $(this).attr("rows",nrow+1);
		 } );
		});
	function postDetailWindow(url) {
		var options = {};
		var selector = ".windowContent";
		$(selector).find("*").each(function(){
			if($(this).attr("type")=="radio" || $(this).attr("type")=="checkbox"){
				if($(this).attr("checked")){
					options[$(this).attr("name")] = $(this).val();
				}
			}
			if($(this).attr("name") && $(this).val()) {
				options[$(this).attr("name")] = $(this).val();
			}
		});
		$(selector).load(url,options);
	}

    function get_height()
    {
    	var h = Math.max.apply( null, [document.body.clientHeight , document.body.scrollHeight, document.documentElement.scrollHeight, document.documentElement.clientHeight] );
    	return h;
    }

    function get_width()
    {
    	var h = Math.max.apply( null, [document.body.clientWidth , document.body.scrollWidth, document.documentElement.scrollWidth, document.documentElement.clientWidth] );
    	return h;
    }

	function htmlspecialchars(ch) {
		if(!ch) return "";
		ch = ch.replace(/&/g, "&amp;");
		ch = ch.replace(/"/g, "&quot;");
		ch = ch.replace(/'/g, "&#039;");
		ch = ch.replace(/</g, "&lt;");
		ch = ch.replace(/>/g, "&gt;");
		ch = ch.trim();
		return ch;
	}

	function displayNowDatetime()
	{
		document.write('<span class="right" id="display_now_time"></span>');
		displayNowDatetimeDatetimeSet();
		setInterval("displayNowDatetimeDatetimeSet()",1000);
	}
	function displayNowDatetimeDatetimeSet()
	{
		var Nowymdhms=new Date();
		var NowYear = padZero(Nowymdhms.getFullYear(),4);
		var NowMon = padZero(Nowymdhms.getMonth() + 1,2);
		var NowDay = padZero(Nowymdhms.getDate(),2);
		var NowWeek = Nowymdhms.getDay();
		var NowHour = padZero(Nowymdhms.getHours(),2);
		var NowMin = padZero(Nowymdhms.getMinutes(),2);
		/*var NowSec = padZero(Nowymdhms.getSeconds(),2);*/
		var Week = new Array("日","月","火","水","木","金","土");
		myMsg = NowYear+"年"+NowMon+"月"+NowDay+"日("+Week[NowWeek]+")"+'&nbsp;&nbsp;'+NowHour+"時"+NowMin+"分";
		document.getElementById('display_now_time').innerHTML = myMsg;
	}
	function padZero(value, length)
	{
	    return new Array(length - ('' + value).length + 1).join('&nbsp;') + value;
	}




