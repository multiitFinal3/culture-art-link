package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin/cultural-properties-regulate")
public class AdminCulturalPropertiesController {


	@Autowired
	private AdminCulturalPropertiesService adminCulturalPropertiesService;

//	@GetMapping
//	public String culturalPropertiesManage(Model model, int page) {
//		List<CulturalPropertiesDTO> culturalPropertiesList = adminCulturalPropertiesService.fetchApiData(page);
//		model.addAttribute("culturalPropertiesList", culturalPropertiesList);
//		return "/admin/culturalProperties/culturalPropertiesRegulate";
//	}

	@GetMapping
	public String culturalPropertiesManage() {


		return "/admin/culturalProperties/culturalPropertiesRegulate";
	}


	@PostMapping("/fetchApiData")
	@ResponseBody
	public List<CulturalPropertiesDTO> culturalPropertiesList(@RequestParam("pageIndex") int pageIndex, Model model) {

		System.out.println("list확인");
		List<CulturalPropertiesDTO> list = adminCulturalPropertiesService.fetchApiData(pageIndex);
		model.addAttribute("culturalPropertiesList", list);
//		try {
//			list = adminCulturalPropertiesService.fetchApiData(pageIndex);
//
//
//
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}


		System.out.println("list : " + list);

		return list;


	}

//	@PostMapping("/insert")
//	@ResponseBody
//	public String insertDB(@RequestBody List<String> checkedItems) {
//		try {
//			for (String index : checkedItems) {
//				// index를 사용하여 CulturalPropertiesDTO 생성
//				CulturalPropertiesDTO dto = new CulturalPropertiesDTO();
//				dto.setId(Integer.parseInt(index)); // 예시로 id 설정
//
//				// 필요한 경우 다른 필드도 설정 가능
//
//				// CulturalPropertiesDTO를 사용하여 DB에 추가
//				adminCulturalPropertiesService.insertDB(dto);
//			}
//			return "문화재가 DB에 성공적으로 추가되었습니다.";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "DB 추가 중 오류가 발생했습니다.";
//		}
//	}

	@PostMapping("/insert")
	public ResponseEntity<String> insertDB(@RequestBody List<CulturalPropertiesDTO> checkedItem){
//		for (String index : checkedItem) {
//			CulturalPropertiesDTO dto = new CulturalPropertiesDTO();
//			dto.setId(Integer.parseInt(index));
			adminCulturalPropertiesService.insertDB(checkedItem);
//		}
		return ResponseEntity.ok("문화재가 DB에 성공적으로 추가되었습니다.");
	}


	@GetMapping("/getDBData")
	public List<CulturalPropertiesDTO> selectDB() {

		return adminCulturalPropertiesService.selectDB(); // 모든 문화재 데이터 가져오기
	}







}


