<%@include file="taglib.jsp"%>
<%@include file="head.jsp"%>
<html>
<body>

<c:choose>
    <c:when test="${empty userName}">
        <a href = "logIn">Log in</a>
    </c:when>
    <c:otherwise>
        <h3>Welcome ${userName}</h3>
    </c:otherwise>
</c:choose>
<a href="search.jsp">Search Users</a>
</body>
</html>
</body>
</html>