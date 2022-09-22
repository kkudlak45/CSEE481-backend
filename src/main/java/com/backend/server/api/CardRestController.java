package com.backend.server.api;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.services.CardService;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Card;

/**
 * This REST controller handles stuff relating to Decks. So far, this REST controller establishes
 * 	the following endpoints:
 * 
 * @author kudlakk
 */
@RestController
@RequestMapping("deck/{deckId}/card")
public class CardRestController {
	
	@GetMapping(path = "/{cardId}",
			produces = "application/json")
	public ResponseEntity<Card> getCard(@PathVariable int deckId, @PathVariable int cardId) {
		try {
			Card card = CardService.get(cardId);
			return new ResponseEntity<>(card, HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@GetMapping(
			produces = "application/json")
	public ResponseEntity<List<Card>> getCards(@PathVariable int deckId) {
		try {
			List<Card> cardList = CardService.getByDeckId(deckId);
			return new ResponseEntity<>(cardList, HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> createCard(@PathVariable int deckId, @RequestBody Card card) {
		try {
			card.setDeckId(deckId);
			CardService.create(card);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@DeleteMapping(path = "/{cardId}",
			produces = "application/json")
	ResponseEntity<HttpStatus> deleteCard(@PathVariable int deckId, @PathVariable int cardId) {
		try {
			CardService.delete(cardId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@PutMapping(path = "/{cardId}",
			produces = "application/json")
	ResponseEntity<HttpStatus> updateCard(@PathVariable int deckId, @PathVariable int cardId, @RequestBody Card card) {
		try {
			card.setId(cardId);
			if (card.getDeckId() == null) {
				card.setDeckId(deckId);
			}
			CardService.update(card);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
