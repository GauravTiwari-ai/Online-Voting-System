package com.votesphere.servlet;

import com.votesphere.dao.PollDAO;
import com.votesphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@WebServlet("/admin/polls")
public class AdminPollServlet extends HttpServlet {
    private final PollDAO pollDAO = new PollDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String editId = request.getParameter("edit");
            if (editId != null) {
                long id = Long.parseLong(editId);
                var poll = pollDAO.findById(id, null);
                if (poll == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("poll", poll);
                request.setAttribute("deadlineValue", poll.deadline() == null ? ""
                        : poll.deadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                request.getRequestDispatcher("/edit-poll.jsp").forward(request, response);
                return;
            }
            if ("1".equals(request.getParameter("new"))) {
                request.getRequestDispatcher("/create-poll.jsp").forward(request, response);
                return;
            }
            request.setAttribute("polls", pollDAO.findAll(null));
            request.getRequestDispatcher("/manage-polls.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load polls.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                createPoll(request);
                request.getSession().setAttribute("flash", "Poll created successfully.");
            } else if ("edit".equals(action)) {
                long pollId = Long.parseLong(request.getParameter("pollId"));
                updatePoll(request, pollId);
                request.getSession().setAttribute("flash", "Poll updated successfully.");
            } else {
                long id = Long.parseLong(request.getParameter("pollId"));
                switch (action == null ? "" : action) {
                    case "activate" -> pollDAO.updateStatus(id, "ACTIVE");
                    case "close" -> pollDAO.updateStatus(id, "CLOSED");
                    case "delete" -> pollDAO.delete(id);
                    default -> throw new IllegalArgumentException("Unknown poll action.");
                }
                request.getSession().setAttribute("flash", "Poll updated successfully.");
            }
            response.sendRedirect(request.getContextPath() + "/admin/polls");
        } catch (IllegalArgumentException | DateTimeParseException e) {
            request.setAttribute("error", e.getMessage());
            if ("edit".equals(action)) {
                try {
                    long id = Long.parseLong(request.getParameter("pollId"));
                    var poll = pollDAO.findById(id, null);
                    if (poll == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("poll", poll);
                    request.setAttribute("deadlineValue", poll.deadline() == null ? ""
                            : poll.deadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                    request.getRequestDispatcher("/edit-poll.jsp").forward(request, response);
                } catch (Exception lookupError) {
                    throw new ServletException("Unable to reload poll for editing.", lookupError);
                }
            } else if ("create".equals(action)) {
                request.getRequestDispatcher("/create-poll.jsp").forward(request, response);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Unable to update poll.", e);
        }
    }

    private void createPoll(HttpServletRequest request) throws Exception {
        String title = value(request.getParameter("title"));
        String description = value(request.getParameter("description"));
        String status = value(request.getParameter("status"));
        List<String> options = parseOptions(request.getParameterValues("options"));
        LocalDateTime deadline = null;
        String deadlineValue = value(request.getParameter("deadline"));
        if (!deadlineValue.isEmpty()) {
            deadline = LocalDateTime.parse(deadlineValue);
            if (!deadline.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Deadline must be in the future.");
            }
        }
        if (title.length() < 3 || title.length() > 180) {
            throw new IllegalArgumentException("Poll title must be between 3 and 180 characters.");
        }
        if (description.length() < 5 || description.length() > 5000) {
            throw new IllegalArgumentException("Poll description must be between 5 and 5000 characters.");
        }
        if (options.size() < 2 || options.stream().anyMatch(option -> option.length() > 180)) {
            throw new IllegalArgumentException("Provide at least two unique options, each no longer than 180 characters.");
        }
        if (!List.of("UPCOMING", "ACTIVE", "CLOSED").contains(status)) {
            throw new IllegalArgumentException("Select a valid poll status.");
        }
        User user = (User) request.getSession().getAttribute("user");
        pollDAO.create(title, description, status, "on".equals(request.getParameter("showResults")),
                deadline, user.id(), options);
    }

    private void updatePoll(HttpServletRequest request, long pollId) throws Exception {
        String title = value(request.getParameter("title"));
        String description = value(request.getParameter("description"));
        String status = value(request.getParameter("status"));
        LocalDateTime deadline = parseDeadline(value(request.getParameter("deadline")));
        validateDetails(title, description, status);
        List<String> options = request.getParameterValues("options") == null
                ? null : parseOptions(request.getParameterValues("options"));
        if (options != null && (options.size() < 2 || options.stream().anyMatch(option -> option.length() > 180))) {
            throw new IllegalArgumentException("Provide at least two unique options, each no longer than 180 characters.");
        }
        pollDAO.updateDetails(pollId, title, description, status,
                "on".equals(request.getParameter("showResults")), deadline, options);
    }

    private LocalDateTime parseDeadline(String deadlineValue) {
        if (deadlineValue.isEmpty()) {
            return null;
        }
        LocalDateTime deadline = LocalDateTime.parse(deadlineValue);
        if (!deadline.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future.");
        }
        return deadline;
    }

    private List<String> parseOptions(String[] suppliedOptions) {
        List<String> options = suppliedOptions == null ? List.of() : Arrays.stream(suppliedOptions)
                .map(String::trim).filter(option -> !option.isEmpty()).toList();
        Set<String> normalized = new HashSet<>();
        if (options.stream().anyMatch(option -> !normalized.add(option.toLowerCase(Locale.ROOT)))) {
            throw new IllegalArgumentException("Voting options must be unique.");
        }
        return options;
    }

    private void validateDetails(String title, String description, String status) {
        if (title.length() < 3 || title.length() > 180) {
            throw new IllegalArgumentException("Poll title must be between 3 and 180 characters.");
        }
        if (description.length() < 5 || description.length() > 5000) {
            throw new IllegalArgumentException("Poll description must be between 5 and 5000 characters.");
        }
        if (!List.of("UPCOMING", "ACTIVE", "CLOSED").contains(status)) {
            throw new IllegalArgumentException("Select a valid poll status.");
        }
    }

    private String value(String value) {
        return value == null ? "" : value.trim();
    }
}
