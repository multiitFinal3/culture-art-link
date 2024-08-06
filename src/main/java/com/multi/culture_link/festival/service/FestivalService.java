package com.multi.culture_link.festival.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.*;

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
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findKeywordListByFestivalId(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception;
	
	TimeDTO findTimeIdByFestivalId(int festivalId) throws Exception;
	
	ArrayList<VWUserReviewDataDTO> findFestivalReviewListByVWUserReviewDTO(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception;
	
	int findFestivalReviewCountByVWUserReviewDTO(int festivalId) throws Exception;
	
	void insertFestivalReview(VWUserReviewDataDTO userReviewDataDTO) throws Exception;
	
	void deleteFestivalReviewByReviewId(int festivalReviewId) throws Exception;
	
	void updateFestivalReview(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception;
	
	ArrayList<FestivalDTO> findSameRegionFestivalByRegionId(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<FestivalDTO> findSameSeasonFestivalBySeason(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<FestivalDTO> findSameManageFestivalByManageInstitution(FestivalDTO festivalDTO) throws Exception;
	
	String findFestivalYoutube(int page, String formattedStart, String festivalName) throws Exception;
	
	NaverArticleDTO findFestivalNaverArticle( FestivalDTO festivalDTO) throws Exception;
	
	NaverBlogDTO findFestivalNaverBlog(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<FestivalDTO> findSameKeywordFestivalByfestivalId(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception;
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findPopularFestivalKeyword(FestivalContentReviewNaverKeywordMapDTO mapDTO) throws Exception;
	
	void insertUserSelectKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception;
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findUserLoveKeywordList(int userId) throws Exception;
	
	ArrayList<FestivalDTO> findSameKeywordFestivalByKeywordId(String festivalKeywordId) throws Exception;
	
	ArrayList<FestivalDTO> findLoveHateFestivalList(UserFestivalLoveHateMapDTO mapDTO) throws Exception;
	
	ArrayList<VWUserReviewDataDTO> findFestivalReviewListByUserId(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception;
	
	int findUserReviewCountByUserReviewDataDTO(VWUserReviewDataDTO vwUserReviewDataDTO) throws Exception;
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findFestivalBigLoveHateKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception;
	
	ArrayList<FestivalContentReviewNaverKeywordMapDTO> findFestivalSmallLoveHateKeyword(UserFestivalLoveHateMapDTO mapDTO) throws Exception;
}
