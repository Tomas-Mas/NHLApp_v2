<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="playerStatsContainerTable">
	<tr class="statHeader">
		<th title="Rank">#</th>
		<th title="Player Name">Player</th>
		<!-- <th title="Season">Season</th>  -->
		<!-- <th title="Teams Played For">Teams</th>  -->
		<th title="Player Position">Pos</th>
		<th title="Games Played">
			<form:radiobutton id="GamesPlayed" path="orderedBy" value="GamesPlayed"/>
			<label class="tblColHeader" for="GamesPlayed">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='GamesPlayed' && statsNavigation.reverseOrder}">GP &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='GamesPlayed' && !statsNavigation.reverseOrder}">GP &#8595</c:when>
					<c:otherwise>GP</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Goals">
			<form:radiobutton id="Goals" path="orderedBy" value="Goals"/>
			<label class="tblColHeader" for="Goals">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='Goals' && statsNavigation.reverseOrder}">G &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='Goals' && !statsNavigation.reverseOrder}">G &#8595</c:when>
					<c:otherwise>G</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Assists">
			<form:radiobutton id="Assists" path="orderedBy" value="Assists"/>
			<label class="tblColHeader" for="Assists">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='Assists' && statsNavigation.reverseOrder}">A &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='Assists' && !statsNavigation.reverseOrder}">A &#8595</c:when>
					<c:otherwise>A</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Points">
			<form:radiobutton id="Points" path="orderedBy" value="Points"/>
			<label class="tblColHeader" for="Points">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='Points' && statsNavigation.reverseOrder}">P &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='Points' && !statsNavigation.reverseOrder}">P &#8595</c:when>
					<c:otherwise>P</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Plus/Minus">
			<form:radiobutton id="PlusMinus" path="orderedBy" value="PlusMinus"/>
			<label class="tblColHeader" for="PlusMinus">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='PlusMinus' && statsNavigation.reverseOrder}">+/- &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='PlusMinus' && !statsNavigation.reverseOrder}">+/- &#8595</c:when>
					<c:otherwise>+/-</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Penalty Minutes">
			<form:radiobutton id="PenaltyMinutes" path="orderedBy" value="PenaltyMinutes"/>
			<label class="tblColHeader" for="PenaltyMinutes">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='PenaltyMinutes' && statsNavigation.reverseOrder}">PIM &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='PenaltyMinutes' && !statsNavigation.reverseOrder}">PIM &#8595</c:when>
					<c:otherwise>PIM</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Points Per Games Played">P/GP</th>
		<th title="Shots">
			<form:radiobutton id="Shots" path="orderedBy" value="Shots"/>
			<label class="tblColHeader" for="Shots">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='Shots' && statsNavigation.reverseOrder}">S &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='Shots' && !statsNavigation.reverseOrder}">S &#8595</c:when>
					<c:otherwise>S</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th title="Shooting Percentage">S%</th>
		<th title="Blocked Shots">
			<form:radiobutton id="BlockedShots" path="orderedBy" value="BlockedShots"/>
			<label class="tblColHeader" for="BlockedShots">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='BlockedShots' && statsNavigation.reverseOrder}">BS &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='BlockedShots' && !statsNavigation.reverseOrder}">BS &#8595</c:when>
					<c:otherwise>BS</c:otherwise>
				</c:choose>
			</label>
		</th>
		<!-- <th title="Time On Ice Average">TOI/GP</th>  -->
		<th title="Time On Ice Average">
			<form:radiobutton id="TimeOnIceAvg" path="orderedBy" value="TimeOnIceAvg"/>
			<label class="tblColHeader" for="TimeOnIceAvg">
				<c:choose>
					<c:when test="${statsNavigation.orderedBy=='TimeOnIceAvg' && statsNavigation.reverseOrder}">TOI/GP &#8593</c:when>
					<c:when test="${statsNavigation.orderedBy=='TimeOnIceAvg' && !statsNavigation.reverseOrder}">TOI/GP &#8595</c:when>
					<c:otherwise>TOI/GP</c:otherwise>
				</c:choose>
			</label>
		</th>
		<th id="orderSettings">
			<form:radiobutton id="true" path="reverseOrder" value="true"/>
			<form:radiobutton id="false" path="reverseOrder" value="false"/>
		</th>
	</tr>
	<c:forEach var="player" varStatus="loop" items="${skatersStats}">
		<tr class="player ${player.playerId} highlightable">
			<td class="numeric">${(statsNavigation.selectedPageNumber - 1) * statsNavigation.rowsPerPage + loop.index + 1}</td>
			<td class="playerName">${player.firstName} ${player.lastName}</td>
			<td>${player.position.abbr}</td>
			<td>${player.gamesPlayed}</td>
			<td>${player.goals}</td>
			<td>${player.assists}</td>
			<td>${player.points}</td>
			<td>${player.plusMinus}</td>
			<td>${player.penaltyMinutes}</td>
			<td>${player.getPointsPerGamesPlayed()}</td>
			<td>${player.shots}</td>
			<td>${player.getShootingPercentage()}</td>
			<td>${player.blockedShots}</td>
			<td>${player.timeOnIceAvg}</td>
		</tr>
	</c:forEach>
</table>