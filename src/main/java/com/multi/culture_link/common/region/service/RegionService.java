package com.multi.culture_link.common.region.service;

import com.multi.culture_link.common.region.model.dto.RegionDTO;

import java.util.ArrayList;

public interface RegionService {
	
	ArrayList<RegionDTO> findAllRegion() throws Exception;
	
}
