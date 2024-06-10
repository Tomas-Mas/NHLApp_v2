const gameId = window.location.pathname.substr(window.location.pathname.lastIndexOf('/') + 1);
const gameOverviewUrl = '/NHL/c/game/overview/' + gameId;
const gameStatsUrl = '/NHL/c/game/stats/' + gameId;
const gameMainSectionUrl = '/NHL/c/game/';
const h2hUrl = '/NHL/c/game/h2h/' + gameId;

const canvasHeight = 10;
const homeLineColor = "#000d1a";
const awayLineColor = "#cce6ff";

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		
		if(window.location.hash.length === 0) {
			history.replaceState({}, '', window.location.pathname + '#overview');
		}
		
		fetchMainSection();
		fetchSidebarSection();
		
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
	//console.log('fetching url: ' + url);
	
	fetch(url)
		.then(response => response.text())
		.then(data => document.getElementById('main-section').innerHTML = data)
		.then(() => {
			setMainSectionEvents();
			if(window.location.hash === '#stats') {
				let periodMenuList = document.querySelectorAll('#period-nav span');
				for(let i = 0; i < periodMenuList.length; i++) {
					let periodSpan = periodMenuList[i];
					periodSpan.addEventListener('click', () => {
						if(!periodSpan.classList.contains('selected')) {
							history.replaceState({periodNum: i}, '');
							fetchMainSection();
						}
					});
				}
				drawStatLines();
			}
		})
		.catch(err => console.log(err));
}

function fetchSidebarSection() {
	fetch(h2hUrl)
		.then(response => response.text())
		.then(data => document.getElementById('right-sidebar-section').innerHTML = data)
		.then(() => {
			document.querySelectorAll('.h2h-game').forEach((game) => {
				game.addEventListener('mousedown', (event) => {
					let targetUrl = gamePageUrl + game.id + window.location.hash;
					if(event.button === 1) {
						window.open(targetUrl, '_blank');
					} else if(event.button === 0 && !game.classList.contains('selected')) {
						//TODO add last x game to h2h list and lock sidebar as navigation and only reload main section
						window.location.href = targetUrl;
					}
				});
			});
		})
		.catch(err => console.log(err));
}

function setMainSectionEvents() {
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