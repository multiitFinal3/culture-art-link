package com.multi.culture_link.users.controller;


import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@GetMapping("/signUp")
	public String signUp(){
	
		return "/user/signUp";
	}
	
	@PostMapping("/signUp")
	public void signUpPost(@RequestParam("userId") int userId, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tel") String tel, @RequestParam("gender") String gender, @RequestParam("regionId") int regionId){
		
		// 회원 users/ admin 넣는 것도 추가할 것 = > 매핑 테이블에도 조인으로 추가하기
		
		
		int result= 0;
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userId);
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		userDTO.setUserName(userName);
		userDTO.setTel(tel);
		userDTO.setGender(gender);
		userDTO.setRegionId(regionId);
		
		/*userDTO.setRoleId(2);*/
		
		
		try {
			
			String encoded_pw = bCryptPasswordEncoder.encode(userDTO.getPassword());
			userDTO.setPassword(encoded_pw);
			
			
			userService.signUp(userDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("회원가입 성공");
		
	}
	
	@PostMapping("/login2")
	public String login2(){
		
		return "redirect:/";
		
	}

	
	@GetMapping("/myPage")
	public String myPage(){
		
		return "/user/myPage";
		
	}


}
