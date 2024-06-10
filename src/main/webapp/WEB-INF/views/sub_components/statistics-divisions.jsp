<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="regulationContainerTable">
	<c:forEach var="division" items="${teamStandings.divisions}">
		<tr>
			<td>
				<table class="divisionTable">
					<tr class="statHeader">
						<th class="divisionName" colspan="3">${division}</th>
						<th class="sortableHeader" title="Games Played">GP</th>
						<th class="sortableHeader" title="Regulation wins">W</th>
						<th class="sortableHeader" title="Overtime/Shootout wins">OW</th>
						<th class="sortableHeader" title="Overtime/Shootout loses">OL</th>
						<th class="sortableHeader" title="Regulation loses">L</th>
						<th class="sortableHeader" title="Goals for">GF</th>
						<th class="sortableHeader" title="Goals against">GA</th>
						<th class="sortableHeader" title="Goals difference">+/-</th>
						<th class="sortableHeader" title="Points">P</th>
						<th class="sortableHeader" title="Points percentage">P%</th>
					</tr>
					<c:forEach var="team" varStatus="loop" items="${teamStandings.getTeamsByDivision(division)}">
						<tr class="team ${team.teamId} highlightable">
							<td class="numeric rank">${loop.index + 1}</td>
							<td>
								<div class="teamPic">
									<img src="/NHL/src/img/team-icons/${team.teamAbbreviation}.png" title="${team.teamName}" alt="${team.teamName}">
								</div>
							</td>
							<td id="${team.teamId}" class="teamName">${team.teamName}</td>
							<td class="numeric">${team.games}</td>
							<td class="numeric">${team.regWins}</td>
							<td class="numeric">${team.otWins}</td>
							<td class="numeric">${team.otLoses}</td>
							<td class="numeric">${team.regLoses}</td>
							<td class="numeric">${team.goalsFor}</td>
							<td class="numeric">${team.goalsAgainst}</td>
							<td class="numeric">${team.goalsFor - team.goalsAgainst}</td>
							<td class="numeric">${team.points}</td>
							<td class="numeric">${team.pointPercentage / 1000}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:forEach>
</table>