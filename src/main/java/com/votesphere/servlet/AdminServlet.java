package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.dao.UserDAO;
import com.votesphere.dao.VoteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();
    private final UserDAO userDAO = new UserDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("userCount", userDAO.count());
            request.setAttribute("pollCount", pollDAO.count());
            request.setAttribute("activeCount", pollDAO.countActive());
            request.setAttribute("voteCount", voteDAO.count());
            request.setAttribute("polls", pollDAO.findAll(null).stream().limit(8).toList());
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load administrator dashboard.", e);
        }
    }
}
