package com.multi.culture_link.exhibition.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordPageDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.PageResponseDto;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionInterestDto;
import com.multi.culture_link.exhibition.service.ExhibitionAnalyzeService;
import com.multi.culture_link.exhibition.service.ExhibitionCommentService;
import com.multi.culture_link.exhibition.service.ExhibitionService;
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
    private final ExhibitionAnalyzeService exhibitionAnalyzeService;


    
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
//        System.out.println("exhibition: " + exhibitionService.getExhibitionById(currentUser.getUserId(), exhibitionId));
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
        exhibitionService.setInterested(currentUser.getUserId(), data.getExhibitionId(), data.getState());
    }

    // 관심도 연결한 전시 list 불러오기
    @GetMapping("/exhibition")
    public List<ExhibitionDto> getDbExhibitions(
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        return exhibitionService.getExhibition(currentUser.getUserId());
    }

    // 사용자가 찜한 전시회 목록
    @GetMapping("/liked")
    public List<ExhibitionApiDto> getLikedExhibition(
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        return exhibitionService.getLikeExhibition(currentUser.getUserId());
    }

    // 사용자가 관심없음으로 표시한 전시회 목록
    @GetMapping("/unliked")
    public List<ExhibitionApiDto> getUnlikeExhibition(
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        return exhibitionService.getUnlikeExhibition(currentUser.getUserId());
    }





    // 댓글 목록 가져오기
    @GetMapping("/exhibition/{exhibitionId}/comment")
    public List<ExhibitionCommentDto> getComment(
            @PathVariable int exhibitionId,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        ExhibitionCommentDto data = new ExhibitionCommentDto();
        data.setUserId(currentUser.getUserId());
        data.setExhibitionId(exhibitionId);
        return exhibitionCommentService.getComment(data);
    }

    // 댓글 작성
    @PostMapping("/exhibition/{exhibitionId}/comment")
    public void setComment(
            @RequestBody ExhibitionCommentDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ) {
        data.setExhibitionId(exhibitionId);
        data.setUserId(currentUser.getUserId());

        System.out.println("data : "+ data);
        exhibitionCommentService.createComment(data);
    }

    // 댓글 삭제
    @DeleteMapping("/exhibition/{exhibitionId}/comment")
    public void deleteComment(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId,
            @RequestBody ExhibitionCommentDto data

            ) {
        data.setExhibitionId(exhibitionId);
        data.setUserId(currentUser.getUserId());
        exhibitionCommentService.deleteComment(data);
    }


    // 분석 목록 가져오기
    @GetMapping("/exhibition/{exhibitionId}/analyze")
    public List<ExhibitionAnalyzeDto> getAnalyze(
            @PathVariable int exhibitionId,
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        ExhibitionAnalyzeDto data = new ExhibitionAnalyzeDto();
        data.setUserId(currentUser.getUserId());
        data.setExhibitionId(exhibitionId);
        return exhibitionAnalyzeService.getAnalyze(data);
    }

    // 분석 작성
    @PostMapping("/exhibition/{exhibitionId}/analyze")
    public void setAnalyze(
            @RequestBody ExhibitionAnalyzeDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ) {
        data.setExhibitionId(exhibitionId);
        data.setUserId(currentUser.getUserId());
        exhibitionAnalyzeService.createAnalyze(data);
    }

    // 분석 수정
    @PatchMapping("/exhibition/{exhibitionId}/analyze")
    public void updateAnalyze(
            @RequestBody ExhibitionAnalyzeDto data,
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId
    ) {
        data.setExhibitionId(exhibitionId);
        data.setUserId(currentUser.getUserId());
        exhibitionAnalyzeService.updateAnalyze(data);


    }


    // 분석 삭제
    @DeleteMapping("/exhibition/{exhibitionId}/analyze")
    public void deleteAnalyze(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            @PathVariable int exhibitionId,
            @RequestBody ExhibitionAnalyzeDto data
    ) {
        data.setExhibitionId(exhibitionId);
        data.setUserId(currentUser.getUserId());
        exhibitionAnalyzeService.deleteAnalyze(data);
    }

    // 사용자별 작성한 댓글 목록
    @GetMapping("/user-comment")
    public List<ExhibitionCommentDto> getUserComment(
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ) {
        return exhibitionCommentService.getUserComment(currentUser.getUserId());
    }



    @PostMapping("/videos")
    public List<Video> getVideos(@RequestParam String query) {
//        String query = title + " " + museum;
        List<Video> videos = exhibitionService.crawlYouTubeVideos(query);
        return videos.subList(0, Math.min(videos.size(), 2)); // 최대 2개의 비디오만 반환
    }

    @PostMapping("/exhibition/{exhibitionId}/rating")
    public double getAverageRating(
            @PathVariable int exhibitionId
     ) {
        return exhibitionCommentService.getAverageRating(exhibitionId);
    };



    @GetMapping("/exhibition/{exhibitionId}/keyword")
    public HashMap<String, List<ExhibitionKeywordDto>> getKeyword(
            @PathVariable int exhibitionId
    ) {
        return exhibitionService.getKeyword(exhibitionId);
    };

    @GetMapping("/exhibition/keyword")
    public PageResponseDto<ExhibitionKeywordPageDto> getAllKeyword(
            // cursor : 다음 값을 가져올 수 있도록 알려주는 값, 넘버링
            @RequestParam(defaultValue = "") String cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        System.out.println("result : " + exhibitionService.getAllKeyword(cursor, size));
        return exhibitionService.getAllKeyword(cursor, size);
    }
}
