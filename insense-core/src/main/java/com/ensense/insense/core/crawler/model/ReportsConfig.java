package com.ensense.insense.core.crawler.model;

public class ReportsConfig {

	private String reportsStartTime;
	private String reportsEndTime;
	private Integer reportsProcessedUrlCount;
	private Integer reportsPendingUrlCount;
	private String lastUpdatedTime;
	public String getReportsStartTime() {
		return reportsStartTime;
	}
	public void setReportsStartTime(String reportsStartTime) {
		this.reportsStartTime = reportsStartTime;
	}
	public String getReportsEndTime() {
		return reportsEndTime;
	}
	public void setReportsEndTime(String reportsEndTime) {
		this.reportsEndTime = reportsEndTime;
	}
	public Integer getReportsProcessedUrlCount() {
		return reportsProcessedUrlCount;
	}
	public void setReportsProcessedUrlCount(Integer reportsProcessedUrlCount) {
		this.reportsProcessedUrlCount = reportsProcessedUrlCount;
	}
	public Integer getReportsPendingUrlCount() {
		return reportsPendingUrlCount;
	}
	public void setReportsPendingUrlCount(Integer reportsPendingUrlCount) {
		this.reportsPendingUrlCount = reportsPendingUrlCount;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String toString() {
		return "ReportsConfig [reportsStartTime=" + reportsStartTime
				+ ", reportsEndTime=" + reportsEndTime
				+ ", reportsProcessedUrlCount=" + reportsProcessedUrlCount
				+ ", reportsPendingUrlCount=" + reportsPendingUrlCount
				+ ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}
}
