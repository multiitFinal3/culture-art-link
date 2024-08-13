package com.multi.culture_link.culturalProperties.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 생성
public class NewsArticle {

//    private int id;
    private String title;
    private String link;
    private String date;
    private String content;
    private String imgUrl;




}
