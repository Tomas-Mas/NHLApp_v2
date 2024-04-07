const canvasHeight = 10;
const homeLineColor = "#000d1a";
const awayLineColor = "#cce6ff";

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
		drawStatLines();
	}
}

function setClickableEvents() {
	document.getElementById("season").disabled = true;
	
	document.getElementById('mainMenu').onclick = menuClicked;
	
	let gameList = document.getElementsByClassName("h2h-game");
	for(let i = 0; i < gameList.length; i++) {
		gameList[i].onmousedown = function(e) {
			gameClicked(e.target.closest("div.h2h-game"), e);
		}
	}
}

function gameClicked(game, e) {
	let link = window.location.href.substring(window.location.href.indexOf("gameStats"));
	
	if(e.button === 1){
		window.open("/NHL/game/" + game.id + "/" + link, "_blank");
	}
	if(e.button === 0 && !game.classList.contains("selected")) {
		window.location.href = "/NHL/game/" + game.id + "/" + link;
	}
}

function drawStatLines() {
	let lines = document.getElementsByClassName("stat-line");
	for(let i = 0; i < lines.length; i++) {
		let line = lines[i];
		let header = line.getElementsByClassName("stat-line-text")[0];
		let headerData = header.getElementsByTagName("div");
		
		let hData = 0;
		let aData = 0;
		if(headerData[0].innerText.indexOf("%") > 0) {
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
		
		let lineDiv = line.getElementsByClassName("stat-line-graphics")[0];
		let canvasWidth = lineDiv.clientWidth;
		let homeWidth = (hData / (hData + aData)) * canvasWidth;
		let awayWidth = (aData / (hData + aData)) * canvasWidth;
		
		let canvas = lineDiv.getElementsByTagName("canvas")[0];
		canvas.width = canvasWidth;
		canvas.height = canvasHeight;
		
		let context = canvas.getContext("2d");
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








