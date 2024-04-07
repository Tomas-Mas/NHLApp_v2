<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/title.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/main-menu.css">
	
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/game-overview.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/submenu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/head-to-head.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/game_page/game-header.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/game_page/overview.js"></script>
</head>

<body>

	<jsp:include page="/WEB-INF/views/main_menu/main-menu.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/views/game_page/submenu.jsp"></jsp:include>
	
	<main>
		<div id="main-section">
		
			<jsp:include page="/WEB-INF/views/game_page/game-header.jsp"></jsp:include>
			
			<div id="event-section">
				<table>
					<tr>
						<jsp:include page="/WEB-INF/views/main_page/game-detail.jsp"></jsp:include>
					</tr>
				</table>
			</div>
		</div>
		
		<div id="stats-section">
			
			<jsp:include page="/WEB-INF/views/game_page/head-to-head.jsp"></jsp:include>
			
		</div>
		
	</main>

</body>

</html>