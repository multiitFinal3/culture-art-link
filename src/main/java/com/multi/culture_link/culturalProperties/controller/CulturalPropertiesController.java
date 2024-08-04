package com.multi.culture_link.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.NewsArticle;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.culturalProperties.model.dto.YoutubeConfig;
import com.multi.culture_link.culturalProperties.service.CulturalPropertiesService;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
@RequestMapping("/cultural-properties")
public class CulturalPropertiesController {


	@Autowired
	private CulturalPropertiesService culturalPropertiesService;

	private final YoutubeConfig youtubeConfig;

	@Autowired
	public CulturalPropertiesController(YoutubeConfig youtubeConfig) {
		this.youtubeConfig = youtubeConfig;
	}

	@Autowired
	private HttpSession httpSession; // 세션을 주입받음

	@Value("${API-KEY.naverClientId}")
	private String naverClientId;


	@GetMapping
	public String culturalProperties() {
		return "/culturalProperties/culturalProperties";
	}



	@GetMapping("/getList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listCulturalProperties(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "0") int size) {

		int totalCount = culturalPropertiesService.getTotalCount(); // 전체 문화재 수

		// size가 0 이하인 경우 전체 데이터 수로 설정
		if (size <= 0) {
			size = totalCount; // 전체 데이터 수로 설정
			page = 1; // 기본 페이지 번호는 1
		}

		int offset = (page - 1) * size; // 오프셋 계산
		List<CulturalPropertiesDTO> culturalProperties = culturalPropertiesService.listCulturalProperties(offset, size);
		int totalPages = (int) Math.ceil((double) totalCount / size); // 전체 페이지 수 계산

		Map<String, Object> response = new HashMap<>();
		response.put("culturalProperties", culturalProperties);
		response.put("totalCount", totalCount);
		response.put("totalPages", totalPages);

		return ResponseEntity.ok(response);
	}


	@GetMapping("/getUserId")
	public ResponseEntity<Map<String, Integer>> getUserId() {
		// 현재 로그인된 사용자 정보를 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();
			int userId = vwUserRoleDTO.getUserId(); // 사용자 ID 가져오기

			// 사용자 ID를 Map 형태로 반환
			Map<String, Integer> response = new HashMap<>();
			response.put("userId", userId);

			return ResponseEntity.ok(response); // 사용자 ID를 응답으로 반환
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인하지 않은 경우
	}


	@PostMapping("/addLike")
	public ResponseEntity<String> addLike(@RequestParam int culturalPropertiesId) {
		// 현재 로그인된 사용자 정보를 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();
			int userId = vwUserRoleDTO.getUserId(); // 사용자 ID 가져오기

			CulturalPropertiesInterestDTO interest = new CulturalPropertiesInterestDTO();
			interest.setCulturalPropertiesId(culturalPropertiesId);
			interest.setUserId(userId); // VWUserRoleDTO에서 userId 가져오기
			interest.setInterestType("LIKE");

			culturalPropertiesService.addLike(interest);
			return ResponseEntity.ok("찜이 추가되었습니다.");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	}


	@PostMapping("/addDislike")
	public ResponseEntity<String> addDislike(@RequestParam int culturalPropertiesId) {
		// 로그인된 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();
			int userId = vwUserRoleDTO.getUserId(); // 사용자 ID 가져오기

			CulturalPropertiesInterestDTO interest = new CulturalPropertiesInterestDTO();
			interest.setCulturalPropertiesId(culturalPropertiesId);
			interest.setUserId(userId); // UserDTO에서 userId 가져오기
			interest.setInterestType("DISLIKE"); // 상태 설정

			// 서비스 호출하여 관심없음 추가
			culturalPropertiesService.addDislike(interest);
			return ResponseEntity.ok("관심없음이 추가되었습니다.");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	}




	@PostMapping("/removeInterest")
	public ResponseEntity<String> removeInterest(@RequestParam int culturalPropertiesId) {
		// 현재 로그인된 사용자 정보를 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 인증된 사용자가 VWUserRoleDTO 인스턴스인지 확인
		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();

			// 문화재 관심 정보를 담을 객체 생성
			CulturalPropertiesInterestDTO interest = new CulturalPropertiesInterestDTO();
			interest.setCulturalPropertiesId(culturalPropertiesId);
			interest.setUserId(vwUserRoleDTO.getUserId()); // VWUserRoleDTO에서 userId 가져오기

			try {
				// 찜 또는 관심없음 삭제
				culturalPropertiesService.removeInterest(interest);
				return ResponseEntity.ok("관심이 제거되었습니다.");
			} catch (Exception e) {
				// 오류 처리
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관심 제거 실패: " + e.getMessage());
			}
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	}



	// 찜 정보 요청 처리
	@GetMapping("/getInterest")
	public ResponseEntity<List<CulturalPropertiesInterestDTO>> getInterest(@RequestParam int userId) {
		List<CulturalPropertiesInterestDTO> interests = culturalPropertiesService.getInterest(userId);
		return ResponseEntity.ok(interests);
	}




	@GetMapping("/searchMain")
	public ResponseEntity<?> searchCulturalProperties(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String culturalPropertiesName,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) String dynasty) {

		int pageSize = 6;
		Page<CulturalPropertiesDTO> propertyPage = culturalPropertiesService.searchCulturalProperties(page, pageSize, category, culturalPropertiesName, region, dynasty);

		return ResponseEntity.ok(propertyPage); // 검색 결과를 ResponseEntity로 반환
	}





//	// 문화재 상세 페이지
//	@GetMapping("/detail/{id}")
////	@ResponseBody
//	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);
//		model.addAttribute("property", property);
//		return "/culturalProperties/culturalPropertiesDetail";
//	}

	// 문화재 상세 페이지
	@GetMapping("/detail/{id}")
	@ResponseBody
	//	@ResponseBody
	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);

		System.out.println("디테일 아이디 "+ id);
		model.addAttribute("property", property);
		return "/culturalProperties/culturalPropertiesDetail";
	}





	@GetMapping("/news")
	@ResponseBody
	public ResponseEntity<List<NewsArticle>> getNewsArticles(Model model) {
		String url = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=%EA%B5%AD%EA%B0%80%EC%9C%A0%EC%82%B0";
		List<NewsArticle> articles = culturalPropertiesService.getNewsArticles();

		try {
			Document document = Jsoup.connect(url).get();
			Elements newsElements = document.select("div.news_area");

			for (Element element : newsElements) {
				String title = element.select("a.news_tit").text(); // 제목
				String link = element.select("a.news_tit").attr("href"); // 링크
				String date = element.select("span.info").text(); // 날짜
				String content = element.select("div.news_dsc").text(); // 내용

				// 이미지 추출
				String imgUrl = "";
				Elements imgElements = newsElements.select("img");
				if (imgElements.size() > 0) {
					imgUrl = imgElements.attr("src"); // img 태그의 src 속성에서 가져오기
				}

				// NewsArticle 객체에 데이터 저장
				articles.add(new NewsArticle(title, link, date, content, imgUrl));
			}

			model.addAttribute("newsArticles", articles);
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", "뉴스를 가져오는 데 실패했습니다.");
		}

		return ResponseEntity.ok(articles); // JSON으로 응답
	}


	@PostMapping("/videos")
	@ResponseBody
	public List<Video> getVideos(@RequestParam String query) {
		List<Video> videos = crawlYouTubeVideos(query);
		return videos; // 비디오 리스트를 JSON으로 반환
	}

	private List<Video> crawlYouTubeVideos(String query) {
		List<Video> videos = new ArrayList<>();
		try {
			String apiKey = youtubeConfig.getApiKey(); // API 키 가져오기
			String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q=" + query + "&type=video&key=" + apiKey;
			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.getForObject(url, String.class);

			JSONObject jsonObject = new JSONObject(response);
			JSONArray items = jsonObject.getJSONArray("items");

			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				JSONObject snippet = item.getJSONObject("snippet");

				String title = snippet.getString("title");
				String videoId = item.getJSONObject("id").getString("videoId");
				String link = "https://www.youtube.com/watch?v=" + videoId;
				String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");

				videos.add(new Video(title, link, thumbnailUrl));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videos;
	}


//	// 문화재 상세 페이지
//	@GetMapping("/detail/{id}")
//	@ResponseBody
//	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertiesById(id);
//		model.addAttribute("property", property);
//		return "culturalProperties/culturalPropertiesDetail"; // detail.html로 변경
//	}


//	@GetMapping("/cultural-properties-detail/{id}")
//	public CulturalPropertiesDTO getCulturalPropertyDetail(@PathVariable int id) {
//		return culturalPropertiesService.getCulturalPropertiesById(id);
//	}


//	@GetMapping("/detail/{id}")
////	@ResponseBody
//	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertiesById(id);
//
//		// 데이터 확인을 위한 로그 추가
//		if (property == null) {
//			System.out.println("Property is null for ID: " + id);
//		} else {
//			System.out.println("Property details: " + property);
//		}
//
//		model.addAttribute("property", property);
//		return "/culturalProperties/culturalPropertiesDetail";
//	}


//이걸로 하는중
//	@GetMapping("/detail/{id}")
////	@ResponseBody // JSON 응답을 위해 추가
//	public ResponseEntity<CulturalPropertiesDTO> getCulturalPropertyDetail(@PathVariable int id) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertiesById(id);
//
//		System.out.println("detail id" + id);
//		// 데이터 확인을 위한 로그 추가
//		if (property == null) {
//			return ResponseEntity.notFound().build(); // 404 Not Found
//		}
//
//		return ResponseEntity.ok(property); // 200 OK와 함께 DTO 반환
//	}


//	@PostMapping("/detail/{id}")
//	public String getDetail(@PathVariable int id, @RequestBody CulturalPropertiesDTO property) {
//		// POST 요청 처리
//	}


	//	@GetMapping("/detail/{id}")
	//	public String CulturalPropertyDetail() {
	//		return "/culturalProperties/culturalPropertiesDetail";
	//	}

	//	@GetMapping("/detail/{id}")
	//	@ResponseBody
	//	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
	//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertiesById(id);
	//		model.addAttribute("property", property);
	//		return "/culturalProperties/culturalPropertiesDetail";   // detail.html로 변경
	//	}


//	@GetMapping("/detail/{id}")
//	@ResponseBody // JSON 형식으로 데이터 전송
//	public ResponseEntity<CulturalPropertiesDTO> getCulturalPropertyDetail(@PathVariable int id) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);
//		if (property != null) {
//			return ResponseEntity.ok(property); // JSON 형태로 반환
//		} else {
//			return ResponseEntity.notFound().build(); // 데이터가 없을 경우 404 응답
//		}
//	}

//	@GetMapping("/detail/{id}")
//	public String culturalPropertiesDetail(@RequestParam("id") int id, Model model, @AuthenticationPrincipal VWUserRoleDTO user) {
//
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);
//		model.addAttribute("culturalProperties", property);
//
//		int userId = user.getUserId();
//		model.addAttribute("userId", userId);
//		model.addAttribute("naverClientId", naverClientId);
//
//		return "/culturalProperties/culturalPropertiesDetail";
//
//	}

//	@GetMapping("/detail/{id}")
////	@ResponseBody
//	public ResponseEntity<CulturalPropertiesDTO> getCulturalPropertyDetail(@PathVariable int id) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);
//		if (property != null) {
//			return ResponseEntity.ok(property); // JSON 형식으로 반환
//		} else {
//			return ResponseEntity.notFound().build(); // 데이터가 없을 경우 404 응답
//		}
//	}

//	//  id 기반 데이터 불러오기(상세정보)
//	@GetMapping("/detail/{id}")
//	public CulturalPropertiesDTO getCulturalPropertyDetail(
//			@AuthenticationPrincipal VWUserRoleDTO userId,
//			@PathVariable int id
//	){
////        System.out.println("exhibition: " + exhibitionService.getExhibitionById(currentUser.getUserId(), exhibitionId));
//		return culturalPropertiesService.getCulturalPropertyById(userId.getUserId(), id);
//	}

//	@GetMapping("/detail/{id}")
//	public String culturalPropertiesDetail(@AuthenticationPrincipal VWUserRoleDTO userId,
//										   @PathVariable int id,
//										   Model model) {
//		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(userId.getUserId(), id);
//		model.addAttribute("culturalProperties", property);
//		return "culturalProperties/culturalPropertiesDetail"; // Thymeleaf 템플릿 경로를 반환
//	}

}


