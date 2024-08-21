package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceReviewService
 * @since : 2024. 8. 7.
 */
@Service
public class PerformanceReviewService {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceReviewService.class);


    @Autowired
    private final PerformanceMapper performancMapper;

    public PerformanceReviewService(PerformanceMapper performancMapper) {
        this.performancMapper = performancMapper;
    }

//    public List<PerformanceReviewDTO> getReviewsByPerformanceId(int performanceId) {
//        return performancMapper.findReviewsByPerformanceId(performanceId);
//    }

    public List<PerformanceReviewDTO> getReviewsByPerformanceId(int performanceId) {
        List<PerformanceReviewDTO> reviews = performancMapper.findReviewsByPerformanceId(performanceId);

        // 리뷰 데이터 로깅
        for (PerformanceReviewDTO review : reviews) {
            logger.info("Review ID: {}, User Name: {}, User Profile Image: {}", review.getId(), review.getUserName(), review.getUserProfileImage());
        }

        return reviews;
    }

    public void addReview(PerformanceReviewDTO review) {
        performancMapper.insertReview(review);
    }

    public void updateReview(PerformanceReviewDTO review) {
        performancMapper.updateReview(review);
    }

    public void deleteReview(int reviewId) {
        performancMapper.deleteReview(reviewId);
    }


    public List<PerformanceReviewDTO> getReviewsByUserId(int userId) {
        return performancMapper.findReviewsByUserId(userId);
    }
}
