const sidebarStatsUrl = '/NHL/c/stats/sidebarBySeasonScope/';
const sidebarRegulationUrl = '/NHL/c/stats/regulation/';
const gameListUrl = '/NHL/c/game/list/';
const gameDetailUrl = '/NHL/c/game/keyEvents/';

var expandedElement = null;

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		fetchComponents();
		setClickableEvents();
	}
}

function fetchComponents() {
	fetch(gameListUrl + getActiveSeason())
		.then(response => response.text())
		.then(data => {
			document.getElementById('game-list-section').innerHTML = data;
		})
		.catch(err => console.log(err));
		
	fetch(sidebarStatsUrl + getActiveSeason())
		.then(response => response.text())
		.then(data => {
			document.getElementById('stats-section').innerHTML = data;
		})
		.catch(err => console.log(err));
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	document.querySelector('select#season').addEventListener('change', function() {
		window.location.href = mainPageUrl + getActiveSeason();
	})
	
	document.getElementById('game-list-section').onclick = function(e) { //'games-table'
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
	
	document.getElementById('stats-section').onclick = function(e) {
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
	window.location.href = gamePageUrl + gameId;
}

function mainTableHeaderClicked(headerRow) {
	var rowId = getIdFromElement(headerRow, true, 'headerRow');
	var dataRow = document.getElementById('dataRow' + rowId);
	
	if(dataRow.getElementsByTagName('td').length == 0) {
		fetch(gameDetailUrl + rowId)
			.then(result => result.text())
			.then(data => dataRow.innerHTML = data)
			.catch(err => console.log(err));
	}
	
	if(dataRow.style.display === 'none') {
		dataRow.style.display = 'block';
	} else {
		dataRow.style.display = 'none';
	}
}

function seasonScopeBtnClicked(btn) {
	fetch(sidebarStatsUrl + getActiveSeason() + '/' + btn.id)
		.then(response => response.text())
		.then(data => document.getElementById('stats-section').innerHTML = data)
		.catch(err => console.log(err));
}

function regulationScopeBtnClicked(btn) {
	fetch(sidebarRegulationUrl + getActiveSeason() + '/' + btn.id)
		.then(response => response.text())
		.then(data => document.getElementById('stats-section').innerHTML = data)
		.catch(err => console.log(err));
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