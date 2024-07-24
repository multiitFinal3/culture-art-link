package com.multi.culture_link.festival.model.mapper;

import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface FestivalMapper {
	
	
	ArrayList<FestivalKeywordDTO> findFestivalKeywordByFestivalId(int festivalId) throws Exception;
	
	void insertUserLoveFestival(UserFestivalLoveHateMapDTO map1) throws Exception;
	
	UserFestivalLoveHateMapDTO findUserMapByUserMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void insertUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void updateUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	ArrayList<Integer> findLoveList(int userId) throws Exception;
	
	void deleteUserLoveFestival(UserFestivalLoveHateMapDTO userMap1) throws Exception;
	
	void deleteUserKeywordMap(UserFestivalLoveHateMapDTO userMap2) throws Exception;
}
