<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>別のページのタイトル</title> <!-- タイトルを具体的にすることをお勧めします -->
</head>
<body>
    <%-- ページ名を設定 --%>
    <%
    request.setAttribute("pageName", "AnotherPage");
    %>
    
    <jsp:include page="header.jsp" />

    <div>
        <!-- ここに別のページの内容を追加します -->
    </div>
</body>
</html>
