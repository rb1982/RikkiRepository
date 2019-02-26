package com.rb.projmgmt.pojo;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public class Properties {
	
	@Value("#{'${effort.requirement.activities}'.split(',')}") 
	private List<String> requirementActivities;
	
	@Value("#{'${effort.design.activities}'.split(',')}") 
	private List<String> designActivities;
	
	@Value("#{'${effort.coding.activities}'.split(',')}") 
	private List<String> codingActivities;
	
	@Value("#{'${effort.codereview.activities}'.split(',')}") 
	private List<String> codeReviewActivities;
	
	@Value("#{'${effort.ut.activities}'.split(',')}") 
	private List<String> utActivities;
	
	@Value("#{'${effort.utreview.activities}'.split(',')}") 
	private List<String> utReviewActivities;
	
	@Value("#{'${effort.qasupport.activities}'.split(',')}") 
	private List<String> qaSupportActivities;
	
	@Value("#{'${effort.rework.activities}'.split(',')}") 
	private List<String> reworkActivities;
	
	@Value("#{'${effort.leaves.activities}'.split(',')}") 
	private List<String> leavesActivities;
	
	@Value("#{'${effort.companyHoliday.activities}'.split(',')}") 
	private List<String> companyHolidayActivities;
	
	@Value("#{'${effort.nonProject.activities}'.split(',')}") 
	private List<String> nonProjectActivities;
	
	@Value("#{'${effort.pm.activities}'.split(',')}") 
	private List<String> pmActivities;
	
	@Value("#{'${effort.bau.activities}'.split(',')}") 
	private List<String> bauActivities;
	
	@Value("#{'${effort.support.activities}'.split(',')}") 
	private List<String> supportActivities;
	
	@Value("#{'${ticket.status.closed}'.split(',')}") 
	private List<String> closedStatuses;
	
	@Value("#{'${ticket.types.bau}'.split(',')}") 
	private List<String> bauTypes;
	
	@Value("#{'${ticket.types.support}'.split(',')}") 
	private List<String> supportTypes;
	
	@Value("#{'${ticket.redirected.old}'.split(',')}") 
	private List<String> redirectedOld;
	
	@Value("#{'${ticket.redirected.new}'.split(',')}") 
	private List<String> redirectedNew;
	
	public List<String> getRedirectedOld() {
		return redirectedOld;
	}

	public List<String> getRedirectedNew() {
		return redirectedNew;
	}

	public List<String> getRequirementActivities() {
		return requirementActivities;
	}
	
	public List<String> getDesignActivities() {
		return designActivities;
	}
	
	public List<String> getCodingActivities() {
		return codingActivities;
	}
	
	public List<String> getCodeReviewActivities() {
		return codeReviewActivities;
	}
	
	public List<String> getUtActivities() {
		return utActivities;
	}
	
	public List<String> getUtReviewActivities() {
		return utReviewActivities;
	}
	
	public List<String> getQaSupportActivities() {
		return qaSupportActivities;
	}
	
	public List<String> getReworkActivities() {
		return reworkActivities;
	}
	
	public List<String> getLeavesActivities() {
		return leavesActivities;
	}
	
	public List<String> getCompanyHolidayActivities() {
		return companyHolidayActivities;
	}
	
	public List<String> getNonProjectActivities() {
		return nonProjectActivities;
	}
	
	public List<String> getPmActivities() {
		return pmActivities;
	}
	
	public List<String> getBauActivities() {
		return bauActivities;
	}
	
	public List<String> getSupportActivities() {
		return supportActivities;
	}
	
	public List<String> getClosedStatuses() {
		return closedStatuses;
	}
	
	public List<String> getBauTypes() {
		return bauTypes;
	}
	
	public List<String> getSupportTypes() {
		return supportTypes;
	}
}
