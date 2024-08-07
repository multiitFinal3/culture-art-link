package com.multi.culture_link.exhibition.service;

import com.multi.culture_link.exhibition.model.dao.ExhibitionCommentDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionCommentService {
	private final ExhibitionCommentDao exhibitionCommentDao;
	
	public List<ExhibitionCommentDto> getComment(ExhibitionCommentDto data) {
		return exhibitionCommentDao.getComment(data);
	}
	
	public void createComment(ExhibitionCommentDto data) {
		exhibitionCommentDao.createComment(data);
	}
	
	public void deleteComment(ExhibitionCommentDto data) {
		exhibitionCommentDao.deleteComment(data);
	}
	
	public double getAverageRating(int exhibitionId) {
		return exhibitionCommentDao.getAverageRating(exhibitionId);
	}
	
	public List<ExhibitionCommentDto> getUserComment(int userId) {
		return exhibitionCommentDao.getUserComment(userId);
	}
}
