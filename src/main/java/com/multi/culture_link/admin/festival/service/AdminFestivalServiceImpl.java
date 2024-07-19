package com.multi.culture_link.admin.festival.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.admin.festival.model.mapper.AdminFestivalMapper;
import com.multi.culture_link.festival.model.dto.PageDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class AdminFestivalServiceImpl implements AdminFestivalService {
	
	private final AdminFestivalMapper adminFestivalMapper;
	private final OkHttpClient client;
	private final Gson gson;
	
	ArrayList<FestivalDTO> list = new ArrayList<>();
	
	public AdminFestivalServiceImpl(OkHttpClient client, Gson gson, OkHttpClient client1, AdminFestivalMapper adminFestivalMapper) {
		this.client = client;
		this.gson = gson;
		this.adminFestivalMapper = adminFestivalMapper;
	}
	
	@Override
	public ArrayList<FestivalDTO> findAPIFestivalList(int page) throws Exception {
		
		//크롬인 것 처럼 속이려했으나 빠르게 클릭하면 여전히 네이버 서버에서 막는다
		Request request = new Request.Builder()
				.url("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6/JmV1TOw/Yu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA==&pageNo=" + page + "&numOfRows=5&type=json")
				.addHeader("User-Agent", "Mozilla/5.0")
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
		
		for (int i = 0; i < items.size(); i++) {
			
			JsonObject item = items.get(i).getAsJsonObject();
			
			/*System.out.println((i+1) + " : " + item);
			System.out.println(item.get("fstvlNm").getAsString());*/
			
			//			400 : {"fstvlNm":"한탄강얼음트레킹축제","opar":"철원 한탄강 물윗길 트레킹 코스 일원","fstvlStartDate":"2024-01-13","fstvlEndDate":"2024-01-21","fstvlCo":"공연+행사+포토존+아이스 썰매존+아이스 겨울 놀이터+아이스 고드름 터널+픽토그램 눈썰매장 등","mnnstNm":"철원문화재단","auspcInsttNm":"철원문화재단","suprtInsttNm":"강원특별자치도 철원군청+철원군의회","phoneNumber":"033-455-7072","homepageUrl":"https://gcwcf.or.kr/w1_c_6_1/4","relateInfo":"","rdnmadr":"강원특별자치도 철원군 동송읍 한탄강길 208","lnmadr":"강원특별자치도 철원군 동송읍 장흥리 725","latitude":"38.20344715","longitude":"127.2700356","referenceDate":"2024-05-27","insttCode":"B551011"}
			//			한탄강얼음트레킹축제
			
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
			
			try {
				startDate = format.parse(startD);
				endDate = format.parse(endD);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			festivalDTO.setStartDate(startDate);
			festivalDTO.setEndDate(endDate);
			
			
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
			
			System.out.println("계절 : " + season);
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
					
					timeId = "전체";
					
				} else {
					
					timeId = "주말";
					
				}
				
			} else {
				
				timeId = "평일";
				
			}
			
			
			festivalDTO.setTimeId(timeId);
			
			//festival content
			
			String content1 = item.get("fstvlCo").getAsString().replace("+", ", ") + ". ";
			
			
			Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=" + festivalName + "+기본정보").get();
			
			String title = document.title();
			System.out.println("title : " + title);
			
			
			String imgUrl = document.select("div.detail_info > a > img").attr("src");
			
			if (!imgUrl.equals("")) {
				
				festivalDTO.setImgUrl(imgUrl);
				
			} else {
			
			
			}
			
			
			String content2 = document.select("div.intro_box > p.text").text();
			
			
			String festivalContent = content1 + content2;
			festivalDTO.setFestivalContent(festivalContent);
			
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
			
			System.out.println("festivalDTO : " + festivalDTO.toString());
			
			list.add(festivalDTO);
			
		}
		
		
		System.out.println("impl list: " + list);
		
		this.list = list;
		
		return list;
		
		
	}
	
	@Override
	public void insertAPIFestivalList(ArrayList<Integer> numList) throws Exception {
		
		
		for (int i : numList) {
			
			FestivalDTO festivalDTO = list.get(i);
			
			System.out.println(festivalDTO.toString());
			
			adminFestivalMapper.insertAPIFestival(festivalDTO);
			
		}
		
		
	}
	
	@Override
	public ArrayList<FestivalDTO> findDBFestivalList(PageDTO pageDTO) throws Exception {
		
		ArrayList<FestivalDTO> list = adminFestivalMapper.findDBFestivalList(pageDTO);
		
		System.out.println("impl list : " + list);
		
		return list;
		
	}
	
	@Override
	public int findDBFestivalCount() throws Exception {
		int count = adminFestivalMapper.findDBFestivalCount();
		
		System.out.println("impl count : " + count);
		return count;
	}
	
	@Override
	public void deleteDBFestivalList(ArrayList<Integer> checks) throws Exception {
		
		
		for (int i=0; i<checks.size(); i++) {
			
			int festivalId = checks.get(i);
			
			System.out.println(festivalId);
			
			adminFestivalMapper.deleteDBFestivalList(festivalId);
			
		}
		
		
	}
	
	@Override
	public FestivalDTO findDBFestivalByFestivalId(int festivalId) throws Exception {
		
		FestivalDTO festivalDTO = adminFestivalMapper.findDBFestivalByFestivalId(festivalId);
		
		return festivalDTO;
	}
	
	@Override
	public void updateDBFestivalByFestival(FestivalDTO festivalDTO) throws Exception {
		
		adminFestivalMapper.updateDBFestivalByFestival(festivalDTO);
		
	}
	

	
	
	
	
	
	/*public static void main(String[] args) throws IOException {
		
		
		Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=" + "눈축제" + "+기본정보").get();
		
		String title = document.title();
		System.out.println("title : " + title);
		
		
		String imgUrl = document.select("div.detail_info > a > img").attr("src");
		
		System.out.println("?? : " + imgUrl);
		
		System.out.println(imgUrl==null);
		System.out.println(imgUrl.equals("")); //true
		
		
	}*/
	
	
}
