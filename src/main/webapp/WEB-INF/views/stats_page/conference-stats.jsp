<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="regulationContainerTable">
	<c:forEach var="conference" items="${teamStandings.conferences}">
		<tr>
			<td>
				<table class="conferenceTable">
					<tr class="statHeader">
						<th class="conferenceName" colspan="3">${conference}</th>
						<th title="Games Played">GP</th>
						<th title="Regulation wins">W</th>
						<th title="Overtime/Shootout wins">OW</th>
						<th title="Overtime/Shootout loses">OL</th>
						<th title="Regulation loses">L</th>
						<th title="Goals for">GF</th>
						<th title="Goals against">GA</th>
						<th title="Goals difference">+/-</th>
						<th title="Points">P</th>
						<th title="Points percentage">P%</th>
					</tr>
					<c:forEach var="team" varStatus="loop" items="${teamStandings.getTeamsByConference(conference)}">
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