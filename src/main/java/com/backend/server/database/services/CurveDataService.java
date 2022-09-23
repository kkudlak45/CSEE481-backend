package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.CurveData;

public class CurveDataService {
	
	public static List<CurveData> getByAccountId(final int accountId) throws DataServiceException {
		List<CurveData> dataList = new LinkedList<>();
		
		final String query = "SELECT \"id\", \"gameType\", \"bestStat\", \"recentStat\" "
				+ "FROM \"CurveData\" "
				+ "WHERE \"accountId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				CurveData data = new CurveData();
				data.setId(rs.getInt(1));
				data.setGameType(rs.getInt(2));
				data.setBestStat(rs.getDouble(3));
				data.setRecentStat(rs.getDouble(4));
				data.setAccountId(accountId);
				dataList.add(data);
			}
					
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
		
		return dataList;
	}
	
	public static CurveData get(final int curveDataId) throws DataServiceException {
		CurveData data = new CurveData();
		
		final String query = "SELECT \"accountId\", \"gameType\", \"bestStat\", \"recentStat\" "
				+ "FROM \"CurveData\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, curveDataId);
			ResultSet rs = getDeckStmt.executeQuery();
			rs.next();

			data.setId(curveDataId);
			data.setAccountId(rs.getInt(1));
			data.setGameType(rs.getInt(2));
			data.setBestStat(rs.getDouble(3));
			data.setRecentStat(rs.getDouble(4));
			
			return data;		
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}

	public static boolean create(CurveData curveData) throws DataServiceException {
		String query = "INSERT INTO \"CurveData\""
				+ "(\"id\", \"gameType\", \"bestStat\", \"recentStat\") "
				+ "VALUES (?, ?, ?, ?)";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, curveData.getId());
			stmt.setInt(2, curveData.getGameType());
			stmt.setDouble(3, curveData.getBestStat());
			stmt.setDouble(4, curveData.getRecentStat());
			
			return stmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
}
