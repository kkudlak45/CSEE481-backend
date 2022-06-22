package com.backend.server.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;
import com.backend.server.objects.Account;

@RestController
public class AccountRestController {

	/**
	 * Creates an Account object with the supplied account JSON's name, username,
	 * 	hashed password, interests, email, and birthdate.
	 * 
	 * @param account - JSON represented account, required fields are: name, username,
	 * 	email, password hash
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
			createAccStmt.setString(1, account.getName());
			createAccStmt.setString(2, account.getUsername());
			createAccStmt.setString(3, account.getPassword());
			createAccStmt.setString(4, account.getInterests());
			createAccStmt.setString(5, account.getEmail());
			createAccStmt.setObject(6, account.getBirthDate());
			
			createAccStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
