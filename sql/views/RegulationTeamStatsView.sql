create or replace view regulationTeamStats
as
select t.name as teamName, t.abbreviation as teamAbrName, regulationData.teamId as teamId, c.name as conference, d.name as division, regulationData.season, 
        regulationData.home_games, regulationData.home_goalsFor, regulationData.home_goalsAgainst, regulationData.home_points, 
        regulationData.home_regWins, regulationData.home_regLoses, regulationData.home_otWins, regulationData.home_otLoses,
        regulationData.away_games, regulationData.away_goalsFor, regulationData.away_goalsAgainst, regulationData.away_points, 
        regulationData.away_regWins, regulationData.away_regLoses, regulationData.away_otWins, regulationData.away_otLoses
from (
    select *
    from (
        select season, gameType, team, teamId, count(g_id) as gamesPlayed, sum(goalsFor) as goalsFor, sum(goalsAgainst) as goalsAgainst, sum(points) as points,
                sum(case when periods <= 3 and goalsFor > goalsAgainst then 1 else 0 end) as regulationWins,
                sum(case when periods <= 3 and goalsFor < goalsAgainst then 1 else 0 end) as regulationLoses,
                sum(case when periods > 3 and goalsFor > goalsAgainst then 1 else 0 end) as overtimeWins,
                sum(case when periods > 3 and goalsFor < goalsAgainst then 1 else 0 end) as overtimeLoses
        from (
            select *
            from (
                select g.season, g.g_id, g.gameType, homeT.t_id as homeTeam, awayT.t_id as awayTeam, myGames.periods, g.homeScore, g.awayScore,
                        case 
                            when homeScore > awayScore then 2
                            when homeScore < awayScore and periods <=3 then 0 else 1
                        end as homePoints,
                        case 
                            when awayScore > homeScore then 2
                            when awayScore < homeScore and periods <=3 then 0 else 1
                        end as awayPoints
                from (
                    select g.g_id, max(gEv.periodNumber) as periods
                    from games g inner join gameEvents gev on g.g_id = gev.gameId 
                    group by g.g_id
                ) myGames 
                    inner join Games g on g.g_id = myGames.g_id
                    inner join Teams homeT on homeT.t_id = g.homeTeamId
                    inner join Teams awayT on awayT.t_id = g.awayTeamId
                where g.gameType = 'R' --and g.season = 20152016
                order by g.g_id
            )
            unpivot (
                (teamId, goalsFor, goalsAgainst, points)
                for team
                in (
                    (homeTeam, homeScore, awayScore, homePoints) as 'home',
                    (awayTeam, awayScore, homeScore, awayPoints) as 'away'
                )
            )
        )
        group by season, gameType, team, teamId
    )
    pivot (
        sum(gamesPlayed) as games, sum(goalsFor) as goalsFor, sum(goalsAgainst) as goalsAgainst, sum(points) as points, sum(regulationWins) as regWins, 
                sum(regulationLoses) as regLoses, sum(overtimeWins) as otWins, sum(overtimeLoses) as otLoses
        for team in ('home' as home, 'away' as away)
    )
) regulationData
    inner join Teams t on t.t_id = regulationData.teamId
    inner join Conference_Teams confT on t.t_id = confT.team_id
    inner join Conferences c on c.c_id = confT.conference_id
    inner join Division_Teams divT on t.t_id = divT.team_id
    inner join Divisions d on d.d_id = divT.division_id
where confT.season = regulationData.season and divT.season = regulationData.season
with read only;