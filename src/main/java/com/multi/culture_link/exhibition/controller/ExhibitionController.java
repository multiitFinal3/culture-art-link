package com.multi.culture_link.exhibition.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionInterestDto;
import com.multi.culture_link.exhibition.service.ExhibitionCommentService;
import com.multi.culture_link.exhibition.service.ExhibitionService;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
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
@RequestMapping("/exhibition")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;
    private final ExhibitionCommentService exhibitionCommentService;
    
    // 상세 검색
    @GetMapping("/search-exhibition")
    public List<ExhibitionApiDto> searchExhibition(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String museum) {

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("title", title);
        searchParams.put("artist", artist);
        searchParams.put("museum", museum);

        return exhibitionService.searchExhibition(searchParams);
    }

    // 전시 id 기반 데이터 불러오기(상세정보)
    @GetMapping("/{exhibitionId}")
    public ExhibitionDto getExhibitionDetail(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ){
        System.out.println("exhibition: " + exhibitionService.getExhibitionById(currentUser.getUserId(), exhibitionId));
        return exhibitionService.getExhibitionById(currentUser.getUserId(), exhibitionId);
    }

    // 데이터별 상세 정보 url 연결
    @GetMapping("/detail-url")
    public ResponseEntity<String> proxyRequest(@RequestParam String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    return ResponseEntity.ok(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("");
            return ResponseEntity.status(500).body("Error fetching the page");
        }
        return ResponseEntity.status(500).body("Unknown error occurred");
    }

    // 찜, 관심없음 설정
    @PostMapping("/interested")
    public void setInterested(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @RequestBody ExhibitionInterestDto data
            ) {
//        System.out.println("컨트롤러");
//        System.out.println("1: " + currentUser + " 2: " +  data.getExhibitionId()  + " 3: " + data.getState());
        exhibitionService.setInterested(currentUser.getUserId(), data.getExhibitionId(), data.getState());
    }

    // 관심도 연결한 전시 list 불러오기
    @GetMapping("/exhibition")
    public List<ExhibitionDto> getDbExhibitions(){
        return exhibitionService.getExhibition();
    }

    // 댓글 목록 가져오기
    @GetMapping("/exhibition/{exhibitionId}/comment")
    public List<ExhibitionCommentDto> getComment(
            @PathVariable int exhibitionId
    ) {
        return exhibitionCommentService.getComment(exhibitionId);
    }

    // 댓글 작성
    @PostMapping("/exhibition/{exhibitionId}/comment")
    public void setComment(
            @RequestBody ExhibitionCommentDto comment,
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ) {
        comment.setExhibitionId(exhibitionId);
        comment.setUserId(currentUser.getUserId());
        exhibitionCommentService.createComment(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/exhibition/{exhibitionId}/comment")
    public void deleteComment(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ) {
        System.out.println("exhibition: " + exhibitionCommentService.getComment(exhibitionId));
        exhibitionCommentService.deleteComment(currentUser.getUserId(), exhibitionId);
    }

}
