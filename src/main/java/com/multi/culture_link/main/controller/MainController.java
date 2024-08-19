package com.multi.culture_link.main.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.main.service.MainService;
import com.multi.culture_link.performance.service.PerformanceService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 메인화면 관련 컨트롤러 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);


	@Value("${API-KEY.naverClientId}")
	private String naverClientId;
	
	@Value("${API-KEY.naverClientSecret}")
	private String naverClientSecret;
	
	private final MainService mainService;
	private final PerformanceService performanceService;



	public MainController(MainService mainService,
						  PerformanceService performanceService) {
		this.mainService = mainService;
		this.performanceService = performanceService;

	}
	
	/**
	 *
	 * 메인화면으로 이동
	 * @return 메인화면 주소
	 */
	@GetMapping(value = {"/", "/home"})
	public String main(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
		model.addAttribute("naverClientId", naverClientId);
		model.addAttribute("naverClientSecret", naverClientSecret);

		if (user != null) {
			int userId = user.getUserId();
			try {
				List<PerformanceDTO> recommendedPerformances = performanceService.getRecommendedPerformances(userId);
				if (recommendedPerformances != null && !recommendedPerformances.isEmpty()) {
					Collections.shuffle(recommendedPerformances);
					List<PerformanceDTO> top4Recommendations = recommendedPerformances.stream().limit(4).collect(Collectors.toList());
					model.addAttribute("recommendedPerformances", top4Recommendations);
				} else {
					model.addAttribute("message", "추천 공연이 없습니다.");
				}
			} catch (Exception e) {
				logger.error("Error fetching recommended performances for user ID: " + userId, e);
				model.addAttribute("message", "추천 공연을 불러오는 중 오류가 발생했습니다.");
			}
		} else {
			model.addAttribute("message", "로그인 후 추천 공연을 확인할 수 있습니다.");
		}

		return "main/main";
	}




	/**
	 * 지역이름을 주면 센터 좌표로 쓸 좌표를 반환
	 * @param regionName
	 * @return
	 */
	@PostMapping("/main/findCenterPositionByRegion")
	@ResponseBody
	public HashMap<String, Double> findCenterPositionByRegion(@RequestParam("regionName") String regionName){
		
		HashMap<String, Double> position = null;
		try {
			position = mainService.findCenterPositionByRegion(regionName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return position;
	}
	
	
	
	/**
	 * 해당 지역의 모든 카테고리 리스트를 반환
	 * @param regionId
	 * @return
	 */
	@PostMapping("/main/findAllCultureCategoryByRegion")
	@ResponseBody
	public HashMap<String, ArrayList> findAllCultureCategoryByRegion(@RequestParam("regionId") int regionId){
		
		HashMap<String, ArrayList> categoryMap = null;
		try {
			categoryMap = mainService.findAllCultureCategoryByRegion(regionId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return categoryMap;
	}
	
	
	/**
	 *
	 * 지도화면으로 이동
	 * @return
	 */
	@GetMapping("/main/mapDetail")
	public String mapDetail(Model model) {
		
		model.addAttribute("naverClientId", naverClientId);
		model.addAttribute("naverClientSecret", naverClientSecret);
		
		return "main/mapDetail";
		
	}
	
	
	/**
	 *
	 * 달력화면으로 이동
	 * @return
	 */
	@GetMapping("/main/calendar")
	public String calendar(Model model) {
		
		model.addAttribute("naverClientId", naverClientId);
		model.addAttribute("naverClientSecret", naverClientSecret);
		
		return "main/calendar";
		
	}
	
	
	
	
}
