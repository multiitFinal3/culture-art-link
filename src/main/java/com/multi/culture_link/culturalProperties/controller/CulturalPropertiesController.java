package com.multi.culture_link.culturalProperties.controller;


import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
import com.multi.culture_link.culturalProperties.model.dto.*;
import com.multi.culture_link.culturalProperties.service.CulturalPropertiesService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
	public String culturalProperties(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {

		model.addAttribute("user", user.getUser());

		return "/culturalProperties/culturalProperties";
	}




	@GetMapping("/getAll")
	@ResponseBody
	public ResponseEntity<List<CulturalPropertiesDTO>> getAllCulturalProperties() {
		List<CulturalPropertiesDTO> properties = culturalPropertiesService.getAllCulturalProperties();

		// 각 문화재의 평균 평점을 설정
		for (CulturalPropertiesDTO property : properties) {
			double averageRating = culturalPropertiesService.averageRating(property.getId());
			property.setAverageRating(averageRating);
		}


		return ResponseEntity.ok(properties);
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
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String culturalPropertiesName,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) String dynasty) {

		List<CulturalPropertiesDTO> properties = culturalPropertiesService.searchCulturalProperties(
				category, culturalPropertiesName, region, dynasty
		);

		return ResponseEntity.ok(properties); // 전체 검색 결과를 ResponseEntity로 반환
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


			// 최신 10개만 남기기
			if (articles.size() > 10) {
				articles = articles.subList(0, 10);
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
//			String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q=" + query + "&type=video&key=" + apiKey;
			String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=6&q=" + query + "&type=video&order=date&key=" + apiKey;
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



	// 문화재 상세 페이지
	@GetMapping("/detail/{id}")
	public String getCulturalPropertyDetail(@PathVariable int id, Model model) {
		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);

		// 리뷰 목록 가져오기
		List<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getReviewsByCulturalPropertyId(id);

		// 평균 평점 계산
		double averageRating = culturalPropertiesService.averageRating(id);
		model.addAttribute("averageRating", averageRating);

		// Naver API 키 추가
		model.addAttribute("naverClientId", naverClientId);

		// 키워드 목록 가져오기
		List<String> keywords = culturalPropertiesService.getKeywordsByCulturalPropertyId(id);

		// 리뷰 키워드 가져오기 (새로 추가)
		List<String> reviewKeywords = culturalPropertiesService.getReviewKeywordsByCulturalPropertyId(id);

		System.out.println("디테일 아이디 "+ id);
		model.addAttribute("property", property);
		model.addAttribute("getNearbyPlace", culturalPropertiesService.getNearbyPlace(property.getRegion(), property.getDistrict(), id));
		model.addAttribute("reviews", reviews);
		model.addAttribute("keywords", keywords);
		model.addAttribute("reviewKeywords", reviewKeywords);

		System.out.println("Reviews: " + reviews);
//		System.out.println("근처 문화재 수: " + nearbyPlaces.size());

		return "/culturalProperties/culturalPropertiesDetail";
	}


	@GetMapping("/detail/{culturalPropertiesId}/getInterest")
	public ResponseEntity<List<CulturalPropertiesInterestDTO>> getDetailInterest(@PathVariable int culturalPropertiesId, @RequestParam int userId) {
		List<CulturalPropertiesInterestDTO> interests = culturalPropertiesService.getDetailInterest(culturalPropertiesId, userId);
		return ResponseEntity.ok(interests);
	}








	//상세페이지 리뷰
	@GetMapping("/detail/{culturalPropertiesId}/review")
	public ResponseEntity<Map<String, Object>> getRecentReview(@PathVariable int culturalPropertiesId) {
		List<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getRecentReview(culturalPropertiesId);
		int totalReviews = culturalPropertiesService.countReviews(culturalPropertiesId); // 총 리뷰 수

		Map<String, Object> response = new HashMap<>();
		response.put("reviews", reviews);
		response.put("totalReviews", totalReviews);

		return ResponseEntity.ok(response); // 200 OK와 함께 응답 반환
	}



	// 리뷰페이지
	@GetMapping("/detail/{id}/review/detail")
	public String culturalPropertiesReviewDetail(@PathVariable int id,
												 @AuthenticationPrincipal VWUserRoleDTO user, Model model) {
		CulturalPropertiesDTO property = culturalPropertiesService.getCulturalPropertyById(id);

		// 리뷰 목록 가져오기
		List<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getReviewsByCulturalPropertyId(id);

		// 현재 로그인된 사용자 정보를 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = null; // 사용자 이름 초기화

		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();
			userName = vwUserRoleDTO.getUsername(); // 사용자 이름 가져오기
		}

		// 평균 평점 계산
		double averageRating = culturalPropertiesService.averageRating(id);
		model.addAttribute("averageRating", averageRating);


		System.out.println("리뷰디테일 아이디 " + id);
		model.addAttribute("property", property);
		model.addAttribute("userName", user.getUsername()); // 사용자 이름 추가
		model.addAttribute("userId2", user.getUserId()); // 사용자 ID 추가
		model.addAttribute("userName", userName);

		if (property == null) {
			// 이곳에 로그 추가하여 property가 null인지 확인
			System.out.println("Property is null for ID: " + id);
		} else {
			System.out.println("Property ID: " + property.getId()); // ID 확인
		}

		model.addAttribute("reviews", reviews);

		System.out.println("Reviews: " + reviews);

		return "/culturalProperties/culturalPropertiesReviewDetail";
	}


	@GetMapping("/detail/{id}/review/getReviewUserId")
	public ResponseEntity<Map<String, Integer>> getReviewUserId() {
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




	@PostMapping("/detail/{id}/review/add")
	public ResponseEntity<String> addReview(@PathVariable int id, @RequestBody CulturalPropertiesReviewDTO reviewDTO) {
		System.out.println("Received review DTO: " + reviewDTO);

		// 현재 로그인된 사용자 정보를 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof VWUserRoleDTO) {
			VWUserRoleDTO vwUserRoleDTO = (VWUserRoleDTO) authentication.getPrincipal();
			int userId = vwUserRoleDTO.getUserId(); // 사용자 ID 가져오기
			String userName = vwUserRoleDTO.getUsername(); // 사용자 이름 가져오기
			String userProfileImage = vwUserRoleDTO.getUserProfilePic();

			reviewDTO.setUserId(userId); // 리뷰 DTO에 사용자 ID 설정
			reviewDTO.setCulturalPropertiesId(id); // culturalPropertiesId도 설정
			reviewDTO.setUserName(userName);
			reviewDTO.setUserProfileImage(userProfileImage);

			// 리뷰 추가 서비스 호출
			culturalPropertiesService.addReview(reviewDTO);
			return ResponseEntity.ok("리뷰가 등록되었습니다.");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	}



	@DeleteMapping("/detail/{culturalPropertiesId}/review/remove")
	public ResponseEntity<String> deleteReview(
			@RequestParam int id,
			@PathVariable int culturalPropertiesId,
			@AuthenticationPrincipal VWUserRoleDTO user) {

		// 현재 로그인한 사용자의 ID 가져오기
		int userId = user.getUserId();
		System.out.println("삭제 userId"+ userId);

		System.out.println("삭제 요청: userId=" + userId + ", reviewId=" + id + ", culturalPropertiesId=" + culturalPropertiesId);
		// 리뷰 삭제 서비스 호출
		boolean deleted = culturalPropertiesService.deleteReview(id, culturalPropertiesId, userId);

		if (deleted) {
			return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
		} else {
			System.out.println("리뷰 삭제 실패: 권한이 없거나 리뷰가 존재하지 않음.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("리뷰 삭제 권한이 없습니다.");
		}
	}




	@PutMapping("/detail/{culturalPropertiesId}/review/update")
	public ResponseEntity<String> updateReview(
			@PathVariable int culturalPropertiesId,
			@RequestParam int id,
			@RequestBody CulturalPropertiesReviewDTO reviewDTO) {

		boolean updated = culturalPropertiesService.updateReview(id, culturalPropertiesId, reviewDTO);

		if (updated) {
			return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("리뷰 수정 권한이 없습니다.");
		}
	}


	@GetMapping("/detail/{culturalPropertiesId}/review/reviewList")
	public ResponseEntity<Map<String, Object>> getReviewList(
			@PathVariable int culturalPropertiesId,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		// Pageable 객체를 생성합니다.
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "created_at"));

		// 서비스 호출
		Page<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getReviewsByCulturalPropertiesId(culturalPropertiesId, pageable);

		// 응답에 필요한 정보를 Map으로 만듭니다.
		Map<String, Object> response = new HashMap<>();
		response.put("reviews", reviews.getContent());
		response.put("totalElements", reviews.getTotalElements());
		response.put("totalPages", reviews.getTotalPages());
		response.put("currentPage", reviews.getNumber());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/detail/{culturalPropertiesId}/reviewList")
	public ResponseEntity<Map<String, Object>> getDetailReviewList(
			@PathVariable int culturalPropertiesId,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		// Pageable 객체를 생성합니다.
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "created_at"));

		// 서비스 호출
		Page<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getReviewsByCulturalPropertiesId(culturalPropertiesId, pageable);

		// 응답에 필요한 정보를 Map으로 만듭니다.
		Map<String, Object> response = new HashMap<>();
		response.put("reviews", reviews.getContent());
		response.put("totalElements", reviews.getTotalElements());
		response.put("totalPages", reviews.getTotalPages());
		response.put("currentPage", reviews.getNumber());

		return ResponseEntity.ok(response);
	}




	@GetMapping("/like/keywords")
	public ResponseEntity<List<KeywordDTO>> getLikedKeywords(@AuthenticationPrincipal VWUserRoleDTO user) {
		return ResponseEntity.ok(culturalPropertiesService.getLikeKeyword(user.getUserId()));
	}

	@GetMapping("/dislike/keywords")
	public ResponseEntity<List<KeywordDTO>> getDislikedKeywords(@AuthenticationPrincipal VWUserRoleDTO user) {
		return ResponseEntity.ok(culturalPropertiesService.getDislikeKeyword(user.getUserId()));
	}

	@GetMapping("/keywords")
	public ResponseEntity<List<KeywordDTO>> getUnselectedKeywords(
			@AuthenticationPrincipal VWUserRoleDTO user,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int limit) {
		return ResponseEntity.ok(culturalPropertiesService.getUnselectedKeywords(user.getUserId(), page, limit));
	}



	@GetMapping("/recommend")
	@ResponseBody
	public List<CulturalPropertiesDTO> getRecommendedCulturalProperties(@AuthenticationPrincipal VWUserRoleDTO user) {
		int userId = user.getUserId();
		return culturalPropertiesService.getRecommendedCulturalProperties(userId);
	}



	@GetMapping("/myReviews")
	public ResponseEntity<Map<String, Object>> getMyReviews(
			@AuthenticationPrincipal VWUserRoleDTO user,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created_at"));
		Page<CulturalPropertiesReviewDTO> reviews = culturalPropertiesService.getMyReviews(user.getUserId(), pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("reviews", reviews.getContent());
		response.put("currentPage", reviews.getNumber());
		response.put("totalItems", reviews.getTotalElements());
		response.put("totalPages", reviews.getTotalPages());

		return ResponseEntity.ok(response);
	}


	@PutMapping("/detail/{culturalPropertiesId}/review/edit")
	public ResponseEntity<String> updateReview(
			@PathVariable int culturalPropertiesId,
			@RequestBody CulturalPropertiesReviewDTO reviewDTO) {
		boolean updated = culturalPropertiesService.editReview(reviewDTO.getId(), culturalPropertiesId, reviewDTO);
		if (updated) {
			return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("리뷰 수정 권한이 없습니다.");
		}
	}


	@GetMapping("/getUserInterest")
	public ResponseEntity<List<CulturalPropertiesDTO>> getUserInterest(
			@RequestParam String interestType,
			@AuthenticationPrincipal VWUserRoleDTO user) {
		List<CulturalPropertiesDTO> interests = culturalPropertiesService.getUserInterest(user.getUserId(), interestType);
		return ResponseEntity.ok(interests);
	}

	@PostMapping("/removeUserInterest")
	public ResponseEntity<String> removeUserInterest(
			@RequestParam int culturalPropertiesId,
			@RequestParam String interestType,
			@AuthenticationPrincipal VWUserRoleDTO user) {
		culturalPropertiesService.removeUserInterest(user.getUserId(), culturalPropertiesId);
		return ResponseEntity.ok("관심이 제거되었습니다.");
	}



	@PostMapping("/addUserInterest")
	public ResponseEntity<String> addUserInterest(
			@RequestParam int culturalPropertiesId,
			@RequestParam String interestType,
			@AuthenticationPrincipal VWUserRoleDTO user) {
		culturalPropertiesService.addUserInterest(user.getUserId(), culturalPropertiesId, interestType);
		return ResponseEntity.ok(interestType.equals("LIKE") ? "찜이 추가되었습니다." : "관심없음이 추가되었습니다.");
	}




	@GetMapping("/main/recommend")
	public ResponseEntity<List<CulturalPropertiesDTO>> getMainRecommend(@AuthenticationPrincipal VWUserRoleDTO user) {
		int userId = user.getUserId();
		List<CulturalPropertiesDTO> recommendedProperties = culturalPropertiesService.getMainRecommend(userId);

		// 각 문화재의 평균 평점을 설정
		for (CulturalPropertiesDTO property : recommendedProperties) {
			double averageRating = culturalPropertiesService.averageRating(property.getId());
			property.setAverageRating(averageRating);
		}


		return ResponseEntity.ok(recommendedProperties);
	}









}


