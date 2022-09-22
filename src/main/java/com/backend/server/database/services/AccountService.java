package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Account;
import com.backend.server.utils.GeneralUtils;

public class AccountService {
	
	private static final BCryptPasswordEncoder HASHER = new BCryptPasswordEncoder();

	public static Account get(final int accountId) throws DataServiceException {
		final String query = "SELECT \"username\", \"picture\", \"name\", \"joinDate\", \"interests\", \"email\", \"birthDate\" "
				+ "FROM \"Account\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getAccStmt = conn.prepareStatement(query)) {
			
			getAccStmt.setInt(1, accountId);
			ResultSet rs = getAccStmt.executeQuery();
			rs.next();
			
			Account account = new Account();
			account.setUsername(rs.getString(1));
			account.setPicture(rs.getString(2));
			account.setName(rs.getString(3));
			account.setJoinDate(GeneralUtils.toLocalDate(rs.getDate(4)));
			account.setInterests(rs.getString(5));
			account.setEmail(rs.getString(6));
			account.setBirthDate(GeneralUtils.toLocalDate(rs.getDate(7)));
			
			return account;
					
		} catch (SQLException e) {
			System.err.println(e);
			throw new DataServiceException(e);
		}
	}
	
	public static boolean create(final Account account) throws DataServiceException {
		final String query = "INSERT INTO \"Account\""
				+ "(\"name\", \"username\", \"password\", \"interests\", \"email\", \"birthDate\") \n"
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createAccStmt = conn.prepareStatement(query)) {
			String hashedPassword = HASHER.encode(account.getPassword());
			
			createAccStmt.setString(1, account.getName());
			createAccStmt.setString(2, account.getUsername()); // TODO: in the DB, we should probably have a uniqueness constraint on this prop & we might want to return a response here that an acc with this user already exists
			createAccStmt.setString(3, hashedPassword);
			createAccStmt.setString(4, account.getInterests());
			createAccStmt.setString(5, account.getEmail()); // TODO: similarly to username, we probably want a uniqueness constraint here & will want a return code if an acc with this email exists
			createAccStmt.setObject(6, account.getBirthDate());
			
			return createAccStmt.execute();
		} catch (SQLException e) {
			System.err.println(e);
			throw new DataServiceException(e);
		}
	}
	
	public static int update(final Account account) throws DataServiceException {
		final String query = "UPDATE \"Account\" "
				+ "SET \"username\"=?, \"picture\"=?, \"password\"=?, \"name\"=?, \"joinDate\"=?, \"interests\"=?, \"email\"=?, \"birthDate\"=? "
				+ "WHERE \"id\"=?;";
	
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			String hashedPassword = HASHER.encode(account.getPassword());
			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPicture());
			stmt.setString(3, hashedPassword);
			stmt.setString(4, account.getName());
			stmt.setObject(5, account.getJoinDate());
			stmt.setString(6, account.getInterests());
			stmt.setString(7, account.getEmail());
			stmt.setObject(8, account.getBirthDate());
			stmt.setInt(9, account.getId());
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static boolean delete(final int accountId) throws DataServiceException {
		final String query = "DELETE FROM \"Account\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, accountId);
			
			return stmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	} 
	
	public static int getIdFromLogin(final Account account) throws DataServiceException {
		final String query = "SELECT \"password\", \"id\" "
				+ "FROM \"Account\" "
				+ "WHERE \"username\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getAccStmt = conn.prepareStatement(query)) {
			
			getAccStmt.setString(1, account.getUsername());
			ResultSet rs = getAccStmt.executeQuery();
			
			rs.next();
			String hashedPassword = rs.getString(1);
			int id = rs.getInt(2);
			boolean doesMatch = HASHER.matches(account.getPassword(), hashedPassword);
			
			if (doesMatch) {
				return id; 
			}
			else {
				return -1;
			}
		} catch (SQLException e) {
			System.err.println(e);
			throw new DataServiceException(e);
		}
	}
	
}
