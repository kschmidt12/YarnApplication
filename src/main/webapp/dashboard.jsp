<%@include file="taglib.jsp"%>
<%@include file="head.jsp"%>
<html>
<body>

<div class="container mt-5">
    <h1>Welcome, <c:out value="${sessionScope.userName}"/>!</h1>
    <p>What would you like to do today?</p>

    <a href="viewYarn" class="btn btn-primary">Go To The Stash</a>
    <a href="viewProject" class="btn btn-primary">Go To Projects</a>
    <a href="findProjects" class="btn btn-primary">
        Find New Projects</a>
</div>

</body>
</html>