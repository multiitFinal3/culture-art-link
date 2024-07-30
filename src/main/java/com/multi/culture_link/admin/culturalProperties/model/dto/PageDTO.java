package com.multi.culture_link.admin.culturalProperties.model.dto;

import lombok.Data;

@Data
public class PageDTO{
	
	private int start;
	private int end;
	private int page;
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public void setStartEnd(int page) {
		
		start = 1 + (page - 1) * 10;
		//1page: 1 + 0 * 10 => start 1
		//2page: 1 + 1 * 10 => start 11  
		end = page * 10;
		//1page: 1 * 10 => end 10
		//2page: 2 * 10 => end 20
	}

	
}
	
	
