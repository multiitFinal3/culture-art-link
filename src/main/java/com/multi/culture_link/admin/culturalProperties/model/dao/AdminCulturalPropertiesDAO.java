package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

@Mapper
public interface AdminCulturalPropertiesDAO {

    void insertDB(CulturalPropertiesDTO culturalPropertiesDTO);

    List<CulturalPropertiesDTO> selectDB();

    int selectCount();
}