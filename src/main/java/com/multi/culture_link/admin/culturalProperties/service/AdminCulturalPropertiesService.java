package com.multi.culture_link.admin.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import lombok.extern.slf4j.Slf4j;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AdminCulturalPropertiesService {
	
	private final AdminCulturalPropertiesDAO adminCulturalPropertiesDAO;
	private ArrayList<CulturalPropertiesDTO> list;
	
	@Autowired
	public AdminCulturalPropertiesService(AdminCulturalPropertiesDAO adminCulturalPropertiesDAO, ArrayList<CulturalPropertiesDTO> list) {
		this.adminCulturalPropertiesDAO = adminCulturalPropertiesDAO;
		this.list = list;
	}
	
	
	private static final String listApiUrl = "http://www.khs.go.kr/cha/SearchKindOpenapiList.do";
	private static final String detailApiUrl = "http://www.khs.go.kr/cha/SearchKindOpenapiDt.do";
	private static final String imageApiUrl = "http://www.khs.go.kr/cha/SearchImageOpenapi.do";
	private static final String videoApiUrl = "http://www.khs.go.kr/cha/SearchVideoOpenapi.do";
	private static final String voiceApiUrl = "http://www.khs.go.kr/cha/SearchVoiceOpenapi.do";
	private static final String[] languages = {"kr", "en", "jpn", "chn"};
	
	private RestTemplate restTemplate = new RestTemplate();
	
	//    @Override
	public List<CulturalPropertiesDTO> fetchApiData(int pageIndex) {
		
		System.out.println("서비스임플1");
		
		List<CulturalPropertiesDTO> culturalPropertiesList = new ArrayList<>();
		
		System.out.println("서비스임플2");
		
		try {
			// 1716 페이지까지 반복
			
			System.out.println("서비스임플1 : pageIndex" + pageIndex);
			String listFullUrl = listApiUrl + "?pageIndex=" + pageIndex;
			String listXmlResponse = restTemplate.getForObject(listFullUrl, String.class);
			JSONObject listJsonObject = XML.toJSONObject(listXmlResponse);
			
			// List API에서 필요한 데이터 추출
			JSONObject listResultObject = listJsonObject.getJSONObject("result");
			JSONArray listItemArray = listResultObject.getJSONArray("item");
			
			// Detail API에서 필요한 데이터 추출하여 매칭
			for (int i = 0; i < listItemArray.length(); i++) {
				JSONObject listItemObject = listItemArray.getJSONObject(i);
				String ccbaKdcd = listItemObject.optString("ccbaKdcd");
				String ccbaAsno = listItemObject.optString("ccbaAsno");
				String ccbaCtcd = listItemObject.optString("ccbaCtcd");
				
				// Detail API 호출하여 XML 데이터 받아오기
				String detailFullUrl = detailApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd;
				String detailXmlResponse = restTemplate.getForObject(detailFullUrl, String.class);
				JSONObject detailJsonObject = XML.toJSONObject(detailXmlResponse);
				
				// Detail API에서 필요한 데이터 추출
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
				
				// List API에서의 필드 추출
				String ccmaName = listItemObject.optString("ccmaName");
				String ccbaMnm1 = listItemObject.optString("ccbaMnm1");
				String longitude = listItemObject.optString("longitude");
				String latitude = listItemObject.optString("latitude");
				
				// Image API 호출하여 XML 데이터 받아오기
				String imageFullUrl = imageApiUrl + "?ccbaKdcd=" + ccbaKdcd + "&ccbaAsno=" + ccbaAsno + "&ccbaCtcd=" + ccbaCtcd;
				String imageXmlResponse = restTemplate.getForObject(imageFullUrl, String.class);
				
				// XML 데이터 문자열 치환
				imageXmlResponse = imageXmlResponse.replaceAll("<ccimDesc>", "<ccimDesc><![CDATA[");
				imageXmlResponse = imageXmlResponse.replaceAll("</ccimDesc>", "]]></ccimDesc>");
				
				
				// Image API에서 필요한 데이터 추출
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
				
				// Image URL 추출
				if (imageResultObject.has("imageUrl")) {
					Object imageUrlObject = imageResultObject.get("imageUrl");
					if (imageUrlObject instanceof JSONArray) {
						imageUrlArray = (JSONArray) imageUrlObject;
					} else {
						imageUrlArray.put(imageUrlObject);
					}
				}
				
				
				JSONArray ccimDescArray = new JSONArray();
				if (imageResultObject.has("ccimDesc")) {
					Object ccimDescObject = imageResultObject.get("ccimDesc");
					if (ccimDescObject instanceof JSONArray) {
						ccimDescArray = (JSONArray) ccimDescObject;
					} else if (ccimDescObject instanceof String) {
						ccimDescArray.put(ccimDescObject);
					}
				}
				
				
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
				
				// 이미지 URL 및 설명 리스트 설정
				List<String> imageUrls = new ArrayList<>();
				for (int j = 0; j < imageUrlArray.length(); j++) {
					imageUrls.add(imageUrlArray.getString(j));
				}
//                dto.setImgUrl(imageUrls.get(1));
				
				if (imageUrls != null) {
					
					dto.setImgUrl(imageUrls);
					
				}
				
				
				List<String> imageDescs = new ArrayList<>();
				for (int j = 0; j < ccimDescArray.length(); j++) {
					imageDescs.add(ccimDescArray.optString(j));
				}
//                dto.setImgDesc(imageDescs.get(1));
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
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("API 응답 처리 중 오류 발생: " + e.getMessage());
		}
		
		System.out.println("serviceImpl 5");
		
		// DTOs가 culturalPropertiesList에 저장되어 있으므로 필요한 작업 수행
		for (CulturalPropertiesDTO dto : culturalPropertiesList) {
			// 여기에 필요한 로직 추가, 예를 들어 데이터베이스에 저장
			System.out.println(dto); // DTO 내용 출력 (toString 메서드 필요)
		}
		
		this.list = (ArrayList<CulturalPropertiesDTO>) culturalPropertiesList;
		
		return culturalPropertiesList;
	}
	

	
	public List<CulturalPropertiesDTO> selectDB(PageDTO pageDto) {


		ArrayList<CulturalPropertiesDTO> list = (ArrayList<CulturalPropertiesDTO>) adminCulturalPropertiesDAO.selectDB(pageDto);

		for(CulturalPropertiesDTO culturalPropertiesDTO : list){

			// 이미지 URL(ImgUrl)의 처음부터 세 번째까지의 요소들만 리스트로 설정
			List<String> imgUrlSublist = culturalPropertiesDTO.getImgUrl().subList(0, Math.min(4, culturalPropertiesDTO.getImgUrl().size()));
			culturalPropertiesDTO.setImgUrl(imgUrlSublist);

			// 이미지 설명(ImgDesc)의 처음부터 세 번째까지의 요소만 리스트로 설정
			List<String> imgDescSublist = culturalPropertiesDTO.getImgDesc().subList(0, Math.min(4, culturalPropertiesDTO.getImgDesc().size()));
			culturalPropertiesDTO.setImgDesc(imgDescSublist);


		}
		return list;
	}
	
	public void addDBData(ArrayList<Integer> numList) {
		
		CulturalPropertiesDTO culturalPropertiesDTO = new CulturalPropertiesDTO();
		
		for (int i :  numList){
			System.out.println("i : " + i);
			culturalPropertiesDTO = list.get(i);
			System.out.println("설명 : " + culturalPropertiesDTO.toString());
			adminCulturalPropertiesDAO.insertDB(culturalPropertiesDTO);
			
		}
	
	}

	public int selectCount() {

		return adminCulturalPropertiesDAO.selectCount();
	}



	public List<CulturalPropertiesDTO> searchDBCulturalProperties(String category, String name, String region, String dynasty, int page) {
		int start = (page - 1) * 10; // 페이지당 10개씩 처리
		int end = 10;

		List<CulturalPropertiesDTO> list = adminCulturalPropertiesDAO.searchDBCulturalProperties(category, name, region, dynasty, start, end);

		for(CulturalPropertiesDTO culturalPropertiesDTO : list){

			// 이미지 URL(ImgUrl)의 처음부터 세 번째까지의 요소들만 리스트로 설정
			List<String> imgUrlSublist = culturalPropertiesDTO.getImgUrl().subList(0, Math.min(4, culturalPropertiesDTO.getImgUrl().size()));
			culturalPropertiesDTO.setImgUrl(imgUrlSublist);

			// 이미지 설명(ImgDesc)의 처음부터 세 번째까지의 요소만 리스트로 설정
			List<String> imgDescSublist = culturalPropertiesDTO.getImgDesc().subList(0, Math.min(4, culturalPropertiesDTO.getImgDesc().size()));
			culturalPropertiesDTO.setImgDesc(imgDescSublist);


		}
		return list;

	}

	public int searchCountCulturalProperties(String category, String name, String region, String dynasty) {
		return adminCulturalPropertiesDAO.searchCountCulturalProperties(category, name, region, dynasty);
	}


	public void deleteDBData(ArrayList<Integer> id) {
		// id를 사용하여 데이터베이스에서 삭제
		if (id != null && !id.isEmpty()) {
			adminCulturalPropertiesDAO.deleteDBData(id);
		}
	}

	public void updateDBData(ArrayList<CulturalPropertiesDTO> data) {

		adminCulturalPropertiesDAO.updateDBData(data);


	}



	//----------------api 검색 보류

//	public List<CulturalPropertiesDTO> searchAPIDataFilter(int pageIndex, String categoryName, String culturalPropertiesName, String region, String dynasty) {
//		List<CulturalPropertiesDTO> culturalPropertiesList = fetchApiData(pageIndex); // 기존 API 호출
//
//		System.out.println("검색culturalPropertiesList: " + culturalPropertiesList);
//
//		// 필터링 로직 추가
//		return culturalPropertiesList.stream()
//				.filter(dto -> (categoryName == null || categoryName.equals("전체") || dto.getCategoryName().equals(categoryName)))
//				.filter(dto -> (culturalPropertiesName == null || culturalPropertiesName.isEmpty() || dto.getCulturalPropertiesName().contains(culturalPropertiesName)))
//				.filter(dto -> (region == null || region.isEmpty() || dto.getRegion().contains(region)))
//				.filter(dto -> (dynasty == null || dynasty.isEmpty() || dto.getDynasty().contains(dynasty)))
//				.collect(Collectors.toList());
//	}


//----------------api 검색 보류 //2222//

//	public List<CulturalPropertiesDTO> searchAPIDataFilter(int pageIndex, String categoryName, String culturalPropertiesName, String region, String dynasty) {
//		List<CulturalPropertiesDTO> culturalPropertiesList = fetchApiData(pageIndex); // 기존 API 호출
//
//		System.out.println("cultu list " + culturalPropertiesList);
//
//		List<Object> list1 = culturalPropertiesList.stream()
//				.filter(dto -> (categoryName == null || categoryName.equals("전체") || dto.getCategoryName().equals(categoryName))
////				&&  (culturalPropertiesName == null || culturalPropertiesName.equals("") || dto.getCulturalPropertiesName().contains(culturalPropertiesName))
////				&& (region == null || region.equals("") || dto.getRegion().contains(region))
////				&& (dynasty == null || dynasty.equals("")|| dto.getDynasty().contains(dynasty)))
//				).collect(Collectors.toList());
//
//		System.out.println("list1 : " + list1);
//
//
//		// 필터링 로직 추가
//		return culturalPropertiesList.stream()
//				.filter(dto -> (categoryName == null || categoryName.equals("전체") || dto.getCategoryName().equals(categoryName)))
//				.filter(dto -> (culturalPropertiesName == null || culturalPropertiesName.isEmpty() || dto.getCulturalPropertiesName().contains(culturalPropertiesName)))
//				.filter(dto -> (region == null || region.isEmpty() || dto.getRegion().contains(region)))
//				.filter(dto -> (dynasty == null || dynasty.isEmpty() || dto.getDynasty().contains(dynasty)))
//				.collect(Collectors.toList());
//	}







}