package com.multi.culture_link.performance.service;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
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
public class PerformanceLocationService {

    private static final String url = "http://www.kopis.or.kr/openApi/restful/pblprfr";

    @Value("${kopis.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PerformanceLocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchPerformanceData(String stdate, String eddate, String signgucode) {
        String requestUrl = String.format("%s?service=%s&stdate=%s&eddate=%s&cpage=1&rows=100&signgucode=%s&newsql=Y",
                url, apiKey, stdate, eddate, signgucode != null ? signgucode : "");
        String response = restTemplate.getForObject(requestUrl, String.class);
        System.out.println("API Response: " + response);
        return response;
    }

    public List<PerformanceDTO> fetchPerformancesByLocation(String stdate, String eddate, String signgucode) {
        String data = fetchPerformanceData(stdate, eddate, signgucode);
        List<PerformanceDTO> performances = parsePerformanceData(data);
        System.out.println("Parsed Data: " + performances);
        return performances;
    }

    private List<PerformanceDTO> parsePerformanceData(String data) {
        List<PerformanceDTO> performances = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList performanceList = document.getElementsByTagName("db");

            System.out.println("Number of performances: " + performanceList.getLength());

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < performanceList.getLength(); i++) {
                Node performance = performanceList.item(i);
                PerformanceDTO dto = new PerformanceDTO();
                dto.setTitle(getTagValue("prfnm", performance));
                dto.setLocation(getTagValue("fcltynm", performance));
                dto.setCode(getTagValue("mt20id", performance));




                String startDateStr = getTagValue("prfpdfrom", performance).trim();
                String endDateStr = getTagValue("prfpdto", performance).trim();

                if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                    LocalDateTime startDate = LocalDate.parse(startDateStr, inputFormatter).atStartOfDay();
                    LocalDateTime endDate = LocalDate.parse(endDateStr, inputFormatter).atTime(23, 59, 59);
                    dto.setStartDate(startDate.format(outputFormatter));
                    dto.setEndDate(endDate.format(outputFormatter));
                }

                dto.setImageMain(getTagValue("poster", performance));
                dto.updateFormattedDate();
                performances.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return performances;
    }

    private String getTagValue(String tag, Node node) {
        NodeList nodeList = ((Element) node).getElementsByTagName(tag).item(0).getChildNodes();
        Node value = nodeList.item(0);
        return value != null ? value.getNodeValue() : "";
    }
}
