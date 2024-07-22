package com.multi.culture_link.admin.performance.service;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceAPIService {

    private static final String url = "http://www.kopis.or.kr/openApi/restful/pblprfr";
    private static final String key = "a0cfef9bedc443bc9153b8b024d1b1dc";

    // 기본 페이지 번호와 크기를 사용하는 오버로드된 메소드
    public Page<PerformanceDTO> fetchData() throws IOException {
        return fetchData(0, 20);
    }

    public Page<PerformanceDTO> fetchData(int page, int size) throws IOException {
        List<PerformanceDTO> performances = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(String.format("%s?service=%s&stdate=20240701&eddate=20240722&rows=10000000&cpage=1", url, key));
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        System.out.println(httpResponse.getStatusLine().getStatusCode());

        BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();

        performances = parseXML(res.toString());

        httpClient.close();

        int start = Math.min((int) PageRequest.of(page, size).getOffset(), performances.size());
        int end = Math.min((start + size), performances.size());
        return new PageImpl<>(performances.subList(start, end), PageRequest.of(page, size), performances.size());
    }


    private List<PerformanceDTO> parseXML(String xmlContent) {
        List<PerformanceDTO> performances = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
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
                    PerformanceDTO performance = new PerformanceDTO();
                    performance.setCode(getValue("mt20id", element));
                    performance.setTitle(getValue("prfnm", element));
                    performance.setStatus(getValue("prfstate", element));
                    performance.setStartDate(getValue("prfpdfrom", element));
                    performance.setEndDate(getValue("prfpdto", element));
                    performance.setLocation(getValue("fcltynm", element));
                    performance.setImageMain(getValue("poster", element));
                    performance.setGenre(getValue("genrenm", element));
                    //performance.setOpenRun(getValue("openrun", element));
                    performances.add(performance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return performances;
    }



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
