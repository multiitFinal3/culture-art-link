package com.multi.culture_link.board.model.dto;

import lombok.Data;


@Data
public class BoardDto {
    public int id;
    public int user_id;
    public String genre;
    public String image;
    public String title;
    public int content;
}
