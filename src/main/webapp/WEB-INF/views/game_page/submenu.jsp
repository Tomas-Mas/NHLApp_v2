<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<div id="submenu">
	<c:choose>
		<c:when test="${pageNavigation == 'Overview'}">
			<a class="selected" href="/NHL/game/${gameId}/overview">Overview</a></c:when>
		<c:otherwise>
			<a href="/NHL/game/${gameId}/overview">Overview</a>
		</c:otherwise>
	</c:choose>
	
	<a>Game Stats</a>
	<a>Player Stats</a>
</div>