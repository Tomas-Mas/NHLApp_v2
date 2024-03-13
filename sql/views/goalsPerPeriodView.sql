create or replace view goalsPerPeriod
as
select sys_guid() as uuid, data.*
from (
    select gameData.gameId, gameData.team, gameData.period, count(*) as goals
    from (
        select g.g_id as gameId, ge.periodNumber as period,
                case
                    when r.t_id = g.homeTeamId then 'Home'
                    else 'Away'
                end as team
        from Games g
            left join GameEvents ge on g.g_id = ge.gameId
            inner join Events e on ge.eventId = e.e_id
            left join EventPlayers ep on ge.ge_id = ep.event_id
            inner join Rosters r on r.r_id = ep.roster_id
        where e.name = 'Goal' and ep.role = 'Scorer'
    ) gameData
    group by gameData.gameId, gameData.team, gameData.period
) data
with read only