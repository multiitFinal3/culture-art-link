package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AdminCulturalPropertiesDAO {

    void insertDB(CulturalPropertiesDTO culturalPropertiesDTO);

//    List<CulturalPropertiesDTO> selectDB();

    int selectCount();

    List<CulturalPropertiesDTO> selectDB(@Param("startIndex") int startIndex, @Param("endIndex") int endIndex);

//    List<CulturalPropertiesDTO> selectCulturalProperties(
//            @Param("page") int page,
//            @Param("itemsPerPage") int itemsPerPage);

//    ArrayList<CulturalPropertiesDTO> getDbPage(int startIdx, int itemsPerPage);
}
