package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCulturalPropertiesDAO {

    void insertDB(CulturalPropertiesDTO culturalPropertiesDTO);

    List<CulturalPropertiesDTO> selectDB(PageDTO pageDto);

    int selectCount();

//    List<CulturalPropertiesDTO> searchCulturalProperties(
//            @Param("categoryName") String categoryName,
//            @Param("culturalPropertiesName") String culturalPropertiesName,
//            @Param("region") String region,
//            @Param("dynasty") String dynasty,
//            @Param("start")int start, @Param("end")int end);

    List<CulturalPropertiesDTO> searchCulturalProperties(
            CulturalPropertiesDTO culturalPropertiesDTO);

}
