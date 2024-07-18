package com.multi.culture_link.admin.festival.model.mapper;

import com.multi.culture_link.festival.model.dto.FestivalDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminFestivalMapper {
	
	
	
	
	void insertAPIFestival(FestivalDTO festivalDTO) throws Exception;
}
