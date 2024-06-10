<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--
sidebar-stats-subnav:
	@modelObject - SeasonScope seasonScope - season scope enum
	
	@modelObject - RegulationScope regulationScope - regulation scope enum
	@modelObject - TeamStandingsDTO standings - team standings data for both conferences and divisions
--%>

<jsp:include page="/WEB-INF/views/sub_components/sidebar-stats-subnav.jsp"></jsp:include>

<div id="team-standings"> <!-- regulationStatsTable -->
	<div id="regulationScope">
		<c:choose>
			<c:when test="${regulationScope.type=='Overall'}">
				<input type="radio" id="overall" class="regulationScope" name="regulationScope" checked />
			</c:when>
			<c:otherwise>
				<input type="radio" id="overall" class="regulationScope" name="regulationScope" />
			</c:otherwise>
		</c:choose>
		<label for="overall">Overall</label>
		<c:choose>
			<c:when test="${regulationScope.type=='Home'}">
				<input type="radio" id="home" class="regulationScope" name="regulationScope" checked />
			</c:when>
			<c:otherwise>
				<input type="radio" id="home" class="regulationScope" name="regulationScope" />
			</c:otherwise>
		</c:choose>
		<label for="home">Home</label>
		<c:choose>
			<c:when test="${regulationScope.type=='Away'}">
				<input type="radio" id="away" class="regulationScope" name="regulationScope" checked />
			</c:when>
			<c:otherwise>
				<input type="radio" id="away" class="regulationScope" name="regulationScope" />
			</c:otherwise>
		</c:choose>
		<label for="away">Away</label>
	</div>

	<table>
		<c:forEach items="${standings.conferenceTeamMap}" var="conference">
			<tr class="header">
				<th class="conferenceName" colspan="3">${conference.key}</th>
				<th title="Games Played">GP</th>
				<th title="Regulation wins">W</th>
				<th title="Overtime/Shootout wins">OW</th>
				<th title="Overtime/Shootout loses">OL</th>
				<th title="Regulation loses">L</th>
				<th title="Points">P</th>
			</tr>
			<c:forEach items="${conference.value}" var="team" varStatus="loop">
				<tr>
					<td class="numeric">${loop.index + 1}</td>
					<td>
						<div class="teamPic">
							<img src="/NHL/src/img/team-icons/${team.teamAbbreviation}.png"
								title="${team.teamName}" alt="${team.teamName}">
						</div>
					</td>
					<td id="${team.teamId}" class="teamName">${team.teamName}</td>
					<td class="numeric">${team.games}</td>
					<td class="numeric">${team.regWins}</td>
					<td class="numeric">${team.otWins}</td>
					<td class="numeric">${team.otLoses}</td>
					<td class="numeric">${team.regLoses}</td>
					<td class="numeric">${team.points}</td>
				</tr>
			</c:forEach>
		</c:forEach>
	</table>
</div>