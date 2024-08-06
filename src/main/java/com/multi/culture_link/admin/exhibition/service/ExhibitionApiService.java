package com.multi.culture_link.admin.exhibition.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.multi.culture_link.admin.exhibition.model.dao.AdminExhibitionDao;
import com.multi.culture_link.admin.exhibition.model.dao.ExhibitionKeywordDao;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiResponseDto;
import com.multi.culture_link.common.keyword.service.KeywordExtractService;
import com.multi.culture_link.exhibition.model.dao.ExhibitionCommentDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionApiService {
    @Value("${API-KEY.clientKey}")
    String clientKey;

    private final RestTemplate restTemplate;
    private final AdminExhibitionDao adminExhibitionDao;
    private final ExhibitionKeywordDao exhibitionKeywordDao;
    private final ExhibitionCommentDao exhibitionCommentDao;
    String path = "classpath:static/txt/exhibition/stop.txt";



    private final KeywordExtractService keywordExtractService;



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
    public ExhibitionApiResponseDto fetchJsonData() {
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
            //System.out.println(response);
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // db 형태로 데이터 변환 후 저장
    public void saveExhibition(List<ExhibitionApiResponseDto.Item> responseData) {
        List<ExhibitionApiDto> processedData = new ArrayList<ExhibitionApiDto>();
        for (ExhibitionApiResponseDto.Item responseDatum : responseData) {
            ExhibitionApiDto eachData = new ExhibitionApiDto();
            eachData.setTitle(responseDatum.getTitle());
            eachData.setArtist(responseDatum.getAuthor());
            eachData.setMuseum(responseDatum.getCntcInsttNm());
            eachData.setLocalId(responseDatum.getLocalId());
            eachData.setStartDate(responseDatum.getStart_date());
            eachData.setEndDate(responseDatum.getEnd_date());
            eachData.setPrice(responseDatum.getCharge());
            eachData.setImage(responseDatum.getImageObject());
            eachData.setDescription(responseDatum.getDescription());
            eachData.setSubDescription(responseDatum.getSubDescription());
            eachData.setUrl(responseDatum.getUrl());
            processedData.add(eachData);
        }
//        System.out.println("processedData: " + processedData);
        adminExhibitionDao.saveData(processedData);
    }

    public void deleteExhibition(List<Integer> id) {
        adminExhibitionDao.deleteData(id);
    }

    public void updateExhibition(List<ExhibitionApiDto> data) {
//        System.out.println("야" + data);
        adminExhibitionDao.updateData(data);
    }

    public List<ExhibitionApiDto> getExhibition() {
        return adminExhibitionDao.getData();
    }



    public void saveKeyword() throws Exception {
        this.saveExhibitionCommentKeyword();
        this.saveExhibitionKeyword();
    }


    public void saveExhibitionKeyword() throws Exception {
        List<ExhibitionApiDto> exhibitions = this.getExhibition();

        for (ExhibitionApiDto exhibition : exhibitions) {
            StringBuilder content = new StringBuilder();

            if (exhibition.getTitle() != null) {
                content.append(exhibition.getTitle()).append(" ");
            }
            if (exhibition.getDescription() != null) {
                content.append(exhibition.getDescription());
            }

            String combinedContent = content.toString().trim();

            if (!combinedContent.isEmpty()) {
            
                HashMap<String, Integer> keyWordsMap = keywordExtractService.getKeywordByKomoran(combinedContent, path);

                System.out.println("Exhibition ID: " + exhibition.getId());
                System.out.println("Combined content: " + combinedContent);
                System.out.println("Key word map: " + keyWordsMap);

                saveExhibitionKeywordDB(exhibition.getId(), keyWordsMap);
            }
        }

    }

    private void saveExhibitionKeywordDB(int exhibitionId, HashMap<String, Integer> keyWordsMap) {
        for (Map.Entry<String, Integer> entry : keyWordsMap.entrySet()) {
            String keyword = entry.getKey();
            int frequency = entry.getValue();
            exhibitionKeywordDao.saveExhibitionKeyword(exhibitionId, keyword, frequency);
        }
    }




    public void saveExhibitionCommentKeyword() throws Exception {
        List<ExhibitionCommentDto> comments = exhibitionCommentDao.getCommentAll();
        Map<Integer, StringBuilder> exhibitionContents = new HashMap<>();

        // 같은 exhibitionId를 가진 코멘트들의 내용을 모음
        for (ExhibitionCommentDto comment : comments) {
            int exhibitionId = comment.getExhibitionId();
            exhibitionContents.computeIfAbsent(exhibitionId, k -> new StringBuilder());

            if (comment.getContent() != null) {
                exhibitionContents.get(exhibitionId).append(comment.getContent()).append(" ");
            }
        }

        // 각 전시회별로 모아진 코멘트 내용에 대해 키워드를 추출하고 저장
        for (Map.Entry<Integer, StringBuilder> entry : exhibitionContents.entrySet()) {
            int exhibitionId = entry.getKey();
            String combinedContent = entry.getValue().toString().trim();
            String path = "classpath:static/txt/festival/stop.txt";

            if (!combinedContent.isEmpty()) {
                HashMap<String, Integer> keyWordsMap = keywordExtractService.getKeywordByKomoran(combinedContent, path);
                saveExhibitionCommentKeywordDB(exhibitionId, keyWordsMap);
            }
        }
    }

    private void saveExhibitionCommentKeywordDB(int exhibitionId, HashMap<String, Integer> keyWordsMap) {
        for (Map.Entry<String, Integer> entry : keyWordsMap.entrySet()) {
            String keyword = entry.getKey();
            int frequency = entry.getValue();
            exhibitionKeywordDao.saveExhibitionCommentKeyword(exhibitionId, keyword, frequency);
        }
    }

}
