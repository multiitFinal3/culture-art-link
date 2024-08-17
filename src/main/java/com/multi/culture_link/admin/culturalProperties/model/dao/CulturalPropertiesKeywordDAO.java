package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CulturalPropertiesKeywordDAO {

    void save(CulturalPropertiesKeywordDTO keywordDTO);
    void saveAll(List<CulturalPropertiesKeywordDTO> keywordDTOList);

    boolean existsByCulturalPropertiesId(int culturalPropertiesId);

}
