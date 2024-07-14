<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--
game-submenu:
	@modelObject - String pageNavigation - string representation of game page subnavigation
	
	@modelObject - List<GameEventDTO> eventsBasicData - basid data of events for sidebar event's navigation
--%>

<div class="flex-submenu">
	<jsp:include page="/WEB-INF/views/sub_components/game-submenu.jsp"></jsp:include>
	<div class="flex-fill"></div>
</div>

<div id="simulation-container">

	<div id="simulation-main">
	
		<div id="game-header">
			<div id="home-?" class="home-team team-info">
				<div class="team-pic">
					<img src="" title="" alt="">
				</div>
				<div class="team-name"></div>
			</div>
			<div class="result">
				<div class="game-date"></div>
				<div class="score"></div>
				<div class="game-status"></div>
			</div>
			<div id="away-?" class="away-team team-info">
				<div class="team-pic">
					<img src="" title="" alt="">
				</div>
				<div class="team-name"></div>
			</div>
		</div>
		<div id="game-event-title">test</div>
	
		<div id="pitch-container">
			
			<div id="puck"><img src="/NHL/src/img/sim-icons/puck.png"></div>
			
			<div id="lineref-home" class="referee"><img src="/NHL/src/img/sim-icons/jersey-default-referee-linesman.png"></div>
			<div id="lineref-away" class="referee"><img src="/NHL/src/img/sim-icons/jersey-default-referee-linesman.png"></div>
			<div id="mainref-home" class="referee"><img src="/NHL/src/img/sim-icons/jersey-default-referee-main.png"></div>
			<div id="mainref-away" class="referee"><img src="/NHL/src/img/sim-icons/jersey-default-referee-main.png"></div>
			
			<div class="goalie goalie-home"></div>
			<div id="goalie-home" class="goalie goalie-home"></div>
			
			<div id="skater-c-home" class="player-animation skater-home"></div>
			<div id="skater-lw-home" class="player-animation skater-home"></div>
			<div id="skater-rw-home" class="player-animation skater-home"></div>
			<div id="skater-d-top-home" class="player-animation skater-home"></div>
			<div id="skater-d-bot-home" class="player-animation skater-home"></div>
			
			<div class="goalie goalie-away"></div>
			<div id="goalie-away" class="goalie goalie-away"></div>
			
			<div id="skater-c-away" class="player-animation skater-away"></div>
			<div id="skater-lw-away" class="player-animation skater-away"></div>
			<div id="skater-rw-away" class="player-animation skater-away"></div>
			<div id="skater-d-top-away" class="player-animation skater-away"></div>
			<div id="skater-d-bot-away" class="player-animation skater-away"></div>
			
		</div>
		
		<div class="simulation-controlls">
			<div class="sim-nav-btn">
				<input type="button" id="sim-stop" value="stop">
			</div>
			<div class="sim-nav-btn">
				<input type="button" id="sim-play" class="inactive" value="play">
			</div>
			<div class="sim-select">
				<select id="sim-speed" title="speed of passing seconds">
					<option value="8">8x</option>
					<option value="4" selected>4x</option>
					<option value="2">2x</option>
					<option value="1">1x</option>
					<option value="0.5">0.5x</option>
				</select>
			</div>
			<div class="sim-nav-btn">
				<input type="button" id="sim-prev" value="<">
			</div>
			<div class="sim-slider">
				<input type="range" id="sim-range" min="0" max="${eventsBasicData.size() - 1}" value="0">
			</div>
			<div class="sim-nav-btn">
				<input type="button" id="sim-next" value=">">
			</div>
		</div>
	</div>

	<div id="simulation-sidebar">
		<table>
			<c:forEach var="event" varStatus="loop" items="${eventsBasicData}">
				<tr class="event-${loop.index}">
					<td>${event.periodNumber}</td>
					<td>${event.periodTime}</td>
					<td title="${event.secondaryType}">${event.name}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	
</div>