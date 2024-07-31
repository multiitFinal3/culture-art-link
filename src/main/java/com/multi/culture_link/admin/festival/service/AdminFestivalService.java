package com.multi.culture_link.admin.festival.service;

import com.multi.culture_link.festival.model.dto.FestivalContentReviewNaverKeywordMapDTO;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;

import java.util.ArrayList;
import java.util.HashMap;

public interface AdminFestivalService {
	ArrayList<FestivalDTO> findAPIFestivalList(int page) throws Exception;
	
	void insertAPIFestivalList(ArrayList<Integer> list) throws Exception;
	
	ArrayList<FestivalDTO> findDBFestivalList(PageDTO pageDTO) throws Exception;
	
	int findDBFestivalCount() throws Exception;
	
	void deleteDBFestivalList(ArrayList<Integer> checks) throws Exception;
	
	FestivalDTO findDBFestivalByFestivalId(int festivalId) throws Exception;
	
	void updateDBFestivalByFestival(FestivalDTO festivalDTO) throws Exception;
	
	
	ArrayList<FestivalDTO> findDBFestivalByMultiple(FestivalDTO festivalDTO) throws Exception;
	
	int findDBFestivalMultipleCount(FestivalDTO festivalDTO) throws Exception;
	
	ArrayList<FestivalDTO> findAPIFestivalByMultiple(FestivalDTO festivalDTO, String urls) throws Exception;
	
	int findAPIFestivalByMultipleCount(FestivalDTO festivalDTO, String urls) throws Exception;
	
	HashMap<String, Integer> findContentKeywordByFestivalId(int festivalId) throws Exception;
	
	FestivalKeywordDTO findKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception;
	
	void insertKeywordByKeyword(FestivalKeywordDTO keyword) throws Exception;
	
	FestivalContentReviewNaverKeywordMapDTO findKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping) throws Exception;
	
	void insertKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapDTO keywordMapping) throws Exception;
	
	HashMap<String, Integer> findNaverArticleKeywordByFestivalId(int festivalId) throws Exception;
}
