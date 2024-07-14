var gameId = window.location.pathname.substr(window.location.pathname.lastIndexOf('/') + 1);

/*const gameOverviewUrl = '/NHL/c/game/overview/' + gameId;
const gameStatsUrl = '/NHL/c/game/stats/' + gameId;
const playersStatsUrl = '/NHL/c/game/players/' + gameId;*/

const jerseySrc = '/NHL/src/img/sim-icons/';

const gameMainSectionUrl = '/NHL/c/game/';
const h2hUrl = '/NHL/c/game/h2h/' + gameId;
const lastGamesUrl = '/NHL/c/game/lastGames/' + gameId;
const fullEventsUrl = '/NHL/c/game/fullEvents/' + gameId;

const defaultStartingEvent = 0;
var eventsData;
var currEventIndex = defaultStartingEvent;
var isFirstPeriodFlipped;
var elementIntervalsMap = {};
var objectCoordsMap = {};

const animationTimeConst = 1000;
const eventAnimationTimeConst = 1000;
const afterEventTimeout = 1500;
const gameSecondTime = 1000;
var gameSecondTimeoutHandler;
const displayedEventsBeforeCurrent = 5;
const displayedEventsAfterCurrent = 20;

const pitchDivWidth = 750;
const pitchDivHeight = 468;
const pitchWidth = 640;
const pitchHeight = 320;
const pitchCenter = {'x': 362.5,'y': 221.5};

const mainRefHomeDefPos = {'x': 345, 'y': 65};
const mainRefAwayDefPos = {'x': 380, 'y': 65};	
const lineRefHomeDefPos = {'x': 270, 'y': 80};
const lineRefAwayDefPos = {'x': 455, 'y': 80};

const goalCelebration = {
	'center': {'x': 362.5, 'y': 80},
	'lw': {'x': 380, 'y': 65},
	'rw': {'x': 380, 'y': 95},
	'dTop': {'x': 345, 'y': 65},
	'dBot': {'x': 345, 'y': 95},
	'offset': {'x': 100, 'y': 0}
};

const benchPlayerCount = 12;
const hBenchStartX = 190;
const hBenchEndX = 350;
const aBenchStartX = 390;
const aBenchEndX = 550;
const benchY = 40;
const penaltyBoxY = 400;
const homePenaltyBoxX = 345;
const awayPenaltyBoxX = 380;
const homeGoalX = 80;
const awayGoalX = 650;

const puckWidth = 25;
const puckHeight = 25;
const playerWidth = 25;
const playerHeight = 25;
const goalieHeight = 50;

const goalieHomeBenchPos = {'x': (hBenchStartX - ((hBenchEndX - hBenchStartX) / benchPlayerCount)), 'y': benchY - 12.5};
const goalieAwayBenchPos = {'x': aBenchEndX, 'y': benchY - 12.5};
const goalieHomeGoalPos = {'x': homeGoalX, 'y': pitchCenter.y - goalieHeight / 4};
const goalieAwayGoalPos = {'x': awayGoalX, 'y': pitchCenter.y - goalieHeight / 4};

const canvasHeight = 10;
const homeLineColor = "#000d1a";
const awayLineColor = "#cce6ff";

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		
		if(window.location.hash.length === 0) {
			history.replaceState({}, '', window.location.pathname + '#overview');
		}
		
		fetchMainSection();
		/*if(window.location.hash != '#simulation') {
			fetchLastGames();
			fetchHeadToHead();
		}*/
		
		document.getElementById('mainMenu').addEventListener('click', menuClicked);
		document.getElementById('season').disabled = true;
		
		window.onpopstate = function() {
			fetchMainSection();
		};
	}
}

function fetchMainSection(event) {
	/*let period = '';
	if(history.state != null && history.state.periodNum != undefined) {
		period = history.state.periodNum;
	}
	
	let url = gameMainSectionUrl + window.location.hash.substr(1) + '/' + gameId + '/' + period;*/
	
	let url = gameMainSectionUrl + window.location.hash.substr(1) + '/' + gameId;
	
	if(history.state != null && history.state.periodNum != undefined) {
		url += '/' + history.state.periodNum;
	}
	
	let formData = document.getElementById('player-filter');
	if(formData) {
		let params = new URLSearchParams();
		new FormData(formData).forEach((value, key) => {
			params.append(key, value);
		});
		url += '?' + params;
	}
	
	if(event && event.type === 'click') {
		let clickedHeader = event.target;
		let clickedHeaderTitle = clickedHeader.closest('th').title.split(' ').join('');
		url += '&orderByColumn=' + clickedHeaderTitle;
		
		if(clickedHeader.textContent.includes(downArrStr)) {
			url += '&isDescOrder=' + false;
		} else {
			url += '&isDescOrder=' + true;
		}
	}
	
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
	document.getElementById('right-sidebar-section').style.display = "block";
	if(document.getElementById('head2head-section').children.length < 3 && window.location.hash != '#simulation') {
		fetchLastGames();
		fetchHeadToHead();
	}
	
	//submenu
	document.querySelectorAll('#submenu span').forEach((element) => {
		element.addEventListener('mousedown', (event) => {
			if(event.button === 0) {
				if(element.textContent === 'Overview' && !element.classList.contains('selected')) {
					window.location.hash = 'overview';
				} else if(element.textContent === 'Game Stats' && !element.classList.contains('selected')) {
					window.location.hash = 'stats';
				} else if(element.textContent === 'Players Stats' && !element.classList.contains('selected')) {
					window.location.hash = 'players';
				} else if(element.textContent === 'Simulation' && !element.classList.contains('selected')) {
					window.location.hash = 'simulation';
				}
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
	if(window.location.hash === '#stats') {
		let periodMenuList = document.querySelectorAll('#period-nav span');
		for (let i = 0; i < periodMenuList.length; i++) {
			let periodSpan = periodMenuList[i];
			periodSpan.addEventListener('click', () => {
				if(!periodSpan.classList.contains('selected')) {
					history.replaceState({ periodNum: i }, '');
					fetchMainSection();
				}
			});
		}
		drawStatLines();
	}
	
	//player stats events
	if(window.location.hash === '#players') {
		document.getElementById('player-filter').addEventListener('submit', (event) => {event.preventDefault;});
		document.getElementById('player-filter').addEventListener('change', fetchMainSection);
		document.querySelectorAll('#skater-stats th').forEach((header) => {
			header.addEventListener('click', fetchMainSection);
		});
	}
	
	//simulation
	if(window.location.hash === '#simulation') {
		document.getElementById('right-sidebar-section').style.display = "none";
		
		fetch(fullEventsUrl)
			.then(response => response.json())
			.then(data => {
				eventsData = data;
				setSidesByEvents();
				manageAnimation(currEventIndex);
				
				document.querySelectorAll('.simulation-controlls .sim-nav-btn input').forEach(
					btn => btn.addEventListener('click', () => {
						let playBtn = document.getElementById('sim-play');
						if(btn.id === 'sim-prev' && currEventIndex > 0) {
							if(playBtn.classList.contains('active')) {
								playBtn.classList = 'inactive';
								playBtn.value = 'play';
								window.clearTimeout(gameSecondTimeoutHandler);
								manageAnimation(currEventIndex);
							} else {
								manageAnimation(currEventIndex - 1);
							}
						} else if(btn.id === 'sim-next' && currEventIndex < eventsData.length - 1) {
							if(playBtn.classList.contains('active')) {
								playBtn.classList = 'inactive';
								playBtn.value = 'play';
								window.clearTimeout(gameSecondTimeoutHandler);
								manageAnimation(currEventIndex + 1);
							} else {
								manageAnimation(currEventIndex + 1);
							}
						} else if(btn.id === 'sim-play' && btn.classList.contains('inactive')) {
							btn.classList = 'active';
							btn.value = 'pause';
							manageAnimation(currEventIndex);
						} else if(btn.id === 'sim-play' && btn.classList.contains('active')) {
							btn.classList = 'inactive';
							btn.value = 'play';
							window.clearTimeout(gameSecondTimeoutHandler);
							currEventIndex++;
						} else if(btn.id === 'sim-stop') {
							playBtn.classList = 'inactive';
							playBtn.value = 'play';
							manageAnimation(0);
						}
					})
				);
				
				document.getElementById('simulation-sidebar').addEventListener('click', (event) => {
					let playBtn = document.getElementById('sim-play');
					let row = event.target.closest('tr');
					
					if(row === null || row.classList.contains('selected'))
						return;
					
					if(playBtn.classList.contains('active')) {
						playBtn.classList.add('alrt');
						setTimeout(() => {
							playBtn.classList.remove('alrt');
						}, 1000);
						return;
					}
					
					document.querySelectorAll('#simulation-sidebar tr.selected').forEach((row) => {row.classList.remove('selected')});
					let eventId = row.classList[0].substring(row.classList[0].indexOf('-') + 1);
					row.classList.add('selected');
					manageAnimation(Number(eventId));
				});
				
				document.getElementById('sim-range').addEventListener('change', (event) => {
					let playBtn = document.getElementById('sim-play');
					if(playBtn.classList.contains('active')) {
						document.getElementById('sim-range').value = currEventIndex;
						playBtn.classList.add('alrt');
						setTimeout(() => {
							playBtn.classList.remove('alrt');
						}, 1000);
						return;
					}
					manageAnimation(Number(event.target.value));
				});
			})
			.catch(err => console.log(err));
	}
}

function manageAnimation(nextEventIndex) {
	let newEvent = eventsData[nextEventIndex];
	
	//update teams on first load
	let homeDiv = document.querySelectorAll('.home-team.team-info')[0];
	if(homeDiv.id.includes('?')) {
		let awayDiv = document.querySelectorAll('.away-team.team-info')[0];
		homeDiv.id = 'home-' + newEvent.scoreboard.homeAbr;
		let homeImg = homeDiv.querySelectorAll('img')[0];
		homeImg.src =  '/NHL/src/img/team-icons/' + newEvent.scoreboard.homeAbr + '.png';
		homeImg.title = newEvent.scoreboard.homeAbr;
		homeImg.alt = newEvent.scoreboard.homeAbr;
		homeDiv.querySelectorAll('.team-name')[0].textContent = newEvent.scoreboard.homeName;
		
		awayDiv.id = 'away-' + newEvent.scoreboard.awayAbr;
		let awayImg = awayDiv.querySelectorAll('img')[0];
		awayImg.src =  '/NHL/src/img/team-icons/' + newEvent.scoreboard.awayAbr + '.png';
		awayImg.title = newEvent.scoreboard.awayAbr;
		awayImg.alt = newEvent.scoreboard.awayAbr;
		awayDiv.querySelectorAll('.team-name')[0].textContent = newEvent.scoreboard.awayName;
		
		drawBenches(newEvent.scoreboard.homeAbr, newEvent.scoreboard.awayAbr);
	}
	
	//runs a loop for each in-game second until game time for next event is reached
	let secondsSpeed = gameSecondTime / document.querySelector('.simulation-controlls #sim-speed').value;
	if(document.getElementById('sim-play').classList.contains('active')) {
		let currTime = getCurrentGameTime();
		let eventTime = timeToSeconds(newEvent.periodNumber, newEvent.periodTime);
		if(currTime < eventTime) {
			gameSecondTimeoutHandler = window.setTimeout(() => {
				document.querySelector('#game-header .result .game-date').textContent = timeToString(getCurrentGameTime() + 1);
				manageAnimation(nextEventIndex);
			}, secondsSpeed);
			return;
		}
	}
	
	updateSimulationSidebar(nextEventIndex);
	document.getElementById('sim-range').value = nextEventIndex;
	currEventIndex = nextEventIndex;
	
	//update scoreboard data
	let scoreboardEl = document.querySelectorAll('#game-header .result')[0];
	scoreboardEl.querySelectorAll('.game-date')[0].textContent = newEvent.periodTime;
	scoreboardEl.querySelectorAll('.score')[0].textContent = newEvent.scoreboard.homeScore + ' - ' + newEvent.scoreboard.awayScore;
	scoreboardEl.querySelectorAll('.game-status')[0].textContent = newEvent.periodNumber + ' ' + newEvent.periodType;
	
	//show players data
	showActorsJerseyNumbers(newEvent);
	
	//animation
	animate(newEvent);
}

function updateSimulationSidebar(eventIndex) {
	let eventRows = document.querySelectorAll('#simulation-sidebar tr');
	eventRows.forEach(row => row.style.display = 'none');
	
	let startIndex = eventIndex - displayedEventsBeforeCurrent;
	let endIndex = eventIndex + displayedEventsAfterCurrent;
	
	let offset = Math.abs(startIndex);
	if(startIndex < 0) {
		startIndex = startIndex + offset
		endIndex = displayedEventsBeforeCurrent + displayedEventsAfterCurrent - startIndex;
	}
	offset = endIndex - eventRows.length + 1;
	if(offset > 0) {
		startIndex = startIndex - offset;
		endIndex = endIndex - offset;
	}
	
	for(let i = startIndex; i <= endIndex; i++) {
		eventRows[i].style.display = 'block';
	}
	
	document.querySelectorAll('#simulation-sidebar tr.selected').forEach((row) => {row.classList.remove('selected')});
	eventRows[eventIndex].classList.add('selected');
	
}

//makes sure team sides are correctly displayed
function setSidesByEvents() {
	let distanceSum = 0;
	let distanceCount = 0;
	for(let i = 0; i < eventsData.length; i++) {
		let e = eventsData[i];
		let eX = translateCoord(e.coordX, e.coordY, 0, 0).x;
		if((e.periodNumber === 1 || e.periodNumber === 3) && (e.name === 'Shot On Goal' || e.name === 'Missed Shot')) {
			if(e.actedBy === 'HOME') {
				distanceSum += Math.abs(eX - awayGoalX);
				distanceCount ++;
			} else if(e.actedBy === 'AWAY') {
				distanceSum += Math.abs(eX - homeGoalX);
				distanceCount ++;
			}
		}
	}
	if(distanceSum / distanceCount > pitchCenter.x - homeGoalX)
		isFirstPeriodFlipped = true;
	else
		isFirstPeriodFlipped = false;
}

function showActorsJerseyNumbers(eventData) {
	//clear old numbers
	document.querySelectorAll('.skater-jersey-num').forEach((span) => {
		span.remove();
	});
	
	if(!eventData.mainActor) {
		return;
	}
	
	if(eventData.actedBy === 'HOME') {
		showActorJersey('skater-c-home', eventData.mainActor.number);
		
		if(eventData.name === 'Faceoff' || eventData.name === 'Hit' || eventData.name === 'Blocked Shot' || eventData.name === 'Penalty') {
			if(eventData.secondaryActors[0])
				showActorJersey('skater-c-away', eventData.secondaryActors[0].number);
		} else if(eventData.name === 'Shot On Goal') {
			if(eventData.secondaryActors[0])
				showActorJersey('goalie-away', eventData.secondaryActors[0].number);
		} else if(eventData.name === 'Goal') {
			let assists = 0;
			eventData.secondaryActors.forEach((actor) => {
				if(actor.role === 'Goalie') {
					showActorJersey('goalie-away', actor.number);
				} else if(actor.role === 'Assist') {
					if(assists === 0) {
						showActorJersey('skater-d-bot-home', actor.number);
						assists++;
					} else {
						showActorJersey('skater-d-top-home', actor.number);
					}
				}
			});
		}
		
	} else if(eventData.actedBy === 'AWAY') {
		showActorJersey('skater-c-away', eventData.mainActor.number);
		
		if(eventData.name === 'Faceoff' || eventData.name === 'Hit' || eventData.name === 'Blocked Shot' || eventData.name === 'Penalty') {
			if(eventData.secondaryActors[0])
				showActorJersey('skater-c-home', eventData.secondaryActors[0].number);
		} else if(eventData.name === 'Shot On Goal') {
			if(eventData.secondaryActors[0])
				showActorJersey('goalie-home', eventData.secondaryActors[0].number);
		} else if(eventData.name === 'Goal') {
			let assists = 0;
			eventData.secondaryActors.forEach((actor) => {
				if(actor.role === 'Goalie') {
					showActorJersey('goalie-home', actor.number);
				} else if(actor.role === 'Assist') {
					if(assists === 0) {
						showActorJersey('skater-d-bot-away', actor.number);
						assists++;
					} else {
						showActorJersey('skater-d-top-away', actor.number);
					}
				}
			});
		}
	}
}

function showActorJersey(actorId, jerseyNum) {
	let skaterDiv = document.getElementById(actorId);
	let span = document.createElement('span');
	span.classList.add('skater-jersey-num');
	span.textContent = jerseyNum;
	skaterDiv.appendChild(span);
}

function animate(newEvent) {
	let runAnimation = document.getElementById('sim-play').classList.contains('active') ? true : false;
	let animationTime, eventAnimationTime;
	if(runAnimation) {
		animationTime = animationTimeConst;
		eventAnimationTime = eventAnimationTimeConst;
	} else {
		animationTime = 10;
		eventAnimationTime = 0;
	}
	
	var eventAnimationCycleCount = {
		'default': 0,
		'faceoff': 2,
		'hit': 2,
		'giveaway': 2,
		'takeaway': 2,
		'shot': 2,
		'blockedShot': 3,
		'penalty': 4,
		'goal': 5,
	};
	nextAnimationTimeout = 0;
	
	var penaltyBox = {'home': newEvent.scoreboard.homeBox, 'away': newEvent.scoreboard.awayBox};
	let newEventCoords = {'x': newEvent.coordX, 'y': newEvent.coordY};
	
	if((isFirstPeriodFlipped && newEvent.periodNumber % 2 === 1) || (!isFirstPeriodFlipped && newEvent.periodNumber % 2 === 0)) {
		newEventCoords = {'x': newEventCoords.x * -1, 'y': newEventCoords.y * -1};
	}
	
	let eventCoords = translateCoord(newEventCoords.x, newEventCoords.y, 0, 0);
	let puckEventStartPos = translateCoord(newEventCoords.x, newEventCoords.y, puckWidth, puckHeight);
	let zone = getEventsZone(eventCoords.x);
	let puckActorOffsetX = 20;
	let playerShotOffsetX = 50;
	
	objectCoordsMap['goalie-home'] = goalieHomeGoalPos;
	objectCoordsMap['goalie-away'] = goalieAwayGoalPos;
	
	if(newEvent.name === 'Period Start' || newEvent.name === 'Stoppage' || newEvent.name === 'Period End' || newEvent.name === 'Shootout End' || newEvent.name === 'Game End') {
		changeEventDescription(newEvent.name, false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.default * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		objectCoordsMap['mainref-home'] = mainRefHomeDefPos;
		objectCoordsMap['mainref-away'] = mainRefAwayDefPos;
		objectCoordsMap['lineref-home'] = lineRefHomeDefPos;
		objectCoordsMap['lineref-away'] = lineRefAwayDefPos;
		
		if(newEvent.name === 'Period Start') {
			objectCoordsMap['goalie-home'] = goalieHomeBenchPos;
			objectCoordsMap['goalie-away'] = goalieAwayBenchPos;	
		}
		
		document.querySelectorAll('.skater-home').forEach((skater) => {
			objectCoordsMap[skater.id] = getRandomBenchCoord('home');
		});
		document.querySelectorAll('.skater-away').forEach((skater) => {
			objectCoordsMap[skater.id] = getRandomBenchCoord('away');
		});
	} else if(newEvent.name === 'Faceoff') {
		changeEventDescription('Faceoff between ' + fullNameOf(newEvent.mainActor) + ' and ' + fullNameOf(newEvent.secondaryActors[0]), false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.faceoff * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setFaceoffStartCoords(penaltyBox, eventCoords, zone);
	} else if(newEvent.name === 'Giveaway') {
		changeEventDescription(fullNameOf(newEvent.mainActor), false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.giveaway * animationTime;
		document.getElementById('puck').style.display = 'none';
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		objectCoordsMap['puck'] = puckEventStartPos;
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x - puckActorOffsetX;
		} else {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x + puckActorOffsetX;
		}
	} else if(newEvent.name === 'Takeaway') {
		changeEventDescription(fullNameOf(newEvent.mainActor), false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.takeaway * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x + puckActorOffsetX;
		} else {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x - puckActorOffsetX;
		}
	} else if(newEvent.name === 'Shot On Goal' || newEvent.name === 'Missed Shot') {
		changeEventDescription(fullNameOf(newEvent.mainActor) + ' is getting ready to shoot', false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.shot * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['skater-c-away'].x = objectCoordsMap['skater-c-away'].x + playerShotOffsetX;
		} else {
			objectCoordsMap['skater-c-home'].x = objectCoordsMap['skater-c-home'].x - playerShotOffsetX;
		}
	} else if(newEvent.name === 'Blocked Shot') {
		changeEventDescription(fullNameOf(newEvent.secondaryActors[0]) + ' is getting ready to shoot', false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.blockedShot * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['skater-c-home'].x = eventCoords.x - Math.abs((goalieHomeGoalPos.x - eventCoords.x) / 2);
			objectCoordsMap['skater-c-home'].y = eventCoords.y + ((goalieHomeGoalPos.y - eventCoords.y) / 2);
		} else {
			objectCoordsMap['skater-c-away'].x = eventCoords.x + Math.abs((goalieAwayGoalPos.x - eventCoords.x) / 2);
			objectCoordsMap['skater-c-away'].y = eventCoords.y + ((goalieAwayGoalPos.y - eventCoords.y) / 2);
		}
	} else if(newEvent.name === 'Penalty') {
		changeEventDescription('Penalty - ' + fullNameOf(newEvent.mainActor) + ' - ' + newEvent.penaltyMinutes + ' for ' + newEvent.secondaryType, false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.penalty * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x + puckActorOffsetX;
		} else {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x - puckActorOffsetX;
		}
	} else if(newEvent.name === 'Goal') {
		changeEventDescription(fullNameOf(newEvent.secondaryActors[0]) + ' is getting ready to shoot', false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.goal * animationTime;
		document.getElementById('puck').style.display = 'none';
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		
		let goalAssists = assistsCount(newEvent);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['skater-c-away'].x = objectCoordsMap['skater-c-away'].x + playerShotOffsetX;
			if(goalAssists === 0) {
				objectCoordsMap['puck'] = puckEventStartPos;
			} else if(goalAssists === 1) {
				objectCoordsMap['puck'] = objectCoordsMap['skater-d-top-home'];
				objectCoordsMap['puck'].x += puckWidth / 2;
			} else {
				objectCoordsMap['puck'] = objectCoordsMap['skater-d-bot-home'];
			}
		} else {
			objectCoordsMap['skater-c-home'].x = objectCoordsMap['skater-c-home'].x - playerShotOffsetX;
			if(goalAssists === 0) {
				objectCoordsMap['puck'] = puckEventStartPos;
			} else if(goalAssists === 1) {
				objectCoordsMap['puck'] = objectCoordsMap['skater-d-top-away'];
			} else {
				objectCoordsMap['puck'] = objectCoordsMap['skater-d-bot-away'];
			}
		}
	} else if(newEvent.name === 'Hit') {
		changeEventDescription(fullNameOf(newEvent.secondaryActors[0]), false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.hit * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x + puckActorOffsetX;
		} else {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x - puckActorOffsetX;
		}
	} else {
		changeEventDescription(newEvent.name, false);
		animationTimeout = eventAnimationTime + eventAnimationCycleCount.default * animationTime;
		document.getElementById('puck').style.display = 'none';
		objectCoordsMap['puck'] = puckEventStartPos;
		setDefaultEventStartCoords(penaltyBox, eventCoords, zone);
		if(newEvent.actedBy === 'HOME') {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x + puckActorOffsetX;
		} else {
			objectCoordsMap['puck'].x = objectCoordsMap['puck'].x - puckActorOffsetX;
		}
	}
	
	Object.keys(objectCoordsMap).forEach(key => {
		move(document.getElementById(key), objectCoordsMap[key], eventAnimationTime);
	});
	
	if(runAnimation) {
		window.setTimeout(() => {
			animateEvent(animationTime, eventCoords, newEvent);
		}, eventAnimationTime);
		
		window.setTimeout(() => {
			manageAnimation(currEventIndex + 1);
		}, animationTimeout + afterEventTimeout);
	}
}

function move(element, newCoord, animationTime) {
	const animationInterval = 10;
	
	let startX = element.style.left.length === 0 ? 0 : readPositionNumber(element.style.left);
	let startY = element.style.top.length === 0 ? 0 : readPositionNumber(element.style.top);
	let id = element.id;
	
	if(startX === 0 || startY === 0) {
		element.style.left = newCoord.x + 'px';
		element.style.top = newCoord.y + 'px';
	} else {
		let stepX = (newCoord.x - startX) / (animationTime / animationInterval);
		let stepY = (newCoord.y - startY) / (animationTime / animationInterval);
		//console.log(element.id + ': newStep: ' + stepX + 'x' + stepY);
		let interval = elementIntervalsMap[id];
		//console.log('interval: ' + interval);
		element.style.left = startX + 'px';
		element.style.top = startY + 'px';
		clearInterval(interval);
		interval = setInterval(() => {
			/*
			//buggy as hell :( - changed to timeouts
			if(((stepX > 0 && readPositionNumber(element.style.left) >= newCoord.x) || (stepX < 0 && readPositionNumber(element.style.left) <= newCoord.x)) ||
					((stepY > 0 && readPositionNumber(element.style.top) >= newCoord.y) || (stepY < 0 && readPositionNumber(element.style.top) <= newCoord.y)) ||
					stepX === 0 || stepY === 0) {
				console.log('clearing interval: ' + interval);
				clearInterval(interval);
			} else {
				element.style.left = readPositionNumber(element.style.left) + stepX + 'px';
				element.style.top = readPositionNumber(element.style.top) + stepY + 'px';
			}*/
			
			element.style.left = readPositionNumber(element.style.left) + stepX + 'px';
			element.style.top = readPositionNumber(element.style.top) + stepY + 'px';
		}, animationInterval);
		elementIntervalsMap[id] = interval;
		window.setTimeout(function() {
			clearInterval(interval)
		}, animationTime);
	}
}

function drawBenches(homeAbr, awayAbr) {
	let pitch = document.getElementById('pitch-container');
	
	//home bench
	for(let i = 0; i < benchPlayerCount; i++) {
		let homePlayerDiv = document.createElement('div');
		homePlayerDiv.classList.add('player-animation');
		
		let playerImg = document.createElement('img');
		playerImg.onerror = function() {this.src = jerseySrc + 'jersey-default-home.png';}
		playerImg.src = jerseySrc + 'jersey-' + homeAbr + '-home.png';
		
		homePlayerDiv.appendChild(playerImg);
		pitch.appendChild(homePlayerDiv);
		
		homePlayerDiv.style.top = benchY + 'px';
		homePlayerDiv.style.left = (hBenchStartX + i * ((hBenchEndX - hBenchStartX) / benchPlayerCount)) + 'px';
	}
	
	//home skaters image and default position in the bench
	document.querySelectorAll('.skater-home').forEach((skater) => {
		let playerImg = document.createElement('img');
		playerImg.onerror = function() {this.src = jerseySrc + 'jersey-default-home.png'; }
		playerImg.src = jerseySrc + 'jersey-' + homeAbr + '-home.png';
		skater.appendChild(playerImg);
	});
	
	//home goalies
	document.querySelectorAll('.goalie-home').forEach(goalie => {
		let playerImg = document.createElement('img');
		playerImg.classList.add('goalie-jersey-img');
		playerImg.onerror = function() { this.src = jerseySrc + 'jersey-default-home.png'; }
		playerImg.src = jerseySrc + 'jersey-' + homeAbr + '-home.png';
		let helmetImg = document.createElement('img');
		helmetImg.classList.add('goalie-helmet-img');
		helmetImg.src = jerseySrc + 'goalie-helmet.png';
		goalie.appendChild(playerImg);
		goalie.appendChild(helmetImg);
		
		goalie.style.top = benchY - 12.5 + 'px';
		goalie.style.left = (hBenchStartX - ((hBenchEndX - hBenchStartX) / benchPlayerCount)) + 'px';
	});
	
	//away bench
	for(let i = 0; i < benchPlayerCount; i++) {
		let awayPlayerDiv = document.createElement('div');
		awayPlayerDiv.classList.add('player-animation');
		
		let playerImg = document.createElement('img');
		playerImg.onerror = function() {this.src = jerseySrc + 'jersey-default-away.png';}
		playerImg.src = jerseySrc + 'jerset-' + awayAbr + '-away.png';
		
		awayPlayerDiv.appendChild(playerImg);
		pitch.appendChild(awayPlayerDiv);
		
		awayPlayerDiv.style.top = benchY + 'px';
		awayPlayerDiv.style.left = (aBenchStartX + i * ((aBenchEndX - aBenchStartX) / benchPlayerCount)) + 'px';
	}
	
	//away skaters image and default position in the bench
	document.querySelectorAll('.skater-away').forEach((skater) => {
		let playerImg = document.createElement('img');
		playerImg.onerror = function() {this.src = jerseySrc + 'jersey-default-away.png'; }
		playerImg.src = jerseySrc + 'jersey-' + awayAbr + '-away.png';
		skater.appendChild(playerImg);
	});
	
	//away goalies
	document.querySelectorAll('.goalie-away').forEach(goalie => {
		let playerImg = document.createElement('img');
		playerImg.classList.add('goalie-jersey-img');
		playerImg.onerror = function() { this.src = jerseySrc + 'jersey-default-away.png'; }
		playerImg.src = jerseySrc + 'jersey-' + awayAbr + '-away.png';
		let helmetImg = document.createElement('img');
		helmetImg.classList.add('goalie-helmet-img');
		helmetImg.src = jerseySrc + 'goalie-helmet.png';
		goalie.appendChild(playerImg);
		goalie.appendChild(helmetImg);
		
		goalie.style.top = benchY - 12.5 + 'px';
		goalie.style.left = aBenchEndX + 'px';
	});
}

function setFaceoffStartCoords(penaltyBox, eventCoords, zone) {
	let homeBoxPlayers = 0;
	let awayBoxPlayers = 0;
	for(let i = 0; i < penaltyBox.home.length; i++) {
		if(penaltyBox.home[i].penaltyType === 'NORMAL' || penaltyBox.home[i].penaltyType === 'MAJOR')
			homeBoxPlayers++;
	}
	for(let i = 0; i < penaltyBox.away.length; i++) {
		if(penaltyBox.away[i].penaltyType === 'NORMAL' ||  penaltyBox.away[i].penaltyType === 'MAJOR')
			awayBoxPlayers++;
	}
		
	let bulyOffset = {
		'centerX': 50,
		'wingX': 10,
		'wingY': 50,
		'dCenterX': 65,
		'dCenterY': 30,
		'dDZoneX': 40,
		'dDZoneY': 40,
		'linerefY': 25,
		'mainrefX': 100,
		'mainrefY': 75,
		'mainrefDZoneX': 60,
		'mainrefDZoneY': 130
	};
	
	let bulyCoords = getBulyCoords(eventCoords.x, eventCoords.y);
	
	if(homeBoxPlayers === 0) {
		objectCoordsMap['skater-lw-home'] = { 'x': bulyCoords.x - bulyOffset.wingX, 'y': bulyCoords.y - bulyOffset.wingY };
		objectCoordsMap['skater-rw-home'] = { 'x': bulyCoords.x - bulyOffset.wingX, 'y': bulyCoords.y + bulyOffset.wingY };
	} else if (homeBoxPlayers === 1) {
		objectCoordsMap['skater-lw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-home'] = { 'x': bulyCoords.x - bulyOffset.wingX, 'y': bulyCoords.y + bulyOffset.wingY };
	} else if(homeBoxPlayers > 1) {
		objectCoordsMap['skater-lw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
	}
	
	if(awayBoxPlayers === 0) {
		objectCoordsMap['skater-lw-away'] = { 'x': bulyCoords.x + bulyOffset.wingX, 'y': bulyCoords.y + bulyOffset.wingY };
		objectCoordsMap['skater-rw-away'] = { 'x': bulyCoords.x + bulyOffset.wingX, 'y': bulyCoords.y - bulyOffset.wingY };
	} else if (awayBoxPlayers === 1) {
		objectCoordsMap['skater-lw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-away'] = { 'x': bulyCoords.x + bulyOffset.wingX, 'y': bulyCoords.y - bulyOffset.wingY };
	} else if(awayBoxPlayers > 1) {
		objectCoordsMap['skater-lw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
	}
	
	objectCoordsMap['skater-c-home'] = { 'x': bulyCoords.x - bulyOffset.centerX, 'y': bulyCoords.y };
	objectCoordsMap['skater-c-away'] = { 'x': bulyCoords.x + bulyOffset.centerX, 'y': bulyCoords.y };

	if(eventCoords.x < (pitchCenter.x + 20)) { //left
		objectCoordsMap['lineref-home'] = { 'x': bulyCoords.x, 'y': bulyCoords.y - bulyOffset.linerefY }
	} else {
		objectCoordsMap['lineref-away'] = { 'x': bulyCoords.x, 'y': bulyCoords.y - bulyOffset.linerefY }
	}

	if(zone === 'center') {
		objectCoordsMap['skater-d-top-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
		objectCoordsMap['skater-d-bot-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
		objectCoordsMap['skater-d-top-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
		objectCoordsMap['skater-d-bot-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
		
		if(eventCoords.y < (pitchCenter.y + 20)) {
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefX, 'y': bulyCoords.y + bulyOffset.mainrefY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefX, 'y': bulyCoords.y + bulyOffset.mainrefY};
		} else {
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefX, 'y': bulyCoords.y - bulyOffset.mainrefY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefX, 'y': bulyCoords.y - bulyOffset.mainrefY};
		}
		
	} else if(zone === 'left') {
		if (eventCoords.y < (pitchCenter.y - 20)) {
			objectCoordsMap['skater-d-top-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y };
			objectCoordsMap['skater-d-bot-home'] = { 'x': bulyCoords.x - bulyOffset.dDZoneX, 'y': bulyCoords.y + bulyOffset.dDZoneY };
			objectCoordsMap['skater-d-top-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
			objectCoordsMap['skater-d-bot-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
			
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefDZoneX, 'y': bulyCoords.y + bulyOffset.mainrefDZoneY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefX * 2, 'y': bulyCoords.y + bulyOffset.mainrefY};
		} else {
			objectCoordsMap['skater-d-top-home'] = { 'x': bulyCoords.x - bulyOffset.dDZoneX, 'y': bulyCoords.y - bulyOffset.dDZoneY };
			objectCoordsMap['skater-d-bot-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y };
			objectCoordsMap['skater-d-top-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
			objectCoordsMap['skater-d-bot-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
			
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefDZoneX, 'y': bulyCoords.y - bulyOffset.mainrefDZoneY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefX * 2, 'y': bulyCoords.y - bulyOffset.mainrefY};
		}
	} else if(zone === 'right') {
		if (eventCoords.y < (pitchCenter.y - 20)) {	//up
			objectCoordsMap['skater-d-top-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
			objectCoordsMap['skater-d-bot-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
			objectCoordsMap['skater-d-top-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y };
			objectCoordsMap['skater-d-bot-away'] = { 'x': bulyCoords.x + bulyOffset.dDZoneX, 'y': bulyCoords.y + bulyOffset.dDZoneY };
			
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefX * 2, 'y': bulyCoords.y + bulyOffset.mainrefY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefDZoneX, 'y': bulyCoords.y + bulyOffset.mainrefDZoneY};
		} else {
			objectCoordsMap['skater-d-top-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y - bulyOffset.dCenterY };
			objectCoordsMap['skater-d-bot-home'] = { 'x': bulyCoords.x - bulyOffset.dCenterX, 'y': bulyCoords.y + bulyOffset.dCenterY };
			objectCoordsMap['skater-d-top-away'] = { 'x': bulyCoords.x + bulyOffset.dDZoneX, 'y': bulyCoords.y - bulyOffset.dDZoneY };
			objectCoordsMap['skater-d-bot-away'] = { 'x': bulyCoords.x + bulyOffset.dCenterX, 'y': bulyCoords.y };
			
			objectCoordsMap['mainref-home'] = { 'x': bulyCoords.x - bulyOffset.mainrefX * 2, 'y': bulyCoords.y - bulyOffset.mainrefY};
			objectCoordsMap['mainref-away'] = { 'x': bulyCoords.x + bulyOffset.mainrefDZoneX, 'y': bulyCoords.y - bulyOffset.mainrefDZoneY};
		}
	}
}

function setDefaultEventStartCoords(penaltyBox, eventCoords, zone) {
	let yAxisOffset = eventCoords.y / pitchCenter.y;
	
	let defaultCoords = {
		'defRightTop': {'x': 600,'y': 173},
		'defRightBot': {'x': 600,'y': 270},
		'defLeftTop': {'x': 120,'y': 173},
		'defLeftBot': {'x': 120,'y': 270},
		'defOffset': {'x': 175, 'y': 100},
		'wingOffset': {'x': 80, 'y': 80},
		'mainrefCloseOffset': {'x': 100, 'y': 0},
		'mainrefFarOffset': {'x': 200, 'y': 0}
	};
	
	let homeBoxPlayers = 0;
	let awayBoxPlayers = 0;
	for(let i = 0; i < penaltyBox.home.length; i++) {
		if(penaltyBox.home[i].penaltyType === 'NORMAL' || penaltyBox.home[i].penaltyType === 'MAJOR')
			homeBoxPlayers++;
	}
	for(let i = 0; i < penaltyBox.away.length; i++) {
		if(penaltyBox.away[i].penaltyType === 'NORMAL' ||  penaltyBox.away[i].penaltyType === 'MAJOR')
			awayBoxPlayers++;
	}
	
	if(homeBoxPlayers === 0) {
		objectCoordsMap['skater-lw-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.wingOffset.x, 'y': eventCoords.y - defaultCoords.wingOffset.y}, '');
		objectCoordsMap['skater-rw-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.wingOffset.x, 'y': eventCoords.y + defaultCoords.wingOffset.y}, '');
	} else if (homeBoxPlayers === 1) {
		objectCoordsMap['skater-lw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.wingOffset.x, 'y': eventCoords.y + defaultCoords.wingOffset.y}, '');
	} else if(homeBoxPlayers > 1) {
		objectCoordsMap['skater-lw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-home'] = { 'x': homePenaltyBoxX, 'y': penaltyBoxY };
	}
	
	if(awayBoxPlayers === 0) {
		objectCoordsMap['skater-lw-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.wingOffset.x, 'y': eventCoords.y + defaultCoords.wingOffset.y}, '');
		objectCoordsMap['skater-rw-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.wingOffset.x, 'y': eventCoords.y - defaultCoords.wingOffset.y}, '');
	} else if (awayBoxPlayers === 1) {
		objectCoordsMap['skater-lw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.wingOffset.x, 'y': eventCoords.y - defaultCoords.wingOffset.y}, '');
	} else if(awayBoxPlayers > 1) {
		objectCoordsMap['skater-lw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
		objectCoordsMap['skater-rw-away'] = { 'x': awayPenaltyBoxX, 'y': penaltyBoxY };
	}
	
	objectCoordsMap['lineref-home'] = lineRefHomeDefPos;
	objectCoordsMap['lineref-away'] = lineRefAwayDefPos;
	
	objectCoordsMap['skater-c-home'] = checkConflict(eventCoords, {'x': eventCoords.x - 20 - playerWidth / 2, 'y': eventCoords.y - playerHeight / 2}, 'center');
	objectCoordsMap['skater-c-away'] = checkConflict(eventCoords, {'x': eventCoords.x + 20 - playerWidth / 2, 'y': eventCoords.y - playerHeight / 2}, 'center');
	
	if(zone === 'center') {
		objectCoordsMap['mainref-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.mainrefFarOffset.x, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['mainref-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.mainrefFarOffset.x, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['skater-d-top-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.defOffset.x, 'y': eventCoords.y - defaultCoords.defOffset.y}, '');
		objectCoordsMap['skater-d-bot-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.defOffset.x, 'y': eventCoords.y + defaultCoords.defOffset.y}, '');
		
		objectCoordsMap['skater-d-top-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.defOffset.x, 'y': (eventCoords.y - defaultCoords.defOffset.y)}, '');
		objectCoordsMap['skater-d-bot-away'] = checkConflict(eventCoords,{'x': eventCoords.x + defaultCoords.defOffset.x, 'y': (eventCoords.y + defaultCoords.defOffset.y)}, '');
	} else if( zone === 'right') {
		objectCoordsMap['mainref-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.mainrefFarOffset.x, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['mainref-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.mainrefCloseOffset, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['skater-d-top-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.defOffset.x, 'y': eventCoords.y - defaultCoords.defOffset.y}, '');
		objectCoordsMap['skater-d-bot-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.defOffset.x, 'y': eventCoords.y + defaultCoords.defOffset.y}, '');
		
		objectCoordsMap['skater-d-top-away'] = checkConflict(eventCoords, {'x': defaultCoords.defRightTop.x, 'y': defaultCoords.defRightTop.y * (yAxisOffset / 1.3)}, '');
		objectCoordsMap['skater-d-bot-away'] = checkConflict(eventCoords,{'x': defaultCoords.defRightBot.x, 'y': defaultCoords.defRightBot.y * (yAxisOffset / 1.3)}, '');
	} else if(zone === 'left') {
		objectCoordsMap['mainref-home'] = checkConflict(eventCoords, {'x': eventCoords.x - defaultCoords.mainrefCloseOffset.x, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['mainref-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.mainrefFarOffset.x, 'y': pitchCenter.y * yAxisOffset}, '');
		objectCoordsMap['skater-d-top-home'] = checkConflict(eventCoords, {'x': defaultCoords.defLeftTop.x, 'y': defaultCoords.defLeftTop.y * (yAxisOffset / 1.3)}, '');
		objectCoordsMap['skater-d-bot-home'] = checkConflict(eventCoords, {'x': defaultCoords.defLeftBot.x, 'y': defaultCoords.defLeftBot.y * (yAxisOffset / 1.3)}, '');
		
		objectCoordsMap['skater-d-top-away'] = checkConflict(eventCoords, {'x': eventCoords.x + defaultCoords.defOffset.x, 'y': (eventCoords.y - defaultCoords.defOffset.y)}, '');
		objectCoordsMap['skater-d-bot-away'] = checkConflict(eventCoords,{'x': eventCoords.x + defaultCoords.defOffset.x, 'y': (eventCoords.y + defaultCoords.defOffset.y)}, '');
	}
}

function animateEvent(animationTime, eventCoords, event) {
	let eventName = event.name;
	let eventActor = event.actedBy;
	
	let faceoffXOffset = 30;
	let faceoffXPuckOffset = 50;
	let hitXPuckOffset = 25;
	let giveawayPlayerXOffset = 20;
	let giveawayPuckXOffset = 120;
	
	let netTopY = 200 - puckHeight / 2;
	let netBotY = 240 + puckHeight / 2;
	let yAxisDelta = pitchCenter.y - eventCoords.y;
	
	let centerHome = document.getElementById('skater-c-home');
	let centerAway = document.getElementById('skater-c-away');
	let goalieHome = document.getElementById('goalie-home');
	let goalieAway = document.getElementById('goalie-away');
	let puckEl = document.getElementById('puck');
	let puckCoord = objectCoordsMap['puck'];
	
	if(eventName === 'Faceoff') {
		objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + faceoffXOffset, 'y': readPositionNumber(centerHome.style.top)};
		objectCoordsMap['skater-c-away'] = { 'x': readPositionNumber(centerAway.style.left) - faceoffXOffset, 'y': readPositionNumber(centerAway.style.top)};
		
		move(centerHome, objectCoordsMap[centerHome.id], animationTime);
		move(centerAway, objectCoordsMap[centerAway.id], animationTime);
		
		window.setTimeout(() => {
			changeEventDescription('won by ' + fullNameOf(event.mainActor), true);
			if(eventActor === 'HOME') {
				objectCoordsMap['puck'] = {'x': puckCoord.x - faceoffXPuckOffset, 'y': puckCoord.y};
			} else {
				objectCoordsMap['puck'] = {'x': puckCoord.x + faceoffXPuckOffset, 'y': puckCoord.y};
			}
			puckEl.style.display = 'block';
			move(puckEl, objectCoordsMap['puck'], animationTime);
		}, animationTime)
	} else if(eventName === 'Hit') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + hitXPuckOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription(' gets smashed by ' + fullNameOf(event.mainActor), true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x - hitXPuckOffset, 'y': puckCoord.y}, 'puck');
				objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) + hitXPuckOffset, 'y': readPositionNumber(centerHome.style.top)};
				centerAway.style.transform = 'rotate(90deg)';
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				move(centerAway, objectCoordsMap[centerAway.id], animationTime);
				
				window.setTimeout(() => {
					centerAway.style.transform = 'rotate(0deg)';
				}, animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - hitXPuckOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription(' gets smashed by ' + fullNameOf(event.mainActor), true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x + hitXPuckOffset, 'y': puckCoord.y}, 'puck');
				objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) - hitXPuckOffset, 'y': readPositionNumber(centerHome.style.top)};
				centerHome.style.transform = 'rotate(-90deg)';
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				move(centerHome, objectCoordsMap[centerHome.id], animationTime);
				
				window.setTimeout(() => {
					centerHome.style.transform = 'rotate(0deg)';
				}, animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Giveaway') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['puck'] = {'x': puckCoord.x + giveawayPlayerXOffset, 'y': puckCoord.y};
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('gave away the puck!', true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x + giveawayPuckXOffset, 'y': puckCoord.y}, 'puck');
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['puck'] = {'x': puckCoord.x - giveawayPlayerXOffset, 'y': puckCoord.y};
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('gave away the puck!', true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x - giveawayPuckXOffset, 'y': puckCoord.y}, 'puck');
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Takeaway') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('took away the puck!', true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x - giveawayPuckXOffset, 'y': puckCoord.y}, 'puck');
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('took away the puck!', true);
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x + giveawayPuckXOffset, 'y': puckCoord.y}, 'puck');
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Shot On Goal') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('saved by ' + fullNameOf(event.secondaryActors[0]), true);
				objectCoordsMap['puck'] = {'x': readPositionNumber(goalieAway.style.left) - puckWidth / 2, 'y': readPositionNumber(goalieAway.style.top) + puckHeight / 2};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('saved by ' + fullNameOf(event.secondaryActors[0]), true);
				objectCoordsMap['puck'] = {'x': readPositionNumber(goalieHome.style.left) + puckWidth / 2, 'y': readPositionNumber(goalieHome.style.top) + puckHeight / 2};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Missed Shot') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('missed the net!', true);
				let newCoordY = 0;
				if(yAxisDelta >= 0) {
					newCoordY = netTopY - yAxisDelta / 6;
				} else {
					newCoordY = netBotY + yAxisDelta / 6;
				}
				objectCoordsMap['puck'] = {'x': readPositionNumber(goalieAway.style.left) + playerWidth, 'y': newCoordY};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('missed the net!', true);
				let newCoordY = 0;
				if(yAxisDelta >= 0) {
					newCoordY = netTopY - yAxisDelta / 6;
				} else {
					newCoordY = netBotY + yAxisDelta / 6;
				}
				objectCoordsMap['puck'] = {'x': readPositionNumber(goalieHome.style.left) - playerWidth, 'y': newCoordY};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Blocked Shot') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = {'x': readPositionNumber(centerHome.style.left), 'y': readPositionNumber(centerHome.style.top)};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				
				window.setTimeout(() => {
					changeEventDescription('blocked by ' + fullNameOf(event.mainActor), true);
					objectCoordsMap['puck'] = {'x': readPositionNumber(goalieHome.style.left) - playerWidth, 'y': yAxisDelta >= 0 ? netTopY - 75 : netBotY + 75};
					move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				}, animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = {'x': readPositionNumber(centerAway.style.left), 'y': readPositionNumber(centerAway.style.top)};
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				
				window.setTimeout(() => {
					changeEventDescription('blocked by ' + fullNameOf(event.mainActor), true);
					objectCoordsMap['puck'] = {'x': readPositionNumber(goalieAway.style.left) + playerWidth, 'y': yAxisDelta >= 0 ? netTopY - 75 : netBotY + 75};
					move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				}, animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Penalty') {
		puckEl.style.display = 'block';
		if(eventActor === 'HOME') {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + 2 * giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x + hitXPuckOffset * 2, 'y': puckCoord.y}, 'puck');
				objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) + hitXPuckOffset, 'y': readPositionNumber(centerAway.style.top)};
				centerAway.style.transform = 'rotate(135deg)';
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				move(centerAway, objectCoordsMap[centerAway.id], animationTime);
				
				window.setTimeout(() => {
					let referee;
					let xOffset;
					if(eventCoords.x <= pitchCenter.x) {
						referee = document.getElementById('mainref-home');
						xOffset = giveawayPlayerXOffset / 3;
					} else {
						referee = document.getElementById('mainref-away');
						xOffset = -1 * giveawayPlayerXOffset / 3;
					}
					
					objectCoordsMap[referee.id] = {'x': readPositionNumber(centerHome.style.left) + xOffset, 'y': readPositionNumber(centerHome.style.top) - 10};
					move(referee, objectCoordsMap[referee.id], animationTime);
					
					window.setTimeout(() => {
						centerAway.style.transform = 'rotate(0deg)';
						objectCoordsMap['skater-c-home'] = {'x': homePenaltyBoxX, 'y': penaltyBoxY};
						objectCoordsMap[referee.id] = {'x': homePenaltyBoxX + xOffset, 'y': penaltyBoxY - 25};
						move(centerHome, objectCoordsMap[centerHome.id], animationTime);
						move(referee, objectCoordsMap[referee.id], animationTime);
					}, animationTime);
				}, animationTime);
			}, animationTime);
		} else {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - 2 * giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = checkConflict(eventCoords, {'x': puckCoord.x - hitXPuckOffset * 2, 'y': puckCoord.y}, 'puck');
				objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) - hitXPuckOffset, 'y': readPositionNumber(centerHome.style.top)};
				centerHome.style.transform = 'rotate(135deg)';
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				move(centerHome, objectCoordsMap[centerHome.id], animationTime);
				
				window.setTimeout(() => {
					let referee;
					let xOffset;
					if(eventCoords.x <= pitchCenter.x) {
						referee = document.getElementById('mainref-home');
						xOffset = giveawayPlayerXOffset / 3;
					} else {
						referee = document.getElementById('mainref-away');
						xOffset = -1 * giveawayPlayerXOffset / 3;
					}
					
					objectCoordsMap[referee.id] = {'x': readPositionNumber(centerAway.style.left) + xOffset, 'y': readPositionNumber(centerAway.style.top) - 10};
					move(referee, objectCoordsMap[referee.id], animationTime);
					
					window.setTimeout(() => {
						centerHome.style.transform = 'rotate(0deg)';
						objectCoordsMap['skater-c-away'] = {'x': awayPenaltyBoxX, 'y': penaltyBoxY};
						objectCoordsMap[referee.id] = {'x': awayPenaltyBoxX + xOffset, 'y': penaltyBoxY - 25};
						move(centerAway, objectCoordsMap[centerAway.id], animationTime);
						move(referee, objectCoordsMap[referee.id], animationTime);
					}, animationTime);
				}, animationTime);
			}, animationTime);
		}
	} else if(eventName === 'Goal') {
		puckEl.style.display = 'block';
		let goalAssists = assistsCount(event);
		
		if(eventActor === 'HOME' && goalAssists === 0) {
			objectCoordsMap['skater-c-home'] = {'x': readPositionNumber(centerHome.style.left) + giveawayPlayerXOffset, 'y': readPositionNumber(centerHome.style.top)};
			move(centerHome, objectCoordsMap[centerHome.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
				homeScoringAnimation(animationTime);
			}, animationTime);
		} else if(eventActor === 'HOME' && goalAssists === 1) {
			objectCoordsMap['puck'] = objectCoordsMap['skater-c-home'];
			objectCoordsMap['puck'].x += puckWidth / 2;
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
				homeScoringAnimation(animationTime);
			}, animationTime);
		} else if(eventActor === 'HOME' && goalAssists > 1) {
			objectCoordsMap['puck'] = objectCoordsMap['skater-d-top-home'];
			objectCoordsMap['puck'].x += puckWidth / 2;
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = objectCoordsMap['skater-c-home'];
				objectCoordsMap['puck'].x += puckWidth / 2;
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				window.setTimeout(() => {
					changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
					homeScoringAnimation(animationTime);
				}, animationTime);
			}, animationTime);
		} else if(eventActor === 'AWAY' && goalAssists === 0) {
			objectCoordsMap['skater-c-away'] = {'x': readPositionNumber(centerAway.style.left) - giveawayPlayerXOffset, 'y': readPositionNumber(centerAway.style.top)};
			move(centerAway, objectCoordsMap[centerAway.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
				awayScoringAnimation(animationTime);
			}, animationTime);
		} else if(eventActor === 'AWAY' && goalAssists === 1) {
			objectCoordsMap['puck'] = objectCoordsMap['skater-c-away'];
			objectCoordsMap['puck'].x -= puckWidth / 2;
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			
			window.setTimeout(() => {
				changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
				awayScoringAnimation(animationTime);
			}, animationTime);
		} else if(eventActor === 'AWAY' && goalAssists > 1) {
			objectCoordsMap['puck'] = objectCoordsMap['skater-d-top-away'];
			objectCoordsMap['puck'].x -= puckWidth / 2;
			move(puckEl, objectCoordsMap[puckEl.id], animationTime);
			
			window.setTimeout(() => {
				objectCoordsMap['puck'] = objectCoordsMap['skater-c-away'];
				objectCoordsMap['puck'].x -= puckWidth / 2;
				move(puckEl, objectCoordsMap[puckEl.id], animationTime);
				window.setTimeout(() => {
					changeEventDescription('Goal!!! scored by ' + fullNameOf(event.mainActor), false);
					awayScoringAnimation(animationTime);
				}, animationTime);
			}, animationTime);
		}
	}
}

function homeScoringAnimation(animationTime) {
	let goalieAway = document.getElementById('goalie-away');
	let puckEl = document.getElementById('puck');
	let centerAway = document.getElementById('skater-c-away');
	
	objectCoordsMap['puck'] = {'x': readPositionNumber(goalieAway.style.left) + puckWidth / 2, 'y': readPositionNumber(goalieAway.style.top) + puckHeight / 2};
	move(puckEl, objectCoordsMap[puckEl.id], animationTime);
	window.setTimeout(() => {
		goalieAway.style.transform = 'rotate(90deg)';
		centerAway.style.transform = 'rotate(90deg)';
		document.getElementById('skater-d-top-away').style.transform = 'rotate(90deg)';
		document.getElementById('skater-d-bot-away').style.transform = 'rotate(90deg)';
		document.getElementById('skater-lw-away').style.transform = 'rotate(90deg)';
		document.getElementById('skater-rw-away').style.transform = 'rotate(90deg)';
		
		objectCoordsMap['skater-c-home'] = {'x': goalCelebration.center.x - goalCelebration.offset.x, 'y': goalCelebration.center.y};
		objectCoordsMap['skater-lw-home'] = {'x': goalCelebration.lw.x - goalCelebration.offset.x, 'y': goalCelebration.lw.y};
		objectCoordsMap['skater-rw-home'] = {'x': goalCelebration.rw.x - goalCelebration.offset.x, 'y': goalCelebration.rw.y};
		objectCoordsMap['skater-d-top-home'] = {'x': goalCelebration.dTop.x - goalCelebration.offset.x, 'y': goalCelebration.dTop.y};
		objectCoordsMap['skater-d-bot-home'] = {'x': goalCelebration.dBot.x - goalCelebration.offset.x, 'y': goalCelebration.dBot.y};
		
		move(document.getElementById('skater-c-home'), objectCoordsMap['skater-c-home'], animationTime);
		move(document.getElementById('skater-lw-home'), objectCoordsMap['skater-lw-home'], animationTime);
		move(document.getElementById('skater-rw-home'), objectCoordsMap['skater-rw-home'], animationTime);
		move(document.getElementById('skater-d-top-home'), objectCoordsMap['skater-d-top-home'], animationTime);
		move(document.getElementById('skater-d-bot-home'), objectCoordsMap['skater-d-bot-home'], animationTime);
					
		window.setTimeout(() => {
			goalieAway.style.transform = 'rotate(0deg)';
			centerAway.style.transform = 'rotate(0deg)';
			document.getElementById('skater-d-top-away').style.transform = 'rotate(0deg)';
			document.getElementById('skater-d-bot-away').style.transform = 'rotate(0deg)';
			document.getElementById('skater-lw-away').style.transform = 'rotate(0deg)';
			document.getElementById('skater-rw-away').style.transform = 'rotate(0deg)';
			
			document.querySelectorAll('.skater-away').forEach((skater) => {
				objectCoordsMap[skater.id] = getRandomBenchCoord('away');
				move(skater, objectCoordsMap[skater.id], animationTime);
			});
			objectCoordsMap['goalie-away'] = goalieAwayBenchPos;
			move(goalieAway, objectCoordsMap[goalieAway.id], animationTime);
			
		}, animationTime);
	}, animationTime);
}

function awayScoringAnimation(animationTime) {
	let goalieHome = document.getElementById('goalie-home');
	let puckEl = document.getElementById('puck');
	let centerHome = document.getElementById('skater-c-home');
	
	objectCoordsMap['puck'] = {'x': readPositionNumber(goalieHome.style.left) - puckWidth / 2, 'y': readPositionNumber(goalieHome.style.top) + puckHeight / 2};
	move(puckEl, objectCoordsMap[puckEl.id], animationTime);
	window.setTimeout(() => {
		goalieHome.style.transform = 'rotate(270deg)';
		centerHome.style.transform = 'rotate(270deg)';
		document.getElementById('skater-d-top-home').style.transform = 'rotate(270deg)';
		document.getElementById('skater-d-bot-home').style.transform = 'rotate(270deg)';
		document.getElementById('skater-lw-home').style.transform = 'rotate(270deg)';
		document.getElementById('skater-rw-home').style.transform = 'rotate(270deg)';
		
		objectCoordsMap['skater-c-away'] = {'x': goalCelebration.center.x + goalCelebration.offset.x, 'y': goalCelebration.center.y};
		objectCoordsMap['skater-lw-away'] = {'x': goalCelebration.lw.x + goalCelebration.offset.x, 'y': goalCelebration.lw.y};
		objectCoordsMap['skater-rw-away'] = {'x': goalCelebration.rw.x + goalCelebration.offset.x, 'y': goalCelebration.rw.y};
		objectCoordsMap['skater-d-top-away'] = {'x': goalCelebration.dTop.x + goalCelebration.offset.x, 'y': goalCelebration.dTop.y};
		objectCoordsMap['skater-d-bot-away'] = {'x': goalCelebration.dBot.x + goalCelebration.offset.x, 'y': goalCelebration.dBot.y};
		
		move(document.getElementById('skater-c-away'), objectCoordsMap['skater-c-away'], animationTime);
		move(document.getElementById('skater-lw-away'), objectCoordsMap['skater-lw-away'], animationTime);
		move(document.getElementById('skater-rw-away'), objectCoordsMap['skater-rw-away'], animationTime);
		move(document.getElementById('skater-d-top-away'), objectCoordsMap['skater-d-top-away'], animationTime);
		move(document.getElementById('skater-d-bot-away'), objectCoordsMap['skater-d-bot-away'], animationTime);
		
		window.setTimeout(() => {
			goalieHome.style.transform = 'rotate(0deg)';
			centerHome.style.transform = 'rotate(0deg)';
			document.getElementById('skater-d-top-home').style.transform = 'rotate(0deg)';
			document.getElementById('skater-d-bot-home').style.transform = 'rotate(0deg)';
			document.getElementById('skater-lw-home').style.transform = 'rotate(0deg)';
			document.getElementById('skater-rw-home').style.transform = 'rotate(0deg)';
			
			document.querySelectorAll('.skater-home').forEach((skater) => {
				objectCoordsMap[skater.id] = getRandomBenchCoord('home');
				move(skater, objectCoordsMap[skater.id], animationTime);
			});
			
			objectCoordsMap['goalie-home'] = goalieHomeBenchPos;
			move(goalieHome, objectCoordsMap[goalieHome.id], animationTime);
			
		}, animationTime);
	}, animationTime);
}

function checkConflict(eventCoord, skaterCoord, type) {
	let margin = 40;
	let pitchYStart = 75;
	let pitchYEnd = 375;
	let pitchXStart = 60;
	let pitchXEnd = 670;
	
	let outOfBoundsXOffset = 5;
	let outOfBoundsYOffset = 10;
	
	if(type === 'center') {
		return skaterCoord;
	}
	
	//skater out of bounds of pitch
	if(skaterCoord.y < pitchYStart) { //too high
		if(skaterCoord.x > pitchCenter.x) { //right side
			return checkConflict(eventCoord, {'x': (skaterCoord.x + outOfBoundsXOffset), 'y': (skaterCoord.y + outOfBoundsYOffset)});
		} else {
			return checkConflict(eventCoord, {'x': (skaterCoord.x - outOfBoundsXOffset), 'y': (skaterCoord.y + outOfBoundsYOffset)});
		}
	} else if(skaterCoord.y > pitchYEnd) { //too low
		if(skaterCoord.x > pitchCenter.x) { //right side
			return checkConflict(eventCoord, {'x': (skaterCoord.x + outOfBoundsXOffset), 'y': (skaterCoord.y - outOfBoundsYOffset)});
		} else {
			return checkConflict(eventCoord, {'x': (skaterCoord.x - outOfBoundsXOffset), 'y': (skaterCoord.y - outOfBoundsYOffset)});
		}
	}
	
	if(skaterCoord.x < pitchXStart) { // too left
		return checkConflict(eventCoord, {'x': skaterCoord.x + outOfBoundsXOffset, 'y': skaterCoord.y});
	} else if(skaterCoord.x > pitchXEnd) { //too right
		return checkConflict(eventCoord, {'x': skaterCoord.x - outOfBoundsXOffset, 'y': skaterCoord.y});
	}
	
	if(type === 'puck') {
		return skaterCoord;
	}
	
	//moving away skater, who is in the way of event center
	if((eventCoord.x + margin > skaterCoord.x && eventCoord.x - margin < skaterCoord.x) &&
			(eventCoord.y + margin > skaterCoord.y && eventCoord.y - margin < skaterCoord.y)) {
		if (eventCoord.y < (pitchCenter.y - 20)) { // up
			return checkConflict(eventCoord, {'x': skaterCoord.x, 'y': skaterCoord.y + margin});
			//skaterCoord.y = skaterCoord.y + margin;
		} else {
			return checkConflict(eventCoord, {'x': skaterCoord.x, 'y': skaterCoord.y - margin});
			//skaterCoord.y = skaterCoord.y - margin;
		}
	}
	return skaterCoord;
}

function changeEventDescription(text, isAdded) {
	let description = '';
	if(isAdded)
		description = document.getElementById('game-event-title').textContent + ' - ';
	
	description += text;
	document.getElementById('game-event-title').textContent = description;
}

function fullNameOf(player) {
	return player.firstName + ' ' + player.lastName;
}

function getEventsZone(x) {
	const leftZoneX = 270;
	const rightZoneX = 455;
	if(x <= leftZoneX) {
		return 'left';
	} else if(x >= rightZoneX) {
		return 'right';
	} else {
		return 'center';
	}
}

function getBulyCoords(evX, evY) {
	const bulyXCoordsArr = [362.5, 141.5, 298.5, 583.5, 432];
	const bulyYCoordsArr = [221.5, 146, 296];
	let margin = 30;
	let bulyX, bulyY;
	
	bulyXCoordsArr.forEach(x => {
		if(x + margin > evX && x - margin < evX) {
			bulyX = x;
		}
	});
	bulyYCoordsArr.forEach(y => {
		if(y + margin > evY && y - margin < evY) {
			bulyY = y;
		}
	});
	return {'x': bulyX,'y': bulyY};
}

function getRandomBenchCoord(team) {
	let benchPos = Number.parseInt(Math.random() * benchPlayerCount);
	if(team === 'home') {
		return {'x': (hBenchStartX + benchPos * ((hBenchEndX - hBenchStartX) / benchPlayerCount)), 'y': benchY};
	} else if(team === 'away') {
		return {'x': (aBenchStartX + benchPos * ((aBenchEndX - aBenchStartX) / benchPlayerCount)),'y': benchY}
	} else {
		console.log('wrong parameter in getRandomBenchCoord function :(');
		return null;
	}
}

function readPositionNumber(attribute) {
	let res = Number.parseFloat(attribute.substring(0, attribute.indexOf('px')));
	return res;
}

function translateCoord(x, y, objWidth, objHeight) {
	let centerX = pitchDivWidth / 2;
	let centerY = pitchDivHeight / 2;
	
	let offsetX = (((pitchWidth / 2) / 100) * x) + centerX - (objWidth / 2);
	let offsetY = (((pitchHeight / 2) / 50) * y) + centerY - (objHeight / 2);
	
	return {'x': offsetX, 'y': offsetY};
}

function assistsCount(event) {
	let count = 0;
	for(let i = 0; i < event.secondaryActors.length; i++) {
		if(event.secondaryActors[i].role === 'Assist')
			count++;
	}
	return count;
}

function timeToSeconds(period, timeStr) {
	return ((period - 1) * 20 * 60) + Number(timeStr.substr(0, 2) * 60) + Number(timeStr.substr(3, 2));
}

function timeToString(time) {
	let min = Math.trunc(time / 60);
	let sec = time % 60;
	let ret = '';
	
	if(min < 10) {
		ret = '0'
	}
	ret += min + ':';
	
	if(sec < 10) {
		ret += '0'
	}
	ret += sec;
	return ret;
}

function getCurrentGameTime() {
	let perTime = document.querySelectorAll('#game-header .result .game-date')[0].textContent;
	let perNum = document.querySelectorAll('#game-header .result .game-status')[0].textContent.substr(0, 1);
	return timeToSeconds(perNum, perTime);
}

/*
Drawing lines in statistics tab
*/
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