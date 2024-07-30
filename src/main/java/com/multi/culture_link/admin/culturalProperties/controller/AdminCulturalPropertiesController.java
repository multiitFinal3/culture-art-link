package com.multi.culture_link.admin.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ArrayList<CulturalPropertiesDTO> selectDB(Model model) {
		
		ArrayList<CulturalPropertiesDTO> list = new ArrayList<CulturalPropertiesDTO>();
		list = (ArrayList<CulturalPropertiesDTO>) adminCulturalPropertiesService.selectDB();

		System.out.println("가져온 리스트 : " + list);

		Object count = null;
		model.addAttribute("count", count);

		return list; // 모든 문화재 데이터 가져오기
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




}


