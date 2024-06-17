<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
	@modelObject - Map<String, List<GameBasicDataDTO> lastGamesMap - lastGames data for home and away team
--%>

<form:form id="last-games-form" modelAttribute="lastGamesNavigation" method="GET">
	
	<c:set var="homeAbr" value="${lastGamesMap.keySet().toArray()[0].toString()}"/>
	<c:set var="awayAbr" value="${lastGamesMap.keySet().toArray()[1].toString()}"/>

	<div class="h2h-label">Last games</div>
	<div class="last-games-header">
		<div class="header-pic">
			<img src="/NHL/src/img/team-icons/${homeAbr}.png"
				title="${homeAbr}" alt="${homeAbr}">
		</div>
		<div class="scope-selector">
			<form:select path="homeScope">
				<form:option value="Overall" label="All"/>
				<form:option value="Home" label="Home"/>
				<form:option value="Away" label="Away"/>
			</form:select>
		</div>
		<div class="nav-lock">
			<label for="nav-locker">&#128274;</label>
			<input type="checkbox" id="nav-locker" title="check this to lock sidebar to current game">
		</div>
		<div class="scope-selector">
			<form:select path="awayScope">
				<form:option value="Overall" label="All"/>
				<form:option value="Home" label="Home"/>
				<form:option value="Away" label="Away"/>
			</form:select>
		</div>
		<div class="header-pic">
			<img src="/NHL/src/img/team-icons/${awayAbr}.png"
				title="${awayAbr}" alt="${awayAbr}">
		</div>
	</div>
	
	<div id="last-games-data">
		<table class="last-games-tbl">
			<c:forEach var="game" items="${lastGamesMap.get(homeAbr)}">
				<c:set var="gameResultClass"/>
				<c:set var="endPeriodType"/>
				<c:choose>
					<c:when test="${(game.homeTeamAbr == homeAbr && game.homeScore > game.awayScore) || (game.awayTeamAbr == homeAbr && game.awayScore > game.homeScore)}">
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="gameResultClass" value="match-win"/>
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="gameResultClass" value="match-otwin"/>
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="gameResultClass" value="match-sowin"/>
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:when>
					<c:when test="${(game.homeTeamAbr == homeAbr && game.homeScore < game.awayScore) || game.awayTeamAbr == homeAbr && game.awayScore < game.homeScore}">
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="gameResultClass" value="match-loss"/>
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="gameResultClass" value="match-otloss"/>
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="gameResultClass" value="match-soloss"/>
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="gameResultClass" value="match-tie"/>
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				
				<tr id="${game.id}" class="${gameResultClass}">
					<td class="match-result">
						<c:choose>
							<c:when test="${game.homeTeamAbr == homeAbr}">
								<span class="score">${game.homeScore} - ${game.awayScore}</span>
							</c:when>
							<c:otherwise>
								<span class="score">${game.awayScore} - ${game.homeScore}</span>
							</c:otherwise>
						</c:choose>
						<span class="end-type">${endPeriodType}</span>
					</td>
					
					<td class="match-type">
						<span class="match-scope">
							<c:choose>
								<c:when test="${game.homeTeamAbr == homeAbr}">VS</c:when>
								<c:when test="${game.awayTeamAbr == homeAbr}">AT</c:when>
								<c:otherwise>Err</c:otherwise>
							</c:choose>
						</span>
						<span class="match-date">${game.gameDate}</span>
					</td>
					
					<td class="match-opponent">
						<c:choose>
							<c:when test="${game.homeTeamAbr == homeAbr}">
								<div class="opponent-img">
									<img src="/NHL/src/img/team-icons/${game.awayTeamAbr}.png" title="${game.awayTeamName}" alt="${game.awayTeamAbr}">
								</div>
							</c:when>
							<c:when test="${game.awayTeamAbr == homeAbr}">
								<span class="opponent-img">
									<img src="/NHL/src/img/team-icons/${game.homeTeamAbr}.png" title="${game.homeTeamAbr}" alt="${game.homeTeamAbr}">
								</span>
							</c:when>
							<c:otherwise>Err</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<table class="last-games-tbl">
			<c:forEach var="game" items="${lastGamesMap.get(awayAbr)}">
				<c:set var="gameResultClass"/>
				<c:set var="endPeriodType"/>
				<c:choose>
					<c:when test="${(game.awayTeamAbr == awayAbr && game.awayScore > game.homeScore) || (game.homeTeamAbr == awayAbr && game.homeScore > game.awayScore)}">
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="gameResultClass" value="match-win"/>
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="gameResultClass" value="match-otwin"/>
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="gameResultClass" value="match-sowin"/>
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:when>
					<c:when test="${(game.awayTeamAbr == awayAbr && game.awayScore < game.homeScore) || game.homeTeamAbr == awayAbr && game.homeScore < game.awayScore}">
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="gameResultClass" value="match-loss"/>
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="gameResultClass" value="match-otloss"/>
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="gameResultClass" value="match-soloss"/>
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="gameResultClass" value="match-tie"/>
						<c:choose>
							<c:when test="${game.endPeriodType == ''}">
								<c:set var="endPeriodType" value=""/>
							</c:when>
							<c:when test="${game.endPeriodType == 'OT'}">
								<c:set var="endPeriodType" value="OT"/>
							</c:when>
							<c:when test="${game.endPeriodType == 'SO'}">
								<c:set var="endPeriodType" value="SO"/>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				
				<tr id="${game.id}" class="${gameResultClass}">
					<td class="match-result">
						<c:choose>
							<c:when test="${game.homeTeamAbr == awayAbr}">
								<span class="score">${game.homeScore} - ${game.awayScore}</span>
							</c:when>
							<c:otherwise>
								<span class="score">${game.awayScore} - ${game.homeScore}</span>
							</c:otherwise>
						</c:choose>
						<span class="end-type">${endPeriodType}</span>
					</td>
					
					<td class="match-type">
						<span class="match-scope">
							<c:choose>
								<c:when test="${game.homeTeamAbr == awayAbr}">VS</c:when>
								<c:when test="${game.awayTeamAbr == awayAbr}">AT</c:when>
								<c:otherwise>Err</c:otherwise>
							</c:choose>
						</span>
						<span class="match-date">${game.gameDate}</span>
					</td>
					
					<td class="match-opponent">
						<c:choose>
							<c:when test="${game.homeTeamAbr == awayAbr}">
								<div class="opponent-img">
									<img src="/NHL/src/img/team-icons/${game.awayTeamAbr}.png" title="${game.awayTeamName}" alt="${game.awayTeamAbr}">
								</div>
							</c:when>
							<c:when test="${game.awayTeamAbr == awayAbr}">
								<span class="opponent-img">
									<img src="/NHL/src/img/team-icons/${game.homeTeamAbr}.png" title="${game.homeTeamAbr}" alt="${game.homeTeamAbr}">
								</span>
							</c:when>
							<c:otherwise>Err</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="last-games-nav">
		<input type="button" id="last-games-expand-btn" value="&#128899">
	</div>
</form:form>