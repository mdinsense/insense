package com.ensense.insense.data.common.model;

public class UsageReportResult {
	
	private int slno;
	private String applicationName;
	private String environmentCategoryName;
	private String groupName;
	private String userName;
	private String solutionTypeName;
	private String functionalityName;
	private String startDate;
	private String endDate;
	private String notes;
	
	public int getSlno() {
		return slno;
	}
	public void setSlno(int slno) {
		this.slno = slno;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getEnvironmentCategoryName() {
		return environmentCategoryName;
	}
	public void setEnvironmentCategoryName(String environmentCategoryName) {
		this.environmentCategoryName = environmentCategoryName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSolutionTypeName() {
		return solutionTypeName;
	}
	public void setSolutionTypeName(String solutionTypeName) {
		this.solutionTypeName = solutionTypeName;
	}
	public String getFunctionalityName() {
		return functionalityName;
	}
	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "UsageReportResult [slno=" + slno + ", applicationName="
				+ applicationName + ", environmentCategoryName="
				+ environmentCategoryName + ", groupName=" + groupName
				+ ", userName=" + userName + ", solutionTypeName="
				+ solutionTypeName + ", functionalityName=" + functionalityName
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", notes=" + notes + "]";
	}
	
}
