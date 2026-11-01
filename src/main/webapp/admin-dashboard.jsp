<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Administrator · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Administration</span><h1>Welcome, <c:out value="${sessionScope.user.fullName}"/>.</h1><p>Manage polls and keep the community moving.</p></div><a class="button button-primary" href="${pageContext.request.contextPath}/admin/polls?new=1">＋ Create new poll</a></div>
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <div class="summary-grid"><article class="summary-card"><span class="summary-icon violet">♙</span><span class="summary-label">Registered users</span><strong>${userCount}</strong><small>Community members</small></article>
        <article class="summary-card"><span class="summary-icon blue">☷</span><span class="summary-label">Total polls</span><strong>${pollCount}</strong><small>Across all statuses</small></article>
        <article class="summary-card"><span class="summary-icon green">◉</span><span class="summary-label">Active polls</span><strong>${activeCount}</strong><small>Open for participation</small></article>
        <article class="summary-card"><span class="summary-icon orange">✓</span><span class="summary-label">Total votes</span><strong>${voteCount}</strong><small>Securely recorded</small></article></div>
    <div class="section-heading dashboard-section-heading"><div><span class="section-kicker">Recent activity</span><h2>Recently created polls</h2></div><a class="text-link" href="${pageContext.request.contextPath}/admin/polls">Manage polls →</a></div>
    <div class="table-wrap"><table class="data-table"><thead><tr><th>Poll</th><th>Status</th><th>Total votes</th><th>Created</th><th>Action</th></tr></thead><tbody>
        <c:forEach var="poll" items="${polls}"><tr><td data-label="Poll"><strong><c:out value="${poll.title}"/></strong></td><td data-label="Status"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span></td>
            <td data-label="Votes">${poll.totalVotes}</td><td data-label="Created"><c:out value="${poll.createdAt}"/></td><td data-label="Action"><a class="text-link" href="${pageContext.request.contextPath}/results?id=${poll.id}">View results →</a></td></tr></c:forEach>
    </tbody></table></div>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
