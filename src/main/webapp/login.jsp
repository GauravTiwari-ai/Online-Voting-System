<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Log in · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head>
<body class="auth-page"><%@ include file="/WEB-INF/views/nav.jspf" %>
<main class="auth-layout"><section class="auth-promo"><a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark" aria-hidden="true"></span> VoteSphere</a>
    <div><span class="eyebrow">Good to see you again</span><h1>Welcome back.</h1><p>Your community is counting on your voice. Sign in to see what’s happening.</p></div>
    <div class="auth-promo-note"><span class="auth-note-icon">✓</span><span><strong>Your voice, your choice.</strong><small>Cast your vote with confidence.</small></span></div>
</section><section class="auth-form-wrap"><form class="auth-card" action="${pageContext.request.contextPath}/login" method="post">
    <input type="hidden" name="csrfToken" value="${requestScope.csrfToken}">
    <span class="section-kicker">Your account</span><h2>Sign in</h2><p class="form-intro">Enter your details to continue.</p>
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <c:if test="${not empty error}"><div class="notice notice-error" role="alert"><c:out value="${error}"/></div></c:if>
    <label for="login">Username or email</label><input id="login" name="login" type="text" autocomplete="username" required maxlength="254" placeholder="you@example.com">
    <label for="password">Password</label><div class="password-field"><input id="password" name="password" type="password" autocomplete="current-password" required><button type="button" class="password-toggle" aria-label="Show password">Show</button></div>
    <button class="button button-primary button-large full-width" type="submit">Log in <span>→</span></button>
    <p class="auth-switch">New to VoteSphere? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</form></section></main>
<script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
