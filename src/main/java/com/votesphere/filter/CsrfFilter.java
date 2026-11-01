package com.votesphere.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter(urlPatterns = {"/login", "/register", "/logout", "/vote", "/dashboard", "/poll",
        "/results", "/history", "/polls", "/admin", "/admin/*", "/login.jsp", "/register.jsp",
        "/dashboard.jsp", "/poll-details.jsp", "/vote-confirmation.jsp", "/results.jsp", "/results-list.jsp",
        "/voting-history.jsp", "/admin-dashboard.jsp", "/create-poll.jsp", "/edit-poll.jsp",
        "/manage-polls.jsp", "/admin-users.jsp"})
public class CsrfFilter implements Filter {
    private static final String TOKEN_ATTRIBUTE = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        var session = httpRequest.getSession(true);
        String token = (String) session.getAttribute(TOKEN_ATTRIBUTE);
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(TOKEN_ATTRIBUTE, token);
        }
        httpRequest.setAttribute(TOKEN_ATTRIBUTE, token);

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String submitted = httpRequest.getParameter(TOKEN_ATTRIBUTE);
            if (submitted == null || !MessageDigest.isEqual(
                    token.getBytes(StandardCharsets.UTF_8), submitted.getBytes(StandardCharsets.UTF_8))) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "The form token is missing or expired.");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
