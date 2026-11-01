<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Vote recorded · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container confirmation-shell">
    <section class="confirmation-card"><div class="success-burst"><span>✓</span></div><span class="section-kicker">Thank you for participating</span>
        <h1>Your vote has been recorded!</h1><p>Your choice is securely saved. Thank you for making your voice heard.</p>
        <div class="confirmation-detail"><span>Poll</span><strong><c:out value="${confirmedPoll}"/></strong><span>Your selection</span><strong><c:out value="${confirmedOption}"/></strong></div>
        <div class="confirmation-actions"><a class="button button-primary" href="${pageContext.request.contextPath}/results?id=${confirmedPollId}">View results</a>
            <a class="button button-light" href="${pageContext.request.contextPath}/dashboard">Return to dashboard</a></div>
    </section>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
