package gamestudio;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;
import gamestudio.service.impl.ScoreServiceSORM;

public class Test {

	public static void main(String[] args) throws Exception {
		Score score = new Score();
		score.setUsername("jaro");
		score.setGame("mines");
		score.setValue(100);
		ScoreService scoreService = new ScoreServiceSORM();
		scoreService.addScore(score);
		
			
		System.out.println(scoreService.getTopScores("mines"));

//		Comment comment = new Comment();
//		comment.setUsername("Alexandra");
//		comment.setGame("mines");
//		comment.setContent("not bad");
//		//comment.setCreatedOn(new Timestamp(comment.getCreatedOn().getTime()));
//		
//		
//		Date date = new Date();
//	    java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
//	    comment.setCreatedOn(sqlDate);
//	    CommentService commentService = new CommentServiceSORM();
//		commentService.addComment(comment);
//		System.out.println(commentService.getComments("mines"));
//
//		Rating rating = new Rating();
//		rating.setUsername("fero");
//		rating.setGame("mines");
//		rating.setValue(3);
//		RatingService ratingService = new RatingServiceSORM();
//		ratingService.setRating(rating);
//
//		System.out.println(ratingService.getAverageRating("mines"));
	}
}
