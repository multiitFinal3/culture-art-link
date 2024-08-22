package com.multi.culture_link.admin.performance.mapper;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.performance.model.dto.PerformanceAddDTO;
import com.multi.culture_link.performance.model.dto.PerformanceKeywordDTO;
import com.multi.culture_link.performance.model.dto.PerformanceReviewDTO;
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

//    String getPerformanceLikeState(int userId, int performanceId);
String getPerformanceLikeState(@Param("userId") int userId, @Param("performanceId") int performanceId);









    // 검색
    // keyword와 genre로 공연 검색
     List<PerformanceDTO> searchPerformancesByKeywordAndGenre(@Param("keyword") String keyword, @Param("genre") String genre);









    // 리뷰
    // 특정 공연의 모든 리뷰를 조회
    List<PerformanceReviewDTO> findReviewsByPerformanceId(@Param("performanceId") int performanceId);

    // 리뷰를 데이터베이스에 삽입

    void insertReview(PerformanceReviewDTO review);

    // 특정 리뷰를 업데이트
    void updateReview(PerformanceReviewDTO review);

    // 특정 리뷰를 삭제
    void deleteReview(@Param("id") int id);
    // 리뷰 관련 메서드







    // 추천관련 메서드
    List<PerformanceDTO> findRecommendedPerformances(@Param("userId") int userId , @Param("genre") String genre);








    // 찜 좋아요 가져오기 - 메인 페이지
    List<PerformanceDTO> findLovedPerformancesByUserId(@Param("userId") int userId);

    List<PerformanceDTO> findHatedPerformancesByUserId(@Param("userId") int userId);





    // 내가 쓴 리뷰 가져오기 ㅡㅜ휴ㅡㅠㅡㅜㅠㅡㅠㅡㅠㅡㅠ
    List<PerformanceReviewDTO> findReviewsByUserId(@Param("userId") int userId);





    // 회원가입할때 공연 장르 넣는 거
    void insertPerformanceKeyword(PerformanceKeywordDTO performanceKeyword);




    // 사용자 찜한 공연 키워드를 가져오는 메서드
    List<PerformanceKeywordDTO> findLoveKeywordsByUserId(@Param("userId") int userId);

    // 사용자 관심없음 공연 키워드를 가져오는 메서드
    List<PerformanceKeywordDTO> findHateKeywordsByUserId(@Param("userId") int userId);

}