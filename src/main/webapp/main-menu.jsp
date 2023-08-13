<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<div id='title'>
	<p class='headline'>Welcome to the jungle</p>
</div>
	
<div id='mainMenu' class='menu'>
	<button class='menulinks'>Home</button>
	<button class='menulinks'>Statistics</button>
	<button class='menulinks'>TODO</button>
</div>
	
<div class='season'>
	<select id='season' name='season'>
		<c:forEach items="${seasons}" var="s">
			<!-- <option> ${s} </option>  -->
			
			<c:choose>
				<c:when test="${s == season}"> <option selected> ${s} </option>	</c:when>
				<c:otherwise> <option> ${s} </option> </c:otherwise>
			</c:choose>
			
		</c:forEach>
	</select>
</div>
