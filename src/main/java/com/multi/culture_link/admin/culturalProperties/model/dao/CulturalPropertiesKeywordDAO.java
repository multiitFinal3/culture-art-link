package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesReviewKeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CulturalPropertiesKeywordDAO {

    void save(CulturalPropertiesKeywordDTO keywordDTO);
    void saveAll(List<CulturalPropertiesKeywordDTO> keywordDTOList);

    boolean existsByCulturalPropertiesId(int culturalPropertiesId);


    List<String> getReviewContentsByCulturalPropertiesId(int culturalPropertiesId);
    boolean existsByReviewCulturalPropertiesId(int culturalPropertiesId);

    List<Integer> getExistingKeywordIds(int culturalPropertiesId);
    void updateExistingKeywords(@Param("keywordList") List<Map<String, Object>> keywordList);
    void insertNewKeywords(@Param("culturalPropertiesId") int culturalPropertiesId, @Param("keywords") List<String> keywords);
}
