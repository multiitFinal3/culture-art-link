package com.multi.culture_link.admin.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ExhibitionKeywordDao {
    void saveExhibitionKeyword(int exhibitionId, String keyword, int frequency);
    void saveExhibitionCommentKeyword(int exhibitionId, String keyword, int frequency);

    List<ExhibitionKeywordDto> getExhibtionKeyword(int exhibitionId);
    List<ExhibitionKeywordDto> getExhibtionCommentKeyword(int exhibitionId);

}
