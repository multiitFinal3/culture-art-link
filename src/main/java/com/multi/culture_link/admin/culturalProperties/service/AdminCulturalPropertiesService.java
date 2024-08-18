package com.multi.culture_link.admin.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dao.CulturalPropertiesKeywordDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesReviewKeywordDTO;
import com.multi.culture_link.common.keyword.service.KeywordExtractService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminCulturalPropertiesService {
	
	private final AdminCulturalPropertiesDAO adminCulturalPropertiesDAO;
	private ArrayList<CulturalPropertiesDTO> list;

	private final KeywordExtractService keywordExtractService;
	private final CulturalPropertiesKeywordDAO culturalPropertiesKeywordDAO;
	
	@Autowired
	public AdminCulturalPropertiesService(AdminCulturalPropertiesDAO adminCulturalPropertiesDAO, ArrayList<CulturalPropertiesDTO> list, KeywordExtractService keywordExtractService, CulturalPropertiesKeywordDAO culturalPropertiesKeywordDAO) {
		this.adminCulturalPropertiesDAO = adminCulturalPropertiesDAO;
		this.list = list;
        this.keywordExtractService = keywordExtractService;
        this.culturalPropertiesKeywordDAO = culturalPropertiesKeywordDAO;
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


			// 키워드 추출 및 저장
			int culturalPropertiesId = culturalPropertiesDTO.getId();
			String content = culturalPropertiesDTO.getContent();
			extractAndSaveKeywords(culturalPropertiesId, content);


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

			adminCulturalPropertiesDAO.deleteDBDataAndKeywords(id);
		}
	}

	public void updateDBData(ArrayList<CulturalPropertiesDTO> data) {

		adminCulturalPropertiesDAO.updateDBData(data);


	}
	
	public ArrayList<CulturalPropertiesDTO> findtotalDBData() {
		
		ArrayList<CulturalPropertiesDTO> list =  adminCulturalPropertiesDAO.findtotalDBData();
		return list;
	
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




	@PostConstruct
	public void initializeKeywords() {
		List<CulturalPropertiesDTO> allCulturalProperties = adminCulturalPropertiesDAO.findAll();

		for (CulturalPropertiesDTO culturalPropertiesDTO : allCulturalProperties) {
			int culturalPropertiesId = culturalPropertiesDTO.getId();
			String content = culturalPropertiesDTO.getContent();

			// cultural_properties_content_keyword 테이블에 해당 cultural_properties_id가 존재하는지 확인
			boolean isKeywordExists = culturalPropertiesKeywordDAO.existsByCulturalPropertiesId(culturalPropertiesId);

			if (!isKeywordExists) {
				// 키워드가 존재하지 않는 경우에만 추출 및 저장 수행
				extractAndSaveKeywords(culturalPropertiesId, content);

			}
		}
	}



	public void extractAndSaveKeywords(int culturalPropertiesId, String content) {
		try {
			// culturalPropertiesId에 해당하는 문화재 데이터 존재 여부 확인
			CulturalPropertiesDTO existingCulturalProperties = adminCulturalPropertiesDAO.selectById(culturalPropertiesId);

			if (existingCulturalProperties != null) {
				ArrayList<String> extractedKeywords = keywordExtractService.getKeywordByGPT(content);

				System.out.println("Extracted Keywords for Cultural Properties ID " + culturalPropertiesId + ":");
				for (String keyword : extractedKeywords) {
					System.out.println(keyword);
				}

				List<CulturalPropertiesKeywordDTO> keywordDTOList = new ArrayList<>();
				for (String keyword : extractedKeywords) {
					CulturalPropertiesKeywordDTO keywordDTO = new CulturalPropertiesKeywordDTO();
					keywordDTO.setCulturalPropertiesId(culturalPropertiesId);
					keywordDTO.setKeyword(keyword);
					keywordDTOList.add(keywordDTO);
				}

				System.out.println("Saving keywords for Cultural Properties ID: " + culturalPropertiesId);
				culturalPropertiesKeywordDAO.saveAll(keywordDTOList);
			} else {
				System.out.println("Cultural Properties ID " + culturalPropertiesId + " does not exist. Skipping keyword extraction and saving.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public List<String> getReviewContentsByCulturalPropertiesId(int culturalPropertiesId) {
		return culturalPropertiesKeywordDAO.getReviewContentsByCulturalPropertiesId(culturalPropertiesId);
	}

	public boolean existsByReviewCulturalPropertiesId(int culturalPropertiesId) {
		return culturalPropertiesKeywordDAO.existsByReviewCulturalPropertiesId(culturalPropertiesId);
	}


	@Transactional
	public void extractAndSaveReviewKeywords(int culturalPropertiesId) {
		List<String> reviewContents = getReviewContentsByCulturalPropertiesId(culturalPropertiesId);
		if (reviewContents.isEmpty()) {
			System.out.println("No reviews found for Cultural Properties ID: " + culturalPropertiesId);
			return;
		}

		String combinedReviewContent = String.join(" ", reviewContents);

		try {
			ArrayList<String> extractedKeywords = keywordExtractService.getKeywordByGPT(combinedReviewContent);
			System.out.println("Extracted Review Keywords for Cultural Properties ID " + culturalPropertiesId + ":");

			List<String> limitedKeywords = extractedKeywords.subList(0, Math.min(5, extractedKeywords.size()));

			for (String keyword : limitedKeywords) {
				System.out.println(keyword);
			}

			List<Integer> existingIds = culturalPropertiesKeywordDAO.getExistingKeywordIds(culturalPropertiesId);

			List<Map<String, Object>> keywordsToUpdate = new ArrayList<>();
			List<String> keywordsToInsert = new ArrayList<>();

			for (int i = 0; i < limitedKeywords.size(); i++) {
				if (i < existingIds.size()) {
					Map<String, Object> updateMap = new HashMap<>();
					updateMap.put("id", existingIds.get(i));
					updateMap.put("keyword", limitedKeywords.get(i));
					keywordsToUpdate.add(updateMap);
				} else {
					keywordsToInsert.add(limitedKeywords.get(i));
				}
			}

			if (!keywordsToUpdate.isEmpty()) {
				culturalPropertiesKeywordDAO.updateExistingKeywords(keywordsToUpdate);
			}

			if (!keywordsToInsert.isEmpty()) {
				culturalPropertiesKeywordDAO.insertNewKeywords(culturalPropertiesId, keywordsToInsert);
			}

			System.out.println("Updated review keywords for Cultural Properties ID: " + culturalPropertiesId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to update review keywords", e);
		}
	}



}