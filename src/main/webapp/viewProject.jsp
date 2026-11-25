<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>

<div class="container-fluid mt-4">
    <h2>My Projects</h2>

    <a href="dashboard" class="btn btn-secondary mb-3">
        Back to The Stash
    </a>
    <a href="addProjects" class="btn btn-success mb-3">
        Add New Project
    </a>

    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Project Name</th>
                <th>Description</th>
                <th>Yarn</th>
                <th>Project Website</th>
                <th>Progress</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="project" items="${projects}">
                <tr>
                    <td><c:out value="${project.project}"/></td>
                    <td><c:out value="${project.projectDescription}"/></td>
                    <td>
                        <c:forEach var="yarn" items="${project.yarns}">
                            <div>
                                <c:out value="${yarn.brandName}"/> -
                                <c:out value="${yarn.color}"/>
                            </div>
                        </c:forEach>

                        <c:if test="${empty project.yarns}">
                            No yarn assigned
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${not empty project.patternUrl}">
                            <a href="<c:out value='${project.patternUrl}'/>"
                               target="_blank"
                               rel="noopener noreferrer">
                                View Pattern
                            </a>
                        </c:if>

                        <c:if test="${empty project.patternUrl}">
                            No URL assigned
                        </c:if>
                    </td>
                    <td><c:out value="${project.progress}"/></td>
                    <td>
                        <a href="editProjects?id=${project.id}"
                           class="btn btn-primary btn-sm">Edit</a>
                    </td>
                    <td>
                       <form action="deleteProjects" method="post"
                              style="display: inline;"
                              onsubmit="return confirm('Are you sure you want to delete this project?');">

                            <input type="hidden" name="id" value="${project.id}">

                            <button type="submit" class="btn btn-danger btn-sm">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty projects}">
        <p>You haven't added any projects yet!</p>
    </c:if>
</div>

</body>
</html>