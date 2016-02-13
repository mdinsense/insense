package com.ensense.insense.data.webservice.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="WSSchedule")
public class WSSchedule{

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsScheduleId")
	private int wsScheduleId;

	@Column(name="isScheduled")
	private boolean isScheduled;

	@Column(name="userId")
	private String userId;
	
	@Column(name="webserviceSuiteId")
	private Integer webserviceSuiteId;

	@Column(name="startTime")
	private Date startTime;
	
	@Column(name="startDate")
	private Date startDate;
	
	@Column(name="reocurrence")
	private boolean reocurrence;
	
	@Column(name="emailOnCompletion")
	private boolean emailOnCompletion;   

	@Column(name="emailIds")
	private String emailIds; 
	
	@Column(name="emailOnFailure")
	private boolean emailOnFailure;

	@Column(name="compareFlag")
	private boolean compareFlag;
	
	@Column(name="scheduledDay")
	private String 	scheduledDay;
	
	@Column(name="recurrenceDateWise")
	private Date recurrenceDateWise;
	
	@Column(name="scheduleType ")
	private char scheduleType;

	public char getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(char scheduleType) {
		this.scheduleType = scheduleType;
	}

	

	public Date getRecurrenceDateWise() {
		return recurrenceDateWise;
	}

	public void setRecurrenceDateWise(Date recurrenceDateWise) {
		this.recurrenceDateWise = recurrenceDateWise;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(Integer webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isReocurrence() {
		return reocurrence;
	}

	public void setReocurrence(boolean reocurrence) {
		this.reocurrence = reocurrence;
	}

	public boolean isEmailOnCompletion() {
		return emailOnCompletion;
	}

	public void setEmailOnCompletion(boolean emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public boolean isEmailOnFailure() {
		return emailOnFailure;
	}

	public void setEmailOnFailure(boolean emailOnFailure) {
		this.emailOnFailure = emailOnFailure;
	}

	public boolean isCompareFlag() {
		return compareFlag;
	}

	public void setCompareFlag(boolean compareFlag) {
		this.compareFlag = compareFlag;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	@Override
	public String toString() {
		return "WSSchedule [wsScheduleId=" + wsScheduleId + ", isScheduled="
				+ isScheduled + ", userId=" + userId + ", webserviceSuiteId="
				+ webserviceSuiteId + ", startTime=" + startTime
				+ ", startDate=" + startDate + ", reocurrence=" + reocurrence
				+ ", emailOnCompletion=" + emailOnCompletion + ", emailIds="
				+ emailIds + ", emailOnFailure=" + emailOnFailure
				+ ", compareFlag=" + compareFlag + "]";
	}

	public void setScheduledDay(String scheduledDay) {
		this.scheduledDay = scheduledDay;
	}

	public String getScheduledDay() {
		return scheduledDay;
	}
}