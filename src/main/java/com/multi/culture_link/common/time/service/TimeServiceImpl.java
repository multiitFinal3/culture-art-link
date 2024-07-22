package com.multi.culture_link.common.time.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.common.time.model.mapper.TimeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TimeServiceImpl implements TimeService {
	
	private final TimeMapper timeMapper;
	
	public TimeServiceImpl(TimeMapper timeMapper) {
		this.timeMapper = timeMapper;
	}
	
	@Override
	public ArrayList<TimeDTO> findAllTime() throws Exception {
		
		ArrayList<TimeDTO> list = timeMapper.findAllTime();
		return list;
		
	}
	
	
}
