package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
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

    @Autowired
    private final PerformanceMapper performancMapper;

    public PerformanceReviewService(PerformanceMapper performancMapper) {
        this.performancMapper = performancMapper;
    }

    public List<PerformanceReviewDTO> getReviewsByPerformanceId(int performanceId) {
        return performancMapper.findReviewsByPerformanceId(performanceId);
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
}
