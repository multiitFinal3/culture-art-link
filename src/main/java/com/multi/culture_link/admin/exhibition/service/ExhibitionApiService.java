package com.multi.culture_link.admin.exhibition.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionApiService {
    @Value("${API-KEY.clientKey}")
    String clientKey;

    private final RestTemplate restTemplate;


    // xml 불러오기
    public String fetchXmlData(String apiUrl) throws Exception {
        StringBuilder xmlResponse = new StringBuilder();
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 스트림을 통해 읽어오기
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    xmlResponse.append(inputLine);
                }
            }
        } else {
            throw new Exception("HTTP GET 요청 실패: " + responseCode);
        }

        return xmlResponse.toString();
    }

    // json 변환
    public String fetchJsonData() {
        String apiUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request?serviceKey=" + clientKey;

        try {
            // XML 데이터 가져오기
            String xmlString = fetchXmlData(apiUrl);

            // XML을 JSON으로 변환
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xmlString.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper(); // JSON으로 변환할 Mapper

            // 들여쓰기된 JSON 형식으로 출력
            String jsonResult = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
//            System.out.println(jsonResult);
            ExhibitionApiResponseDto response = jsonMapper.readValue(jsonResult, ExhibitionApiResponseDto.class);
            System.out.println(response);
            return jsonResult;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    public ExhibitionApiResponseDto getApiResponse() {
//        // String apiUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request?serviceKey=" + clientKey;
//        String apiUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request?serviceKey=c9130d29-65ea-495e-9701-9025010a1b92";
//        System.out.println(restTemplate.getForObject(apiUrl, ExhibitionApiResponseDto.class));
//        return restTemplate.getForObject(apiUrl, ExhibitionApiResponseDto.class);
//
//    }





}
