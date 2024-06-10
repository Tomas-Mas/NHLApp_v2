<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--
sidebar-stats-subnav:
	@modelObject - SeasonScope seasonScope - season scope enum
	
	@modelObject - PlayoffSpiderDTO playoff - all data for playoff (seeding, bracket, matches)
--%>

<jsp:include page="/WEB-INF/views/sub_components/sidebar-stats-subnav.jsp"></jsp:include>

<div id="spider-container"> <!-- playoffContainer -->
	<c:forEach items="${playoff.brackets}" varStatus="mainLoop" var="bracket">
		<div class="conference-bracket-container">
		
			<c:forEach items="${bracket.firstRound}" var="match">
				<div class="first-round playoff-bracket">
					
					<div class="match-header">
						<table class="match-table">
							<tr class="team${match.higherSeedTeam.teamId} highlightable">
								<td class="team-pic">
									<div class="teamPic">
										<img src="/NHL/src/img/team-icons/${match.higherSeedTeam.teamAbbreviation}.png"
											title="${match.higherSeedTeam.teamName}" alt="${match.higherSeedTeam.teamName}">
									</div>
								</td>
								<td class="team-seeding">(${match.higherSeedSeeding})</td>
								<c:choose>
									<c:when test="${match.higherSeedTeam == match.winner}">
										<td class="team-name bracket-winner">${match.higherSeedTeam.teamAbbreviation}</td>
									</c:when>
									<c:otherwise>
										<td class="team-name">${match.higherSeedTeam.teamAbbreviation}</td>
									</c:otherwise>
								</c:choose>
								<td class="team-points">${match.higherSeedScore}</td>
							</tr>
							
							<tr class="team${match.lowerSeedTeam.teamId} highlightable">
								<td class="team-pic">
									<div class="teamPic">
										<img src="/NHL/src/img/team-icons/${match.lowerSeedTeam.teamAbbreviation}.png"
											title="${match.lowerSeedTeam.teamName}" alt="${match.lowerSeedTeam.teamName}">
									</div>
								</td>
								<td class="team-seeding">(${match.lowerSeedSeeding})</td>
								<c:choose>
									<c:when test="${match.lowerSeedTeam == match.winner}">
										<td class="team-name bracket-winner">${match.lowerSeedTeam.teamAbbreviation}</td>
									</c:when>
									<c:otherwise>
										<td class="team-name">${match.lowerSeedTeam.teamAbbreviation}</td>
									</c:otherwise>
								</c:choose>
								<td class="team-points">${match.lowerSeedScore}</td>
							</tr>
						</table>
						
					</div>
					
					<div class="match-results" style="display:none">
						<table class="results-table">
							<c:forEach items="${match.games}" var="game">
								<tr id="${game.id}">
									<td class="res-date">${fn:substring(game.gameDate, 0, 6)}</td>
									<td class="res-teams">${game.homeTeamAbr} - ${game.awayTeamAbr}</td>
									<td class="res-score">${game.homeScore}:${game.awayScore}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:forEach>
		
		
			<c:forEach items="${bracket.secondThirdRound}" varStatus="loop" var="match"> 
				<c:choose>
					<c:when test="${loop.index == 0}"><c:set var="className" value="div-finals-top playoff-bracket"/></c:when>
					<c:when test="${loop.index == 1}"><c:set var="className" value="div-finals-bot playoff-bracket"/></c:when>
					<c:otherwise><c:set var="className" value="conf-finals playoff-bracket"/></c:otherwise>
				</c:choose>
				<div class="${className}">
					<div class="match-header">
						<table class="match-table">
						
							<c:choose>
								<c:when test="${match.higherSeedTeam != null}">
									<tr class="team${match.higherSeedTeam.teamId} highlightable">
										<td class="team-pic">
											<div class="teamPic">
												<img src="/NHL/src/img/team-icons/${match.higherSeedTeam.teamAbbreviation}.png"
													title="${match.higherSeedTeam.teamName}" alt="${match.higherSeedTeam.teamName}">
											</div>
										</td>
										<td class="team-seeding">(${match.higherSeedSeeding})</td>
										<c:choose>
											<c:when test="${match.higherSeedTeam == match.winner}">
												<td class="team-name bracket-winner">${match.higherSeedTeam.teamAbbreviation}</td>
											</c:when>
											<c:otherwise>
												<td class="team-name">${match.higherSeedTeam.teamAbbreviation}</td>
											</c:otherwise>
										</c:choose>
										<td class="team-points">${match.higherSeedScore}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${match.higherSeedMatch != null && (match.higherSeedMatch.higherSeedTeam != null && match.higherSeedMatch.lowerSeedTeam != null)}">
											<tr>
												<td class="team-pic-undecided">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${match.higherSeedMatch.higherSeedTeam.teamAbbreviation}.png"
															title="${match.higherSeedMatch.higherSeedTeam.teamName}" alt="${match.higherSeedMatch.higherSeedTeam.teamName}">
													</div>
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${match.higherSeedMatch.lowerSeedTeam.teamAbbreviation}.png"
															title="${match.higherSeedMatch.lowerSeedTeam.teamName}" alt="${match.higherSeedMatch.lowerSeedTeam.teamName}">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points">-</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="team-pic">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/tbd.png"
															title="tbd" alt="tbd">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points"></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<c:when test="${match.lowerSeedTeam != null}">
									<tr class="team${match.lowerSeedTeam.teamId} highlightable">
										<td class="team-pic">
											<div class="teamPic">
												<img src="/NHL/src/img/team-icons/${match.lowerSeedTeam.teamAbbreviation}.png"
													title="${match.lowerSeedTeam.teamName}" alt="${match.lowerSeedTeam.teamName}">
											</div>
										</td>
										<td class="team-seeding">(${match.lowerSeedSeeding})</td>
										<c:choose>
											<c:when test="${match.lowerSeedTeam == match.winner}">
												<td class="team-name bracket-winner">${match.lowerSeedTeam.teamAbbreviation}</td>
											</c:when>
											<c:otherwise>
												<td class="team-name">${match.lowerSeedTeam.teamAbbreviation}</td>
											</c:otherwise>
										</c:choose>
										<td class="team-points">${match.lowerSeedScore}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${match.lowerSeedMatch != null && (match.lowerSeedMatch.higherSeedTeam != null && match.lowerSeedMatch.lowerSeedTeam != null)}">
											<tr>
												<td class="team-pic-undecided">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${match.lowerSeedMatch.higherSeedTeam.teamAbbreviation}.png"
															title="${match.lowerSeedMatch.higherSeedTeam.teamName}" alt="${match.lowerSeedMatch.higherSeedTeam.teamName}">
													</div>
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${match.lowerSeedMatch.lowerSeedTeam.teamAbbreviation}.png"
															title="${match.lowerSeedMatch.lowerSeedTeam.teamName}" alt="${match.lowerSeedMatch.lowerSeedTeam.teamName}">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points">-</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="team-pic">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/tbd.png"
															title="tbd" alt="tbd">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points"></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							
						</table>
					</div>
					<div class="match-results" style="display:none">
						<table class="results-table">
							<c:forEach items="${match.games}" var="game">
								<tr id="${game.id}">
									<td class="res-date">${fn:substring(game.gameDate, 0, 6)}</td>
									<td class="res-teams">${game.homeTeamAbr} - ${game.awayTeamAbr}</td>
									<td class="res-score">${game.homeScore}:${game.awayScore}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:forEach>
		</div>
		
		<c:choose>
			<c:when test="${mainLoop.index == 0}">
				<div class="playoff-finals-container">
					<div class="stanley-cup-logo"><img src="/NHL/src/img/stanley_cup_logo.png"></div>
					<div class="stanley-cup-match playoff-bracket">
						<div class="match-header">
							<table class="match-table">
								<c:choose>
									<c:when test="${playoff.finals.higherSeedTeam != null}">
										<tr class="team${playoff.finals.higherSeedTeam.teamId} highlightable">
											<td class="team-pic">
												<div class="teamPic">
													<img src="/NHL/src/img/team-icons/${playoff.finals.higherSeedTeam.teamAbbreviation}.png"
														title="${playoff.finals.higherSeedTeam.teamName}" alt="${playoff.finals.higherSeedTeam.teamName}">
												</div>
											</td>
											<td class="team-seeding">(${playoff.finals.higherSeedSeeding})</td>
											<c:choose>
												<c:when test="${playoff.finals.higherSeedTeam == playoff.finals.winner}">
													<td class="team-name bracket-winner">${playoff.finals.higherSeedTeam.teamAbbreviation}</td>
												</c:when>
												<c:otherwise>
													<td class="team-name">${playoff.finals.higherSeedTeam.teamAbbreviation}</td>
												</c:otherwise>
											</c:choose>
											<td class="team-points">${playoff.finals.higherSeedScore}</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${playoff.finals.higherSeedMatch != null && (maplayoff.finalstch.higherSeedMatch.higherSeedTeam != null && playoff.finals.higherSeedMatch.lowerSeedTeam != null)}">
												<tr>
													<td class="team-pic-undecided">
														<div class="teamPic">
															<img src="/NHL/src/img/team-icons/${playoff.finals.higherSeedMatch.higherSeedTeam.teamAbbreviation}.png"
																title="${playoff.finals.higherSeedMatch.higherSeedTeam.teamName}" alt="${playoff.finals.higherSeedMatch.higherSeedTeam.teamName}">
														</div>
														<div class="teamPic">
															<img src="/NHL/src/img/team-icons/${playoff.finals.higherSeedMatch.lowerSeedTeam.teamAbbreviation}.png"
																title="${playoff.finals.higherSeedMatch.lowerSeedTeam.teamName}" alt="${playoff.finals.higherSeedMatch.lowerSeedTeam.teamName}">
														</div>
													</td>
													<td class="team-seeding"></td>
													<td class="team-name">TBD</td>
													<td class="team-points">-</td>
												</tr>
											</c:when>
											<c:otherwise>
												<tr>
													<td class="team-pic">
														<div class="teamPic">
															<img src="/NHL/src/img/team-icons/tbd.png"
																title="tbd" alt="tbd">
														</div>
													</td>
													<td class="team-seeding"></td>
													<td class="team-name">TBD</td>
													<td class="team-points"></td>
												</tr>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
								
								<c:choose>
								<c:when test="${playoff.finals.lowerSeedTeam != null}">
									<tr class="team${playoff.finals.lowerSeedTeam.teamId} highlightable">
										<td class="team-pic">
											<div class="teamPic">
												<img src="/NHL/src/img/team-icons/${playoff.finals.lowerSeedTeam.teamAbbreviation}.png"
													title="${playoff.finals.lowerSeedTeam.teamName}" alt="${playoff.finals.lowerSeedTeam.teamName}">
											</div>
										</td>
										<td class="team-seeding">(${playoff.finals.lowerSeedSeeding})</td>
										<c:choose>
											<c:when test="${playoff.finals.lowerSeedTeam == playoff.finals.winner}">
												<td class="team-name bracket-winner">${playoff.finals.lowerSeedTeam.teamAbbreviation}</td>
											</c:when>
											<c:otherwise>
												<td class="team-name">${playoff.finals.lowerSeedTeam.teamAbbreviation}</td>
											</c:otherwise>
										</c:choose>
										<td class="team-points">${playoff.finals.lowerSeedScore}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${playoff.finals.lowerSeedMatch != null && (playoff.finals.lowerSeedMatch.higherSeedTeam != null && playoff.finals.lowerSeedMatch.lowerSeedTeam != null)}">
											<tr>
												<td class="team-pic-undecided">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${playoff.finals.lowerSeedMatch.higherSeedTeam.teamAbbreviation}.png"
															title="${playoff.finals.lowerSeedMatch.higherSeedTeam.teamName}" alt="${playoff.finals.lowerSeedMatch.higherSeedTeam.teamName}">
													</div>
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/${playoff.finals.lowerSeedMatch.lowerSeedTeam.teamAbbreviation}.png"
															title="${playoff.finals.lowerSeedMatch.lowerSeedTeam.teamName}" alt="${playoff.finals.lowerSeedMatch.lowerSeedTeam.teamName}">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points">-</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="team-pic">
													<div class="teamPic">
														<img src="/NHL/src/img/team-icons/tbd.png"
															title="tbd" alt="tbd">
													</div>
												</td>
												<td class="team-seeding"></td>
												<td class="team-name">TBD</td>
												<td class="team-points"></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							</table>
						</div>
						<div class="match-results" style="display:none">
						<table class="results-table">
							<c:forEach items="${playoff.finals.games}" var="game">
								<tr id="${game.id}">
									<td class="res-date">${fn:substring(game.gameDate, 0, 6)}</td>
									<td class="res-teams">${game.homeTeamAbr} - ${game.awayTeamAbr}</td>
									<td class="res-score">${game.homeScore}:${game.awayScore}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
					</div>
				</div>
			</c:when>
		</c:choose>
		
	</c:forEach>
</div>