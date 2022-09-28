package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.PersonalData;

public class PersonalDataService {
	
	public static List<PersonalData> getByAccountId(final int accountId) throws DataServiceException {
		List<PersonalData> dataList = new LinkedList<>();
		
		final String query = "SELECT \"id\", \"gameType\", \"stat\" "
				+ "FROM \"PersonalData\" "
				+ "WHERE \"accountId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				PersonalData data = new PersonalData();
				data.setId(rs.getInt(1));
				data.setGameType(rs.getInt(2));
				data.setStat(rs.getDouble(3));
				data.setAccountId(accountId);
				dataList.add(data);
			}
					
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
		
		return dataList;
	}
	
	public static PersonalData get(final int personalDataId) throws DataServiceException {
		PersonalData data = new PersonalData();
		
		final String query = "SELECT \"gameType\", \"stat\", \"accountId\" "
				+ "FROM \"PersonalData\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, personalDataId);
			ResultSet rs = getDeckStmt.executeQuery();
			rs.next();

			data.setId(personalDataId);
			data.setGameType(rs.getInt(1));
			data.setStat(rs.getDouble(2));
			data.setAccountId(rs.getInt(3));
			
			return data;		
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}

	public static boolean create(PersonalData personalData) throws DataServiceException {
		String query = "INSERT INTO \"PersonalData\""
				+ "(\"gameType\", \"stat\", \"accountId\") "
				+ "VALUES (?, ?, ?)";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDataStmt = conn.prepareStatement(query)) {
			
			createDataStmt.setInt(1, personalData.getGameType());
			createDataStmt.setDouble(2, personalData.getStat());
			createDataStmt.setInt(3, personalData.getAccountId());
			
			return createDataStmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
}
