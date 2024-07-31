package com.multi.culture_link.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.culturalProperties.service.CulturalPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cultural-properties")
public class CulturalPropertiesController {


	@Autowired
	private CulturalPropertiesService culturalPropertiesService;


	@GetMapping
	public String culturalProperties() {
		return "/culturalProperties/culturalProperties";
	}



	@GetMapping("/getList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listCulturalProperties(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "0") int size) {

		int totalCount = culturalPropertiesService.getTotalCount(); // 전체 문화재 수

		// size가 0 이하인 경우 전체 데이터 수로 설정
		if (size <= 0) {
			size = totalCount; // 전체 데이터 수로 설정
			page = 1; // 기본 페이지 번호는 1
		}

		int offset = (page - 1) * size; // 오프셋 계산
		List<CulturalPropertiesDTO> culturalProperties = culturalPropertiesService.listCulturalProperties(offset, size);
		int totalPages = (int) Math.ceil((double) totalCount / size); // 전체 페이지 수 계산

		Map<String, Object> response = new HashMap<>();
		response.put("culturalProperties", culturalProperties);
		response.put("totalCount", totalCount);
		response.put("totalPages", totalPages);

		return ResponseEntity.ok(response);
	}

	// 문화재 상세 페이지
	@GetMapping("/detail/{id}")
	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);
		model.addAttribute("property", property);
		return "culturalProperties/detail"; // detail.html로 변경
	}


}


