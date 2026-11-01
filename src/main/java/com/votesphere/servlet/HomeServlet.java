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
import java.sql.SQLException;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();
    private final UserDAO userDAO = new UserDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("polls", pollDAO.findAll(null).stream().limit(3).toList());
            request.setAttribute("activePolls", pollDAO.countActive());
            request.setAttribute("voters", userDAO.countVoters());
            request.setAttribute("votes", voteDAO.count());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load the home page.", e);
        }
    }
}
