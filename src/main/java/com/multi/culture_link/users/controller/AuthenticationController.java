package com.multi.culture_link.users.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	
	
	@GetMapping("/users/login")
	public String login(){
		
		return "/users/login";
	}
	
	
}
