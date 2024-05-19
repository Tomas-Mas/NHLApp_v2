create or replace view SkaterStatsPerGame
as
select g.season, g.gameType, g.g_id, g.p_id, g.firstName, g.lastName, g.position, g.team, max(g.timeOnIce) as TimeOnIce, max(g.plusMinus) as PlusMinus, max(g.GamesPlayed) as GamesPlayed, 
        sum(g.Goals) as Goals, sum(g.Assists) as Assists, sum(g.Points) as Points, 
        sum(g.PPGoals) as PPGoals, sum(g.PPPoints) as PPPoints, sum(g.SHGoals) as SHGoals, sum(g.SHPoints) as SHPoints, sum(g.OTGoals) as OTGoals, 
        sum(g.ShootoutTaken) as ShootoutTaken, sum(g.ShootoutGoals) as ShootoutGoals, sum(g.PIM) as PIM, sum(g.Shots) as Shots, sum(g.BlockedShots) as BlockedShots, 
        sum(g.Faceoffs) as Faceoffs, sum(g.FaceoffsWon) as FaceoffsWon, sum(g.Hits) as Hits, sum(g.HitsTaken) as HitsTaken, sum(g.Takeaway) as Takeaway, sum(g.Giveaway) as Giveaway
from (
    select data.g_id, data.p_id, data.firstName, data.lastName, data.position, data.season, data.gameType, data.team, data.timeOnIce, data.plusMinus,
            case when data.timeOnIce != '0' then 1 else 0 end as GamesPlayed,
            case when data.name = 'Goal' and data.role = 'Scorer' and (data.periodType = 'REG' or data.periodType = 'OT') then 1 else 0 end as Goals,
            case when data.name = 'Goal' and data.role = 'Assist' then 1 else 0 end as Assists,
            case when data.name = 'Goal' and (data.role = 'Scorer' OR data.role = 'Assist') and (data.periodType = 'REG' or data.periodType = 'OT') then 1 else 0 end as Points,
            case when data.name = 'Goal' and data.role = 'Scorer' and data.strength = 'Power Play' then 1 else 0 end as PPGoals,
            case when data.name = 'Goal' and data.strength = 'Power Play' and (data.role = 'Scorer' OR data.role = 'Assist') then 1 else 0 end as PPPoints,
            case when data.name = 'Goal' and data.role = 'Scorer' and data.strength = 'Short Handed' then 1 else 0 end as SHGoals,
            case when data.name = 'Goal' and data.strength = 'Short Handed' and (data.role = 'Scorer' OR data.role = 'Assist') then 1 else 0 end as SHPoints,
            case when data.name = 'Goal' and data.periodType = 'OT' and data.role = 'Scorer' then 1 else 0 end as OTGoals,
            case when data.periodType = 'SO' and data.role != 'Goalie' then 1 else 0 end as ShootoutTaken,
            case when data.name = 'Goal' and data.periodType = 'SO' and data.role = 'Scorer' then 1 else 0 end as ShootoutGoals,
            case when data.name = 'Penalty' and data.role = 'PenaltyOn' then data.penaltyMinutes else 0 end as PIM,
            case when data.name = 'Shot On Goal' and data.role = 'Shooter' and data.periodType != 'SO' then 1
                when data.name = 'Goal' and data.role = 'Scorer' and (data.periodType = 'REG' or data.periodType = 'OT') then 1 else 0 end as Shots,
            case when data.name = 'Blocked Shot' and data.role = 'Blocker' then 1 else 0 end as BlockedShots,
            case when data.name = 'Faceoff' and (data.role = 'Winner' or data.role = 'Loser') then 1 else 0 end as Faceoffs,
            case when data.name = 'Faceoff' and data.role = 'Winner' then 1 else 0 end as FaceoffsWon,
            case when data.name = 'Hit' and data.role = 'Hitter' then 1 else 0 end as Hits,
            case when data.name = 'Hit' and data.role = 'Hittee' then 1 else 0 end as HitsTaken,
            case when data.name = 'Takeaway' then 1 else 0 end as Takeaway,
            case when data.name = 'Giveaway' then 1 else 0 end as Giveaway
    from (
        select g.g_id, g.season, g.gameType,
                case when g.homeTeamId = r.t_id then 'home' else 'away' end as team,
                p.p_id, p.firstName, p.lastName, pos.name as position, ge.periodType, e.name, e.penaltyMinutes, e.strength, ep.role, 
                NVL(r.plusMinus, 0) as plusMinus, NVL(SUBSTR(r.timeOnIce, 0, INSTR(r.timeOnIce, ':') - 1) * 60, 0) + NVL(SUBSTR(r.timeOnIce, INSTR(r.timeOnIce, ':') + 1), 0) as timeOnIce
        from games g
            left join Rosters r on g.g_id = r.g_id
            left join Players p on p.p_id = r.p_id
            left join Positions pos on pos.p_id = p.positionId
            inner join EventPlayers ep on r.r_id = ep.roster_id
            left join gameEvents ge on ge.ge_id = ep.event_id
            left join Events e on e.e_id = ge.eventId
        where (ep.role != 'Goalie' or ep.role is null) and pos.name != 'Goalie'
        order by ge.ge_id
        ) data
) g
group by g.season, g.gameType, g.g_id, g.p_id, g.firstName, g.lastName, g.position, g.team
with read only;