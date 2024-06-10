<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--
	@modelObject - String pageNavigation - represents content of main container
	@modelObject - GameBasicDataDTO gameData - data of game based of id provided in url
	@modelObject - List<GamePeriodKeyEventsDTO> gameEvents - game events formated by each period
--%>

<jsp:include page="/WEB-INF/views/sub_components/game-submenu.jsp"></jsp:include>

<jsp:include page="/WEB-INF/views/sub_components/game-header.jsp"></jsp:include>

<div id="event-section">
	<table>
		<tr>
			<jsp:include page="/WEB-INF/views/components/game-keyevents-td.jsp"></jsp:include>
		</tr>
	</table>
</div>