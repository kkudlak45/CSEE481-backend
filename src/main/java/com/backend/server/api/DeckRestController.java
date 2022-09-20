package com.backend.server.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;
import com.backend.server.database.services.DeckService;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Account;
import com.backend.server.objects.Deck;
import com.backend.server.utils.GeneralUtils;

/**
 * This REST controller handles stuff relating to Accounts. So far, this REST controller establishes
 * 	the following endpoints:
 * 
 * 	createAccount - can be used to insert an Account object into the DB
 *  verifyAccount - takes a username and password combo & returns T/F if the acc exists
 * 
 * @author kudlakk
 */
@RestController
@RequestMapping("account/{accountId}/deck")
public class DeckRestController {
	
	@GetMapping(path = "/{deckId}",
			produces = "application/json")
	public ResponseEntity<Deck> getDeck(@PathVariable int accountId, @PathVariable int deckId) {
		try {
			Deck deck = DeckService.get(deckId);
			return new ResponseEntity<>(deck, HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@GetMapping(
			produces = "application/json")
	public ResponseEntity<List<Deck>> getDecks(@PathVariable int accountId) {
		try {
			List<Deck> deckList = DeckService.getByAccountId(accountId);
			return new ResponseEntity<>(deckList, HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> createDeck(@PathVariable int accountId, @RequestBody Deck deck) {
		try {
			deck.setAccountId(accountId);
			DeckService.create(deck);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@DeleteMapping(path = "/{deckId}",
			produces = "application/json")
	ResponseEntity<HttpStatus> deleteDeck(@PathVariable int accountId, @PathVariable int deckId) {
		try {
			DeckService.delete(deckId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
