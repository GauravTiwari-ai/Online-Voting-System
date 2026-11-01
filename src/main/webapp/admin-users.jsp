<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Registered users · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css"></head>
<body><%@ include file="/WEB-INF/views/nav.jspf" %><main class="container page-shell">
    <div class="page-heading"><div><span class="section-kicker">Administration</span><h1>Registered users</h1><p>Recently created accounts. Passwords and voting selections are never displayed here.</p></div></div>
    <div class="table-wrap"><table class="data-table"><thead><tr><th>Name</th><th>Username</th><th>Email</th><th>Role</th><th>Joined</th></tr></thead><tbody>
        <c:forEach var="user" items="${users}"><tr><td data-label="Name"><strong><c:out value="${user.fullName}"/></strong></td><td data-label="Username"><c:out value="${user.username}"/></td>
            <td data-label="Email"><c:out value="${user.email}"/></td><td data-label="Role"><c:out value="${user.role}"/></td>
            <td data-label="Joined"><c:out value="${user.createdAt}"/></td></tr></c:forEach>
    </tbody></table></div>
</main><%@ include file="/WEB-INF/views/footer.jspf" %><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
