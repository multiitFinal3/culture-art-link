package com.multi.culture_link.board.model.dao;

import com.multi.culture_link.board.model.dto.BoardCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardCommentDao {

    List<BoardCommentDto> getComment(BoardCommentDto data);
    List<BoardCommentDto> getCommentAll();
    void createComment(BoardCommentDto data);
    void deleteComment(BoardCommentDto data);

}
