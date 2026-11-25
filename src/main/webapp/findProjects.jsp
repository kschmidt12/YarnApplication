<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>

<div class="container mt-4">

    <h2>Find Your Next Project</h2>

    <p>Search for free crochet patterns using yarn
        you already have in your stash!</p>

    <a href="dashboard" class="btn btn-secondary mb-3">
        Back To The Stash
    </a>

    <form action="findProjects" method="post">

        <div class="mb-3">
            <label for="yarnId" class="form-label">
                Select Yarn
            </label>

            <select class="form-select" id="yarnId"
                    name="yarnId" required>

                <option value="">Select your yarn</option>

                <c:forEach var="yarn" items="${yarns}">
                    <option value="${yarn.id}"
                        ${selectedYarn.id eq yarn.id ? 'selected' : ''}>

                        <c:out value="${yarn.brandName}"/> -
                        <c:out value="${yarn.color}"/> -
                        <c:out value="${yarn.yarnSize}"/>

                    </option>
                </c:forEach>

            </select>
        </div>

        <button type="submit" class="btn btn-success">
            Find Projects
        </button>

    </form>

    <c:if test="${empty yarns}">
        <p class="mt-3">
            Add yarn to your stash before searching for projects.
        </p>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <c:if test="${not empty results}">

        <h3 class="mt-4">Search Results</h3>

        <c:forEach var="result" items="${results}">

            <div class="card mb-3">
                <div class="card-body">

                    <h5 class="card-title">
                        <c:out value="${result.title}"/>
                    </h5>

                    <p class="card-text">
                        <c:out value="${result.content}"/>
                    </p>

                    <a href="<c:out value='${result.url}'/>"
                       target="_blank"
                       rel="noopener noreferrer"
                       class="btn btn-primary">
                        View Pattern
                    </a>
                    <form action="addProjects" method="get" style="display: inline;">

                        <input type="hidden" name="project"
                               value="<c:out value='${result.title}'/>">

                        <input type="hidden" name="projectDescription"
                               value="<c:out value='${result.content}'/>">

                        <input type="hidden" name="patternUrl"
                               value="<c:out value='${result.url}'/>">

                        <input type="hidden" name="yarnId"
                               value="${selectedYarn.id}">

                        <button type="submit" class="btn btn-success">
                            + Add to Projects
                        </button>

                    </form>

                </div>
            </div>

        </c:forEach>

    </c:if>

    <c:if test="${not empty selectedYarn and empty results and empty error}">
        <p class="mt-3">No patterns found.</p>
    </c:if>

</div>

</body>
</html>
