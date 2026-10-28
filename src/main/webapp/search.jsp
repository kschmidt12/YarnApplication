<%@include file="head.jsp"%>
<html>
<body>

<h2>User Display Exercise</h2>

<form action="${pageContext.request.contextPath}/searchUser" method="get">
<label for="last_name">Search by Last Name: </label>
    <input type="text" id="last_name" name="last_name">
<input type="submit" name="" value="Search" />

</form>
</body>
</html>