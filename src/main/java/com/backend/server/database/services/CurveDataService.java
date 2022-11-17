package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.backend.server.constants.GameTypeConstants;
import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.CurveData;
import com.backend.server.objects.PersonalData;

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
	
	public static CurveData getByAccountAndGameId(final int accountId, final int gameType) throws DataServiceException {
		final String query = "SELECT \"id\", \"bestStat\", \"recentStat\" "
				+ "FROM \"CurveData\" "
				+ "WHERE \"gameType\"=? AND \"accountId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, gameType);
			stmt.setInt(2, accountId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			CurveData data = new CurveData();
			data.setId(rs.getInt(1));
			data.setBestStat(rs.getDouble(2));
			data.setRecentStat(rs.getDouble(3));
			data.setAccountId(accountId);
			data.setGameType(gameType);
			
			return data;	
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
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
				+ "(\"accountId\", \"gameType\", \"bestStat\", \"recentStat\") "
				+ "VALUES (?, ?, ?, ?)";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, curveData.getAccountId());
			stmt.setInt(2, curveData.getGameType());
			stmt.setDouble(3, curveData.getBestStat());
			stmt.setDouble(4, curveData.getRecentStat());
			
			return stmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static int update(final CurveData curveData) throws DataServiceException {
		final String query = "UPDATE \"CurveData\" "
				+ "SET \"bestStat\"=?, \"recentStat\"=? "
				+ "WHERE \"id\"=?;";
	
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setDouble(1, curveData.getBestStat());
			stmt.setDouble(2, curveData.getRecentStat());
			stmt.setInt(3, curveData.getId());
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	/**
	 * "Upserts" a CurveData. Right now, CurveData only supports "bestStat" and "recentStat", so this will
	 * 	be used to update those properties based on a PersonalData being recorded. If a corresponding 
	 * 	CurveData doesn't exist yet, it will be created (inserted) into the DB.
	 * 
	 * @param personalData
	 * @return
	 * @throws DataServiceException
	 */
	public static boolean upsert(PersonalData personalData) throws DataServiceException {
		
		CurveData curveData;
		try {
			curveData = getByAccountAndGameId(personalData.getAccountId(), personalData.getGameType());
			
		} catch (DataServiceException e) { // if curve data doesn't exist yet, make it.
			curveData = new CurveData();
			curveData.setAccountId(personalData.getAccountId());
			curveData.setBestStat(personalData.getStat());
			curveData.setRecentStat(personalData.getStat());
			curveData.setGameType(personalData.getGameType());
			
			return create(curveData);
		}
		
		// if curve data does exist, update it
		if (
			personalData.getGameType() == GameTypeConstants.MEMORY_GAME
			|| personalData.getGameType() == GameTypeConstants.COLOR_GAME
			&& curveData.getBestStat() < personalData.getStat() // if the new stat is higher, overwrite best stat
		) {
			curveData.setBestStat(personalData.getStat());
		}
		else if (
			personalData.getGameType() == GameTypeConstants.SLIDING_PUZZLE_GAME
			|| personalData.getGameType() == GameTypeConstants.REACTION_GAME
			&& curveData.getBestStat() > personalData.getStat() // if the new stat is lower, overwrite best stat
		) {
			curveData.setBestStat(personalData.getStat());
		}
		curveData.setRecentStat(personalData.getStat());
		
		return 1 == update(curveData);
	}
	
	public static Double getPercentile(final int accountId, final int gameType) throws DataServiceException {
		List<CurveData> dataList = new LinkedList<>();
		
		String orderType = "";
		if (
			gameType == GameTypeConstants.MEMORY_GAME
			|| gameType == GameTypeConstants.COLOR_GAME
		) {
			orderType = " ASC";
		}
		else if (
			gameType == GameTypeConstants.SLIDING_PUZZLE_GAME
			|| gameType == GameTypeConstants.REACTION_GAME
		) {
			orderType = " DESC";
		}
		
		
		final String query = "SELECT \"accountId\", \"bestStat\" FROM \"CurveData\" "
				+ "WHERE \"gameType\"=? "
				+ "ORDER BY \"bestStat\"" + orderType + ";";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, gameType);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				CurveData data = new CurveData();
				data.setAccountId(rs.getInt(1));
				data.setBestStat(rs.getDouble(2));
				dataList.add(data);
			}
			
			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getAccountId() == accountId) {
					return (i+1d)/dataList.size() * 100;
				}
			}
			
			return -1d;	
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
	}
	
}
