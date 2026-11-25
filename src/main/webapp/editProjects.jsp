<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>

<div class="container mt-4">
    <h2>Edit Project</h2>

    <form action="editProjects" method="post">

        <input type="hidden" name="id" value="${project.id}">

        <div class="mb-3">
            <label for="project" class="form-label">Project Name</label>
            <input type="text" class="form-control"
                   id="project" name="project"
                   value="<c:out value='${project.project}'/>" required>
        </div>

        <div class="mb-3">
            <label for="projectDescription" class="form-label">
                Project Description
            </label>

            <textarea class="form-control"
                      id="projectDescription"
                      name="projectDescription"
                      rows="3"><c:out value="${project.projectDescription}"/></textarea>
        </div>

        <div class="mb-3">
            <label for="progress" class="form-label">Progress</label>

            <select class="form-select" id="progress" name="progress">

                <option value="Not Started"
                    ${project.progress eq 'Not Started' ? 'selected' : ''}>
                    Not Started
                </option>

                <option value="In Progress"
                    ${project.progress eq 'In Progress' ? 'selected' : ''}>
                    In Progress
                </option>

                <option value="On Hold"
                    ${project.progress eq 'On Hold' ? 'selected' : ''}>
                    On Hold
                </option>

                <option value="Completed"
                    ${project.progress eq 'Completed' ? 'selected' : ''}>
                    Completed
                </option>

            </select>
        </div>

        <div class="mb-3">
            <h4>Select Yarn From Your Stash</h4>

            <c:forEach var="yarn" items="${yarns}">
                <div class="form-check">

                    <input type="checkbox" class="form-check-input"
                           name="yarnIds" value="${yarn.id}"
                           id="yarn${yarn.id}"
                           ${selectedYarnIds.contains(yarn.id) ? 'checked' : ''}>

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
        <div class="mb-3">
            <label for="patternUrl" class="form-label">
                Pattern URL (Optional)
            </label>

            <input type="url" class="form-control"
                   id="patternUrl" name="patternUrl"
                   maxlength="2048"
                   value="<c:out value='${project.patternUrl}'/>"
                   placeholder="https://example.com/pattern">
        </div>

        <button type="submit" class="btn btn-success">
            Save Changes
        </button>

        <a href="viewProject" class="btn btn-secondary">Back To The Stash</a>

    </form>
</div>

</body>
</html>