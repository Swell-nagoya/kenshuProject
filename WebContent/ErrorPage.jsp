<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<%-- 
    isErrorPage="true" にすることで、このページは「エラー専用ページ」になります。
    これで、発生したエラーの内容（exception変数）を受け取れるようになります。
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>エラー</title>
</head>
<body>
    <h2>システムエラーが発生しました</h2>
    <p>申し訳ありません。予期せぬエラーが発生しました。</p>
    <hr>
    
    <%-- 開発中はエラーの原因を知りたいので、詳細を表示 --%>
    <h3>エラー詳細情報（デバッグ用）:</h3>
    <p>
        <strong>メッセージ:</strong> 
        <%= exception != null ? exception.getMessage() : "エラー情報の取得に失敗しました" %>
    </p>

    <%-- スタックトレース（エラーの履歴）を表示 --%>
    <div style="background-color: #f0f0f0; padding: 10px; border: 1px solid #ccc;">
        <pre><% 
            if (exception != null) {
                exception.printStackTrace(new java.io.PrintWriter(out));
            }
        %></pre>
    </div>

    <br>
    <a href="Login.jsp">ログイン画面へ戻る</a>
</body>
</html>