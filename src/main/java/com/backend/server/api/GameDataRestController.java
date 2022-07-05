package com.backend.server.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;
import com.backend.server.objects.PersonalData;

@RestController
public class GameDataRestController {

	@PostMapping(path = "/storeData",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:3000") // TODO make this configurable (this solves CORS issues)
	ResponseEntity<HttpStatus> storeData(@RequestBody PersonalData personalData) {
		
		String query = "INSERT INTO \"PersonalData\""
				+ "(\"gameType\", \"stat\", \"accountId\") "
				+ "VALUES (?, ?, ?)";
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement createDataStmt = conn.prepareStatement(query)) {
			
			createDataStmt.setInt(1, personalData.getGameType());
			createDataStmt.setDouble(2, personalData.getStat());
			createDataStmt.setInt(3, personalData.getAccountId());
			
			createDataStmt.execute();
		} catch (SQLException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
