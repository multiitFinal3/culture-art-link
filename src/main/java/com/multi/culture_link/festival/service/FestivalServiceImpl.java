package com.multi.culture_link.festival.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.culture_link.admin.festival.model.mapper.AdminFestivalMapper;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.*;
import com.multi.culture_link.festival.model.mapper.FestivalMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Service
public class FestivalServiceImpl implements FestivalService {
	
	private final FestivalMapper festivalMapper;
	private final AdminFestivalMapper adminFestivalMapper;
	private final OkHttpClient client;
	private final Gson gson;
	
	@Value("${API-KEY.youtubeKey}")
	private String youtubeKey;
	
	@Value("${API-KEY.naverArticleClientId}")
	private String XNaverClientId;
	
	@Value("${API-KEY.naverArticleClientSecret}")
	private String XNaverClientSecret;
	
	
	public FestivalServiceImpl(FestivalMapper festivalMapper, AdminFestivalMapper adminFestivalMapper, OkHttpClient client, Gson gson) {
		this.adminFestivalMapper = adminFestivalMapper;
		this.festivalMapper = festivalMapper;
		this.client = client;
		this.gson = gson;
	}
	
	/**
	 * 페스티펄 아이디로 연관된 키워드 리스트 구하기
	 *
	 * @param festivalId
	 * @return 키워드 리스트
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalKeywordDTO> findFestivalKeywordByFestivalId(int festivalId) throws Exception {
		
		ArrayList<FestivalKeywordDTO> list = festivalMapper.findFestivalKeywordByFestivalId(festivalId);
		
		return list;
		
	}
	
	/**
	 * 회원 페스티발 찜 관심없음 매핑 테이블에 삽입
	 *
	 * @param map1
	 * @throws Exception
	 */
	@Override
	public void insertUserLoveFestival(UserFestivalLoveHateMapDTO map1) throws Exception {
		
		System.out.println("insertUserLoveFestival impl");
		festivalMapper.insertUserLoveFestival(map1);
		
	}
	
	/**
	 * 회원 찜 관심없음 키워드 매핑이 존재하는 지 확인
	 *
	 * @param userMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserFestivalLoveHateMapDTO findUserMapByUserMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		UserFestivalLoveHateMapDTO userMap2 = festivalMapper.findUserMapByUserMap(userMap);
		
		return userMap2;
		
		
	}
	
	/**
	 * 유저 찜 관심없음 키워드 매핑 테이블에 삽입
	 *
	 * @param userMap
	 * @throws Exception
	 */
	@Override
	public void insertUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		festivalMapper.insertUserKeywordMap(userMap);
		
	}
	
	/**
	 * 이미 존재하는 유저 찜 관심없음 키워드 매핑의 카운트를 증가시킴
	 *
	 * @param userMap
	 * @throws Exception
	 */
	@Override
	public void updateUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		festivalMapper.updateUserKeywordMap(userMap);
		
	}
	
	/**
	 * 회원이 찜한 축제 리스트를 반환
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<Integer> findLoveList(int userId) throws Exception {
		ArrayList<Integer> list = festivalMapper.findLoveList(userId);
		
		return list;
	}
	
	/**
	 * 회원이 찜한 축제 매핑을 삭제
	 *
	 * @param userMap1
	 * @throws Exception
	 */
	@Override
	public void deleteUserLoveFestival(UserFestivalLoveHateMapDTO userMap1) throws Exception {
		
		festivalMapper.deleteUserLoveFestival(userMap1);
		
	}
	
	/**
	 * 회원과 찜 /관심없음 키워드 매핑을 삭제
	 *
	 * @param userMap2
	 * @throws Exception
	 */
	@Override
	public void deleteUserKeywordMap(UserFestivalLoveHateMapDTO userMap2) throws Exception {
		festivalMapper.deleteUserKeywordMap(userMap2);
	}
	
	/**
	 * 회원이 관심없음 버튼을 누른 페스티벌을 매핑 테이블에 삽입
	 *
	 * @param map1
	 * @throws Exception
	 */
	@Override
	public void insertUserHateFestival(UserFestivalLoveHateMapDTO map1) throws Exception {
		festivalMapper.insertUserHateFestival(map1);
	}
	
	/**
	 * 회원이 관심없음을 한 축제 목록을 반환
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<Integer> findHateList(int userId) throws Exception {
		ArrayList<Integer> list = festivalMapper.findHateList(userId);
		
		return list;
	}
	
	/**
	 * 해당 축제의 컨텐트 키워드를 반환
	 *
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findKeywordListByFestivalId(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findKeywordListByFestivalId(mapDTO);
		
		return list;
		
	}
	
	/**
	 * 특정 축제의 시간대를 반환
	 *
	 * @param festivalId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TimeDTO findTimeIdByFestivalId(int festivalId) throws Exception {
		
		TimeDTO timeDTO = festivalMapper.findTimeIdByFestivalId(festivalId);
		
		return timeDTO;
		
	}
	
	/**
	 * 특정 축제의 리뷰를 리뷰 페이지 번호에 맞게 가져오기
	 *
	 * @param vwUserReviewDataDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<VWUserReviewDataDTO> findFestivalReviewListByVWUserReviewDTO(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception {
		
		ArrayList<VWUserReviewDataDTO> list = festivalMapper.findFestivalReviewListByVWUserReviewDTO(vwUserReviewDataDTO);
		
		return list;
		
	}
	
	/**
	 * 특정 축제의 리뷰의 전체 갯수를 가져오기
	 *
	 * @param festivalId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int findFestivalReviewCountByVWUserReviewDTO(int festivalId) throws Exception {
		
		int count = festivalMapper.findFestivalReviewCountByVWUserReviewDTO(festivalId);
		
		return count;
		
	}
	
	/**
	 * 리뷰를 리뷰 테이블에 삽입
	 *
	 * @param userReviewDataDTO
	 * @throws Exception
	 */
	@Override
	public void insertFestivalReview(VWUserReviewDataDTO userReviewDataDTO) throws Exception {
		
		festivalMapper.insertFestivalReview(userReviewDataDTO);
		
	}
	
	/**
	 * 리뷰를 삭제
	 *
	 * @param festivalReviewId
	 * @throws Exception
	 */
	@Override
	public void deleteFestivalReviewByReviewId(int festivalReviewId) throws Exception {
		
		festivalMapper.deleteFestivalReviewByReviewId(festivalReviewId);
		
	}
	
	/**
	 * 리뷰 내용, 별점을 수정
	 *
	 * @param vwUserReviewDataDTO
	 * @throws Exception
	 */
	@Override
	public void updateFestivalReview(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception {
		
		festivalMapper.updateFestivalReview(vwUserReviewDataDTO);
		
	}
	
	/**
	 * 같은 지역이고 본인을 제외한 축제 리스트를 반환
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findSameRegionFestivalByRegionId(FestivalDTO festivalDTO) throws Exception {
		
		ArrayList<FestivalDTO> list = festivalMapper.findSameRegionFestivalByRegionId(festivalDTO);
		
		return list;
	}
	
	/**
	 * 같은 계절이고 본인을 제외한 축제 리스트를 반환
	 *
	 * @param festivalDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findSameSeasonFestivalBySeason(FestivalDTO festivalDTO) throws Exception {
		
		ArrayList<FestivalDTO> list = festivalMapper.findSameSeasonFestivalBySeason(festivalDTO);
		
		return list;
		
	}
	
	
	/**
	 * 같은 주체기관이고 본인을 제외한 축제 리스트를 반환
	 *
	 * @param festivalDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findSameManageFestivalByManageInstitution(FestivalDTO festivalDTO) throws Exception {
		
		ArrayList<FestivalDTO> list = festivalMapper.findSameManageFestivalByManageInstitution(festivalDTO);
		
		return list;
		
	}
	
	/**
	 * 관련된 유튜브 id를 반환
	 *
	 * @param page
	 * @param formattedStart
	 * @param festivalName
	 * @return
	 * @throws Exception
	 */
	@Override
	public String findFestivalYoutube(int page, String formattedStart, String festivalName) throws Exception {
		
		System.out.println("findFestivalYoutube : " + festivalName);

//		festivalName = formattedStart.substring(0, 4) + festivalName + " ";
		
		Request request = new Request.Builder()
				.url("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&order=relevance&q=" + festivalName + "&regionCode=KR&videoDuration=any&type=video&videoEmbeddable=true" + "&key=" + youtubeKey)
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
				.addHeader("Connection", "keep-alive")
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		
		System.out.println("json: " + json.toString());
		
		JsonArray items = json.getAsJsonArray("items");
		
		if (items == null) {
			
			System.out.println("items is null");
			return "noId";
			
		}
		
		JsonObject item = items.get(page - 1).getAsJsonObject();
		
		JsonObject id = item.getAsJsonObject("id");
		String youtubeId = id.get("videoId").getAsString();
		
		System.out.println("festivalName : " + festivalName);
		System.out.println("youtubeId : " + youtubeId);
		
		return youtubeId;
	}
	
	
	/**
	 * 네이버 기사 api로 해당 기사의 대략적인 정보와 나와있는 원본 링크를 이용해 자세한 내용을 dto에 저장
	 *
	 * @param festivalDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public NaverArticleDTO findFestivalNaverArticle(FestivalDTO festivalDTO) throws Exception {
		
		int page = festivalDTO.getPageDTO().getPage();
		
		String festivalName = /*festivalDTO.getFormattedStart().substring(0, 4) +*/ festivalDTO.getFestivalName()/* + " "*/;
		
		Request request = new Request.Builder()
				.url("https://openapi.naver.com/v1/search/news.json?query=" + festivalName + "&display=10&sort=sim")
				.addHeader("X-Naver-Client-Id", XNaverClientId)
				.addHeader("X-Naver-Client-Secret", XNaverClientSecret)
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		int display = json.getAsJsonPrimitive("display").getAsInt();
		JsonArray items = json.getAsJsonArray("items");
		
		NaverArticleDTO naverArticleDTO = null;
		
		if (!items.isEmpty()) {
			
			JsonObject item = items.get(page - 1).getAsJsonObject();
			
			String title = item.get("title").getAsString();
			
			
			String originalLink = item.get("originallink").getAsString();
			String description = item.get("description").getAsString();
			String pubDate = item.get("pubDate").getAsString().substring(0, 16);
			
			System.out.println("origin link : " + originalLink);
			
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
			Date date = formatter.parse(pubDate);
			
			
			System.out.println(description);
			
			title = title.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			description = description.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			
			System.out.println("description : " + description);
			
			naverArticleDTO = new NaverArticleDTO();
			naverArticleDTO.setFestivalId(festivalDTO.getFestivalId());
			naverArticleDTO.setDescription(description);
			naverArticleDTO.setTitle(title);
			naverArticleDTO.setPubDate(date);
			naverArticleDTO.setOriginalLink(originalLink);
			naverArticleDTO.setDisplay(display);
			
			//festival naver content + img
			
			Document document = Jsoup.connect(originalLink).get();
			
			Elements els = document.select("[itemprop=articleBody]");
			Element el = null;
			
			if (els.isEmpty()) {
				
				els = document.select("[id=articleBody]");
				
				if (els.isEmpty()) {
					
					el = document.body();
					
				} else {
					
					el = els.get(0);
					
				}
				
				
			} else {
				
				el = els.get(0);
				
			}
			
			
			String imgUrl = el.select("img").attr("src");
			
			if (!imgUrl.equals("")) {
				
				naverArticleDTO.setImgUrl(imgUrl);
				
			}
			
			
			String content = el.text();
			content = content.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			
			if (content != "") {
				naverArticleDTO.setTotalContent(content);
			}
			
			System.out.println("serviceIMpl : naverArticleDTO : " + naverArticleDTO);
			
		}
		
		
		return naverArticleDTO;
	}
	
	/**
	 * 네이버 블로그 api로 해당 블로그의 대략적인 정보와 나와있는 원본 링크를 dto에 저장. 동적으로 열려 셀레니움 필요한 전체 내용과 이미지 크롤링은 실패함
	 *
	 * @param festivalDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public NaverBlogDTO findFestivalNaverBlog(FestivalDTO festivalDTO) throws Exception {
		
		//festival naver content + img : dynamic하게 로딩되어 셀레니움이 필요해 일단 api 정보로만 저장함

//		Document document = Jsoup.connect(link).get();
//
//		System.out.println("blog link : " + link);
//		Element el = document.select(".se-main-container").get(0);
//
//
//		String imgUrl = el.select("img").attr("src");
//
//		if (!imgUrl.equals("")) {
//
//			naverBlogDTO.setImgUrl(imgUrl);
//
//		}
//
//
//		String content = el.text();
//		content = content.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
//
//		if (content != "") {
//			naverBlogDTO.setTotalContent(content);
//		}
//
		
		int page = festivalDTO.getPageDTO().getPage();
		
		String festivalName = /*festivalDTO.getFormattedStart().substring(0, 4) +*/ festivalDTO.getFestivalName()/* + " "*/;
		
		Request request = new Request.Builder()
				.url("https://openapi.naver.com/v1/search/blog.json?query=" + festivalName + "&display=10&sort=sim")
				.addHeader("X-Naver-Client-Id", XNaverClientId)
				.addHeader("X-Naver-Client-Secret", XNaverClientSecret)
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		int display = json.getAsJsonPrimitive("display").getAsInt();
		JsonArray items = json.getAsJsonArray("items");
		
		NaverBlogDTO naverBlogDTO = new NaverBlogDTO();
		
		if (!items.isEmpty()) {
			
			JsonObject item = items.get(page - 1).getAsJsonObject();
			String title = item.get("title").getAsString();
			String link = item.get("link").getAsString();
			String description = item.get("description").getAsString();
			String postdate = item.get("postdate").getAsString();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date date = formatter.parse(postdate);
			
			
			System.out.println(description);
			
			title = title.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replace("&lt", "").replace("&gt", "").replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			description = description.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replace("&lt", "").replace("&gt", "").replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			
			System.out.println("description : " + description);
			
			naverBlogDTO.setFestivalId(festivalDTO.getFestivalId());
			naverBlogDTO.setDescription(description);
			naverBlogDTO.setTitle(title);
			naverBlogDTO.setPostDate(date);
			naverBlogDTO.setLink(link);
			naverBlogDTO.setDisplay(display);
			
			//festival naver blog content + img : 동적으로 열려서 셀레니움 필요함

//			Document document = Jsoup.connect(link).get();
//			Elements els = document.select("[itemprop=articleBody]");
//			Element el = null;
//
//			if (els.isEmpty()) {
//				els = document.select("[id=articleBody]");
//				if (els.isEmpty()) {
//					el = document.body();
//				} else {
//					el = els.get(0);
//				}
//
//			} else {
//				el = els.get(0);
//			}
//
//
//			String imgUrl = el.select("img").attr("src");
//
//			if (!imgUrl.equals("")) {
//				naverBlogDTO.setImgUrl(imgUrl);
//			}
//
//
//			String content = el.text();
//			content = content.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
//
//			if (content != "") {
//				naverBlogDTO.setTotalContent(content);
//			}
//
//			System.out.println("serviceIMpl : naverBlogDTO : " + naverBlogDTO);
			
		}
		
		
		return naverBlogDTO;
	}
	
	/**
	 * 본인을 제외한 같은 키워드의 축제를 추천함
	 *
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findSameKeywordFestivalByfestivalId(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> mapDTOS = festivalMapper.findKeywordListByFestivalId(mapDTO);
		
		ArrayList<Integer> festivalIdList = new ArrayList<>();
		
		for (FestivalContentReviewNaverKeywordMapDTO mapDTO1 : mapDTOS) {
			
			String festivalKeywordId = mapDTO1.getFestivalKeywordId();
			System.out.println("findSameKeywordFestivalByfestivalId의 키워드 : " + festivalKeywordId);
			FestivalContentReviewNaverKeywordMapDTO mapDTO2 = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO2.setFestivalKeywordId(festivalKeywordId);
			
			ArrayList<FestivalContentReviewNaverKeywordMapDTO> festivalIdList1 = festivalMapper.findKeywordListByFestivalId(mapDTO2);
			
			for (FestivalContentReviewNaverKeywordMapDTO map : festivalIdList1) {
				
				int festivalId = map.getFestivalId();
				if (!(festivalIdList.contains(festivalId)) && (festivalId != mapDTO.getFestivalId())) {
					festivalIdList.add(festivalId);
				}
				
			}
			
			
		}
		
		ArrayList<FestivalDTO> list = new ArrayList<>();
		
		for (int i : festivalIdList) {
			
			FestivalDTO festivalDTO = adminFestivalMapper.findDBFestivalByFestivalId(i);
			list.add(festivalDTO);
			
		}
		
		return list;
		
	}
	
	/**
	 * tf_idf가 높은 키워드 순으로 추천 키워드로 추가됨
	 *
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findPopularFestivalKeyword(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception {
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findPopularFestivalKeyword(mapDTO);
		return list;
	}
	
	/**
	 * 유저가 회원가입 시 선택한 키워드를 테이블에 삽입
	 *
	 * @param mapDTO
	 * @throws Exception
	 */
	@Override
	public void insertUserSelectKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception {
		
		festivalMapper.insertUserSelectKeyword(mapDTO);
		
	}
	
	/**
	 * 유저의 선택과 찜에 의한 선호도 모두를 반영하는 뷰에서 카운트 총 합이 10개 이상인 키워드만 반환
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findUserLoveKeywordList(int userId) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findUserLoveKeywordList(userId);
		
		return list;
	}
	
	/**
	 * 같은 키워드를 가지는 축제 리스트를 반환
	 *
	 * @param festivalKeywordId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findSameKeywordFestivalByKeywordId(String festivalKeywordId) throws Exception {
		
		ArrayList<FestivalDTO> list = festivalMapper.findSameKeywordFestivalByKeywordId(festivalKeywordId);
		
		return list;
	}
	
	/**
	 * 유저가 찜 관심없음 처리한 축제 리스트를 반환
	 *
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalDTO> findLoveHateFestivalList(UserFestivalLoveHateMapDTO mapDTO) throws Exception {
		
		System.out.println("findLoveHateFestivalList serimpl ");
		ArrayList<FestivalDTO> list = festivalMapper.findLoveHateFestivalList(mapDTO);
		System.out.println("ser impl : " + list);
		return list;
		
	}
	
	/**
	 * 특정 유저의 전체 리뷰 내역을 반환
	 *
	 * @param vwUserReviewDataDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<VWUserReviewDataDTO> findFestivalReviewListByUserId(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception {
		
		ArrayList<VWUserReviewDataDTO> list = new ArrayList<>();
		list = festivalMapper.findFestivalReviewListByUserId(vwUserReviewDataDTO);
		return list;
		
	}
	
	
	/**
	 * 특정 유저가 작성한 리뷰 전부의 수를 반환
	 * @param vwUserReviewDataDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public int findUserReviewCountByUserReviewDataDTO(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception {
		
		int count = festivalMapper.findUserReviewCountByUserReviewDataDTO(vwUserReviewDataDTO);
		return count;
		
	}
	
	
	/**
	 * 특정 유저의 찜 또는 관심없음 한 키워드의 카운트가 10이상인 것만 조회
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findFestivalBigLoveHateKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findFestivalBigLoveHateKeyword(mapDTO);
		
		return list;
		
	}
	
	
	/**
	 * 유저의 찜 / 관심없음 키워드의 카운트가 10 미만인 것을 카운트 내림차순으로 반환
	 * @param mapDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findFestivalSmallLoveHateKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findFestivalSmallLoveHateKeyword(mapDTO);
		return list;
		
	}
	
}
