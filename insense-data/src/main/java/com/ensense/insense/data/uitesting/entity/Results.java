package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "Results")
public class Results implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "resultId")
	private int resultId;
	
	@Column(name = "scheduleId")
	private int scheduleId;
	
	@Column(name = "scheduleExecutionId")
	private int scheduleExecutionId;
	
	@Column(name = "testCaseId")
	private int testCaseId;
	
	@Column(name = "loginId")
	private int loginId;
	
	@Column(name = "executionStatusId")
	private int executionStatusId;
	
	@Column(name="snapShotPath")
	private String snapShotPath;
	
	@Column(name="generatedDate")
	private Date generatedDate;
	
	@Column(name = "executionStatusRefId")
	private int executionStatusRefId;
	
	@Column(name="harReportsPath")
	private String harReportsPath;
	
	@Column(name="textFilePath")
	private String textFilePath;
	
	@Column(name="executionTimestamp")
	private String executionTimestamp;
	
	public String getExecutionTimestamp() {
		return executionTimestamp;
	}

	public void setExecutionTimestamp(String executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}

	public String getTextFilePath() {
		return textFilePath;
	}

	public void setTextFilePath(String textFilePath) {
		this.textFilePath = textFilePath;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

	public int getExecutionStatusId() {
		return executionStatusId;
	}

	public void setExecutionStatusId(int executionStatusId) {
		this.executionStatusId = executionStatusId;
	}

	public String getSnapShotPath() {
		return snapShotPath;
	}

	public void setSnapShotPath(String snapShotPath) {
		this.snapShotPath = snapShotPath;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public int getExecutionStatusRefId() {
		return executionStatusRefId;
	}

	public void setExecutionStatusRefId(int executionStatusRefId) {
		this.executionStatusRefId = executionStatusRefId;
	}

	public int getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(int testCaseId) {
		this.testCaseId = testCaseId;
	}

	
	public String getHarReportsPath() {
		return harReportsPath;
	}

	public void setHarReportsPath(String harReportsPath) {
		this.harReportsPath = harReportsPath;
	}

	@Override
	public String toString() {
		return "TestResult [resultId=" + resultId + ", scheduleId="
				+ scheduleId + ", testCaseId=" + testCaseId + ", loginId="
				+ loginId + ", executionStatusId=" + executionStatusId
				+ ", snapShotPath=" + snapShotPath + ", generatedDate="
				+ generatedDate + ", executionStatusRefId="
				+ executionStatusRefId + ", harReportsPath=" + harReportsPath
				+ ", textFilePath=" + textFilePath + ", executionTimestamp="
				+ executionTimestamp + "]";
	}

	public void setScheduleExecutionId(int scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}

	public int getScheduleExecutionId() {
		return scheduleExecutionId;
	}

}
