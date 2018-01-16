package gamestudio.entity;

public class GameInfo {

	private String gameName;
	private Score bestScore;
	private boolean favorite;
	private double averageRating;
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Score getBestScore() {
		return bestScore;
	}
	public void setBestScore(Score bestScore) {
		this.bestScore = bestScore;
	}
	
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	@Override
	public String toString() {
		return "GameInfo [gameName=" + gameName + ", bestScore=" + bestScore + ", favorite=" + favorite
				+ ", averageRating=" + averageRating + "]";
	}
	
	
}
