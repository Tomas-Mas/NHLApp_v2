<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>

<div id="game-header">
	<div class="home-team team-info">
		<div class="team-pic">
			<a href="/NHL/team/${gameData.homeTeamId}"> 
				<img src="/NHL/src/img/team-icons/${gameData.homeTeamAbr}.png" title="${gameData.homeTeamName}" alt="${gameData.homeTeamName}">
			</a>
		</div>
		<div class="team-name">
			<a href="/NHL/team/${gameData.homeTeamId}">${gameData.homeTeamName}</a>
		</div>
	</div>
	<div class="result">
		<div class="game-date">${gameData.gameDate}</div>
		<div class="score">${gameData.homeScore} - ${gameData.awayScore}</div>
		<div class="game-status">${gameData.gameStatus}</div>
	</div>
	<div class="away-team team-info">
		<div class="team-pic">
			<a href="/NHL/team/${gameData.awayTeamId}"> 
				<img src="/NHL/src/img/team-icons/${gameData.awayTeamAbr}.png" title="${gameData.awayTeamName}" alt="${gameData.awayTeamName}">
			</a>
		</div>
		<div class="team-name">
			<a href="/NHL/team/${gameData.awayTeamId}">${gameData.awayTeamName}</a>
		</div>
	</div>
</div>