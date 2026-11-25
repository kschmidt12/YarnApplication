package controller;

import entity.Project;
import persistence.GenericDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles deleting projects belonging to the logged-in user.
 */
@WebServlet(urlPatterns = {"/deleteProjects"})
public class DeleteProjects extends HttpServlet {

    /**
     * Deletes the selected project after verifying ownership.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        int projectId;

        try {
            projectId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GenericDao<Project> projectDao = new GenericDao<>(Project.class);
        Project project = projectDao.getById(projectId);

        // Verify the project belongs to the logged-in user
        if (project == null || project.getUser().getId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        projectDao.delete(project);

        resp.sendRedirect("viewProject");
    }
}