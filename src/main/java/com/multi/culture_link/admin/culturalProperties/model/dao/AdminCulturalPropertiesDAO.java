package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
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

//    List<CulturalPropertiesDTO> searchDBCulturalProperties(
//            CulturalPropertiesDTO culturalPropertiesDTO);


    List<CulturalPropertiesDTO> searchDBCulturalProperties(
            CulturalPropertiesDTO culturalPropertiesDTO);

    void deleteDBData(@Param("list") List<Integer> id);

    void updateDBData(@Param("list") List<CulturalPropertiesDTO> data);


//    List<CulturalPropertiesDTO> searchDBCulturalProperties(
//            PageDTO pageDto, CulturalPropertiesDTO culturalPropertiesDTO);

//    List<CulturalPropertiesDTO> searchDBCulturalProperties(
//            PageDTO pageDTO);

}
