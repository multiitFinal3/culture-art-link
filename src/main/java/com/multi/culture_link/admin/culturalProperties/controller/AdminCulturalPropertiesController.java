package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
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



//	@PostMapping("/searchAPI")
//	public String searchAPI(Model model,
//										   @ModelAttribute("searchForm") CulturalPropertiesDTO searchForm) {
//
//		// CulturalPropertiesDTO를 통해 다중 검색 수행
//		List<CulturalPropertiesDTO> culturalPropertiesList = adminCulturalPropertiesService.searchAPI(searchForm);
//
//		// 검색 결과를 모델에 추가
//		model.addAttribute("culturalPropertiesList", culturalPropertiesList);
//
//		return "/admin/culturalProperties/list";
//	}

//	// 검색 기능을 처리하는 핸들러 메소드
//	@PostMapping("/searchAPI")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchAPI(@ModelAttribute("searchForm") CulturalPropertiesDTO searchForm) {
//		// CulturalPropertiesDTO를 통해 다중 검색 수행
//		List<CulturalPropertiesDTO> culturalPropertiesList = adminCulturalPropertiesService.searchAPI(searchForm);
//
//		return culturalPropertiesList; // JSON 형식으로 검색 결과 반환
//	}

//---------------
//	@PostMapping("/searchAPI")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchAPI(
//			@RequestParam(value = "categoryName", required = false) String categoryName,
//			@RequestParam(value = "searchName", required = false) String searchName,
//			@RequestParam(value = "searchRegion", required = false) String searchRegion,
//			@RequestParam(value = "searchDynasty", required = false) String searchDynasty) {
//
//		CulturalPropertiesDTO searchForm = new CulturalPropertiesDTO();
//		searchForm.setCategoryName(categoryName);
//		searchForm.setCulturalPropertiesName(searchName);
//		searchForm.setRegion(searchRegion);
//		searchForm.setDynasty(searchDynasty);
//
//		System.out.println("searchForm- category : " + categoryName);
//		System.out.println(searchName);
//		System.out.println(searchRegion);
//		System.out.println(searchDynasty);
//
//		List<CulturalPropertiesDTO> culturalPropertiesList = adminCulturalPropertiesService.searchAPI(searchForm);
//
//		return culturalPropertiesList; // JSON 형식으로 검색 결과 반환
//	}


	//-----------------

//	@PostMapping("/searchAPI")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchAPI(@RequestBody CulturalPropertiesDTO searchForm) {
//		List<CulturalPropertiesDTO> culturalPropertiesList = adminCulturalPropertiesService.searchAPI(searchForm);
//		return culturalPropertiesList;
//	}





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


//	@GetMapping("/searchAPI")
//	public List<CulturalPropertiesDTO> searchAPICulturalProperties(
//			@RequestParam(required = false) String categoryName,
//			@RequestParam(required = false) String culturalPropertiesName,
//			@RequestParam(required = false) String region,
//			@RequestParam(required = false) String dynasty) {
//
//		// 서비스를 통해 검색 기준에 맞는 CulturalPropertiesDTO 목록을 가져온다
//		List<CulturalPropertiesDTO> searchResult = adminCulturalPropertiesService.searchAPICulturalProperties(
//				categoryName, culturalPropertiesName, region, dynasty);
//
//		return searchResult;
//	}

	//----------------------- 수정중


//	@GetMapping("/searchAPI")
//	public ResponseEntity<Page<CulturalPropertiesDTO>> searchAPICulturalProperties(
//			@RequestParam(required = false) String categoryName,
//			@RequestParam(required = false) String culturalPropertiesName,
//			@RequestParam(required = false) String region,
//			@RequestParam(required = false) String dynasty,
//			@RequestParam(defaultValue = "0") int pageIndex,
//			@RequestParam(defaultValue = "10") int pageSize) {
//
//		System.out.println("API 호출됨: categoryName=" + categoryName + ", culturalPropertiesName=" + culturalPropertiesName
//				+ ", region=" + region + ", dynasty=" + dynasty + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize);
//
//		try {
//			// 서비스 호출
//			Page<CulturalPropertiesDTO> searchResult = adminCulturalPropertiesService.searchAPICulturalProperties(
//					categoryName, culturalPropertiesName, region, dynasty, PageRequest.of(pageIndex, pageSize));
//			return ResponseEntity.ok().body(searchResult);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("API 응답 처리 중 오류 발생: " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

	//////------------------------

	@PostMapping("/searchAPI")
	@ResponseBody
	public List<CulturalPropertiesDTO> searchAPIDataFilter(
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("categoryName") String categoryName,
			@RequestParam("culturalPropertiesName") String culturalPropertiesName,
			@RequestParam("region") String region,
			@RequestParam("dynasty") String dynasty) {
		
		System.out.println("들어옴...");
		System.out.println(pageIndex);
		System.out.println(categoryName);
		System.out.println(culturalPropertiesName);
		System.out.println(region);
		System.out.println(dynasty);
		
		// API 호출 및 필터링 처리
		List<CulturalPropertiesDTO> filteredList = adminCulturalPropertiesService.searchAPIDataFilter(pageIndex, categoryName, culturalPropertiesName, region, dynasty);
		
		System.out.println("controller searchAPIDataFilter : " + filteredList);
		
		
		return filteredList;
	}


//	@GetMapping("/searchAPI")
//	public ResponseEntity<Page<CulturalPropertiesDTO>> searchAPICulturalProperties(
//			@RequestParam(required = false) String categoryName,
//			@RequestParam(required = false) String culturalPropertiesName,
//			@RequestParam(defaultValue = "0") int pageIndex,
//			@RequestParam(defaultValue = "10") int pageSize) {
//
//		try {
//			// 서비스를 통해 검색 기준에 맞는 CulturalPropertiesDTO 페이지를 가져온다
//			Page<CulturalPropertiesDTO> searchResult = adminCulturalPropertiesService.searchAPICulturalProperties(
//					categoryName, culturalPropertiesName, PageRequest.of(pageIndex, pageSize));
//
//			return ResponseEntity.ok().body(searchResult);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}


//	@PostMapping("/searchAPI")
//	public ResponseEntity<List<CulturalPropertiesDTO>> searchAPI(
//			@RequestParam int pageIndex,
//			@RequestParam(required = false) String categoryName,
//			@RequestParam(required = false) String culturalPropertiesName,
//			@RequestParam(required = false) String region,
//			@RequestParam(required = false) String dynasty) {
//
//		List<CulturalPropertiesDTO> resultList = adminCulturalPropertiesService.searchAPIData(pageIndex, categoryName, culturalPropertiesName, region, dynasty);
//		return ResponseEntity.ok(resultList);
//	}





}


