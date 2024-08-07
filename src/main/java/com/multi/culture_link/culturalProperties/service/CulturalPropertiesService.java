package com.multi.culture_link.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public List<CulturalPropertiesDTO> listCulturalProperties(int offset, int limit) {
        // limit가 0이면 모든 데이터를 가져옴
        if (limit <= 0) {
            return culturalPropertiesDAO.listAllCulturalProperties(); // 전체 문화재를 가져오는 메소드 호출
        }
        return culturalPropertiesDAO.listCulturalProperties(offset, limit);
    }


    public int getTotalCount() {
        return culturalPropertiesDAO.getTotalCount();
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


    public Page<CulturalPropertiesDTO> searchCulturalProperties(int page, int pageSize, String category, String culturalPropertiesName, String region, String dynasty) {
        int offset = (page - 1) * pageSize;
        List<CulturalPropertiesDTO> properties = culturalPropertiesDAO.searchCulturalProperties(category, culturalPropertiesName, region, dynasty, offset, pageSize);
        long total = culturalPropertiesDAO.countCulturalProperties(category, culturalPropertiesName, region, dynasty);

        return new PageImpl<>(properties, PageRequest.of(page - 1, pageSize), total);
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

////네모버튼
//    public void likeAttraction(int id) {
//        culturalPropertiesDAO.likeAttraction(id);
//    }
//
//    public void dislikeAttraction(int id) {
//        culturalPropertiesDAO.dislikeAttraction(id);
//    }
//}




//    public boolean isPropertyLikedByUser(int propertyId, int userId) {
//        // DB에서 해당 사용자의 찜 여부를 체크하는 로직
//    }
//
//    public boolean isPropertyDislikedByUser(int propertyId, int userId) {
//        // DB에서 해당 사용자의 관심 없음 여부를 체크하는 로직
//    }



//    public void addInterest(int userId, int id, String interestType) {
//        culturalPropertiesDAO.insertInterest(userId, id, interestType);
//    }
//
//    public void deleteInterest(int userId, int id) {
//        culturalPropertiesDAO.deleteInterest(userId, id);
//    }
//
//    public boolean isLiked(int userId, int id) {
//        return culturalPropertiesDAO.isLiked(userId, id);
//    }
//
//    public boolean isDisliked(int userId, int id) {
//        return culturalPropertiesDAO.isDisliked(userId, id);
//    }

    public void addReview(CulturalPropertiesReviewDTO reviewDTO) {
        System.out.println("리뷰 DTO: " + reviewDTO);
        culturalPropertiesDAO.addReview(reviewDTO); // 리뷰 등록을 위한 매퍼 호출
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




}