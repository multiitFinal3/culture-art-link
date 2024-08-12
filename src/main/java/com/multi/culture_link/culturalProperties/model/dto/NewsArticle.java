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


//    // 날짜 문자열을 Date 객체로 변환하는 메소드
//    public Date getParsedDate() {
//        return parseDate(this.date);
//    }
//
//    // "몇 시간 전", "몇 일 전" 형식을 Date로 변환하는 메소드
//    private Date parseDate(String dateString) {
//        try {
//            if (dateString.contains("시간 전")) {
//                int hours = Integer.parseInt(dateString.split(" ")[0]);
//                return new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(hours));
//            } else if (dateString.contains("일 전")) {
//                int days = Integer.parseInt(dateString.split(" ")[0]);
//                return new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days));
//            } else if (dateString.contains("초 전")) {
//                int seconds = Integer.parseInt(dateString.split(" ")[0]);
//                return new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(seconds));
//            } else if (dateString.equals("방금")) {
//                return new Date(); // 방금은 현재 시간
//            }
//            // 지원하지 않는 형식인 경우 현재 시간을 반환
//            return new Date();
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            return new Date(); // 예외 발생 시 현재 시간 반환
//        }
//    }

}
