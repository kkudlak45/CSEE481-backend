package com.backend.server.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;
import com.backend.server.objects.Account;
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
@RequestMapping("Account")
public class AccountRestController {
	
	BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();
	
	@GetMapping(path = "/{id}",
			produces = "application/json")
	public ResponseEntity<Account> getAccount(@PathVariable int id) {
		
		String query = "SELECT \"username\", \"picture\", \"name\", \"joinDate\", \"interests\", \"email\", \"birthDate\" "
				+ "FROM \"Account\" "
				+ "WHERE \"id\"=?;";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement getAccStmt = conn.prepareStatement(query)) {
			
			getAccStmt.setInt(1, id);
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
			
			return new ResponseEntity<>(account, HttpStatus.OK);
					
		} catch (SQLException e) {
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
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
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
			System.err.println(e);
			return new ResponseEntity<>(-1, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
