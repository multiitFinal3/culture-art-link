package com.multi.culture_link.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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


    List<CulturalPropertiesDTO> searchCulturalProperties(@Param("category") String category,
                                                    @Param("culturalPropertiesName") String culturalPropertiesName,
                                                    @Param("region") String region,
                                                    @Param("dynasty") String dynasty,
                                                    @Param("offset") int offset,
                                                    @Param("pageSize") int pageSize);

    long countCulturalProperties(@Param("category") String category,
                                 @Param("culturalPropertiesName") String culturalPropertiesName,
                                 @Param("region") String region,
                                 @Param("dynasty") String dynasty);

    List<String> getAllCategories();




//    CulturalPropertiesDTO getCulturalPropertyById(int id);

    // 특정 문화재 ID로 조회
//    CulturalPropertiesDTO getCulturalPropertyById(@Param("id") int id);

//    CulturalPropertiesDTO getCulturalPropertyById(int userId, int id);


    List<CulturalPropertiesDTO> getNearbyPlace(@Param("region") String region, @Param("district") String district, @Param("id") int id);

    List<CulturalPropertiesDTO> getRandomPlace(@Param("region") String region, @Param("id") int id);


//    void likeAttraction(int id);
//    void dislikeAttraction(int id);

//    void insertInterest(@Param("userId") int userId,
//                     @Param("id") int id,
//                     @Param("interestType") String interestType);
//
//    void deleteInterest(@Param("userId") int userId,
//                        @Param("id") int id);
//
//    boolean isLiked(@Param("userId") int userId,
//                    @Param("id") int id);
//
//    boolean isDisliked(@Param("userId") int userId,
//                       @Param("id") int id);



}
