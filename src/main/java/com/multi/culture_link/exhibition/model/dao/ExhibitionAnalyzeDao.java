package com.multi.culture_link.exhibition.model.dao;

import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExhibitionAnalyzeDao {

    List<ExhibitionAnalyzeDto> getAnalyze(ExhibitionAnalyzeDto data);
    void createAnalyze(ExhibitionAnalyzeDto data);
    void updateAnalyze(ExhibitionAnalyzeDto data);
    void deleteAnalyze(ExhibitionAnalyzeDto data);

}


