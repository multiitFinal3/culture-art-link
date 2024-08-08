package com.multi.culture_link.performance.service;

import com.multi.culture_link.performance.mapper.PerformanceReviewMapper;
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
    private PerformanceReviewMapper performanceReviewMapper;

    public List<PerformanceReviewDTO> getAllReviews() {
        return performanceReviewMapper.getAllReviews();
    }

    public List<PerformanceReviewDTO> getReviewsByPerformanceId(int performanceId) {
        return performanceReviewMapper.getReviewsByPerformanceId(performanceId);
    }

    public void insertReview(PerformanceReviewDTO review) {
        performanceReviewMapper.insertReview(review);
    }

    public void updateReview(PerformanceReviewDTO review) {
        performanceReviewMapper.updateReview(review);
    }

    public void deleteReview(int id) {
        performanceReviewMapper.deleteReview(id);
    }
}
