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
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/stats-page.css">
	<link rel="stylesheet" type="text/css" href="/NHL/src/css/playoff-spider.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
	<script src="/NHL/src/js/menu.js"></script>
	<script src="/NHL/src/js/stats-page.js"></script>
</head>

<body>

	<jsp:include page="/WEB-INF/views/main_menu/main-menu.jsp"></jsp:include>

	<div id="mainContent">
	
		<div id="statisticsContainer">
			<div>
				<jsp:include page="/WEB-INF/views/stats_page/stats-navigation.jsp"></jsp:include>
			</div>
			
		</div>
		
		
		
		<div id="playoffContainer">
			<jsp:include page="/WEB-INF/views/stats_page/playoff-spiders.jsp"></jsp:include>
		</div>
		
	</div>
</body>

</html>