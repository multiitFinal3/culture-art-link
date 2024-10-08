package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dao.CulturalPropertiesKeywordDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import com.multi.culture_link.common.keyword.service.KeywordExtractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/cultural-properties-regulate")
public class AdminCulturalPropertiesController {


	@Autowired
	private AdminCulturalPropertiesService adminCulturalPropertiesService;

	@Autowired
	public AdminCulturalPropertiesController(AdminCulturalPropertiesService adminCulturalPropertiesService) {
		this.adminCulturalPropertiesService = adminCulturalPropertiesService;
	}


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




	@GetMapping("/select")
	@ResponseBody
	public ArrayList<CulturalPropertiesDTO> selectDB(Model model, @RequestParam("page") int page) {

		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);


		pageDTO.setStartEnd(pageDTO.getPage());
		System.out.println("pageDTO : " + pageDTO);



		ArrayList<CulturalPropertiesDTO> list = new ArrayList<CulturalPropertiesDTO>();
		list = (ArrayList<CulturalPropertiesDTO>) adminCulturalPropertiesService.selectDB(pageDTO);


		System.out.println("가져온 리스트 : " + list);

		Object count = null;
		model.addAttribute("count", count);

		return list; // 모든 문화재 데이터 가져오기
	}





	@GetMapping("/findListPage")
	@ResponseBody
	public int findListPage(@RequestParam(value = "page", defaultValue = "1") int page,
							@RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage
							){


		// 전체 데이터 개수 가져오기
		int count = adminCulturalPropertiesService.selectCount();

		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) count / itemsPerPage);

		System.out.println("count :" + count);
		System.out.println("total pages!! : " + totalPages);

		return totalPages;

	}


	@PostMapping("/addDBData")
	@ResponseBody
	public void addDBData(@RequestBody ArrayList<Integer> numList){
		
		System.out.println("numList : " + numList.toString());
		
		adminCulturalPropertiesService.addDBData(numList);
		
	}



	@PostMapping("/count")
	@ResponseBody
	public int selectCount() {
		int count = 0;
		count = adminCulturalPropertiesService.selectCount();

		return count;
	}




	@PostMapping("/searchDB")
	@ResponseBody
	public List<CulturalPropertiesDTO> searchDBCulturalProperties(
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "dynasty", required = false) String dynasty,
			@RequestParam("page") int page) {
		return adminCulturalPropertiesService.searchDBCulturalProperties(category, name, region, dynasty, page);
	}

	@GetMapping("/searchCount")
	@ResponseBody
	public int searchCountCulturalProperties(
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "dynasty", required = false) String dynasty) {
		return adminCulturalPropertiesService.searchCountCulturalProperties(category, name, region, dynasty);
	}



	@PostMapping("/deleteDBData")
	@ResponseBody
	public void deleteDBData(@RequestBody ArrayList<Integer> check) {

		System.out.println("check : " + check.toString());

		adminCulturalPropertiesService.deleteDBData(check);

	}


	@PostMapping("/updateDBData")
	@ResponseBody
	public void updateDBData(@RequestBody ArrayList<CulturalPropertiesDTO> data) {

		System.out.println("update data : " + data);

		adminCulturalPropertiesService.updateDBData(data);

	}





	//////------------------------api 검색 보류

//	@PostMapping("/searchAPI")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchAPIDataFilter(
//			@RequestParam int pageIndex,
//			@RequestParam(required = false) String categoryName,
//			@RequestParam(required = false) String culturalPropertiesName,
//			@RequestParam(required = false) String region,
//			@RequestParam(required = false) String dynasty) {
//
//
//		System.out.println("검색pageIndex: " + pageIndex);
//		System.out.println("검색categoryName: " + categoryName);
//		System.out.println("검색culturalPropertiesName: " + culturalPropertiesName);
//		System.out.println("검색region: " + region);
//		System.out.println("검색dynasty: " + dynasty);
//
//		// API 호출 및 필터링 처리
//		List<CulturalPropertiesDTO> filteredList = adminCulturalPropertiesService.searchAPIDataFilter(pageIndex, categoryName, culturalPropertiesName, region, dynasty);
//		return filteredList;
//	}


	//////------------------------api 검색 보류 //2222//


	// @PostMapping("/searchAPI")
	// @ResponseBody
	// public List<CulturalPropertiesDTO> searchAPIDataFilter(
	// 		@RequestParam("pageIndex") int pageIndex,
	// 		@RequestParam("categoryName") String categoryName,
	// 		@RequestParam("culturalPropertiesName") String culturalPropertiesName,
	// 		@RequestParam("region") String region,
	// 		@RequestParam("dynasty") String dynasty) {

	// 	System.out.println("들어옴...");
	// 	System.out.println(pageIndex);
	// 	System.out.println(categoryName);
	// 	System.out.println(culturalPropertiesName);
	// 	System.out.println(region);
	// 	System.out.println(dynasty);

	// 	// API 호출 및 필터링 처리
	// 	List<CulturalPropertiesDTO> filteredList = adminCulturalPropertiesService.searchAPIDataFilter(pageIndex, categoryName, culturalPropertiesName, region, dynasty);

	// 	System.out.println("controller searchAPIDataFilter : " + filteredList);


	// 	return filteredList;
	// }
	
	
	
	@PostMapping("/findtotalDBData")
	@ResponseBody
	public ArrayList<CulturalPropertiesDTO> findtotalDBData(){
		
		ArrayList<CulturalPropertiesDTO> list = adminCulturalPropertiesService.findtotalDBData();
		
		System.out.println("total db : "  + list);
		
		return list;
		
	}





	@GetMapping("/keywords")
	public ResponseEntity<List<KeywordDTO>> getKeywords(
			@RequestParam(defaultValue = "5") int limit,
			@RequestParam(required = false) List<Integer> excludedKeywordIds) {
		List<KeywordDTO> keywords = adminCulturalPropertiesService.getKeywords(excludedKeywordIds, limit);
		return ResponseEntity.ok(keywords);
	}






}


