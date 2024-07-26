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



	@PostMapping("/search")
	@ResponseBody
	public List<CulturalPropertiesDTO> searchCulturalProperties(
			@RequestParam("page") int page,
			@RequestParam(required = false) String categoryName,
			@RequestParam(required = false) String culturalPropertiesName,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) String dynasty
	) {

		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		pageDTO.setStartEnd(pageDTO.getPage());

		System.out.println("검색pageDTO : " + pageDTO);


		return adminCulturalPropertiesService.searchCulturalProperties(pageDTO, categoryName, culturalPropertiesName, region, dynasty);
	}



}


