package com.multi.culture_link.admin.festival.service;

import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;

import java.util.ArrayList;

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
}
