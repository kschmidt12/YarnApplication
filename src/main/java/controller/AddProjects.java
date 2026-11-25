package controller;

import entity.Project;
import entity.User;
import entity.Yarn;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles adding projects for the logged-in user.
 */
@WebServlet(urlPatterns = {"/addProjects"})
public class AddProjects extends HttpServlet {

    /**
     * Displays the add project form and available yarn.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getLoggedInUser(req, resp);

        if (user == null) {
            return;
        }

        // Retrieve the user's yarn stash
        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        List<Yarn> yarns = yarnDao.findByPropertyEqual("user", user);

        req.setAttribute("yarns", yarns);

        // Retrieve information passed from API search results
        req.setAttribute("projectName", req.getParameter("project"));
        req.setAttribute("projectDescription",
                req.getParameter("projectDescription"));
        req.setAttribute("patternUrl", req.getParameter("patternUrl"));
        req.setAttribute("selectedYarnId", req.getParameter("yarnId"));

        req.getRequestDispatcher("/addProjects.jsp").forward(req, resp);
    }

    /**
     * Saves a new project and its associated yarn.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getLoggedInUser(req, resp);

        if (user == null) {
            return;
        }

        String projectName = req.getParameter("project");
        String projectDescription = req.getParameter("projectDescription");
        String progress = req.getParameter("progress");
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

        Project project = new Project();

        project.setProject(projectName);
        project.setProjectDescription(projectDescription);
        project.setProgress(progress);
        project.setPatternUrl(patternUrl);
        project.setUser(user);

        // Retrieve selected yarn IDs
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

                // Verify the yarn belongs to the logged-in user
                if (yarn == null || yarn.getUser().getId() != user.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                selectedYarns.add(yarn);
            }
        }

        // Associate selected yarn with the project
        project.setYarns(selectedYarns);

        // Save project
        GenericDao<Project> projectDao = new GenericDao<>(Project.class);
        projectDao.insert(project);

        resp.sendRedirect("viewProject");
    }

    /**
     * Retrieves the currently logged-in user.
     */
    private User getLoggedInUser(HttpServletRequest req,
                                 HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return null;
        }

        int userId = (int) session.getAttribute("userId");

        GenericDao<User> userDao = new GenericDao<>(User.class);
        User user = userDao.getById(userId);

        if (user == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return user;
    }
}