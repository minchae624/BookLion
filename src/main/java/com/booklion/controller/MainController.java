package com.booklion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String main() {
		return "main.html";
	}
	
	@GetMapping("/api/users/login")
	public String login() {
		return "login.html";
	}
}
