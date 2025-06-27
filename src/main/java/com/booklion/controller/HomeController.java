package com.booklion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String mainFormPage() {

		return "home/main.html";
	}

	@GetMapping("/api/home")
	public String homeFormPage() {
		
		return "home/home.html";
	}
}