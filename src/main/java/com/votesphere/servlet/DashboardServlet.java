package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.dao.VoteDAO;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            var polls = pollDAO.findAll(user.id());
            long availableCount = polls.stream().filter(poll -> poll.isActive()).count();
            long remainingCount = polls.stream().filter(poll -> poll.isActive()).filter(poll -> !poll.voted()).count();
            request.setAttribute("polls", polls);
            request.setAttribute("votedCount", voteDAO.countByUser(user.id()));
            request.setAttribute("availableCount", availableCount);
            request.setAttribute("remainingCount", remainingCount);
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load dashboard.", e);
        }
    }
}
