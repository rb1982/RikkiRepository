package com.rb.projmgmt.pojo;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class Analysis {
	
	private String ticketId;
	private Set<String> errorMsg = new TreeSet<>();
	private double effortRequirement;
	private double effortDesign;
	private double effortCoding;
	private double effortCodeReview;
	private double effortUt;
	private double effortUtReview;
	private double effortQaSupport;
	private double effortRework;
	private double effortLeaves;
	private double effortCompanyHolidays;
	private double effortNonProject;
	private double effortPm;
	private double effortBau;
	private double effortSupport;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private String type;
	private String owner;
	
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public double getEffortSupport() {
		return effortSupport;
	}
	public void addEffortSupport(double effortSupport) {
		this.effortSupport += effortSupport;
	}
	public double getEffortBau() {
		return effortBau;
	}
	public void addEffortBau(double effortBau) {
		this.effortBau += effortBau;
	}
	public double getEffortPm() {
		return effortPm;
	}
	public void addEffortPm(double effortPm) {
		this.effortPm += effortPm;
	}
	public double getEffortNonProject() {
		return effortNonProject;
	}
	public void addEffortNonProject(double effortNonProject) {
		this.effortNonProject += effortNonProject;
	}
	public double getEffortCompanyHolidays() {
		return effortCompanyHolidays;
	}
	public void addEffortCompanyHolidays(double effortCompanyHolidays) {
		this.effortCompanyHolidays += effortCompanyHolidays;
	}
	public double getEffortLeaves() {
		return effortLeaves;
	}
	public void addEffortLeaves(double effortLeaves) {
		this.effortLeaves += effortLeaves;
	}
	
	public double getEffortRework() {
		return effortRework;
	}
	public void addEffortRework(double effortRework) {
		this.effortRework += effortRework;
	}
	public double getEffortQaSupport() {
		return effortQaSupport;
	}
	public void addEffortQaSupport(double effortQaSupport) {
		this.effortQaSupport += effortQaSupport;
	}
	public double getEffortUtReview() {
		return effortUtReview;
	}
	public void addEffortUtReview(double effortUtReview) {
		this.effortUtReview += effortUtReview;
	}
	public double getEffortUt() {
		return effortUt;
	}
	public void addEffortUt(double effortUt) {
		this.effortUt += effortUt;
	}
	public double getEffortCodeReview() {
		return effortCodeReview;
	}
	public void addEffortCodeReview(double effortCodeReview) {
		this.effortCodeReview += effortCodeReview;
	}
	public Analysis(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getTicketId() {
		return ticketId;
	}
	public double getEffortRequirement() {
		return effortRequirement;
	}
	public void addEffortRequirement(double effortRequirement) {
		this.effortRequirement += effortRequirement;
	}
	public String getErrorMsg() {
		String msg = "";
		if (errorMsg.size() > 0) {
			msg = errorMsg.toString();
		}
		return msg;
	}
	public void addErrorMsg(String errorMsg) {
		this.errorMsg.add(errorMsg);
	}
	public double getEffortDesign() {
		return effortDesign;
	}
	public void addEffortDesign(double effortDesign) {
		this.effortDesign += effortDesign;
	}
	public double getEffortCoding() {
		return effortCoding;
	}
	public void addEffortCoding(double effortCoding) {
		this.effortCoding += effortCoding;
	}

	
}
