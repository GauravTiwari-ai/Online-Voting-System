<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Voting activity · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Your participation</span><h1>Voting activity</h1><p>A private record of the polls you’ve taken part in.</p></div></div>
    <c:choose><c:when test="${empty records}"><div class="empty-state"><span class="empty-icon">▤</span><h3>Your activity starts here</h3><p>Once you vote in a poll, it’ll appear here.</p><a class="button button-primary" href="${pageContext.request.contextPath}/polls">Explore polls</a></div></c:when>
        <c:otherwise><div class="table-wrap"><table class="data-table"><thead><tr><th>Poll</th><th>Your selection</th><th>Date voted</th><th>Status</th><th></th></tr></thead><tbody>
            <c:forEach var="record" items="${records}"><tr><td data-label="Poll"><strong><c:out value="${record.pollTitle}"/></strong></td><td data-label="Your selection"><c:out value="${record.optionText}"/></td>
                <td data-label="Date voted"><c:out value="${record.votedAt}"/></td><td data-label="Status"><span class="status-badge status-${record.pollStatus}"><c:out value="${record.pollStatus}"/></span></td>
                <td data-label="Results"><c:if test="${record.showResults or sessionScope.user.role eq 'ADMIN'}"><a class="text-link" href="${pageContext.request.contextPath}/results?id=${record.pollId}">Results →</a></c:if></td></tr></c:forEach>
        </tbody></table></div></c:otherwise></c:choose>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
