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
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-page.css">
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/main-page.js"></script>
</head>

<body>

	<jsp:include page="main-menu.jsp" />

	<div id="mainContent">
		<div id="regulationContainer">
			<table id="mainFinishedTableRegulation" class="mainFinishedTable">
				<c:forEach items="${games}" var="game">
					<tr id="headerRow${game.id}" class="clickableTr">
						<td>
							<table class="resultsHeader">
								<tr>
									<td rowspan="2" class="resultsHeaderDate"> ${game.formattedGameDate} </td>
									<td class="resultsPics">
										<div class="teamPic">
											<img src="/NHL/src/img/team-icons/${game.homeTeam.abbreviation}.png" title="${game.homeTeam.name}" alt="${game.homeTeam.name}">
										</div>
									</td>
									<td class="resultsTeamName"> ${game.homeTeam.name} </td>
									<td class="resultsScore"> ${game.homeScore} </td>
									<td rowspan="2" class="resultsDetail"> ${game.resultDetail} </td>
									<c:forEach items="${game.homePeriodScore}" var="score">
										<td class="resultsPeriodScore numeric"> ${score} </td>
									</c:forEach>
									<td rowspan="2" class="resultsButton">
										<button id="${game.id}" class="gamePageButton"> Game page </button>
									</td>
								</tr>
								<tr>
									<td class="resultsPics">
										<div class="teamPic">
											<img src="/NHL/src/img/team-icons/${game.awayTeam.abbreviation}.png" title="${game.awayTeam.name}" alt="${game.awayTeam.name}">
										</div>
									</td>
									<td class="resultsTeamName"> ${game.awayTeam.name} </td>
									<td class="resultsScore"> ${game.awayScore} </td>
									<c:forEach items="${game.awayPeriodScore}" var="score">
										<td class="resultsPeriodScore numeric"> ${score} </td>
									</c:forEach>
								</tr>
							</table>
						</td>
					</tr>
					
					<tr id="dataRow${game.id}" class="dataRow" style="display: none">
						<td class="matchData"> <!--colspan="3" -->
							<c:forEach items="${game.eventsPerPeriod}" var="periodEvents" varStatus="loop">
								<div class="periodHeader">
									<div> ${loop.index+1}.period</div>
									<div> ${game.homePeriodScore[loop.index]} - ${game.awayPeriodScore[loop.index]} </div>
								</div>
								<c:forEach items="${periodEvents}" var="event">
									<div class="eventLine${event.actedBy}">
										<div class="periodEventTime">${event.periodTime}</div>
										<c:choose>
											<c:when test="${event.event.name == 'Goal'}" >
												<div class="periodEventIcon"><img src="/NHL/src/img/game-detail-icons/goal-icon.png" title="${event.event.secondaryType}"></div>
												<div class="periodEventMainPlayer">
													<a id="${event.mainActor.id.roster.player.id}" class="gameDetailPlayerName">
														${event.mainActor.id.roster.player.firstName} ${event.mainActor.id.roster.player.lastName}
													</a>
												</div>
												<c:if test="${event.event.strength != 'Even'}">
													<div class="periodEventStrength"> (${event.event.strength}) </div>
												</c:if>
												<div class="periodEventAssist">
													<c:set value="true" var="isFirst"/>
													<c:forEach items="${event.players}" var="player">
														<c:if test="${player.role == 'Assist'}">
															<c:choose>
																<c:when test="${isFirst}"> 
																	${player.id.roster.player.firstName} ${player.id.roster.player.lastName} 
																	<c:set value="false" var="isFirst"/>
																</c:when>
																<c:otherwise>
																	+ ${player.id.roster.player.firstName} ${player.id.roster.player.lastName} 
																</c:otherwise>
															</c:choose>
														</c:if>
													</c:forEach>
												</div>
												
											</c:when>
											<c:otherwise>
												<div class="periodEventIcon">
													<img src="/NHL/src/img/game-detail-icons/penalty${event.event.penaltyMinutes}.png" 
															title="${event.event.penaltyMinutes} minutes ${event.event.penaltySeverity} - ${event.event.secondaryType}">
												</div>
												<div class="periodEventMainPlayer">
													<a id="${event.mainActor.id.roster.player.id}" class="gameDetailPlayerName">
														${event.mainActor.id.roster.player.firstName} ${event.mainActor.id.roster.player.lastName}
													</a>
												</div>
												<div class="periodEventPenaltyType"> (${event.event.secondaryType})</div>
											</c:otherwise>
										</c:choose>
									</div>
								</c:forEach>
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</table>
		
		</div>
	</div>

</body>
</html>