package com.ensense.insense.data.analytics.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AnalyticsAuditSummary")
public class AnalyticsAuditSummary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="analyticsAuditSummaryId")
	private Integer analyticsAuditSummaryId;
	
	@Column(name="scheduleId")
	private Integer scheduleId;
	
	@Lob
	@Column(name="analyticsAuditXml")
	private String analyticsAuditXml;
	
	@Column(name="transactionTestcaseFlag")
	private Boolean transactionTestcaseFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="reportGeneratedDate")
	private Date reportGeneratedDate;
	
	public Integer getAnalyticsAuditSummaryId() {
		return analyticsAuditSummaryId;
	}

	public void setAnalyticsAuditSummaryId(Integer analyticsAuditSummaryId) {
		this.analyticsAuditSummaryId = analyticsAuditSummaryId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getAnalyticsAuditXml() {
		return analyticsAuditXml;
	}

	public void setAnalyticsAuditXml(String analyticsAuditXml) {
		this.analyticsAuditXml = analyticsAuditXml;
	}

	public Boolean getTransactionTestcaseFlag() {
		return transactionTestcaseFlag;
	}

	public void setTransactionTestcaseFlag(Boolean transactionTestcaseFlag) {
		this.transactionTestcaseFlag = transactionTestcaseFlag;
	}

	public Date getReportGeneratedDate() {
		return reportGeneratedDate;
	}

	public void setReportGeneratedDate(Date reportGeneratedDate) {
		this.reportGeneratedDate = reportGeneratedDate;
	}

	@Override
	public String toString() {
		return "AnalyticsAuditSummary [analyticsAuditSummaryId="
				+ analyticsAuditSummaryId + ", scheduleId=" + scheduleId
				+ ", analyticsAuditXml=" + analyticsAuditXml
				+ ", transactionTestcaseFlag=" + transactionTestcaseFlag
				+ ", reportGeneratedDate=" + reportGeneratedDate + "]";
	}

	
}
