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

@WebServlet("/results")
public class ResultsServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            String pollId = request.getParameter("id");
            if (pollId == null || pollId.isBlank()) {
                request.setAttribute("polls", pollDAO.findAll(user.id()));
                request.getRequestDispatcher("/results-list.jsp").forward(request, response);
                return;
            }
            long id = Long.parseLong(pollId);
            Poll poll = pollDAO.findById(id, user.id());
            if (poll == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!poll.showResults() && !user.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Results are hidden by the poll administrator.");
                return;
            }
            long highest = poll.options().stream().mapToLong(option -> option.votes()).max().orElse(0);
            long leaders = poll.options().stream().filter(option -> option.votes() == highest).count();
            request.setAttribute("isTie", poll.totalVotes() > 0 && leaders > 1);
            request.setAttribute("winner", poll.options().stream()
                    .filter(option -> option.votes() == highest).map(option -> option.text())
                    .findFirst().orElse(""));
            request.setAttribute("poll", poll);
            request.getRequestDispatcher("/results.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (java.sql.SQLException e) {
            throw new ServletException("Unable to load poll results.", e);
        }
    }
}
