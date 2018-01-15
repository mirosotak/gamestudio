package gamestudio.service.impl;

import java.util.List;

import gamestudio.SORM;
import gamestudio.entity.Rating;
import gamestudio.service.RatingService;

public class RatingServiceSORM implements RatingService {

	private SORM sorm = new SORM();

	@Override
	public void setRating(Rating rating) {
		sorm.insert(rating);

	}

	@Override
	public double getAverageRating(String game) {
		List<Rating> list = sorm.select(Rating.class, String.format("WHERE game = '%s'", game));
		double ratingValues = 0;
		for (Rating rating : list) {
			ratingValues += rating.getValue();

		}
		return ratingValues / list.size();

	}

}
