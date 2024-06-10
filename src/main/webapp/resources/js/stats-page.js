const sidebarPlayoffUrl = '/NHL/c/stats/playoff/';
const statisticsUrl = '/NHL/c/stats/fullStats/';

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		fetchComponents();
		fetchStats();
		
		document.getElementById('mainMenu').onclick = menuClicked;
		document.querySelector('select#season').addEventListener('change', function() {
			window.location.href = statsPageUrl + getActiveSeason();
		});
		//setMouseHoverEvents();
	}
}

var expandedElement = null;
var sortedColumnIndex = 0;
var descSortDirection = true;
var markedHeaders = [];

function fetchComponents() {
	fetch(sidebarPlayoffUrl + getActiveSeason())
		.then(response => response.text())
		.then((data) => {
			document.getElementById('playoff-section').innerHTML = data;
		})
		.then(() => {
			document.getElementById('spider-container').onclick = playoffClicked;
			document.getElementById('seasonScope').remove();
		})
		.catch(err => console.log(err));
}

function fetchStats() {
	var url = statisticsUrl + getActiveSeason();
	var formData = document.getElementById('statsNavigation');
	
	if(formData != null) {
		var params = new URLSearchParams();
		new FormData(formData).forEach((value, key) => {
			params.append(key, value);
		});
		url = statisticsUrl + getActiveSeason() + '?' + params;
	}
	console.log('url: ' + url);
	
	fetch(url)
		.then(response => response.text())
		.then(data => document.getElementById('stats-section').innerHTML = data)
		.then(setStatsClickableEvents)
		.catch(err => console.log(err));
	
}

function setStatsClickableEvents() {
	document.getElementById('statsNavigation').addEventListener('submit', function(e) {
		e.preventDefault();
	});
	
	tblHeaders = document.getElementsByClassName('sortableHeader');
	for(let i = 0; i < tblHeaders.length; i++) {
		tblHeaders[i].onclick = sortTable;
	}
	
	var radios = document.getElementById('statsNav').getElementsByTagName('input');
	for(let i = 0; i < radios.length; i++) {
		radios[i].addEventListener('change', function() {
			fetchStats();
		});
	}
	
	var playerTbls = document.getElementsByClassName('playerStatsContainerTable');
	if(playerTbls.length > 0) {
		tblOrderByChange();
		tblReverseOrderChange();
		tblPaginationChange();
		tblResize();
	}
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

function tblOrderByChange() {
	var radios = document.querySelectorAll('[name="orderedBy"]');
	for(let i = 0; i < radios.length; i++) {
		let radio = radios[i];
		//if same column is clicked flip order
		radio.addEventListener('click', function() {
			if(radio.getAttribute('checked') === 'checked') {
				let reverseRadios = document.querySelectorAll('[name="reverseOrder"]');
				if(reverseRadios[0].getAttribute('checked') === 'checked') {
					reverseRadios[1].click();
				} else {
					reverseRadios[0].click();
				}
			}
		});
		radio.addEventListener('change', function() {
			let radios = document.querySelectorAll('[name="reverseOrder"]');
			if(radios[0].getAttribute('checked') === 'checked') {
				radios[1].click();
			} else {
				//document.getElementById('statsNavigation').submit();
				fetchStats();
			}
		});
	}
}

function tblReverseOrderChange() {
	var radios = document.querySelectorAll('[name="reverseOrder"]');
	for(let i = 0; i < radios.length; i++) {
		radios[i].addEventListener('change', function() {
			//document.getElementById('statsNavigation').submit();
			fetchStats()
		});
	}
}

function tblPaginationChange() {
	document.getElementById('rowsPerPage').addEventListener('change', fetchStats);
	document.getElementById('pageNum').addEventListener('change', fetchStats);
	document.getElementsByClassName('tableNavLeft')[0].addEventListener('click', function() {
		let pageNum = document.getElementById('pageNum');
		pageNum.setAttribute('value', pageNum.getAttribute('value') - 1);
		fetchStats();
	});
	document.getElementsByClassName('tableNavRight')[0].addEventListener('click', function() {
		let pageNum = document.getElementById('pageNum');
		pageNum.setAttribute('value', Number(pageNum.getAttribute('value')) + 1);
		fetchStats();
	});
}

function tblResize() {
	let resizeBtn = document.getElementsByClassName('tblPopupResize')[0];
	resizeBtn.addEventListener('click', function() {
		let playoffSection = document.getElementById('playoff-section');
		if(playoffSection.style.display === '') 
			playoffSection.style.display = 'none';
		else
			playoffSection.style.display = '';
	});
}

function sortTable(event) {
	let th = event.target;
	let tr = th.parentNode;
	var index = Array.prototype.indexOf.call(tr.children, event.target);
	var offset = 2;
	var tables = [];
	var switching, rows, x, y, i, shouldSwitch;
	
	tables = document.getElementsByClassName('conferenceTable');
	if(tables.length === 0)
		tables = document.getElementsByClassName('divisionTable');
	
	if(sortedColumnIndex === (index + offset)) {
		descSortDirection = !descSortDirection;
	} else {
		sortedColumnIndex = index + offset;
		descSortDirection = true;
	}
	
	markHeaders(descSortDirection, tables, index);
	
	for(let l = 0; l < tables.length; l++) {
		switching = true;
		while(switching) {
			switching = false;
			rows = tables[l].rows;
			for(i = 1; i < (rows.length - 1); i++) {
				shouldSwitch = false;
				x = rows[i].getElementsByTagName('td')[index + offset];
				y = rows[i + 1].getElementsByTagName('td')[index + offset];
				if(descSortDirection) {
					if(Number(x.innerHTML) < Number(y.innerHTML)) {
						shouldSwitch = true;
						break;
					}
				} else {
					if(Number(x.innerHTML) > Number(y.innerHTML)) {
						shouldSwitch = true;
						break;
					}
				}
			}
			if(shouldSwitch) {
				rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
	      		switching = true;
			}
		}
	}
}

//'\u2191' up
//'\u2193' down

function markHeaders(descSort, tables, headerIndex) {
	markedHeaders.forEach((header) => {
		header.textContent = header.textContent.substring(0, header.textContent.indexOf(' '));
	});
	
	var headers = [];
	for(let i = 0; i < tables.length; i++) {
		headers[i] = tables[i].getElementsByTagName('tr')[0].getElementsByTagName('th')[headerIndex];
	}
	
	if(descSort) {
		headers.forEach((header) => {
			header.textContent += ' \u2193';
		});
	} else {
		headers.forEach((header) => {
			header.textContent += ' \u2191';
		});
	}
	markedHeaders = headers;
}

