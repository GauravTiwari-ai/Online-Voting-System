package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.model.Poll;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/poll")
public class PollServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long pollId = Long.parseLong(request.getParameter("id"));
            User user = (User) request.getSession().getAttribute("user");
            Poll poll = pollDAO.findById(pollId, user.id());
            if (poll == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("poll", poll);
            request.getRequestDispatcher("/poll-details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new ServletException("Unable to load poll.", e);
        }
    }
}
