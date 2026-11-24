package controller;

import entity.User;
import entity.Yarn;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles adding yarn to the logged-in user's stash.
 */
@WebServlet(urlPatterns = {"/addYarn"})
public class AddYarn extends HttpServlet {

    /**
     * Display the add yarn form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        req.getRequestDispatcher("/addYarn.jsp").forward(req, resp);
    }

    /**
     * Adds a new yarn record to the database.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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

        String brandName = req.getParameter("brandName");
        String color = req.getParameter("color");
        String yarnAmount = req.getParameter("yarnAmount");
        String yarnSize = req.getParameter("yarnSize");
        String hookSize = req.getParameter("hookSize");
        String boughtFrom = req.getParameter("boughtFrom");

        Yarn yarn = new Yarn();

        yarn.setBrandName(brandName);
        yarn.setColor(color);
        yarn.setYarnAmount(yarnAmount);
        yarn.setYarnSize(yarnSize);
        yarn.setHookSize(hookSize);
        yarn.setBoughtFrom(boughtFrom);

        yarn.setUser(user);

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        yarnDao.insert(yarn);

        resp.sendRedirect("viewYarn");
    }
}