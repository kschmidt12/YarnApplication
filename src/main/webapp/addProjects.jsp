<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>

<div class="container mt-4">
    <h2>Add New Project</h2>

    <form action="addProjects" method="post">

        <div class="mb-3">
            <label for="project" class="form-label">Project Name</label>

            <%-- Populate project name if coming from API --%>
            <input type="text" class="form-control"
                   id="project" name="project"
                   value="<c:out value='${projectName}'/>"
                   maxlength="100"
                   placeholder="e.g. Christmas Scarf" required>
        </div>

        <div class="mb-3">
            <label for="projectDescription" class="form-label">
                Project Description
            </label>

            <%-- Populate description if coming from API --%>
            <textarea class="form-control"
                      id="projectDescription"
                      name="projectDescription" rows="3"><c:out value="${projectDescription}"/></textarea>
        </div>

        <div class="mb-3">
            <label for="patternUrl" class="form-label">
                Pattern URL (Optional)
            </label>

            <%-- Populate pattern URL if coming from API --%>
            <input type="url" class="form-control"
                   id="patternUrl" name="patternUrl"
                   maxlength="2048"
                   value="<c:out value='${patternUrl}'/>"
                   placeholder="https://example.com/pattern">
        </div>

        <div class="mb-3">
            <label for="progress" class="form-label">Progress</label>
            <select class="form-select" id="progress" name="progress" required>
                <option value="Not Started">Not Started</option>
                <option value="In Progress">In Progress</option>
                <option value="On Hold">On Hold</option>
                <option value="Completed">Completed</option>
            </select>
        </div>

        <div class="mb-3">
            <h4>Select Yarn From Your Stash</h4>

            <c:forEach var="yarn" items="${yarns}">
                <div class="form-check">

                    <%-- Preselect yarn used in API search --%>
                    <input type="checkbox" class="form-check-input"
                           name="yarnIds" value="${yarn.id}"
                           id="yarn${yarn.id}"
                           ${not empty selectedYarnId and selectedYarnId eq yarn.id
                               ? 'checked' : ''}>

                    <label class="form-check-label" for="yarn${yarn.id}">
                        <c:out value="${yarn.brandName}"/> -
                        <c:out value="${yarn.color}"/>
                    </label>
                </div>
            </c:forEach>

            <c:if test="${empty yarns}">
                <p>You don't have any yarn in your stash yet.</p>
            </c:if>
        </div>

        <button type="submit" class="btn btn-success">Add Project</button>
        <a href="viewProject" class="btn btn-secondary">Cancel</a>

    </form>
</div>

</body>
</html>