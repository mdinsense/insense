package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "ScheduleExecution")
public class ScheduleExecution implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "scheduleExecutionId", nullable = false)
	private Integer scheduleExecutionId;
	
	@Column(name = "baselineScheduleExecutionId")
	private Integer baselineScheduleExecutionId;
	
	@Column(name = "testExecutionStatusRefId")
	private int testExecutionStatusRefId;
	
	@Column(name="testExecutionStartTime")
	private Date testExecutionStartTime;
	
	@Column(name="testExecutionEndTime")
	private Date testExecutionEndTime;
	
	@Column(name="dateCreated")
	private Date dateCreated;
	
	@Column(name="dateModified")
	private Date dateModified;
	
	@Column(name = "scheduleId", nullable = false)
	private Integer scheduleId;
	
	@Column(name="crawlStatusDirectory")
	private String crawlStatusDirectory;
	
	@Column(name="harReportsDirectory")
	private String harReportsDirectory;
	
	@Column(name="reportsDirectory")
	private String reportsDirectory;
	
	//Reports Start
	@Column(name="reportStatusRefId")
	private Integer reportStatusRefId;
	
	@Column(name="analyticsReportStatusId")
	private Integer analyticsReportStatusId;
	
	@Column(name="analyticsReportGenStartTime")
	private Date analyticsReportGenStartTime;
	
	@Column(name="analyticsReportGenEndTime")
	private Date analyticsReportGenEndTime;
	
	@Column(name="textOrImageReportGenEndTime")
	private Date textOrImageReportGenEndTime;
	
	@Column(name="textOrImageReportStatusId")
	private Integer textOrImageReportStatusId;
	
	

	@Column(name="textOrImageReportGenStartTime")
	private Date textOrImageReportGenStartTime;
	
	@Column(name="brokenUrlReportStatusId")
	private Integer brokenUrlReportStatusId;
	
	@Column(name="brokenUrlReportGenStartTime")
	private String brokenUrlReportGenStartTime;
	
	@Column(name="brokenUrlReportGenEndTime")
	private String brokenUrlReportGenEndTime;
	
	@Column(name="textMatchingReportStatusId")
	private Integer textMatchingReportStatusId;
	
	//Reports End
	
	//Comparison Starts
	@Column(name="comparisonStatusRefId")
	private Integer comparisonStatusRefId;
	
	@Column(name="textComparisonStatusId")
	private Integer textComparisonStatusId;
	
	@Column(name="textComparisonStartTime")
	private String textComparisonStartTime;
	
	@Column(name="textComparisonEndTime")
	private String textComparisonEndTime;
	
	@Column(name="htmlComparisonStatusId")
	private Integer htmlComparisonStatusId;
	
	@Column(name="htmlComparisonStartTime")
	private String htmlComparisonStartTime;
	
	@Column(name="htmlComparisonEndTime")
	private String htmlComparisonEndTime;
	
	@Column(name="imageComparisonStatusId")
	private Integer imageComparisonStatusId;
	
	@Column(name="imageComparisonStartTime")
	private String imageComparisonStartTime;
	
	@Column(name="imageComparisonEndTime")
	private String imageComparisonEndTime;
	//Comparison Ends
	
	
	
	public ScheduleExecution(){
		
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "scheduleExecutionId", insertable = false, updatable = false, nullable = true, unique = false)
	private List<ScheduleScriptXref> scheduleScriptXrefList;
	
	public String getCrawlStatusDirectory() {
		return crawlStatusDirectory;
	}

	public Integer getBaselineScheduleExecutionId() {
		return baselineScheduleExecutionId;
	}

	public void setBaselineScheduleExecutionId(Integer baselineScheduleExecutionId) {
		this.baselineScheduleExecutionId = baselineScheduleExecutionId;
	}

	public void setCrawlStatusDirectory(String crawlStatusDirectory) {
		this.crawlStatusDirectory = crawlStatusDirectory;
	}

	public Integer getScheduleExecutionId() {
		return scheduleExecutionId;
	}

	public void setScheduleExecutionId(Integer scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}

	public int getTestExecutionStatusRefId() {
		return testExecutionStatusRefId;
	}

	public void setTestExecutionStatusRefId(int testExecutionStatusRefId) {
		this.testExecutionStatusRefId = testExecutionStatusRefId;
	}

	public Date getTestExecutionStartTime() {
		return testExecutionStartTime;
	}

	public void setTestExecutionStartTime(Date testExecutionStartTime) {
		this.testExecutionStartTime = testExecutionStartTime;
	}

	public Date getTestExecutionEndTime() {
		return testExecutionEndTime;
	}
	/*
    @PrePersist
    protected void onCreate() {
    	testExecutionStartTime = new Date();
    }
    
	@PreUpdate
    protected void onUpdate() {
		testExecutionEndTime = new Date();
    }
	*/
	public void setTestExecutionEndTime(Date testExecutionEndTime) {
		this.testExecutionEndTime = testExecutionEndTime;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getAnalyticsReportStatusId() {
		return analyticsReportStatusId;
	}

	public void setAnalyticsReportStatusId(Integer analyticsReportStatusId) {
		this.analyticsReportStatusId = analyticsReportStatusId;
	}

	public Date getAnalyticsReportGenStartTime() {
		return analyticsReportGenStartTime;
	}

	public void setAnalyticsReportGenStartTime(Date analyticsReportGenStartTime) {
		this.analyticsReportGenStartTime = analyticsReportGenStartTime;
	}

	public Date getAnalyticsReportGenEndTime() {
		return analyticsReportGenEndTime;
	}

	public void setAnalyticsReportGenEndTime(Date analyticsReportGenEndTime) {
		this.analyticsReportGenEndTime = analyticsReportGenEndTime;
	}

	public Integer getBrokenUrlReportStatusId() {
		return brokenUrlReportStatusId;
	}

	public void setBrokenUrlReportStatusId(Integer brokenUrlReportStatusId) {
		this.brokenUrlReportStatusId = brokenUrlReportStatusId;
	}

	public String getBrokenUrlReportGenStartTime() {
		return brokenUrlReportGenStartTime;
	}

	public void setBrokenUrlReportGenStartTime(String brokenUrlReportGenStartTime) {
		this.brokenUrlReportGenStartTime = brokenUrlReportGenStartTime;
	}

	public String getBrokenUrlReportGenEndTime() {
		return brokenUrlReportGenEndTime;
	}

	public void setBrokenUrlReportGenEndTime(String brokenUrlReportGenEndTime) {
		this.brokenUrlReportGenEndTime = brokenUrlReportGenEndTime;
	}

	public String getTextComparisonStartTime() {
		return textComparisonStartTime;
	}

	public void setTextComparisonStartTime(String textComparisonStartTime) {
		this.textComparisonStartTime = textComparisonStartTime;
	}

	public String getTextComparisonEndTime() {
		return textComparisonEndTime;
	}

	public void setTextComparisonEndTime(String textComparisonEndTime) {
		this.textComparisonEndTime = textComparisonEndTime;
	}

	public String getHtmlComparisonStartTime() {
		return htmlComparisonStartTime;
	}

	public void setHtmlComparisonStartTime(String htmlComparisonStartTime) {
		this.htmlComparisonStartTime = htmlComparisonStartTime;
	}

	public String getHtmlComparisonEndTime() {
		return htmlComparisonEndTime;
	}

	public void setHtmlComparisonEndTime(String htmlComparisonEndTime) {
		this.htmlComparisonEndTime = htmlComparisonEndTime;
	}

	public String getImageComparisonStartTime() {
		return imageComparisonStartTime;
	}

	public void setImageComparisonStartTime(String imageComparisonStartTime) {
		this.imageComparisonStartTime = imageComparisonStartTime;
	}

	public String getImageComparisonEndTime() {
		return imageComparisonEndTime;
	}

	public void setImageComparisonEndTime(String imageComparisonEndTime) {
		this.imageComparisonEndTime = imageComparisonEndTime;
	}

	public Integer getTextMatchingReportStatusId() {
		return textMatchingReportStatusId;
	}

	public void setTextMatchingReportStatusId(Integer textMatchingReportStatusId) {
		this.textMatchingReportStatusId = textMatchingReportStatusId;
	}

	public Integer getReportStatusRefId() {
		return reportStatusRefId;
	}

	public void setReportStatusRefId(Integer reportStatusRefId) {
		this.reportStatusRefId = reportStatusRefId;
	}

	public Integer getComparisonStatusRefId() {
		return comparisonStatusRefId;
	}

	public void setComparisonStatusRefId(Integer comparisonStatusRefId) {
		this.comparisonStatusRefId = comparisonStatusRefId;
	}

	public Integer getTextComparisonStatusId() {
		return textComparisonStatusId;
	}

	public void setTextComparisonStatusId(Integer textComparisonStatusId) {
		this.textComparisonStatusId = textComparisonStatusId;
	}

	public Integer getHtmlComparisonStatusId() {
		return htmlComparisonStatusId;
	}

	public void setHtmlComparisonStatusId(Integer htmlComparisonStatusId) {
		this.htmlComparisonStatusId = htmlComparisonStatusId;
	}

	public Integer getImageComparisonStatusId() {
		return imageComparisonStatusId;
	}

	public void setImageComparisonStatusId(Integer imageComparisonStatusId) {
		this.imageComparisonStatusId = imageComparisonStatusId;
	}

	public String getHarReportsDirectory() {
		return harReportsDirectory;
	}

	public void setHarReportsDirectory(String harReportsDirectory) {
		this.harReportsDirectory = harReportsDirectory;
	}

	public String getReportsDirectory() {
		return reportsDirectory;
	}

	public void setReportsDirectory(String reportsDirectory) {
		this.reportsDirectory = reportsDirectory;
	}
	
	public List<ScheduleScriptXref> getScheduleScriptXrefList() {
		return scheduleScriptXrefList;
	}

	public void setScheduleScriptXrefList(List<ScheduleScriptXref> scheduleScriptXrefList) {
		this.scheduleScriptXrefList = scheduleScriptXrefList;
	}

	public Date getTextOrImageReportGenEndTime() {
		return textOrImageReportGenEndTime;
	}

	public void setTextOrImageReportGenEndTime(Date textOrImageReportGenEndTime) {
		this.textOrImageReportGenEndTime = textOrImageReportGenEndTime;
	}

	public Integer getTextOrImageReportStatusId() {
		return textOrImageReportStatusId;
	}

	public void setTextOrImageReportStatusId(Integer textOrImageReportStatusId) {
		this.textOrImageReportStatusId = textOrImageReportStatusId;
	}

	public Date getTextOrImageReportGenStartTime() {
		return textOrImageReportGenStartTime;
	}

	public void setTextOrImageReportGenStartTime(Date textOrImageReportGenStartTime) {
		this.textOrImageReportGenStartTime = textOrImageReportGenStartTime;
	}

	@Override
	public String toString() {
		return "ScheduleExecution [scheduleExecutionId=" + scheduleExecutionId
				+ ", baselineScheduleExecutionId="
				+ baselineScheduleExecutionId + ", testExecutionStatusRefId="
				+ testExecutionStatusRefId + ", testExecutionStartTime="
				+ testExecutionStartTime + ", testExecutionEndTime="
				+ testExecutionEndTime + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + ", scheduleId="
				+ scheduleId + ", crawlStatusDirectory=" + crawlStatusDirectory
				+ ", harReportsDirectory=" + harReportsDirectory
				+ ", reportsDirectory=" + reportsDirectory
				+ ", reportStatusRefId=" + reportStatusRefId
				+ ", analyticsReportStatusId=" + analyticsReportStatusId
				+ ", analyticsReportGenStartTime="
				+ analyticsReportGenStartTime + ", analyticsReportGenEndTime="
				+ analyticsReportGenEndTime + ", textOrImageReportGenEndTime="
				+ textOrImageReportGenEndTime + ", textOrImageReportStatusId="
				+ textOrImageReportStatusId
				+ ", textOrImageReportGenStartTime="
				+ textOrImageReportGenStartTime + ", brokenUrlReportStatusId="
				+ brokenUrlReportStatusId + ", brokenUrlReportGenStartTime="
				+ brokenUrlReportGenStartTime + ", brokenUrlReportGenEndTime="
				+ brokenUrlReportGenEndTime + ", textMatchingReportStatusId="
				+ textMatchingReportStatusId + ", comparisonStatusRefId="
				+ comparisonStatusRefId + ", textComparisonStatusId="
				+ textComparisonStatusId + ", textComparisonStartTime="
				+ textComparisonStartTime + ", textComparisonEndTime="
				+ textComparisonEndTime + ", htmlComparisonStatusId="
				+ htmlComparisonStatusId + ", htmlComparisonStartTime="
				+ htmlComparisonStartTime + ", htmlComparisonEndTime="
				+ htmlComparisonEndTime + ", imageComparisonStatusId="
				+ imageComparisonStatusId + ", imageComparisonStartTime="
				+ imageComparisonStartTime + ", imageComparisonEndTime="
				+ imageComparisonEndTime + ", scheduleScriptXrefList="
				+ scheduleScriptXrefList + "]";
	}
	
}
