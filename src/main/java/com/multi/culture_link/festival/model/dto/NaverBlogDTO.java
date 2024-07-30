package com.multi.culture_link.festival.model.dto;


import lombok.Data;

import java.util.Date;

@Data
public class NaverBlogDTO {
	
	private String title;
	private String link;
	private String description;
	private String imgUrl;
	private String bloggerName;
	private String totalContent;
	private Date postDate;
	
	
}
