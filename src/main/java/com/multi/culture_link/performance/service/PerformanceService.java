package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.performance.model.dto.PerformanceAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

