<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<%--
	@modelObject - List<GameBasicDataDTO games - list with basic game data
--%>

<table id="games-table">	<!-- id=mainFinishedTableRegulation class=mainFinishedTable -->
	<c:forEach items="${games}" var="game">
		<tr id="headerRow${game.id}" class="clickableTr">
			<td>
				<table class="resultsHeader">
					<tr>
						<td rowspan="2" class="resultsHeaderDate">${game.gameDate}</td>
						<td class="resultsPics">
							<div class="teamPic">
								<img src="/NHL/src/img/team-icons/${game.homeTeamAbr}.png"
									title="${game.homeTeamName}" alt="${game.homeTeamName}">
							</div>
						</td>
						<td class="resultsTeamName">${game.homeTeamName}</td>
						<td class="resultsScore">${game.homeScore}</td>
						<td rowspan="2" class="resultsDetail">${game.endPeriodType}</td>
						<c:forEach items="${game.homePeriodsScore}" var="score">
							<td class="resultsPeriodScore numeric">${score}</td>
						</c:forEach>
						<td rowspan="2" class="resultsButton">
							<button id="${game.id}" class="gamePageButton">Game page
							</button>
						</td>
					</tr>
					<tr>
						<td class="resultsPics">
							<div class="teamPic">
								<img src="/NHL/src/img/team-icons/${game.awayTeamAbr}.png"
									title="${game.awayTeamName}" alt="${game.awayTeamName}">
							</div>
						</td>
						<td class="resultsTeamName">${game.awayTeamName}</td>
						<td class="resultsScore">${game.awayScore}</td>
						<c:forEach items="${game.awayPeriodsScore}" var="score">
							<td class="resultsPeriodScore numeric">${score}</td>
						</c:forEach>
					</tr>
				</table>
			</td>
		</tr>

		<tr id="dataRow${game.id}" class="dataRow" style="display: none">

		</tr>
	</c:forEach>
</table>