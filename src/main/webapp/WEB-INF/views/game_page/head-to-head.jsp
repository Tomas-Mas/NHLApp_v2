<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="head2head-section">
	<div class="h2h-section">
		<c:choose>
			<c:when test="${headToHead.get('Playoff').size() > 0}">
				<div class="h2h-label">Playoff Head To Head</div>
				<div class="h2h-list">
					<c:forEach items="${headToHead.get('Playoff')}" var="game">
						<c:choose>
							<c:when test="${game.id == gameData.id}">
								<div id="${game.id}" class="h2h-game selected">
							</c:when>
							<c:otherwise>
								<div id="${game.id}" class="h2h-game">
							</c:otherwise>
						</c:choose>
						<div class="h2h-team-info">
							<div class="h2h-team-pic">
								<img src="/NHL/src/img/team-icons/${game.homeTeamAbr}.png"
									title="${game.homeTeamName}" alt="${game.homeTeamName}">
							</div>
							<div class="h2h-team-name">${game.homeTeamAbr}</div>
						</div>
						<div class="h2h-result">
							<div class="h2h-game-date">${game.gameDate}</div>
							<div class="h2h-score">${game.homeScore} - ${game.awayScore}</div>
						</div>
						<div class="h2h-team-info">
							<div class="h2h-team-pic">
								<img src="/NHL/src/img/team-icons/${game.awayTeamAbr}.png"
									title="${game.awayTeamName}" alt="${game.awayTeamName}">
							</div>
							<div class="h2h-team-name">${gameData.awayTeamAbr}</div>
						</div></div>
					</c:forEach>
				</div>
			</c:when>
		</c:choose>
	</div>

	<div class="h2h-section">
		<div class="h2h-label">Regulation Head To Head</div>
		<div class="h2h-list">
			<c:forEach items="${headToHead.get('Regulation')}" var="game">
				<c:choose>
					<c:when test="${game.id == gameData.id}">
						<div id="${game.id}" class="h2h-game selected">
					</c:when>
					<c:otherwise>
						<div id="${game.id}" class="h2h-game">
					</c:otherwise>
				</c:choose>
				<div class="h2h-team-info">
					<div class="h2h-team-pic">
						<img src="/NHL/src/img/team-icons/${game.homeTeamAbr}.png"
							title="${game.homeTeamName}" alt="${game.homeTeamName}">
					</div>
					<div class="h2h-team-name">${game.homeTeamAbr}</div>
				</div>
				<div class="h2h-result">
					<div class="h2h-game-date">${game.gameDate}</div>
					<div class="h2h-score">${game.homeScore} - ${game.awayScore}</div>
				</div>
				<div class="h2h-team-info">
					<div class="h2h-team-pic">
						<img src="/NHL/src/img/team-icons/${game.awayTeamAbr}.png"
							title="${game.awayTeamName}" alt="${game.awayTeamName}">
					</div>
					<div class="h2h-team-name">${gameData.awayTeamAbr}</div>
				</div></div>
			</c:forEach>
		</div>
	</div>
</div>