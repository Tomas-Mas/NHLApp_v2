<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form:form modelAttribute="statsNavigation" method="GET">
	<div id="statsNav">
		<div class="navScopeContainer">
			<div class="navUnit2Items">
				<form:radiobutton id="teams" path="dataScope" value="Teams"/>
				<label class="el2Label" for="teams">Teams</label>
			</div>
			<div class="navUnit2Items">
				<form:radiobutton id="players" path="dataScope" value="Players"/>
				<label class="el2Label" for="players">Players</label>
			</div>
		</div>
		
		<div class="navScopeContainer">
			<c:choose>
				<c:when test="${statsNavigation.dataScope=='Players'}">
					<div class="navUnit2Items">
						<form:radiobutton id="regulation" path="seasonScope" value="Regulation"/>
						<label class="el2Label" for="regulation">Regulation</label>
					</div>
					<div class="navUnit2Items">
						<form:radiobutton id="playoff" path="seasonScope" value="Playoff"/>
						<label class="el2Label" for="playoff">Play-Off</label>
					</div>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${statsNavigation.dataScope=='Teams'}">
					<div class="navUnit2Items">
						<form:radiobutton id="conference" path="standingsScope" value="Conference"/>
						<label class="el2Label" for="conference">Conferences</label>
					</div>
					<div class="navUnit2Items">
						<form:radiobutton id="division" path="standingsScope" value="Division"/>
						<label class="el2Label" for="division">Divisions</label>
					</div>
				</c:when>
			</c:choose>
		</div>
	
		<div class="navScopeContainer">
			<div class="navUnit3Items">
				<form:radiobutton id="overall" path="regulationScope" value="Overall"/>
				<label class="el3Label" for="overall">Overall</label>
			</div>
			<div class="navUnit3Items">
				<form:radiobutton id="home" path="regulationScope" value="Home"/>
				<label class="el3Label" for="home">Home</label>
			</div>
			<div class="navUnit3Items">
				<form:radiobutton id="away" path="regulationScope" value="Away"/>
				<label class="el3Label" for="away">Away</label>
			</div>
		</div>
	</div>

	<c:choose>
		<c:when
			test="${statsNavigation.dataScope == 'Teams' && statsNavigation.standingsScope == 'Conference'}">
			<jsp:include page="/WEB-INF/views/stats_page/conference-stats.jsp"></jsp:include>
		</c:when>
		<c:when
			test="${statsNavigation.dataScope == 'Teams' && statsNavigation.standingsScope == 'Division'}">
			<jsp:include page="/WEB-INF/views/stats_page/division-stats.jsp"></jsp:include>
		</c:when>
		<c:when test="${statsNavigation.dataScope == 'Players'}">
			<div id="tblPopupNav">
				<input type="button" class="tblPopupResize" title="Maximize/Minimize Table" value="+">
			</div>
			<jsp:include page="/WEB-INF/views/stats_page/skater-stats.jsp"></jsp:include>

			<div id="tblNavigation">
				<div class="tblArrow">
					<input type="button" class="tableNavLeft" value="<">
				</div>
				<div class="pageFormItem">${statsNavigation.resCount} records returned</div>
				<div class="pageFormItem">Page
					<form:input id="pageNum" path="selectedPageNumber"/>of ${statsNavigation.maxPage}
				</div>
				<div class="pageFormItem">
					<form:select path="rowsPerPage">
						<c:forEach var="pageNum" items="${statsNavigation.getRowsPerPageOptions()}">
							<form:option value="${pageNum}" label="${pageNum} rows"/>
						</c:forEach>
					</form:select>
				</div>
				<div class="tblArrow">
					<input type="button" class="tableNavRight" value=">">
				</div>
			</div>

		</c:when>
	</c:choose>



</form:form>