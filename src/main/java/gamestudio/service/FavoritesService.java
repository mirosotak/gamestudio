package gamestudio.service;

import java.util.List;

import gamestudio.entity.Favorites;

public interface FavoritesService {
	void addFavorite(Favorites favorites);

	

	boolean isFavorite(Favorites favorites);



	List<Favorites> getFavorites(String userName);

}


