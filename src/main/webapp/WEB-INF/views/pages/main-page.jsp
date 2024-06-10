<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/root.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/pages/main-page.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/sub_components/main-menu.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/main-page-games-tbl.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/sub_components/sidebar-stats-subnav.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/sidebar-stats-standings.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/sidebar-playoff-spider.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/components/game-keyevents-td.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/main-page.js"></script>
</head>

<body>

	<jsp:include page="/WEB-INF/views/sub_components/main-menu.jsp"></jsp:include>

	<main>
		<div id="game-list-section"></div>
		<div id="stats-section"></div>
	</main>

</body>
</html>