package com.multi.culture_link.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExhibitionDao {
    List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams);
}
