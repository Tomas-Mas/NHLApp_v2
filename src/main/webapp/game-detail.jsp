<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<td class="matchData">
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