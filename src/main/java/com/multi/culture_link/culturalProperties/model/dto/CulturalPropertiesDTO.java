package com.multi.culture_link.culturalProperties.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CulturalPropertiesDTO extends PageDTO {


//    private int id;
//    private String categoryCode;         - ccbaKdcd
//    private String managementNumber;     - ccbaAsno
//    private String cityCode;             - ccbaCtcd
//    private String nation;
//    private String categoryName;         - ccmaName
//    private String culturalPropertiesName;   - ccbaMnm1
//    private String address;        - ccbaLcad
//    private String region;           - ccbaCtcdNm
//    private String district;         - ccsiName
//    private String dynasty;           - ccceName
//    private String registrationDate;   -ccbaAsdt
//    private String longitude;          - longitude
//    private String latitude;           - latitude
//    private String classifyA;           - gcodeName
//    private String classifyB;           - bcodeName
//    private String classifyC;           - mcodeName
//    private String classifyD;            - scodeName
//    private String content;            - content
//    //    private String imgUrl;
//    private String mainImgUrl;      -  imageUrl
////    private String imgDesc;
////    private String videoUrl;
////    private String narrationUrl;
//
//    private List<String> imgUrl;
//    private List<String> imgDesc;
//    private List<String> videoUrl;
//    private List<String> narrationUrl;

    private int id;
    private String categoryCode;
    private String managementNumber;
    private String cityCode;
    private String categoryName;
    private String culturalPropertiesName;
    private String address;
    private String region;
    private String district;
    private String dynasty;
    private String registrationDate;
    private String longitude;
    private String latitude;
    private String classifyA;
    private String classifyB;
    private String classifyC;
    private String classifyD;
    private String content;
    private String mainImgUrl;

//    private String imgUrl;
//    private String imgDesc;
//    private String videoUrl;
//    private String narrationUrl;

    private List<String> imgUrl;
    private List<String> imgDesc;
    private List<String> videoUrl;
    private List<String> narrationUrl;




    private PageDTO pageDTO;
    private int start;
    private int end;



}
