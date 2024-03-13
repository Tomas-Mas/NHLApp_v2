function menuClicked(event) {
	if(event.target.className === 'menulinks') {
		var button = event.target.textContent;
		if(button.includes('Home')) {
			window.location.href = '/NHL/mainpage/' + getActiveSeason();
		}
		else if(button.includes('Statistics')) {
			window.location.href = '/NHL/stats/' + getActiveSeason();
		}
	}
}

function getActiveSeason() {
	let index = document.getElementById("season").selectedIndex;
	let season = document.getElementById("season")[index].text;
	return season;
}