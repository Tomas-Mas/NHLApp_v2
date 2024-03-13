var expandedElement = null;

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
	}
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	document.querySelector('select#season').addEventListener('change', function() {
		window.location.replace('/NHL/mainpage/' + getActiveSeason());
	})
	
	document.getElementById('mainFinishedTableRegulation').onclick = function(e) {
		if(e.target.className === 'gameDetailPlayerName') {
			playerClicked(e.target);
		} else if (e.target.className === 'gamePageButton') {
			gamePageButtonClicked(e.target);
		} else if (e.target.closest('tr') !== null && e.target.closest('tr').className === 'clickableTr') {
			mainTableHeaderClicked(e.target.closest('tr'));
		} else if (e.target.closest('table') !== null && e.target.closest('table').className === 'resultsHeader') {
			mainTableHeaderClicked(e.target.closest('table').closest('tr'));
		}
	}
	
	document.getElementById('regulationStats').onclick = function(e) {
		if(e.target.className === 'teamName') {
			teamClicked(e.target);
		} else if(e.target.className === 'seasonScope') {
			seasonScopeBtnClicked(e.target);
		} else if(e.target.className === 'regulationScope') {
			regulationScopeBtnClicked(e.target);
		} else if(e.target.closest('div').className === 'match-header') {
			playoffClicked(e);
		}
	}
	
	document.getElementById('season').removeAttribute('disabled');
}

function playerClicked(target) {
	var playerId = target.id;
	//window.location.href = 'player.jsp?id=' + playerId;
}

function teamClicked(target) {
	var teamId = target.id;
	//alert(teamId);
	//window.location.href = 'team.jsp?id=' + teamId;
}

function gamePageButtonClicked(target) {
	var gameId = target.id;
	//window.location.href = 'game.jsp?id=' + gameId;
}

function mainTableHeaderClicked(headerRow) {
	var rowId = getIdFromElement(headerRow, true, 'headerRow');
	var dataRow = document.getElementById('dataRow' + rowId);
	
	if(dataRow.getElementsByTagName('td').length == 0) {
		$.get("/NHL/mainpage/showGameDetail/" + rowId, (data) => {
			dataRow.innerHTML = data;
		})
	}
	
	if(dataRow.style.display === 'none') {
		dataRow.style.display = 'block';
	} else {
		dataRow.style.display = 'none';
	}
}

function seasonScopeBtnClicked(btn) {
	$.get("/NHL/mainpage/changeSeasonScope/" + getActiveSeason() + "/" + btn.id, (data) => {
		document.getElementById('regulationStats').innerHTML = data;
	});
}

function regulationScopeBtnClicked(btn) {
	$.get("/NHL/mainpage/changeRegulationScope/" + getActiveSeason() + "/" + btn.id, (data) => {
		document.getElementById('regulationStats').innerHTML = data;
	});
}

function playoffClicked(event) {
	var clickedDiv = event.target.closest('div');
	if(clickedDiv.className === 'match-header') {
		var results = clickedDiv.nextElementSibling;
		if(results.style.display === 'none') {
			unexpandBrackets();
			results.style.display = 'block';
			clickedDiv.parentElement.classList.add('expanded');
			expandedElement = clickedDiv;
		} else {
			results.style.display = 'none';
			clickedDiv.parentElement.classList.remove('expanded');
		}
	} else if(clickedDiv.className === 'match-results') {
		alert(event.target.closest('tr').id);
		//todo go to match page
	} else {
		unexpandBrackets();
	}
}

function unexpandBrackets() {
	if(expandedElement != null) {
		expandedElement.parentElement.classList.remove('expanded');
		expandedElement.nextElementSibling.style.display = 'none';
		expandedElement = null;
	}
}

function getIdFromElement(element, prefix, idPrefix) {
	if(prefix) 
		return element.id.replace(idPrefix, '');
	else
		return element.id;
}