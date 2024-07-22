package com.multi.culture_link.common.region.model.mapper;

import com.multi.culture_link.common.region.model.dto.RegionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface RegionMapper {
	
	ArrayList<RegionDTO> findAllRegion() throws Exception;
	
	
}
