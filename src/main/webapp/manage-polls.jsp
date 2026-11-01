<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manage polls · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Administration</span><h1>Manage polls</h1><p>Create, review, and manage poll availability.</p></div><a class="button button-primary" href="${pageContext.request.contextPath}/admin/polls?new=1">＋ Create poll</a></div>
    <%@ include file="/WEB-INF/views/flash.jspf" %><c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>
    <div class="table-wrap"><table class="data-table"><thead><tr><th>Poll ID</th><th>Poll title</th><th>Status</th><th>Total votes</th><th>Created</th><th>Actions</th></tr></thead><tbody>
        <c:forEach var="poll" items="${polls}"><tr><td data-label="Poll ID">#${poll.id}</td><td data-label="Poll title"><strong><c:out value="${poll.title}"/></strong></td>
            <td data-label="Status"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span></td><td data-label="Votes">${poll.totalVotes}</td>
            <td data-label="Created"><c:out value="${poll.createdAt}"/></td><td data-label="Actions"><div class="table-actions">
                <a class="text-link" href="${pageContext.request.contextPath}/poll?id=${poll.id}">View</a>
                <a class="text-link" href="${pageContext.request.contextPath}/admin/polls?edit=${poll.id}">Edit</a>
                <c:if test="${poll.status ne 'ACTIVE'}"><form action="${pageContext.request.contextPath}/admin/polls" method="post"><input type="hidden" name="csrfToken" value="${requestScope.csrfToken}"><input type="hidden" name="pollId" value="${poll.id}"><input type="hidden" name="action" value="activate"><button class="link-button" type="submit">Activate</button></form></c:if>
                <c:if test="${poll.status eq 'ACTIVE'}"><form action="${pageContext.request.contextPath}/admin/polls" method="post" data-confirm="Close this poll? Voting will stop immediately."><input type="hidden" name="csrfToken" value="${requestScope.csrfToken}"><input type="hidden" name="pollId" value="${poll.id}"><input type="hidden" name="action" value="close"><button class="link-button" type="submit">Close</button></form></c:if>
                <form action="${pageContext.request.contextPath}/admin/polls" method="post" data-confirm="Delete this poll and its votes? This cannot be undone."><input type="hidden" name="csrfToken" value="${requestScope.csrfToken}"><input type="hidden" name="pollId" value="${poll.id}"><input type="hidden" name="action" value="delete"><button class="link-button danger-link" type="submit">Delete</button></form>
            </div></td></tr></c:forEach>
    </tbody></table></div><c:if test="${empty polls}"><div class="empty-state"><h3>No polls created</h3><a class="button button-primary" href="${pageContext.request.contextPath}/admin/polls?new=1">Create your first poll</a></div></c:if>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
