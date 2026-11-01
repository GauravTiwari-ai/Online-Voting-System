package com.votesphere.servlet;

import com.votesphere.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = value(request, "fullName");
        String username = value(request, "username");
        String email = value(request, "email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");
        String error = null;
        if (fullName.length() < 2 || fullName.length() > 120 || !fullName.matches("[\\p{L} .'-]+")) {
            error = "Enter a valid full name.";
        } else if (!username.matches("[A-Za-z0-9_]{3,40}")) {
            error = "Username must be 3–40 characters using letters, numbers, or underscores.";
        } else if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") || email.length() > 254) {
            error = "Enter a valid email address.";
        } else if (password == null || !password.matches("(?=.*[A-Za-z])(?=.*\\d).{8,72}")) {
            error = "Password must be 8–72 characters and include at least one letter and number.";
        } else if (!password.equals(confirm)) {
            error = "The passwords do not match.";
        }
        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
            return;
        }
        try {
            userDAO.register(fullName, username, email, password);
            request.getSession().setAttribute("flash", "Account created. You can now sign in.");
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "That username or email is already registered.");
            doGet(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to register user.", e);
        }
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}
