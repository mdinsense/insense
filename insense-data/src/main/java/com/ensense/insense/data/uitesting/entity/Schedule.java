package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the test_schedule_table database table.
 * 
 */
@Entity
@Table(name="Schedule")
public class Schedule implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "scheduleId")
	private Integer scheduleId;
	
	@Column(name = "suitId")
	private Integer suitId;
	
	@Column(name = "applicationId")
	private Integer applicationId;

	@Column(name = "environmentId")
	private Integer environmentId;

	@Column(name = "environmentCategoryId ")
	private Integer environmentCategoryId;
	
	@Column(name = "loginUserDetailId")
	private Integer loginUserDetailId;

	@Column(name = "browserTypeId")
	private Integer browserTypeId;
	
	@Column(name = "solutionTypeId")
	private Integer solutionTypeId;
	
	@Column(name="moduleIds")
	private String moduleIds;
	
	@Column(name="textCompare")
	private boolean textCompare;
	
	@Column(name="htmlCompare")
	private boolean htmlCompare;
	
	@Column(name="screenCompare ")
	private boolean screenCompare;

	@Column(name = "analyticsTesting")
	private boolean analyticsTesting;

	@Column(name = "robotClicking")
	private boolean robotClicking;

	@Column(name = "brandingUrlReport")
	private boolean brandingUrlReport;

	@Column(name = "brokenUrlReport")
	private boolean brokenUrlReport;

	@Column(name = "processChildUrls")
	private boolean processChildUrls;
	
	@Column(name="regressionTesting")
	private boolean regressionTesting;

	@Column(name = "stopAfter")
	private int stopAfter;

	@Column(name = "akamai_testing")
	private boolean akamaiTesting;

	@Column(name = "emailOnCompletion")
	private boolean emailOnCompletion;

	@Column(name = "emailOnFailure")
	private boolean emailOnFailure;

	@Column(name = "emailIds")
	private String emailIds;
	
	@Column(name = "recordStatus")
	private String recordStatus;

	@Column(name = "userId")
	private int userId;
	
	@Column(name="isTransactionTestcase")
	private boolean isTransactionTestcase;
	
	@Column(name="startTime")
	private Time startTime;
	
	
	@Column(name="startDate")
	private Date startDate;
	
	@Column(name="isScheduled")
	private boolean isScheduled;
	
	@Column(name="recurrence")
	private boolean recurrence;
	
	@Column(name="scheduleType ")
	private char scheduleType;
	
	@Column(name="recurrenceDateWise")
	private Date recurrenceDateWise;
	
	@Column(name="baselineScheduleExecutionId")
	private Integer baselineScheduleExecutionId;
	
	@Column(name="scheduleDDay")
	private String 	scheduleDDay;
	
	@Column(name="notes")
	private String 	notes;
	
	@Column(name = "urlLevel")
	private int urlLevel;
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  
	
	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getSuitId() {
		return suitId;
	}

	public void setSuitId(Integer suitId) {
		this.suitId = suitId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public Integer getLoginUserDetailId() {
		return loginUserDetailId;
	}

	public void setLoginUserDetailId(Integer loginUserDetailId) {
		this.loginUserDetailId = loginUserDetailId;
	}

	public Integer getBrowserTypeId() {
		return browserTypeId;
	}

	public void setBrowserTypeId(Integer browserTypeId) {
		this.browserTypeId = browserTypeId;
	}

	public Integer getSolutionTypeId() {
		return solutionTypeId;
	}

	public void setSolutionTypeId(Integer solutionTypeId) {
		this.solutionTypeId = solutionTypeId;
	}


	public boolean isTextCompare() {
		return textCompare;
	}

	public void setTextCompare(boolean textCompare) {
		this.textCompare = textCompare;
	}

	public boolean isHtmlCompare() {
		return htmlCompare;
	}

	public void setHtmlCompare(boolean htmlCompare) {
		this.htmlCompare = htmlCompare;
	}

	public boolean isScreenCompare() {
		return screenCompare;
	}

	public void setScreenCompare(boolean screenCompare) {
		this.screenCompare = screenCompare;
	}

	public boolean isAnalyticsTesting() {
		return analyticsTesting;
	}

	public void setAnalyticsTesting(boolean analyticsTesting) {
		this.analyticsTesting = analyticsTesting;
	}

	public boolean isRobotClicking() {
		return robotClicking;
	}

	public void setRobotClicking(boolean robotClicking) {
		this.robotClicking = robotClicking;
	}

	public boolean isBrandingUrlReport() {
		return brandingUrlReport;
	}

	public void setBrandingUrlReport(boolean brandingUrlReport) {
		this.brandingUrlReport = brandingUrlReport;
	}

	public boolean isBrokenUrlReport() {
		return brokenUrlReport;
	}

	public void setBrokenUrlReport(boolean brokenUrlReport) {
		this.brokenUrlReport = brokenUrlReport;
	}

	public boolean isProcessChildUrls() {
		return processChildUrls;
	}

	public void setProcessChildUrls(boolean processChildUrls) {
		this.processChildUrls = processChildUrls;
	}

	public boolean isRegressionTesting() {
		return regressionTesting;
	}

	public void setRegressionTesting(boolean regressionTesting) {
		this.regressionTesting = regressionTesting;
	}

	public int getStopAfter() {
		return stopAfter;
	}

	public void setStopAfter(int stopAfter) {
		this.stopAfter = stopAfter;
	}

	public boolean isAkamaiTesting() {
		return akamaiTesting;
	}

	public void setAkamaiTesting(boolean akamaiTesting) {
		this.akamaiTesting = akamaiTesting;
	}

	public boolean isEmailOnCompletion() {
		return emailOnCompletion;
	}

	public void setEmailOnCompletion(boolean emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}

	public boolean isEmailOnFailure() {
		return emailOnFailure;
	}

	public void setEmailOnFailure(boolean emailOnFailure) {
		this.emailOnFailure = emailOnFailure;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getModuleIds() {
		return moduleIds;
	}

	public void setTransactionTestcase(boolean isTransactionTestcase) {
		this.isTransactionTestcase = isTransactionTestcase;
	}

	public boolean isTransactionTestcase() {
		return isTransactionTestcase;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getStartTime() {
		return startTime;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

	public boolean isRecurrence() {
		return recurrence;
	}


	public char getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(char scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getScheduleDDay() {
		return scheduleDDay;
	}

	public void setScheduleDDay(String scheduleDDay) {
		this.scheduleDDay = scheduleDDay;
	}

	public void setRecurrenceDateWise(Date recurrenceDateWise) {
		this.recurrenceDateWise = recurrenceDateWise;
	}

	public Date getRecurrenceDateWise() {
		return recurrenceDateWise;
	}

	public Integer getBaselineScheduleExecutionId() {
		return baselineScheduleExecutionId;
	}

	public void setBaselineScheduleExecutionId(Integer baselineScheduleExecutionId) {
		this.baselineScheduleExecutionId = baselineScheduleExecutionId;
	}

	public int getUserId() {
		return userId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getUrlLevel() {
		return urlLevel;
	}

	public void setUrlLevel(int urlLevel) {
		this.urlLevel = urlLevel;
	}

	@Override
	public String toString() {
		return "Schedule [scheduleId=" + scheduleId + ", suitId=" + suitId
				+ ", applicationId=" + applicationId + ", environmentId="
				+ environmentId + ", environmentCategoryId="
				+ environmentCategoryId + ", loginUserDetailId="
				+ loginUserDetailId + ", browserTypeId=" + browserTypeId
				+ ", solutionTypeId=" + solutionTypeId + ", moduleIds="
				+ moduleIds + ", textCompare=" + textCompare + ", htmlCompare="
				+ htmlCompare + ", screenCompare=" + screenCompare
				+ ", analyticsTesting=" + analyticsTesting + ", robotClicking="
				+ robotClicking + ", brandingUrlReport=" + brandingUrlReport
				+ ", brokenUrlReport=" + brokenUrlReport
				+ ", processChildUrls=" + processChildUrls
				+ ", regressionTesting=" + regressionTesting + ", stopAfter="
				+ stopAfter + ", akamaiTesting=" + akamaiTesting
				+ ", emailOnCompletion=" + emailOnCompletion
				+ ", emailOnFailure=" + emailOnFailure + ", emailIds="
				+ emailIds + ", recordStatus=" + recordStatus + ", userId="
				+ userId + ", isTransactionTestcase=" + isTransactionTestcase
				+ ", startTime=" + startTime + ", startDate=" + startDate
				+ ", isScheduled=" + isScheduled + ", recurrence=" + recurrence
				+ ", scheduleType=" + scheduleType + ", recurrenceDateWise="
				+ recurrenceDateWise + ", baselineScheduleExecutionId="
				+ baselineScheduleExecutionId + ", scheduleDDay="
				+ scheduleDDay + ", notes=" + notes + ", urlLevel=" + urlLevel+"]";
	}

	
}