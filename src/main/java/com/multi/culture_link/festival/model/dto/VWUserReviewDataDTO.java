package com.multi.culture_link.festival.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class VWUserReviewDataDTO {
	
	private int festivalReviewId;
	private int userId;
	private int festivalId;
	private int festivalReviewStar;
	private String festivalReviewContent;
	private String attachment;
	private String userName;
	private String userProfilePic;
	private PageDTO pageDTO;
	private Date createdAt;
	private Date updatedAt;
	
}
