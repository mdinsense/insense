package com.ensense.insense.data.analytics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the analytics_audit_summary_table database table.
 * 
 */
@Entity
@Table(name="analytics_audit_summary_table")
public class DashboardsHomeDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="test_schedule_id")
	private int testScheduleId;

	@Column(name="analytics_audit_xml")
	private String auditAnalyticsXml;
	
	@Column(name="transaction_testcase_flag")
	private int transactiontestcaseflag;

	
	public int getTestScheduleId() {
		return testScheduleId;
	}
	public void setTestScheduleId(int testScheduleId) {
		this.testScheduleId = testScheduleId;
	}
	public int getTransactiontestcaseflag() {
		return transactiontestcaseflag;
	}
	public void setTransactiontestcaseflag(int transactiontestcaseflag) {
		this.transactiontestcaseflag = transactiontestcaseflag;
	}
	public String getAuditAnalyticsXml() {
		return auditAnalyticsXml;
	}
	public void setAuditAnalyticsXml(String auditAnalyticsXml) {
		this.auditAnalyticsXml = auditAnalyticsXml;
	}
	
	
 
}
