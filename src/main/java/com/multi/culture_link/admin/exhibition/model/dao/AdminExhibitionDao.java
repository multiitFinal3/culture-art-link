package com.multi.culture_link.admin.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminExhibitionDao {
    void saveData(@Param("processedData") List<ExhibitionApiDto> processedData);
    void deleteData(List<Integer> id);
    void updateData(int id, String data);
    List<ExhibitionApiDto> getData();


}
