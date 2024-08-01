package com.multi.culture_link.culturalProperties.service;

import com.multi.culture_link.admin.culturalProperties.model.dao.AdminCulturalPropertiesDAO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.PageDTO;
import com.multi.culture_link.culturalProperties.model.dao.CulturalPropertiesDAO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.NewsArticle;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        return culturalPropertiesDAO.getCulturalPropertyById(id);
    }

//    // 찜 추가 메소드
//    public void addLike(int culturalPropertiesId, int userId) {
//        // 먼저 기존 데이터를 삭제하고 새로 추가
//        culturalPropertiesDAO.deleteByCulturalPropertiesIdAndUserId(culturalPropertiesId, userId);
//
//        CulturalPropertiesInterestDTO interest = new CulturalPropertiesInterestDTO(culturalPropertiesId, userId, true, false);
//        culturalPropertiesDAO.save(interest);
//    }
//
//    // 관심없음 추가 메소드
//    public void addDislike(int culturalPropertiesId, int userId) {
//        // 먼저 기존 데이터를 삭제하고 새로 추가
//        culturalPropertiesDAO.deleteByCulturalPropertiesIdAndUserId(culturalPropertiesId, userId);
//
//        CulturalPropertiesInterestDTO interest = new CulturalPropertiesInterestDTO(culturalPropertiesId, userId, false, true);
//        culturalPropertiesDAO.save(interest);
//    }

//    // 찜 추가
//    public void addLike(CulturalPropertiesInterestDTO interest) {
//        culturalPropertiesDAO.addLike(interest);
//    }
//
//    // 관심없음 추가
//    public void addDislike(CulturalPropertiesInterestDTO interest) {
//        culturalPropertiesDAO.addDislike(interest);
//    }
//
//    // 특정 사용자의 관심 목록 가져오기
//    public List<CulturalPropertiesInterestDTO> getInterestsByUser(int userId) {
//        return culturalPropertiesDAO.getInterestsByUser(userId);
//    }


//    // 이걸로
//    // 찜 추가
//    public void addLike(CulturalPropertiesInterestDTO interest) {
//        // 이미 찜한 경우 처리
//        if (!culturalPropertiesDAO.isLiked(interest)) {
//            culturalPropertiesDAO.addInterest(interest); // DAO를 호출하여 찜 추가
//        } else {
//            throw new IllegalArgumentException("이미 찜한 문화재입니다."); // 예외 처리
//        }
//    }
//
//    // 관심없음 추가
//    public void addDislike(CulturalPropertiesInterestDTO interest) {
//        // 이미 관심없음인 경우 처리
//        if (!culturalPropertiesDAO.isDisliked(interest)) {
//            culturalPropertiesDAO.addInterest(interest); // DAO를 호출하여 관심없음 추가
//        } else {
//            throw new IllegalArgumentException("이미 관심없음으로 설정된 문화재입니다."); // 예외 처리
//        }
//    }
//
//    // 찜 또는 관심없음 삭제
//    public void removeInterest(CulturalPropertiesInterestDTO interest) {
//        culturalPropertiesDAO.removeInterest(interest); // DAO를 호출하여 관심 제거
//    }
//
//    // 사용자의 찜 목록 가져오기
//    public List<CulturalPropertiesInterestDTO> getInterestsByUser(int userId) {
//        return culturalPropertiesDAO.getInterestsByUser(userId);
//    }

    public List<NewsArticle> getNewsArticles() {
        // 크롤링 로직
        List<NewsArticle> articles = new ArrayList<>();

        return articles;
    }


}