package com.multi.culture_link.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/cultural-properties")
public class CulturalPropertiesController {


//	@Autowired
//	private CulturalPropertiesService culturalPropertiesService;


//	@GetMapping
//	public String listCulturalProperties(Model model) {
////		List<CulturalPropertiesDTO> properties = culturalPropertiesService.getAllCulturalProperties();
////		model.addAttribute("culturalProperties", properties);
//		return "cultural-properties";
//	}

	@GetMapping
	public String listCulturalProperties() {
		return "/culturalProperties/culturalProperties"; // 반환할 뷰의 이름 (culturalProperties.html)
	}


//	@GetMapping("/getList")
//	@ResponseBody
//	public String listCulturalProperties(Model model, @RequestParam(defaultValue = "1") int page) {
//		int pageSize = 6;
//		Page<CulturalPropertiesDTO> propertyPage = culturalPropertiesService.getPageProperties(page, pageSize);
//
//		model.addAttribute("culturalProperties", propertyPage.getContent());
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPages", propertyPage.getTotalPages());
//
//		int startPage = Math.max(1, page - 2);
//		int endPage = Math.min(startPage + 4, propertyPage.getTotalPages());
//
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);
//
//		return "cultural-properties";
//	}


}


