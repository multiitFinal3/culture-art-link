package com.multi.culture_link.admin.culturalProperties.controller;



import com.multi.culture_link.admin.culturalProperties.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/cultural-properties-regulate")
public class AdminCulturalPropertiesController {


	@Autowired
	private AdminCulturalPropertiesService culturalPropertiesService;

	@GetMapping
	public String culturalPropertiesManage(Model model) {
		List<CulturalPropertiesDTO> culturalPropertiesList = culturalPropertiesService.fetchApiData();
		model.addAttribute("culturalPropertiesList", culturalPropertiesList);
		return "/admin/culturalProperties/culturalPropertiesRegulate";
	}




}


