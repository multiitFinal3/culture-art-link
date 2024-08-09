package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AdminCulturalPropertiesDAO {

    void insertDB(CulturalPropertiesDTO culturalPropertiesDTO);

    List<CulturalPropertiesDTO> selectDB(PageDTO pageDto);

    int selectCount();


//    List<CulturalPropertiesDTO> searchAPI(@Param("searchForm") CulturalPropertiesDTO searchForm);


    List<CulturalPropertiesDTO> searchDBCulturalProperties(String category, String name, String region, String dynasty, int start, int end);

    int searchCountCulturalProperties(String category, String name, String region, String dynasty);



    void deleteDBData(@Param("list") List<Integer> id);

    void updateDBData(@Param("list") List<CulturalPropertiesDTO> data);
	
	
	ArrayList<CulturalPropertiesDTO> findtotalDBData();
}
