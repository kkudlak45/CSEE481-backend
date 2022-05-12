package com.backend.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManagerSmokeTest {

	public static void main(String args[]) throws SQLException {
		Connection conn = ConnectionManager.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM test_table");
		
		while (rs.next()) {
			System.out.println(rs.getInt(1) + ":"  + rs.getString(2));
		}
		
		conn.close();
	}
	
}
