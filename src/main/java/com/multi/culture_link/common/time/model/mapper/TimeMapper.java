package com.multi.culture_link.common.time.model.mapper;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TimeMapper {
	
	
	ArrayList<TimeDTO> findAllTime() throws Exception;
}
