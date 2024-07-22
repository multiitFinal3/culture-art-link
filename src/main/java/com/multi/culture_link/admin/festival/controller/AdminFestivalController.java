package com.multi.culture_link.admin.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.service.RegionService;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.common.time.service.TimeService;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


// json 파싱 예시
	/*public static void main(String[] args) throws IOException {
		
		Request request = new Request.Builder()
				.url("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6/JmV1TOw/Yu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA==&pageNo=1&numOfRows=10&type=json")
				.get()
				.build();
		
		
		OkHttpClient client = new OkHttpClient();
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		JsonObject response1 = json.getAsJsonObject("response");
		JsonObject body1 = response1.getAsJsonObject("body");
		JsonArray items = body1.getAsJsonArray("items");
		*//*System.out.println("items : " + items);*//*
		
		ArrayList<FestivalDTO> list = new ArrayList<>();
		
		for (int i = 0; i < items.size(); i++) {
			
			JsonObject item = items.get(i).getAsJsonObject();
			
			*//*System.out.println((i+1) + " : " + item);
			System.out.println(item.get("fstvlNm").getAsString());*//*
			
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
			
			int regionId = 0;
			
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
			
			
			int diffDays = (int) ((endDate.getTime() - startDate.getTime())/(1000*24*60*60));
			
			System.out.println(startDate);
			System.out.println(endDate);
			System.out.println("일 수 차이 : " + diffDays);
			
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(startDate);
			int startMonth = calendar1.get(Calendar.MONTH) + 1;
			System.out.println("시작월 : " + startMonth);
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(endDate);
			int endMonth = calendar2.get(Calendar.MONTH) + 1;
			System.out.println(endMonth);
			
			
			
			String season= "";
			
			if ((startMonth==12) || (startMonth==1) || (startMonth==2)){
				
				season="겨울";
			
			}else if((startMonth==3) || (startMonth==4) || (startMonth==5)){
				
				season="봄";
				
			}else if((startMonth==6) || (startMonth==7) || (startMonth==8)){
				
				season="여름";
				
			}else{
				
				season="가을";
				
			}
			
			System.out.println("계절 : " + season);
			festivalDTO.setSeason(season);
			
			ArrayList<Integer> days = new ArrayList<Integer>();
			
			for (int j = 0; j < diffDays+1; j++) {
				
				System.out.println("축제 기간 : " + (diffDays + 1));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startDate);
				calendar.add(Calendar.DATE, j);
				
				System.out.println("calendar.get(Calendar.DAY_OF_WEEK) : " + calendar.get(Calendar.DAY_OF_WEEK));
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
			
			
			Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query="+ festivalName + "+기본정보").get();
			
			String title = document.title();
			System.out.println("title : " + title);
			
			
			
			
			String imgUrl = document.select("div.detail_info > a > img").attr("src");
			festivalDTO.setImgUrl(imgUrl);
			
			
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
			festivalDTO.setLatitude(item.get("latitude").getAsDouble());
			festivalDTO.setLongtitude(item.get("longitude").getAsDouble());
			
			festivalDTO.setAvgRate(0);
			
			System.out.println("festivalDTO : " + festivalDTO.toString());
			
		}
		
		
	}*/


// jsoup 로 네이버 축제 기본정보 파싱 예제
	/*public static void main(String[] args) throws IOException {
		
		
		Document document = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%95%88%EC%82%B0%EA%B5%AD%EC%A0%9C%EA%B1%B0%EB%A6%AC%EA%B7%B9%EC%B6%95%EC%A0%9C+%EA%B8%B0%EB%B3%B8%EC%A0%95%EB%B3%B4&oquery=%EC%95%88%EC%82%B0%EA%B5%AD%EC%A0%9C%EA%B1%B0%EB%A6%AC%EA%B7%B9%EC%B6%95%EC%A0%9C+%EA%B8%B0%EB%B3%B8%EC%A0%95%EB%B3%B4&tqi=ipQIGdqo15wssg7RjWNssssssK0-415284").get();
		
		String title = document.title();
		
		System.out.println("title : " + title);
		
		Element element = document.getElementsByTag("img").get(0);
		
		System.out.println("src : " + element.attr("src"));
		
		String target = document.select("div.intro_box > p.text").text();
		
		System.out.println(target);

		//title : 안산국제거리극축제 기본정보 : 네이버 검색
		//src : https://search.pstatic.net/common?type=n&size=174x250&quality=85&direct=true&src=https%3A%2F%2Fcsearch-phinf.pstatic.net%2F20240215_25%2F1707961154908D68E9_JPEG%2F110_25962793_manual_image_url_1707961154877.jpg
		//	안산국제거리극축제는 안산의 도시적 특성을 살리고 지역에 활력을 불어넣기 위해 지난 2005년부터 20년간 시민과 함께 지속해온 거리예술축제다. 매년 5월 어린이날 전후로 안산문화광장 일대를 공연, 거리미술, 놀이, 워크숍 등으로 채우며 시민에게 예술적 감동과 일상의 특별한 경험을 선사했다. 올해도 작년에 이어 ‘광장’, ‘도시’, ‘숲’, ‘횡단’ 4가지 키워드를 바탕으로 축제를 구성한다. 나아가 다양한 관객의 취향과 관심사를 반영하여 모두에게 열린 축제를 만든다.
		
	
	}*/


// xml >> json 바로 예시
	/*public static void main(String[] args) {
		
		
		UriComponents comp = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("apis.data.go.kr")
				.path("B551011/KorService1/areaCode1")
				.queryParam("MobileOS","WIN")
				.queryParam("MobileApp","service")
				.queryParam("serviceKey","chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6%2FJmV1TOw%2FYu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA%3D%3D")
				.build(true);
		
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(StandardCharsets.UTF_8));
		
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(comp.toUri(), String.class);
		
		
		String xmlResponse = responseEntity.getBody();
		
		// XML을 JSON으로 변환
		JSONObject jsonObject = XML.toJSONObject(xmlResponse.toString());
		
		// JSON 객체 출력
		System.out.println(jsonObject.toString(4)); // 들여쓰기 포함 출력
	}*/


// XML >> string >> json 예시
	/*public static void main(String[] args) {
		
		final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
				"<Emp id=\"1\"><name>Pankaj</name><age>25</age>\n"+
				"<role>Developer</role><gen>Male</gen></Emp>";
		
		System.out.println(xmlStr);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			System.out.println();
			System.out.println(output.getClass().getName());
			System.out.println("스트링 : " + output);
			
			JSONObject xmlJSONObj = XML.toJSONObject(output);
			String jsonPrettyPrintString = xmlJSONObj.toString(4);
			System.out.println("json : " + jsonPrettyPrintString);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}*/


@Controller
@RequestMapping("/admin/festival-regulate")
public class AdminFestivalController {
	
	private final AdminFestivalService adminFestivalService;
	private final RegionService regionService;
	private final TimeService timeService;
	private ArrayList<FestivalDTO> list2;
	
	public AdminFestivalController(AdminFestivalService adminFestivalService, RegionService regionService, TimeService timeService, ArrayList<FestivalDTO> list2) {
		this.adminFestivalService = adminFestivalService;
		this.regionService = regionService;
		this.timeService = timeService;
		this.list2 = list2;
	}
	
	@GetMapping
	public String festivalManage() {
		
		
		return "/admin/festival/festivalRegulate";
		
	}
	
	
	@PostMapping("/findAPIFestivalList")
	@ResponseBody
	public ArrayList<FestivalDTO> findAPIFestivalList(@RequestParam("page") int page) {
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findAPIFestivalList(page);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for (FestivalDTO festivalDTO : list) {
				
				
				festivalDTO.setFormattedEnd(dateFormat.format(festivalDTO.getEndDate()));
				festivalDTO.setFormattedStart(dateFormat.format(festivalDTO.getStartDate()));
				
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		System.out.println("list : " + list);
		
		return list;
		
		
	}
	
	
	@PostMapping("/insertAPIFestivalList")
	@ResponseBody
	public String insertAPIFestivalList(@RequestBody ArrayList<Integer> checks) {
		
		System.out.println("checks : " + checks);
		
		ArrayList<Integer> list = new ArrayList();
		
		for (int i : checks) {
			
			list.add(i);
		}
		
		
		try {
			adminFestivalService.insertAPIFestivalList(list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "삽입 성공";
	}
	
	
	@PostMapping("/findDBFestivalList")
	@ResponseBody
	public ArrayList<FestivalDTO> findDBFestivalList(@RequestParam("page") int page) {
		
		System.out.println("page : " + page);
		
		PageDTO pageDTO = new PageDTO();
		pageDTO.setStartEnd(page);
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findDBFestivalList(pageDTO);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for (FestivalDTO festivalDTO : list) {
				
				festivalDTO.setFormattedEnd(dateFormat.format(festivalDTO.getEndDate()));
				festivalDTO.setFormattedStart(dateFormat.format(festivalDTO.getStartDate()));
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("list : " + list);
		
		return list;
		
		
	}
	
	
	@PostMapping("/findDBFestivalCount")
	@ResponseBody
	public int findDBFestivalCount() {
		
		int count = 0;
		try {
			count = adminFestivalService.findDBFestivalCount();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		System.out.println("count : " + count);
		
		return count;
		
		
	}
	
	
	@PostMapping("/deleteDBFestivalList")
	@ResponseBody
	public String deleteDBFestivalList(@RequestBody ArrayList<Integer> checks) {
		
		System.out.println("checks : " + checks);
		
		
		try {
			adminFestivalService.deleteDBFestivalList(checks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "삭제 성공";
	}
	
	@PostMapping("/findDBFestivalByFestivalId")
	@ResponseBody
	public FestivalDTO findDBFestivalByFestivalId(@RequestBody Map<String, Integer> request) {
		
		int festivalId = request.get("festivalId");
		
		System.out.println("받은 페스티벌 아이디 : " + festivalId);
		
		FestivalDTO festivalDTO = null;
		try {
			festivalDTO = adminFestivalService.findDBFestivalByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("start : " + festivalDTO.getStartDate());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		festivalDTO.setFormattedEnd(dateFormat.format(festivalDTO.getEndDate()));
		festivalDTO.setFormattedStart(dateFormat.format(festivalDTO.getStartDate()));
		
		
		return festivalDTO;
		
		
	}
	
	
	@PostMapping("/updateDBFestivalByFestival")
	public String updateDBFestivalByFestival(FestivalDTO festivalDTO, RedirectAttributes redirectAttributes) {
		
		System.out.println("form : " + festivalDTO);
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		
		Date start = null;
		Date end = null;
		try {
			start = dateFormat.parse(festivalDTO.getFormattedStart());
			festivalDTO.setStartDate(start);
			
			
			end = dateFormat.parse(festivalDTO.getFormattedEnd());
			festivalDTO.setEndDate(end);
			
			adminFestivalService.updateDBFestivalByFestival(festivalDTO);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return "redirect:/admin/festival-regulate";
		
		
	}
	
	
	@PostMapping("/findAllRegionAndTime")
	@ResponseBody
	public Map<String, ArrayList> findAllRegionAndTime() {
		
		ArrayList<RegionDTO> list1 = null;
		ArrayList<TimeDTO> list2 = null;
		
		try {
			list1 = regionService.findAllRegion();
			list2 = timeService.findAllTime();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Map<String, ArrayList> map = new HashMap<String, ArrayList>();
		
		map.put("regionList", list1);
		map.put("timeList", list2);
		
		
		return map;
		
		
	}
	
	
	@PostMapping("/findDBFestivalByMultiple")
	@ResponseBody
	public ArrayList<FestivalDTO> findDBFestivalByMultiple(
			@RequestBody ArrayList<HashMap<String, String>> mapList,
			@RequestParam("page") int page) {
		
		
		System.out.println("findDBFestivalByMultiple : " + mapList.toString());
		
		
		FestivalDTO festivalDTO = new FestivalDTO();
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		pageDTO.setStartEnd(pageDTO.getPage());
		festivalDTO.setPageDTO(pageDTO);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		
		for (int i = 0; i < mapList.size(); i++) {
			
			if (!mapList.get(i).get("value").isEmpty()) {
				
				switch (i) {
					
					case 0:
						festivalDTO.setRegionId(Integer.parseInt(mapList.get(i).get("value")));
						break;
					
					case 1:
						festivalDTO.setTimeId(mapList.get(i).get("value"));
						break;
					
					case 2:
						festivalDTO.setFestivalName(mapList.get(i).get("value").trim());
						break;
					
					case 3:
						festivalDTO.setFestivalContent(mapList.get(i).get("value").trim());
						break;
					
					case 4:
						festivalDTO.setManageInstitution(mapList.get(i).get("value").trim());
						break;
					
					case 5:
						festivalDTO.setHostInstitution(mapList.get(i).get("value").trim());
						break;
					
					
					case 6:
						festivalDTO.setSponserInstitution(mapList.get(i).get("value").trim());
						break;
					
					
					case 7:
						festivalDTO.setTel(mapList.get(i).get("value").replace("-", "").trim());
						break;
					
					
					case 8:
						festivalDTO.setPlace(mapList.get(i).get("value").replace("-", "").trim());
						break;
					
					
					case 9:
						System.out.println("start-c: " + mapList.get(i).get("value").trim());
						Date date = null;
						try {
							date = simpleDateFormat.parse(mapList.get(i).get("value").trim());
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setStartDate(date);
						break;
					
					
					case 10:
						Date date2 = null;
						try {
							date2 = simpleDateFormat.parse(mapList.get(i).get("value").trim());
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setEndDate(date2);
						break;
					
					case 11:
						festivalDTO.setAvgRate(Double.valueOf(mapList.get(i).get("value").trim()));
						break;
					
					
					case 12:
						festivalDTO.setSeason(mapList.get(i).get("value").trim());
						break;
					
				}
				
				
			}
			
		}
		
		
		System.out.println("findDBFestivalByMultiple 정보가 담긴 축제 : " + festivalDTO.toString());
		
		//		정보가 담긴 축제 : FestivalDTO(festivalId=0, regionId=11, regionName=null, timeId=평일, festivalName=null, festivalContent=null, manageInstitution=null, hostInstitution=null, sponserInstitution=null, tel=null, homepageUrl=null, detailAddress=null, latitude=0.0, longtitude=0.0, place=null, startDate=null, endDate=null, formattedStart=null, formattedEnd=null, avgRate=0.0, season=봄, imgUrl=null, exist=null)
		
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findDBFestivalByMultiple(festivalDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("받아온 리스트 : " + list);
		for (FestivalDTO festivalDTO1 : list) {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			festivalDTO1.setFormattedStart(dateFormat.format(festivalDTO1.getStartDate()));
			festivalDTO1.setFormattedEnd(dateFormat.format(festivalDTO1.getEndDate()));
			
			
			System.out.println(festivalDTO1.toString());
			
		}
		
		return list;
	}
	
	
	@PostMapping("/findDBFestivalMultipleCount")
	@ResponseBody
	public int findDBFestivalMultipleCount(@RequestBody ArrayList<HashMap<String, String>> mapList) {
		
		
		System.out.println("findDBFestivalMultipleCount : " + mapList.toString());
		
		FestivalDTO festivalDTO = new FestivalDTO();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		
		for (int i = 0; i < mapList.size(); i++) {
			
			if (!mapList.get(i).get("value").isEmpty()) {
				
				switch (i) {
					
					case 0:
						festivalDTO.setRegionId(Integer.parseInt(mapList.get(i).get("value")));
						break;
					
					case 1:
						festivalDTO.setTimeId(mapList.get(i).get("value"));
						break;
					
					case 2:
						festivalDTO.setFestivalName(mapList.get(i).get("value"));
						break;
					
					case 3:
						festivalDTO.setFestivalContent(mapList.get(i).get("value"));
						break;
					
					case 4:
						festivalDTO.setManageInstitution(mapList.get(i).get("value"));
						break;
					
					case 5:
						festivalDTO.setHostInstitution(mapList.get(i).get("value"));
						break;
					
					
					case 6:
						festivalDTO.setSponserInstitution(mapList.get(i).get("value"));
						break;
					
					
					case 7:
						festivalDTO.setTel(mapList.get(i).get("value"));
						break;
					
					
					case 8:
						festivalDTO.setPlace(mapList.get(i).get("value"));
						break;
					
					
					case 9:
						System.out.println("start : " + mapList.get(i).get("value").trim());
						System.out.println();
						Date date = null;
						try {
							date = simpleDateFormat.parse(mapList.get(i).get("value").trim());
							System.out.println("c: start pared date : " + date);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						
						System.out.println(date);
						festivalDTO.setStartDate(date);
						break;
					
					
					case 10:
						System.out.println();
						Date date2 = null;
						try {
							System.out.println("c : date2");
							date2 = simpleDateFormat.parse(mapList.get(i).get("value").trim());
							System.out.println("parsed : " + date2);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setEndDate(date2);
						break;
					
					case 11:
						festivalDTO.setAvgRate(Double.valueOf(mapList.get(i).get("value")));
						break;
					
					
					case 12:
						festivalDTO.setSeason(mapList.get(i).get("value"));
						break;
					
				}
				
				
			}
			
		}
		
		
		System.out.println("findDBFestivalMultipleCount 정보가 담긴 축제 : " + festivalDTO.toString());
		
		
		int count = 0;
		try {
			count = adminFestivalService.findDBFestivalMultipleCount(festivalDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		System.out.println("MULTIPLE count : " + count);
		
		return count;
		
		
	}
	
	
	// api 상세조건 검색 내용 받기
	@PostMapping("/findAPIFestivalByMultiple")
	@ResponseBody
	public ArrayList<FestivalDTO> findAPIFestivalByMultiple(
			@RequestBody ArrayList<HashMap<String, String>> mapList,
			@RequestParam("page") int page) {
		
		
		System.out.println("findDBFestivalByMultiple : " + mapList.toString());
		
		
		FestivalDTO festivalDTO = new FestivalDTO();
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		pageDTO.setStartEnd(pageDTO.getPage());
		festivalDTO.setPageDTO(pageDTO);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder stringBuilder = new StringBuilder();
		
		
		// 받아온 정보를 깔끔하게 바꾸기
		for (int i = 0; i < mapList.size(); i++) {
			
			String value = mapList.get(i).get("value");
			
			if (!value.isEmpty()) {
				
				switch (i) {
					
					case 0:
						festivalDTO.setFestivalName(value);
						try {
							stringBuilder.append("&fstvlNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					case 1:
						festivalDTO.setManageInstitution(value);
						try {
							stringBuilder.append("&mnnstNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					case 2:
						festivalDTO.setHostInstitution(value);
						try {
							stringBuilder.append("&auspcInsttNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 3:
						festivalDTO.setSponserInstitution(value);
						try {
							stringBuilder.append("&suprtInsttNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 4:
						
						String tel = value.replace("-", "").trim();
						
						tel = tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6);
						
						festivalDTO.setSponserInstitution(value);
						try {
							stringBuilder.append("&phoneNumber=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 5:
						
						try {
							stringBuilder.append("&opar=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 6:
						String date1 = value;
						Date date = null;
						try {
							date = simpleDateFormat.parse(date1);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setStartDate(date);
						festivalDTO.setFormattedStart(date1);
						
						try {
							stringBuilder.append("&fstvlStartDate=").append(URLEncoder.encode(date1, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						
						break;
					
					
					case 7:
						String date2 = value;
						Date dateEnd = null;
						try {
							dateEnd = simpleDateFormat.parse(date2);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setEndDate(dateEnd);
						festivalDTO.setFormattedEnd(date2);
						
						try {
							stringBuilder.append("&fstvlEndDate=").append(URLEncoder.encode(date2, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						
						break;
					
					
				}
				
				
			}
			
		}
		
		
		System.out.println("findAPIFestivalByMultiple 정보가 담긴 축제 : " + festivalDTO.toString());
		
		String urls = stringBuilder.toString();
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findAPIFestivalByMultiple(festivalDTO, urls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("받아온 리스트 : " + list);
		for (FestivalDTO festivalDTO1 : list) {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			festivalDTO1.setFormattedStart(dateFormat.format(festivalDTO1.getStartDate()));
			festivalDTO1.setFormattedEnd(dateFormat.format(festivalDTO1.getEndDate()));
			
			
			System.out.println(festivalDTO1.toString());
			
		}
		
		return list;
	}
	
	
	@PostMapping("/findAPIFestivalMultipleCount")
	@ResponseBody
	public int findAPIFestivalMultipleCount(@RequestBody ArrayList<HashMap<String, String>> mapList) {
		
		
		System.out.println("findDBFestivalByMultiple : " + mapList.toString());
		
		
		FestivalDTO festivalDTO = new FestivalDTO();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder stringBuilder = new StringBuilder();
		
		
		// 받아온 정보를 깔끔하게 바꾸기
		for (int i = 0; i < mapList.size(); i++) {
			
			String value = mapList.get(i).get("value");
			
			if (!value.isEmpty()) {
				
				switch (i) {
					
					case 0:
						festivalDTO.setFestivalName(value);
						try {
							stringBuilder.append("&fstvlNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					case 1:
						festivalDTO.setManageInstitution(value);
						try {
							stringBuilder.append("&mnnstNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					case 2:
						festivalDTO.setHostInstitution(value);
						try {
							stringBuilder.append("&auspcInsttNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 3:
						festivalDTO.setSponserInstitution(value);
						try {
							stringBuilder.append("&suprtInsttNm=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 4:
						
						String tel = value.replace("-", "").trim();
						
						tel = tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6);
						
						festivalDTO.setSponserInstitution(value);
						try {
							stringBuilder.append("&phoneNumber=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 5:
						
						try {
							stringBuilder.append("&opar=").append(URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						break;
					
					
					case 6:
						String date1 = value;
						Date date = null;
						try {
							date = simpleDateFormat.parse(date1);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setStartDate(date);
						festivalDTO.setFormattedStart(date1);
						
						try {
							stringBuilder.append("&fstvlStartDate=").append(URLEncoder.encode(date1, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						
						break;
					
					
					case 7:
						String date2 = value;
						Date dateEnd = null;
						try {
							dateEnd = simpleDateFormat.parse(date2);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
						festivalDTO.setEndDate(dateEnd);
						festivalDTO.setFormattedEnd(date2);
						
						try {
							stringBuilder.append("&fstvlEndDate=").append(URLEncoder.encode(date2, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						
						break;
					
					
				}
				
				
			}
		}
		
		
		System.out.println("findAPIFestivalByMultiple 정보가 담긴 축제 : " + festivalDTO.toString());
		
		String urls = stringBuilder.toString();
		
		int count = 0;
		try {
			count = adminFestivalService.findAPIFestivalByMultipleCount(festivalDTO, urls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("받아온 카운트 : " + count);
		
		
		return count;
		
		
	}
	
	
	@PostMapping("/insertContentKeywordByFestivalId")
	@ResponseBody
	public ArrayList<String> insertContentKeywordByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		
		ArrayList<String> list = null;
		
		try {
			list = adminFestivalService.insertContentKeywordByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return list;
		
		
	}
	
	
}