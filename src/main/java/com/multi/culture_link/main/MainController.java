package com.multi.culture_link.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	/**
	 * 메인화면으로 이동
	 * @return 메인화면 주소
	 */
	@GetMapping(value = {"/", "/home"})
	public String main() {		
		
		return "main/main";
		
	}
	
	
}
