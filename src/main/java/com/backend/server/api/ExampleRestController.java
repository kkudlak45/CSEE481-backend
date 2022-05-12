package com.backend.server.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.database.ConnectionManager;

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
  
	@GetMapping("/database")
	String database() throws SQLException {
		Connection conn = ConnectionManager.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM test_table");
		rs.next();
		
		conn.close();
		
		return rs.getInt(1) + ":" + rs.getString(2);
	}
  
}