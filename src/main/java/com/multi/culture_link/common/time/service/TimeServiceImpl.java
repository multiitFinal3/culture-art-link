package com.multi.culture_link.common.time.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.common.time.model.mapper.TimeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 시간대 관련 서비스 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Service
public class TimeServiceImpl implements TimeService {
	
	private final TimeMapper timeMapper;
	
	/**
	 * 타임 매퍼를 생성자 주입
	 * @param timeMapper 타임 매퍼
	 */
	public TimeServiceImpl(TimeMapper timeMapper) {
		this.timeMapper = timeMapper;
	}
	
	/**
	 * 모든 시간을 찾아서 반환
	 * @return 시간DTO 전부 반환
	 * @throws Exception 컨트롤러로 예외 던짐
	 */
	@Override
	public ArrayList<TimeDTO> findAllTime() throws Exception {
		
		ArrayList<TimeDTO> list = timeMapper.findAllTime();
		return list;
		
	}
	
	
}
