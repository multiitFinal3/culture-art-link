package com.multi.culture_link.festival.model.dto;


import lombok.Data;

import java.util.Date;

@Data
public class FestivalDTO {
	
	private int festivalId;
	private int regionId;
	private String regionName;
	private String timeId;
	private String festivalName;
	private String festivalContent;
	private String manageInstitution;
	private String hostInstitution;
	private String sponserInstitution;
	private String tel;
	private String homepageUrl;
	private String detailAddress;
	private double latitude;
	private double longtitude;
	private String place;
	private Date startDate;
	private Date endDate;
	private double avgRate;
	private String season;
	private String imgUrl;
	
	
}
