package com.multi.culture_link.board.service;

import com.multi.culture_link.board.model.dao.BoardCommentDao;
import com.multi.culture_link.board.model.dto.BoardCommentDto;
import com.multi.culture_link.exhibition.model.dao.ExhibitionCommentDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardCommentService {
    private final BoardCommentDao boardCommentDao;

    public List<BoardCommentDto> getComment(BoardCommentDto data) {
        return boardCommentDao.getComment(data);
    }

    public void createComment(BoardCommentDto data) {
        boardCommentDao.createComment(data);
    }

    public void deleteComment(BoardCommentDto data) {
        boardCommentDao.deleteComment(data);
    }
}
