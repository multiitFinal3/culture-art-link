package com.multi.culture_link.exhibition.service;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dao.ExhibitionCommentDao;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionCommentService {
    private final ExhibitionCommentDao ExhibitionCommentDao;

    public List<ExhibitionCommentDto> getComment(ExhibitionCommentDto data) {
        return ExhibitionCommentDao.getComment(data);
    }

    public void createComment(ExhibitionCommentDto data) {
        ExhibitionCommentDao.createComment(data);
    }

    public void deleteComment(ExhibitionCommentDto data) {
        ExhibitionCommentDao.deleteComment(data);
    }
    public double getAverageRating(int exhibitionId){
        return ExhibitionCommentDao.getAverageRating(exhibitionId);
    };
}
