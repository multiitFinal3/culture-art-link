package com.multi.culture_link.common.region.service;

import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.region.model.mapper.RegionMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegionServiceImpl implements RegionService {
	
	private final RegionMapper regionMapper;
	
	public RegionServiceImpl(RegionMapper regionMapper) {
		this.regionMapper = regionMapper;
	}
	
	@Override
	public ArrayList<RegionDTO> findAllRegion() throws Exception {
		
		ArrayList<RegionDTO> list = regionMapper.findAllRegion();
		return list;
		
	}


}
