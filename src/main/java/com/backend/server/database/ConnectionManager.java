package com.backend.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionManager {

	private static final Dotenv ENV = Dotenv.load();
	
	public static final String HOST     = ENV.get("HOST");
	public static final String PORT     = ENV.get("PORT");
	public static final String DATABASE = ENV.get("DATABASE");
	public static final String JDBC_URL = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, DATABASE);
	
	/**
	 * Establishes a connection to a postgresql database
	 * Connection details must be set using a .env file in the root directory of this project
	 * A .env.example is provided in the root directory to show what the .env file should look like
	 * The database connection is static and will only be created once. After that, everything will go through this connection
	 * 
	 * NOTE: Connections should only be obtained in a "try with resources" block, otherwise, always be sure to close your connections.
	 * 	"try with resources" will close connections for you, if you don't use it, you *must* manually close these
	 * 
	 * @return A connection to a database. 
	 */
	public static Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(JDBC_URL, ENV.get("DB_USER"), ENV.get("DB_PASS"));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
}