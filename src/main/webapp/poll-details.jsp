<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${poll.title}"/> · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container narrow-shell">
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <a class="back-link" href="${pageContext.request.contextPath}/dashboard">← Back to dashboard</a>
    <section class="vote-panel"><div class="poll-card-top"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span>
        <c:if test="${not empty poll.deadline}"><span class="deadline">Voting closes <c:out value="${poll.deadline}"/></span></c:if></div>
        <span class="section-kicker">Cast your vote</span><h1><c:out value="${poll.title}"/></h1><p class="poll-description"><c:out value="${poll.description}"/></p>
        <c:if test="${poll.active and not poll.voted}"><a class="button button-primary button-large full-width" href="#vote-form">Vote now <span>→</span></a></c:if>
        <div class="instruction-box"><span>ⓘ</span><p>Select one option below. Your selection can’t be changed after submission.</p></div>
        <c:choose><c:when test="${poll.voted}"><div class="already-voted"><span>✓</span><div><strong>You have already voted in this poll.</strong><p>Thank you for making your voice count.</p></div></div></c:when>
            <c:when test="${not poll.active}"><div class="notice"><strong>This poll is not open for voting.</strong> You can still view results if they are available.</div></c:when>
            <c:otherwise><form id="vote-form" action="${pageContext.request.contextPath}/vote" method="post" class="vote-form" data-confirm="Submit your vote? You can only vote once in this poll.">
                <input type="hidden" name="csrfToken" value="${requestScope.csrfToken}">
                <input type="hidden" name="pollId" value="${poll.id}">
                <fieldset><legend class="option-legend">Choose one option</legend><div class="options-list"><c:forEach var="option" items="${poll.options}">
                    <label class="option-card"><input type="radio" name="optionId" value="${option.id}" required>
                        <span class="radio-indicator"></span><span class="option-copy"><strong><c:out value="${option.text}"/></strong>
                        <c:if test="${not empty option.description}"><small><c:out value="${option.description}"/></small></c:if></span></label>
                </c:forEach></div></fieldset>
                <button class="button button-primary button-large full-width submit-vote" type="submit">Submit my vote <span>→</span></button>
            </form></c:otherwise></c:choose>
        <c:if test="${poll.showResults or sessionScope.user.role eq 'ADMIN'}"><a class="button button-light full-width results-link" href="${pageContext.request.contextPath}/results?id=${poll.id}">View results</a></c:if>
    </section>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/voting.js" defer></script><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
