package com.multi.culture_link.culturalProperties.model.dto;

import lombok.Data;

@Data
public class CulturalPropertiesReviewDTO {

//	id INT AUTO_INCREMENT PRIMARY KEY,
//	cultural_properties_id INT not null,
//	user_id INT not null,
//	star INT not null,
//	content varchar(500),


	private int id;
	private int culturalPropertiesId;
	private int userId;
	private int star;
	private String content;

	private String userName;
	private String userProfileImage;

}
	
	
