package gamestudio;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gamestudio.entity.Comment;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.service.impl.JDBCConnection;

public class SORM {
	private static final String IDENT_FIELD = "ident";

	public String getCreateTableString(Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(getTableName(clazz));
		sb.append(" (");

		boolean isFirst = true;
		for (Field field : clazz.getDeclaredFields()) {
			if (!isFirst)
				sb.append(", ");
			sb.append(getColumnName(field));
			sb.append(" ");
			sb.append(getSQLType(field));
			if (IDENT_FIELD.equals(field.getName()))
				sb.append(" PRIMARY KEY");
			isFirst = false;
		}

		sb.append(")");
		return sb.toString();
	}

	public String getInsertString(Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(getTableName(clazz));
		sb.append(" (");

		boolean isFirst = true;
		for (Field field : clazz.getDeclaredFields()) {
			if (!isFirst)
				sb.append(", ");
			sb.append(getColumnName(field));
			isFirst = false;
		}

		sb.append(") VALUES (");

		isFirst = true;
		for (Field field : clazz.getDeclaredFields()) {
			if (!isFirst)
				sb.append(", ");
			if (IDENT_FIELD.equals(field.getName()))
				sb.append("nextval('ident_seq')");
			else
				sb.append("?");
			isFirst = false;
		}

		sb.append(")");

		return sb.toString();
	}

	public String getUpdateString(Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(getTableName(clazz));
		sb.append(" SET ");

		boolean isFirst = true;
		for (Field field : clazz.getDeclaredFields()) {
			if (!isFirst)
				sb.append(", ");
			if (!IDENT_FIELD.equals(field.getName())) {
				sb.append(getColumnName(field) + " = ?");
				isFirst = false;
			}
		}

		sb.append(" WHERE ident = ?");
		
		return sb.toString();
	}

	public String getDeleteString(Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(getTableName(clazz));
		sb.append(" WHERE ident = ?");
		return sb.toString();
	}

	public String getSelectString(Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");

		boolean isFirst = true;
		for (Field field : clazz.getDeclaredFields()) {
			if (!isFirst)
				sb.append(", ");
			sb.append(getColumnName(field));
			isFirst = false;
		}

		sb.append(" FROM ");
		sb.append(getTableName(clazz));
		return sb.toString();
	}

	public void insert(Object object) {
		Class clazz = object.getClass();
		String command = getInsertString(clazz);

		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(command)) {

			int index = 1;
			for (Field field : clazz.getDeclaredFields()) {
				if (!IDENT_FIELD.equals(field.getName())) {
					field.setAccessible(true);
					Object value = field.get(object);
					ps.setObject(index, value);
					index++;
				}
			}

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Object object) {
		Class clazz = object.getClass();
		String command = getUpdateString(clazz);

		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(command)) {

			int index = 1;
			for (Field field : clazz.getDeclaredFields()) {
				if (!IDENT_FIELD.equals(field.getName())) {
					field.setAccessible(true);
					Object value = field.get(object);
					ps.setObject(index, value);
					index++;
				}
			}

			Field field = clazz.getDeclaredField(IDENT_FIELD);
			field.setAccessible(true);
			Object value = field.get(object);
			ps.setObject(index, value);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Object object) {
		Class clazz = object.getClass();
		String command = getDeleteString(clazz);

		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD); PreparedStatement ps = connection.prepareStatement(command)) {
			Field field = clazz.getDeclaredField(IDENT_FIELD);
			field.setAccessible(true);
			Object value = field.get(object);
			ps.setObject(1, value);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T> T find(Class<T> clazz, Object ident) {
		List<T> result = select(clazz, " WHERE ident = " + ident);
		if(result.size() == 0)
			return null;
		return result.get(0);
	}
	
	public <T> List<T> select(Class<T> clazz) {
		return select(clazz, null);
	}

	public <T> List<T> select(Class<T> clazz, String condition) {
		String command = getSelectString(clazz) + (condition != null ? " " + condition : "");

		try (Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER,
				JDBCConnection.PASSWORD);
				Statement ps = connection.createStatement();
				ResultSet rs = ps.executeQuery(command)) {

			List<T> list = new ArrayList<>();
			while (rs.next()) {
				T object = clazz.newInstance();
				int index = 1;
				for (Field field : clazz.getDeclaredFields()) {
					Object value = rs.getObject(index);
					field.setAccessible(true);
					field.set(object, value);
					index++;
				}
				list.add(object);
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getTableName(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	private String getColumnName(Field field) {
		return field.getName();
	}

	private String getSQLType(Field field) {
		String type = field.getType().getCanonicalName();
		switch (type) {
		case "int":
			return "INTEGER";
		case "java.lang.String":
			return "VARCHAR(32)";
		case "java.util.Date":
			return "TIMESTAMP";
		default:
			throw new IllegalArgumentException("Not supported Java type " + type);
		}
	}

	public static void main(String[] args) {
		SORM sorm = new SORM();

		System.out.println(sorm.getCreateTableString(Score.class));
		System.out.println(sorm.getCreateTableString(Rating.class));
		System.out.println(sorm.getCreateTableString(Comment.class));

		System.out.println(sorm.getSelectString(Score.class));
		System.out.println(sorm.getSelectString(Rating.class));
		System.out.println(sorm.getSelectString(Comment.class));

		System.out.println(sorm.getInsertString(Score.class));
		System.out.println(sorm.getInsertString(Rating.class));
		System.out.println(sorm.getInsertString(Comment.class));

		System.out.println(sorm.getUpdateString(Score.class));
		System.out.println(sorm.getUpdateString(Rating.class));
		System.out.println(sorm.getUpdateString(Comment.class));

		System.out.println(sorm.getDeleteString(Score.class));
		System.out.println(sorm.getDeleteString(Rating.class));
		System.out.println(sorm.getDeleteString(Comment.class));
		//
		// Score score = new Score();
		// score.setUsername("jaro");
		// score.setGame("mines");
		// score.setValue(100);
		//
		// sorm.insert(score);
		//
		// Rating rating = new Rating();
		// rating.setUsername("fero");
		// rating.setGame("mines");
		// rating.setValue(2);
		// sorm.insert(rating);
		//
		// System.out.println(sorm.getCreateTableString(Osoba.class));
		// System.out.println(sorm.getSelectString(Osoba.class));
		//
		// Osoba osoba = new Osoba();
		// osoba.setMeno("Janko");
		// osoba.setPriezvisko("Hrasko");
		// osoba.setVek(23);
		// sorm.insert(osoba);

		// System.out.println(sorm.select(Osoba.class));
		// System.out.println(sorm.select(Osoba.class, "WHERE vek > 23"));

//		List<Score> scores = sorm.select(Score.class, "WHERE ident > 22");
//		System.out.println(scores);
//		for (Score score : scores) {
//			score.setValue(500);
//			sorm.update(score);
//		}

		Score score = sorm.find(Score.class, 22);
		System.out.println(score);
	}
}
