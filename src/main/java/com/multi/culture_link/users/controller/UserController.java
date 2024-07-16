package com.multi.culture_link.users.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {



	@GetMapping
	public void signUp(){
	
	
	}
	
	@PostMapping("/login2")
	public String login2(){
		
		return "redirect:/";
		
	}



}
