package com.multi.culture_link.admin.culturalProperties.model.dto;

import lombok.Data;

@Data
public class KeywordDTO {


    private int id;
    private String keyword;
    private String categoryName;
    private String keywordType;  // 'content' or 'review'
    private int selectCount;

}
