# NHLApp_v2

Dynamic web project showing data from Oracle db, that are loaded from api [NHLDataLoader2](https://github.com/Tomas-Mas/NHLDataLoader_v2/) to html pages.

## Technologies
- Java 8 <br>
- Oracle 19 <br>
- Maven <br>
- Spring Web MVC <br>
- JPA/Hibernate (HQL, JPA criteria API) <br>
- HTML, JSTL, CSS, Javascript <br>

## General description
User request are served by Spring controllers. <br>
Data are retrieved from db using HQL for simple queries, JPA criteria API for more complex queries. <br>
Html page is then filled by retrieved data and styled with CSS and Javascript.

## Pages

### Main page
![mainpage screenshot](readme-imgs/main-page-screen.png)
- sub-navigation (regulation/playoff and overall/home/away data) requests are handled by javascript, then whole stats container is overwritten by controller's response
![mainpage game detail screenshot](readme-imgs/mainpage-game-detail-screen.png)
- game details are loaded on click on game row by javascript into data row
### Stats page
![statspage screenshot](readme-imgs/statspage-screen.png)
![statspage sub navigation](readme-imgs/statspage-subnavigation-screen.png)
- all the sub navigation including pagination of player's stats is binded to single class through form
![statspage players pagination](readme-imgs/statspage-player-pagination-screen.png)
### Game page
#### Overview
![gamepage overview](readme-imgs/game-page-overview-screen.png)
### Player page
TODO
### Team Page
TODO