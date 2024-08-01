package com.multi.culture_link.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CulturalPropertiesDAO {

    List<CulturalPropertiesDTO> listCulturalProperties(@Param("offset") int offset, @Param("limit") int limit);
    List<CulturalPropertiesDTO> listAllCulturalProperties();

    int getTotalCount();

    CulturalPropertiesDTO getCulturalPropertyById(int id);

//    void addLike(CulturalPropertiesInterestDTO interest);
//    void addDislike(CulturalPropertiesInterestDTO interest);
//    List<CulturalPropertiesInterestDTO> getInterestsByUser(int userId);


//    //이걸로
//    // 찜 추가 또는 관심없음 추가
//    void addInterest(CulturalPropertiesInterestDTO interest);
//
//    // 찜 여부 확인
//    boolean isLiked(CulturalPropertiesInterestDTO interest);
//
//    // 관심없음 여부 확인
//    boolean isDisliked(CulturalPropertiesInterestDTO interest);
//
//    // 찜 또는 관심없음 삭제
//    void removeInterest(CulturalPropertiesInterestDTO interest);
//
//    // 사용자의 찜 목록 가져오기
//    List<CulturalPropertiesInterestDTO> getInterestsByUser(int userId);
}
