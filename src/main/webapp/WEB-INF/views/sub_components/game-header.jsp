<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%--
	@modelObject - GameBasicDataDTO gameData - contains basic data of game
--%>

<div id="game-header">
	<div id="team-${gameData.homeTeamAbr}" class="home-team team-info">
		<div class="team-pic">
			<img src="/NHL/src/img/team-icons/${gameData.homeTeamAbr}.png" title="${gameData.homeTeamName}" alt="${gameData.homeTeamName}">
		</div>
		<div class="team-name">${gameData.homeTeamName}</div>
	</div>
	<div class="result">
		<div class="game-date">${gameData.gameDate}</div>
		<div class="score">${gameData.homeScore} - ${gameData.awayScore}</div>
		<div class="game-status">${gameData.gameStatus}</div>
	</div>
	<div id="team-${gameData.awayTeamAbr}" class="away-team team-info">
		<div class="team-pic">
			<img src="/NHL/src/img/team-icons/${gameData.awayTeamAbr}.png" title="${gameData.awayTeamName}" alt="${gameData.awayTeamName}">
		</div>
		<div class="team-name">${gameData.awayTeamName}</div>
	</div>
</div>