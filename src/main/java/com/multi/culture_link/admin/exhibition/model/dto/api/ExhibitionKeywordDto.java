package com.multi.culture_link.admin.exhibition.model.dto.api;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ExhibitionKeywordDto {
    private int id;
    private int exhibitionId;
    private String keyword;
    private int frequency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "ExhibitionKeywordDto{" +
                "id=" + id +
                ", exhibitionId=" + exhibitionId +
                ", keyword='" + keyword + '\'' +
                ", frequency=" + frequency +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}