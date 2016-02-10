package com.ensense.insense.data.common.model;

public class ReportStatus {

	private int scheduleId;
	private int scheduleExecutionId;
	private String scheduleStartDate;
	private int noOfurls;
	private String totalUrlsFailed;
	private String totalComparisonMatched;
	private String totalComparisonNotMatched;
	private boolean brokenUrlReportAvailable;
	private boolean analyticsReportAvailable;
	private boolean textOrImageReportAvailable;
	
	private boolean reportsAvailable;
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public int getScheduleExecutionId() {
		return scheduleExecutionId;
	}
	public void setScheduleExecutionId(int scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}
	public String getScheduleStartDate() {
		return scheduleStartDate;
	}
	public void setScheduleStartDate(String scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}
	public int getNoOfurls() {
		return noOfurls;
	}
	public void setNoOfurls(int noOfurls) {
		this.noOfurls = noOfurls;
	}
	public String getTotalUrlsFailed() {
		return totalUrlsFailed;
	}
	public void setTotalUrlsFailed(String totalUrlsFailed) {
		this.totalUrlsFailed = totalUrlsFailed;
	}
	public String getTotalComparisonMatched() {
		return totalComparisonMatched;
	}
	public void setTotalComparisonMatched(String totalComparisonMatched) {
		this.totalComparisonMatched = totalComparisonMatched;
	}
	public String getTotalComparisonNotMatched() {
		return totalComparisonNotMatched;
	}
	public void setTotalComparisonNotMatched(String totalComparisonNotMatched) {
		this.totalComparisonNotMatched = totalComparisonNotMatched;
	}
	public boolean isBrokenUrlReportAvailable() {
		return brokenUrlReportAvailable;
	}
	public void setBrokenUrlReportAvailable(boolean brokenUrlReportAvailable) {
		this.brokenUrlReportAvailable = brokenUrlReportAvailable;
	}
	public boolean isAnalyticsReportAvailable() {
		return analyticsReportAvailable;
	}
	public void setAnalyticsReportAvailable(boolean analyticsReportAvailable) {
		this.analyticsReportAvailable = analyticsReportAvailable;
	}
	public boolean isReportsAvailable() {
		return reportsAvailable;
	}
	public void setReportsAvailable(boolean reportsAvailable) {
		this.reportsAvailable = reportsAvailable;
	}
	
	@Override
	public String toString() {
		return "ReportStatus [scheduleId=" + scheduleId
				+ ", scheduleExecutionId=" + scheduleExecutionId
				+ ", scheduleStartDate=" + scheduleStartDate + ", noOfurls="
				+ noOfurls + ", totalUrlsFailed=" + totalUrlsFailed
				+ ", totalComparisonMatched=" + totalComparisonMatched
				+ ", totalComparisonNotMatched=" + totalComparisonNotMatched
				+ ", brokenUrlReportAvailable=" + brokenUrlReportAvailable
				+ ", analyticsReportAvailable=" + analyticsReportAvailable
				+ ", textOrImageReportAvailable=" + textOrImageReportAvailable
				+ ", reportsAvailable=" + reportsAvailable + "]";
	}
	public boolean isTextOrImageReportAvailable() {
		return textOrImageReportAvailable;
	}
	public void setTextOrImageReportAvailable(boolean textOrImageReportAvailable) {
		this.textOrImageReportAvailable = textOrImageReportAvailable;
	}
	
}
