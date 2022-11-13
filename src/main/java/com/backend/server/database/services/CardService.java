package com.backend.server.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.backend.server.database.ConnectionManager;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Card;

public class CardService {

	public static List<Card> getByDeckId(final int deckId) throws DataServiceException {
		List<Card> cardList = new LinkedList<>();
		
		final String query = "SELECT \"id\", \"name\", \"picture\", \"relationship\" "
				+ "FROM \"Card\" "
				+ "WHERE \"deckId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, deckId);
			ResultSet rs = getDeckStmt.executeQuery();
			
			while (rs.next()) {
				Card card = new Card();
				card.setId(rs.getInt(1));
				card.setName(rs.getString(2));
				card.setPicture(rs.getString(3));
				card.setRelationship(rs.getString(4));
				card.setDeckId(deckId);
				cardList.add(card);
			}
					
		} catch (SQLException e) {
			throw new DataServiceException(e); // the reason im wrapping with a new exception is so i can use a try-with-resources block
		}
		
		return cardList;
	}
	
	public static Card get(final int cardId) throws DataServiceException {
		Card cardToReturn = new Card();
		
		final String query = "SELECT \"name\", \"picture\", \"relationship\", \"deckId\" "
				+ "FROM \"Card\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getDeckStmt = conn.prepareStatement(query)) {
			
			getDeckStmt.setInt(1, cardId);
			ResultSet rs = getDeckStmt.executeQuery();
			rs.next();

			cardToReturn.setId(cardId);
			cardToReturn.setName(rs.getString(1));
			cardToReturn.setPicture(rs.getString(2));
			cardToReturn.setRelationship(rs.getString(3));
			cardToReturn.setDeckId(rs.getInt(4));
					
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
		
		return cardToReturn;
	}
	
	public static boolean create(final Card card) throws DataServiceException {
		
		final String query = "INSERT INTO \"Card\" "
				+ "(\"name\", \"picture\", \"relationship\", \"deckId\") "
				+ "VALUES (?, ?, ?, ?);";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDeckStmt = conn.prepareStatement(query)) {
			
			createDeckStmt.setString(1, card.getName());
			createDeckStmt.setString(2, card.getPicture());
			createDeckStmt.setString(3, card.getRelationship());
			createDeckStmt.setInt(4, card.getDeckId());
			
			return createDeckStmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static boolean delete(final int cardId) throws DataServiceException {
		final String query = "DELETE FROM \"Card\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, cardId);
			
			return stmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static boolean deleteByDeckId(final int deckId) throws DataServiceException {
		final String query = "DELETE FROM \"Card\" "
				+ "WHERE \"deckId\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, deckId);
			
			return stmt.execute();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
	public static int update(final Card card) throws DataServiceException {
		final String query = "UPDATE \"Card\" "
				+ "SET \"name\"=?, \"picture\"=?, \"relationship\"=?, \"deckId\"=? "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setString(1,card.getName());
			stmt.setString(2, card.getPicture());
			stmt.setString(3, card.getRelationship());
			stmt.setInt(4, card.getDeckId());
			stmt.setInt(5, card.getId());
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataServiceException(e);
		}
	}
	
}
