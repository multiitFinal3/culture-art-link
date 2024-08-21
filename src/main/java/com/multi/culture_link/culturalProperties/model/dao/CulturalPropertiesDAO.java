package com.multi.culture_link.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesReviewDTO;
import com.multi.culture_link.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface CulturalPropertiesDAO {

    List<CulturalPropertiesDTO> listCulturalProperties(@Param("offset") int offset, @Param("limit") int limit);
    List<CulturalPropertiesDTO> listAllCulturalProperties();

    int getTotalCount();

    CulturalPropertiesDTO getCulturalPropertyById(int id);

    void addInterest(CulturalPropertiesInterestDTO interest);
    void removeInterest(CulturalPropertiesInterestDTO interest);


    // 사용자 찜 정보 가져오기
    List<CulturalPropertiesInterestDTO> getInterest(@Param("userId") int userId);


    List<CulturalPropertiesDTO> searchCulturalProperties(String category, String culturalPropertiesName, String region, String dynasty);
    long countCulturalProperties(String category, String culturalPropertiesName, String region, String dynasty);

    List<String> getAllCategories();



    List<CulturalPropertiesDTO> getNearbyPlace(@Param("region") String region, @Param("district") String district, @Param("id") int id);

    List<CulturalPropertiesDTO> getRandomPlace(@Param("region") String region, @Param("id") int id);

    // 사용자 찜 정보 가져오기
    List<CulturalPropertiesInterestDTO> getDetailInterest(@Param("culturalPropertiesId") int culturalPropertiesId, @Param("userId") int userId);



    void addReview(CulturalPropertiesReviewDTO reviewDTO);

    List<CulturalPropertiesReviewDTO> getReviewsByCulturalPropertyId(@Param("culturalPropertiesId") int culturalPropertiesId);

    CulturalPropertiesReviewDTO findByReviewId(int id);
    void deleteReview(int id);

    void updateReview(CulturalPropertiesReviewDTO review);


    List<CulturalPropertiesReviewDTO> getReviewList(@Param("culturalPropertiesId") int culturalPropertiesId, @Param("pageable") Pageable pageable);
    int countReview(@Param("culturalPropertiesId") int culturalPropertiesId);


    // 평균 평점 조회
    double averageRating(int culturalPropertiesId);

    List<CulturalPropertiesReviewDTO> getRecentReview(int culturalPropertiesId);

    List<String> getKeywordsByCulturalPropertyId(int culturalPropertiesId);

    List<String> getReviewKeywordsByCulturalPropertyId(int culturalPropertiesId);

    List<CulturalPropertiesReviewDTO> getMyReviews(@Param("userId") int userId, @Param("pageable") Pageable pageable);
    int countMyReviews(@Param("userId") int userId);
    void editReview(CulturalPropertiesReviewDTO review);


    List<CulturalPropertiesDTO> getUserInterest(@Param("userId") int userId, @Param("interestType") String interestType);


    void addUserInterest(CulturalPropertiesInterestDTO interest);
    void removeUserInterest(@Param("userId") int userId, @Param("culturalPropertiesId") int culturalPropertiesId);




}
