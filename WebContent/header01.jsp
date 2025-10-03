<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
    <%-- ページ名を設定 --%>
    <%
    request.setAttribute("pageName", "RoomList");
    %>
    
    <jsp:include page="header.jsp" />

    <div>
        <!-- ここにページの内容を追加します -->
    </div>
</body>
</html>
