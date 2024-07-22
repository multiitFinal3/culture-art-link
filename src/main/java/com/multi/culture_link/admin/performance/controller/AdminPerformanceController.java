package com.multi.culture_link.admin.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.admin.performance.service.PerformanceAPIService;
import com.multi.culture_link.admin.performance.service.PerformanceDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/performance-regulate")
public class AdminPerformanceController {

    @Autowired
    private PerformanceAPIService performanceAPIService;

    @Autowired
    private PerformanceDBService performanceDBService;

    @GetMapping
    public String performanceManage(Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "20") int size) {
        try {
            // API 및 DB에서 데이터 가져오기
            Page<PerformanceDTO> performances = performanceAPIService.fetchData(page, size);
            List<PerformanceDTO> dbPerformances = performanceDBService.getAllPerformances();

            // 이미 DB에 있는 공연을 필터링
            List<PerformanceDTO> filteredPerformances = performances.getContent().stream()
                    .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                    .collect(Collectors.toList());

            // 모델에 속성 추가
            model.addAttribute("performances", filteredPerformances);
            model.addAttribute("dbPerformances", dbPerformances);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", performances.getTotalPages());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/admin/performance/performanceRegulate";
    }

    @PostMapping("/saveToDB")
    public String saveToDB(@RequestParam("selectedIds") List<String> selectedIds,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "20") int size,
                           Model model) {
        System.out.println("Selected IDs: " + selectedIds);
        int savedCount = performanceDBService.savePerformances(selectedIds);
        System.out.println("데이터 베이스 저장 (" + savedCount + "개) 성공");

        try {
            // DB에서 최신 데이터 가져오기
            List<PerformanceDTO> dbPerformances = performanceDBService.getAllPerformances();

            // API에서 최신 데이터 가져오기
            List<PerformanceDTO> allPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
            List<PerformanceDTO> filteredPerformances = allPerformances.stream()
                    .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                    .collect(Collectors.toList());

            // 현재 페이지에 맞게 20개로 자르기
            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, filteredPerformances.size());
            List<PerformanceDTO> paginatedPerformances = filteredPerformances.subList(fromIndex, toIndex);

            // 모델에 속성 추가
            model.addAttribute("performances", paginatedPerformances);
            model.addAttribute("dbPerformances", dbPerformances);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", (int) Math.ceil((double) filteredPerformances.size() / size));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 페이지 번호와 사이즈를 리다이렉트 경로에 추가
        return "redirect:/admin/performance-regulate?page=" + page + "&size=" + size;
    }
}
