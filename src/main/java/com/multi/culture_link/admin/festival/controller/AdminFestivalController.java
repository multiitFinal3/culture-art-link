package com.multi.culture_link.admin.festival.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/festival-regulate")
public class AdminFestivalController {
	
	
	@GetMapping
	public String festivalManage(){

		
		
		return "/admin/festival/festivalRegulate";
		
	}
	
	
	
	
	
}
