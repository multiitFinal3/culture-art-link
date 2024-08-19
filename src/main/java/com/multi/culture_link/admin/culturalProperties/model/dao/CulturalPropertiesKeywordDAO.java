package com.multi.culture_link.admin.culturalProperties.model.dao;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesReviewKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
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



//    List<KeywordDTO> getKeywords(@Param("offset") int offset, @Param("limit") int limit);
//    void incrementKeywordSelectCount(@Param("keywordId") Long keywordId, @Param("keywordType") String keywordType);

    /**
     * 문화재 키워드를 가져오는 메서드
     * @param offset 페이지 시작 위치
     * @param limit  페이지당 키워드 수
     * @param keywordType 키워드 유형
     * @return 키워드 목록
     */
//    List<KeywordDTO> getKeywords(@Param("offset") int offset, @Param("limit") int limit, @Param("keywordType") String keywordType);

    /**
     * 키워드 선택 카운트를 증가시키는 메서드
     * @param keywordId 키워드 ID
     * @param keywordType 키워드 유형
     */
    void incrementKeywordSelectCount(@Param("keywordId") Long keywordId, @Param("keywordType") String keywordType);


    /**
     * 카테고리 목록을 가져오는 메서드
     * @return 카테고리 이름 목록
     */
//    List<String> getDistinctCategories();

    List<KeywordDTO> getKeywords(@Param("offset") int offset, @Param("limit") int limit,
                                 @Param("keywordType") String keywordType,
                                 @Param("keywordIndex") int keywordIndex);

    List<String> getDistinctCategories();

}
