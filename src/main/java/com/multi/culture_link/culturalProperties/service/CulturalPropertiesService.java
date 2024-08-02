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




    public List<NewsArticle> getNewsArticles() {
        // 크롤링 로직
        List<NewsArticle> articles = new ArrayList<>();

        return articles;
    }


}