package com.multi.culture_link.admin.festival.model.mapper;

import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.festival.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface AdminFestivalMapper {
	
	
	void insertAPIFestival(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<FestivalDTO> findDBFestivalList(PageDTO pageDTO) throws Exception;
	
	int findDBFestivalCount() throws Exception;
	
	void deleteDBFestivalList(int festivalId) throws Exception;
	
	FestivalDTO findDBFestivalByFestivalId(int festivalId) throws Exception;
	
	void updateDBFestivalByFestival(FestivalDTO festivalDTO) throws Exception;
	
	FestivalDTO findDBFestivalByFestival(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<RegionDTO> findAllRegion() throws Exception;
	
	ArrayList<FestivalDTO> findDBFestivalByMultiple(FestivalDTO festivalDTO) throws Exception;
	
	int findDBFestivalMultipleCount(FestivalDTO festivalDTO) throws Exception;
	
	FestivalKeywordDTO findKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception;
	
	void insertKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception;
	
	FestivalContentReviewNaverKeywordMapDTO findKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping) throws Exception;
	
	void insertKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping) throws Exception;
	
	NaverArticleDTO findFestivalNaverUrlByNaverArticle(NaverArticleDTO naverArticleDTO) throws Exception;
	
	void insertFestivalNaverUrlMappingByNaverArticle(NaverArticleDTO naverArticleDTO) throws Exception;
	
	void updateKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping1) throws Exception;
	
	NaverBlogDTO findFestivalNaverUrlByNaverBlog(NaverBlogDTO naverBlogDTO) throws Exception;
	
	void insertFestivalNaverUrlMappingByNaverBlog(NaverBlogDTO naverBlogDTO) throws Exception;
}
