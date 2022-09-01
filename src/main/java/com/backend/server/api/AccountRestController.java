package com.backend.server.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;
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
public class AccountRestController {
	
	BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();

	/**
	 * Creates an Account object with the supplied account JSON's name, username,
	 * 	password, interests, email, and birthdate.
	 * 
	 * @param account - JSON represented account, required fields are: name, username, email, password
	 * @return - either a success response or an error response.
	 */
	@PostMapping(path = "/createAccount",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> createAccount(@RequestBody Account account) {
		
		String query = "INSERT INTO \"Account\""
				+ "(\"name\", \"username\", \"password\", \"interests\", \"email\", \"birthDate\") \n"
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createAccStmt = conn.prepareStatement(query)) {
			String hashedPassword = this.hasher.encode(account.getPassword());
			
			createAccStmt.setString(1, account.getName());
			createAccStmt.setString(2, account.getUsername()); // TODO: in the DB, we should probably have a uniqueness constraint on this prop & we might want to return a response here that an acc with this user already exists
			createAccStmt.setString(3, hashedPassword); // TODO: hash password before storing in db
			createAccStmt.setString(4, account.getInterests());
			createAccStmt.setString(5, account.getEmail()); // TODO: similarly to username, we probably want a uniqueness constraint here & will want a return code if an acc with this email exists
			createAccStmt.setObject(6, account.getBirthDate());
			
			createAccStmt.execute();
		} catch (SQLException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * An endpoint that checks the database to see whether an account with the supplied username and password exists
	 * 	and returns an answer in the form of an integer. If integer is -1, account could not be found, otherwise is the account's id
	 * 
	 * @param account - an account object containing a username and password mapping, no other mappings are required
	 * @return ResponseEntity containing True or False if the account exists w/ this username and password (TODO: we will probably want to return the acc's ID)
	 */
	@PostMapping(path = "/verifyLogin",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Integer> verifyLogin(@RequestBody Account account) {
		
		String query = "SELECT \"password\", \"id\" "
				+ "FROM \"Account\" "
				+ "WHERE \"username\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getAccStmt = conn.prepareStatement(query)) {
			
			getAccStmt.setString(1, account.getUsername());
			ResultSet rs = getAccStmt.executeQuery();
			
			rs.next();
			String hashedPassword = rs.getString(1);
			int id = rs.getInt(2);
			boolean doesMatch = this.hasher.matches(account.getPassword(), hashedPassword);
			
			if (doesMatch) {
				return new ResponseEntity<>(id, HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>(-1, HttpStatus.OK);
			}
		} catch (SQLException e) {
			return new ResponseEntity<>(-1, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
