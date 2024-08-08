package com.multi.culture_link.performance.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceReviewDTO
 * @since : 2024. 8. 7.
 */
@Data
public class PerformanceReviewDTO {
    private int id;
    private int userId;
    private int performanceId;
    private LocalDateTime dateTime;
    private double starRating;
    private String content;
    private LocalDateTime reviewDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
