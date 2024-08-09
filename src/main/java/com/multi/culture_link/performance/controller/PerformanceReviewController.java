package com.multi.culture_link.performance.controller;

import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
import com.multi.culture_link.performance.service.PerformanceReviewService;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceReviewController
 * @since : 2024. 8. 7.
 */
@RestController
@RequestMapping("/reviews") // 변경된 부분: 기본 경로를 /reviews로 설정
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;
    private final UserService userService; // UserService 주입

    @Autowired
    public PerformanceReviewController(PerformanceReviewService performanceReviewService, UserService userService) {
        this.performanceReviewService = performanceReviewService;
        this.userService = userService; // UserService 초기화
    }

    @GetMapping
    public List<PerformanceReviewDTO> getAllReviews() {
        return performanceReviewService.getAllReviews();
    }

    @GetMapping("/performance/{performanceId}")
    public List<PerformanceReviewDTO> getReviewsByPerformanceId(@PathVariable int performanceId) {
        return performanceReviewService.getReviewsByPerformanceId(performanceId);
    }

    @PostMapping
    public void insertReview(@RequestBody PerformanceReviewDTO review, Principal principal) {
        // 사용자 이름을 기반으로 userId를 가져오는 로직 추가
        String username = principal.getName();
        UserDTO userDTO;
        try {
            userDTO = userService.findUserByEmail(username);
        } catch (Exception e) {
            throw new RuntimeException("User not found");
        }

        int userId = userDTO.getUserId(); // 사용자 ID를 가져옴
        review.setUserId(userId);
        performanceReviewService.insertReview(review);
    }

    @PutMapping("/{id}")
    public void updateReview(@PathVariable int id, @RequestBody PerformanceReviewDTO review) {
        review.setId(id);
        performanceReviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        performanceReviewService.deleteReview(id);
    }
}