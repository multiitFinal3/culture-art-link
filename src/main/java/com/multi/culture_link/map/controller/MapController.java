package com.multi.culture_link.map.controller;

import com.multi.culture_link.map.model.dto.NaverLocalSearchResponse;
import com.multi.culture_link.map.service.NaverMapService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {
    private final NaverMapService naverMapService;

    
    // 상세 검색
    @GetMapping("/naver")
    public ResponseEntity<NaverLocalSearchResponse> searchLocationByNaver(
            @RequestParam String query
    ) {
        return naverMapService.searchLocation(query);
    }


}
