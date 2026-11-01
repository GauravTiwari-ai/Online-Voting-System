<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VoteSphere — Your Vote, Your Voice</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/views/nav.jspf" %>
<main>
    <section class="hero container">
        <div class="hero-copy">
            <span class="eyebrow"><span class="eyebrow-dot"></span> Your voice matters</span>
            <h1>Every vote counts.<br><span>Make yours matter.</span></h1>
            <p>Take part in the decisions that shape your community. Cast a secure vote and see your voice make a difference.</p>
            <div class="hero-actions"><a class="button button-primary button-large" href="${pageContext.request.contextPath}/polls">Explore polls <span aria-hidden="true">→</span></a>
                <a class="button button-light button-large" href="${pageContext.request.contextPath}/register">Create an account</a></div>
            <div class="hero-proof"><span class="proof-icon">✓</span> One person, one vote. Your participation matters.</div>
        </div>
        <div class="hero-art" aria-label="Illustration of a ballot being submitted">
            <div class="art-glow"></div><div class="art-orbit orbit-one"></div><div class="art-orbit orbit-two"></div>
            <div class="ballot-card"><div class="ballot-top"><span class="mini-mark">✓</span><span class="mini-lines"></span></div>
                <div class="ballot-option"><span class="check">✓</span><span class="line line-wide"></span></div>
                <div class="ballot-option"><span class="check"></span><span class="line line-mid"></span></div>
                <div class="ballot-option"><span class="check"></span><span class="line line-short"></span></div>
                <div class="ballot-submit">Vote submitted <span>✓</span></div></div>
            <div class="float-card float-votes"><span class="float-icon">✦</span><span><strong>Every voice</strong><small>makes a difference</small></span></div>
            <div class="float-card float-secure"><span class="shield">✓</span><span><strong>Secure voting</strong><small>Your vote is protected</small></span></div>
        </div>
    </section>
    <%@ include file="/WEB-INF/views/flash.jspf" %>
    <section class="stats-strip container" aria-label="Platform statistics">
        <div><strong><c:out value="${activePolls}"/></strong><span>Active polls</span></div>
        <div><strong><c:out value="${voters}"/></strong><span>Registered voters</span></div>
        <div><strong><c:out value="${votes}"/></strong><span>Votes recorded</span></div>
        <div class="stats-note"><span class="live-dot"></span> Real participation. Real impact.</div>
    </section>
    <section class="section container" id="polls">
        <div class="section-heading"><div><span class="section-kicker">Have your say</span><h2>Polls worth your time</h2>
            <p>Explore current conversations and make your perspective count.</p></div><a class="text-link" href="${pageContext.request.contextPath}/polls">Browse all polls <span>→</span></a></div>
        <div class="poll-grid">
            <c:choose><c:when test="${empty polls}"><div class="empty-state"><span class="empty-icon">◷</span><h3>No polls yet</h3><p>Check back soon for new ways to participate.</p></div></c:when>
                <c:otherwise><c:forEach var="poll" items="${polls}">
                    <article class="poll-card"><div class="poll-card-top"><span class="status-badge status-${poll.status}"><c:out value="${poll.status}"/></span><span class="card-arrow">↗</span></div>
                        <h3><c:out value="${poll.title}"/></h3><p><c:out value="${poll.description}"/></p>
                        <div class="poll-meta"><span>◉ ${poll.optionCount} options</span><span>▤ ${poll.totalVotes} votes</span></div>
                        <a class="button button-light card-button" href="${pageContext.request.contextPath}/poll?id=${poll.id}">View poll <span>→</span></a>
                    </article>
                </c:forEach></c:otherwise></c:choose>
        </div>
    </section>
    <section class="how-section" id="about"><div class="container section">
        <div class="section-heading centered"><span class="section-kicker">Simple by design</span><h2>Make your voice count in 3 steps</h2><p>Participation should be easy, clear, and secure.</p></div>
        <div class="steps-grid">
            <article class="step-card"><span class="step-number">01</span><span class="step-icon">♙</span><h3>Sign up or log in</h3><p>Create your account to access polls available to you.</p></article>
            <article class="step-card"><span class="step-number">02</span><span class="step-icon">☷</span><h3>Choose your option</h3><p>Review the choices and select the one that represents you.</p></article>
            <article class="step-card"><span class="step-number">03</span><span class="step-icon">✓</span><h3>Submit your vote</h3><p>Confirm your selection. One vote per person, securely recorded.</p></article>
        </div>
    </div></section>
</main>
<%@ include file="/WEB-INF/views/footer.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/main.js" defer></script>
</body></html>
