package com.multi.culture_link.board.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardCommentDto {
    private int id;
    private int boardId;
    private int userId;
    private boolean isAuth;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}