package com.multi.culture_link.festival.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
	private final OkHttpClient client;
	private final Gson gson;
	
	@Value("${API-KEY.youtubeKey}")
	private String youtubeKey;
	
	@Value("${API-KEY.naverArticleClientId}")
	private String XNaverClientId;
	
	@Value("${API-KEY.naverArticleClientSecret}")
	private String XNaverClientSecret;
	
	
	public FestivalServiceImpl(FestivalMapper festivalMapper, OkHttpClient client, Gson gson) {
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
	 * @param festivalId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findContentKeywordListByFestivalId(int festivalId) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findContentKeywordListByFestivalId(festivalId);
		
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
		
		
		festivalName = formattedStart.substring(0, 4) + festivalName + " ";
		
		Request request = new Request.Builder()
				.url("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&order=relevance&q=" + festivalName + "&regionCode=KR&videoDuration=any&type=video&videoEmbeddable=true" + "&key=" + youtubeKey)
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
				.addHeader("Connection", "keep-alive")
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonArray items = json.getAsJsonArray("items");
		
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
		
		JsonObject item = items.get(page - 1).getAsJsonObject();
		
		String title = item.get("title").getAsString();
		
		
		String originalLink = item.get("originallink").getAsString();
		String description = item.get("description").getAsString();
		String pubDate = item.get("pubDate").getAsString().substring(0, 16);
		
		System.out.println("origin link : " + originalLink);
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
		Date date = formatter.parse(pubDate);
		
		
		System.out.println(description);
		
		title = title.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
		description = description.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
		
		System.out.println("description : " + description);
		
		NaverArticleDTO naverArticleDTO = new NaverArticleDTO();
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
		content = content.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
		
		if (content != "") {
			naverArticleDTO.setTotalContent(content);
		}
		
		System.out.println("serviceIMpl : naverArticleDTO : " + naverArticleDTO);
		
		return naverArticleDTO;
	}
	
	/**
	 * 네이버 블로그 api로 해당 블로그의 대략적인 정보와 나와있는 원본 링크를 dto에 저장. 동적으로 열려 셀레니움 필요한 전체 내용과 이미지 크롤링은 실패함
	 *
	 * @param page
	 * @param formattedStart
	 * @param festivalName
	 * @return
	 * @throws Exception
	 */
	@Override
	public NaverBlogDTO findFestivalNaverBlog(int page, String formattedStart, String festivalName) throws Exception {
		festivalName = formattedStart.substring(0, 4) + festivalName + " ";
		
		Request request = new Request.Builder()
				.url("https://openapi.naver.com/v1/search/blog.json?query=" + festivalName + "&display=10&sort=sim")
				.addHeader("X-Naver-Client-Id", XNaverClientId)
				.addHeader("X-Naver-Client-Secret", XNaverClientSecret)
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonArray items = json.getAsJsonArray("items");
		
		JsonObject item = items.get(page - 1).getAsJsonObject();
		
		String title = item.get("title").getAsString();
		String link = item.get("link").getAsString();
		String description = item.get("description").getAsString();
		String bloggerName = item.get("bloggername").getAsString();
		String postDate = item.get("postdate").getAsString();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
		Date date = formatter.parse(postDate);
		
		
		System.out.println(description);
		
		title = title.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
		description = description.replaceAll("</[a-z]*>", "").replaceAll("<[a-z]*>", "").replaceAll("/", "").replaceAll("▲", "");
		
		System.out.println("description : " + description);
		
		
		NaverBlogDTO naverBlogDTO = new NaverBlogDTO();
		naverBlogDTO.setTitle(title);
		naverBlogDTO.setDescription(description);
		naverBlogDTO.setBloggerName(bloggerName);
		naverBlogDTO.setLink(link);
		naverBlogDTO.setPostDate(date);
		
		
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
		System.out.println("serviceIMpl : naverBlogDTO : " + naverBlogDTO);
		
		return naverBlogDTO;
	}
	
	
}
