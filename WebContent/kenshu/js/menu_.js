function menuDisplay(){
    var html = "";

		html += '<div id="sub">';
		html += '<h2>メニュー</h2>';
		html += '<div id="menu">';
		html += '<ul>';
		html += '<li><a href="index.html">TOP</a></li>';
		html += '<li><a href="sqlKiso.html">SQL基礎課題</a></li>';
		html += '<li><a href="kiso15.html">JAVA 基礎15問課題</a></li>';
		html += '<li><a href="javaReport.html">JAVAレポート課題</a></li>';		
		html += '<li><a href="sugoroku.html">すごろく問題</a></li>';
		html += '<li><a href="htmlKiso.html">HTML課題</a></li>';		
		html += '<li><a href="cssKiso.html">css課題</a></li>';		
		html += '<li><a href="jsKiso.html">javaScript課題</a></li>';
		html += '<li><a href="servlet.html">Servlet課題</a></li>';
		html += '<li><a href="jsp.html">JSP課題</a></li>';
		html += '<li><a href="jdbc.html">JDBC課題</a></li>';
		html += '<li><a href="oyouSql.html">応用SQL課題</a></li>';
		html += '<li><a href="jdbcOyou.html">応用JDBC課題</a></li>';
		html += '<li><a href="phpKiso.html">PHP課題</a></li>';
		html += '</ul>';
		html += '</div>';
		html += '</div>';

    document.write(html);
}
