package com.votesphere.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vote/confirmation")
public class ConfirmationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var session = request.getSession();
        Object poll = session.getAttribute("confirmedPoll");
        Object option = session.getAttribute("confirmedOption");
        Object pollId = session.getAttribute("confirmedPollId");
        if (poll == null || option == null || pollId == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.setAttribute("confirmedPoll", poll);
        request.setAttribute("confirmedOption", option);
        request.setAttribute("confirmedPollId", pollId);
        session.removeAttribute("confirmedPoll");
        session.removeAttribute("confirmedOption");
        session.removeAttribute("confirmedPollId");
        request.getRequestDispatcher("/vote-confirmation.jsp").forward(request, response);
    }
}
