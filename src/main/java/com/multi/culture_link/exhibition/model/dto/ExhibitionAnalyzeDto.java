package com.multi.culture_link.exhibition.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ExhibitionAnalyzeDto {
    private int id;
    private int exhibitionId;
    private int userId;
    private String name;
    private String image;
    private String artwork;
    private String content;
    private boolean isAuth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}