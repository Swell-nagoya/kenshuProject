<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>カスタムエラーページ</title>
</head>
<body>
    <h1>エラーが発生しました</h1>
    <p>発生したエラーの種類：<%= exception.getClass().getName() %></p>
    <p>エラー内容：<%= exception.getMessage() %></p>
</body>
</html>