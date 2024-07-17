package com.multi.culture_link.admin.festival.service;

import com.multi.culture_link.festival.model.dto.FestivalDTO;

import java.util.ArrayList;

public interface AdminFestivalService {
	ArrayList<FestivalDTO> findAPIFestivalList(int page) throws Exception;
}
