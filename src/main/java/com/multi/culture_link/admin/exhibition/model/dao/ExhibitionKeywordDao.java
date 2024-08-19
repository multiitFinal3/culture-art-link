package com.multi.culture_link.admin.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordPageDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExhibitionKeywordDao {
    void saveExhibitionKeyword(int exhibitionId, String keyword, int frequency);

    void saveExhibitionCommentKeyword(int exhibitionId, String keyword, int frequency);

    List<ExhibitionKeywordDto> getExhibitionKeyword(int exhibitionId);

    List<ExhibitionKeywordDto> getExhibitionCommentKeyword(int exhibitionId);

    List<ExhibitionKeywordPageDto> getExhibitionAllKeyword(String nextCursor, int size);

    List<ExhibitionKeywordPageDto> getExhibitionAllKeywordByUser(String nextCursor, int size, VWUserRoleDTO user, Boolean isUserSelected);

    List<String> getExhibitionKeywordById(int exhibitionId);

    void updateUserKeyword(int userId, String keyword, int countChange);

    List<String> findKeywordsByCount(int userId);

    List<Integer> findExhibitionIdByKeywords(List<String> keyword);

    List<ExhibitionDto> findAllById(List<Integer> exhibitionId);
}

