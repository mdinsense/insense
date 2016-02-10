package com.ensense.insense.core.analytics.model;

import java.util.Date;

public class AnalyticsAuditSummaryReportDates {

	private Integer analyticsAuditSummaryId;
	private Date reportGeneratedDate;
	private int scheduleId;
	
	public Integer getAnalyticsAuditSummaryId() {
		return analyticsAuditSummaryId;
	}
	public void setAnalyticsAuditSummaryId(Integer analyticsAuditSummaryId) {
		this.analyticsAuditSummaryId = analyticsAuditSummaryId;
	}
	public Date getReportGeneratedDate() {
		return reportGeneratedDate;
	}
	public void setReportGeneratedDate(Date reportGeneratedDate) {
		this.reportGeneratedDate = reportGeneratedDate;
	}
	
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	@Override
	public String toString() {
		return "AnalyticsAuditSummaryReportDates [analyticsAuditSummaryId="
				+ analyticsAuditSummaryId + ", reportGeneratedDate="
				+ reportGeneratedDate + ", scheduleId=" + scheduleId + "]";
	}
	
}
