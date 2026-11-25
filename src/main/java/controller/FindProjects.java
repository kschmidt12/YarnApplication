package controller;

import entity.User;
import entity.Yarn;
import entity.TavilyResponse;
import persistence.GenericDao;
import persistence.TavilyDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Searches for crochet patterns based on the user's yarn.
 */
@WebServlet(urlPatterns = {"/findProjects"})
public class FindProjects extends HttpServlet {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Displays the search form with the user's yarn stash.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getLoggedInUser(req, resp);

        if (user == null) {
            return;
        }

        loadYarns(req, user);
        req.getRequestDispatcher("/findProjects.jsp").forward(req, resp);
    }

    /**
     * Searches for patterns using the selected yarn weight.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getLoggedInUser(req, resp);

        if (user == null) {
            return;
        }

        loadYarns(req, user);
        int yarnId;

        try {
            yarnId = Integer.parseInt(req.getParameter("yarnId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        Yarn yarn = yarnDao.getById(yarnId);

        // Verify yarn belongs to the logged-in user
        if (yarn == null || yarn.getUser().getId() != user.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("selectedYarn", yarn);

        try {
            TavilyDao tavilyDao = new TavilyDao();

            TavilyResponse response =
                    tavilyDao.searchProjects(yarn.getYarnSize());

            req.setAttribute("results", response.getResults());

        } catch (Exception e) {
            logger.error("Error searching for crochet patterns", e);

            req.setAttribute("error",
                    "Unable to retrieve patterns. Please try again.");
        }

        req.getRequestDispatcher("/findProjects.jsp").forward(req, resp);
    }

    /**
     * Retrieves the user's yarn stash.
     */
    private void loadYarns(HttpServletRequest req, User user) {

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        List<Yarn> yarns = yarnDao.findByPropertyEqual("user", user);

        req.setAttribute("yarns", yarns);
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