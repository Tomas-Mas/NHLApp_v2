var gameId = window.location.pathname.substr(window.location.pathname.lastIndexOf('/') + 1);
const gameOverviewUrl = '/NHL/c/game/overview/' + gameId;
const gameStatsUrl = '/NHL/c/game/stats/' + gameId;
const gameMainSectionUrl = '/NHL/c/game/';
const h2hUrl = '/NHL/c/game/h2h/' + gameId;
const lastGamesUrl = '/NHL/c/game/lastGames/' + gameId;

const canvasHeight = 10;
const homeLineColor = "#000d1a";
const awayLineColor = "#cce6ff";

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		
		if(window.location.hash.length === 0) {
			history.replaceState({}, '', window.location.pathname + '#overview');
		}
		
		fetchMainSection();
		fetchLastGames();
		fetchHeadToHead();
		
		document.getElementById('mainMenu').addEventListener('click', menuClicked);
		document.getElementById('season').disabled = true;
		
		window.onpopstate = function() {
			fetchMainSection();
		};
	}
}

function fetchMainSection() {
	let period = '';
	if(history.state != null && history.state.periodNum != undefined) {
		period = history.state.periodNum;
	}
	
	let url = gameMainSectionUrl + window.location.hash.substr(1) + '/' + gameId + '/' + period;
	
	fetch(url)
		.then(response => response.text())
		.then(data => document.getElementById('main-section').innerHTML = data)
		.then(setMainSectionEvents)
		.catch(err => console.log(err));
}

function fetchLastGames(event) {
	let locker = document.getElementById('nav-locker');
	if(locker && locker.checked) {
		document.querySelector('.last-games-header .nav-lock').classList.add('alrt');
		setTimeout(() => {
			document.querySelector('.last-games-header .nav-lock').classList.remove('alrt');
		}, 1000);
		return;
	}
	
	let url = lastGamesUrl;
	let formData = document.getElementById('last-games-form');
	
	if(formData != null) {
		let params = new URLSearchParams();
		new FormData(formData).forEach((value, key) => {
			params.append(key, value);
		});
		url += '?' + params;
	}
	
	if(event != undefined && event.target.id === 'last-games-expand-btn') {
		url += '&homeGamesCount=' + document.querySelectorAll('#last-games-data .last-games-tbl')[0].querySelectorAll('tr').length;
		url += '&awayGamesCount=' + document.querySelectorAll('#last-games-data .last-games-tbl')[1].querySelectorAll('tr').length;
	}
	
	fetch(url)
		.then(response => response.text())
		.then(data => document.getElementById('h2h-last-games').innerHTML = data)
		.then(() => {
			document.getElementById('last-games-expand-btn').addEventListener('click', fetchLastGames);
			document.getElementById('homeScope').addEventListener('change', fetchLastGames);
			document.getElementById('awayScope').addEventListener('change', fetchLastGames);
			
			document.getElementById('nav-locker').addEventListener('change', (event) => {
				if(!event.target.checked && gameId != window.location.pathname.substr(window.location.pathname.lastIndexOf('/') + 1)) {
					window.location.href = gamePageUrl + gameId + window.location.hash;
				}
				
				if(event.target.checked) {
					document.getElementById('homeScope').disabled = true;
					document.getElementById('awayScope').disabled = true;
				} else {
					document.getElementById('homeScope').disabled = false;
					document.getElementById('awayScope').disabled = false;
				}
			});
			
			document.querySelectorAll('.last-games-tbl tr').forEach((gameRow) => {
				gameRow.addEventListener('mousedown', (event) => {
					let targetUrl = gamePageUrl + gameRow.id + window.location.hash;
					if(event.button === 1) {
						window.open(targetUrl, '_blank');
					} else if(event.button === 0) {
						if(document.getElementById('nav-locker').checked) {
							gameId = gameRow.id;
							fetchMainSection();
							
							let selectedGame = document.querySelector('#last-games-data tr.selected');
							if(selectedGame) 
								selectedGame.classList.remove('selected');
							gameRow.classList.add('selected');
						} else {
						window.location.href = targetUrl;
						}
					}
				});
			});
		})
		.catch(err => console.log(err));
}

function fetchHeadToHead() {
	fetch(h2hUrl)
		.then(response => response.text())
		.then(data =>	document.getElementById('head2head-section').innerHTML += data)
		.then(() => {
			document.querySelectorAll('.h2h-game').forEach((game) => {
				game.addEventListener('mousedown', (event) => {
					let targetUrl = gamePageUrl + game.id + window.location.hash;
					if(event.button === 1) {
						window.open(targetUrl, '_blank');
					} else if(event.button === 0 && !game.classList.contains('selected')) {
						if(document.getElementById('nav-locker').checked) {
							gameId = game.id;
							fetchMainSection();
							document.querySelector('.h2h-game.selected').classList.remove('selected');
							game.classList.add('selected');
						} else {
						window.location.href = targetUrl;
						}
					}
				});
			});
		})
		.catch(err => console.log(err));
}

function setMainSectionEvents() {
	//submenu
	document.querySelectorAll('#submenu span').forEach((element) => {
		element.addEventListener('mousedown', (event) => {
			if(event.button === 0) {
				if(element.textContent === 'Overview' && !element.classList.contains('selected')) {
					window.location.hash = 'overview';
				} else if(element.textContent === 'Game Stats' && !element.classList.contains('selected')) {
					window.location.hash = 'stats';
				}
				//fetchMainSection();
			}
		});
	});
	
	//game header
	document.querySelectorAll('.team-info div').forEach((element) => {
		element.addEventListener('click', () => {
			let teamAbr = element.parentElement.id.substr(element.parentElement.id.indexOf('-') + 1);
			window.location.href = teamPageUrl + teamAbr;
		});
	});
	
	//stats only events(drawing lines and period submenu events)
	if (window.location.hash === '#stats') {
		let periodMenuList = document.querySelectorAll('#period-nav span');
		for (let i = 0; i < periodMenuList.length; i++) {
			let periodSpan = periodMenuList[i];
			periodSpan.addEventListener('click', () => {
				if (!periodSpan.classList.contains('selected')) {
					history.replaceState({ periodNum: i }, '');
					fetchMainSection();
				}
			});
		}
		drawStatLines();
	}
}

function drawStatLines() {
	let lines = document.getElementsByClassName('stat-line');
	for(let i = 0; i < lines.length; i++) {
		let line = lines[i];
		let header = line.getElementsByClassName('stat-line-text')[0];
		let headerData = header.getElementsByTagName('div');
		
		let hData = 0;
		let aData = 0;
		if(headerData[0].innerText.indexOf('%') > 0) {
			hData = Number(headerData[0].innerText.substring(0, headerData[0].innerText.indexOf("%")));
			aData = Number(headerData[2].innerText.substring(0, headerData[2].innerText.indexOf("%")));
		} else if(headerData[0].innerText.indexOf(" ") > 0) {
			hData = Number(headerData[0].innerText.substring(0, headerData[0].innerText.indexOf(" ")));
			aData = Number(headerData[2].innerText.substring(0, headerData[2].innerText.indexOf(" ")));
		} else {
			hData = Number(headerData[0].innerText);
			aData = Number(headerData[2].innerText);
		}
		
		if(hData === 0 && aData == 0) {
			hData = 1;
			aData = 1;
		}
		
		let lineDiv = line.getElementsByClassName('stat-line-graphics')[0];
		let canvasWidth = lineDiv.clientWidth;
		let homeWidth = (hData / (hData + aData)) * canvasWidth;
		let awayWidth = (aData / (hData + aData)) * canvasWidth;
		
		let canvas = lineDiv.getElementsByTagName('canvas')[0];
		canvas.width = canvasWidth;
		canvas.height = canvasHeight;
		
		let context = canvas.getContext('2d');
		drawLine(context, homeLineColor, 0, homeWidth - 5);
		drawLine(context, awayLineColor, canvasWidth, canvasWidth - awayWidth + 5);
	}
}

function drawLine(context, color, startWidth, endWidth) {
	context.strokeStyle = color;
	context.lineWidth = canvasHeight;
	context.beginPath();
	context.moveTo(startWidth, canvasHeight / 2);
	context.lineTo(endWidth, canvasHeight / 2);
	context.stroke();
}