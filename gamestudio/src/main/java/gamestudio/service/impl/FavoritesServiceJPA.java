package gamestudio.service.impl;


import java.util.List;

import javax.persistence.EntityManager;
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
		entityManager.persist(favorites);
		
	}

	@Override
	public List<Favorites> getFavorites(String userName) {
		return entityManager.createQuery("SELECT f FROM Favorites f WHERE f.username =:username")
				.setParameter("username", userName)
				.getResultList();
	}
	
	
}
	
	