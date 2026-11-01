<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Dashboard · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Your VoteSphere</span><h1>Welcome, <c:out value="${sessionScope.user.fullName}"/>.</h1><p>Here’s what’s happening in your community.</p></div><a class="button button-primary" href="${pageContext.request.contextPath}/polls">Explore polls <span>→</span></a></div>
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <div class="summary-grid"><article class="summary-card"><span class="summary-icon violet">◉</span><span class="summary-label">Available polls</span><strong>${availableCount}</strong><small>Open for your vote</small></article>
        <article class="summary-card"><span class="summary-icon green">✓</span><span class="summary-label">Polls voted in</span><strong>${votedCount}</strong><small>Your participation matters</small></article>
        <article class="summary-card"><span class="summary-icon blue">☷</span><span class="summary-label">Remaining eligible polls</span><strong>${remainingCount}</strong><small>Still open to your vote</small></article></div>
    <div class="section-heading dashboard-section-heading"><div><span class="section-kicker">Your opportunities</span><h2>Polls for you</h2></div><a class="text-link" href="${pageContext.request.contextPath}/history">Voting activity →</a></div>
    <div class="poll-grid"><c:forEach var="poll" items="${polls}"><article class="poll-card"><div class="poll-card-top"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span>
        <c:if test="${poll.voted}"><span class="voted-label">✓ Voted</span></c:if></div><h3><c:out value="${poll.title}"/></h3><p><c:out value="${poll.description}"/></p>
        <div class="poll-meta"><span>◉ ${poll.optionCount} options</span><span>▤ ${poll.totalVotes} votes</span></div>
        <a class="button button-light card-button" href="${pageContext.request.contextPath}/poll?id=${poll.id}"><c:choose><c:when test="${poll.voted}">View poll</c:when><c:otherwise>View details</c:otherwise></c:choose> <span>→</span></a>
    </article></c:forEach></div>
    <c:if test="${empty polls}"><div class="empty-state"><span class="empty-icon">◷</span><h3>No polls just yet</h3><p>When polls open for participation, you’ll find them here.</p></div></c:if>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
