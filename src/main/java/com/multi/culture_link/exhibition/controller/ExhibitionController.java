package com.multi.culture_link.exhibition.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.culturalProperties.model.dto.YoutubeConfig;
import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.exhibition.model.dto.ExhibitionInterestDto;
import com.multi.culture_link.exhibition.service.ExhibitionAnalyzeService;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
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
    private final YoutubeConfig youtubeConfig;

    
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
//        System.out.println("컨트롤러");
//        System.out.println("1: " + currentUser + " 2: " +  data.getExhibitionId()  + " 3: " + data.getState());
        exhibitionService.setInterested(currentUser.getUserId(), data.getExhibitionId(), data.getState());
    }

    // 관심도 연결한 전시 list 불러오기
    @GetMapping("/exhibition")
    public List<ExhibitionDto> getDbExhibitions(
            @AuthenticationPrincipal VWUserRoleDTO currentUser
    ){
        return exhibitionService.getExhibition(currentUser.getUserId());
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



//    @PostMapping("/exhibitionYoutube")
//    public String findFestivalYoutube(
//            @RequestParam("title") String title
//    ) {
//        String youtubeId = null;
//
//        try {
//            youtubeId = ExhibitionService.findExhibitionYoutube(title);
//            System.out.println("유튜브 아이디 : " + youtubeId);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return youtubeId;
//    }

    @PostMapping("/videos")
    public List<Video> getVideos(@RequestParam String query) {
//        String query = title + " " + museum;
        List<Video> videos = crawlYouTubeVideos(query);
        return videos.subList(0, Math.min(videos.size(), 2)); // 최대 2개의 비디오만 반환
    }

    private List<Video> crawlYouTubeVideos(String query) {
        List<Video> videos = new ArrayList<>();
        try {
            String apiKey = youtubeConfig.getApiKey();
            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=2&q=" + query + "&type=video&key=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonObject = new JSONObject(response);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                String title = snippet.getString("title");
                String videoId = item.getJSONObject("id").getString("videoId");
                String link = "https://www.youtube.com/watch?v=" + videoId;
                String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                videos.add(new Video(title, link, thumbnailUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }

}
