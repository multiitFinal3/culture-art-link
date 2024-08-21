package com.multi.culture_link.performance.controller;

import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
import com.multi.culture_link.performance.service.PerformanceReviewService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceReviewController
 * @since : 2024. 8. 7.
 */
@RestController
@RequestMapping("/reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    @Autowired
    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    // 공연 리뷰 조회
    @GetMapping("/{performanceId}")
    public ResponseEntity<List<PerformanceReviewDTO>> getReviews(@PathVariable int performanceId) {
        List<PerformanceReviewDTO> reviews = performanceReviewService.getReviewsByPerformanceId(performanceId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 추가
    @PostMapping // 중복된 경로 수정
    public ResponseEntity<?> addReview(@AuthenticationPrincipal VWUserRoleDTO user, @RequestBody PerformanceReviewDTO review) {
        try {
            // 현재 로그인된 사용자의 user_id 설정
            review.setUserId(user.getUser().getUserId());

            // 리뷰 추가 서비스 호출
            performanceReviewService.addReview(review);

            return ResponseEntity.ok("Review added successfully.");
        } catch (Exception e) {
            // 예외 발생 시 구체적인 메시지를 포함
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review: " + e.getMessage());
        }
    }

    // 리뷰 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable int id, @RequestBody PerformanceReviewDTO review) {
        review.setId(id);
        performanceReviewService.updateReview(review);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        performanceReviewService.deleteReview(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }











    // 사용자가 작성한 공연 리뷰 조회
    @GetMapping("/user")
    public ResponseEntity<List<PerformanceReviewDTO>> getUserReviews(@AuthenticationPrincipal VWUserRoleDTO user) {
        List<PerformanceReviewDTO> userReviews = performanceReviewService.getReviewsByUserId(user.getUser().getUserId());
        return ResponseEntity.ok(userReviews);
    }
}