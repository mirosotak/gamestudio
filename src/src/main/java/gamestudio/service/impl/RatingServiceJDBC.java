package gamestudio.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import gamestudio.entity.Comment;
import gamestudio.entity.Rating;
import gamestudio.service.RatingService;

public class RatingServiceJDBC implements RatingService {
	private static final String SELECT_COMMAND = "select avg(value) from rating where game = ?";
	private static final String INSERT_COMMAND = "insert into rating(ident, username, game, value) values (nextval('ident_seq'),?,?,?)";
	private static final String DELETE_COMMAND = "delete from rating where username = ? and game = ?";

	@Override
	public void setRating(Rating rating) {
		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD)) {
			try (PreparedStatement ps = connection.prepareStatement(DELETE_COMMAND)) {
				ps.setString(1, rating.getUsername());
				ps.setString(2, rating.getGame());
				ps.executeUpdate();

			}
			try (PreparedStatement ps = connection.prepareStatement(INSERT_COMMAND)) {
				ps.setString(1, rating.getUsername());
				ps.setString(2, rating.getGame());
				ps.setInt(3, rating.getValue());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public double getAverageRating(String game) {
		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(SELECT_COMMAND)) {
			ps.setString(1, game);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
