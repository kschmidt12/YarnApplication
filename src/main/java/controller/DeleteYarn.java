package controller;

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
 * Handles deleting yarn from the logged-in user's stash.
 */
@WebServlet(urlPatterns = {"/deleteYarn"})
public class DeleteYarn extends HttpServlet {

    /**
     * Deletes an existing yarn record.
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

        int yarnId;

        try {
            yarnId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        Yarn yarn = yarnDao.getById(yarnId);

        if (yarn == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (yarn.getUser().getId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        yarnDao.delete(yarn);

        resp.sendRedirect("viewYarn");
    }
}