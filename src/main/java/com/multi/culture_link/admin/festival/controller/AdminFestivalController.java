package com.multi.culture_link.admin.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.service.RegionService;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.common.time.service.TimeService;
import com.multi.culture_link.festival.model.dto.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

/**
 * 관리자 축제 관련 컨트롤러 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Controller
@RequestMapping("/admin/festival-regulate")
public class AdminFestivalController {
	
	private final AdminFestivalService adminFestivalService;
	private final RegionService regionService;
	private final TimeService timeService;
	private ArrayList<FestivalDTO> list2;
	private MongoTemplate mongoTemplate;
	
	/**
	 * 생성자 주입
	 *
	 * @param adminFestivalService 축제 관리자 서비스
	 * @param regionService        지역 서비스
	 * @param timeService          시간 서비스
	 * @param list2                현재 서비스단에 저장된 축제 리스트
	 */
	public AdminFestivalController(AdminFestivalService adminFestivalService, RegionService regionService, TimeService timeService, ArrayList<FestivalDTO> list2, MongoTemplate mongoTemplate) {
		this.adminFestivalService = adminFestivalService;
		this.regionService = regionService;
		this.timeService = timeService;
		this.list2 = list2;
		this.mongoTemplate = mongoTemplate;
	}
	
	/**
	 * 축제 관리자 화면으로 전환
	 *
	 * @return 축제 관리자 화면
	 */
	@GetMapping
	public String festivalManage() {
		
		return "/admin/festival/festivalRegulate";
		
	}
	
	
	/**
	 * 요구되는 페이지의 축제 api 정보를 반환
	 *
	 * @param page 실시간 축제 정보의 요구 페이지 번호
	 * @return 페스티벌 dto 리스트
	 */
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
		
		return list;
		
		
	}
	
	/**
	 * api 리스트에서 체크된 부분을 DB에 저장
	 *
	 * @param checks 체크된 숫자 번호로 서비스단에 저장된 리스트의 순서 번호와 일치
	 * @return 삽입성공 알림 스트링 반환
	 */
	@PostMapping("/insertAPIFestivalList")
	@ResponseBody
	public String insertAPIFestivalList(@RequestBody ArrayList<Integer> checks) {
		
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
	
	/**
	 * DB에서 해당하는 부분의 행들을 반환
	 *
	 * @param page 페이지 번호로 1페이지에 5개씩 반환
	 * @return 해당하는 페스티벌 DTO 리스트
	 */
	@PostMapping("/findDBFestivalList")
	@ResponseBody
	public ArrayList<FestivalDTO> findDBFestivalList(@RequestParam("page") int page) {
		
		PageDTO pageDTO = new PageDTO();
		pageDTO.setStartEnd(page);
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findDBFestivalList(pageDTO);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for (FestivalDTO festivalDTO : list) {
				
				double avgRate = Math.round(festivalDTO.getAvgRate() * 100) / 100.0;
				festivalDTO.setAvgRate(avgRate);
				festivalDTO.setFormattedEnd(dateFormat.format(festivalDTO.getEndDate()));
				festivalDTO.setFormattedStart(dateFormat.format(festivalDTO.getStartDate()));
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
		
	}
	
	/**
	 * 전체 축제 DB 갯수 반환
	 *
	 * @return 갯수를 반환
	 */
	@PostMapping("/findDBFestivalCount")
	@ResponseBody
	public int findDBFestivalCount() {
		
		int count = 0;
		try {
			count = adminFestivalService.findDBFestivalCount();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return count;
		
		
	}
	
	/**
	 * 체크된 목록을 DB에서 삭제
	 *
	 * @param checks 체크된 DB 번호 리스트
	 * @return 삭제 성공 스트링 반환
	 */
	@PostMapping("/deleteDBFestivalList")
	@ResponseBody
	public String deleteDBFestivalList(@RequestBody ArrayList<Integer> checks) {
		
		try {
			adminFestivalService.deleteDBFestivalList(checks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "삭제 성공";
	}
	
	/**
	 * @param request 페스티벌 DB 번호가 담긴 맵
	 * @return 해당 페스티벌 DTO 반환
	 */
	@PostMapping("/findDBFestivalByFestivalId")
	@ResponseBody
	public FestivalDTO findDBFestivalByFestivalId(@RequestBody Map<String, Integer> request) {
		
		int festivalId = request.get("festivalId");
		
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
		double avgRate = Math.round(festivalDTO.getAvgRate() * 100) / 100.0;
		festivalDTO.setAvgRate(avgRate);
		
		
		return festivalDTO;
		
		
	}
	
	/**
	 * 해당 페스티벌의 내용을 수정
	 *
	 * @param festivalDTO 해당 번호와 수정된 내용을 담고있는 페스티벌 DTO
	 * @return 리다이렉트 주소
	 */
	@PostMapping("/updateDBFestivalByFestival")
	public String updateDBFestivalByFestival(FestivalDTO festivalDTO) {
		
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
	
	/**
	 * DB에 있는 지역, 시간을 전부 반환
	 *
	 * @return 맵 형태로 지역, 시간 리스트를 반환
	 */
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
	
	/**
	 * DB 데이터를 다중 조건을 가공 및 적용해 반환함
	 *
	 * @param mapList 다중 조건 리스트
	 * @param page    요구되는 페이지 번호
	 * @return 페스티벌 DTO 리스트
	 */
	@PostMapping("/findDBFestivalByMultiple")
	@ResponseBody
	public ArrayList<FestivalDTO> findDBFestivalByMultiple(
			@RequestBody ArrayList<HashMap<String, String>> mapList,
			@RequestParam("page") int page) {
		
		
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
			
			double avgRate = Math.round(festivalDTO1.getAvgRate() * 100) / 100.0;
			festivalDTO1.setAvgRate(avgRate);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			festivalDTO1.setFormattedStart(dateFormat.format(festivalDTO1.getStartDate()));
			festivalDTO1.setFormattedEnd(dateFormat.format(festivalDTO1.getEndDate()));
			
			
			System.out.println(festivalDTO1.toString());
			
		}
		
		return list;
	}
	
	/**
	 * DB 데이터에 다중 조건을 적용한 총 숫자
	 *
	 * @param mapList 다중 조건 리스트
	 * @return 해당 갯수
	 */
	@PostMapping("/findDBFestivalMultipleCount")
	@ResponseBody
	public int findDBFestivalMultipleCount(@RequestBody ArrayList<HashMap<String, String>> mapList) {
		
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
	
	
	/**
	 * API 데이터에서 다중조건을 적용한 결과
	 *
	 * @param mapList 다중 조건
	 * @param page    요구되는 페이지 숫자
	 * @return 페스티벌 DTO 리스트
	 */
	@PostMapping("/findAPIFestivalByMultiple")
	@ResponseBody
	public ArrayList<FestivalDTO> findAPIFestivalByMultiple(
			@RequestBody ArrayList<HashMap<String, String>> mapList,
			@RequestParam("page") int page) {
		
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
		
		String urls = stringBuilder.toString();
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findAPIFestivalByMultiple(festivalDTO, urls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		for (FestivalDTO festivalDTO1 : list) {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			festivalDTO1.setFormattedStart(dateFormat.format(festivalDTO1.getStartDate()));
			festivalDTO1.setFormattedEnd(dateFormat.format(festivalDTO1.getEndDate()));
			
		}
		
		return list;
	}
	
	/**
	 * API 데이터에서 다중조건을 적용한 결과 갯수
	 *
	 * @param mapList 다중 조건
	 * @return 갯수
	 */
	@PostMapping("/findAPIFestivalMultipleCount")
	@ResponseBody
	public int findAPIFestivalMultipleCount(@RequestBody ArrayList<HashMap<String, String>> mapList) {
		
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
	
	/**
	 * 축제 내용 키워드를 삽입
	 *
	 * @param festivalId
	 * @return 키워드 스트링
	 */
	@PostMapping("/insertContentKeywordByFestivalId")
	@ResponseBody
	public ArrayList<String> insertContentKeywordByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		try {
			map = adminFestivalService.findContentKeywordByFestivalId(festivalId);
			
			System.out.println("admin contr findContentKeywordByFestivalId : " + map);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		ArrayList<FestivalKeywordDTO> keywordList = new ArrayList<FestivalKeywordDTO>();
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			
			FestivalKeywordDTO festivalKeywordDTO = new FestivalKeywordDTO();
			festivalKeywordDTO.setFestivalKeywordId(entry.getKey());
			festivalKeywordDTO.setFreq(entry.getValue());
			
			DictionaryDTO dictionaryDTO = new DictionaryDTO();
			
			Query query = new Query(new Criteria("word").is(entry.getKey()));
			dictionaryDTO = mongoTemplate.findOne(query, DictionaryDTO.class, "dictionary");
			if (dictionaryDTO != null) {
				festivalKeywordDTO.setEmotionStat(dictionaryDTO.getPolarity());
			} else {
				festivalKeywordDTO.setEmotionStat(0);
			}
			
			keywordList.add(festivalKeywordDTO);
			
		}
		
		
		System.out.println("keywordList : " + keywordList);
		
		for (FestivalKeywordDTO keyword : keywordList) {
			
			FestivalContentReviewNaverKeywordMapDTO keywordMapping1 = new FestivalContentReviewNaverKeywordMapDTO();
			
			try {
				FestivalKeywordDTO festivalKeywordDTO = adminFestivalService.findKeywordByKeyword(keyword);
				if (festivalKeywordDTO == null) {
					
					adminFestivalService.insertKeywordByKeyword(keyword);
					
					
				} else {
					
					System.out.println("키워드 테이블에 존재");
					
				}
				
				keywordMapping1.setFestivalId(festivalId);
				keywordMapping1.setFestivalKeywordId(keyword.getFestivalKeywordId());
				keywordMapping1.setSortCode("C");
				keywordMapping1.setFreq(keyword.getFreq());
				
				System.out.println("keywordMapping1 : " + keywordMapping1);
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping = adminFestivalService.findKeywordMappingByKeywordMapping(keywordMapping1);
				
				if (keywordMapping == null) {
					adminFestivalService.insertKeywordMappingByKeywordMapping(keywordMapping1);
					
				} else {
					System.out.println("키워드 컨텐트 매핑 내역 존재");
				}
				
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		}
		
		
		ArrayList<String> keys = new ArrayList<String>();
		
		for (String key : map.keySet()) {
			
			keys.add(key);
			
		}
		
		System.out.println("admin controller keys : " + keys);
		
		return keys;
	}
	
	/**
	 * 축제 네이버 기사 키워드를 삽입
	 *
	 * @param festivalId
	 * @return 키워드 스트링
	 */
	@PostMapping("/insertNaverArticleKeywordByFestivalId")
	@ResponseBody
	public ArrayList<String> insertNaverArticleKeywordByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		
		ArrayList<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		
		try {
			list = adminFestivalService.findNaverArticleKeywordByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("insertNaverArticleKeywordByFestivalId list : " + list);
		
		ArrayList<FestivalKeywordDTO> keywordList = new ArrayList<FestivalKeywordDTO>();
		ArrayList<String> keys = new ArrayList<String>();
		
		
		for (HashMap<String, Integer> map : list) {
			
			if (map != null) {
				
				for (Map.Entry<String, Integer> entry : map.entrySet()) {
					
					FestivalKeywordDTO festivalKeywordDTO = new FestivalKeywordDTO();
					festivalKeywordDTO.setFestivalKeywordId(entry.getKey());
					keys.add(entry.getKey());
					festivalKeywordDTO.setFreq(entry.getValue());
					
					DictionaryDTO dictionaryDTO = new DictionaryDTO();
					
					Query query = new Query(new Criteria("word").is(entry.getKey()));
					dictionaryDTO = mongoTemplate.findOne(query, DictionaryDTO.class, "dictionary");
					if (dictionaryDTO != null) {
						festivalKeywordDTO.setEmotionStat(dictionaryDTO.getPolarity());
					} else {
						festivalKeywordDTO.setEmotionStat(0);
					}
					
					keywordList.add(festivalKeywordDTO);
					
				}
				
				
			}
			
			
		}
		
		System.out.println("keywordList : " + keywordList);
		
		for (FestivalKeywordDTO keyword : keywordList) {
			
			try {
				FestivalKeywordDTO festivalKeywordDTO = adminFestivalService.findKeywordByKeyword(keyword);
				if (festivalKeywordDTO == null) {
					adminFestivalService.insertKeywordByKeyword(keyword);
				} else {
					
					System.out.println("키워드 테이블에 존재");
					
				}
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping1 = new FestivalContentReviewNaverKeywordMapDTO();
				keywordMapping1.setFestivalId(festivalId);
				keywordMapping1.setFestivalKeywordId(keyword.getFestivalKeywordId());
				keywordMapping1.setSortCode("NA");
				keywordMapping1.setFreq(keyword.getFreq());

//				System.out.println("keywordMapping1 : " + keywordMapping1);
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping = adminFestivalService.findKeywordMappingByKeywordMapping(keywordMapping1);
				
				if (keywordMapping == null) {
					adminFestivalService.insertKeywordMappingByKeywordMapping(keywordMapping1);
					
				} else {
					System.out.println("키워드 네이버 기사 매핑 내역 존재");
					int freq = keywordMapping.getFreq() + keywordMapping1.getFreq();
					keywordMapping1.setFreq(freq);
					adminFestivalService.updateKeywordMappingByKeywordMapping(keywordMapping1);
					
					
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		}
		
		
		return keys;
		
		
	}
	
	/**
	 * 네이버 블로그 키워드를 삽입
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/insertNaverBlogKeywordByFestivalId")
	@ResponseBody
	public ArrayList<String> insertNaverBlogKeywordByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		
		try {
			list = adminFestivalService.findNaverBlogKeywordByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("insertNaverBlogKeywordByFestivalId list : " + list);
		
		ArrayList<FestivalKeywordDTO> keywordList = new ArrayList<FestivalKeywordDTO>();
		ArrayList<String> keys = new ArrayList<String>();
		
		
		for (HashMap<String, Integer> map : list) {
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				
				FestivalKeywordDTO festivalKeywordDTO = new FestivalKeywordDTO();
				festivalKeywordDTO.setFestivalKeywordId(entry.getKey());
				keys.add(entry.getKey());
				festivalKeywordDTO.setFreq(entry.getValue());
				
				DictionaryDTO dictionaryDTO = new DictionaryDTO();
				
				Query query = new Query(new Criteria("word").is(entry.getKey()));
				dictionaryDTO = mongoTemplate.findOne(query, DictionaryDTO.class, "dictionary");
				if (dictionaryDTO != null) {
					festivalKeywordDTO.setEmotionStat(dictionaryDTO.getPolarity());
				} else {
					festivalKeywordDTO.setEmotionStat(0);
				}
				
				keywordList.add(festivalKeywordDTO);
				
			}
			
		}
		
		System.out.println("keywordList : " + keywordList);
		
		for (FestivalKeywordDTO keyword : keywordList) {
			
			try {
				FestivalKeywordDTO festivalKeywordDTO = adminFestivalService.findKeywordByKeyword(keyword);
				if (festivalKeywordDTO == null) {
					adminFestivalService.insertKeywordByKeyword(keyword);
				} else {
					
					System.out.println("키워드 테이블에 존재");
					
				}
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping1 = new FestivalContentReviewNaverKeywordMapDTO();
				keywordMapping1.setFestivalId(festivalId);
				keywordMapping1.setFestivalKeywordId(keyword.getFestivalKeywordId());
				keywordMapping1.setSortCode("NB");
				keywordMapping1.setFreq(keyword.getFreq());

//				System.out.println("keywordMapping1 : " + keywordMapping1);
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping = adminFestivalService.findKeywordMappingByKeywordMapping(keywordMapping1);
				
				if (keywordMapping == null) {
					adminFestivalService.insertKeywordMappingByKeywordMapping(keywordMapping1);
					
				} else {
					System.out.println("키워드 네이버 블로그 매핑 내역 존재");
					int freq = keywordMapping.getFreq() + keywordMapping1.getFreq();
					keywordMapping1.setFreq(freq);
					adminFestivalService.updateKeywordMappingByKeywordMapping(keywordMapping1);
					
					
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		}
		
		
		return keys;
		
	}
	
	
	/**
	 * 사이트 리뷰 키워드를 삽입
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/insertReviewKeywordByFestivalId")
	@ResponseBody
	public ArrayList<String> insertReviewKeywordByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		
		try {
			list = adminFestivalService.findReviewKeywordByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("insertReviewKeywordByFestivalId list : " + list);
		
		ArrayList<FestivalKeywordDTO> keywordList = new ArrayList<FestivalKeywordDTO>();
		ArrayList<String> keys = new ArrayList<String>();
		
		
		for (HashMap<String, Integer> map : list) {
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				
				FestivalKeywordDTO festivalKeywordDTO = new FestivalKeywordDTO();
				festivalKeywordDTO.setFestivalKeywordId(entry.getKey());
				keys.add(entry.getKey());
				festivalKeywordDTO.setFreq(entry.getValue());
				
				DictionaryDTO dictionaryDTO = new DictionaryDTO();
				
				Query query = new Query(new Criteria("word").is(entry.getKey()));
				dictionaryDTO = mongoTemplate.findOne(query, DictionaryDTO.class, "dictionary");
				if (dictionaryDTO != null) {
					festivalKeywordDTO.setEmotionStat(dictionaryDTO.getPolarity());
				} else {
					festivalKeywordDTO.setEmotionStat(0);
				}
				
				keywordList.add(festivalKeywordDTO);
				
			}
			
		}
		
		System.out.println("keywordList : " + keywordList);
		
		for (FestivalKeywordDTO keyword : keywordList) {
			
			try {
				FestivalKeywordDTO festivalKeywordDTO = adminFestivalService.findKeywordByKeyword(keyword);
				if (festivalKeywordDTO == null) {
					adminFestivalService.insertKeywordByKeyword(keyword);
				} else {
					
					System.out.println("키워드 테이블에 존재");
					
				}
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping1 = new FestivalContentReviewNaverKeywordMapDTO();
				keywordMapping1.setFestivalId(festivalId);
				keywordMapping1.setFestivalKeywordId(keyword.getFestivalKeywordId());
				keywordMapping1.setSortCode("R");
				keywordMapping1.setFreq(keyword.getFreq());

//				System.out.println("keywordMapping1 : " + keywordMapping1);
				
				FestivalContentReviewNaverKeywordMapDTO keywordMapping = adminFestivalService.findKeywordMappingByKeywordMapping(keywordMapping1);
				
				if (keywordMapping == null) {
					adminFestivalService.insertKeywordMappingByKeywordMapping(keywordMapping1);
					
				} else {
					System.out.println("키워드 리뷰 매핑 내역 존재");
					int freq = keywordMapping.getFreq() + keywordMapping1.getFreq();
					keywordMapping1.setFreq(freq);
					adminFestivalService.updateKeywordMappingByKeywordMapping(keywordMapping1);
					
					
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		}
		
		
		return keys;
		
	}
	
	
	/**
	 * 전체 축제 리스트를 반환
	 *
	 *
	 * @return
	 */
	@PostMapping("/findDBFestivalAllList")
	@ResponseBody
	public ArrayList<FestivalDTO> findDBFestivalAllList() {
		
		ArrayList<FestivalDTO> list = null;
		try {
			list = adminFestivalService.findDBFestivalAllList();
			System.out.println("all list : " + list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
		
	}
	
	
}