function createMenu(){
	var txt = "";
	txt += '<h3>メニュー<span id="navBtn"><span id="navBtnIcon"></span></span></h3>'
	txt += '<ul>'
	txt += '<li><a href="index.html">ホーム</a></li>'
	txt += '<li><a href="htmlKiso.html">HTML</a></li>'
	txt += '<li><a href="cssKiso.html">CSS</a></li>'
	txt += '<li><a href="jsKiso.html">JavaScript</a></li>'
	txt += '<li><a href="jQuery.html">jQuery</a></li>'
	txt += '<li><a href="kiso15.html">Java基礎15問</a></li>'
	txt += '<li><a href="javaReport.html">Javaレポート</a></li>'
	txt += '<li><a href="sugoroku.html">すごろく</a></li>'
	txt += '<li><a href="servlet.html">MVC</a></li>'
	txt += '<li><a href="sqlKiso.html">SQL基礎</a></li>'
	txt += '<li><a href="oyouSql.html">SQL応用</a></li>'
	txt += '<li><a href="frameWork.html">フレームワーク</a></li>'
	txt += '<li><a href="jdbc.html">JDBC</a></li>'
	txt += '<li><a href="jdbcOyou.html">JDBC応用</a></li>'
	txt += '<li><a href="batch.html">バッチ</a></li>'
	txt += '<li><a href="phpKiso.html">PHP</a></li>'
	txt += '<li><a href="excel.html">Excel</a></li>'
	txt += '<li><a href="vbaKiso.html">VBA</a></li>'
	txt += '</ul>'
	
	$('#navigation').html(txt);
}