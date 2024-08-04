package com.multi.culture_link.festival.controller;


import com.multi.culture_link.admin.festival.service.AdminFestivalService;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.*;
import com.multi.culture_link.festival.service.FestivalService;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;


/**
 * 일반회원 축제 관련 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Controller
@RequestMapping("/festival")
public class FestivalController {
	
	private final AdminFestivalService adminFestivalService;
	private final FestivalService festivalService;
	
	@Value("${API-KEY.naverClientId}")
	private String naverClientId;
	
	public FestivalController(AdminFestivalService adminFestivalService, FestivalService festivalService) {
		this.adminFestivalService = adminFestivalService;
		this.festivalService = festivalService;
	}
	
	/**
	 * 메뉴바에서 페스티벌 리스트 화면으로 이동
	 *
	 * @param user  유저의 정보
	 * @param model 화면으로 보내는 정보를 담는 모델
	 * @return 페스티벌 리스트 주소 반환
	 */
	@GetMapping("/festival-list")
	public String festivalListPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model) {

//		System.out.println("@AuthenticationPrincipal : " + user.toString());
		model.addAttribute("user", user.getUser());
		
		return "/festival/festivalList";
		
	}
	
	/**
	 * 축제 상세 화면으로 이동
	 *
	 * @param festivalId 해당 축제 DB 아이디
	 * @param model      화면에 정보를 담아감
	 * @return 화면으로 이동
	 */
	@GetMapping("/festival-detail")
	public String festivalDetail(@RequestParam("festivalId") int festivalId, Model model, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			FestivalDTO festivalDTO = adminFestivalService.findDBFestivalByFestivalId(festivalId);
			model.addAttribute("festival", festivalDTO);
			
			int userId = user.getUserId();
			model.addAttribute("userId", userId);
			model.addAttribute("naverClientId", naverClientId);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "/festival/festivalDetailPage";
		
	}
	
	/**
	 * 찜이 안되어있는 축제에 대해 찜매핑 테이블에 인서트
	 *
	 * @param festivalId
	 * @param user
	 */
	@PostMapping("/insertUserLoveFestival")
	@ResponseBody
	public String insertUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		String sortCode = "L";
		
		UserFestivalLoveHateMapDTO map1 = new UserFestivalLoveHateMapDTO();
		map1.setUserId(userId);
		map1.setSortCode(sortCode);
		map1.setFestivalId(festivalId);
		
		ArrayList<FestivalKeywordDTO> keywordList = null;
		
		try {
			
			festivalService.insertUserLoveFestival(map1);
			// 뷰에 키워드 매핑이 자동으로 생성된다
//			keywordList = festivalService.findFestivalKeywordByFestivalId(festivalId);
//
//			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
//
//			for (FestivalKeywordDTO keywordDTO : keywordList) {
//
//				if (!list.contains(keywordDTO)) {
//					list.add(keywordDTO);
//				}
//
//			}
//
//			for (FestivalKeywordDTO keywordDTO : list) {
//
//				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
//				userMap.setUserId(userId);
//				userMap.setSortCode(sortCode);
//				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
//
//				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
//
//				if (userMap2 == null) {
//
//					userMap.setFestivalCount(1);
//					festivalService.insertUserKeywordMap(userMap);
//
//				} else {
//
//					int count = userMap2.getFestivalCount() + 1;
//					System.out.println("count : " + count);
//					userMap2.setFestivalCount(count);
//					System.out.println("update : " + userMap2);
//					festivalService.updateUserKeywordMap(userMap2);
//
//				}
//
//			}
			
			return "찜하기 및 관련 키워드 추가 성공";
			
			
		} catch (Exception e) {
			
			return "에러 발생";
		}
		
		
	}
	
	
	/**
	 * 유저가 찜한 목록을 표시하기 위해 리스트를 가져옴
	 *
	 * @param user
	 * @return 축제 DB 번호
	 */
	@PostMapping("/findLoveList")
	@ResponseBody
	public ArrayList<Integer> findLoveList(@AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		
		ArrayList<Integer> list = null;
		try {
			list = festivalService.findLoveList(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("love list : " + list);
		
		return list;
		
	}
	
	
	/**
	 * 찜한 목록을 삭제하고 그와 연관된 키워드는 뷰에서 자동으로 차감됨
	 *
	 * @param festivalId
	 * @param user
	 * @return
	 */
	@PostMapping("/deleteUserLoveFestival")
	@ResponseBody
	public String deleteUserLoveFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			
			int userId = user.getUserId();
			String sortCode = "L";
			
			UserFestivalLoveHateMapDTO userMap1 = new UserFestivalLoveHateMapDTO();
			userMap1.setUserId(userId);
			userMap1.setFestivalId(festivalId);
			userMap1.setSortCode(sortCode);
			festivalService.deleteUserLoveFestival(userMap1);
			
			// 뷰 생성으로 인해 키워드 처리는 전부 뷰에서 자동으로 생성 및 차감 됨

//			ArrayList<FestivalKeywordDTO> keyList = festivalService.findFestivalKeywordByFestivalId(festivalId);
//
//			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
//
//			for (FestivalKeywordDTO keywordDTO : keyList) {
//
//				if (!list.contains(keywordDTO)) {
//					list.add(keywordDTO);
//				}
//
//			}
//
//			for (FestivalKeywordDTO keywordDTO : list) {
//
//				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
//				userMap.setUserId(userId);
//				userMap.setSortCode(sortCode);
//				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
//
//				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
//
//				if (userMap2 != null) {
//
//					festivalService.deleteUserKeywordMap(userMap2);
//
//				}
//
//			}
		
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return "찜 목록 및 관련 키워드 삭제 성공!";
		
	}
	
	
	/**
	 * 관심없음이 안되어있는 축제에 대해 찜 관심없음 매핑 테이블에 인서트
	 *
	 * @param festivalId
	 * @param user
	 */
	@PostMapping("/insertUserHateFestival")
	@ResponseBody
	public String insertUserHateFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		String sortCode = "H";
		
		UserFestivalLoveHateMapDTO map1 = new UserFestivalLoveHateMapDTO();
		map1.setUserId(userId);
		map1.setSortCode(sortCode);
		map1.setFestivalId(festivalId);

//		ArrayList<FestivalKeywordDTO> keywordList = null;
		
		try {
			
			festivalService.insertUserHateFestival(map1);


//			keywordList = festivalService.findFestivalKeywordByFestivalId(festivalId);
//
//			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
//
//			for (FestivalKeywordDTO keywordDTO : keywordList) {
//
//				if (!list.contains(keywordDTO)) {
//					list.add(keywordDTO);
//				}
//
//			}
//
//			for (FestivalKeywordDTO keywordDTO : list) {
//
//				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
//				userMap.setUserId(userId);
//				userMap.setSortCode(sortCode);
//				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
//
//				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
//
//				if (userMap2 == null) {
//
//					userMap.setFestivalCount(1);
//					festivalService.insertUserKeywordMap(userMap);
//
//				} else {
//
//					int count = userMap2.getFestivalCount() + 1;
//					userMap2.setFestivalCount(count);
//					System.out.println("update : " + userMap2);
//					festivalService.updateUserKeywordMap(userMap2);
//
//				}
//
//			}
			
			return "관심없음 및 관련 관심없음 키워드 추가 성공";
			
			
		} catch (Exception e) {
			
			return "에러 발생";
		}
		
		
	}
	
	
	/**
	 * 유저가 관심없음한 목록을 표시하기 위해 리스트를 가져옴
	 *
	 * @param user
	 * @return 축제 DB 번호
	 */
	@PostMapping("/findHateList")
	@ResponseBody
	public ArrayList<Integer> findHateList(@AuthenticationPrincipal VWUserRoleDTO user) {
		
		int userId = user.getUserId();
		
		ArrayList<Integer> list = null;
		try {
			list = festivalService.findHateList(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("hate list : " + list);
		
		return list;
		
	}
	
	
	/**
	 * 관심없음한 목록을 삭제하고 그와 연관된 키워드는 뷰에서 차감 또는 삭제 됨
	 *
	 * @param festivalId
	 * @param user
	 * @return
	 */
	@PostMapping("/deleteUserHateFestival")
	@ResponseBody
	public String deleteUserHateFestival(@RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			
			int userId = user.getUserId();
			String sortCode = "H";
			
			UserFestivalLoveHateMapDTO userMap1 = new UserFestivalLoveHateMapDTO();
			userMap1.setUserId(userId);
			userMap1.setFestivalId(festivalId);
			userMap1.setSortCode(sortCode);
			festivalService.deleteUserLoveFestival(userMap1);

//			ArrayList<FestivalKeywordDTO> keyList = festivalService.findFestivalKeywordByFestivalId(festivalId);
//
//			ArrayList<FestivalKeywordDTO> list = new ArrayList<FestivalKeywordDTO>();
//
//			for (FestivalKeywordDTO keywordDTO : keyList) {
//
//				if (!list.contains(keywordDTO)) {
//					list.add(keywordDTO);
//				}
//
//			}
//
//			for (FestivalKeywordDTO keywordDTO : list) {
//
//				UserFestivalLoveHateMapDTO userMap = new UserFestivalLoveHateMapDTO();
//				userMap.setUserId(userId);
//				userMap.setSortCode(sortCode);
//				userMap.setFestivalKeywordId(keywordDTO.getFestivalKeywordId());
//
//				UserFestivalLoveHateMapDTO userMap2 = festivalService.findUserMapByUserMap(userMap);
//
//				if (userMap2 != null) {
//
//					festivalService.deleteUserKeywordMap(userMap2);
//
//				}
//
//			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return "관심없음 목록 및 관련 키워드 삭제 성공!";
		
	}
	
	
	/**
	 * 해당 축제의 키워드를 반환
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("findContentKeywordListByFestivalId")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findContentKeywordListByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		try {
			
			FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO.setFestivalId(festivalId);
			mapDTO.setSortCode("C");
			list = festivalService.findKeywordListByFestivalId(mapDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	/**
	 * 해당 축제의 시간대 구하기
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findTimeIdByFestivalId")
	@ResponseBody
	public TimeDTO findTimeIdByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		TimeDTO timeDTO = null;
		try {
			timeDTO = festivalService.findTimeIdByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return timeDTO;
		
	}
	
	/**
	 * 특정 축제의 리뷰를 리뷰 페이지 번호에 맞게 가져오기
	 *
	 * @param festivalId
	 * @param page
	 * @return
	 */
	@PostMapping("/findFestivalReviewListByVWUserReviewDTO")
	@ResponseBody
	public ArrayList<VWUserReviewDataDTO> findFestivalReviewListByVWUserReviewDTO(@RequestParam("festivalId") int festivalId, @RequestParam("page") int page) {
		
		VWUserReviewDataDTO vwUserReviewDataDTO = new VWUserReviewDataDTO();
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		pageDTO.setStartEnd(pageDTO.getPage());
		vwUserReviewDataDTO.setPageDTO(pageDTO);
		vwUserReviewDataDTO.setFestivalId(festivalId);
		
		System.out.println("보내는 : " + vwUserReviewDataDTO);
		
		ArrayList<VWUserReviewDataDTO> list = null;
		try {
			list = festivalService.findFestivalReviewListByVWUserReviewDTO(vwUserReviewDataDTO);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	/**
	 * 특정 축제에 대한 리뷰가 총 몇개인 지 반환함
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findFestivalReviewCountByVWUserReviewDTO")
	@ResponseBody
	public int findFestivalReviewCountByVWUserReviewDTO(@RequestParam("festivalId") int festivalId) {
		
		int count = 0;
		try {
			count = festivalService.findFestivalReviewCountByVWUserReviewDTO(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return count;
		
	}
	
	/**
	 * 리뷰로 업로드 된 파일의 이름을 바꾸고 저장 후 경로를 DB에 저장함
	 *
	 * @param reviewTextArea
	 * @param reviewStar
	 * @param uploadFile
	 * @return
	 */
	@PostMapping("/insertFestivalReview")
	public String insertFestivalReview(@RequestParam("reviewTextArea") String reviewTextArea, @RequestParam("reviewStar") double reviewStar, @RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("festivalId") int festivalId, @AuthenticationPrincipal VWUserRoleDTO user) {
		
		try {
			
			String fileUUIDName = UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();
			String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/festival/reviewAttach/";
			Files.createDirectories(Paths.get(uploadDir));
			
			System.out.println("id : " + festivalId);
			
			File savedFile = new File(uploadDir + fileUUIDName);
			
			System.out.println("savedFile.getAbsolutePath() : " + savedFile.getAbsolutePath());
			System.out.println("savedFile.getName() : " + savedFile.getName());
			System.out.println("path : " + savedFile.getPath());
			System.out.println(System.getProperty("user.dir"));
			
			uploadFile.transferTo(savedFile);
			
			String attachment = uploadDir + fileUUIDName;
			
			VWUserReviewDataDTO userReviewDataDTO = new VWUserReviewDataDTO();
			userReviewDataDTO.setFestivalId(festivalId);
			userReviewDataDTO.setFestivalReviewStar(reviewStar);
			
			reviewTextArea = reviewTextArea.trim();
			
			userReviewDataDTO.setFestivalReviewContent(reviewTextArea);
			
			int userId = user.getUserId();
			
			
			int startIndex = attachment.indexOf("/img");
			System.out.println(startIndex);
			attachment = attachment.substring(startIndex);
			
			
			userReviewDataDTO.setAttachment(attachment);
			userReviewDataDTO.setUserId(userId);
			
			System.out.println("controller userReviewDTO : " + userReviewDataDTO);
			
			try {
				festivalService.insertFestivalReview(userReviewDataDTO);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		return "redirect:/festival/festival-detail?festivalId=" + festivalId;
	}
	
	
	/**
	 * 리뷰 삭제
	 *
	 * @param festivalReviewId
	 * @return
	 */
	@PostMapping("/deleteFestivalReviewByReviewId")
	@ResponseBody
	public String deleteFestivalReviewByReviewId(@RequestParam("festivalReviewId") int festivalReviewId) {
		
		try {
			festivalService.deleteFestivalReviewByReviewId(festivalReviewId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "삭제 성공";
	}
	
	
	/**
	 * 리뷰 수정
	 *
	 * @param festivalReviewId
	 * @return
	 */
	@PostMapping("/updateFestivalReview")
	@ResponseBody
	public String updateFestivalReview(@RequestParam("festivalReviewId") int festivalReviewId, @RequestParam("reviewText") String reviewText, @RequestParam("reviewStar") double reviewStar) {
		
		try {
			
			VWUserReviewDataDTO vwUserReviewDataDTO = new VWUserReviewDataDTO();
			vwUserReviewDataDTO.setFestivalReviewContent(reviewText);
			vwUserReviewDataDTO.setFestivalReviewStar(reviewStar);
			vwUserReviewDataDTO.setFestivalReviewId(festivalReviewId);
			festivalService.updateFestivalReview(vwUserReviewDataDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "수정 성공";
	}
	
	
	/**
	 * 같은 지역의 축제를 추천리스트로 가져가며 본인은 제외함
	 *
	 * @param festivalId
	 * @param regionId
	 * @return
	 */
	@PostMapping("/findSameRegionFestivalByRegionId")
	@ResponseBody
	public ArrayList<FestivalDTO> findSameRegionFestivalByRegionId(@RequestParam("festivalId") int festivalId, @RequestParam("regionId") int regionId) {
		
		
		ArrayList<FestivalDTO> list = null;
		try {
			
			FestivalDTO festivalDTO = new FestivalDTO();
			festivalDTO.setRegionId(regionId);
			festivalDTO.setFestivalId(festivalId);
			
			list = festivalService.findSameRegionFestivalByRegionId(festivalDTO);
			System.out.println("findSameRegionFestivalByRegionId list : " + list);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
	}
	
	
	/**
	 * 같은 계절의 축제를 추천리스트로 가져가며 본인은 제외함
	 *
	 * @param festivalId
	 * @param season
	 * @return
	 */
	@PostMapping("/findSameSeasonFestivalBySeason")
	@ResponseBody
	public ArrayList<FestivalDTO> findSameSeasonFestivalBySeason(@RequestParam("festivalId") int festivalId, @RequestParam("season") String season) {
		
		
		ArrayList<FestivalDTO> list = null;
		try {
			
			FestivalDTO festivalDTO = new FestivalDTO();
			festivalDTO.setSeason(season);
			festivalDTO.setFestivalId(festivalId);
			
			list = festivalService.findSameSeasonFestivalBySeason(festivalDTO);
			System.out.println("findSameSeasonFestivalBySeason list : " + list);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
	}
	
	
	/**
	 * 같은 주체기관의 축제를 추천리스트로 가져가며 본인은 제외함
	 *
	 * @param festivalId
	 * @param manageInstitution
	 * @return
	 */
	@PostMapping("/findSameManageFestivalByManageInstitution")
	@ResponseBody
	public ArrayList<FestivalDTO> findSameManageFestivalByManageInstitution(@RequestParam("festivalId") int festivalId, @RequestParam("manageInstitution") String manageInstitution) {
		
		
		ArrayList<FestivalDTO> list = null;
		try {
			
			FestivalDTO festivalDTO = new FestivalDTO();
			festivalDTO.setManageInstitution(manageInstitution);
			festivalDTO.setFestivalId(festivalId);
			
			list = festivalService.findSameManageFestivalByManageInstitution(festivalDTO);
			System.out.println("findSameManageFestivalByManageInstitution : " + list);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
	}
	
	
	/**
	 * 같은 키워드의 축제를 추천리스트로 가져가며 본인은 제외함
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findSameKeywordFestivalByfestivalId")
	@ResponseBody
	public ArrayList<FestivalDTO> findSameKeywordFestivalByfestivalId(@RequestParam("festivalId") int festivalId) {
		
		
		ArrayList<FestivalDTO> list = null;
		try {
			
			FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO.setFestivalId(festivalId);
			
			list = festivalService.findSameKeywordFestivalByfestivalId(mapDTO);
			System.out.println("findSameKeywordFestivalByfestivalId list : " + list);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("findSameKeywordFestivalByfestivalId : " + list);
		
		return list;
	}
	
	
	/**
	 * 관련된 페스티벌 이름으로 유튜브 영상을 검색
	 *
	 * @param page
	 * @param festivalName
	 * @return
	 */
	@PostMapping("/findFestivalYoutube")
	@ResponseBody
	public String findFestivalYoutube(@RequestParam("page") int page, @RequestParam("formattedStart") String formattedStart, @RequestParam("festivalName") String festivalName) {
		
		String youtubeId = null;
		
		try {
			youtubeId = festivalService.findFestivalYoutube(page, formattedStart, festivalName);
			System.out.println("findFestivalYoutube id : " + youtubeId);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return youtubeId;
	}
	
	
	/**
	 * 관련된 페스티벌 이름으로 네이버 기사를 검색
	 *
	 * @param page
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findFestivalNaverArticle")
	@ResponseBody
	public NaverArticleDTO findFestivalNaverArticle(@RequestParam("page") int page, @RequestParam("festivalId") int festivalId) {
		
		NaverArticleDTO naverArticleDTO = new NaverArticleDTO();
		
		FestivalDTO festivalDTO = null;
		try {
			festivalDTO = adminFestivalService.findDBFestivalByFestivalId(festivalId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		festivalDTO.setPageDTO(pageDTO);
		
		try {
			naverArticleDTO = festivalService.findFestivalNaverArticle(festivalDTO);
			System.out.println("findFestivalNaverArticle : " + naverArticleDTO);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return naverArticleDTO;
	}
	
	
	/**
	 * 관련된 페스티벌 이름으로 네이버 블로그를 검색
	 *
	 * @param page
	 * @param festivalName
	 * @return
	 */
	@PostMapping("/findFestivalNaverBlog")
	@ResponseBody
	public NaverBlogDTO findFestivalNaverBlog(@RequestParam("page") int page, @RequestParam("formattedStart") String formattedStart, @RequestParam("festivalName") String festivalName) {
		
		NaverBlogDTO naverBlogDTO = new NaverBlogDTO();
		FestivalDTO festivalDTO = new FestivalDTO();
		festivalDTO.setFormattedStart(formattedStart);
		festivalDTO.setFestivalName(festivalName);
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPage(page);
		festivalDTO.setPageDTO(pageDTO);
		
		try {
			naverBlogDTO = festivalService.findFestivalNaverBlog(festivalDTO);
			System.out.println("findFestivalNaverBlog : " + naverBlogDTO);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return naverBlogDTO;
	}
	
	
	/**
	 * 축제의 리뷰 키워드를 반환
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findReviewKeywordListByFestivalId")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findReviewKeywordListByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		try {
			FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO.setFestivalId(festivalId);
			mapDTO.setSortCode("R");
			list = festivalService.findKeywordListByFestivalId(mapDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	
	/**
	 * 축제의 네이버 기사 키워드를 반환
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findNaverArticleKeywordListByFestivalId")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findNaverArticleKeywordListByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		try {
			FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO.setFestivalId(festivalId);
			mapDTO.setSortCode("NA");
			list = festivalService.findKeywordListByFestivalId(mapDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	
	/**
	 * 축제의 네이버 블로그 키워드를 반환
	 *
	 * @param festivalId
	 * @return
	 */
	@PostMapping("/findNaverBlogKeywordListByFestivalId")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findNaverBlogKeywordListByFestivalId(@RequestParam("festivalId") int festivalId) {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		try {
			FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
			mapDTO.setFestivalId(festivalId);
			mapDTO.setSortCode("NB");
			list = festivalService.findKeywordListByFestivalId(mapDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	/**
	 * vw_festival_keyword_tf_idf 테이블에서 tf_idf가 높은 순서대로 반환
	 *
	 * @param page
	 * @return
	 */
	@PostMapping("/findPopularFestivalKeyword")
	@ResponseBody
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findPopularFestivalKeyword(@RequestParam("page") int page) {
		
		System.out.println("들어롬");
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = null;
		PageDTO pageDTO = new PageDTO();
		pageDTO.setStartEnd(page);
//		pageDTO.setStart(1);
		FestivalContentReviewNaverKeywordMapDTO mapDTO = new FestivalContentReviewNaverKeywordMapDTO();
		mapDTO.setPageDTO(pageDTO);
		try {
			list = festivalService.findPopularFestivalKeyword(mapDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("findPopularFestivalKeyword : " + list);
		
		return list;
		
	}
	
	
}
