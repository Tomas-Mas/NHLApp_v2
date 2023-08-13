<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/title.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-menu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-page.css">
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
									<td rowspan="2" class="resultsHeaderDate"> ${game.gameDate} </td>
									<td class="resultsPics">
										<div class="teamPic">
											<img src="/NHL/src/img/team-icons/${game.homeTeam.abbreviation}.png" title="${game.homeTeam.name}" alt="${game.homeTeam.name}">
										</div>
									</td>
									<td class="resultsTeamName"> ${game.homeTeam.name} </td>
									<td class="resultsScore"> ${game.homeScore} </td>
									<td rowspan="2" class="resultsDetail"> TD </td>
								</tr>
								<tr>
									<td class="resultsPics">
										<div class="teamPic">
											<img src="/NHL/src/img/team-icons/${game.awayTeam.abbreviation}.png" title="${game.awayTeam.name}" alt="${game.awayTeam.name}">
										</div>
									</td>
									<td class="resultsTeamName"> ${game.awayTeam.name} </td>
									<td class="resultsScore"> ${game.awayScore} </td>
								</tr>
							</table>
						</td>
					</tr>
					
					<tr id="dataRow${game.id}" class="dataRow">
					</tr>
				</c:forEach>
			</table>
		
		</div>
	</div>

</body>
</html>