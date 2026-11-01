<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Results · <c:out value="${poll.title}"/> · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <a class="back-link" href="${pageContext.request.contextPath}/dashboard">← Back to dashboard</a>
    <div class="page-heading results-heading"><div><span class="section-kicker">Poll results</span><h1><c:out value="${poll.title}"/></h1><p><c:out value="${poll.description}"/></p></div>
        <span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span></div>
    <div class="results-total"><span class="results-total-icon">▤</span><div><strong>${poll.totalVotes}</strong><span>total votes recorded</span></div></div>
    <section class="results-panel"><div class="section-heading"><div><span class="section-kicker">The breakdown</span><h2>Votes by option</h2></div></div>
        <c:choose><c:when test="${poll.totalVotes == 0}"><div class="empty-state"><span class="empty-icon">◷</span><h3>No votes recorded yet</h3><p>Results will appear here once people have voted.</p></div></c:when>
        <c:otherwise><div class="result-list"><c:forEach var="option" items="${poll.options}"><c:set var="percentage" value="${option.votes * 100.0 / poll.totalVotes}"/>
            <article class="result-row"><div class="result-label"><strong><c:out value="${option.text}"/></strong><span>${option.votes} votes · <fmt:formatNumber value="${percentage}" maxFractionDigits="1"/>%</span></div>
                <div class="progress-track"><div class="progress-fill" style="width: ${percentage}%"></div></div></article>
        </c:forEach></div>
        <c:if test="${poll.status eq 'CLOSED'}"><div class="winner-panel"><span class="winner-icon">✦</span><div><span class="section-kicker">Final result</span>
            <strong><c:choose><c:when test="${isTie}">It’s a tie!</c:when><c:otherwise><c:out value="${winner}"/> leads the poll</c:otherwise></c:choose></strong>
            <p><c:choose><c:when test="${isTie}">Multiple options received the same highest number of votes.</c:when><c:otherwise>Congratulations to the option with the most votes.</c:otherwise></c:choose></p></div></div></c:if>
        </c:otherwise></c:choose>
    </section>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
