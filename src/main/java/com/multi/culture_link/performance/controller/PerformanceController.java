package com.multi.culture_link.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
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
 * 공연 정보를 처리하는 컨트롤러 클래스입니다.
 *
 * @since : 7/25/24
 */
@Controller
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceRankingService performanceRankingService;

    public PerformanceController(PerformanceRankingService performanceRankingService) {
        this.performanceRankingService = performanceRankingService;
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
        String date = "20240729"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData = performanceRankingService.fetchGenreRanking(genre, date);
        System.out.println("Fetched Data: " + rankingData); // 로그 추가
        model.addAttribute("user", user.getUser());
        model.addAttribute("genre", genre);
        model.addAttribute("rankingData", rankingData);
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
     * 공연 랭킹 데이터를 가져옵니다.
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
     * 공연 장르별 랭킹 데이터를 가져옵니다.
     *
     * @param genre the genre
     * @param model the model
     * @return 공연 장르별 랭킹 페이지
     */
    @GetMapping("/genre-rankings")
    public String getPerformanceGenreRankings(@RequestParam String genre, Model model) {
        String date = "20240729"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData = performanceRankingService.fetchGenreRanking(genre, date);
        model.addAttribute("genre", genre);
        model.addAttribute("rankingData", rankingData);
        return "performance/performanceGenreRankings";
    }


    @GetMapping("/performance-Ranking")
    public String performanceRankingPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
        model.addAttribute("user", user.getUser());
        return "/performance/performanceRanking";
    }
}
