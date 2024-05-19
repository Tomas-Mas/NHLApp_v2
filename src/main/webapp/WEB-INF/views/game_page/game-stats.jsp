<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<title>Game Stats</title>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/title.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-menu.css">
	
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/game-stats.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/submenu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/game-header.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/head-to-head.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/game_page/game-stats.js"></script>
</head>
<body>

	<jsp:include page="/WEB-INF/views/main_menu/main-menu.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/views/game_page/submenu.jsp"></jsp:include>
	
	<main>
	
		<div id="main-section">
		
			<jsp:include page="/WEB-INF/views/game_page/game-header.jsp"></jsp:include>
			
			<div id="period-nav">
				<c:choose>
					<c:when test="${periodNum.orElse(0) == 0}">
						<a class="selected" href="/NHL/game/${gameId}/gameStats">Overall</a>
					</c:when>
					<c:otherwise>
						<a href="/NHL/game/${gameId}/gameStats">Overall</a>
					</c:otherwise>
				</c:choose>
				<c:forEach items="${periods}" var="period" varStatus="loop">
					<c:choose>
						<c:when test="${period == 'REG'}"><c:set var="periodName" value="${loop.index + 1}. Period" scope="page"></c:set></c:when>
						<c:when test="${period == 'OT'}"><c:set var="periodName" value="${loop.index - 2}. OT" scope="page"></c:set></c:when>
						<c:when test="${period == 'SO'}"><c:set var="periodName" value="Shootout" scope="page"></c:set></c:when>
					</c:choose>
					<c:choose>
						<c:when test="${periodNum.orElse(0) == loop.index + 1}">
							<a class="selected" href="/NHL/game/${gameId}/gameStats/${loop.index + 1}">${periodName}</a>
						</c:when>
						<c:otherwise>
							<a href="/NHL/game/${gameId}/gameStats/${loop.index + 1}">${periodName}</a>
						</c:otherwise>
					</c:choose>
				</c:forEach> 
			</div>
			
			<div id="game-stats">
				
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').shotsOnGoal}</div>
						<div>Shots on Goal</div>
						<div>${gameStatsMap.get('away').shotsOnGoal}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').shots}</div>
						<div>Shots Overall</div>
						<div>${gameStatsMap.get('away').shots}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').blockedShots}</div>
						<div>Blocked Shots</div>
						<div>${gameStatsMap.get('away').blockedShots}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${Integer.valueOf(gameStatsMap.get('home').faceoffs / (gameStatsMap.get('home').faceoffs + gameStatsMap.get('away').faceoffs) * 100)}% 
								(${gameStatsMap.get('home').faceoffs})</div>
						<div>Faceoff %</div>
						<div>${Integer.valueOf(gameStatsMap.get('away').faceoffs / (gameStatsMap.get('home').faceoffs + gameStatsMap.get('away').faceoffs) * 100)}%
								(${gameStatsMap.get('away').faceoffs})</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').penaltyMinutes} (${gameStatsMap.get('home').penalties})</div>
						<div>Penalty Minutes (Penalties)</div>
						<div>${gameStatsMap.get('away').penaltyMinutes} (${gameStatsMap.get('away').penalties})</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${Integer.valueOf(gameStatsMap.get('home').powerPlayGoals / gameStatsMap.get('away').penalties * 100)}% 
								(${gameStatsMap.get('home').powerPlayGoals}/${gameStatsMap.get('away').penalties})</div>
						<div>Power-Play %</div>
						<div>${Integer.valueOf(gameStatsMap.get('away').powerPlayGoals / gameStatsMap.get('home').penalties * 100)}% 
								(${gameStatsMap.get('away').powerPlayGoals}/${gameStatsMap.get('home').penalties})</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').hits}</div>
						<div>Hits</div>
						<div>${gameStatsMap.get('away').hits}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').giveaways}</div>
						<div>Giveaways</div>
						<div>${gameStatsMap.get('away').giveaways}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').takeaways}</div>
						<div>Takeaways</div>
						<div>${gameStatsMap.get('away').takeaways}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
				<div class="stat-line">
					<div class="stat-line-text">
						<div>${gameStatsMap.get('home').missedShots}</div>
						<div>Missed Shots</div>
						<div>${gameStatsMap.get('away').missedShots}</div>
					</div>
					<div class="stat-line-graphics"><canvas></canvas></div>
				</div>
			</div>
		</div>
		
		
		<div id="stats-section">
			
			<jsp:include page="/WEB-INF/views/game_page/head-to-head.jsp"></jsp:include>
			
		</div>
	
	</main>

</body>
</html>