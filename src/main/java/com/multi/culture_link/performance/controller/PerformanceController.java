package com.multi.culture_link.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.admin.performance.service.PerformanceDBService;
import com.multi.culture_link.performance.model.dto.PerformanceAddDTO;
import com.multi.culture_link.performance.model.dto.PerformanceKeywordDTO;
import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
import com.multi.culture_link.performance.service.PerformanceLocationService;
import com.multi.culture_link.performance.service.PerformanceRankingService;
import com.multi.culture_link.performance.service.PerformanceReviewService;
import com.multi.culture_link.performance.service.PerformanceService;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 공연 정보를 처리하는 컨트롤러 클래스
 *
 * @since : 7/25/24
 */
@Controller
@RequestMapping("/performance")
public class PerformanceController {

    @Value("${API-KEY.naverClientId}")
    private String naverClientId;

    private final PerformanceRankingService performanceRankingService;
    private final PerformanceLocationService performanceLocationService;
    private final PerformanceDBService performanceDBService;
    private final PerformanceService performanceService;

    private final PerformanceReviewService performanceReviewService;


    public PerformanceController(PerformanceRankingService performanceRankingService,
                                 PerformanceLocationService performanceLocationService,
                                 PerformanceDBService performanceDBService,
                                 PerformanceService performanceService,
                                 PerformanceReviewService performanceReviewService) {
        this.performanceRankingService = performanceRankingService;
        this.performanceLocationService = performanceLocationService;
        this.performanceDBService = performanceDBService;
        this.performanceService = performanceService;
        this.performanceReviewService = performanceReviewService;

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

        // 추천 공연 목록 가져오기
        List<PerformanceDTO> recommendedPerformances = performanceService.getRecommendedPerformances(user.getUserId());
        if (recommendedPerformances == null || recommendedPerformances.isEmpty()) {
            model.addAttribute("message", "추천 공연이 없습니다.");
        } else {
            model.addAttribute("recommends", recommendedPerformances);
        }

        return "/performance/performanceHome";
    }

    /**
     * 공연 장르 페이지 매핑 -> 5위까지 performanceGenre.html
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

        PerformanceDTO performance = null;

        if ("추천".equals(genre)) {
            return "redirect:/performance/performance-home";
        }

        String date = "20240821"; // 일간 데이터 날짜
        List<PerformanceDTO> rankingData = performanceRankingService.fetchGenreRanking(genre, date, 5);
        System.out.println("Fetched Data: " + rankingData); // 로그 추가

        // 장르에 따른 전체 공연 목록 추가
        List<PerformanceDTO> allPerformances = performanceDBService.getPerformancesByGenre(genre);
        System.out.println("All Performances: " + allPerformances); // 디버깅 로그 추가

        // 날짜 포맷 업데이트
        allPerformances.forEach(PerformanceDTO::updateFormattedDate);

        // user 객체가 null인지 확인하여 안전하게 처리
        if (user != null && user.getUser() != null) {
            model.addAttribute("user", user.getUser());
        } else {
            model.addAttribute("message", "로그인 후 이용해 주세요."); // 로그인하지 않은 경우 메시지 추가
        }

        model.addAttribute("genre", genre);
        model.addAttribute("rankingData", rankingData);
        model.addAttribute("allPerformances", allPerformances);
        model.addAttribute("performance", performance);


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
     * 공연 랭킹 페이지 -> 50위 performanceRanking.html
     *
     * @param genre the genre
     * @return 공연 장르별 랭킹 페이지
     */
    @GetMapping("/genre-rankings")
    public ResponseEntity<List<PerformanceDTO>> getPerformanceGenreRankings(@RequestParam String genre) {
        String date = "20240821"; // 일간 데이터 날짜
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
        String date = "20240821"; // 일간 데이터 날짜
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
        String stdate = "20240821"; // 시작 날짜
        String eddate = "20240921"; // 종료 날짜

        System.out.println("Received locationCode: " + locationCode); // Debug line

        List<PerformanceDTO> performances = performanceLocationService.fetchPerformancesByLocation(stdate, eddate, locationCode);

        model.addAttribute("user", user.getUser());
        model.addAttribute("performances", performances);

        return "/performance/performanceLocation";
    }


    @GetMapping("/performanceDetail")
    public String performanceDetailPage(@AuthenticationPrincipal VWUserRoleDTO user,
                                        @RequestParam("performanceCode") String performanceCode,
                                        @RequestParam(value = "source", required = false, defaultValue = "db") String source,
                                        Model model) {
        PerformanceDTO performance = null;
        System.out.println("Requested performanceCode: " + performanceCode);
        System.out.println("Data source: " + source);

        if ("db".equals(source)) {
            performance = performanceDBService.getPerformanceByCode(performanceCode);
            System.out.println("Fetched from DB: " + performance);
        } else if ("api".equals(source)) {
            try {
                performance = performanceRankingService.getPerformanceDetailFromAPI(performanceCode);
                System.out.println("Fetched from API: " + performance);
            } catch (Exception e) {
                model.addAttribute("error", "공연 정보를 가져오는 중 오류가 발생했습니다.");
                e.printStackTrace();
                return "redirect:/errorPage";
            }
        }

        // performance 객체가 null인 경우 처리
        if (performance != null) {
            performance.updateFormattedDate(); // 날짜 포맷 업데이트
            model.addAttribute("user", user.getUser());
            model.addAttribute("performance", performance);

            // 리뷰 가져오기
            List<PerformanceReviewDTO> reviews = performanceReviewService.getReviewsByPerformanceId(performance.getId());
            model.addAttribute("reviews", reviews);

            // 평균 별점 계산 및 포맷팅
            double averageRating = reviews.stream()
                    .mapToDouble(PerformanceReviewDTO::getStarRating)
                    .average()
                    .orElse(0.0);

            // 소수점 첫째 자리까지만 표시하고 별점 계산
            int fullStars = (int) averageRating; // 정수 부분
            boolean halfStar = (averageRating - fullStars) >= 0.25 && (averageRating - fullStars) < 0.75; // 소수 부분이 0.25 이상 0.75 미만인 경우 반별

            model.addAttribute("fullStars", fullStars);
            model.addAttribute("halfStar", halfStar);
            model.addAttribute("averageRating", String.format("%.1f", averageRating)); // 소수점 첫째 자리까지만 표시

        } else {
            // 공연 정보를 가져오지 못했을 때의 처리
            model.addAttribute("error", "공연 정보를 가져오지 못했습니다.");
            return "redirect:/errorPage";  // Error 페이지로 리다이렉트 또는 에러 메시지 출력 페이지로 이동
        }

        model.addAttribute("naverClientId", naverClientId); // 클라이언트 ID를 모델에 추가

        return "/performance/performanceDetail";
    }






    @GetMapping("/performanceDetailByTitle")
    public String performanceDetailByTitle(@RequestParam("performanceTitle") String performanceTitle, Model model) {
        // DB에서 공연명을 통해 공연 정보를 검색
        PerformanceDTO performance = performanceDBService.getPerformanceByTitle(performanceTitle);

        if (performance == null) {
            model.addAttribute("error", "해당 공연명을 가진 공연을 찾을 수 없습니다.");
            return "/performance/performanceDetail";
        }

        // 공연 디테일 페이지에 정보를 전달
        model.addAttribute("performance", performance);
        model.addAttribute("naverClientId", naverClientId);
        return "/performance/performanceDetail";
    }



    @GetMapping("/getPerformanceCode")
    public ResponseEntity<Map<String, String>> getPerformanceCode(@RequestParam("performanceTitle") String performanceTitle) {
        String performanceCode = performanceDBService.getPerformanceCodeByTitle(performanceTitle);
        Map<String, String> response = new HashMap<>();
        response.put("performanceCode", performanceCode);
        return ResponseEntity.ok(response);
    }


    // 좋아요/싫어요 상태 업데이트 메서드
    @PostMapping("/updatePerformanceLikeState")
    public ResponseEntity<?> updatePerformanceLikeState(@RequestBody PerformanceAddDTO performanceAddDTO) {
        try {
            performanceService.updatePerformanceLikeState(performanceAddDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // 현재 좋아요/싫어요 상태를 가져오는 메서드
    @GetMapping("/getPerformanceLikeState")
    @ResponseBody
    public Map<String, String> getPerformanceLikeState(@RequestParam int userId, @RequestParam int performanceId) {
        Map<String, String> response = new HashMap<>();
        try {
            String state = performanceService.getPerformanceLikeState(userId, performanceId);
            // 로그 추가 (선택 사항)
            System.out.println("Retrieved state: " + state);

            // 상태가 null인 경우 기본값 설정
            if (state == null) {
                state = "none";
            }

            response.put("state", state);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("state", "none");  // 기본값을 설정
        }
        return response;
    }


    // 검색
    @GetMapping("/search")
    public String searchPerformances(
            @AuthenticationPrincipal VWUserRoleDTO user,  // 인증된 사용자를 받아옵니다.
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "genre", required = false) String genre,
            Model model) {

        if (user == null) {
            return "redirect:/login";  // user가 null이라면 로그인 페이지로 리다이렉트
        }

        List<PerformanceDTO> performances = performanceService.searchPerformances(keyword, genre);
        model.addAttribute("allPerformances", performances);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("keyword", keyword);  // 검색어를 모델에 추가
        model.addAttribute("user", user.getUser());  // user 정보를 모델에 추가

        // 각 공연에 대해 formattedDate 설정
        for (PerformanceDTO performance : performances) {
            performance.updateFormattedDate();
        }

        // 검색 결과가 없을 경우 플래그 추가
        boolean noResults = performances.isEmpty();
        model.addAttribute("noResults", noResults);

        return "performance/performanceGenre"; // 적절한 템플릿 경로로 변경
    }






    @GetMapping("/recommendations")
    public String showRecommendations(@AuthenticationPrincipal UserDTO user, Model model) {
        System.out.println("showRecommendations 메서드 호출됨");  // 확인용 로그
        int userId = user.getUserId(); // UserDTO에서 사용자 ID를 가져옴

        List<PerformanceDTO> recommendedPerformances = performanceService.getRecommendedPerformances(userId);
        System.out.println("추천 공연 목록 크기: " + recommendedPerformances.size());  // 추천 목록의 크기 확인

        if (recommendedPerformances == null || recommendedPerformances.isEmpty()) {
            model.addAttribute("message", "추천 공연이 없습니다.");
        } else {
            model.addAttribute("recommends", recommendedPerformances);
        }

        model.addAttribute("user", user);
        return "performance/performanceHome"; // 추천 목록을 보여줄 Thymeleaf 템플릿 이름
    }












    /**
     * 사용자가 찜한 공연 목록을 반환하는 메서드
     *
     * @param user 인증된 사용자 정보
     * @return 사용자가 찜한 공연 목록
     */


    @GetMapping("/findLovePerformanceList")
    @ResponseBody
    public ResponseEntity<List<PerformanceDTO>> findLovePerformanceList(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
        int userId = user.getUser().getUserId();


        // 사용자의 찜한 공연 목록을 가져옴
        List<PerformanceDTO> lovePerformanceList = performanceService.getLovePerformancesByUserId(user.getUserId());

        model.addAttribute("user", user);

        return ResponseEntity.ok(lovePerformanceList);
    }


    /**
     * 사용자가 관심없음 공연 목록을 반환하는 메서드
     *
     * @param user 인증된 사용자 정보
     * @return 사용자가 관심없음 공연 목록
     */


    @GetMapping("/findHatePerformanceList")
    @ResponseBody
    public ResponseEntity<List<PerformanceDTO>> findHatePerformanceList(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {
        int userId = user.getUser().getUserId();


        // 사용자의 찜한 공연 목록을 가져옴
        List<PerformanceDTO> hatePerformanceList = performanceService.getHatePerformancesByUserId(user.getUserId());

        model.addAttribute("user", user);

        return ResponseEntity.ok(hatePerformanceList);
    }











    /**
     * 사용자가 찜한 공연 키워드를 반환하는 메서드
     *
     * @param user  인증된 사용자 정보
     * @param type  키워드 타입 (찜: 'L', 관심없음: 'H')
     * @return 사용자가 선택한 공연 키워드 목록
     */
//    @GetMapping("/getUserKeywords")
//    public ResponseEntity<Map<String, Object>> getUserKeywords(
//            @AuthenticationPrincipal VWUserRoleDTO user,
//            @RequestParam("type") String type) {
//
//        Map<String, Object> response = new HashMap<>();
//        try {
//            List<PerformanceKeywordDTO> keywords;
//            if ("L".equals(type)) {
//                keywords = performanceService.getLoveKeywordsByUserId(user.getUser().getUserId());
//            } else if ("H".equals(type)) {
//                keywords = performanceService.getHateKeywordsByUserId(user.getUser().getUserId());
//            } else {
//                throw new IllegalArgumentException("Invalid keyword type: " + type);
//            }
//
//            response.put("keywords", keywords);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", "Error fetching keywords: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    @GetMapping("/getUserKeywords")
    public ResponseEntity<Map<String, Object>> getUserKeywords(
            @AuthenticationPrincipal VWUserRoleDTO user) {

        Map<String, Object> response = new HashMap<>();
        try {
            // 사용자가 찜한 키워드를 가져옴
            List<PerformanceKeywordDTO> loveKeywords = performanceService.getLoveKeywordsByUserId(user.getUser().getUserId());

            // 사용자가 찜하지 않은 나머지 키워드를 관심없음 키워드로 처리
            List<PerformanceKeywordDTO> hateKeywords = performanceService.getHateKeywordsByUserId(user.getUser().getUserId());

            response.put("loveKeywords", loveKeywords);
            response.put("hateKeywords", hateKeywords);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error fetching keywords: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}