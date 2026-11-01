<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Poll results · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">The community has spoken</span><h1>Poll results</h1><p>Explore recorded results. Polls with hidden results are available to administrators only.</p></div></div>
    <div class="poll-grid"><c:forEach var="poll" items="${polls}"><article class="poll-card">
        <div class="poll-card-top"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span><span class="poll-meta">${poll.totalVotes} votes</span></div>
        <h3><c:out value="${poll.title}"/></h3><p><c:out value="${poll.description}"/></p>
        <c:choose><c:when test="${poll.showResults or sessionScope.user.role eq 'ADMIN'}"><a class="button button-light card-button" href="${pageContext.request.contextPath}/results?id=${poll.id}">View results <span>→</span></a></c:when>
            <c:otherwise><div class="notice">Results are hidden by the poll administrator.</div></c:otherwise></c:choose>
    </article></c:forEach></div>
    <c:if test="${empty polls}"><div class="empty-state"><h3>No poll results yet</h3></div></c:if>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
