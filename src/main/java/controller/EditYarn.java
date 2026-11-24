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
 * Handles editing yarn in the logged-in user's stash.
 */
@WebServlet(urlPatterns = {"/editYarn"})
public class EditYarn extends HttpServlet {

    /**
     * Retrieves the existing yarn and displays the edit form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Yarn yarn = getUserYarn(req, resp);

        if (yarn == null) {
            return;
        }

        req.setAttribute("yarn", yarn);
        req.getRequestDispatcher("/editYarn.jsp").forward(req, resp);
    }

    /**
     * Updates an existing yarn record.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Yarn yarn = getUserYarn(req, resp);

        if (yarn == null) {
            return;
        }

        String brandName = req.getParameter("brandName");
        String color = req.getParameter("color");
        int yarnAmount = Integer.parseInt(req.getParameter("yarnAmount"));
        String yarnSize = req.getParameter("yarnSize");
        String hookSize = req.getParameter("hookSize");
        String boughtFrom = req.getParameter("boughtFrom");


        yarn.setBrandName(brandName);
        yarn.setColor(color);
        yarn.setYarnAmount(String.valueOf(yarnAmount));
        yarn.setYarnSize(yarnSize);
        yarn.setHookSize(hookSize);
        yarn.setBoughtFrom(boughtFrom);

        // Save changes to the database
        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        yarnDao.update(yarn);

        resp.sendRedirect("viewYarn");
    }

    /**
     * Retrieves yarn and verifies that it belongs
     * to the logged-in user.
     */
    private Yarn getUserYarn(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("index.jsp");
            return null;
        }

        int userId = (int) session.getAttribute("userId");

        int yarnId;

        try {
            yarnId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        Yarn yarn = yarnDao.getById(yarnId);

        if (yarn == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        if (yarn.getUser().getId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return yarn;
    }
}