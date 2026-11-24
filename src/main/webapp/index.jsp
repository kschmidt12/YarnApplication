<%@include file="taglib.jsp"%>
<%@include file="head.jsp"%>
<html>
<body>

<div class="container mt-5 text-center">
    <h1>Welcome to My Yarn Stash!</h1>
    <p>Manage your yarn inventory and keep track of your projects.</p>

    <c:choose>
        <c:when test="${empty sessionScope.userName}">
            <a href="logIn" class="btn btn-primary">Log In / Register</a>
        </c:when>
        <c:otherwise>
            <a href="dashboard" class="btn btn-primary">Go to Dashboard</a>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>