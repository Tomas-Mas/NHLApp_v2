<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/title.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-menu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game-overview.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/game_page/overview.js"></script>
</head>

<body>

	<jsp:include page="/WEB-INF/views/main_menu/main-menu.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/views/game_page/submenu.jsp"></jsp:include>
	
	<main>
		<div id="main-section">
		
			<div id="score-section">
				<div class="home-team team-info">
					<div class="team-pic">
						<a href="/NHL/team/${gameData.homeTeamId}">
							<img src="/NHL/src/img/team-icons/${gameData.homeTeamAbr}.png" title="${gameData.homeTeamName}" alt="${gameData.homeTeamName}">
						</a>
					</div>
					<div class="team-name">
						<a href="/NHL/team/${gameData.homeTeamId}">${gameData.homeTeamName}</a>
					</div>
				</div>
				<div class="result">
					<div class="game-date">${gameData.gameDate}</div>
					<div class="score">${gameData.homeScore} - ${gameData.awayScore}</div>
					<div class="game-status">${gameData.gameStatus}</div>
				</div>
				<div class="away-team team-info">
					<div class="team-pic">
						<a href="/NHL/team/${gameData.awayTeamId}">
							<img src="/NHL/src/img/team-icons/${gameData.awayTeamAbr}.png" title="${gameData.awayTeamName}" alt="${gameData.awayTeamName}">
						</a>
					</div>
					<div class="team-name">
						<a href="/NHL/team/${gameData.awayTeamId}">${gameData.awayTeamName}</a>
					</div>
				</div>
			</div>
			
			<div id="event-section">
				<table>
					<tr>
						<jsp:include page="/WEB-INF/views/main_page/game-detail.jsp"></jsp:include>
					</tr>
				</table>
			</div>
		</div>
		
		<div id="stats-section">
		
			<div id="head2head-section">
				<div class="h2h-section">
					<c:choose>
						<c:when test="${headToHead.get('Playoff').size() > 0}">
							<div class="h2h-label">Playoff Head To Head</div>
							<div class="h2h-list">
								<c:forEach items="${headToHead.get('Playoff')}" var="game">
									<c:choose>
										<c:when test="${game.id == gameId}">
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
										<div class="h2h-score">${game.homeScore}-
											${game.awayScore}</div>
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
								<c:when test="${game.id == gameId}">
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
							<div class="h2h-score">${game.homeScore}- ${game.awayScore}</div>
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
		</div>
		
	</main>

</body>

</html>