package com.multi.culture_link.admin.performance.model.dto;

import lombok.Data;

@Data
public class PerformanceDTO {
    private int id;
    private String code;
    private String title;
    private String status;
    private String startDate;
    private String endDate;
    private String location;
    private int regionId;
    private String casting;
    private String runtime;
    private String age;
    private String organizer;
    private String producer;
    private String price;
    private String imageMain;
    private String imageDetail1;
    private String imageDetail2;
    private String imageDetail3;
    private String imageDetail4;
    private String genre;
    private String ticketing;
    private String ticketingUrl;
    private double avg;
    private String createdAt;
    private String updatedAt;


}
