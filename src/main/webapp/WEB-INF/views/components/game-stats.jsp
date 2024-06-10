<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<%--
game-submenu:
	@modelObject - String pageNavigation - string representation of game page subnavigation
game-header:
	@modelObject - GameBasicDataDTO gameData - contains basic data of game
	
	@modelObject - int periodNum - period number (0 for all periods combined)
	@modelObject - List<String> periods - list with all periods types ordered by period number
	@modelObject - Map<String, GameStatsDTO> gameStatsMap - game stats data mapped for each team
--%>

<jsp:include page="/WEB-INF/views/sub_components/game-submenu.jsp"></jsp:include>

<jsp:include page="/WEB-INF/views/sub_components/game-header.jsp"></jsp:include>

<div id="period-nav">
	<c:choose>
		<c:when test="${periodNum.orElse(0) == 0}">
			<span class="selected">Overall</span>
		</c:when>
		<c:otherwise>
			<span>Overall</span>
		</c:otherwise>
	</c:choose>
	<c:forEach items="${periods}" var="period" varStatus="loop">
		<c:choose>
			<c:when test="${period == 'REG'}">
				<c:set var="periodName" value="${loop.index + 1}. Period"
					scope="page"></c:set>
			</c:when>
			<c:when test="${period == 'OT'}">
				<c:set var="periodName" value="${loop.index - 2}. OT" scope="page"></c:set>
			</c:when>
			<c:when test="${period == 'SO'}">
				<c:set var="periodName" value="Shootout" scope="page"></c:set>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${periodNum.orElse(0) == loop.index + 1}">
				<span class="selected">${periodName}</span>
			</c:when>
			<c:otherwise>
				<span>${periodName}</span>
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
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').shots}</div>
			<div>Shots Overall</div>
			<div>${gameStatsMap.get('away').shots}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').blockedShots}</div>
			<div>Blocked Shots</div>
			<div>${gameStatsMap.get('away').blockedShots}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${Integer.valueOf(gameStatsMap.get('home').faceoffs / (gameStatsMap.get('home').faceoffs + gameStatsMap.get('away').faceoffs) * 100)}%
				(${gameStatsMap.get('home').faceoffs})</div>
			<div>Faceoff %</div>
			<div>${Integer.valueOf(gameStatsMap.get('away').faceoffs / (gameStatsMap.get('home').faceoffs + gameStatsMap.get('away').faceoffs) * 100)}%
				(${gameStatsMap.get('away').faceoffs})</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').penaltyMinutes}
				(${gameStatsMap.get('home').penalties})</div>
			<div>Penalty Minutes (Penalties)</div>
			<div>${gameStatsMap.get('away').penaltyMinutes}
				(${gameStatsMap.get('away').penalties})</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${Integer.valueOf(gameStatsMap.get('home').powerPlayGoals / gameStatsMap.get('away').penalties * 100)}%
				(${gameStatsMap.get('home').powerPlayGoals}/${gameStatsMap.get('away').penalties})</div>
			<div>Power-Play %</div>
			<div>${Integer.valueOf(gameStatsMap.get('away').powerPlayGoals / gameStatsMap.get('home').penalties * 100)}%
				(${gameStatsMap.get('away').powerPlayGoals}/${gameStatsMap.get('home').penalties})</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').hits}</div>
			<div>Hits</div>
			<div>${gameStatsMap.get('away').hits}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').giveaways}</div>
			<div>Giveaways</div>
			<div>${gameStatsMap.get('away').giveaways}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').takeaways}</div>
			<div>Takeaways</div>
			<div>${gameStatsMap.get('away').takeaways}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
	<div class="stat-line">
		<div class="stat-line-text">
			<div>${gameStatsMap.get('home').missedShots}</div>
			<div>Missed Shots</div>
			<div>${gameStatsMap.get('away').missedShots}</div>
		</div>
		<div class="stat-line-graphics">
			<canvas></canvas>
		</div>
	</div>
</div>