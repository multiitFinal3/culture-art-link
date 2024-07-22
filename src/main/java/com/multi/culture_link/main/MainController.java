package com.multi.culture_link.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping(value = {"/","/home"})
	public String main(){
		
		
		
		
		return "main/main";
	}
	
	
	
}
