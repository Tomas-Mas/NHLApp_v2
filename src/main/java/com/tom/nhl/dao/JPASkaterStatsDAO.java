package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.PlayerStatsFilterDTO;
import com.tom.nhl.dto.SkaterStatsDTO;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.Player;
import com.tom.nhl.entity.view.SkaterStatsPerGame;
import com.tom.nhl.enums.PlayerPosition;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPASkaterStatsDAO implements SkaterStatsDAO {

	public int getCount(int season, SeasonScope seasonScope, RegulationScope regulationScope) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<SkaterStatsPerGame> statsRoot = query.from(SkaterStatsPerGame.class);
		
		List<Predicate> predicates = buildPredicate(cb, statsRoot, season, seasonScope, regulationScope);
		
		query.select(statsRoot.get("id").<Integer>get("playerId"))
				.distinct(true)
				.where(predicates.toArray(new Predicate[] {}));
		
		Integer statCount = em.createQuery(query).getResultList().size();
		em.close();
		return statCount.intValue();
	}
	
	public List<SkaterStatsDTO> getByGameId(int gameId, PlayerStatsFilterDTO statsFilter) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<SkaterStatsDTO> query = cb.createQuery(SkaterStatsDTO.class);
		Root<SkaterStatsPerGame> statsRoot = query.from(SkaterStatsPerGame.class);
		statsRoot.fetch("id", JoinType.INNER);
		
		//home abbreviation subquery
		Subquery<String> sqHomeTeam = query.subquery(String.class);
		Root<Game> gameRootHome = sqHomeTeam.from(Game.class);
		gameRootHome.join("homeTeam", JoinType.INNER);
		sqHomeTeam.select(gameRootHome.get("homeTeam").<String>get("abbreviation"))
				.where(cb.equal(gameRootHome, gameId));
		
		//away abbreviation subquery
		Subquery<String> sqAwayTeam = query.subquery(String.class);
		Root<Game> gameRootAway = sqAwayTeam.from(Game.class);
		gameRootAway.join("awayTeam", JoinType.INNER);
		sqAwayTeam.select(gameRootAway.get("awayTeam").<String>get("abbreviation"))
				.where(cb.equal(gameRootAway, gameId));
		
		List<Expression<?>> selectExp = new ArrayList<Expression<?>>();
		selectExp.add(statsRoot.get("id").<Integer>get("playerId"));
		selectExp.add(statsRoot.<String>get("firstName"));
		selectExp.add(statsRoot.<String>get("lastName"));
		selectExp.add(statsRoot.<String>get("position"));
		selectExp.add(cb.selectCase(statsRoot.<String>get("team"))
				.when("home", sqHomeTeam)
				.when("away", sqAwayTeam)
				.otherwise("Unknown"));
		selectExp.add(statsRoot.<Integer>get("goals"));
		selectExp.add(statsRoot.<Integer>get("assists"));
		selectExp.add(statsRoot.<Integer>get("points"));
		selectExp.add(statsRoot.<Integer>get("plusMinus"));
		selectExp.add(statsRoot.<Integer>get("pim"));
		selectExp.add(statsRoot.<Integer>get("shots"));
		selectExp.add(statsRoot.<Integer>get("blockedShots"));
		selectExp.add(statsRoot.<Integer>get("timeOnIce"));
		selectExp.add(statsRoot.<Integer>get("faceoffs"));
		selectExp.add(statsRoot.<Integer>get("faceoffsWon"));
		
		Expression<Object> faceoffsPercentageExp = cb.selectCase(statsRoot.<Integer>get("faceoffs"))
				.when(0, -1.0f)
				.otherwise(statsRoot.<Float>get("faceoffsPercentage"));
		selectExp.add(faceoffsPercentageExp);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(statsRoot.get("id").get("gameId"), gameId));
		
		if(statsFilter.getOnlyProductive()) {
			predicates.add(cb.gt(statsRoot.<Integer>get("points"), 0));
		}
		if(statsFilter.getPlayerPositionEnum() != PlayerPosition.SKATERS) {
			predicates.add(cb.equal(statsRoot.<String>get("position"), statsFilter.getPlayerPosition()));
		}
		if(statsFilter.getRegulationScopeEnum() != RegulationScope.OVERALL) {
			predicates.add(cb.equal(statsRoot.<String>get("team"), statsFilter.getRegulationScopeEnum().getValue()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		boolean descOrder = statsFilter.getIsDescOrder();
		
		switch(statsFilter.getOrderByColumnEnum()) {
		case GOALS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("goals")) : cb.asc(statsRoot.get("goals")));
			orderList.add(descOrder ? cb.desc(statsRoot.get("points")) : cb.asc(statsRoot.get("points")));
			break;
		case ASSISTS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("assists")) : cb.asc(statsRoot.get("assists")));
			orderList.add(descOrder ? cb.desc(statsRoot.get("goals")) : cb.asc(statsRoot.get("goals")));
			break;
		case POINTS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("points")) : cb.asc(statsRoot.get("points")));
			orderList.add(descOrder ? cb.desc(statsRoot.get("goals")) : cb.asc(statsRoot.get("goals")));
			break;
		case TEAM:
			orderList.add(descOrder ? cb.desc(statsRoot.get("team")) : cb.asc(statsRoot.get("team")));
			orderList.add(cb.desc(statsRoot.get("points")));
			orderList.add(cb.desc(statsRoot.get("goals")));
			break;
		case PLAYERNAME:
			orderList.add(!descOrder ? cb.desc(statsRoot.get("lastName")) : cb.asc(statsRoot.get("lastName")));
			orderList.add(!descOrder ? cb.desc(statsRoot.get("firstName")) : cb.asc(statsRoot.get("firstName")));
			break;
		case PLAYERPOSITION:
			orderList.add(!descOrder ? cb.desc(statsRoot.get("position")) : cb.asc(statsRoot.get("position")));
			orderList.add(cb.desc(statsRoot.get("points")));
			orderList.add(cb.desc(statsRoot.get("goals")));
			break;
		case PLUSMINUS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("plusMinus")) : cb.asc(statsRoot.get("plusMinus")));
			orderList.add(cb.desc(statsRoot.get("points")));
			orderList.add(cb.desc(statsRoot.get("goals")));
			break;
		case PENALTYMINUTES:
			orderList.add(descOrder ? cb.desc(statsRoot.get("pim")) : cb.asc(statsRoot.get("pim")));
			break;
		case SHOTS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("shots")) : cb.asc(statsRoot.get("shots")));
			orderList.add(cb.desc(statsRoot.get("points")));
			orderList.add(cb.desc(statsRoot.get("goals")));
			break;
		case BLOCKEDSHOTS:
			orderList.add(descOrder ? cb.desc(statsRoot.get("blockedShots")) : cb.asc(statsRoot.get("blockedShots")));
			orderList.add(cb.desc(statsRoot.get("points")));
			orderList.add(cb.desc(statsRoot.get("goals")));
			break;
		case TIMEONICE:
			orderList.add(descOrder ? cb.desc(statsRoot.get("timeOnIce")) : cb.asc(statsRoot.get("timeOnIce")));
			break;
		case FACEOFFSOVERALL:
			orderList.add(descOrder ? cb.desc(statsRoot.get("faceoffs")) : cb.asc(statsRoot.get("faceoffs")));
			break;
		case FACEOFFSPERCENTAGE:
			orderList.add(descOrder ? cb.desc(faceoffsPercentageExp) : cb.asc(faceoffsPercentageExp));
			orderList.add(descOrder ? cb.desc(statsRoot.get("faceoffs")) : cb.asc(statsRoot.get("faceoffs")));
			break;
		default:
			orderList.add(descOrder ? cb.desc(statsRoot.get("points")) : cb.asc(statsRoot.get("points")));
			orderList.add(descOrder ? cb.desc(statsRoot.get("goals")) : cb.asc(statsRoot.get("goals")));
		}
		
		query.select(cb.construct(SkaterStatsDTO.class, selectExp.toArray(new Expression<?>[] {})))
				.where(predicates.toArray(new Predicate[] {}))
				.orderBy(orderList);
		
		List<SkaterStatsDTO> stats = em.createQuery(query).getResultList();
		
		return stats;
	}

	public List<SkaterStatsDTO> getBySeasonAndNavigationDTO(int season, StatsNavigationDTO statsNavigation) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<SkaterStatsDTO> query = cb.createQuery(SkaterStatsDTO.class);
		Root<SkaterStatsPerGame> statsRoot = query.from(SkaterStatsPerGame.class);
		
		List<Predicate> predicates = buildPredicate(cb, statsRoot, season, statsNavigation.getSeasonScopeEnum(), statsNavigation.getRegulationScopeEnum());
		
		List<Expression<?>> groupByExp = new ArrayList<Expression<?>>();
		Expression<Integer> seasonExp = statsRoot.get("season");
		Expression<String> gameTypeExp = statsRoot.get("gameType");
		Expression<Player> playerExp = statsRoot.get("id").get("playerId");
		Expression<String> firstNameExp = statsRoot.get("firstName");
		Expression<String> lastNameExp = statsRoot.get("lastName");
		Expression<String> positionExp = statsRoot.get("position");
		groupByExp.add(seasonExp);
		groupByExp.add(gameTypeExp);
		groupByExp.add(playerExp);
		groupByExp.add(firstNameExp);
		groupByExp.add(lastNameExp);
		groupByExp.add(positionExp);
		
		List<Expression<?>> aggregateExp = new ArrayList<Expression<?>>();
		Expression<Integer> gamesPlayedExp = cb.sum(statsRoot.<Integer>get("gamesPlayed"));
		Expression<Double> timeOnIceAvgExp = cb.avg(statsRoot.<Integer>get("timeOnIce"));
		Expression<Integer> plusMinusExp = cb.sum(statsRoot.<Integer>get("plusMinus"));
		Expression<Integer> goalsExp = cb.sum(statsRoot.<Integer>get("goals"));
		Expression<Integer> assistsExp = cb.sum(statsRoot.<Integer>get("assists"));
		Expression<Integer> pointsExp = cb.sum(statsRoot.<Integer>get("points"));
		Expression<Integer> ppGoalsExp = cb.sum(statsRoot.<Integer>get("ppGoals"));
		Expression<Integer> ppPointsExp = cb.sum(statsRoot.<Integer>get("ppPoints"));
		Expression<Integer> shGoalsExp = cb.sum(statsRoot.<Integer>get("shGoals"));
		Expression<Integer> shPointsExp = cb.sum(statsRoot.<Integer>get("shPoints"));
		Expression<Integer> otGoalsExp = cb.sum(statsRoot.<Integer>get("otGoals"));
		Expression<Integer> shootoutTakenExp = cb.sum(statsRoot.<Integer>get("shootoutTaken"));
		Expression<Integer> shootoutGoalsExp = cb.sum(statsRoot.<Integer>get("shootoutGoals"));
		Expression<Integer> penaltyMinutesExp = cb.sum(statsRoot.<Integer>get("pim"));
		Expression<Integer> shotsExp = cb.sum(statsRoot.<Integer>get("shots"));
		Expression<Integer> blockedShotsExp = cb.sum(statsRoot.<Integer>get("blockedShots"));
		Expression<Integer> faceoffsExp = cb.sum(statsRoot.<Integer>get("faceoffs"));
		Expression<Integer> faceoffsWonExp = cb.sum(statsRoot.<Integer>get("faceoffsWon"));
		Expression<Integer> hitsExp = cb.sum(statsRoot.<Integer>get("hits"));
		Expression<Integer> hitsTakenExp = cb.sum(statsRoot.<Integer>get("hitsTaken"));
		Expression<Integer> takeawayExp = cb.sum(statsRoot.<Integer>get("takeaway"));
		Expression<Integer> giveawayExp = cb.sum(statsRoot.<Integer>get("giveaway"));
		aggregateExp.add(gamesPlayedExp);
		aggregateExp.add(timeOnIceAvgExp);
		aggregateExp.add(plusMinusExp);
		aggregateExp.add(goalsExp);
		aggregateExp.add(assistsExp);
		aggregateExp.add(pointsExp);
		aggregateExp.add(ppGoalsExp);
		aggregateExp.add(ppPointsExp);
		aggregateExp.add(shGoalsExp);
		aggregateExp.add(shPointsExp);
		aggregateExp.add(otGoalsExp);
		aggregateExp.add(shootoutTakenExp);
		aggregateExp.add(shootoutGoalsExp);
		aggregateExp.add(penaltyMinutesExp);
		aggregateExp.add(shotsExp);
		aggregateExp.add(blockedShotsExp);
		aggregateExp.add(faceoffsExp);
		aggregateExp.add(faceoffsWonExp);
		aggregateExp.add(hitsExp);
		aggregateExp.add(hitsTakenExp);
		aggregateExp.add(takeawayExp);
		aggregateExp.add(giveawayExp);
		
		List<Expression<?>> selectionExp = new ArrayList<Expression<?>>();
		selectionExp.addAll(groupByExp);
		selectionExp.addAll(aggregateExp);
		
		
		query.groupBy(groupByExp)
				.select(cb.construct(SkaterStatsDTO.class, selectionExp.toArray(new Expression<?>[] {})))
				.where(predicates.toArray(new Predicate[] {}))
				.having(cb.greaterThan(gamesPlayedExp, 0));
		
		switch(statsNavigation.getOrderedByEnum()) {
		case GAMESPLAYED:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), gamesPlayedExp);
			break;
		case GOALS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), goalsExp);
			break;
		case ASSISTS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), assistsExp);
			break;
		case POINTS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), pointsExp);
			break;
		case PLUSMINUS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), plusMinusExp);
			break;
		case PENALTYMINUTES:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), penaltyMinutesExp);
			break;
		case SHOTS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), shotsExp);
			break;
		case BLOCKEDSHOTS:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), blockedShotsExp);
			break;
		case TIMEONICEAVG:
			addOrderByClauseToQuery(query, cb, statsNavigation.getReverseOrder(), timeOnIceAvgExp);
			break;
		default:
			break;
		}
		
		List<SkaterStatsDTO> stats = em.createQuery(query)
				.setFirstResult((Integer.valueOf(statsNavigation.getSelectedPageNumber()) - 1) * statsNavigation.getRowsPerPage())
				.setMaxResults(statsNavigation.getRowsPerPage())
				.getResultList();
		
		em.close();
		return stats;
	}
	
	private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<SkaterStatsPerGame> root, int season, SeasonScope seasonScope, RegulationScope regulationScope) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(root.get("gameType"), seasonScope.getValue()));
		predicates.add(cb.equal(root.get("season"), season));
		if(regulationScope == RegulationScope.HOME || regulationScope == RegulationScope.AWAY) {
			predicates.add(cb.equal(root.get("team"), regulationScope.getValue()));
		}
		return predicates;
	}
	
	private void addOrderByClauseToQuery(CriteriaQuery<SkaterStatsDTO> query, CriteriaBuilder cb, boolean reverseOrder, Expression<?> exp) {
		if(reverseOrder) {
			query.orderBy(cb.asc(exp));
		} else {
			query.orderBy(cb.desc(exp));
		}
	}

}
