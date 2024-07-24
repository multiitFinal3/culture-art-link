package com.multi.culture_link.festival.service;

import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;

import java.util.ArrayList;

public interface FestivalService {
	ArrayList<FestivalKeywordDTO> findFestivalKeywordByFestivalId(int festivalId) throws Exception;
	
	void insertUserLoveFestival(UserFestivalLoveHateMapDTO map1) throws Exception;
	
	UserFestivalLoveHateMapDTO findUserMapByUserMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void insertUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	void updateUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception;
	
	ArrayList<Integer> findLoveList(int userId) throws Exception;
}
