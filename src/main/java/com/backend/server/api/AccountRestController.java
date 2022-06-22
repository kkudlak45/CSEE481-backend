package com.backend.server.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.objects.Account;

@RestController
public class AccountRestController {

	@PostMapping(path = "/createAccount",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	String createAccount(@RequestBody Account account) {
		
		System.out.println(account.getName());
		
		return "hello world 2";
	}
	
}
