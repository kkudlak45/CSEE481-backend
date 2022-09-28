package com.backend.server.api;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
@RequestMapping("discord")
public class DiscordAPIBridge {
	
	private static final Dotenv ENV = Dotenv.load();
	private static final String DISCORD_TOKEN = ENV.get("DISCORD_TOKEN");
	
	@GetMapping(path = "/todos")
	public ResponseEntity<String> getTodoMessages(@RequestParam Optional<Integer> limit) {
		OkHttpClient client = new OkHttpClient();
		
		if (limit.isEmpty())
			limit = Optional.of(10);
		
		Request request = new Request.Builder()
		  .url("https://discord.com/api/v9/channels/1007366826319171604/messages?limit=" + limit.get())
		  .method("GET", null)
		  .addHeader("authorization", "Bot " + DISCORD_TOKEN)
		  .build();
		
		try {
			Response response = client.newCall(request).execute();
			String json = response.body().string();
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
