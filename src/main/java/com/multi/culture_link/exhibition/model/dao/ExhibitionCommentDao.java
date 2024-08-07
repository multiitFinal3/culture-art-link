package com.multi.culture_link.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExhibitionCommentDao {

    List<ExhibitionCommentDto> getComment(ExhibitionCommentDto data);
    List<ExhibitionCommentDto> getCommentAll();
    void createComment(ExhibitionCommentDto data);
    void deleteComment(ExhibitionCommentDto data);
    double getAverageRating(int exhibitionId);
    List<ExhibitionCommentDto> getUserComment(int userId);

}
