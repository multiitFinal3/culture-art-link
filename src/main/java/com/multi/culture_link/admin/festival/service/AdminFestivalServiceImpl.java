package com.multi.culture_link.admin.festival.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.culture_link.admin.festival.model.mapper.AdminFestivalMapper;
import com.multi.culture_link.common.keyword.service.KeywordExtractService1;
import com.multi.culture_link.festival.model.dto.FestivalContentReviewNaverKeywordMapDTO;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 페스티벌 관리자의 서비스 구현 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Service
public class AdminFestivalServiceImpl implements AdminFestivalService {
	
	private final AdminFestivalMapper adminFestivalMapper;
	private final OkHttpClient client;
	private final Gson gson;
	private final KeywordExtractService1 keywordExtractService;
	
	
	ArrayList<FestivalDTO> list = new ArrayList<>();
	
	
	/**
	 * 생성자 주입
	 *
	 * @param client                api와의 통신을 위해 사용
	 * @param gson                  json 오브젝트를 다시 java 객체로 역직렬화
	 * @param adminFestivalMapper   페스티벌 관리자 매퍼
	 * @param keywordExtractService 키워드 추출 서비스
	 */
	public AdminFestivalServiceImpl(OkHttpClient client, Gson gson, OkHttpClient client1, AdminFestivalMapper adminFestivalMapper, KeywordExtractService1 keywordExtractService) {
		this.client = client;
		this.gson = gson;
		this.adminFestivalMapper = adminFestivalMapper;
		this.keywordExtractService = keywordExtractService;
	}
	
	
	/**
	 * api에서 받은 리스트를 가공하여 서비스임플단에 저장하고 페이지에 해당하는 목록을 찾아 일부만 반환
	 *
	 * @param page 페이지 버튼에서 받아오는 페이지 번호
	 * @return 페스티벌들의 리스트를 반환
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public ArrayList<FestivalDTO> findAPIFestivalList(int page) throws Exception {
		
		Request request = new Request.Builder()
				.url("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6/JmV1TOw/Yu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA==&pageNo=1&numOfRows=100&type=json")
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
				.addHeader("Connection", "keep-alive")
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonObject response1 = json.getAsJsonObject("response");
		JsonObject body1 = response1.getAsJsonObject("body");
		JsonArray items = body1.getAsJsonArray("items");
		/*System.out.println("items : " + items);*/
		
		ArrayList<FestivalDTO> list = new ArrayList<>();
		
		PageDTO pageDTO = new PageDTO();
		pageDTO.setStartEnd(page);
		int start = pageDTO.getStart();
		int end = pageDTO.getEnd();
		
		for (int i = 0; i < items.size(); i++) {
			
			JsonObject item = items.get(i).getAsJsonObject();
			
			FestivalDTO festivalDTO = new FestivalDTO();
			
			String festivalName = item.get("fstvlNm").getAsString();
			festivalDTO.setFestivalName(festivalName);
			
			String place = item.get("opar").getAsString();
			festivalDTO.setPlace(place);
			
			String detailAddress = item.get("lnmadr").getAsString().trim();
			festivalDTO.setDetailAddress(detailAddress);
			
			String firstAddress = detailAddress.split(" ")[0];
			
			int regionId = 66;
			
			switch (firstAddress) {
				
				case "서울특별시":
					regionId = 11;
					break;
				
				case "부산광역시":
					regionId = 21;
					break;
				
				case "대구광역시":
					regionId = 21;
					break;
				
				case "인천광역시":
					regionId = 23;
					break;
				
				case "광주광역시":
					regionId = 24;
					break;
				
				case "대전광역시":
					regionId = 25;
					break;
				
				case "울산광역시":
					regionId = 26;
					break;
				
				case "경기도":
					regionId = 31;
					break;
				
				case "강원특별자치도":
					regionId = 32;
					break;
				
				case "충청북도":
					regionId = 33;
					break;
				
				case "충청남도":
					regionId = 34;
					break;
				
				case "전북특별자치도":
					regionId = 35;
					break;
				
				case "전라남도":
					regionId = 36;
					break;
				
				case "경상북도":
					regionId = 37;
					break;
				
				
				case "경상남도":
					regionId = 38;
					break;
				
				
				case "세종특별자치시":
					regionId = 45;
					break;
				
				
				case "제주특별자치도":
					regionId = 50;
					break;
				
				
			}
			
			festivalDTO.setRegionId(regionId);
			
			String startD = item.get("fstvlStartDate").getAsString();
			String endD = item.get("fstvlEndDate").getAsString();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			Date startDate = null;
			Date endDate = null;
			String timeId = "";
			String timeDescription = "";
			
			try {
				startDate = format.parse(startD);
				endDate = format.parse(endD);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			festivalDTO.setStartDate(startDate);
			festivalDTO.setEndDate(endDate);
			
			
			int diffDays = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 24 * 60 * 60));
			
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(startDate);
			int startMonth = calendar1.get(Calendar.MONTH) + 1;
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(endDate);
			int endMonth = calendar2.get(Calendar.MONTH) + 1;
			
			
			String season = "";
			
			if ((startMonth == 12) || (startMonth == 1) || (startMonth == 2)) {
				
				season = "겨울";
				
			} else if ((startMonth == 3) || (startMonth == 4) || (startMonth == 5)) {
				
				season = "봄";
				
			} else if ((startMonth == 6) || (startMonth == 7) || (startMonth == 8)) {
				
				season = "여름";
				
			} else {
				
				season = "가을";
				
			}

			festivalDTO.setSeason(season);
			
			ArrayList<Integer> days = new ArrayList<Integer>();
			
			for (int j = 0; j < diffDays + 1; j++) {
				
				/*System.out.println("축제 기간 : " + (diffDays + 1));*/
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startDate);
				calendar.add(Calendar.DATE, j);
				
				/*System.out.println("calendar.get(Calendar.DAY_OF_WEEK) : " + calendar.get(Calendar.DAY_OF_WEEK));*/
				days.add(calendar.get(Calendar.DAY_OF_WEEK));
				
			}
			
			if (((days.contains(1)) || (days.contains(7)))) {
				
				if ((days.contains(2)) || (days.contains(3)) || (days.contains(4)) || (days.contains(5)) || (days.contains(6))) {
					
					timeId = "A";
					timeDescription = "평일 , 주말 전부 포함";
					
				} else {
					
					timeId = "WE";
					timeDescription = "토~ 일 사이";
					
				}
				
			} else {
				
				timeId = "WD";
				timeDescription = "월 ~ 금 사이";
				
			}
			
			
			festivalDTO.setTimeId(timeId);
			festivalDTO.setTimeDescription(timeDescription);
			
			
			String content1 = item.get("fstvlCo").getAsString().replace("+", ", ") + ". ";
			
			
			festivalDTO.setFestivalContent(content1);
			
			String manageInstitution = item.get("mnnstNm").getAsString().replace("+", ", ");
			String hostInstitution = item.get("auspcInsttNm").getAsString().replace("+", ", ");
			String sponserInstitution = item.get("suprtInsttNm").getAsString().replace("+", ", ");
			
			festivalDTO.setManageInstitution(manageInstitution);
			festivalDTO.setHostInstitution(hostInstitution);
			festivalDTO.setSponserInstitution(sponserInstitution);
			
			festivalDTO.setTel(item.get("phoneNumber").getAsString());
			festivalDTO.setHomepageUrl(item.get("homepageUrl").getAsString());
			
			
			double lat = item.get("latitude") == null ? 0.0
					: item.get("latitude").getAsString().equals("") ? 0.0
					: item.get("latitude").getAsDouble();
			
			
			double longi = item.get("longitude") == null ? 0.0
					: item.get("longitude").getAsString().equals("") ? 0.0
					: item.get("longitude").getAsDouble();
			
			
			festivalDTO.setLatitude(lat);
			festivalDTO.setLongtitude(longi);
			
			festivalDTO.setAvgRate(0);

			FestivalDTO festivalExist = adminFestivalMapper.findDBFestivalByFestival(festivalDTO);
			
			if (festivalExist != null) {
				/*System.out.println("리스트에서 제외 : " + festivalExist.toString());*/
				/*festivalDTO.setExist("Y");*/
				continue;
				
			}
			
			
			list.add(festivalDTO);
			
		}

		this.list = list;
		
		ArrayList<FestivalDTO> list2 = new ArrayList<FestivalDTO>();
		
		int realEnd = list.size() > end ? end : list.size();
		
		if (list.size() > 0) {
			
			for (int i = start - 1; i <= realEnd - 1; i++) {
				
				list2.add(list.get(i));
				
			}
			
		}
		
		return list2;
		
		
	}
	
	
	/**
	 * 현재 저장되어있는 페스티벌 리스트의 번호를 받아 DB에 저장함
	 *
	 * @param numList 삽입을 할 리스트의 번호로, 현재 impl 단에서 저장된 리스트의 순서이다
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public void insertAPIFestivalList(ArrayList<Integer> numList) throws Exception {
		
		
		for (int i : numList) {
			
			FestivalDTO festivalDTO = list.get(i);
			
			String festivalName = festivalDTO.getFestivalName();
			String content1 = festivalDTO.getFestivalContent();
			
			
			//festival naver content + img
			
			Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=" + festivalName + "+기본정보").get();
			
			String title = document.title();
			
			String imgUrl = document.select("div.detail_info > a > img").attr("src");
			
			if (!imgUrl.equals("")) {
				
				festivalDTO.setImgUrl(imgUrl);
				
			}
			
			String content2 = document.select("div.intro_box > p.text").text();
			
			
			String festivalContent = content1 + content2;
			
			festivalDTO.setFestivalContent(festivalContent);
			
			adminFestivalMapper.insertAPIFestival(festivalDTO);
			
		}
		
		
	}
	
	
	/**
	 * 페이지 버튼에서 넘어간 페이지 번호에 해당하는 DB의 페스티벌 리스트를 반환하며 1페이지 당 5개씩 가져옴
	 *
	 * @param pageDTO 페이지 버튼의 숫자
	 * @return 해당하는 페스티벌 리스트
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public ArrayList<FestivalDTO> findDBFestivalList(PageDTO pageDTO) throws Exception {
		
		ArrayList<FestivalDTO> list = adminFestivalMapper.findDBFestivalList(pageDTO);

		return list;
		
	}
	
	/**
	 * 전체 축제 DB의 갯수를 반환함
	 *
	 * @return DB에 저장된 전체 축제 갯수
	 * @throws Exception
	 */
	@Override
	public int findDBFestivalCount() throws Exception {
		int count = adminFestivalMapper.findDBFestivalCount();
		return count;
	}
	
	/**
	 * 페스티벌 리스트 삭제를 위해 해당 번호의 리스트를 받아서 DB에서 삭제
	 *
	 * @param checks 체크된 항목의 번호 리스트
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public void deleteDBFestivalList(ArrayList<Integer> checks) throws Exception {
		
		
		for (int i = 0; i < checks.size(); i++) {
			
			int festivalId = checks.get(i);
			
			adminFestivalMapper.deleteDBFestivalList(festivalId);
			
		}
		
		
	}
	
	/**
	 * 페스티벌 아이디를 이용해 페스티벌을 찾음
	 *
	 * @param festivalId DB의 페스티벌 아이디
	 * @return 페스티벌 DTO 반환
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public FestivalDTO findDBFestivalByFestivalId(int festivalId) throws Exception {
		
		FestivalDTO festivalDTO = adminFestivalMapper.findDBFestivalByFestivalId(festivalId);
		
		return festivalDTO;
	}
	
	/**
	 * 페스티벌 DTO를 이용해 페스티벌의 정보를 업데이트 함
	 *
	 * @param festivalDTO 해당 DB번호 및 수정된 정보를 담고있는 페스티벌 DTO
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public void updateDBFestivalByFestival(FestivalDTO festivalDTO) throws Exception {
		
		adminFestivalMapper.updateDBFestivalByFestival(festivalDTO);
		
	}
	
	/**
	 * 다중 조건을 이용한 DB검색으로 축제 리스트를 반환
	 *
	 * @param festivalDTO 조건의 내용을 담고있는 페스티벌 DTO
	 * @return 페스티벌  DTO 리스트 반환
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public ArrayList<FestivalDTO> findDBFestivalByMultiple(FestivalDTO festivalDTO) throws Exception {
		ArrayList<FestivalDTO> list = adminFestivalMapper.findDBFestivalByMultiple(festivalDTO);
		
		return list;
	}
	
	/**
	 * 다중 조건을 만족하는 축제 데이터의 갯수 반환
	 *
	 * @param festivalDTO 다중조건 담고있는 페스티벌 DTO
	 * @return 다중 조건을 만족 축제 데이터의 갯수
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public int findDBFestivalMultipleCount(FestivalDTO festivalDTO) throws Exception {
		int count = adminFestivalMapper.findDBFestivalMultipleCount(festivalDTO);
		
		return count;
	}
	
	/**
	 * API 다중조건 검색 및 가공 후 서비스 임플단에 저장, 해당 페이지의 부분만 반환
	 *
	 * @param festivalDTO 필요한 페이지 정보를 담고있는 페스티벌 DTO
	 * @param urls        요청 파라미터들을 다 연결한 전체 스트링
	 * @return 해당하는 축제 리스트 반환
	 * @throws Exception 예외를 컨트롤러까지 던짐
	 */
	@Override
	public ArrayList<FestivalDTO> findAPIFestivalByMultiple(FestivalDTO festivalDTO, String urls) throws Exception {
		
		int page = festivalDTO.getPageDTO().getPage();
		
		String url1 = "http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6/JmV1TOw/Yu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA==&pageNo=1";
		
		// 조건에 의한 파라미터들
		String url2 = urls;
		
		String url3 = "&numOfRows=100&type=json";
		
		
		String urlFinal = url1 + url2 + url3;

		Request request = new Request.Builder()
				.url(urlFinal)
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
				.addHeader("Connection", "keep-alive")
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonObject response1 = json.getAsJsonObject("response");
		JsonObject body1 = response1.getAsJsonObject("body");
		JsonArray items = body1.getAsJsonArray("items");
		/*System.out.println("items : " + items);*/
		
		ArrayList<FestivalDTO> list = new ArrayList<>();
		
		PageDTO pageDTO = new PageDTO();
		pageDTO.setStartEnd(page);
		int start = pageDTO.getStart();
		int end = pageDTO.getEnd();
		
		for (int i = 0; i < items.size(); i++) {
			
			JsonObject item = items.get(i).getAsJsonObject();
			
			FestivalDTO festivalDTO2 = new FestivalDTO();
			
			String festivalName = item.get("fstvlNm").getAsString();
			festivalDTO2.setFestivalName(festivalName);
			
			String place = item.get("opar").getAsString();
			festivalDTO2.setPlace(place);
			
			String detailAddress = item.get("lnmadr").getAsString().trim();
			festivalDTO2.setDetailAddress(detailAddress);
			
			String firstAddress = detailAddress.split(" ")[0];
			
			int regionId = 66;
			
			switch (firstAddress) {
				
				case "서울특별시":
					regionId = 11;
					break;
				
				case "부산광역시":
					regionId = 21;
					break;
				
				case "대구광역시":
					regionId = 21;
					break;
				
				case "인천광역시":
					regionId = 23;
					break;
				
				case "광주광역시":
					regionId = 24;
					break;
				
				case "대전광역시":
					regionId = 25;
					break;
				
				case "울산광역시":
					regionId = 26;
					break;
				
				case "경기도":
					regionId = 31;
					break;
				
				case "강원특별자치도":
					regionId = 32;
					break;
				
				case "충청북도":
					regionId = 33;
					break;
				
				case "충청남도":
					regionId = 34;
					break;
				
				case "전북특별자치도":
					regionId = 35;
					break;
				
				case "전라남도":
					regionId = 36;
					break;
				
				case "경상북도":
					regionId = 37;
					break;
				
				
				case "경상남도":
					regionId = 38;
					break;
				
				
				case "세종특별자치시":
					regionId = 45;
					break;
				
				
				case "제주특별자치도":
					regionId = 50;
					break;
				
				
			}
			
			festivalDTO2.setRegionId(regionId);
			
			String startD = item.get("fstvlStartDate").getAsString();
			String endD = item.get("fstvlEndDate").getAsString();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			Date startDate = null;
			Date endDate = null;
			String timeId = "";
			String timeDescription = "";
			
			try {
				startDate = format.parse(startD);
				endDate = format.parse(endD);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			festivalDTO2.setStartDate(startDate);
			festivalDTO2.setEndDate(endDate);
			
			
			int diffDays = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 24 * 60 * 60));
			
			/*System.out.println(startDate);
			System.out.println(endDate);
			System.out.println("일 수 차이 : " + diffDays);*/
			
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(startDate);
			int startMonth = calendar1.get(Calendar.MONTH) + 1;
			/*System.out.println("시작월 : " + startMonth);*/
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(endDate);
			int endMonth = calendar2.get(Calendar.MONTH) + 1;
			/*System.out.println(endMonth);*/
			
			
			String season = "";
			
			if ((startMonth == 12) || (startMonth == 1) || (startMonth == 2)) {
				
				season = "겨울";
				
			} else if ((startMonth == 3) || (startMonth == 4) || (startMonth == 5)) {
				
				season = "봄";
				
			} else if ((startMonth == 6) || (startMonth == 7) || (startMonth == 8)) {
				
				season = "여름";
				
			} else {
				
				season = "가을";
				
			}

			festivalDTO2.setSeason(season);
			
			ArrayList<Integer> days = new ArrayList<Integer>();
			
			for (int j = 0; j < diffDays + 1; j++) {
				
				/*System.out.println("축제 기간 : " + (diffDays + 1));*/
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startDate);
				calendar.add(Calendar.DATE, j);
				
				/*System.out.println("calendar.get(Calendar.DAY_OF_WEEK) : " + calendar.get(Calendar.DAY_OF_WEEK));*/
				days.add(calendar.get(Calendar.DAY_OF_WEEK));
				
			}
			
			if (((days.contains(1)) || (days.contains(7)))) {
				
				if ((days.contains(2)) || (days.contains(3)) || (days.contains(4)) || (days.contains(5)) || (days.contains(6))) {
					
					timeId = "A";
					timeDescription = "평일 , 주말 전부 포함";
					
				} else {
					
					timeId = "WE";
					timeDescription = "토~ 일 사이";
					
				}
				
			} else {
				
				timeId = "WD";
				timeDescription = "월 ~ 금 사이";
				
			}
			
			
			festivalDTO2.setTimeId(timeId);
			festivalDTO2.setTimeDescription(timeDescription);
			
			//festival content
			
			String content1 = item.get("fstvlCo").getAsString().replace("+", ", ") + ". ";
			
			
			Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=" + festivalName + "+기본정보").get();
			
			String title = document.title();
			
			String imgUrl = document.select("div.detail_info > a > img").attr("src");
			
			if (!imgUrl.equals("")) {
				
				festivalDTO2.setImgUrl(imgUrl);
				
			}
			
			String content2 = document.select("div.intro_box > p.text").text();
			
			
			String festivalContent = content1 + content2;
			festivalDTO2.setFestivalContent(festivalContent);
			
			String manageInstitution = item.get("mnnstNm").getAsString().replace("+", ", ");
			String hostInstitution = item.get("auspcInsttNm").getAsString().replace("+", ", ");
			String sponserInstitution = item.get("suprtInsttNm").getAsString().replace("+", ", ");
			
			festivalDTO2.setManageInstitution(manageInstitution);
			festivalDTO2.setHostInstitution(hostInstitution);
			festivalDTO2.setSponserInstitution(sponserInstitution);
			
			festivalDTO2.setTel(item.get("phoneNumber").getAsString());
			festivalDTO2.setHomepageUrl(item.get("homepageUrl").getAsString());
			
			
			double lat = item.get("latitude") == null ? 0.0
					: item.get("latitude").getAsString().equals("") ? 0.0
					: item.get("latitude").getAsDouble();
			
			
			double longi = item.get("longitude") == null ? 0.0
					: item.get("longitude").getAsString().equals("") ? 0.0
					: item.get("longitude").getAsDouble();
			
			
			festivalDTO2.setLatitude(lat);
			festivalDTO2.setLongtitude(longi);
			
			festivalDTO2.setAvgRate(0);
			
			FestivalDTO festivalExist = adminFestivalMapper.findDBFestivalByFestival(festivalDTO2);
			
			if (festivalExist != null) {
				/*System.out.println("리스트에서 제외 : " + festivalExist.toString());*/
				/*festivalDTO2.setExist("Y");*/
				continue;
				
			}
			
			
			list.add(festivalDTO2);
			
		}
		
		
		System.out.println("impl list: " + list);
		
		this.list = list;
		
		ArrayList<FestivalDTO> list2 = new ArrayList<FestivalDTO>();
		
		int realEnd = list.size() > end ? end : list.size();
		
		if (list.size() > 0) {
			
			for (int i = start - 1; i <= realEnd - 1; i++) {
				
				list2.add(list.get(i));
				
			}
			
		}
		
		
		return list2;
	}
	
	/**
	 * API 다중조건 검색 후 전체 갯수 반환
	 *
	 * @param festivalDTO
	 * @param urls 요청파라미터 url
	 * @return
	 * @throws Exception
	 */
	@Override
	public int findAPIFestivalByMultipleCount(FestivalDTO festivalDTO, String urls) throws Exception {
		
		String url1 = "http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6/JmV1TOw/Yu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA==&pageNo=1";
		
		// 조건에 의한 파라미터들
		String url2 = urls;
		
		String url3 = "&numOfRows=100&type=json";
		
		
		String urlFinal = url1 + url2 + url3;
		System.out.println("urlFinal : " + urlFinal);
		
		Request request = new Request.Builder()
				.url(urlFinal)
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
				.addHeader("Connection", "keep-alive")
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonObject response1 = json.getAsJsonObject("response");
		JsonObject body1 = response1.getAsJsonObject("body");
		int count = body1.getAsJsonPrimitive("totalCount").getAsInt();
		
		System.out.println("count : " + count);
		
		
		return count;
	}
	
	/**
	 * 해당 축제의 내용에서 추출한 키워드를 삽입
	 *
	 * @param festivalId 해당 페스티벌 DB 아이디
	 * @return 키워드 해시맵 반환
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Integer> findContentKeywordByFestivalId(int festivalId) throws Exception {
		
		FestivalDTO festivalDTO = adminFestivalMapper.findDBFestivalByFestivalId(festivalId);
		String content = festivalDTO.getFestivalContent();
		String title = festivalDTO.getFestivalName();
		
		String all = content + " " + title;
		
		// 코모란으로 결정
		HashMap<String, Integer> map = keywordExtractService.getKeywordByKomoran(all);
//		ArrayList<String> list = keywordExtractService.getKeywordByApacheLucene(all);
		
		LinkedList<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		
		if (list.size() >= 5) {
			
			list = list.stream().limit(5).collect(Collectors.toCollection(LinkedList::new));
			
		}
		
		map = new HashMap<String, Integer>();
		
		for (int i = 0; i < list.size(); i++) {
			
			map.put(list.get(i).getKey(), list.get(i).getValue());
			
		}
		
		System.out.println("keyword map top 5: " + map);
		
		
		return map;
	}
	
	/**
	 * 키워드가 키워드 테이블에 이미 존재하는 지 확인
	 * @param keyword 키워드 DTO
	 * @return 키워드 DTO
	 * @throws Exception
	 */
	@Override
	public FestivalKeywordDTO findKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception {
		
		FestivalKeywordDTO festivalKeywordDTO =  adminFestivalMapper.findKeywordByKeyword(keyword);
		
		return festivalKeywordDTO;
	}
	
	/**
	 * 키워드를 키워드 테이블에 삽입
	 * @param keyword 키워드 DTO
	 * @throws Exception
	 */
	@Override
	public void insertKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception {
		
		adminFestivalMapper.insertKeywordByKeyword(keyword);
	}
	
	/**
	 * 해당 키워드의 매핑이 존재하는 지 확인
	 * @param keywordMapping1 키워드 DTO
	 * @return 키워드 매핑 DTO
	 * @throws Exception
	 */
	@Override
	public FestivalContentReviewNaverKeywordMapDTO findKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping1) throws Exception {
		
		FestivalContentReviewNaverKeywordMapDTO keywordMapping = adminFestivalMapper.findKeywordMappingByKeywordMapping(keywordMapping1);
		
		return keywordMapping;
	}
	
	/**
	 * 축제 - 키워드 매핑을 인서트
	 * @param keywordMapping 키워드 매핑 DTO
	 * @throws Exception
	 */
	@Override
	public void insertKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping) throws Exception {
		
		adminFestivalMapper.insertKeywordMappingByKeywordMapping(keywordMapping);
		
	}
	
	
}

