<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%--
game-submenu:
	@modelObject - String pageNavigation - string representation of game page subnavigation
game-header:
	@modelObject - GameBasicDataDTO gameData - contains basic data of game
	
	@modelObject - List<SkaterStatsDTO> skaterStats - skaters data
	@modelObject - PlayerStatsFilterDTO playerStatsFilter - form filter data
--%>

<jsp:include page="/WEB-INF/views/sub_components/game-submenu.jsp"></jsp:include>

<jsp:include page="/WEB-INF/views/sub_components/game-header.jsp"></jsp:include>

<form:form id="player-filter" modelAttribute="playerStatsFilter" method="GET">
	<div>
		<label for="team-select">Team</label>
		<form:select id="team-select" path="regulationScope">
			<form:option value="Overall" label="All"/>
			<form:option value="Home" label="${gameData.homeTeamAbr}"/>
			<form:option value="Away" label="${gameData.awayTeamAbr}"/>
		</form:select>
	</div>
	<div>
		<label for="position-select">Position</label>
		<form:select id="position-select" path="playerPosition">
			<form:option value="Skaters" label="All"/>
			<form:option value="Center" label="C"/>
			<form:option value="Left Wing" label="LW"/>
			<form:option value="Right Wing" label="RW"/>
			<form:option value="Defenseman" label="D"/>
		</form:select>
	</div>
	<div class="form-checkbox-cont">
		<label for="productivity-check" title="if checked only productive players will be displayed">Productive</label>
		<form:checkbox id="productivity-check" path="onlyProductive"/>
	</div>
</form:form>

<table id="skater-stats" class="player-stats-tbl">
	<tr>
		<c:choose>
			<c:when test="${playerStatsFilter.isDescOrder}">
				<c:set var="orderSymbol" value="&#8595"/>
			</c:when>
			<c:otherwise>
				<c:set var="orderSymbol" value="&#8593"/>
			</c:otherwise>
		</c:choose>
		
		<th title="Team">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'Team'}"><span>Team ${orderSymbol}</span></c:when>
				<c:otherwise><span>Team</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Player Name">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'PlayerName'}"><span>Player ${orderSymbol}</span></c:when>
				<c:otherwise><span>Player</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Player Position">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'PlayerPosition'}"><span>Position ${orderSymbol}</span></c:when>
				<c:otherwise><span>Position</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Goals">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'Goals'}"><span>G ${orderSymbol}</span></c:when>
				<c:otherwise><span>G</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Assists">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'Assists'}"><span>A ${orderSymbol}</span></c:when>
				<c:otherwise><span>A</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Points">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'Points'}"><span>P ${orderSymbol}</span></c:when>
				<c:otherwise><span>P</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Plus Minus">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'PlusMinus'}"><span>+/- ${orderSymbol}</span></c:when>
				<c:otherwise><span>+/-</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Penalty Minutes">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'PenaltyMinutes'}"><span>PIM ${orderSymbol}</span></c:when>
				<c:otherwise><span>PIM</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Shots">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'Shots'}"><span>S ${orderSymbol}</span></c:when>
				<c:otherwise><span>S</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Blocked Shots">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'BlockedShots'}"><span>BS ${orderSymbol}</span></c:when>
				<c:otherwise><span>BS</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Time On Ice">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'TimeOnIce'}"><span>TOI ${orderSymbol}</span></c:when>
				<c:otherwise><span>TOI</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Faceoffs Overall">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'FaceoffsOverall'}"><span>FO ${orderSymbol}</span></c:when>
				<c:otherwise><span>FO</span></c:otherwise>
			</c:choose>
		</th>
		<th title="Faceoffs Percentage">
			<c:choose>
				<c:when test="${playerStatsFilter.orderByColumn == 'FaceoffsPercentage'}"><span>FO% ${orderSymbol}</span></c:when>
				<c:otherwise><span>FO%</span></c:otherwise>
			</c:choose>
		</th>
	</tr>
	<c:forEach var="player" varStatus="loop" items="${skaterStats}">
		<tr class="player ${player.playerId}">
			<td><img src="/NHL/src/img/team-icons/${player.team}.png" title="${player.team}" alt="${player.team}"></td>
			<td class="player-name">
				<span>${player.firstName} ${player.lastName}</span>
			</td>
			<td>${player.position.abbr}</td>
			<td>${player.goals}</td>
			<td>${player.assists}</td>
			<td>${player.points}</td>
			<td>${player.plusMinus}</td>
			<td>${player.penaltyMinutes}</td>
			<td>${player.shots}</td>
			<td>${player.blockedShots}</td>
			<td>${player.timeOnIce}</td>
			<td>${player.faceoffs}</td>
			<td title="won ${player.faceoffsWon} out of ${player.faceoffs}">${player.faceoffsPercentage}</td>
		</tr>
	</c:forEach>
</table>