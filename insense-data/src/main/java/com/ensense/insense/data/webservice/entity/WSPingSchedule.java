package com.ensense.insense.data.webservice.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the ws_ping_test_schedule_table database table.
 * added by 424596
 */

@Entity
@Table(name="WSPingSchedule")
public class WSPingSchedule {
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsPingScheduleId")
	private int wsPingScheduleId;

	
	@Column(name="isScheduled")
	private boolean isScheduled;

	@Column(name="userId")
	private String userId;

	@Column(name="startTime")
	private Time startTime;
	
	@Column(name="startDate")
	private Date startDate;
	
	@Column(name="reocurrence")
	private boolean reocurrence;

	@Column(name="sendEmail")
	private boolean sendEmail;
	
	@Column(name="emailIds")
	private String emailIds;

	@Column(name="serviceNames")
	private String serviceNames;
	
	@Column(name="environment")
	private String environment;

	@Column(name="status")
	private String status;

	@Column(name="emailOnlyOnFailure")
	private boolean emailOnlyOnFailure;
	
	@Column(name="emailOnlyOnCompletion")
	private boolean emailOnlyOnCompletion;
	
	
	public boolean isEmailOnlyOnCompletion() {
		return emailOnlyOnCompletion;
	}

	public void setEmailOnlyOnCompletion(boolean emailOnlyOnCompletion) {
		this.emailOnlyOnCompletion = emailOnlyOnCompletion;
	}

	public int getWsPingScheduleId() {
		return wsPingScheduleId;
	}

	public void setWsPingScheduleId(int wsPingScheduleId) {
		this.wsPingScheduleId = wsPingScheduleId;
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

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
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

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public String getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(String serviceNames) {
		this.serviceNames = serviceNames;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isEmailOnlyOnFailure() {
		return emailOnlyOnFailure;
	}

	public void setEmailOnlyOnFailure(boolean emailOnlyOnFailure) {
		this.emailOnlyOnFailure = emailOnlyOnFailure;
	}

	@Override
	public String toString() {
		return "WSPingSchedule [wsPingScheduleId="
				+ wsPingScheduleId + ", isScheduled=" + isScheduled
				+ ", userId=" + userId + ", startTime=" + startTime
				+ ", startDate=" + startDate + ", reocurrence=" + reocurrence
				+ ", sendEmail=" + sendEmail + ", emailIds=" + emailIds
				+ ", serviceNames=" + serviceNames + ", environment="
				+ environment + ", status=" + status + ", emailOnlyOnFailure="
				+ emailOnlyOnFailure + "]";
	}

}
