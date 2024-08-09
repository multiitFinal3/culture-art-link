package com.multi.culture_link.admin.performance.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String formattedDate; // 포맷된 날짜 (시작일자 - 종료일자)

    private String rank;

    private double latitude; // 위도
    private double longitude; // 경도


    // formattedDate 필드를 업데이트하는 메소드
    public void updateFormattedDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 입력된 날짜 형식과 변환할 날짜 형식을 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        LocalDateTime start = LocalDateTime.parse(startDate, dateTimeFormatter); // 문자열 형식의 날짜를 LocalDateTime 객체로 변환
        LocalDateTime end = LocalDateTime.parse(endDate, dateTimeFormatter);

        this.formattedDate = start.format(formatter) + " - " + end.format(formatter); // LocalDateTime 객체를 원하는 형식의 문자열로 변환하여 formattedDate 필드에 저장
    }



}