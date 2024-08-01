package com.multi.culture_link.festival.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NaverArticleDTO {

	private int festivalId;
	private String title;
	private String originalLink;
	private String description;
	private String imgUrl;
	private String totalContent;
	private Date pubDate;
	private int display;


}
