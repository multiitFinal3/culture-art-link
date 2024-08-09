package com.multi.culture_link.culturalProperties.model.dto;

import lombok.Data;

import java.util.Date;


@Data
public class CulturalPropertiesReviewDTO {

//	id INT AUTO_INCREMENT PRIMARY KEY,
//	cultural_properties_id INT not null,
//	user_id INT not null,
//	star INT not null,
//	content varchar(500),
//created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//	updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


	private int id;
	private int culturalPropertiesId;
	private int userId;
	private int star;
	private String content;

	private Date createdAt;
	private Date updatedAt;

	private String userName;
	private String userProfileImage;

	private double averageRating;


}
	
	
