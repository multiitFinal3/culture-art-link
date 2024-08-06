package com.multi.culture_link.board.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.board.model.dto.BoardDto;
import com.multi.culture_link.board.service.BoardService;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionInterestDto;
import com.multi.culture_link.exhibition.service.ExhibitionAnalyzeService;
import com.multi.culture_link.exhibition.service.ExhibitionCommentService;
import com.multi.culture_link.exhibition.service.ExhibitionService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping()
    public void createBoard(
            @RequestBody BoardDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        System.out.println("data : "+data);
        boardService.insertBoard(data, currentUser.getUserId());
    }

    @GetMapping()
    public List<BoardDto> getBoardList(
            @RequestParam(required = false, defaultValue = "all") String genre,
            @RequestParam(required = false) String query
    ) {
        return boardService.getList(genre, query);
    }

    @DeleteMapping()
    public void deleteBoard(
            @RequestBody BoardDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        boardService.deleteBoard(data, currentUser);
    }

    @PatchMapping()
    public void updateBoard(
            @RequestBody BoardDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        boardService.updateBoard(data, currentUser);
    }

    @GetMapping("/{boardId}")
    public BoardDto selectBoard(
            @RequestBody BoardDto data,
            @PathVariable int boardId,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        return boardService.selectBoard(data, currentUser);

    }




}
