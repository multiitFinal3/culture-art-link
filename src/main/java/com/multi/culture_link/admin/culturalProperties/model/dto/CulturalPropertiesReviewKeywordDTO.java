package com.multi.culture_link.admin.culturalProperties.model.dto;

import lombok.Data;

@Data
public class CulturalPropertiesReviewKeywordDTO {


//    id INT AUTO_INCREMENT PRIMARY KEY,
//    cultural_properties_id INT not null,
//    keyword varchar(100),
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    private int id;
    private int culturalPropertiesId;
    private String keyword;

}
