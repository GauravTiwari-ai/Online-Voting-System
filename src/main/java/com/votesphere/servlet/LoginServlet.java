package com.votesphere.servlet;

import com.votesphere.dao.UserDAO;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Enter your username or email and password.");
            doGet(request, response);
            return;
        }
        try {
            User user = userDAO.authenticate(login.trim(), password);
            if (user == null) {
                request.setAttribute("error", "The username/email or password is incorrect.");
                doGet(request, response);
                return;
            }
            request.changeSessionId();
            request.getSession().setAttribute("user", user);
            request.getSession().removeAttribute("csrfToken");
            response.sendRedirect(request.getContextPath() + (user.isAdmin() ? "/admin" : "/dashboard"));
        } catch (SQLException e) {
            throw new ServletException("Unable to authenticate user.", e);
        }
    }
}
