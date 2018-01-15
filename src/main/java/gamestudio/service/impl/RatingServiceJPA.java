package gamestudio.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Rating;
import gamestudio.service.RatingService;

@Transactional
public class RatingServiceJPA implements RatingService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void setRating(Rating rating) {
		try {
			Rating r = (Rating) entityManager
					.createQuery("SELECT r FROM Rating r WHERE r.username = :username AND r.game = :game")
					.setParameter("username", rating.getUsername()).setParameter("game", rating.getGame())
					.getSingleResult();
			r.setValue(rating.getValue());

		} catch (NoResultException e) {
			entityManager.persist(rating);
		}

	}

	public double getAverageRating(String game) {
		Object averageRating = entityManager.createQuery("SELECT AVG(r.value) FROM Rating r WHERE r.game = :game ")
				.setParameter("game", game).getSingleResult();
		return averageRating == null ? 0 : (double) averageRating;

	}

}
