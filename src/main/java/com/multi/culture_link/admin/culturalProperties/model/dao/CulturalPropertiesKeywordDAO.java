package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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



//    List<KeywordDTO> getInitialKeywords();

    List<KeywordDTO> getKeywords(@Param("excludedKeywordIds") List<Integer> excludedKeywordIds, @Param("limit") int limit);

    void saveUserKeyword(@Param("userId") int userId, @Param("keyword") String keyword, @Param("count") int count);




    int getTotalKeywordCount();


    List<KeywordDTO> getLikeKeyword(@Param("userId") int userId);
    List<KeywordDTO> getDislikeKeyword(@Param("userId") int userId);
    List<KeywordDTO> getUnselectedKeywords(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);



    void insertUserSelectKeyword(@Param("userId") int userId, @Param("interestType") String interestType,  @Param("keyword") String keyword, @Param("count") int count);

    void deleteUserSelectKeywordByType(@Param("userId") int userId, @Param("interestType") String interestType);


    List<String> getRecommendedKeywords(int userId);

    List<CulturalPropertiesDTO> getRandomCulturalPropertiesByKeywords(@Param("keywords") List<String> keywords, @Param("limit") int limit);

}
