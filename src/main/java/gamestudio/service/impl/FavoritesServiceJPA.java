package gamestudio.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Favorites;
import gamestudio.service.FavoritesService;

@Transactional
public class FavoritesServiceJPA implements FavoritesService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addFavorite(Favorites favorites) {
		try {
			Favorites f = (Favorites) entityManager
					.createQuery("SELECT f FROM Favorites f WHERE f.username =:username AND f.game =:game")
					.setParameter("username", favorites.getUsername()).setParameter("game", favorites.getGame())
					.getSingleResult();
			entityManager.remove(f);
		} catch (NoResultException e) {
			entityManager.persist(favorites);

		}

	}

	@Override
	public boolean isFavorite(Favorites favorites) {
		try {
			entityManager.createQuery("SELECT f FROM Favourite f WHERE f.username =:username AND f.game =:game")
					.setParameter("username", favorites.getUsername()).setParameter("game", favorites.getGame())
					.getSingleResult();
			return true;
		} catch (NoResultException e) {

		}
		return false;

	}

	@Override
	public List<Favorites> getFavorites(String userName) {
		return entityManager.createQuery("SELECT f FROM Favorites f WHERE f.username =:username")
				.setParameter("username", userName).getResultList();
	}

}

//
// @Override
// public void addFavourite(Favourite favourite) {
// try {
// Favourite f = (Favourite)entityManager.createQuery("SELECT f FROM Favourite f
// WHERE f.username =:username AND f.game =:game")
// .setParameter("username", favourite.getUsername()).setParameter("game",
// favourite.getGame())
// .getSingleResult();
//
// entityManager.remove(f);
// } catch (NoResultException e) {
// entityManager.persist(favourite);
// }
//
// }
//
// @Override
// public boolean isFavourite(Favourite favourite) {
//
// try {
// entityManager.createQuery("SELECT f FROM Favourite f WHERE f.username
// =:username AND f.game =:game")
// .setParameter("username", favourite.getUsername()).setParameter("game",
// favourite.getGame())
// .getSingleResult();
// return true;
// } catch (NoResultException e) {
//
// }
// return false;
// }