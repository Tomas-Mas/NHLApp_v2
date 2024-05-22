package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.SkaterStatsDTO;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.entity.Player;
import com.tom.nhl.entity.view.SkaterStatsPerGame;
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
