package com.multi.culture_link.users.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	
	
	@GetMapping("/user/login")
	public String login(){
		
		return "/user/login";
	}
	
	
}
