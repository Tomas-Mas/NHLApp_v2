create or replace view MainPageGameBasicData
as
select g.g_id, g.gameDate, homeT.name as homeTeam, homeT.abbreviation as homeAbr, g.homeScore as homeScore, 
        awayT.name as awayTeam, awayT.abbreviation as awayAbr, g.awayScore as awayScore, endPeriod.periodNum, periodType.periodType
from Games g
    inner join Teams homeT on g.homeTeamId = homeT.t_id
    inner join Teams awayT on g.awayTeamId = awayT.t_id
    left join (
        select distinct g.g_id as gameId, ge.periodType as periodType
        from Games g 
            left join GameEvents ge on g.g_id = ge.gameId 
            inner join Events e on ge.eventId = e.e_id
        where e.name = 'Goal' and ge.periodNumber = (
                select max(periodNumber) 
                from GameEvents ev join Events evnt on evnt.e_id = ev.eventId 
                where evnt.name = 'Goal' and ev.gameId = g.g_id)
    ) periodType on g.g_id = periodType.gameId
    inner join (
        select g.g_id, max(ev.periodNumber) as periodNum
        from Games g inner join GameEvents ev on g.g_id = ev.gameId
        group by g.g_id
    ) endPeriod on endPeriod.g_id = g.g_id
order by g.gameDate desc
with read only