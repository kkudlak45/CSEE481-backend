package com.backend.server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.services.PersonalDataService;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.PersonalData;

@RestController
public class GameDataRestController {

	@PostMapping(path = "/storeData",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> storeData(@RequestBody PersonalData personalData) {
		
		try {
			PersonalDataService.create(personalData);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
