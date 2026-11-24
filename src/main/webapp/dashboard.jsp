<%@include file="taglib.jsp"%>
<%@include file="head.jsp"%>
<html>
<body>

<div class="container mt-5">
    <h1>Welcome, <c:out value="${sessionScope.userName}"/>!</h1>
    <p>What would you like to do today?</p>

    <a href="viewYarn" class="btn btn-primary">Go To The Stash</a>
</div>

</body>
</html>