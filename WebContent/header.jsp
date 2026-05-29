<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
    <header>
        <h1>
            <c:choose>
                <c:when test="${pageName == 'RoomList'}">部屋情報一覧</c:when>
                <c:when test="${pageName == 'AnotherPage'}">別のページタイトル</c:when>
                <c:otherwise>デフォルトタイトル</c:otherwise>
            </c:choose>
        </h1>
    </header>
    <!-- 本文の内容をここに追加 -->
</body>
</html>
