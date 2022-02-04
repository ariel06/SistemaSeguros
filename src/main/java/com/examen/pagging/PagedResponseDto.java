package com.examen.pagging;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


public class PagedResponseDto<T> {
	
	
	  private List<T> items;
	  
	  @Autowired
	  private PageDto pageDto;
	  
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public PageDto getPageDto() {
		return pageDto;
	}
	public void setPageDto(PageDto pageDto) {
		this.pageDto = pageDto;
	}
	  
	  
}
