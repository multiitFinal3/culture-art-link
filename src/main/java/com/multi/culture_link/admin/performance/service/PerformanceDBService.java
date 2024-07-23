package com.multi.culture_link.admin.performance.service;

import com.multi.culture_link.admin.performance.mapper.PerformanceMapper;
import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

@Service
public class PerformanceDBService { // 상세데이터, 외부 API로부터 공연 상세 데이터를 가져와 데이터베이스에 저장

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private PerformanceRegionService performanceRegionService;

    private static final String DETAIL_URL = "http://www.kopis.or.kr/openApi/restful/pblprfr/%s?service=a0cfef9bedc443bc9153b8b024d1b1dc&newsql=Y";

    // 모든 공연 데이터
    public List<PerformanceDTO> getAllPerformances() {
        return performanceMapper.getAllPerformances();
    }

    // 키워드를 사용하여 공연 데이터를 검색
    public List<PerformanceDTO> searchPerformances(String keyword) {
        return performanceMapper.searchPerformances(keyword);
    }

    // 선택된 공연 ID 목록을 받아, 각 ID에 대해 공연 상세 데이터를 API에서 가져오고, 데이터베이스에 저장
    public int savePerformances(List<String> selectedIds) {
        int savedCount = 0;
        for (String id : selectedIds) {
            try {
                System.out.println("Fetching data for ID: " + id);
                PerformanceDTO performance = fetchDetailData(id);
                if (performance != null) {
                    System.out.println("Inserting performance: " + performance);
                    performanceMapper.insertPerformance(performance);
                    savedCount++;
                } else {
                    System.out.println("Performance data is null for ID: " + id);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savedCount;
    }

    // 선택된 공연 ID 목록을 삭제
    public int deletePerformances(List<String> selectedIds) {
        return performanceMapper.deletePerformances(selectedIds);
    }

    // 특정 공연 ID에 대해 API를 호출해 상세 데이터를 가져오기
    private PerformanceDTO fetchDetailData(String id) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(String.format(DETAIL_URL, id));
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();
        httpClient.close();

        PerformanceDTO performance = parseXML(res.toString());
        if (performance == null) {
            System.out.println("Performance data is null for ID: " + id);
        }
        return performance;
    }

    // API로부터 받은 XML 데이터를 parseXML(String xmlContent) 메서드를 통해 파싱하여 PerformanceDTO 객체로 변환
    private PerformanceDTO parseXML(String xmlContent) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        PerformanceDTO performance = null;
        try {
            InputSource is = new InputSource(new StringReader(xmlContent));
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);

            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    performance = new PerformanceDTO();
                    performance.setCode(getValue("mt20id", element)); // 공연코드
                    performance.setTitle(getValue("prfnm", element)); // 공연명
                    performance.setStatus(getValue("prfstate", element)); // 공연상태
                    performance.setStartDate(getValue("prfpdfrom", element)); // 공연시작날짜
                    performance.setEndDate(getValue("prfpdto", element)); // 공연종료날짜
                    performance.setLocation(getValue("fcltynm", element)); // 공연장소

                    // 지역 정보를 문자열로 가져온 다음, 지역 ID로 변환
                    String regionName = getValue("area", element);
                    int regionId = performanceRegionService.getRegionId(regionName);
                    performance.setRegionId(regionId);

                    performance.setCasting(getValue("prfcast", element)); // 출연진
                    performance.setRuntime(getValue("prfruntime", element)); // 공연런타임
                    performance.setAge(getValue("prfage", element)); // 연령
                    performance.setOrganizer(getValue("entrpsnm", element)); // 기관
                    performance.setProducer(getValue("entrpsnmH", element)); // 주최
                    performance.setPrice(getValue("pcseguidance", element)); // 가격
                    performance.setImageMain(getValue("poster", element)); // 이미지메인
                    performance.setImageDetail1(getValue("styurl", element)); // 이미지상세1
                    performance.setGenre(getValue("genrenm", element)); // 공연장르
                    performance.setTicketing(getValue("relatenm", element)); // 티켓팅
                    performance.setTicketingUrl(getValue("relateurl", element));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return performance;
    }

    // XML 요소에서 특정 태그의 값을 가져오기
    private String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            NodeList childNodes = nodes.item(0).getChildNodes();
            if (childNodes.getLength() > 0) {
                Node node = childNodes.item(0);
                if (node != null) {
                    return node.getNodeValue();
                }
            }
        }
        return null;
    }
}
