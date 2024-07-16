package com.multi.culture_link.admin.exhibition.service;

import com.multi.culture_link.admin.exhibition.model.dto.ExhibitionApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionApiService {
    @Value("${API-KEY.clientKey}")
    String clientKey;

    private final RestTemplate restTemplate;

    public ExhibitionApiResponseDto getApiResponse() {
        String apiUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request?serviceKey=" + clientKey;
        // String apiUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request?serviceKey=c9130d29-65ea-495e-9701-9025010a1b92";
        System.out.println(restTemplate.getForObject(apiUrl, ExhibitionApiResponseDto.class));
        return restTemplate.getForObject(apiUrl, ExhibitionApiResponseDto.class);

    }
}
