//package com.multi.culture_link.board.controller;
//
//import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
//import com.multi.culture_link.culturalProperties.model.dto.Video;
//import com.multi.culture_link.exhibition.model.dto.ExhibitionAnalyzeDto;
//import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
//import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
//import com.multi.culture_link.exhibition.model.dto.ExhibitionInterestDto;
//import com.multi.culture_link.exhibition.service.ExhibitionAnalyzeService;
//import com.multi.culture_link.exhibition.service.ExhibitionCommentService;
//import com.multi.culture_link.exhibition.service.ExhibitionService;
//import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
//import lombok.RequiredArgsConstructor;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/exhibition")
//public class BoardController {
//    private final BoardService boardService;
//
//
//
//    // 상세 검색
//    @GetMapping("/search-exhibition")
//    public List<ExhibitionApiDto> searchExhibition(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String artist,
//            @RequestParam(required = false) String museum) {
//
//        Map<String, String> searchParams = new HashMap<>();
//        searchParams.put("title", title);
//        searchParams.put("artist", artist);
//        searchParams.put("museum", museum);
//
//        return exhibitionService.searchExhibition(searchParams);
//    }
//
//
//
//
//}
