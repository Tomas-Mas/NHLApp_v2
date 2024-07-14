<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/sub_components/main-menu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/pages/game-page.css">
	
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-keyevents-td.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-h2h.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-stats.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-last-games.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-players-stats.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-simulation.css">
	
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/sub_components/game-submenu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/sub_components/game-header.css">
	
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/game-page.js"></script>
</head>

<body>

	<jsp:include page="/WEB-INF/views/sub_components/main-menu.jsp"></jsp:include>
	
	<main>
	
		<div id="main-section"></div>
		
		<div id="right-sidebar-section">
			<div id="head2head-section">
				<div id="h2h-last-games" class="h2h-section"></div>
			</div>
		</div>
		
	</main>
	
</body>

</html>