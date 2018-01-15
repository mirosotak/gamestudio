package gamestudio.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;

@Transactional
public class ScoreServiceJPA implements ScoreService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addScore(Score score) {
		entityManager.persist(score);

	}

	@Override
	public List<Score> getTopScores(String game) {

		try {
			return entityManager.createQuery("SELECT s FROM Score s WHERE s.game = :game ORDER BY s.value DESC")
					.setParameter("game", game).setMaxResults(10).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Score getBestScore(String game) {

		try {
			return (Score) entityManager.createQuery("SELECT s FROM Score s WHERE s.game = :game ORDER BY s.value DESC")
					.setParameter("game", game).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
