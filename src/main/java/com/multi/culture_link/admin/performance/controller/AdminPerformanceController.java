package com.multi.culture_link.admin.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.admin.performance.service.PerformanceAPIService;
import com.multi.culture_link.admin.performance.service.PerformanceDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping // 공연 관리 페이지를 반환하는 메소드
    public String performanceManage(Model model,
                                    @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                                    @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                                    @RequestParam(name = "size", defaultValue = "20") int size,
                                    @RequestParam(name = "dbSearchKeyword", required = false) String dbSearchKeyword,
                                    @RequestParam(name = "apiSearchKeyword", required = false) String apiSearchKeyword) {
        try {
            List<PerformanceDTO> dbPerformances; // DB에서 검색 키워드가 있을 경우 해당 키워드로 공연 검색
            if (dbSearchKeyword != null && !dbSearchKeyword.isEmpty()) {
                dbPerformances = performanceDBService.searchPerformances(dbSearchKeyword);
            } else {
                dbPerformances = performanceDBService.getAllPerformances();
            }

            // 날짜 형식을 업데이트
            dbPerformances.forEach(PerformanceDTO::updateFormattedDate);

            int dbStart = Math.min(dbPage * size, dbPerformances.size());
            int dbEnd = Math.min(dbStart + size, dbPerformances.size());
            Page<PerformanceDTO> dbPerformancesPage = new PageImpl<>(dbPerformances.subList(dbStart, dbEnd), PageRequest.of(dbPage, size), dbPerformances.size());

            List<PerformanceDTO> filteredPerformances;
            if (apiSearchKeyword != null && !apiSearchKeyword.isEmpty()) {
                filteredPerformances = performanceAPIService.searchPerformances(apiSearchKeyword);
            } else {
                List<PerformanceDTO> allApiPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
                filteredPerformances = allApiPerformances.stream()
                        .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                        .collect(Collectors.toList());
            }

            int fromIndex = apiPage * size;
            int toIndex = Math.min(fromIndex + size, filteredPerformances.size());
            List<PerformanceDTO> paginatedPerformances = filteredPerformances.subList(fromIndex, Math.min(toIndex, filteredPerformances.size()));

            model.addAttribute("performances", paginatedPerformances);
            model.addAttribute("dbPerformances", dbPerformancesPage.getContent());
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", (int) Math.ceil((double) filteredPerformances.size() / size));
            model.addAttribute("dbTotalPages", dbPerformancesPage.getTotalPages());
            model.addAttribute("dbSearchKeyword", dbSearchKeyword);
            model.addAttribute("apiSearchKeyword", apiSearchKeyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/admin/performance/performanceRegulate";
    }



    @PostMapping("/saveToDB") // 선택된 API 공연 데이터를 DB에 저장하는 메소드
    public String saveToDB(@RequestParam(name = "selectedIdsAPI", required = false) List<String> selectedIdsAPI,
                           @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                           @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                           @RequestParam(name = "size", defaultValue = "20") int size,
                           Model model) {
        if (selectedIdsAPI != null && !selectedIdsAPI.isEmpty()) {
            int savedCount = performanceDBService.savePerformances(selectedIdsAPI);
            System.out.println("데이터 베이스 저장 (" + savedCount + "개) 성공");
        }

        try {
            // DB에서 최신 데이터 가져오기
            List<PerformanceDTO> dbPerformances = performanceDBService.getAllPerformances();

            // 전체 API 데이터를 가져와서 필터링
            List<PerformanceDTO> allApiPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
            List<PerformanceDTO> filteredPerformances = allApiPerformances.stream()
                    .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                    .collect(Collectors.toList());

            // 현재 페이지에 맞게 20개로 자르기
            int fromIndex = apiPage * size;
            int toIndex = Math.min(fromIndex + size, filteredPerformances.size());
            List<PerformanceDTO> paginatedPerformances = filteredPerformances.subList(fromIndex, Math.min(toIndex, filteredPerformances.size()));

            // 모델에 속성 추가
            model.addAttribute("performances", paginatedPerformances);
            model.addAttribute("dbPerformances", dbPerformances.subList(0, Math.min(size, dbPerformances.size())));
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", (int) Math.ceil((double) filteredPerformances.size() / size));
            model.addAttribute("dbTotalPages", (int) Math.ceil((double) dbPerformances.size() / size));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 페이지 번호와 사이즈를 리다이렉트 경로에 추가
        return "redirect:/admin/performance-regulate?dbPage=" + dbPage + "&apiPage=" + apiPage + "&size=" + size;
    }


    @PostMapping("/deleteFromDB")
    public String deleteFromDB(@RequestParam(name = "selectedIds", required = false) List<String> selectedIds,
                               @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                               @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                               @RequestParam(name = "size", defaultValue = "20") int size,
                               Model model) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            int deletedCount = performanceDBService.deletePerformances(selectedIds);
            System.out.println("데이터 베이스 삭제 (" + deletedCount + "개) 성공");

            model.addAttribute("deletedCount", deletedCount);
        }

        try {
            // DB에서 최신 데이터 가져오기
            List<PerformanceDTO> dbPerformances = performanceDBService.getAllPerformances();

            // 전체 API 데이터를 가져와서 필터링
            List<PerformanceDTO> allApiPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
            List<PerformanceDTO> filteredPerformances = allApiPerformances.stream()
                    .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                    .collect(Collectors.toList());

            // 현재 페이지에 맞게 20개로 자르기
            int fromIndex = apiPage * size;
            int toIndex = Math.min(fromIndex + size, filteredPerformances.size());
            List<PerformanceDTO> paginatedPerformances = filteredPerformances.subList(fromIndex, Math.min(toIndex, filteredPerformances.size()));

            // 모델에 속성 추가
            model.addAttribute("performances", paginatedPerformances);
            model.addAttribute("dbPerformances", dbPerformances.subList(0, Math.min(size, dbPerformances.size())));
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", (int) Math.ceil((double) filteredPerformances.size() / size));
            model.addAttribute("dbTotalPages", (int) Math.ceil((double) dbPerformances.size() / size));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 페이지 번호와 사이즈를 리다이렉트 경로에 추가
        return "redirect:/admin/performance-regulate?dbPage=" + dbPage + "&apiPage=" + apiPage + "&size=" + size + (model.containsAttribute("deletedCount") ? "&deletedCount=" + model.getAttribute("deletedCount") : "");
    }

}


