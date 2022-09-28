package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Deck;
import com.backend.server.utils.GeneralUtils;

public class DeckService {

	public static List<Deck> getByAccountId(final int accountId) throws DataServiceException {
		List<Deck> deckList = new LinkedList<>();
		
		final String query = "SELECT \"id\", \"name\", \"creationDate\", \"lastUsed\" "
				+ "FROM \"Deck\" "
				+ "WHERE \"accountId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, accountId);
			ResultSet rs = getDeckStmt.executeQuery();
			
			while (rs.next()) {
				Deck deck = new Deck();
				deck.setId(rs.getInt(1));
				deck.setName(rs.getString(2));
				deck.setCreationDate(GeneralUtils.toLocalDate(rs.getDate(3)));
				deck.setLastUsed(GeneralUtils.toLocalDate(rs.getDate(4)));	
				deck.setAccountId(accountId);
				deckList.add(deck);
			}
					
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
		
		return deckList;
	}
	
	public static Deck get(final int deckId) throws DataServiceException {
		Deck deckToReturn = new Deck();
		
		final String query = "SELECT \"accountId\", \"name\", \"creationDate\", \"lastUsed\" "
				+ "FROM \"Deck\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, deckId);
			ResultSet rs = getDeckStmt.executeQuery();
			rs.next();

			deckToReturn.setId(deckId);
			deckToReturn.setAccountId(rs.getInt(1));
			deckToReturn.setName(rs.getString(2));
			deckToReturn.setCreationDate(GeneralUtils.toLocalDate(rs.getDate(3)));
			deckToReturn.setLastUsed(GeneralUtils.toLocalDate(rs.getDate(4)));
					
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
		
		return deckToReturn;
	}
	
	public static boolean create(final Deck deck) throws DataServiceException {
		
		final String query = "INSERT INTO \"Deck\" "
				+ "(\"accountId\", \"name\", \"creationDate\", \"lastUsed\") "
				+ "VALUES (?, ?, ?, ?);";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDeckStmt = conn.prepareStatement(query)) {
			
			createDeckStmt.setInt(1, deck.getAccountId());
			createDeckStmt.setString(2, deck.getName());
			createDeckStmt.setObject(3, deck.getCreationDate());
			createDeckStmt.setObject(4, deck.getLastUsed());
			
			return createDeckStmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static boolean delete(final int deckId) throws DataServiceException {
		final String query = "DELETE FROM \"Deck\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDeckStmt = conn.prepareStatement(query)) {
			
			createDeckStmt.setInt(1, deckId);
			
			return createDeckStmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static int update(final Deck deck) throws DataServiceException {
		final String query = "UPDATE \"Deck\" "
				+ "SET \"name\"=?, \"creationDate\"=?, \"lastUsed\"=?, \"accountId\"=? "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDeckStmt = conn.prepareStatement(query)) {
			
			createDeckStmt.setString(1, deck.getName());
			createDeckStmt.setObject(2, deck.getCreationDate());
			createDeckStmt.setObject(3, deck.getLastUsed());
			createDeckStmt.setInt(4, deck.getAccountId());
			createDeckStmt.setInt(5, deck.getId());
			
			return createDeckStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
}
