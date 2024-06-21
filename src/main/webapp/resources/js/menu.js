const mainPageUrl = '/NHL/mainpage/';
const statsPageUrl = '/NHL/stats/';
const gamePageUrl = '/NHL/game/';
const teamPageUrl = '/NHL/team/';

const downArrStr = '\u2193';
const upArrStr = '\u2191';

function menuClicked(event) {
	if(event.target.className === 'menulinks') {
		var button = event.target.textContent;
		if(button.includes('Home')) {
			window.location.href = mainPageUrl + getActiveSeason();
		}
		else if(button.includes('Statistics')) {
			window.location.href = statsPageUrl + getActiveSeason();
		}
	}
}

function getActiveSeason() {
	let index = document.getElementById("season").selectedIndex;
	let season = document.getElementById("season")[index].text;
	return season;
}