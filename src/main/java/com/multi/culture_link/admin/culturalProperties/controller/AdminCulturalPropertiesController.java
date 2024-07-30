package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

//	@PostMapping("/search")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchCulturalProperties(
//			@RequestParam("page") int page,
//			@RequestParam(value = "category", required = false) String category,
//			@RequestParam(value = "name", required = false) String name,
//			@RequestParam(value = "region", required = false) String region,
//			@RequestParam(value = "dynasty", required = false) String dynasty
////			@RequestParam(required = false) String category,
////			@RequestParam(required = false) String name,
////			@RequestParam(required = false) String region,
////			@RequestParam(required = false) String dynasty
//	) {
//
//		PageDTO pageDTO = new PageDTO();
//		pageDTO.setPage(page);
//		pageDTO.setStartEnd(pageDTO.getPage());
//
//		System.out.println("검색pageDTO : " + pageDTO);
//
//
//		CulturalPropertiesDTO culturalPropertiesDTO = new CulturalPropertiesDTO();
//		culturalPropertiesDTO.setCategoryName(category);
//
////		FestivalDTO festivalDTO = new FestivalDTO();
////
////		com.multi.culture_link.festival.model.dto.PageDTO pageDTO2 = new com.multi.culture_link.festival.model.dto.PageDTO();
////		pageDTO2.setPage(page);
////		pageDTO2.setStartEnd(pageDTO2.getPage());
////
////		festivalDTO.setPageDTO(pageDTO2);
////
//
//
//		return adminCulturalPropertiesService.searchCulturalProperties(pageDTO, category, name, region, dynasty);
//
////		return adminCulturalPropertiesService.searchCulturalProperties(festivalDTO)
	}



	@PostMapping("/searchDB")
	@ResponseBody
	public List<CulturalPropertiesDTO> searchDBCulturalProperties(
			@RequestParam("page") int page,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "dynasty", required = false) String dynasty
//			@RequestParam(required = false) String category,
//			@RequestParam(required = false) String name,
//			@RequestParam(required = false) String region,
//			@RequestParam(required = false) String dynasty
	) {

		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		pageDTO.setStartEnd(pageDTO.getPage());

		System.out.println("검색pageDTO : " + pageDTO);
		System.out.println("controller- category : " + category);
		System.out.println(name);
		System.out.println(region);
		System.out.println(dynasty);
		CulturalPropertiesDTO culturalPropertiesDTO = new CulturalPropertiesDTO();
		culturalPropertiesDTO.setCategoryName(category);

//		FestivalDTO festivalDTO = new FestivalDTO();
//
//		com.multi.culture_link.festival.model.dto.PageDTO pageDTO2 = new com.multi.culture_link.festival.model.dto.PageDTO();
//		pageDTO2.setPage(page);
//		pageDTO2.setStartEnd(pageDTO2.getPage());
//
//		festivalDTO.setPageDTO(pageDTO2);
//
		category = category.trim();
		name= name.trim();
//		region = region.trim().replace(",","");
//		dynasty = dynasty.trim().replace(",","");
		region = region.trim();
		dynasty = dynasty.trim();


//		int start = pageDTO.getStart();
//		int end = pageDTO.getEnd();
//
//		System.out.println("start : " + start);
//		System.out.println("end : " + end);
//		System.out.println();
//
//		culturalPropertiesDTO.setPageDTO(pageDTO);
//		culturalPropertiesDTO.setStart(start);
//		culturalPropertiesDTO.setEnd(end);


		return adminCulturalPropertiesService.searchDBCulturalProperties(pageDTO, category, name, region, dynasty);

//		return adminCulturalPropertiesService.searchCulturalProperties(festivalDTO)
	}


//    // 페이지안뜸
//	@PostMapping("/searchDB")
//	@ResponseBody
//	public List<CulturalPropertiesDTO> searchDBCulturalProperties(
//			@RequestParam("page") int page,
//			@RequestParam(value = "category", required = false) String categoryName,
//			@RequestParam(value = "name", required = false) String culturalPropertiesName,
//			@RequestParam(value = "region", required = false) String region,
//			@RequestParam(value = "dynasty", required = false) String dynasty
//	) {
//
//		PageDTO pageDTO = new PageDTO();
//		pageDTO.setPage(page);
//		pageDTO.setStartEnd(pageDTO.getPage());
//		int start = pageDTO.getStart();
//		int end = pageDTO.getEnd();
//
//
//
//
//
//
//
//		CulturalPropertiesDTO culturalPropertiesDTO = new CulturalPropertiesDTO();
//		culturalPropertiesDTO.setCategoryName(categoryName);
//		culturalPropertiesDTO.setCulturalPropertiesName(culturalPropertiesName);
//		culturalPropertiesDTO.setRegion(region);
//		culturalPropertiesDTO.setDynasty(dynasty);
//		culturalPropertiesDTO.setPageDTO(pageDTO);
//		culturalPropertiesDTO.setStart(start);
//		culturalPropertiesDTO.setEnd(end);
//
//
//
//		System.out.println("service-categoryName : " + categoryName);
//		System.out.println("culturalPropertiesName : " + culturalPropertiesName);
//		System.out.println("region : " + region);
//		System.out.println("dynasty : " + dynasty);
//		System.out.println("start : " + start);
//		System.out.println("end : " + end);
//		System.out.println();
//
//
////		categoryName = categoryName.trim();
////		culturalPropertiesName= culturalPropertiesName.trim();
//////		region = region.trim().replace(",","");
//////		dynasty = dynasty.trim().replace(",","");
////		region = region.trim();
////		dynasty = dynasty.trim();
//
//
//		System.out.println("dto : " + culturalPropertiesDTO);
//
//
//		List<CulturalPropertiesDTO> list = adminCulturalPropertiesService.searchDBCulturalProperties(culturalPropertiesDTO);
//
//		// 리스트의 개수 가져오기
//		int count = list.size();
//		System.out.println("컨트롤러 리스트의 개수: " + count);
//
//
//		return list;
//	}





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




}


