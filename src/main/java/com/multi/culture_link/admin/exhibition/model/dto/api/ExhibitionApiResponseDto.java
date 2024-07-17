package com.multi.culture_link.admin.exhibition.model.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

//@JacksonXmlRootElement(localName = "response")
@Data
public class ExhibitionApiResponseDto {
    @JsonProperty("header")
    private Header header;

    @JsonProperty("body")
    private Body body;

    // getters and setters for header and body

    @Data
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;

        // getters and setters for resultCode and resultMsg
    }

//    public static class Body {
//        @JacksonXmlProperty(localName = "items")
//        @JacksonXmlElementWrapper(useWrapping = false)
//        private List<Item> items;
//
//        // getter and setter for items
//    }

    @Data
    public static class Body {
        @JsonProperty("items")
        private Items items;

        @JsonProperty("numOfRows")
        private String numOfRows;

        @JsonProperty("pageNo")
        private String pageNo;

        @JsonProperty("totalCount")
        private String totalCount;

        // getters and setters
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<Item> item;

        // getter and setter
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("TITLE")
        private String title;

        @JsonProperty("CNTC_INSTT_NM")
        private String cntcInsttNm;

        @JsonProperty("COLLECTED_DATE")
        private String collectedDate;

        @JsonProperty("ISSUED_DATE")
        private String issuedDate;

        @JsonProperty("DESCRIPTION")
        private String description;

        @JsonProperty("IMAGE_OBJECT")
        private String imageObject;

        @JsonProperty("LOCAL_ID")
        private String localId;

        @JsonProperty("URL")
        private String url;

        @JsonProperty("VIEW_COUNT")
        private String viewCount;

        @JsonProperty("SUB_DESCRIPTION")
        private String subDescription;

        @JsonProperty("SPATIAL_COVERAGE")
        private String spatialCoverage;

        @JsonProperty("EVENT_SITE")
        private String eventSite;

        @JsonProperty("GENRE")
        private String genre;

        @JsonProperty("DURATION")
        private String duration;

        @JsonProperty("NUMBER_PAGES")
        private String numberPages;

        @JsonProperty("TABLE_OF_CONTENTS")
        private String tableOfContents;

        @JsonProperty("AUTHOR")
        private String author;

        @JsonProperty("CONTACT_POINT")
        private String contactPoint;

        @JsonProperty("ACTOR")
        private String actor;

        @JsonProperty("CONTRIBUTOR")
        private String contributor;

        @JsonProperty("AUDIENCE")
        private String audience;

        @JsonProperty("CHARGE")
        private String charge;

        @JsonProperty("PERIOD")
        private String period;

        @JsonProperty("EVENT_PERIOD")
        private String eventPeriod;

    }
}