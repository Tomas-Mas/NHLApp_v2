create or replace view gameStatsView 
as
select gameData.g_id as gameId, gameData.periodNumber, gameData.team,
        case when gameData.name = 'Faceoff' then 1 else 0 end as faceoffs,
        case when gameData.name = 'Penalty' then 1 else 0 end as penalties,
        case when gameData.name = 'Penalty' then gameData.penaltyMinutes else 0 end as penaltyMinutes,
        case when gameData.name = 'Giveaway' then 1 else 0 end as giveaways,
        case when gameData.name = 'Takeaway' then 1 else 0 end as takeaways,
        case when gameData.name = 'Hit' then 1 else 0 end as hits,
        case when gameData.name in ('Shot', 'Blocked Shot', 'Missed Shot', 'Goal') then 1 else 0 end as shots,
        case when gameData.name in ('Shot', 'Goal') then 1 else 0 end as shotsOnGoal, 
        case when gameData.name = 'Blocked Shot' then 1 else 0 end as blockedShots,
        case when gameData.name = 'Missed Shot' then 1 else 0 end as missedShots,
        case when gameData.name = 'Goal' then 1 else 0 end as goals,
        case when gameData.name = 'Goal' and gameData.strength = 'Power Play' then 1 else 0 end as ppGoals
from (
    select g.g_id, ge.periodNumber, e.name, r.t_id, e.emptyNet, e.penaltyMinutes, e.strength,
            case when r.t_id = homeT.t_id then 'home' else 'away' end as team
    from Games g inner join Teams homeT on homeT.t_id = g.homeTeamId
        inner join Teams awayT on awayT.t_id = g.awayTeamId
        inner join GameEvents ge on g.g_id = ge.gameId
        inner join Events e on ge.eventId = e.e_id
        inner join EventPlayers ep on ge.ge_id = ep.event_id
        inner join Rosters r on r.r_id = ep.roster_id
    where (e.name = 'Faceoff' and ep.role = 'Winner') or
        (e.name = 'Penalty' and ep.role = 'PenaltyOn') or
        (e.name = 'Giveaway') or 
        (e.name = 'Takeaway') or
        (e.name = 'Hit' and ep.role = 'Hitter') or
        (e.name = 'Shot' and ep.role = 'Shooter') or
        (e.name = 'Missed Shot' and ep.role = 'Shooter') or
        (e.name = 'Blocked Shot' and ep.role = 'Blocker') or
        (e.name = 'Goal' and ep.role = 'Scorer')
    ) gameData
with read only;