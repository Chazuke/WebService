package com.web.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class WebServiceController {

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello, World!";
	}

}
