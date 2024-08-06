package com.multi.culture_link.board.model.dto;

import lombok.Data;


@Data
public class BoardDto {
    public int id;
    public int userId;
    public String author;
    public String genre;
    public String title;
    public String content;
    public String createdAt;
}
