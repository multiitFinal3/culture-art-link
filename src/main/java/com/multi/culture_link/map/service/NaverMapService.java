package com.multi.culture_link.map.service;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.map.model.dto.NaverLocalSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverMapService {
    @Value("${API-KEY.naverArticleClientId}")
    private String clientId;

    @Value("${API-KEY.naverArticleClientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<NaverLocalSearchResponse> searchLocation(String query){
        String url = "https://openapi.naver.com/v1/search/local.json";

        HttpHeaders headers = new HttpHeaders();
        
        // 인증 정보 넣기
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<NaverLocalSearchResponse> response = restTemplate.exchange(
                    url + "?query=" + query + "&display=1",
                    HttpMethod.GET,
                    entity,
                    NaverLocalSearchResponse.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }




}
