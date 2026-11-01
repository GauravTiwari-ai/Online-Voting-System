<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Edit poll · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container narrow-shell">
    <a class="back-link" href="${pageContext.request.contextPath}/admin/polls">← Manage polls</a><section class="form-panel">
        <span class="section-kicker">Poll management</span><h1>Edit poll</h1><p class="form-intro">Update this poll’s details. Existing voting options and votes are retained.</p>
        <c:if test="${not empty error}"><div class="notice notice-error" role="alert"><c:out value="${error}"/></div></c:if>
        <form action="${pageContext.request.contextPath}/admin/polls" method="post">
            <input type="hidden" name="csrfToken" value="${requestScope.csrfToken}">
            <input type="hidden" name="action" value="edit"><input type="hidden" name="pollId" value="${poll.id}">
            <label for="title">Poll title</label><input id="title" name="title" value="<c:out value='${poll.title}'/>" required minlength="3" maxlength="180">
            <label for="description">Description</label><textarea id="description" name="description" required minlength="5" maxlength="5000" rows="4"><c:out value="${poll.description}"/></textarea>
            <label for="status">Poll status</label><select id="status" name="status" required>
                <option value="UPCOMING" ${poll.status eq 'UPCOMING' ? 'selected' : ''}>Upcoming</option>
                <option value="ACTIVE" ${poll.status eq 'ACTIVE' ? 'selected' : ''}>Active</option>
                <option value="CLOSED" ${poll.status eq 'CLOSED' ? 'selected' : ''}>Closed</option></select>
            <label for="deadline">Voting deadline <span class="optional">(optional)</span></label><input id="deadline" name="deadline" type="datetime-local" value="${deadlineValue}">
            <label class="checkbox-label"><input type="checkbox" name="showResults" ${poll.showResults ? 'checked' : ''}><span>Allow voters to see results</span></label>
            <div class="form-section-row"><div><h2>Voting options (${poll.optionCount})</h2>
                <c:choose><c:when test="${poll.totalVotes == 0}"><p>Options can be changed until the first vote is recorded.</p></c:when>
                    <c:otherwise><p>Options are locked because this poll already has votes.</p></c:otherwise></c:choose></div>
                <c:if test="${poll.totalVotes == 0}"><button class="button button-light" type="button" id="add-option">＋ Add option</button></c:if>
            </div>
            <div class="option-inputs"><c:forEach var="option" items="${poll.options}" varStatus="loop"><div class="option-input-row">
                <c:choose><c:when test="${poll.totalVotes == 0}">
                    <label class="sr-only" for="option-${loop.index + 1}">Option ${loop.index + 1}</label>
                    <input id="option-${loop.index + 1}" name="options" value="<c:out value='${option.text}'/>" required maxlength="180">
                    <button class="remove-option" type="button" aria-label="Remove option ${loop.index + 1}">×</button>
                </c:when><c:otherwise><input value="<c:out value='${option.text}'/>" disabled></c:otherwise></c:choose>
            </div></c:forEach></div>
            <div class="form-actions"><a class="button button-light" href="${pageContext.request.contextPath}/admin/polls">Cancel</a><button class="button button-primary" type="submit">Save changes <span>→</span></button></div>
        </form></section></main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script><script src="${pageContext.request.contextPath}/assets/js/admin.js" defer></script></body></html>
