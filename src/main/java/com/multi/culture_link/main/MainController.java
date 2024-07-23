package com.multi.culture_link.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * 메인화면 관련 컨트롤러 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Controller
public class MainController {
	
	/**
	 *
	 * 메인화면으로 이동
	 * @return 메인화면 주소
	 */
	@GetMapping(value = {"/", "/home"})
	public String main() {		
		
		return "main/main";
		
	}
	
	
}
