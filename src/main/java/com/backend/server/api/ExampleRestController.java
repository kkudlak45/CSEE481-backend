package com.backend.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExampleController {

  @GetMapping("/hello")
  String all() {
    return "hello world";
  }
  
}