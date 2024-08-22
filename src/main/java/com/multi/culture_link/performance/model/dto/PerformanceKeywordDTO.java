package com.multi.culture_link.performance.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : PerformanceKeywordDTO
 * @since : 2024. 8. 22.
 */

@Data
public class PerformanceKeywordDTO {
    private int id;
    private String genre;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
