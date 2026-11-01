package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/polls")
public class PollsServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            request.setAttribute("polls", pollDAO.findAll(user == null ? null : user.id()));
            request.getRequestDispatcher("/polls.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load polls.", e);
        }
    }
}
