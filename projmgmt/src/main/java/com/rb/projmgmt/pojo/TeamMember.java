package com.rb.projmgmt.pojo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TeamMember {
	
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public TeamMember(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.endDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	

}
