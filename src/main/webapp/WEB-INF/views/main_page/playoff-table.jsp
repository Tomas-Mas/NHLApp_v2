<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div id="seasonScope">
	<c:choose>
		<c:when test="${seasonScope.type=='Regulation'}">
			<input type="radio" id="regulation" class="seasonScope"
				name="seasonScope" checked disabled />
		</c:when>
		<c:otherwise>
			<input type="radio" id="regulation" class="seasonScope"
				name="seasonScope" />
		</c:otherwise>
	</c:choose>
	<label for="regulation">Regulation</label>

	<c:choose>
		<c:when test="${seasonScope.type=='Playoff'}">
			<input type="radio" id="playoff" class="seasonScope"
				name="seasonScope" checked disabled />
		</c:when>
		<c:otherwise>
			<input type="radio" id="playoff" class="seasonScope"
				name="seasonScope" />
		</c:otherwise>
	</c:choose>
	<label for="playoff">Playoff</label>
</div>

<p>
	TODO nothing to report here momentarily :(
</p>

<style>
	p {
		font-color: white;
	}
</style>