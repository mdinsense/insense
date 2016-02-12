package com.ensense.insense.data.uitesting.entity;

import com.ensense.insense.data.analytics.model.SummaryReport;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.HashMap;

/**
 * The persistent class for the applications_table database table.
 * 
 */
@Entity
@Table(name="reports_table")
public class Reports implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="reportsId")
	int reportsId;
	
	@Column(name="scheduleId")
	int scheduleId;
	
	@Column(name = "scheduleExecutionId")
	private int scheduleExecutionId;
	
	@Column(name="generatedDate")
	Date generatedDate;
	
	public int getScheduleExecutionId() {
		return scheduleExecutionId;
	}

	public void setScheduleExecutionId(int scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}

	@Column(name="generationStartTime")
	Date generationStartTime;
	
	@Column(name="generationEndTime")
	Date generationEndTime;
	
	@Lob
	@Column(name="resultsFileContent")
	Clob resultsFileContent;

	@Column(name = "resultId")
	private int resultId;
	
	@Column(name = "baselineResultId")
	private int baselineResultId;
	
	@Column(name = "executionStatusRefId")
	private int executionStatusRefId;
	
	@Column(name="summaryReportObj", columnDefinition="blob")
	private HashMap<String, SummaryReport> summaryReportObj;
	
	@Transient
	private String SuitNameWithDt;
	
	public String getSuitNameWithDt() {
		return SuitNameWithDt;
	}

	public void setSuitNameWithDt(String suitNameWithDt) {
		SuitNameWithDt = suitNameWithDt;
	}

	public HashMap<String, SummaryReport> getSummaryReportObj() {
		return summaryReportObj;
	}

	public void setSummaryReportObj(HashMap<String, SummaryReport> summaryReportObj) {
		this.summaryReportObj = summaryReportObj;
	}

	public int getReportsId() {
		return reportsId;
	}

	public void setReportsId(int reportsId) {
		this.reportsId = reportsId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public int getBaselineResultId() {
		return baselineResultId;
	}

	public void setBaselineResultId(int baselineResultId) {
		this.baselineResultId = baselineResultId;
	}

	public int getExecutionStatusRefId() {
		return executionStatusRefId;
	}

	public void setExecutionStatusRefId(int executionStatusRefId) {
		this.executionStatusRefId = executionStatusRefId;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}


	public Date getGenerationStartTime() {
		return generationStartTime;
	}

	public void setGenerationStartTime(Date generationStartTime) {
		this.generationStartTime = generationStartTime;
	}

	public Date getGenerationEndTime() {
		return generationEndTime;
	}

	public void setGenerationEndTime(Date generationEndTime) {
		this.generationEndTime = generationEndTime;
	}

	public Clob getResultsFileContent() {
		return resultsFileContent;
	}

	public void setResultsFileContent(Clob resultsFileContent) {
		this.resultsFileContent = resultsFileContent;
	}

	@Override
	public String toString() {
		return "Reports [reportsId=" + reportsId + ", scheduleId=" + scheduleId
				+ ", generatedDate=" + generatedDate + ", generationStartTime="
				+ generationStartTime + ", generationEndTime="
				+ generationEndTime + ", resultsFileContent="
				+ resultsFileContent + ", resultId=" + resultId
				+ ", baselineResultId=" + baselineResultId
				+ ", executionStatusRefId=" + executionStatusRefId
				+ ", summaryReportObj=" + summaryReportObj
				+ ", SuitNameWithDt=" + SuitNameWithDt + "]";
	}

}
