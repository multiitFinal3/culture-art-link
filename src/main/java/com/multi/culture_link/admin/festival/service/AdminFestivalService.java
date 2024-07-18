package com.multi.culture_link.admin.festival.service;

import com.multi.culture_link.festival.model.dto.FestivalDTO;
import com.multi.culture_link.festival.model.dto.PageDTO;

import java.util.ArrayList;

public interface AdminFestivalService {
	ArrayList<FestivalDTO> findAPIFestivalList(int page) throws Exception;
	
	void insertAPIFestivalList(ArrayList<Integer> list) throws Exception;
	
	ArrayList<FestivalDTO> findDBFestivalList(PageDTO pageDTO) throws Exception;
	
	int findDBFestivalCount() throws Exception;
}
