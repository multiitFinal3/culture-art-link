package com.multi.culture_link.board.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.board.model.dto.BoardCommentDto;
import com.multi.culture_link.board.model.dto.BoardDto;
import com.multi.culture_link.board.service.BoardCommentService;
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
    private final BoardCommentService boardcommentService;

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
        boardService.deleteBoard(data, currentUser.getUserId());
    }

    @PatchMapping()
    public void updateBoard(
            @RequestBody BoardDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        boardService.updateBoard(data, currentUser.getUserId());
    }

    @GetMapping("/{boardId}")
    public BoardDto selectBoard(
            @PathVariable int boardId,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        return boardService.selectBoard(boardId, currentUser.getUserId());

    }







    // 댓글 목록 가져오기
    @GetMapping("/{boardId}/comment")
    public List<BoardCommentDto> getComment(
            @PathVariable int boardId,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        BoardCommentDto data = new BoardCommentDto();
        data.setUserId(currentUser.getUserId());
        data.setBoardId(boardId);
        return boardcommentService.getComment(data);
    }

    // 댓글 작성
    @PostMapping("/{boardId}/comment")
    public void setComment(
            @RequestBody BoardCommentDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int boardId
    ) {
        data.setBoardId(boardId);
        data.setUserId(currentUser.getUserId());

        System.out.println("data : "+ data);
        boardcommentService.createComment(data);
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}/comment")
    public void deleteComment(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int boardId,
            @RequestBody BoardCommentDto data

    ) {
        data.setBoardId(boardId);
        data.setUserId(currentUser.getUserId());
        boardcommentService.deleteComment(data);
    }




}
