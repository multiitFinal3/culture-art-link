package com.multi.culture_link.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dao.CulturalPropertiesKeywordDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.admin.culturalProperties.service.AdminCulturalPropertiesService;
import com.multi.culture_link.culturalProperties.model.dao.CulturalPropertiesDAO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesReviewDTO;
import com.multi.culture_link.culturalProperties.model.dto.NewsArticle;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class CulturalPropertiesService {

	@Autowired
	private CulturalPropertiesDAO culturalPropertiesDAO;
//    private final AdminCulturalPropertiesDAO adminCulturalPropertiesDAO;
    private final AdminCulturalPropertiesService adminCulturalPropertiesService;
    private final CulturalPropertiesKeywordDAO culturalPropertiesKeywordDAO;

    public CulturalPropertiesService(AdminCulturalPropertiesDAO adminCulturalPropertiesDAO, AdminCulturalPropertiesService adminCulturalPropertiesService, CulturalPropertiesKeywordDAO culturalPropertiesKeywordDAO) {
//        this.adminCulturalPropertiesDAO = adminCulturalPropertiesDAO;
        this.adminCulturalPropertiesService = adminCulturalPropertiesService;
        this.culturalPropertiesKeywordDAO = culturalPropertiesKeywordDAO;
    }


    public int getTotalCount() {
        return culturalPropertiesDAO.getTotalCount();
    }

   

    public List<CulturalPropertiesDTO> getAllCulturalProperties() {
        return culturalPropertiesDAO.listAllCulturalProperties();
    }

    public CulturalPropertiesDTO getCulturalPropertyById(int id) {

        System.out.println("디테일 아이디 서비스 "+ id);
        return culturalPropertiesDAO.getCulturalPropertyById(id);
    }


    public void addLike(CulturalPropertiesInterestDTO interest) {
        interest.setInterestType("LIKE"); // LIKE 유형 설정
        culturalPropertiesDAO.addInterest(interest);
    }

    public void addDislike(CulturalPropertiesInterestDTO interest) {
        interest.setInterestType("DISLIKE"); // DISLIKE 유형 설정
        culturalPropertiesDAO.addInterest(interest);
    }

    public void removeInterest(CulturalPropertiesInterestDTO interest) {
        culturalPropertiesDAO.removeInterest(interest);
    }


    // 사용자 찜 정보 가져오기
    public List<CulturalPropertiesInterestDTO> getInterest(int userId) {
        return culturalPropertiesDAO.getInterest(userId);
    }



    public List<CulturalPropertiesDTO> searchCulturalProperties(String category, String culturalPropertiesName, String region, String dynasty) {
        long total = culturalPropertiesDAO.countCulturalProperties(category, culturalPropertiesName, region, dynasty); // 총 개수 조회
        List<CulturalPropertiesDTO> properties = culturalPropertiesDAO.searchCulturalProperties(category, culturalPropertiesName, region, dynasty);


        return properties; // 검색된 문화재 목록 반환
    }


    public List<String> getAllCategories() {
        return culturalPropertiesDAO.getAllCategories();
    }




    public List<NewsArticle> getNewsArticles() {
        // 크롤링 로직
        List<NewsArticle> articles = new ArrayList<>();

        return articles;
    }


    public List<CulturalPropertiesDTO> getNearbyPlace(String region, String district, int id) {
        List<CulturalPropertiesDTO> nearbyPlaces = culturalPropertiesDAO.getNearbyPlace(region, district, id);

        // 근처 문화재가 없는 경우 랜덤 데이터 가져오기
        if (nearbyPlaces.isEmpty()) {
            nearbyPlaces = culturalPropertiesDAO.getRandomPlace(region, id);
        }

        return nearbyPlaces;
    }


    // 사용자 찜 정보 가져오기
    public List<CulturalPropertiesInterestDTO> getDetailInterest(int culturalPropertiesId, int userId) {
        return culturalPropertiesDAO.getDetailInterest(culturalPropertiesId, userId);
    }



    public void addReview(CulturalPropertiesReviewDTO reviewDTO) {
        System.out.println("리뷰 DTO: " + reviewDTO);
        culturalPropertiesDAO.addReview(reviewDTO); // 리뷰 등록을 위한 매퍼 호출

        // 키워드 추출 및 저장
        adminCulturalPropertiesService.extractAndSaveReviewKeywords(reviewDTO.getCulturalPropertiesId());
    }

    public List<CulturalPropertiesReviewDTO> getReviewsByCulturalPropertyId(int id) {
        // 해당 문화재 ID에 대한 리뷰를 반환하는 로직
        return culturalPropertiesDAO.getReviewsByCulturalPropertyId(id);
    }

    public boolean deleteReview(int id, int culturalPropertiesId, int userId) {
        // 리뷰를 조회하여 작성자와 문화재 ID를 확인
        CulturalPropertiesReviewDTO review = culturalPropertiesDAO.findByReviewId(id);

        if (review != null && review.getUserId() == userId && review.getCulturalPropertiesId() == culturalPropertiesId) {
            // 조건이 일치하면 삭제
            culturalPropertiesDAO.deleteReview(id);
            return true; // 삭제 성공
        }
        return false; // 삭제 실패
    }


    public boolean updateReview(int id, int culturalPropertiesId, CulturalPropertiesReviewDTO reviewDTO) {

        CulturalPropertiesReviewDTO review = culturalPropertiesDAO.findByReviewId(id);

        // 리뷰가 존재하고, 문화재 ID가 일치하는지 확인
        if (review != null && review.getCulturalPropertiesId() == culturalPropertiesId) {
            review.setContent(reviewDTO.getContent()); // 수정할 내용 설정
            review.setStar(reviewDTO.getStar()); // 별점 수정
            culturalPropertiesDAO.updateReview(review); // 리뷰 업데이트
            return true; // 수정 성공
        }
        return false; // 수정 실패
    }



    public Page<CulturalPropertiesReviewDTO> getReviewsByCulturalPropertiesId(int culturalPropertiesId, Pageable pageable) {
        List<CulturalPropertiesReviewDTO> reviews = culturalPropertiesDAO.getReviewList(culturalPropertiesId, pageable);
        int totalReviews = culturalPropertiesDAO.countReview(culturalPropertiesId); // 총 리뷰 개수 가져오기
        return new PageImpl<>(reviews, pageable, totalReviews); // PageImpl 사용하여 Page 객체 생성
    }

    public double averageRating(int culturalPropertiesId) {
        return culturalPropertiesDAO.averageRating(culturalPropertiesId);
    }


    public List<CulturalPropertiesReviewDTO> getRecentReview(int culturalPropertiesId) {
        return culturalPropertiesDAO.getRecentReview(culturalPropertiesId);
    }

    public int countReviews(int culturalPropertiesId) {
        return culturalPropertiesDAO.countReview(culturalPropertiesId); // 총 리뷰 수를 반환하는 DAO 메서드
    }

    public List<String> getKeywordsByCulturalPropertyId(int culturalPropertiesId) {
        return culturalPropertiesDAO.getKeywordsByCulturalPropertyId(culturalPropertiesId);
    }

    public List<String> getReviewKeywordsByCulturalPropertyId(int culturalPropertiesId) {
        return culturalPropertiesDAO.getReviewKeywordsByCulturalPropertyId(culturalPropertiesId);
    }




    public int getTotalKeywordCount() {
        return culturalPropertiesKeywordDAO.getTotalKeywordCount();
    }



    public List<KeywordDTO> getLikeKeyword(int userId) {
        return culturalPropertiesKeywordDAO.getLikeKeyword(userId);
    }

    public List<KeywordDTO> getDislikeKeyword(int userId) {
        return culturalPropertiesKeywordDAO.getDislikeKeyword(userId);
    }

    public List<KeywordDTO> getUnselectedKeywords(int userId, int page, int limit) {
        int offset = (page - 1) * limit;
        return culturalPropertiesKeywordDAO.getUnselectedKeywords(userId, offset, limit);
    }


}