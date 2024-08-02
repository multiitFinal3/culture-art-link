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

    void addInterest(CulturalPropertiesInterestDTO interest);
    void removeInterest(CulturalPropertiesInterestDTO interest);


    // 사용자 찜 정보 가져오기
    List<CulturalPropertiesInterestDTO> getInterest(@Param("userId") int userId);


}
