package com.votesphere.servlet;

import com.votesphere.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("users", userDAO.findRecent());
            request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load registered users.", e);
        }
    }
}
