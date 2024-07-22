package com.multi.culture_link.common.time.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;

import java.util.ArrayList;

public interface TimeService {
	
	ArrayList<TimeDTO> findAllTime() throws Exception;
	
}
