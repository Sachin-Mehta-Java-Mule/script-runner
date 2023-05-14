package com.affiliate.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.jdbc.EmbeddedDriver;

public class DBUtils {

	private final static String url = "jdbc:postgresql://localhost:5432/test";
	private final static String user = "postgres";
	private final static String password = "postgres";

	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 * @throws java.sql.SQLException
	 */
	public static Connection connect() throws SQLException {
//		return DriverManager.getConnection(url, user, password);
		DriverManager.registerDriver(new EmbeddedDriver());
		return DriverManager.getConnection("jdbc:derby:test2;create=true");
	}

	public static long insertDeals(Deals deal) throws Exception {
		String SQL = "INSERT INTO deals(sha1, message, time) " + "VALUES(?,?,?)";

		long id = 0;

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, deal.getSha1());
			pstmt.setString(2, deal.getMessage());
			pstmt.setTimestamp(3, deal.getTime());

			int affectedRows = pstmt.executeUpdate();
			// check the affected rows
			if (affectedRows > 0) {
				// get the ID back
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException ex) {
					id = 99999999;
					System.out.println(ex.getMessage());
				}
			}
		} catch (SQLException ex) {
			try {
			connect().createStatement().executeLargeUpdate("CREATE TABLE DEALS("
					+ "id INTEGER NOT NULL  PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 0, Increment by 1), "
					+ "sha1 VARCHAR(9999) NOT NULL," + "message VARCHAR(9999) NOT NULL," + "time timestamp NOT NULL,"
					+ " unique (sha1, message) )");}
		catch (Exception e) {
				// TODO: handle exception
			}
//			System.out.println(ex.getMessage());
		}
		return id;
	}

}