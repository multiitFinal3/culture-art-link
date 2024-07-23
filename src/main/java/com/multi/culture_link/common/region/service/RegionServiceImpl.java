package com.multi.culture_link.common.region.service;

import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.model.mapper.RegionMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegionServiceImpl implements RegionService {
	
	private final RegionMapper regionMapper;
	
	/**
	 * 리전 매퍼 생성자 주입
	 * @param regionMapper
	 */
	public RegionServiceImpl(RegionMapper regionMapper) {
		this.regionMapper = regionMapper;
	}
	
	/**
	 * 모든 지역을 반환
	 * @return 리전 DTO 리스트 반환
	 * @throws Exception 컨트롤러로 예외 던짐
	 */
	@Override
	public ArrayList<RegionDTO> findAllRegion() throws Exception {
		
		ArrayList<RegionDTO> list = regionMapper.findAllRegion();
		return list;
		
	}


}
