package com.booklion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String mainFormPage() {

		return "home/main";
	}

	@GetMapping("/api/home")
	public String homeFormPage() {
		
		return "home/home";
	}
	
	@GetMapping("/login")
	public String loginFormPage() {
	    return "users/login";
	}
	@GetMapping("/signup")
	public String signupFormPage() {
	    return "users/signup";
	}
	
	@GetMapping("/api/reviews")
	public String reviewsPage() {
	    return "review/reviews";
	}

	@GetMapping("/api/qna")
	public String qnaPage() {
	    return "qna/qna";
	}
	
	@GetMapping("/mypage")
	public String myPage() {
	    return "users/mypage";
	}
	
}