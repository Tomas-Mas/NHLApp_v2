<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<%--
	@modelObject - String pageNavigation - string representation of game page subnavigation
--%>

<div id="submenu">
	<c:choose>
		<c:when test="${pageNavigation == 'Overview'}">
			<span class="selected">Overview</span></c:when>
		<c:otherwise>
			<span>Overview</span>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${pageNavigation == 'GameStats'}">
			<span class="selected">Game Stats</span></c:when>
		<c:otherwise>
			<span>Game Stats</span>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${pageNavigation == 'PlayersStats'}">
			<span class="selected">Players Stats</span></c:when>
		<c:otherwise>
			<span>Players Stats</span>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${pageNavigation == 'Simulation'}">
			<span class="selected">Simulation</span></c:when>
		<c:otherwise>
			<span>Simulation</span>
		</c:otherwise>
	</c:choose>
</div>