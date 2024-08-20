package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.performance.model.dto.PerformanceAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : performanceService
 * @since : 2024. 8. 12.
 */


@Service
public class PerformanceService {

    @Autowired
    private PerformanceMapper performanceMapper;

    // 좋아요 싫어요
    public void updatePerformanceLikeState(PerformanceAddDTO performanceAddDTO) {
        PerformanceAddDTO existingRecord = performanceMapper.findByUserIdAndPerformanceId(performanceAddDTO.getUserId(), performanceAddDTO.getPerformanceId());

        if (existingRecord != null) {
            // 기존 레코드가 있으면 업데이트
            existingRecord.setState(performanceAddDTO.getState());
            performanceMapper.updatePerformanceAdd(existingRecord);
        } else {
            // 기존 레코드가 없으면 삽입
            PerformanceAddDTO newRecord = new PerformanceAddDTO();
            newRecord.setUserId(performanceAddDTO.getUserId());
            newRecord.setPerformanceId(performanceAddDTO.getPerformanceId());
            newRecord.setState(performanceAddDTO.getState());
            performanceMapper.insertPerformanceAdd(newRecord);
        }
    }

    public String getPerformanceLikeState(int userId, int performanceId) {
        // MyBatis를 통해 데이터베이스에서 상태를 조회
        String state = performanceMapper.getPerformanceLikeState(userId, performanceId);

        System.out.println("getPerformanceLikeState result: " + state);


        // 상태가 null인 경우 "none"을 반환하여 기본 상태로 설정
        if (state == null) {
            state = "none";
        }

        return state;
    }








    // 검색
    public List<PerformanceDTO> searchPerformances(String keyword, String genre) {
        return performanceMapper.searchPerformancesByKeywordAndGenre(keyword, genre);
    }

    public PerformanceDTO getPerformanceByTitle(String title) {
        return performanceMapper.getPerformanceByTitle(title);
    }




    public List<PerformanceDTO> getRecommendedPerformances(int userId) {
        List<PerformanceDTO> recommendedPerformances = new ArrayList<>();

        // 특정 장르 리스트 정의
        List<String> genres = Arrays.asList("연극", "뮤지컬", "무용", "대중무용", "서양음악(클래식)",
                "한국음악(국악)", "대중음악", "복합", "서커스/마술");

        for (String genre : genres) {
            List<PerformanceDTO> performances = performanceMapper.findRecommendedPerformances(userId, genre);
            // 무작위로 섞은 후 최대 3개 선택
            Collections.shuffle(performances);
            recommendedPerformances.addAll(performances.stream().limit(3).collect(Collectors.toList()));
        }

        // 최종 결과를 무작위로 섞기
        Collections.shuffle(recommendedPerformances);
        return recommendedPerformances.stream().limit(15).collect(Collectors.toList());
    }








    public List<PerformanceDTO> getLovePerformancesByUserId(int userId) {
        // 데이터베이스에서 찜한 공연 목록을 가져오는 로직 구현
        return performanceMapper.findLovedPerformancesByUserId(userId);
    }

    public List<PerformanceDTO> getHatePerformancesByUserId(int userId) {
        return performanceMapper.findHatedPerformancesByUserId(userId);
    }
}

