<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Available polls · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Make your voice heard</span><h1>Available polls</h1><p>Browse current polls and take part in the conversation.</p></div></div>
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <div class="poll-grid"><c:choose><c:when test="${empty polls}"><div class="empty-state"><span class="empty-icon">◷</span><h3>No polls available</h3><p>Please check back later.</p></div></c:when>
        <c:otherwise><c:forEach var="poll" items="${polls}"><article class="poll-card">
            <div class="poll-card-top"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span><c:if test="${poll.voted}"><span class="voted-label">✓ Voted</span></c:if></div>
            <h3><c:out value="${poll.title}"/></h3><p><c:out value="${poll.description}"/></p>
            <div class="poll-meta"><span>◉ ${poll.optionCount} options</span><span>▤ ${poll.totalVotes} votes</span></div>
            <a class="button button-light card-button" href="${pageContext.request.contextPath}/poll?id=${poll.id}">View details <span>→</span></a>
        </article></c:forEach></c:otherwise></c:choose></div>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
