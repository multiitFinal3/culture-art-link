package com.multi.culture_link.culturalProperties.model.dto;

import lombok.Data;

@Data
public class CulturalPropertiesInterestDTO {

    private int id;
    private int culturalPropertiesId;
    private int userId;
    private String interestType;

}
