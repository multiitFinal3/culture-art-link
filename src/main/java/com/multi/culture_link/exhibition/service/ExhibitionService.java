package com.multi.culture_link.exhibition.service;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionService {
    private final ExhibitionDao ExhibitionDao;

    public List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams) {
        return ExhibitionDao.searchExhibition(searchParams);
    }

    public ExhibitionApiDto getExhibitionById(int id){
        return ExhibitionDao.getExhibitionById(id);
    }

    public void setInterested(int useId, int exhibitionId, String state){
        ExhibitionDao.setInterested(useId, exhibitionId, state);
    }

    public List<ExhibitionDto> getExhibition(){
        return ExhibitionDao.getExhibition();
    }

    public List<ExhibitionApiDto> getUserInterestedExhibitions(int id){
        return ExhibitionDao.getUserInterestedExhibitions(id);
    }



}
