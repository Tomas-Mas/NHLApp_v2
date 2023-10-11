CREATE TABLE Games
  (
    g_id       INTEGER NOT NULL ,
    g_jsonId   INTEGER NOT NULL ,
    gameType   VARCHAR2(5) NOT NULL ,
    season     INTEGER NOT NULL ,
    gameDate   Timestamp NOT NULL ,
    awayScore  INTEGER NOT NULL ,
    awayTeamId INTEGER NOT NULL ,
    homeScore  INTEGER NOT NULL ,
    homeTeamId INTEGER NOT NULL ,
    venueId    INTEGER NOT NULL ,
    gameStatus INTEGER NOT NULL
  );
ALTER TABLE Games ADD CONSTRAINT Games_PK PRIMARY KEY ( g_id );
ALTER TABLE Games ADD CONSTRAINT Games_JsonId UNIQUE ( g_jsonId );

CREATE TABLE GameStatus
  (
    code INTEGER NOT NULL,
    name VARCHAR2 (15)
  );
ALTER TABLE GameStatus ADD CONSTRAINT GameStatus_PK PRIMARY KEY ( code );

CREATE TABLE Teams
  (
    t_id         INTEGER NOT NULL ,
    t_jsonId     INTEGER NOT NULL ,
    name         VARCHAR2 (50) NOT NULL ,
    abbreviation VARCHAR2 (5) ,
    teamName     VARCHAR2 (15) NOT NULL ,
    shortName    VARCHAR2 (15) NOT NULL ,
    venueId      INTEGER NOT NULL ,
    timeZoneId   INTEGER NOT NULL ,
    location     VARCHAR2 (25) NOT NULL ,
    firstYear    INTEGER NOT NULL ,
    active       VARCHAR2 (5) NOT NULL
  );
ALTER TABLE Teams ADD CONSTRAINT Teams_PK PRIMARY KEY ( t_id );
ALTER TABLE Teams ADD CONSTRAINT Teams_JsonId UNIQUE ( t_jsonId );

CREATE TABLE Venues
  (
    v_id INTEGER NOT NULL ,
    name VARCHAR2 (50) NOT NULL ,
    city VARCHAR2 (25)
  );
ALTER TABLE Venues ADD CONSTRAINT Venues_PK PRIMARY KEY ( v_id );

CREATE TABLE TimeZones
  (
    tz_id  INTEGER NOT NULL ,
    name   VARCHAR2 (30) NOT NULL ,
    offset INTEGER NOT NULL
  );
ALTER TABLE TimeZones ADD CONSTRAINT TimeZones_PK PRIMARY KEY ( tz_id );

CREATE TABLE Divisions
  (
    d_id     INTEGER NOT NULL ,
    d_jsonId INTEGER NOT NULL ,
    name     VARCHAR2 (15) NOT NULL
  );
ALTER TABLE Divisions ADD CONSTRAINT Divisions_PK PRIMARY KEY ( d_id );
ALTER TABLE Divisions ADD CONSTRAINT Divisions_JsonId UNIQUE ( d_jsonId );

create table Division_Teams (
    division_id integer not null,
    team_id integer not null,
    season integer not null
);
alter table Division_Teams add constraint Division_Teams_PK primary key (division_id, team_id, season);

CREATE TABLE Conferences
  (
    c_id     INTEGER NOT NULL ,
    c_jsonId INTEGER NOT NULL ,
    name     VARCHAR2 (15) NOT NULL
  ) ;
ALTER TABLE Conferences ADD CONSTRAINT Conferences_PK PRIMARY KEY ( c_id );
ALTER TABLE Conferences ADD CONSTRAINT Conferences_JsonId UNIQUE ( c_jsonId );

create table Conference_Teams (
    conference_id integer not null,
    team_id integer not null,
    season integer not null
);
alter table conference_Teams add constraint Conference_Teams_PK primary key (conference_id, team_id, season);

CREATE TABLE Conference_Division
  (
    conference_id INTEGER NOT NULL ,
    division_id   INTEGER NOT NULL ,
    season integer not null
  ) ;
ALTER TABLE Conference_Division ADD CONSTRAINT Conference_Division_PK PRIMARY KEY ( conference_id, division_id, season );

create table Events(
    e_id integer not null,
    name varchar2(15),
    secondaryType varchar2(50),
    strength varchar2(20),
    emptyNet varchar2(10),
    penaltySeverity varchar2(30),
    penaltyMinutes integer
);
alter table Events add constraint Events_PK primary key(e_id);

create table GameEvents(
    ge_id integer not null,
    gameId integer not null,
    gameEventId integer not null,
    eventId integer not null,
    periodNumber integer not null,
    periodType varchar2(15) not null,
    periodTime varchar2(10) not null,
    coordX integer,
    coordY integer
);
alter table GameEvents add constraint GameEvents_PK primary key(ge_id);

CREATE TABLE Positions
  (
    p_id         INTEGER NOT NULL ,
    name         VARCHAR2 (15) NOT NULL ,
    type         VARCHAR2 (15) NOT NULL ,
    abbreviation VARCHAR2 (2)
  );
ALTER TABLE Positions ADD CONSTRAINT Positions_PK PRIMARY KEY ( p_id );
ALTER TABLE Positions ADD CONSTRAINT Conferences_Unique UNIQUE ( name, type );

CREATE TABLE Players
  (
    p_id          INTEGER NOT NULL ,
    p_jsonId      INTEGER NOT NULL ,
    firstName     VARCHAR2 (25) NOT NULL ,
    lastName      VARCHAR2 (25) NOT NULL ,
    primaryNumber INTEGER ,
    age           integer,
    birthDate     DATE ,
    birthCountry  VARCHAR2 (3) ,
    currentTeamId INTEGER ,
    positionId    INTEGER NOT NULL
  );
ALTER TABLE Players ADD CONSTRAINT Players_PK PRIMARY KEY ( p_id );
ALTER TABLE Players ADD CONSTRAINT Players_jsonId UNIQUE ( p_jsonId );

create table SkaterStats(
    ss_id integer not null,
    timeOnIce varchar2(10) not null,
    ppTimeOnIce varchar2(10) not null,
    shTimeOnIce varchar2(10) not null,
    penaltyMinutes integer not null,
    plusMinus integer not null
);
alter table SkaterStats add constraint SkaterStats_PK primary key(ss_id);

create table GoalieStats(
    gs_id integer not null,
    timeOnIce varchar2(10) not null,
    penaltyMinutes integer not null,
    shots integer not null,
    saves integer not null,
    ppShots integer not null,
    ppSaves integer not null,
    shShots integer not null,
    shSaves integer not null
);
alter table GoalieStats add constraint GoalieStats_PK primary key(gs_id);

create table Rosters(
    r_id integer not null,
    g_id integer not null,
    t_id integer not null,
    p_id integer not null,
    ss_id integer,
    gs_id integer
);
alter table Rosters add constraint Rosters_PK primary key(r_id);
alter table rosters add constraint Rosters_unique_gtp unique (g_id, t_id, p_id);
alter table rosters add constraint Rosters_unique_gp unique (g_id, p_id);

create table EventPlayers(
    event_id integer not null,
    roster_id integer not null,
    role varchar2(50)
);
alter table EventPlayers add constraint EventPlayers_PK primary key(event_id, roster_id);



ALTER TABLE Teams ADD CONSTRAINT Teams_Venues_FK FOREIGN KEY ( venueId ) REFERENCES Venues ( v_id );
ALTER TABLE Teams ADD CONSTRAINT Teams_TimeZones_FK FOREIGN KEY ( timeZoneId ) REFERENCES TimeZones ( tz_id );
ALTER TABLE Games ADD CONSTRAINT Games_Venues_FK FOREIGN KEY ( venueId ) REFERENCES Venues ( v_id );
ALTER TABLE Games ADD CONSTRAINT Games_GameStatus_FK FOREIGN KEY ( gameStatus ) REFERENCES GameStatus ( code );
ALTER TABLE Games ADD CONSTRAINT Games_Teams_FK_Away FOREIGN KEY ( awayTeamId ) REFERENCES Teams ( t_id );
ALTER TABLE Games ADD CONSTRAINT Games_Teams_FK_Home FOREIGN KEY ( homeTeamId ) REFERENCES Teams ( t_id );
ALTER TABLE Division_Teams ADD CONSTRAINT Division_Teams_Teams_FK FOREIGN KEY (team_id) REFERENCES Teams (t_id);
ALTER TABLE Division_Teams ADD CONSTRAINT Division_Teams_Divisions_FK FOREIGN KEY (division_id) REFERENCES Divisions (d_id);
ALTER TABLE Conference_Teams ADD CONSTRAINT Conference_Teams_Conferences_FK FOREIGN KEY (conference_id) REFERENCES Conferences (c_id);
ALTER TABLE Conference_Teams ADD CONSTRAINT Conference_Teams_Teams_FK FOREIGN KEY (team_id) REFERENCES Teams (t_id);
ALTER TABLE Conference_Division ADD CONSTRAINT Conference_Div_Con_FK FOREIGN KEY ( conference_id ) REFERENCES Conferences ( c_id );
ALTER TABLE Conference_Division ADD CONSTRAINT Con_Div_Division_FK FOREIGN KEY ( division_id ) REFERENCES Divisions ( d_id );
ALTER TABLE GameEvents ADD CONSTRAINT GameEvents_Games_FK FOREIGN KEY(gameId) REFERENCES Games(g_id);
ALTER TABLE GameEvents ADD CONSTRAINT GameEvents_Events_FK FOREIGN KEY(eventId) REFERENCES Events(e_id);
ALTER TABLE Players ADD CONSTRAINT Players_Positions_FK FOREIGN KEY ( positionId ) REFERENCES Positions ( p_id );
ALTER TABLE Players ADD CONSTRAINT Players_Teams_FK FOREIGN KEY ( currentTeamId ) REFERENCES Teams ( t_id );
ALTER TABLE Rosters ADD CONSTRAINT Rosters_Games_FK FOREIGN KEY (g_id) REFERENCES Games (g_id);
ALTER TABLE Rosters ADD CONSTRAINT Rosters_Teams_FK FOREIGN KEY (t_id) REFERENCES Teams (t_id);
ALTER TABLE Rosters ADD CONSTRAINT Rosters_Players_FK FOREIGN KEY (p_id) REFERENCES Players (p_id);
ALTER TABLE Rosters ADD CONSTRAINT Rosters_SkaterStats_FK FOREIGN KEY(ss_id) REFERENCES SkaterStats(ss_id);
ALTER TABLE Rosters ADD CONSTRAINT Rosters_GoalieStats_FK FOREIGN KEY(gs_id) REFERENCES GoalieStats(gs_id);
ALTER TABLE EventPlayers ADD CONSTRAINT EventPlayers_GameEvents_FK FOREIGN KEY(event_id) REFERENCES GameEvents (ge_id);
ALTER TABLE EventPlayers ADD CONSTRAINT EventPlayers_Rosters_FK FOREIGN KEY(roster_id) REFERENCES Rosters(r_id);




CREATE SEQUENCE seq_games_id
START WITH 200000
INCREMENT BY 1
MINVALUE 200000
NOMAXVALUE
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_teams_id
START WITH 100
INCREMENT BY 1
MINVALUE 100
MAXVALUE 499
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_venues_id
START WITH 500
INCREMENT BY 1
MINVALUE 500
MAXVALUE 999
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_timeZones_id
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 1000
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_divisions_id
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 29
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_conferences_id
START WITH 30
INCREMENT BY 1
MINVALUE 30
MAXVALUE 49
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_events_id
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 10000
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_gameEvents_id
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9999999999
CACHE 20
NOCYCLE;

/

CREATE SEQUENCE seq_positions_id
START WITH 100
INCREMENT BY 1
MINVALUE 100
MAXVALUE 1000
NOCACHE
NOCYCLE;

/

CREATE SEQUENCE seq_players_id
START WITH 1000
INCREMENT BY 1
MINVALUE 1000
MAXVALUE 999999
NOCACHE
NOCYCLE;

/

create sequence seq_rosters_id
start with 1
increment by 1
minvalue 1
maxvalue 9999999999
cache 5
nocycle;

/

create sequence seq_skaterStats_id
start with 1
increment by 1
minvalue 1
maxvalue 99999999999
cache 5
nocycle;

/

create sequence seq_goalieStats_id
start with 1
increment by 1
minvalue 1
maxvalue 9999999999
cache 2
nocycle;