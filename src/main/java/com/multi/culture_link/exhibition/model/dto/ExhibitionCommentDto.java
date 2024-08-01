package com.multi.culture_link.exhibition.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ExhibitionCommentDto {
    private int id;
    private int exhibitionId;
    private int userId;
    private double stars;
    private boolean isAuth;
    private String content;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}