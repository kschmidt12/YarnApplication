package controller;

import entity.User;
import entity.Yarn;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/viewYarn"})
public class ViewYarn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = (String) req.getSession().getAttribute("userName");

        // Check if the user is logged in
        if (userName == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        GenericDao<User> userDao = new GenericDao<>(User.class);
        List<User> users = userDao.findByPropertyEqual("userName", userName);

        if (users.size() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        User user = users.get(0);

        GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);
        List<Yarn> yarns = yarnDao.findByPropertyEqual("user", user);

        req.setAttribute("yarns", yarns);

        req.getRequestDispatcher("/viewYarn.jsp").forward(req, resp);

    }
}