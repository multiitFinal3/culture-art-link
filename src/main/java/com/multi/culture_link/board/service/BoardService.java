package com.multi.culture_link.board.service;

import com.google.gson.Gson;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.board.model.dao.BoardDao;
import com.multi.culture_link.board.model.dto.BoardDto;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.culturalProperties.model.dto.YoutubeConfig;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardDao boardDao;

    public void insertBoard(BoardDto data, int userId) {
        boardDao.setBoard(data, userId);
    }


    public List<BoardDto> getList(String genre, String query) {
        return boardDao.getBoardList(genre, query);
    }

    public void deleteBoard(BoardDto data, int userId) {
        boardDao.deleteBoard(data, userId);
    }

    public void updateBoard(BoardDto data, int userId) {
        boardDao.updateBoard(data, userId);
    }

    public BoardDto selectBoard(int boardId, int userId) {
        return boardDao.getBoardDetail(boardId, userId);
    }
}
