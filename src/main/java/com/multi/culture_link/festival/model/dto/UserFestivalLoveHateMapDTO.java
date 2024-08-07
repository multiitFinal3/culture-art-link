package com.multi.culture_link.festival.model.dto;

import lombok.Data;

@Data
public class UserFestivalLoveHateMapDTO {

	private int userId;
	private String sortCode;
	private int festivalId;
	private String festivalKeywordId;
	private int festivalCount;
	private PageDTO pageDTO;
	

}
