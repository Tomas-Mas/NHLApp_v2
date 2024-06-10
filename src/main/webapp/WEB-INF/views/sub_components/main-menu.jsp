<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<div id='title'>
	<div id='logo'><img src="/NHL/src/img/nhl-logo.png"></div>
	<div id='headline'><p>NHL</p></div>
</div>
	
<div id='mainMenu' class='menu'>
	<button class='menulinks'>Home</button>
	<button class='menulinks'>Statistics</button>
	<button class='menulinks'>TODO</button>
</div>
	
<div class='season'>
	<select id='season' name='season'>
		<c:forEach items="${seasons}" var="s">
			<c:choose>
				<c:when test="${s == selectedSeason}"> <option selected> ${s} </option>	</c:when>
				<c:otherwise> <option> ${s} </option> </c:otherwise>
			</c:choose>
			
		</c:forEach>
	</select>
</div>