package com.multi.culture_link.board.model.dao;

import com.multi.culture_link.board.model.dto.BoardDto;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


//
@Mapper
public interface BoardDao {
    void setBoard(BoardDto data, int userId);
    List<BoardDto> getBoardList(String genre, String query);
    void deleteBoard(BoardDto data, int userId);
    void updateBoard(BoardDto data, int userId);
    BoardDto getBoardDetail(int boardId, int userId);
}