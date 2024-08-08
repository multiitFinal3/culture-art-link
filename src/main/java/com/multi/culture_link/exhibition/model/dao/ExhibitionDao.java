package com.multi.culture_link.exhibition.model.dao;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExhibitionDao {

    List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams);
    ExhibitionDto getExhibitionById(int userId, int exhibitionId);
    // 찜, 관심없음 변동
    void setInterested(int userId, int exhibitionId, String state);
    // 전시 리스트 관심 연동하여 가져오기
    List<ExhibitionDto> getExhibition(int userId);
    
    // 찜, 관심없음 리스트
//    List<ExhibitionApiDto> getUserInterestedExhibitions(int id);

    List<ExhibitionApiDto> getLikeExhibition(int userId);
    List<ExhibitionApiDto> getUnlikeExhibition(int userId);
    String getInterestState(int userId, int exhibitionId);

}
