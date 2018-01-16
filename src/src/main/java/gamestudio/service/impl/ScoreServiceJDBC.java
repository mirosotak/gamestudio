package gamestudio.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;

public class ScoreServiceJDBC implements ScoreService {
	private static final String INSERT_COMMAND = "insert into score(ident, username, game, value) values (nextval('ident_seq'),?,?,?)";
	private static final String SELECT_COMMAND = "select ident, username, game, value from score where game = ? order by value desc limit 10";

	@Override
	public void addScore(Score score) {
		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(INSERT_COMMAND)) {
			ps.setString(1, score.getUsername());
			ps.setString(2, score.getGame());
			ps.setInt(3, score.getValue());
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Score> getTopScores(String game) {
		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(SELECT_COMMAND)) {
			ps.setString(1, game);
			try (ResultSet rs = ps.executeQuery()) {
				List<Score> list = new ArrayList<>();
				while (rs.next()) {
					Score score = new Score();
					score.setIdent(rs.getInt(1));
					score.setUsername(rs.getString(2));
					score.setGame(rs.getString(3));
					score.setValue(rs.getInt(4));
					list.add(score);
				}
				return (list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Score getBestScore(String game) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
