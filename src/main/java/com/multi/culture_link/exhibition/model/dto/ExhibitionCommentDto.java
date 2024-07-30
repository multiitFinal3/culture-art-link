package com.multi.culture_link.exhibition.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ExhibitionCommentDto {
    private Long id;
    private Long exhibitionId;
    private String name;
    private double stars;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}