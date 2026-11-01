package com.votesphere.servlet;

import com.votesphere.dao.VoteDAO;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            request.setAttribute("records", voteDAO.findByUser(user.id()));
            request.getRequestDispatcher("/voting-history.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load your voting history.", e);
        }
    }
}
