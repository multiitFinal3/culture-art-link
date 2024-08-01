package com.multi.culture_link.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.admin.performance.service.PerformanceDBService;
import com.multi.culture_link.performance.service.PerformanceLocationService;
import com.multi.culture_link.performance.service.PerformanceRankingService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 공연 정보를 처리하는 컨트롤러 클래스
 *
 * @since : 7/25/24
 */
@Controller
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceRankingService performanceRankingService;
    private final PerformanceLocationService performanceLocationService;

    private final PerformanceDBService performanceDBService;


    public PerformanceController(PerformanceRankingService performanceRankingService,
                                 PerformanceLocationService performanceLocationService,
                                 PerformanceDBService performanceDBService) {
        this.performanceRankingService = performanceRankingService;
        this.performanceLocationService = performanceLocationService;
        this.performanceDBService = performanceDBService;

    }

    /**
     * 공연 홈페이지 매핑
     *
     * @param user  the user
     * @param model the model
     * @return 공연 홈페이지
     */
    @GetMapping("/performance-home")
    public String performanceHomePage(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
        model.addAttribute("user", user.getUser());
        return "/performance/performanceHome";
    }

    /**
     * 공연 장르 페이지 매핑
     *
     * @param user  the user
     * @param genre the genre
     * @param model the model
     * @return 공연 각 장르별 페이지
     */
    @GetMapping("/performance-genre")
    public String performanceGenrePage(@AuthenticationPrincipal VWUserRoleDTO user,
                                       @RequestParam("genre") String genre,
                                       Model model) {
        String date = "20240730"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData = performanceRankingService.fetchGenreRanking(genre, date, 5);
        System.out.println("Fetched Data: " + rankingData); // 로그 추가

        // 장르에 따른 전체 공연 목록 추가
        List<PerformanceDTO> allPerformances = performanceDBService.getPerformancesByGenre(genre);
        System.out.println("All Performances: " + allPerformances); // 디버깅 로그 추가

        model.addAttribute("user", user.getUser());
        model.addAttribute("genre", genre);
        model.addAttribute("rankingData", rankingData);
        model.addAttribute("allPerformances", allPerformances); // 전체 공연 목록 모델에 추가

        return "/performance/performanceGenre";
    }

    /**
     * 기본 페이지로 리디렉션
     *
     * @return 기본 페이지
     */
    @GetMapping
    public String defaultPage() {
        return "redirect:/performance/performance-home";
    }

    /**
     * 공연 랭킹 데이터
     *
     * @param ststype  the ststype
     * @param catecode the catecode
     * @param date     the date
     * @return 공연 랭킹 데이터
     */
    @GetMapping("/rankings")
    public ResponseEntity<String> getPerformanceRankings(
            @RequestParam String ststype,
            @RequestParam String catecode,
            @RequestParam String date) {
        String data = performanceRankingService.fetchPerformanceData(ststype, catecode, date);
        return ResponseEntity.ok(data);
    }

    /**
     * 공연 장르별 랭킹 데이터
     *
     * @param genre the genre
     * @return 공연 장르별 랭킹 페이지
     */
    @GetMapping("/genre-rankings")
    public ResponseEntity<List<PerformanceDTO>> getPerformanceGenreRankings(@RequestParam String genre) {
        String date = "20240730"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData;

        if (genre.equals("전체")) {
            rankingData = performanceRankingService.fetchTopRankings(date);
        } else {
            rankingData = performanceRankingService.fetchGenreRanking(genre, date, 50);
        }

        return ResponseEntity.ok(rankingData);
    }


    /**
     * Performance ranking page string.
     *
     * @param user  the user
     * @param model the model
     * @param genre the genre
     * @return performanceRanking
     */
    // PerformanceController.java

    @GetMapping("/performanceRanking")
    public String performanceRankingPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model,
                                         @RequestParam(required = false) String genre) {
        model.addAttribute("user", user.getUser());
        String date = "20240730"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData;

        if (genre == null || genre.isEmpty() || genre.equals("전체")) {
            rankingData = performanceRankingService.fetchTopRankings(date); // 전체 랭킹 데이터
        } else {
            rankingData = performanceRankingService.fetchGenreRanking(genre, date, 50); //  장르 랭킹 데이터
        }

        model.addAttribute("rankingData", rankingData);
        model.addAttribute("genre", genre);
        return "/performance/performanceRanking";
    }










    @GetMapping("/performanceLocation")
    public String performanceLocationPage(@AuthenticationPrincipal VWUserRoleDTO user,
                                          @RequestParam(required = false) String locationCode,
                                          Model model) {
        String stdate = "20240730"; // 시작 날짜
        String eddate = "20240830"; // 종료 날짜

        System.out.println("Received locationCode: " + locationCode); // Debug line

        List<PerformanceDTO> performances = performanceLocationService.fetchPerformancesByLocation(stdate, eddate, locationCode);

        model.addAttribute("user", user.getUser());
        model.addAttribute("performances", performances);

        return "/performance/performanceLocation";
    }




    @GetMapping("/performanceDetail")
    public String performanceDetailPage(@AuthenticationPrincipal VWUserRoleDTO user,
                                        @RequestParam("performanceId") int performanceId,
                                        Model model) {
        PerformanceDTO performance = performanceRankingService.getPerformanceDetail(performanceId);
        model.addAttribute("user", user.getUser());
        model.addAttribute("performance", performance);
        return "/performance/performanceDetail";
    }




}
