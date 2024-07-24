package com.multi.culture_link.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;
import com.multi.culture_link.festival.service.FestivalService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * 일반회원 축제 관련 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Controller
@RequestMapping("/festival")
public class FestivalController {
	
	private final AdminFestivalService adminFestivalService;
	private final FestivalService festivalService;
	
	public FestivalController(AdminFestivalService adminFestivalService, FestivalService festivalService) {
		this.adminFestivalService = adminFestivalService;
		this.festivalService = festivalService;
	}
	
	/**
	 * 메뉴바에서 페스티벌 리스트 화면으로 이동
	 *
	 * @param user  유저의 정보
	 * @param model 화면으로 보내는 정보를 담는 모델
	 * @return 페스티벌 리스트 주소 반환
	 */
	@GetMapping("/festival-list")
	public String festivalListPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {

//		System.out.println("@AuthenticationPrincipal : " + user.toString());
		model.addAttribute("user", user.getUser());
		
		return "/festival/festivalList";
		
	}
	
	/**
	 * 축제 상세 화면으로 이동
	 *
	 * @param festivalId 해당 축제 DB 아이디
	 * @param model      화면에 정보를 담아감
	 * @return 화면으로 이동
	 */
	@GetMapping("/festival-detail")
	public String festivalDetail(@RequestParam("festivalId") int festivalId, Model model) {
		
		try {
			FestivalDTO festivalDTO = adminFestivalService.findDBFestivalByFestivalId(festivalId);
			model.addAttribute("festival", festivalDTO);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "/festival/festivalDetail";
		
	}
	
	/**
	 * 찜이 안되어있는 축제에 대해 찜매핑 테이블에 인서트
	 *
	 * @param festivalId
	 * @param user
	 */
	@PostMapping("/insertUserLoveFestival")
	@ResponseBody
	public String insertUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		String sortCode = "L";
		
		UserFestivalLoveHateMapDTO map1 = new UserFestivalLoveHateMapDTO();
		map1.setUserId(userId);
		map1.setSortCode(sortCode);
		map1.setFestivalId(festivalId);
		
		ArrayList<FestivalKeywordDTO> keywordList = null;
		
		try {
			
			festivalService.insertUserLoveFestival(map1);
			
			
			keywordList = festivalService.findFestivalKeywordByFestivalId(festivalId);
			
			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
			
			for (FestivalKeywordDTO keywordDTO : keywordList) {
				
				if (!list.contains(keywordDTO)) {
					list.add(keywordDTO);
				}
				
			}
			
			for (FestivalKeywordDTO keywordDTO : list) {
				
				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
				userMap.setUserId(userId);
				userMap.setSortCode(sortCode);
				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
				
				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
				
				if (userMap2 == null) {
					
					userMap.setFestivalCount(1);
					festivalService.insertUserKeywordMap(userMap);
					
				} else {
					
					int count = userMap2.getFestivalCount() + 1;
					userMap2.setFestivalCount(count);
					System.out.println("update : " + userMap2);
					festivalService.updateUserKeywordMap(userMap2);
					
				}
				
			}
			
			return "찜하기 및 관련 키워드 추가 성공";
			
			
		} catch (Exception e) {
			
			return "에러 발생";
		}
		
		
	}
	
	
	/**
	 * 유저가 찜한 목록을 표시하기 위해 리스트를 가져옴
	 * @param user
	 * @return 축제 DB 번호
	 */
	@PostMapping("/findLoveList")
	@ResponseBody
	public ArrayList<Integer> findLoveList(@AuthenticationPrincipal VWUserRoleDTO user){
		
		int userId = user.getUserId();
		
		ArrayList<Integer> list = null;
		try {
			list = festivalService.findLoveList(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("love list : " + list);
		
		return list;
		
	}
	
	
	/**
	 * 찜한 목록을 삭제하고 그와 연관된 키워드도 전부 삭제
	 * @param festivalId
	 * @param user
	 * @return
	 */
	@PostMapping("/deleteUserLoveFestival")
	@ResponseBody
	public String deleteUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user){
		
		try {
			
			int userId = user.getUserId();
			String sortCode = "L";
			
			UserFestivalLoveHateMapDTO userMap1 = new UserFestivalLoveHateMapDTO();
			userMap1.setUserId(userId);
			userMap1.setFestivalId(festivalId);
			userMap1.setSortCode(sortCode);
			festivalService.deleteUserLoveFestival(userMap1);
			
			ArrayList<FestivalKeywordDTO> keyList = festivalService.findFestivalKeywordByFestivalId(festivalId);
			
			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
			
			for (FestivalKeywordDTO keywordDTO : keyList) {
				
				if (!list.contains(keywordDTO)) {
					list.add(keywordDTO);
				}
				
			}
			
			for (FestivalKeywordDTO keywordDTO : list) {
				
				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
				userMap.setUserId(userId);
				userMap.setSortCode(sortCode);
				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
				
				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
				
				if (userMap2 != null) {
					
					festivalService.deleteUserKeywordMap(userMap2);
					
				}
				
			}
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return "찜 목록 및 관련 키워드 삭제 성공!";
	
	}
	
}
