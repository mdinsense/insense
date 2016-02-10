package com.ensense.insense.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UsageReport")
public class UsageReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="reportId")
	private int reportId;
	
	@Column(name="referenceId", nullable=false)
	private int referenceId;
	
	@Column(name = "solutionTypeId", nullable=false)
	private int solutionTypeId;
	
	@Column(name = "applicationId", nullable=false)
	private int applicationId;
	
	@Column(name = "environmentId", nullable=false)
	private int environmentId;

	@Column(name = "groupId", nullable=false)
	private int groupId;

	@Column(name = "userId", nullable=false)
	private int userId;	
	
	@Column(name = "startDateTime", nullable=false)
	private Date startDateTime;

	@Column(name = "endDateTime", nullable=false)
	private Date endDateTime;

	@Column(name="notes")
	private String 	notes;

	@Column(name = "functionalityTypeId", nullable=false)
	private int functionalityTypeId;

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public int getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getFunctionalityTypeId() {
		return functionalityTypeId;
	}

	public void setFunctionalityTypeId(int functionalityTypeId) {
		this.functionalityTypeId = functionalityTypeId;
	}

	public int getSolutionTypeId() {
		return solutionTypeId;
	}

	public void setSolutionTypeId(int solutionTypeId) {
		this.solutionTypeId = solutionTypeId;
	}	
	 
}
