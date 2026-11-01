<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Create account · VoteSphere</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head>
<body class="auth-page"><%@ include file="/WEB-INF/views/nav.jspf" %>
<main class="auth-layout"><section class="auth-promo"><a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark" aria-hidden="true"></span> VoteSphere</a>
    <div><span class="eyebrow">Be part of the conversation</span><h1>Your voice<br>starts here.</h1><p>Join a community where every perspective helps shape better decisions.</p></div>
    <div class="auth-promo-note"><span class="auth-note-icon">✦</span><span><strong>One community. Many voices.</strong><small>Make yours heard today.</small></span></div>
</section><section class="auth-form-wrap"><form class="auth-card" action="${pageContext.request.contextPath}/register" method="post" data-validate>
    <input type="hidden" name="csrfToken" value="${requestScope.csrfToken}">
    <span class="section-kicker">Get started</span><h2>Create your account</h2><p class="form-intro">A few details and you’re ready to participate.</p>
    <c:if test="${not empty error}"><div class="notice notice-error" role="alert"><c:out value="${error}"/></div></c:if>
    <label for="fullName">Full name</label><input id="fullName" name="fullName" autocomplete="name" required minlength="2" maxlength="120" placeholder="Your full name">
    <label for="username">Username</label><input id="username" name="username" autocomplete="username" required minlength="3" maxlength="40" pattern="[A-Za-z0-9_]+" placeholder="Choose a username">
    <label for="email">Email address</label><input id="email" name="email" type="email" autocomplete="email" required maxlength="254" placeholder="you@example.com">
    <label for="reg-password">Password</label><div class="password-field"><input id="reg-password" name="password" type="password" autocomplete="new-password" required minlength="8" maxlength="72" pattern="(?=.*[A-Za-z])(?=.*\d).{8,72}" aria-describedby="password-hint"><button type="button" class="password-toggle" aria-label="Show password">Show</button></div>
    <small id="password-hint" class="field-hint">At least 8 characters, including a letter and a number.</small>
    <label for="confirmPassword">Confirm password</label><input id="confirmPassword" name="confirmPassword" type="password" autocomplete="new-password" required minlength="8">
    <button class="button button-primary button-large full-width" type="submit">Create account <span>→</span></button>
    <p class="auth-switch">Already registered? <a href="${pageContext.request.contextPath}/login">Log in</a></p>
</form></section></main>
<script src="${pageContext.request.contextPath}/assets/js/validation.js" defer></script><script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script></body></html>
