<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Create poll · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container narrow-shell">
    <a class="back-link" href="${pageContext.request.contextPath}/admin/polls">← Manage polls</a><section class="form-panel">
        <span class="section-kicker">Poll management</span><h1>Create a new poll</h1><p class="form-intro">Write a clear question and give voters meaningful options.</p>
        <c:if test="${not empty error}"><div class="notice notice-error" role="alert"><c:out value="${error}"/></div></c:if>
        <form action="${pageContext.request.contextPath}/admin/polls" method="post" data-validate>
            <input type="hidden" name="csrfToken" value="${requestScope.csrfToken}">
            <input type="hidden" name="action" value="create">
            <label for="title">Poll title</label><input id="title" name="title" required minlength="3" maxlength="180" placeholder="What would you like to ask?">
            <label for="description">Description</label><textarea id="description" name="description" required minlength="5" maxlength="5000" rows="4" placeholder="Add context and instructions for voters"></textarea>
            <div class="form-section-row"><div><h2>Voting options</h2><p>Add at least two unique options.</p></div><button class="button button-light" type="button" id="add-option">＋ Add option</button></div>
            <div class="option-inputs"><div class="option-input-row"><label class="sr-only" for="option-1">Option 1</label><input id="option-1" name="options" required maxlength="180" placeholder="Option 1"></div>
                <div class="option-input-row"><label class="sr-only" for="option-2">Option 2</label><input id="option-2" name="options" required maxlength="180" placeholder="Option 2"></div></div>
            <label for="status">Poll status</label><select id="status" name="status" required><option value="UPCOMING">Upcoming</option><option value="ACTIVE">Active</option><option value="CLOSED">Closed</option></select>
            <label for="deadline">Voting deadline <span class="optional">(optional)</span></label><input id="deadline" name="deadline" type="datetime-local">
            <label class="checkbox-label"><input type="checkbox" name="showResults" checked><span>Allow voters to see results</span></label>
            <div class="form-actions"><a class="button button-light" href="${pageContext.request.contextPath}/admin/polls">Cancel</a><button class="button button-primary" type="submit">Save poll <span>→</span></button></div>
        </form></section></main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script><script src="${pageContext.request.contextPath}/assets/js/admin.js" defer></script></body></html>
