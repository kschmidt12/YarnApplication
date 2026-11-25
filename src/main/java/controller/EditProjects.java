package controller;

import entity.Project;
import entity.User;
import entity.Yarn;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles editing existing projects.
 */
@WebServlet(urlPatterns = {"/editProjects"})
public class EditProjects extends HttpServlet {

    /**
     * Displays the edit project form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Project project = getUserProject(req, resp);

        if (project == null) {
            return;
        }

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);

        List<Yarn> yarns = yarnDao.findByPropertyEqual(
                "user", project.getUser());
        Set<Integer> selectedYarnIds = new HashSet<>();

        for (Yarn yarn : project.getYarns()) {
            selectedYarnIds.add(yarn.getId());
        }

        req.setAttribute("project", project);
        req.setAttribute("yarns", yarns);
        req.setAttribute("selectedYarnIds", selectedYarnIds);

        req.getRequestDispatcher("/editProjects.jsp").forward(req, resp);
    }

    /**
     * Saves changes to an existing project.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Project project = getUserProject(req, resp);

        if (project == null) {
            return;
        }

        project.setProject(req.getParameter("project"));
        project.setProjectDescription(
                req.getParameter("projectDescription"));
        project.setProgress(req.getParameter("progress"));
        String patternUrl = req.getParameter("patternUrl");

        if (patternUrl != null && !patternUrl.isBlank()) {
            patternUrl = patternUrl.trim();

            if (patternUrl.length() > 2048 ||
                    !(patternUrl.startsWith("https://") ||
                            patternUrl.startsWith("http://"))) {

                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid pattern URL");
                return;
            }
        } else {
            patternUrl = null;
        }

        project.setPatternUrl(patternUrl);

        String[] yarnIds = req.getParameterValues("yarnIds");

        Set<Yarn> selectedYarns = new HashSet<>();
        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);

        if (yarnIds != null) {
            for (String id : yarnIds) {

                int yarnId;

                try {
                    yarnId = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Yarn yarn = yarnDao.getById(yarnId);
                if (yarn == null || yarn.getUser().getId()
                        != project.getUser().getId()) {

                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                selectedYarns.add(yarn);
            }
        }

        project.setYarns(selectedYarns);

        GenericDao<Project> projectDao = new GenericDao<>(Project.class);
        projectDao.update(project);

        resp.sendRedirect("viewProject");
    }

    /**
     * Retrieves a project and verifies user ownership.
     */
    private Project getUserProject(HttpServletRequest req,
                                   HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return null;
        }

        int userId = (int) session.getAttribute("userId");

        int projectId;

        try {
            projectId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        GenericDao<Project> projectDao = new GenericDao<>(Project.class);
        Project project = projectDao.getById(projectId);

        if (project == null || project.getUser().getId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return project;
    }
}