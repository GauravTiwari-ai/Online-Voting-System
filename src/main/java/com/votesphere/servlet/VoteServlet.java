package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.dao.VoteDAO;
import com.votesphere.model.Poll;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    private final VoteDAO voteDAO = new VoteDAO();
    private final PollDAO pollDAO = new PollDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            long pollId = Long.parseLong(request.getParameter("pollId"));
            long optionId = Long.parseLong(request.getParameter("optionId"));
            Poll poll = pollDAO.findById(pollId, user.id());
            if (poll == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (poll.voted()) {
                request.getSession().setAttribute("flashError", "You have already voted in this poll.");
                response.sendRedirect(request.getContextPath() + "/poll?id=" + pollId);
                return;
            }
            voteDAO.castVote(pollId, optionId, user.id());
            request.getSession().setAttribute("confirmedPollId", pollId);
            request.getSession().setAttribute("confirmedPoll", poll.title());
            request.getSession().setAttribute("confirmedOption",
                    poll.options().stream().filter(option -> option.id() == optionId)
                            .map(option -> option.text()).findFirst().orElse(""));
            response.sendRedirect(request.getContextPath() + "/vote/confirmation");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalStateException | IllegalArgumentException e) {
            request.getSession().setAttribute("flashError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/poll?id=" + request.getParameter("pollId"));
        } catch (SQLIntegrityConstraintViolationException e) {
            request.getSession().setAttribute("flashError", "You have already voted in this poll.");
            response.sendRedirect(request.getContextPath() + "/poll?id=" + request.getParameter("pollId"));
        } catch (Exception e) {
            throw new ServletException("Unable to record vote.", e);
        }
    }
}
