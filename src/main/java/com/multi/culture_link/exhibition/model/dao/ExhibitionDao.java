package com.multi.culture_link.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExhibitionDao {

    List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams);
    ExhibitionApiDto getExhibitionById(int id);
    // 찜, 관심없음 변동
    void setInterested(int useId, int exhibitionId, String state);
    // 전시 리스트 관심 연동하여 가져오기
    List<ExhibitionDto> getExhibition();
    
    // 찜, 관심없음 리스트
    List<ExhibitionApiDto> getUserInterestedExhibitions(int id);

}
