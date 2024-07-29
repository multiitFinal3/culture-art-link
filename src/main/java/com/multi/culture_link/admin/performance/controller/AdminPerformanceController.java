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

/**
 * The type Admin performance controller.
 */
@Controller
@RequestMapping("/admin/performance-regulate")
public class AdminPerformanceController {

    @Autowired
    private PerformanceAPIService performanceAPIService;

    @Autowired
    private PerformanceDBService performanceDBService;

    /**
     * Performance manage string.
     *
     * @param model            the model
     * @param dbPage           the db page
     * @param apiPage          the api page
     * @param size             the size
     * @param dbSearchKeyword  the db search keyword
     * @param apiSearchKeyword the api search keyword
     * @return the string
     */
    @GetMapping // 공연 관리 페이지를 반환하는 메소드
    public String performanceManage(Model model,
                                    @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                                    @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                                    @RequestParam(name = "size", defaultValue = "100") int size,
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

            List<PerformanceDTO> filteredPerformances;
            if (apiSearchKeyword != null && !apiSearchKeyword.isEmpty()) {
                filteredPerformances = performanceAPIService.searchPerformances(apiSearchKeyword);
            } else {
                List<PerformanceDTO> allApiPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
                filteredPerformances = allApiPerformances.stream()
                        .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                        .collect(Collectors.toList());
            }

            // 페이지네이션을 적용한 데이터를 모델에 추가
            Page<PerformanceDTO> dbPerformancesPage = paginateList(dbPerformances, dbPage, size);
            Page<PerformanceDTO> apiPerformancesPage = paginateList(filteredPerformances, apiPage, size);

            model.addAttribute("performances", apiPerformancesPage.getContent());
            model.addAttribute("dbPerformances", dbPerformancesPage.getContent());
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", apiPerformancesPage.getTotalPages());
            model.addAttribute("dbTotalPages", dbPerformancesPage.getTotalPages());
            model.addAttribute("dbSearchKeyword", dbSearchKeyword);
            model.addAttribute("apiSearchKeyword", apiSearchKeyword);

            // 검색 결과가 없는 경우 플래그 추가
            if (dbPerformances.isEmpty()) {
                model.addAttribute("dbSearchEmpty", true);
            }
            if (filteredPerformances.isEmpty()) {
                model.addAttribute("apiSearchEmpty", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/admin/performance/performanceRegulate";
    }

    // 리스트를 페이지네이션하는 헬퍼 메소드
    private Page<PerformanceDTO> paginateList(List<PerformanceDTO> list, int page, int size) {
        int fromIndex = Math.min(page * size, list.size());
        int toIndex = Math.min(fromIndex + size, list.size());

        List<PerformanceDTO> paginatedList = list.subList(fromIndex, toIndex);
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), list.size());
    }

    /**
     * Save to db string.
     *
     * @param selectedIdsAPI the selected ids api
     * @param apiPage        the api page
     * @param dbPage         the db page
     * @param size           the size
     * @param model          the model
     * @return the string
     */
    @PostMapping("/saveToDB")
    public String saveToDB(@RequestParam(name = "selectedIdsAPI", required = false) List<String> selectedIdsAPI,
                           @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                           @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                           @RequestParam(name = "size", defaultValue = "100") int size,
                           Model model) {
        int savedCount = 0;
        if (selectedIdsAPI != null && !selectedIdsAPI.isEmpty()) {
            savedCount = performanceDBService.savePerformances(selectedIdsAPI);
            System.out.println("데이터 베이스 저장 (" + savedCount + "개) 성공");
            model.addAttribute("savedCount", savedCount);
        }

        try {
            List<PerformanceDTO> dbPerformances = performanceDBService.getAllPerformances();
            List<PerformanceDTO> allApiPerformances = performanceAPIService.fetchData(0, Integer.MAX_VALUE).getContent();
            List<PerformanceDTO> filteredPerformances = allApiPerformances.stream()
                    .filter(p -> dbPerformances.stream().noneMatch(db -> db.getCode().equals(p.getCode())))
                    .collect(Collectors.toList());

            // 페이지네이션을 적용한 데이터를 모델에 추가
            Page<PerformanceDTO> dbPerformancesPage = paginateList(dbPerformances, dbPage, size);
            Page<PerformanceDTO> apiPerformancesPage = paginateList(filteredPerformances, apiPage, size);

            model.addAttribute("performances", apiPerformancesPage.getContent());
            model.addAttribute("dbPerformances", dbPerformancesPage.getContent());
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", apiPerformancesPage.getTotalPages());
            model.addAttribute("dbTotalPages", dbPerformancesPage.getTotalPages());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/performance-regulate?dbPage=" + dbPage + "&apiPage=" + apiPage + "&size=" + size + (model.containsAttribute("savedCount") ? "&savedCount=" + model.getAttribute("savedCount") : "");
    }

    /**
     * Delete from db string.
     *
     * @param selectedIds the selected ids
     * @param apiPage     the api page
     * @param dbPage      the db page
     * @param size        the size
     * @param model       the model
     * @return the string
     */
    @PostMapping("/deleteFromDB")
    public String deleteFromDB(@RequestParam(name = "selectedIds", required = false) List<String> selectedIds,
                               @RequestParam(name = "apiPage", defaultValue = "0") int apiPage,
                               @RequestParam(name = "dbPage", defaultValue = "0") int dbPage,
                               @RequestParam(name = "size", defaultValue = "100") int size,
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

            // 페이지네이션을 적용한 데이터를 모델에 추가
            Page<PerformanceDTO> dbPerformancesPage = paginateList(dbPerformances, dbPage, size);
            Page<PerformanceDTO> apiPerformancesPage = paginateList(filteredPerformances, apiPage, size);

            model.addAttribute("performances", apiPerformancesPage.getContent());
            model.addAttribute("dbPerformances", dbPerformancesPage.getContent());
            model.addAttribute("dbCurrentPage", dbPage);
            model.addAttribute("apiCurrentPage", apiPage);
            model.addAttribute("apiTotalPages", apiPerformancesPage.getTotalPages());
            model.addAttribute("dbTotalPages", dbPerformancesPage.getTotalPages());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 페이지 번호와 사이즈를 리다이렉트 경로에 추가
        return "redirect:/admin/performance-regulate?dbPage=" + dbPage + "&apiPage=" + apiPage + "&size=" + size + (model.containsAttribute("deletedCount") ? "&deletedCount=" + model.getAttribute("deletedCount") : "");
    }
}
