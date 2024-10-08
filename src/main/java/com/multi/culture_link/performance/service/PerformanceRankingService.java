package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceRankingService {

    @Autowired
    private PerformanceMapper performanceMapper;

    private static final String url = "http://www.kopis.or.kr/openApi/restful/boxoffice";

    @Value("${kopis.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PerformanceRankingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchPerformanceData(String ststype, String catecode, String date) {
        String requestUrl = String.format("%s?service=%s&ststype=%s&catecode=%s&date=%s", url, apiKey, ststype, catecode, date);
        String response = restTemplate.getForObject(requestUrl, String.class);
        System.out.println("API Response: " + response);
        return response;
    }

    public List<PerformanceDTO> fetchGenreRanking(String genre, String date,int limit) {
        String ststype = "day";
        String catecode = getCategoryCode(genre);
        if (catecode == null) {
            return new ArrayList<>();
        }
        String data = fetchPerformanceData(ststype, catecode, date);
        List<PerformanceDTO> rankingData = parseRankingData(data, limit);
        System.out.println("Parsed Data: " + rankingData);
        return rankingData;
    }

    private List<PerformanceDTO> parseRankingData(String data,int limit) {
        List<PerformanceDTO> rankingData = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList performanceList = document.getElementsByTagName("boxof");

            System.out.println("Number of performances: " + performanceList.getLength());

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < Math.min(limit, performanceList.getLength()); i++) { // 지정한 개수만큼 항목 추가
                Node performance = performanceList.item(i);
                PerformanceDTO dto = new PerformanceDTO();
                dto.setTitle(getTagValue("prfnm", performance));
                dto.setLocation(getTagValue("prfplcnm", performance));

                dto.setCode(getTagValue("mt20id", performance));

                String[] period = getTagValue("prfpd", performance).split("~");
                LocalDateTime startDate = LocalDate.parse(period[0].trim(), inputFormatter).atStartOfDay();
                LocalDateTime endDate = LocalDate.parse(period[1].trim(), inputFormatter).atTime(23, 59, 59);
                dto.setStartDate(startDate.format(outputFormatter));
                dto.setEndDate(endDate.format(outputFormatter));

                dto.setImageMain("https://www.kopis.or.kr" + getTagValue("poster", performance));
                dto.updateFormattedDate();
                dto.setRank(getTagValue("rnum", performance)); // 순위 설정
                rankingData.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rankingData;
    }

    public List<PerformanceDTO> fetchTopRankings(String date) {
        String ststype = "day";
        String catecode = ""; // 모든 카테고리
        String data = fetchPerformanceData(ststype, catecode, date);
        List<PerformanceDTO> rankingData = parseRankingData(data, 50); // 지정한 개수만큼 항목 추가
        System.out.println("Parsed Data: " + rankingData);
        return rankingData;
    }

    private String getTagValue(String tag, Node node) {
        NodeList nodeList = ((Element) node).getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value != null ? value.getNodeValue() : "";
    }

    private String getCategoryCode(String genre) {
        switch (genre) {
            case "연극":
                return "AAAA";
            case "뮤지컬":
                return "GGGA";
            case "서양음악":
                return "CCCA";
            case "한국음악":
                return "CCCC";
            case "대중음악":
                return "CCCD";
            case "무용":
                return "BBBC";
            case "대중무용":
                return "BBBR";
            case "서커스/마술":
                return "EEEB";
            case "복합":
                return "EEEA";
            default:
                throw new IllegalArgumentException("Unexpected value: " + genre);
        }
    }

    public PerformanceDTO getPerformanceDetail(int performanceId) {
        return null;
    }

    public List<PerformanceDTO> getAllPerformances() {
        return performanceMapper.getAllPerformances();
    }





    public PerformanceDTO getPerformanceDetailFromAPI(String performanceCode) {
        String url = String.format("http://www.kopis.or.kr/openApi/restful/pblprfr/%s?service=%s&newsql=Y", performanceCode, apiKey);
        System.out.println("Fetching performance by CODE from API: " + url);
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("API Response: " + response);

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("API 응답이 없습니다.");
        }

        PerformanceDTO performance = parsePerformanceDetail(response);
        System.out.println("Parsed Performance: " + performance);
        return performance;
    }


    private PerformanceDTO parsePerformanceDetail(String data) {
        PerformanceDTO performance = new PerformanceDTO();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("api");

            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);

                performance.setCode(getTagValue("mt20id", node));
                performance.setTitle(getTagValue("prfnm", node));
                performance.setStartDate(formatDate(getTagValue("prfpdfrom", node)));
                performance.setEndDate(formatDate(getTagValue("prfpdto", node)));
                performance.setLocation(getTagValue("fcltynm", node));
                performance.setCasting(getTagValue("prfcast", node));
                performance.setRuntime(getTagValue("prfruntime", node));
                performance.setAge(getTagValue("prfage", node));
                performance.setProducer(getTagValue("entrpsnmH", node));
                performance.setPrice(getTagValue("pcseguidance", node));
                performance.setImageMain(getTagValue("poster", node));
                performance.setGenre(getTagValue("genrenm", node));

                NodeList relateList = ((Element) node).getElementsByTagName("relate");
                if (relateList.getLength() > 0) {
                    Node relateNode = relateList.item(0);
                    performance.setTicketing(getTagValue("relatenm", relateNode));
                    performance.setTicketingUrl(getTagValue("relateurl", relateNode));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return performance;
    }


    public PerformanceDTO getPerformanceDetail(String performanceCode) {
        PerformanceDTO performanceDTO = performanceMapper.getPerformanceByCode(performanceCode);
        if (performanceDTO != null) {
            performanceDTO.updateFormattedDate(); // formattedDate 필드를 업데이트
        }
        return performanceDTO;
    }


    private String formatDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(date.trim(), inputFormatter).atStartOfDay().format(outputFormatter);
    }

}