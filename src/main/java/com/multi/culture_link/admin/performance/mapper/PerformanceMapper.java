package com.multi.culture_link.admin.performance.mapper;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PerformanceMapper {
    List<PerformanceDTO> getAllPerformances();

    void insertPerformance(PerformanceDTO performance);

    int deletePerformances(List<String> selectedIds);

    List<PerformanceDTO> searchPerformances(String keyword);

    List<PerformanceDTO> getPerformancesByGenre(String genre);


    // 상세정보가져오려고
    //PerformanceDTO getPerformanceByCode(String performanceCode);
    PerformanceDTO getPerformanceByCode(@Param("performanceCode") String performanceCode);




    // 추가: 공연명으로 검색하는 메서드
    //PerformanceDTO getPerformanceByTitle(String performanceTitle);
    PerformanceDTO getPerformanceByTitle(@Param("performanceTitle") String performanceTitle);



}