package com.backend.server.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.services.CurveDataService;
import com.backend.server.database.services.PersonalDataService;
import com.backend.server.exception.DataServiceException;
import com.backend.server.objects.CurveData;
import com.backend.server.objects.PersonalData;

@RestController
@RequestMapping("account/{accountId}")
public class GameDataRestController {
	
	@GetMapping(path = "/personalData",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<PersonalData>> getPersonalData(@PathVariable int accountId) {
		try {
			return new ResponseEntity<>(PersonalDataService.getByAccountId(accountId), HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@GetMapping(path = "/personalData/{gameType}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<PersonalData>> getPersonalDataGivenGameType(@PathVariable int accountId, @PathVariable int gameType) {
		try {
			return new ResponseEntity<>(PersonalDataService.getByAccountAndGameId(accountId, gameType), HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@GetMapping(path = "/curveData/{gameType}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CurveData> getCurveDataGivenGameType(@PathVariable int accountId, @PathVariable int gameType) {
		try {
			return new ResponseEntity<>(CurveDataService.getByAccountAndGameId(accountId, gameType), HttpStatus.OK);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * This accepts a PersonalData, so you should just be able to send it:
	 * {
	 * 	gameType: [int],
	 * 	stat: [number]
	 * }
	 * 
	 * It will create a PersonalData and update the curveData appropriately
	 * 
	 * @param personalData
	 * @return
	 */
	@PostMapping(path = "/storeData",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<HttpStatus> storeData(@RequestBody PersonalData personalData, @PathVariable int accountId) {
		try {
			personalData.setAccountId(accountId);
			PersonalDataService.create(personalData);
			CurveDataService.upsert(personalData);
			
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DataServiceException e) {
			System.err.println(e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
}
