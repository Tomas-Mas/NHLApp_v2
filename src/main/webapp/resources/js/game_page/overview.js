document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
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
	if(e.button === 1){
		window.open("/NHL/game/" + game.id, "_blank");
	}
	if(e.button === 0 && !game.classList.contains("selected")) {
		window.location.href = "/NHL/game/" + game.id;
	}
}