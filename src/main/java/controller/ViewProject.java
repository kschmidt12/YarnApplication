package controller;

import entity.User;
import entity.Project;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Displays projects belonging to the logged-in user.
 */
@WebServlet(urlPatterns = {"/viewProject"})
public class ViewProject extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        GenericDao<User> userDao = new GenericDao<>(User.class);
        User user = userDao.getById(userId);

        if (user == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        GenericDao<Project> projectDao = new GenericDao<>(Project.class);

        List<Project> projects = projectDao.findByPropertyEqual("user", user);

        req.setAttribute("projects", projects);
        req.getRequestDispatcher("/viewProject.jsp").forward(req, resp);
    }
}