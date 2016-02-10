package com.ensense.insense.data.common.model;

import java.util.Arrays;

public class UsageReportForm {
	
	private String[] groupName;
	private String[] users;
	private String[] solutionType;
	private String[] functionality;
	private String[] environmentCategoryId;
	private String fromDate;
	private String toDate;
	private String notes;
	private Integer tab;
	private Integer criteriaId;
	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String[] getGroupName() {
		return groupName;
	}
	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}
	public String[] getUsers() {
		return users;
	}
	public void setUsers(String[] users) {
		this.users = users;
	}
	public String[] getSolutionType() {
		return solutionType;
	}
	public void setSolutionType(String[] solutionType) {
		this.solutionType = solutionType;
	}
	public String[] getFunctionality() {
		return functionality;
	}
	public void setFunctionality(String[] functionality) {
		this.functionality = functionality;
	}
	public String[] getEnvironmentCategoryId() {
		return environmentCategoryId;
	}
	public void setEnvironmentCategoryId(String[] environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Integer getTab() {
		return tab;
	}
	public void setTab(Integer tab) {
		this.tab = tab;
	}
	public Integer getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(Integer criteriaId) {
		this.criteriaId = criteriaId;
	}
	@Override
	public String toString() {
		return "UsageReportForm [groupName=" + Arrays.toString(groupName)
				+ ", users=" + Arrays.toString(users) + ", solutionType="
				+ Arrays.toString(solutionType) + ", functionality="
				+ Arrays.toString(functionality) + ", environmentCategoryId="
				+ Arrays.toString(environmentCategoryId) + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", notes=" + notes + "]";
	}
}
