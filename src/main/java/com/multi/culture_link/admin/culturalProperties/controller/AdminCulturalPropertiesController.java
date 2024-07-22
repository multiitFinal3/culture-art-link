package com.multi.culture_link.admin.culturalProperties.controller;



import com.multi.culture_link.admin.culturalProperties.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	public List<CulturalPropertiesDTO> culturalPropertiesList(@RequestParam("page") int page) {

		System.out.println("list확인");
//		List<CulturalPropertiesDTO> list = adminCulturalPropertiesService.fetchApiData(page);
//		model.addAttribute("culturalPropertiesList", list);
		List<CulturalPropertiesDTO> list = null;
//		try {
//			list = adminCulturalPropertiesService.fetchApiData(page);
//
//
//
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}


		System.out.println("list : " + list);

		return list;


	}




}


