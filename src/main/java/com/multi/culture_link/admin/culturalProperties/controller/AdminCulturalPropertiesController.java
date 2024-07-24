package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin/cultural-properties-regulate")
public class AdminCulturalPropertiesController {


	@Autowired
	private AdminCulturalPropertiesService adminCulturalPropertiesService;


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

		System.out.println("list : " + list);

		return list;


	}


//	@GetMapping("/select")
//	@ResponseBody
//	public ArrayList<CulturalPropertiesDTO> selectDB(Model model) {
//
//		ArrayList<CulturalPropertiesDTO> list = new ArrayList<CulturalPropertiesDTO>();
//		list = (ArrayList<CulturalPropertiesDTO>) adminCulturalPropertiesService.selectDB();
//
//		System.out.println("가져온 리스트 : " + list);
//
//		Object count = null;
//		model.addAttribute("count", count);
//
//
//		return list; // 모든 문화재 데이터 가져오기
//	}


	@GetMapping("/select")
	@ResponseBody
	public ArrayList<CulturalPropertiesDTO> selectDB(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage,
			Model model) {

		// 전체 데이터 개수 가져오기
		int totalCount = adminCulturalPropertiesService.selectCount();

		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / itemsPerPage);

		// startIndex, endIndex 계산
		int startIndex = (page - 1) * itemsPerPage;
		int endIndex = Math.min(startIndex + itemsPerPage, totalCount);


		// 전체 문화재 데이터 가져오기
		ArrayList<CulturalPropertiesDTO> list = new ArrayList<CulturalPropertiesDTO>();
		list = (ArrayList<CulturalPropertiesDTO>) adminCulturalPropertiesService.selectDB(startIndex, endIndex);



		// 모델에 필요한 데이터 추가
		model.addAttribute("list", list); // 전체 데이터
		model.addAttribute("currentPage", page); // 현재 페이지 번호
		model.addAttribute("totalPages", totalPages); // 전체 페이지 수
		
		System.out.println("totalPages : " + totalPages);

		return list; // 페이지네이션을 포함한 문화재 데이터 반환
	}

//	@PostMapping("/count")
//	@ResponseBody
//	public int selectCount() {
//		int count = 0;
//		count = adminCulturalPropertiesService.selectCount();
//
//		return count;
//	}


	@PostMapping("/addDBData")
	@ResponseBody
	public void addDBData(@RequestBody ArrayList<Integer> numList){
		
		System.out.println("numList : " + numList.toString());
		
		adminCulturalPropertiesService.addDBData(numList);
		
	}

//	@GetMapping("/properties")
//	public String getProperties(@RequestParam(value = "page", defaultValue = "1") int page,
//								@RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage,
//								Model model) {
//
//		// 전체 문화재 데이터 가져오기
//		ArrayList<CulturalPropertiesDTO> allProperties = adminCulturalPropertiesService.selectAllProperties();
//
//		// 페이징을 위한 서브리스트 계산
//		int startIndex = (page - 1) * itemsPerPage;
//		int endIndex = Math.min(startIndex + itemsPerPage, allProperties.size());
//		ArrayList<CulturalPropertiesDTO> properties = new ArrayList<>(allProperties.subList(startIndex, endIndex));
//
//		// 전체 페이지 수 계산
//		int totalPages = (int) Math.ceil((double) allProperties.size() / itemsPerPage);
//
//		model.addAttribute("properties", properties);
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPages", totalPages);
//
//		return "properties"; // properties.html로 포워딩
//	}








}


