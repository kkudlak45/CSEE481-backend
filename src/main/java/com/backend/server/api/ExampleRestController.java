package com.backend.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * this is just here to provide an example of what a rest controller will look like.
 * 
 * in addition, the hello world endpoint can be used to show that the server is running.
 * 
 * @author kudlakk
 *
 */
@RestController
class ExampleController {

	@GetMapping("/hello")
	String helloWorld() {
		return "hello world";
	}
	
	@GetMapping("/helloparam")
	String paramHelloWorld(@RequestParam String name) {
		return "hello " + name;
	}
  
}