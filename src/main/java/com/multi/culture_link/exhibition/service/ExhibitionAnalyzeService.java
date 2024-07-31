package com.multi.culture_link.exhibition.service;

import com.multi.culture_link.exhibition.model.dao.ExhibitionAnalyzeDao;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionAnalyzeService {
    private final ExhibitionAnalyzeDao exhibitionAnalyzeDao;

    public List<ExhibitionAnalyzeDto> getAnalyze(int exhibitionId) {
        return exhibitionAnalyzeDao.getAnalyze(exhibitionId);
    }

    public void createAnalyze(ExhibitionAnalyzeDto data) {
        exhibitionAnalyzeDao.createAnalyze(data);
    }

    public void updateAnalyze(ExhibitionAnalyzeDto data) {
        exhibitionAnalyzeDao.updateAnalyze(data);
    }

    public void deleteAnalyze(int userId, int exhibitionId) {
        ExhibitionAnalyzeDto data = new ExhibitionAnalyzeDto();
        data.setUserId(userId);
        data.setExhibitionId(exhibitionId);
        exhibitionAnalyzeDao.deleteAnalyze(userId, exhibitionId);
    }


}
