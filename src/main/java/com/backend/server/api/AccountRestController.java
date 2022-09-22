package com.backend.server.api;

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

import com.backend.server.database.services.AccountService;
import com.backend.server.database.services.CardService;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.Account;

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
@RequestMapping("account")
public class AccountRestController {
	
	@GetMapping(path = "/{id}",
			produces = "application/json")
	public ResponseEntity<Account> getAccount(@PathVariable int id) {
		try {
			Account account = AccountService.get(id);
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Creates an Account object with the supplied account JSON's name, username,
	 * 	password, interests, email, and birthdate.
	 * 
	 * @param account - JSON represented account, required fields are: name, username, email, password
	 * @return - either a success response or an error response.
	 */
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> createAccount(@RequestBody Account account) {
		try {
			AccountService.create(account);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@PutMapping(path = "/{accountId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> updateAccount(@PathVariable int accountId, @RequestBody Account account) {
		try {
			account.setId(accountId);
			AccountService.update(account);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@DeleteMapping(path = "/{accountId}",
			produces = "application/json")
	ResponseEntity<HttpStatus> deleteCard(@PathVariable int accountId) {
		try {
			AccountService.delete(accountId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	/**
	 * An endpoint that checks the database to see whether an account with the supplied username and password exists
	 * 	and returns an answer in the form of an integer. If integer is -1, account could not be found, otherwise is the account's id
	 * 
	 * @param account - an account object containing a username and password mapping, no other mappings are required
	 * @return ResponseEntity containing the account's database id to use as a session id, 
	 */
	@PostMapping(path = "/verifyLogin",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Integer> verifyLogin(@RequestBody Account account) {
		try {
			return new ResponseEntity<>(AccountService.getIdFromLogin(account), HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(-1, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
