package com.multi.culture_link.admin.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.dto.CulturalPropertiesDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.thymeleaf.util.StringUtils.escapeXml;


@Slf4j
@Service
public class AdminCulturalPropertiesServiceImpl implements AdminCulturalPropertiesService {

    private static final String listApiUrl = "http://www.khs.go.kr/cha/SearchKindOpenapiList.do";
    private static final String detailApiUrl = "http://www.khs.go.kr/cha/SearchKindOpenapiDt.do";
    private static final String imageApiUrl = "http://www.khs.go.kr/cha/SearchImageOpenapi.do";
    private static final String videoApiUrl = "http://www.khs.go.kr/cha/SearchVideoOpenapi.do";
    private static final String voiceApiUrl = "http://www.khs.go.kr/cha/SearchVoiceOpenapi.do";
    private static final String[] languages = {"kr", "en", "jpn", "chn"};

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<CulturalPropertiesDTO> fetchApiData() {
        List<CulturalPropertiesDTO> culturalPropertiesList = new ArrayList<>();


        try {
            // 1716 페이지까지 반복
            for (int pageIndex = 1; pageIndex <= 1716; pageIndex++) {
                String listFullUrl = listApiUrl + "?pageIndex=" + pageIndex + "&numOfRows=5&type=json";
                String listXmlResponse = restTemplate.getForObject(listFullUrl, String.class);
                JSONObject listJsonObject = XML.toJSONObject(listXmlResponse);

                JSONObject listResultObject = listJsonObject.getJSONObject("result");
                JSONArray listItemArray = listResultObject.getJSONArray("item");

                for (int i = 0; i < listItemArray.length(); i++) {
                    JSONObject listItemObject = listItemArray.getJSONObject(i);
                    String ccbaKdcd = listItemObject.optString("ccbaKdcd");
                    String ccbaAsno = listItemObject.optString("ccbaAsno");
                    String ccbaCtcd = listItemObject.optString("ccbaCtcd");

                    String detailFullUrl = detailApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd;
                    String detailXmlResponse = restTemplate.getForObject(detailFullUrl, String.class);
                    JSONObject detailJsonObject = XML.toJSONObject(detailXmlResponse);

                    JSONObject detailResultObject = detailJsonObject.getJSONObject("result").getJSONObject("item");
                    String gcodeName = detailResultObject.optString("gcodeName");
                    String bcodeName = detailResultObject.optString("bcodeName");
                    String mcodeName = detailResultObject.optString("mcodeName");
                    String scodeName = detailResultObject.optString("scodeName");
                    String ccbaAsdt = detailResultObject.optString("ccbaAsdt");
                    String ccbaCtcdNm = detailResultObject.optString("ccbaCtcdNm");
                    String ccsiName = detailResultObject.optString("ccsiName");
                    String ccbaLcad = detailResultObject.optString("ccbaLcad");
                    String ccceName = detailResultObject.optString("ccceName");
                    String imageUrl = detailResultObject.optString("imageUrl");
                    String content = detailResultObject.optString("content");

                    String ccmaName = listItemObject.optString("ccmaName");
                    String ccbaMnm1 = listItemObject.optString("ccbaMnm1");
                    String longitude = listItemObject.optString("longitude");
                    String latitude = listItemObject.optString("latitude");

                    String imageFullUrl = imageApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd;
                    String imageXmlResponse = restTemplate.getForObject(imageFullUrl, String.class);

                    JSONObject imageJsonObject;
                    try {
                        imageJsonObject = XML.toJSONObject(imageXmlResponse);
                    } catch (Exception e) {
                        System.out.println("XML 파싱 오류 발생:");
                        System.out.println(imageXmlResponse);
                        e.printStackTrace();
                        continue;
                    }

                    JSONObject imageResultObject = imageJsonObject.getJSONObject("result").getJSONObject("item");

                    JSONArray imageUrlArray = new JSONArray();

                    if (imageResultObject.has("imageUrl")) {
                        Object imageUrlObject = imageResultObject.get("imageUrl");
                        if (imageUrlObject instanceof JSONArray) {
                            imageUrlArray = (JSONArray) imageUrlObject;
                        } else {
                            imageUrlArray.put(imageUrlObject);
                        }
                    }


//                    JSONArray ccimDescArray = new JSONArray();
//                    if (imageResultObject.has("ccimDesc")) {
//                        Object ccimDescObject = imageResultObject.get("ccimDesc");
//
//                        // ccimDesc 값을 HTML 엔티티로 변환
//                        String ccimDescXml = ccimDescObject.toString();
//                        ccimDescXml = ccimDescXml.replace("<", "&lt;").replace(">", "&gt;");
//
//                        // HTML 엔티티로 변환된 ccimDesc를 JSON 객체로 변환
//                        JSONObject ccimDescJsonObject = XML.toJSONObject("<ccimDesc>" + ccimDescXml + "</ccimDesc>");
//
//
////                        if (ccimDescObject instanceof JSONArray) {
////                            ccimDescArray = (JSONArray) ccimDescObject;
////                        } else if (ccimDescObject instanceof String) {
////                            // 이미지 설명이 하나만 있는 경우 JSONArray로 변환
////                            ccimDescArray.put(ccimDescJsonObject);
////                        }
//
//                        if (ccimDescJsonObject != null) {
//                            ccimDescArray.put(ccimDescJsonObject);
//                        }
//                    }



                    JSONArray ccimDescArray = new JSONArray();
                    if (imageResultObject.has("ccimDesc")) {
                        Object ccimDescObject = imageResultObject.get("ccimDesc");

                        if (ccimDescObject instanceof String) {
                            // ccimDesc 값이 String인 경우에 대한 처리
                            String ccimDescXml = escapeXml(ccimDescObject.toString());
                            JSONObject ccimDescJsonObject = XML.toJSONObject("<ccimDesc>" + ccimDescXml + "</ccimDesc>");
                            ccimDescArray.put(ccimDescJsonObject);
                        } else if (ccimDescObject instanceof JSONArray) {
                            // ccimDesc 값이 JSONArray인 경우에 대한 처리
                            JSONArray ccimDescArrayObject = (JSONArray) ccimDescObject;
                            for (int j = 0; j < ccimDescArrayObject.length(); j++) {
                                String ccimDescXml = escapeXml(ccimDescArrayObject.optString(j));
                                JSONObject ccimDescJsonObject = XML.toJSONObject("<ccimDesc>" + ccimDescXml + "</ccimDesc>");
                                ccimDescArray.put(ccimDescJsonObject);
                            }
                        }
                    }
//                    JSONArray imageUrlArray;
//                    if (imageResultObject.has("imageUrl")) {
//                        Object imageUrlObject = imageResultObject.get("imageUrl");
//                        if (imageUrlObject instanceof JSONArray) {
//                            imageUrlArray = (JSONArray) imageUrlObject;
//                        } else {
//                            imageUrlArray = new JSONArray().put(imageUrlObject);
//                        }
//                    } else {
//                        imageUrlArray = new JSONArray();
//                    }
//
//                    JSONArray ccimDescArray = new JSONArray();
//                    if (imageResultObject.has("ccimDesc")) {
//                        Object ccimDescObject = imageResultObject.get("ccimDesc");
//                        if (ccimDescObject instanceof JSONArray) {
//                            ccimDescArray = (JSONArray) ccimDescObject;
//                        } else if (ccimDescObject instanceof String) {
//                            ccimDescArray.put(ccimDescObject);
//                        }
//                    }


                    String videoFullUrl = videoApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd;
                    String videoXmlResponse = restTemplate.getForObject(videoFullUrl, String.class);
                    JSONObject videoJsonObject = null;

                    try {
                        videoJsonObject = XML.toJSONObject(videoXmlResponse);
                    } catch (Exception e) {
                        System.out.println("XML 파싱 오류 발생:");
                        System.out.println(videoXmlResponse);
                        e.printStackTrace();
                        continue;
                    }

                    JSONArray videoUrlArray = new JSONArray();
                    if (videoJsonObject != null && videoJsonObject.has("result")) {
                        JSONObject videoResultObject = videoJsonObject.getJSONObject("result").getJSONObject("item");

                        if (videoResultObject.has("videoUrl")) {
                            Object videoUrlObject = videoResultObject.get("videoUrl");
                            if (videoUrlObject instanceof JSONArray) {
                                videoUrlArray = (JSONArray) videoUrlObject;
                            } else {
                                videoUrlArray.put(videoUrlObject);
                            }
                        }
                    }

                    String[] voiceUrls = new String[languages.length];
                    for (int langIndex = 0; langIndex < languages.length; langIndex++) {
                        String language = languages[langIndex];
                        String voiceFullUrl = voiceApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd + "&ccbaGbn=" + language;
                        String voiceXmlResponse = restTemplate.getForObject(voiceFullUrl, String.class);

                        JSONObject voiceJsonObject;
                        try {
                            voiceJsonObject = XML.toJSONObject(voiceXmlResponse);
                        } catch (Exception e) {
                            System.out.println("XML 파싱 오류 발생:");
                            System.out.println(voiceXmlResponse);
                            e.printStackTrace();
                            continue;
                        }

                        JSONObject voiceResultObject = voiceJsonObject.getJSONObject("result").getJSONObject("item");
                        String voiceUrl;
                        if (voiceResultObject.has("voiceUrl")) {
                            Object voiceUrlObject = voiceResultObject.get("voiceUrl");
                            if (voiceUrlObject instanceof JSONArray) {
                                JSONArray voiceUrlArray = (JSONArray) voiceUrlObject;
                                if (voiceUrlArray.length() > 0) {
                                    voiceUrl = voiceUrlArray.getString(0); // 첫 번째 음성 URL만 사용
                                } else {
                                    voiceUrl = "";
                                }
                            } else {
                                voiceUrl = voiceUrlObject.toString();
                            }
                        } else {
                            voiceUrl = "";
                        }

                        voiceUrls[langIndex] = voiceUrl;
                    }

                    CulturalPropertiesDTO dto = new CulturalPropertiesDTO();
                    dto.setCategoryCode(ccbaKdcd);
                    dto.setManagementNumber(ccbaAsno);
                    dto.setCityCode(ccbaCtcd);
                    dto.setNation("");
                    dto.setCategoryName(ccmaName);
                    dto.setCulturalPropertiesName(ccbaMnm1);
                    dto.setLongitude(longitude);
                    dto.setLatitude(latitude);
                    dto.setClassifyA(gcodeName);
                    dto.setClassifyB(bcodeName);
                    dto.setClassifyC(mcodeName);
                    dto.setClassifyD(scodeName);
                    dto.setRegistrationDate(ccbaAsdt);
                    dto.setRegion(ccbaCtcdNm);
                    dto.setDistrict(ccsiName);
                    dto.setAddress(ccbaLcad);
                    dto.setDynasty(ccceName);
                    dto.setContent(content);
                    dto.setMainImgUrl(imageUrl);

                    List<String> imageUrls = new ArrayList<>();
                    for (int j = 0; j < imageUrlArray.length(); j++) {
                        imageUrls.add(imageUrlArray.getString(j));
                    }
                    dto.setImgUrl(imageUrls);

                    List<String> imageDescs = new ArrayList<>();
                    for (int j = 0; j < ccimDescArray.length(); j++) {
                        imageDescs.add(ccimDescArray.optString(j));
                    }
                    dto.setImgDesc(imageDescs);

                    List<String> videoUrls = new ArrayList<>();
                    for (int k = 0; k < videoUrlArray.length(); k++) {
                        videoUrls.add(videoUrlArray.getString(k));
                    }
                    dto.setVideoUrl(videoUrls);

                    List<String> narrationUrls = new ArrayList<>();
                    for (int langIndex = 0; langIndex < languages.length; langIndex++) {
                        String voiceUrl = voiceUrls[langIndex].trim();
                        narrationUrls.add(voiceUrl);
                    }
                    dto.setNarrationUrl(narrationUrls);

                    culturalPropertiesList.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 응답 처리 중 오류 발생: " + e.getMessage());
        }

        // DTOs가 culturalPropertiesList에 저장되어 있으므로 필요한 작업 수행
        for (CulturalPropertiesDTO dto : culturalPropertiesList) {
            // 여기에 필요한 로직 추가, 예를 들어 데이터베이스에 저장
            System.out.println(dto); // DTO 내용 출력 (toString 메서드 필요)
        }
        return culturalPropertiesList;
    }


    private String escapeXml(String xmlString) {
        // XML 특수 문자를 JSON으로 변환할 때 이스케이핑 처리
        return xmlString.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");

    }


}