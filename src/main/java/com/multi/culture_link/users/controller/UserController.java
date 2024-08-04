package com.multi.culture_link.users.controller;


import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.service.RegionService;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
	
	
	private final UserService userService;
	private final RegionService regionService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(UserService userService, RegionService regionService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.regionService = regionService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@GetMapping("/signUp")
	public String signUp() {
		
		return "/user/signUp";
	}
	
	@PostMapping("/signUp")
	public void signUpPost(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("userId") int userId, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tel") String tel, @RequestParam("userAge") int userAge, @RequestParam("gender") String gender, @RequestParam("regionId") int regionId) {
		
		// 회원 users/ admin 넣는 것도 추가할 것 = > 매핑 테이블에도 조인으로 추가하기
		int result = 0;
		UserDTO userDTO = new UserDTO();
		
		String fileUUIDName = UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();
		String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/user/userProfile/";
		try {
			Files.createDirectories(Paths.get(uploadDir));
			File savedFile = new File(uploadDir + fileUUIDName);
			
			System.out.println("savedFile.getAbsolutePath() : " + savedFile.getAbsolutePath());
			System.out.println("savedFile.getName() : " + savedFile.getName());
			System.out.println("path : " + savedFile.getPath());
			System.out.println(System.getProperty("user.dir"));
			
			uploadFile.transferTo(savedFile);
			
			String attachment = uploadDir + fileUUIDName;
			
			int startIndex = attachment.indexOf("/img");
			System.out.println(startIndex);
			attachment = attachment.substring(startIndex);
			
			
			userDTO.setUserProfilePic(attachment);
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		userDTO.setUserId(userId);
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		userDTO.setUserName(userName);
		userDTO.setTel(tel);
		userDTO.setUserAge(userAge);
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
	public String login2() {
		
		return "redirect:/";
		
	}
	
	
	@GetMapping("/myPage")
	public String myPage() {
		
		return "/user/myPage";
		
	}
	
	@PostMapping("/signUp/findAllRegion")
	@ResponseBody
	public ArrayList<RegionDTO> findAllRegion() {
		
		ArrayList<RegionDTO> list = null;
		
		try {
			list = regionService.findAllRegion();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Map<String, ArrayList> map = new HashMap<String, ArrayList>();
		
		map.put("regionList", list);
		
		return list;
		
	}
	
	
}
