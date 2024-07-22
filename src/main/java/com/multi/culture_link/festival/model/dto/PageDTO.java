package com.multi.culture_link.festival.model.dto;

import lombok.Data;

@Data
public class PageDTO {
	
	
	private int start;
	private int end;
	private int page;
	
	public void setStartEnd(int page) {
		
		this.start = (page - 1) * 5 + 1;
		this.end = page * 5;
		
		
	}
	
	
}
