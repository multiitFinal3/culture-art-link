package com.multi.culture_link.admin.culturalProperties.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class CulturalPropertiesDTO {


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
    private String nation;
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
//    private String imgUrl;
    private String mainImgUrl;
//    private String imgDesc;
//    private String videoUrl;
//    private String narrationUrl;

//    private String imgUrl;
//    private String imgDesc;
//    private String videoUrl;
//    private String narrationUrl;

    private List<String> imgUrl;
    private List<String> imgDesc;
    private List<String> videoUrl;
    private List<String> narrationUrl;


//    // imgUrl의 Setter: 리스트를 쉼표로 구분된 문자열로 변환하여 저장
//    public void setImgUrl(List<String> imgUrlList) {
//        this.imgUrl = String.join(",", imgUrlList);
//    }
//
//    // imgUrl의 Getter: 문자열을 쉼표로 구분하여 리스트로 변환하여 반환
//    public List<String> getImgUrl() {
//        return imgUrl != null ? Arrays.asList(imgUrl.split(",")) : Collections.emptyList();
//    }
//
//
//    // imgDesc의 Setter: 리스트를 쉼표로 구분된 문자열로 변환하여 저장
//    public void setImgDesc(List<String> imgDescList) {
//        this.imgDesc = String.join(",", imgDescList);
//    }
//
//    // imgDesc의 Getter: 문자열을 쉼표로 구분하여 리스트로 변환하여 반환
//    public List<String> getImgDesc() {
//        return imgDesc != null ? Arrays.asList(imgDesc.split(",")) : Collections.emptyList();
//    }
//
//    // videoUrl의 Setter: 리스트를 쉼표로 구분된 문자열로 변환하여 저장
//    public void setVideoUrl(List<String> videoUrlList) {
//        this.videoUrl = String.join(",", videoUrlList);
//    }
//
//    // videoUrl의 Getter: 문자열을 쉼표로 구분하여 리스트로 변환하여 반환
//    public List<String> getVideoUrl() {
//        return videoUrl != null ? Arrays.asList(videoUrl.split(",")) : Collections.emptyList();
//    }
//
//    // narrationUrl의 Setter: 리스트를 쉼표로 구분된 문자열로 변환하여 저장
//    public void setNarrationUrl(List<String> narrationUrlList) {
//        this.narrationUrl = String.join(",", narrationUrlList);
//    }
//
//    // narrationUrl의 Getter: 문자열을 쉼표로 구분하여 리스트로 변환하여 반환
//    public List<String> getNarrationUrl() {
//        return narrationUrl != null ? Arrays.asList(narrationUrl.split(",")) : Collections.emptyList();
//    }


//    private List<ImgInfo> imgInfos;



//    public void setImgUrl(List<String> imgUrl) {
//        this.imgUrl = imgUrl;
//    }





}
