package com.multi.culture_link.performance.mapper;

import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceReviewMapper
 * @since : 2024. 8. 7.
 */
@Mapper
public interface PerformanceReviewMapper {
    List<PerformanceReviewDTO> getAllReviews();

    List<PerformanceReviewDTO> getReviewsByPerformanceId(int performanceId);

    void insertReview(PerformanceReviewDTO review);

    void updateReview(PerformanceReviewDTO review);

    void deleteReview(int id);
}
