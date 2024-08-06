package com.multi.culture_link.users.controller;


import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.service.RegionService;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;
import com.multi.culture_link.festival.service.FestivalService;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import com.multi.culture_link.users.model.mapper.UserMapper;
import com.multi.culture_link.users.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private final UserMapper userMapper;
	private final FestivalService festivalService;
	private final RegionService regionService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(UserService userService, UserMapper userMapper, FestivalService festivalService, RegionService regionService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.userMapper = userMapper;
		this.festivalService = festivalService;
		this.regionService = regionService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	/**
	 * 회원가입 페이지로 이동
	 *
	 * @return
	 */
	@GetMapping("/signUp")
	public String signUp() {
		
		return "/user/signUp";
	}
	
	
	/**
	 * 회원가입 및 회원 선택 선호 키워드를 표시
	 *
	 * @param uploadFile
	 * @param email
	 * @param password
	 * @param userName
	 * @param tel
	 * @param userAge
	 * @param gender
	 * @param regionId
	 * @param festivalSelectKeyword
	 */
	@PostMapping("/signUp")
	public void signUpPost(@RequestParam("uploadFile") MultipartFile uploadFile, /*@RequestParam("userId") int userId,*/ @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tel") String tel, @RequestParam("userAge") int userAge, @RequestParam("gender") String gender, @RequestParam("regionId") int regionId, @RequestParam("festivalSelectKeyword") String festivalSelectKeyword) {
		
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
		
		
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		userDTO.setUserName(userName);
		userDTO.setTel(tel);
		userDTO.setUserAge(userAge);
		userDTO.setGender(gender);
		userDTO.setRegionId(regionId);
		
		System.out.println("userdto : " + userDTO);
		
		
		try {
			
			String encoded_pw = bCryptPasswordEncoder.encode(userDTO.getPassword());
			userDTO.setPassword(encoded_pw);
			userService.signUp(userDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		UserDTO user = null;
		try {
			user = userService.findUserByEmail(email);
			user.setRoleId("ROLE_ADMIN");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		int userId = user.getUserId();
		
		try {
			userService.insertRoleId(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		String[] list = festivalSelectKeyword.trim().split(" ");
		for (String s : list) {
			
			try {
				
				UserFestivalLoveHateMapDTO mapDTO = new UserFestivalLoveHateMapDTO();
				mapDTO.setFestivalKeywordId(s);
				mapDTO.setSortCode("L");
				mapDTO.setUserId(userId);
				mapDTO.setFestivalCount(15);
				festivalService.insertUserSelectKeyword(mapDTO);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		}
		
		
		System.out.println("회원가입 성공");
		
	}
	
	
	/**
	 * 로그인 후 홈페이지로 이동
	 *
	 * @return
	 */
	@PostMapping("/login2")
	public String login2() {
		
		return "redirect:/";
		
	}
	
	/**
	 * 마이페이지로 이동
	 *
	 * @return
	 */
	@GetMapping("/myPage")
	public String myPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
		
		UserDTO userDTO = user.getUserDTO();
		System.out.println("userdto" + userDTO);
		System.out.println("pr : " + user.getUserProfilePic());
		
		model.addAttribute("user", userDTO);
/*		model.addAttribute("gender", user.getGender());
		model.addAttribute("regionId", user.getRegionId());
		model.addAttribute("img", user.getUserProfilePic());*/
		
		
		return "/user/myPage";
		
	}
	
	
	/**
	 * 회원가입 페이지의 지역정보를 가져옴
	 *
	 * @return
	 */
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
	
	
	/**
	 * 회원가입 페이지의 지역정보를 가져옴
	 *
	 * @return
	 */
	@PostMapping("/deleteUserAccount")
	@ResponseBody
	public void deleteUserAccount(@AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			userService.deleteUserAccount(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	/**
	 * 회원정보 수정을 함
	 *
	 * @return
	 */
	@PostMapping("/updateUserAccount")
	@ResponseBody
	public void updateUserAccount(@AuthenticationPrincipal VWUserRoleDTO user, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tel") String tel, @RequestParam("userAge") int userAge, @RequestParam("gender") String gender, @RequestParam("regionId") int regionId) {
		
		String attachment = "";
		
		try {
			
			if (file != null && !file.isEmpty()) {
				String fileUUIDName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
				String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/user/userProfile/";
				
				
				System.out.println("???");
				
				Files.createDirectories(Paths.get(uploadDir));
				System.out.println("id : " + file);
				
				File savedFile = new File(uploadDir + fileUUIDName);
				
				System.out.println("savedFile.getAbsolutePath() : " + savedFile.getAbsolutePath());
				System.out.println("savedFile.getName() : " + savedFile.getName());
				System.out.println("path : " + savedFile.getPath());
				System.out.println(System.getProperty("user.dir"));
				
				file.transferTo(savedFile);
				
				attachment = uploadDir + fileUUIDName;
				int startIndex = attachment.indexOf("/img");
				attachment = attachment.substring(startIndex);
				
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(user.getUserId());
		userDTO.setUserProfilePic(attachment);
		userDTO.setEmail(email);
		
		System.out.println("입력된 비밀번호 : " + password);
		System.out.println("원래 비밀번호 : " + user.getPassword());
		
		if (!password.equals(user.getPassword())) {
			System.out.println("비밀번호 다름");
			String encryptedPassword = bCryptPasswordEncoder.encode(password);
			System.out.println("암호화 된 비밀번호 : " + encryptedPassword);
			userDTO.setPassword(encryptedPassword);
		} else {
			userDTO.setPassword(password);
		}
		
		
		userDTO.setUserName(userName);
		userDTO.setTel(tel);
		userDTO.setUserAge(userAge);
		userDTO.setGender(gender);
		userDTO.setRegionId(regionId);
		
		
		System.out.println("받아온 정보 : " + userDTO);
		
		try {
			userService.updateUserAccount(userDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		try {
//			UserDTO newUser = userService.findUserByEmail(email);
			VWUserRoleDTO newVWUser = new VWUserRoleDTO(userDTO);
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(newVWUser, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	/**
	 * 찜 관심없음 선택 키워드를 삽입
	 * @param user
	 * @param performanceKeyword
	 * @param exhibitionKeyword
	 * @param festivalKeyword
	 * @param culturalPropertiesKeyword
	 * @param loveOrHate
	 * @return
	 */
	@PostMapping("/insertUserBigLoveHateKeyword")
	public String insertUserBigLoveHateKeyword(@AuthenticationPrincipal VWUserRoleDTO user, @RequestParam(name = "performanceKeyword", required = false) String performanceKeyword, @RequestParam(name = "exhibitionKeyword", required = false) String exhibitionKeyword, @RequestParam(name = "festivalKeyword", required = false) String festivalKeyword, @RequestParam(name = "culturalPropertiesKeyword", required = false) String culturalPropertiesKeyword, @RequestParam(name = "loveOrHate", required = false) String loveOrHate) {
		
		System.out.println("lh : " + loveOrHate);
		System.out.println("fk : " + festivalKeyword);
		
		if (loveOrHate.equals("L")) {
			
			UserFestivalLoveHateMapDTO mapDTO = new UserFestivalLoveHateMapDTO();
			mapDTO.setUserId(user.getUserId());
			mapDTO.setSortCode("L");
			
			try {
				festivalService.deleteAllUserSelectFestivalKeyword(mapDTO);
				if ((!festivalKeyword.trim().equals("")) && (!festivalKeyword.isEmpty()) && (festivalKeyword!=null)){
					
					String[] festivalList = null;
					festivalList = festivalKeyword.trim().split(" ");
					
					for(String s : festivalList){
						
						mapDTO.setFestivalKeywordId(s);
						mapDTO.setFestivalCount(15);
						festivalService.insertUserSelectKeyword(mapDTO);
						
					}
					
					
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
			
			
			
		} else if (loveOrHate.equals("H")) {
		
		
		}
		
		return "redirect:/user/myPage";
	}
	
	
}
