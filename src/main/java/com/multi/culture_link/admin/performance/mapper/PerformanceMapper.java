package com.multi.culture_link.admin.performance.mapper;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.performance.model.dto.PerformanceAddDTO;
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




    // 공연명으로 검색하는 메서드
    PerformanceDTO getPerformanceByTitle(@Param("performanceTitle") String performanceTitle);





    //
    PerformanceAddDTO findByUserIdAndPerformanceId(@Param("userId") int userId, @Param("performanceId") int performanceId);
    void updatePerformanceAdd(PerformanceAddDTO performanceAddDTO);
    void insertPerformanceAdd(PerformanceAddDTO performanceAddDTO);

    String getPerformanceLikeState(int userId, int performanceId);



    // 검색
    // keyword와 genre로 공연 검색
    List<PerformanceDTO> searchPerformancesByKeywordAndGenre(@Param("keyword") String keyword, @Param("genre") String genre);




}