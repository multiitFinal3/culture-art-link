package com.multi.culture_link.festival.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.FestivalContentReviewNaverKeywordMapDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;
import com.multi.culture_link.festival.model.dto.VWUserReviewDataDTO;

import java.util.ArrayList;

public interface FestivalService {
	ArrayList<FestivalKeywordDTO> findFestivalKeywordByFestivalId(int festivalId) throws Exception;
	
	void insertUserLoveFestival(UserFestivalLoveHateMapDTO map1) throws Exception;
	
	UserFestivalLoveHateMapDTO findUserMapByUserMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void insertUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void updateUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	ArrayList<Integer> findLoveList(int userId) throws Exception;
	
	void deleteUserLoveFestival(UserFestivalLoveHateMapDTO userMap1) throws Exception;
	
	void deleteUserKeywordMap(UserFestivalLoveHateMapDTO userMap2) throws Exception;
	
	void insertUserHateFestival(UserFestivalLoveHateMapDTO map1) throws Exception;
	
	ArrayList<Integer> findHateList(int userId) throws Exception;
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findContentKeywordListByFestivalId(int festivalId) throws Exception;
	
	TimeDTO findTimeIdByFestivalId(int festivalId) throws Exception;
	
	ArrayList<VWUserReviewDataDTO> findFestivalReviewListByVWUserReviewDTO(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception;
	
	int findFestivalReviewCountByVWUserReviewDTO(int festivalId) throws Exception;
	
	void insertFestivalReview(VWUserReviewDataDTO userReviewDataDTO) throws Exception;
}
