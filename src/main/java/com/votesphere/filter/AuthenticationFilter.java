package com.votesphere.filter;

import com.votesphere.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(urlPatterns = {"/dashboard", "/poll", "/vote", "/results", "/history", "/admin", "/admin/*",
        "/dashboard.jsp", "/poll-details.jsp", "/vote-confirmation.jsp", "/results.jsp", "/results-list.jsp",
        "/voting-history.jsp", "/admin-dashboard.jsp", "/create-poll.jsp", "/edit-poll.jsp",
        "/manage-polls.jsp", "/admin-users.jsp"})
public class AuthenticationFilter implements Filter {
    private static final Set<String> ADMIN_PATHS = Set.of("/admin", "/admin/polls", "/admin-dashboard.jsp",
            "/create-poll.jsp", "/edit-poll.jsp", "/manage-polls.jsp", "/admin-users.jsp");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        User user = (User) httpRequest.getSession().getAttribute("user");
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        if (ADMIN_PATHS.contains(httpRequest.getServletPath()) && !user.isAdmin()) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
