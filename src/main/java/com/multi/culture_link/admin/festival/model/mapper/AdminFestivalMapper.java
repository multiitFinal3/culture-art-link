package com.multi.culture_link.admin.festival.model.mapper;

import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.festival.model.dto.FestivalContentReviewNaverKeywordMapping;
import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;
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
	
	FestivalContentReviewNaverKeywordMapping findKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapping keywordMapping) throws Exception;
	
	void insertKeywordMappingByKeywordMapping(FestivalContentReviewNaverKeywordMapping keywordMapping) throws Exception;
}
