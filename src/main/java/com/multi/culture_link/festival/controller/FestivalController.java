package com.multi.culture_link.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.service.FestivalService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
	 * @param festivalId 해당 축제 DB 아이디
	 * @param model 화면에 정보를 담아감
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
	 * @param festivalId
	 * @param user
	 */
	@PostMapping("/insertUserLoveFestival")
	@ResponseBody
	public void  insertUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user){
	
		int userId = user.getUserId();
		String sortCode = "L";
		
//		ArrayList<FestivalKeywordDTO> keywordList = festivalService.
	
	
	}
	
	
}
