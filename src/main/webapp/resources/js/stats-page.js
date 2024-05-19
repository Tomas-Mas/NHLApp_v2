document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
		setMouseHoverEvents();
	}
}

var expandedElement = null;
var sortedColumnIndex = 0;
var descSortDirection = true;
var markedHeaders = [];

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	document.querySelector('select#season').addEventListener('change', function() {
		let formAction = document.getElementById('statsNavigation').getAttribute('action');
		let newAction = formAction.substr(0, formAction.lastIndexOf('/') + 1);
		newAction += getActiveSeason();
		
		let paramsStart = formAction.indexOf('?');
		if(paramsStart > 0) {
			newAction += formAction.substr(paramsStart);
			document.getElementById('statsNavigation').setAttribute('action', newAction)
			document.getElementById('statsNavigation').submit();
		} else {
			window.location.href = newAction;
		}
	});
	
	document.getElementById('playoffContainer').onclick = playoffClicked;
	
	tblHeaders = document.getElementsByClassName('sortableHeader');
	for(let i = 0; i < tblHeaders.length; i++) {
		tblHeaders[i].onclick = sortTable;
	}
	
	//nav buttons
	navBtnChange();
	
	//player table events
	let playerTbls = document.getElementsByClassName('playerStatsContainerTable');
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

function navBtnChange() {
	var radios = document.getElementById("statsNav").getElementsByTagName("Input");
	for(let i = 0; i < radios.length; i++) {
		radios[i].addEventListener('change', function() {
			document.getElementById('statsNavigation').submit();
		});
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
				document.getElementById('statsNavigation').submit();
			}
		});
	}
}

function tblReverseOrderChange() {
	var radios = document.querySelectorAll('[name="reverseOrder"]');
	for(let i = 0; i < radios.length; i++) {
		radios[i].addEventListener('change', function() {
			document.getElementById('statsNavigation').submit();
		});
	}
}

function tblPaginationChange() {
	document.getElementById('rowsPerPage').addEventListener('change', function() {
		document.getElementById('statsNavigation').submit();
	});
	document.getElementById('pageNum').addEventListener('change', function() {
		document.getElementById('statsNavigation').submit();
	});
	document.getElementsByClassName('tableNavLeft')[0].addEventListener('click', function() {
		let pageNum = document.getElementById('pageNum');
		pageNum.setAttribute('value', pageNum.getAttribute('value') - 1);
		document.getElementById('statsNavigation').submit();
	});
	document.getElementsByClassName('tableNavRight')[0].addEventListener('click', function() {
		let pageNum = document.getElementById('pageNum');
		pageNum.setAttribute('value', Number(pageNum.getAttribute('value')) + 1);
		document.getElementById('statsNavigation').submit();
	});
}

function tblResize() {
	let resizeBtn = document.getElementsByClassName('tblPopupResize')[0];
	resizeBtn.addEventListener('click', function() {
		let playoffContainer = document.getElementById('playoffContainer');
		if(playoffContainer.style.display === '') 
			playoffContainer.style.display = 'none';
		else
			playoffContainer.style.display = '';
	});
}

function setMouseHoverEvents() {
	var els = document.getElementsByClassName('highlightable');
	for(var i = 0; i < els.length; i++) {
		els[i].addEventListener("mouseover", mouseOver);
		els[i].addEventListener("mouseout", mouseOut);
	}
}

function mouseOver(event) {
	var row = event.target.closest('tr').className;
	var teamId = row.substring(0, 7);
	var teamElements = document.getElementsByClassName(teamId);
	for(var i = 0; i < teamElements.length; i++) {
		teamElements[i].classList.add("ghost-highlight");
	}
}

function mouseOut(event) {
	var row = event.target.closest('tr').className;
	var teamId = row.substring(0, 7);
	var teamElements = document.getElementsByClassName(teamId);
	for(var i = 0; i < teamElements.length; i++) {
		teamElements[i].classList.remove("ghost-highlight");
	}
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

