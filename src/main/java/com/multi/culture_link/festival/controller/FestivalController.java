package com.multi.culture_link.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.*;
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
	public String festivalDetail(@RequestParam("festivalId") int festivalId, Model model, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			FestivalDTO festivalDTO = adminFestivalService.findDBFestivalByFestivalId(festivalId);
			model.addAttribute("festival", festivalDTO);
			
			int userId = user.getUserId();
			model.addAttribute("userId", userId);
			
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
					System.out.println("count : " + count);
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
	 *
	 * @param user
	 * @return 축제 DB 번호
	 */
	@PostMapping("/findLoveList")
	@ResponseBody
	public ArrayList<Integer> findLoveList(@AuthenticationPrincipal VWUserRoleDTO user) {
		
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
	 *
	 * @param festivalId
	 * @param user
	 * @return
	 */
	@PostMapping("/deleteUserLoveFestival")
	@ResponseBody
	public String deleteUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
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
	
	
	/**
	 * 관심없음이 안되어있는 축제에 대해 찜 관심없음 매핑 테이블에 인서트
	 *
	 * @param festivalId
	 * @param user
	 */
	@PostMapping("/insertUserHateFestival")
	@ResponseBody
	public String insertUserHateFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		String sortCode = "H";
		
		UserFestivalLoveHateMapDTO map1 = new UserFestivalLoveHateMapDTO();
		map1.setUserId(userId);
		map1.setSortCode(sortCode);
		map1.setFestivalId(festivalId);
		
		ArrayList<FestivalKeywordDTO> keywordList = null;
		
		try {
			
			festivalService.insertUserHateFestival(map1);
			
			
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
			
			return "관심없음 및 관련 관심없음 키워드 추가 성공";
			
			
		} catch (Exception e) {
			
			return "에러 발생";
		}
		
		
	}
	
	
	/**
	 * 유저가 관심없음한 목록을 표시하기 위해 리스트를 가져옴
	 *
	 * @param user
	 * @return 축제 DB 번호
	 */
	@PostMapping("/findHateList")
	@ResponseBody
	public ArrayList<Integer> findHateList(@AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		
		ArrayList<Integer> list = null;
		try {
			list = festivalService.findHateList(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("hate list : " + list);
		
		return list;
		
	}
	
	
	/**
	 * 관심없음한 목록을 삭제하고 그와 연관된 키워드도 전부 삭제
	 *
	 * @param festivalId
	 * @param user
	 * @return
	 */
	@PostMapping("/deleteUserHateFestival")
	@ResponseBody
	public String deleteUserHateFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			
			int userId = user.getUserId();
			String sortCode = "H";
			
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
		
		
		return "관심없음 목록 및 관련 키워드 삭제 성공!";
		
	}
	
	
	/**
	 * 해당 축제의 컨텐트 키워드를 반환
	 * @param festivalId
	 * @return
	 */
	@PostMapping("findContentKeywordListByFestivalId")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findContentKeywordListByFestivalId(@RequestParam("festivalId") int festivalId){
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		try {
			list = festivalService.findContentKeywordListByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	
	@PostMapping("/findTimeIdByFestivalId")
	@ResponseBody
	public TimeDTO findTimeIdByFestivalId(@RequestParam("festivalId") int festivalId){
		
		TimeDTO timeDTO = null;
		try {
			timeDTO = festivalService.findTimeIdByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return timeDTO;
		
	}
	
	
	@PostMapping("/findFestivalReviewListByFestivalId")
	@ResponseBody
	public ArrayList<VWUserReviewDataDTO> findFestivalReviewListByFestivalId(@RequestParam("festivalId") int festivalId){
		
		ArrayList<VWUserReviewDataDTO> list = null;
		try {
			list = festivalService.findFestivalReviewListByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	
}
