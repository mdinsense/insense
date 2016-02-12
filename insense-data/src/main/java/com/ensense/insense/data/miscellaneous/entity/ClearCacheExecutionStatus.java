package com.ensense.insense.data.miscellaneous.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ClearCacheExecutionStatus")
public class ClearCacheExecutionStatus {
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "clearCacheExecutionStatus")
	private int clearCacheExecutionStatus;
	
	@Column(name = "clearCacheId")
	private int clearCacheId;
	
	@Column(name = "runDate")
	private Date runDate;
	
	@Column(name = "executionStatus")
	private String executionStatus;

	public int getClearCacheExecutionStatus() {
		return clearCacheExecutionStatus;
	}

	public void setClearCacheExecutionStatus(int clearCacheExecutionStatus) {
		this.clearCacheExecutionStatus = clearCacheExecutionStatus;
	}

	public int getClearCacheId() {
		return clearCacheId;
	}

	public void setClearCacheId(int clearCacheId) {
		this.clearCacheId = clearCacheId;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	@Override
	public String toString() {
		return "ClearCacheExecutionStatus [clearCacheExecutionStatus="
				+ clearCacheExecutionStatus + ", clearCacheId=" + clearCacheId
				+ ", runDate=" + runDate + ", executionStatus="
				+ executionStatus + "]";
	}
}
