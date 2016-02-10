package com.ensense.insense.data.common.model;

import java.util.ArrayList;
import java.util.List;

import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.crawler.model.CompareConfig;
import com.cts.mint.uitesting.entity.ScheduleScript;

public class ScheduleStatus {
	private Integer scheduleId;
	private Integer scheduleExecutionId;
	private String crawlStartTime;
	private String crawlEndTime;
	private String crawlStatus;
	private String lastUpdatedTime;
	private int processedUrlCount;
	private int pendingUrlCount;
	private String reportsStartTime;
	private String reportsEndTime;
	private Integer reportsPendingUrlCount;
	private Integer reportsProcessedUrlCount;
	private String reportsStatus;
	private String comparisonStartTime;
	private String comparisonEndTime;
	private Integer comparisonPendingUrlCount;
	private Integer comparisonProcessedUrlCount;
	private String comparisonStatus;
	private String scheduledBy;
	private String startTime;
	private String scheduledDate;
	private String scheduledDay;
	private boolean reccurrence;
	private String pendingForThisWeek;
	private String executionStatus;
	private int percentageMatch;
	private String group;
	private String runningTime;
	private String applicationName;
	private String environmentCategory;
	private List<ExecutionStatus> action;
	private String suitName;
	private List<Link> crawledLink;
	
	private boolean transactionTesting;
	private List<ScheduleScript> scheduleScriptList;
	private boolean analyticsReportAvailable;
	
	private CompareConfig compareConfig;
	
	public CompareConfig getCompareConfig() {
		if ( null == compareConfig ){
			compareConfig = new CompareConfig();
		}
		return compareConfig;
	}
	
	public boolean isAnalyticsReportAvailable() {
		return analyticsReportAvailable;
	}

	public void setAnalyticsReportAvailable(boolean analyticsReportAvailable) {
		this.analyticsReportAvailable = analyticsReportAvailable;
	}

	public List<Link> getCrawledLink() {
		return crawledLink;
	}

	public void setCrawledLink(List<Link> crawledLink) {
		this.crawledLink = crawledLink;
	}

	public int getPercentageMatch() {
		return percentageMatch;
	}

	public void setPercentageMatch(int percentageMatch) {
		this.percentageMatch = percentageMatch;
	}

	public void setCompareConfig(CompareConfig compareConfig) {
		this.compareConfig = compareConfig;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getScheduleExecutionId() {
		return scheduleExecutionId;
	}
	public void setScheduleExecutionId(Integer scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}
	public String getCrawlStartTime() {
		return crawlStartTime;
	}
	public void setCrawlStartTime(String crawlStartTime) {
		this.crawlStartTime = crawlStartTime;
	}
	public String getCrawlEndTime() {
		return crawlEndTime;
	}
	public void setCrawlEndTime(String crawlEndTime) {
		this.crawlEndTime = crawlEndTime;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	public int getProcessedUrlCount() {
		return processedUrlCount;
	}
	public void setProcessedUrlCount(int processedUrlCount) {
		this.processedUrlCount = processedUrlCount;
	}
	public int getPendingUrlCount() {
		return pendingUrlCount;
	}
	public void setPendingUrlCount(int pendingUrlCount) {
		this.pendingUrlCount = pendingUrlCount;
	}
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
	public String getComparisonStartTime() {
		return comparisonStartTime;
	}
	public void setComparisonStartTime(String comparisonStartTime) {
		this.comparisonStartTime = comparisonStartTime;
	}
	public String getComparisonEndTime() {
		return comparisonEndTime;
	}
	public void setComparisonEndTime(String comparisonEndTime) {
		this.comparisonEndTime = comparisonEndTime;
	}
	public Integer getReportsPendingUrlCount() {
		return reportsPendingUrlCount;
	}
	public void setReportsPendingUrlCount(Integer reportsPendingUrlCount) {
		this.reportsPendingUrlCount = reportsPendingUrlCount;
	}
	public Integer getReportsProcessedUrlCount() {
		return reportsProcessedUrlCount;
	}
	public void setReportsProcessedUrlCount(Integer reportsProcessedUrlCount) {
		this.reportsProcessedUrlCount = reportsProcessedUrlCount;
	}
	public Integer getComparisonPendingUrlCount() {
		return comparisonPendingUrlCount;
	}
	public void setComparisonPendingUrlCount(Integer comparisonPendingUrlCount) {
		this.comparisonPendingUrlCount = comparisonPendingUrlCount;
	}
	public Integer getComparisonProcessedUrlCount() {
		return comparisonProcessedUrlCount;
	}
	public void setComparisonProcessedUrlCount(Integer comparisonProcessedUrlCount) {
		this.comparisonProcessedUrlCount = comparisonProcessedUrlCount;
	}
	public String getScheduledBy() {
		return scheduledBy;
	}
	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}
	public String getCrawlStatus() {
		return crawlStatus;
	}
	public void setCrawlStatus(String crawlStatus) {
		this.crawlStatus = crawlStatus;
	}
	public String getReportsStatus() {
		return reportsStatus;
	}
	public void setReportsStatus(String reportsStatus) {
		this.reportsStatus = reportsStatus;
	}
	public String getComparisonStatus() {
		return comparisonStatus;
	}
	public void setComparisonStatus(String comparisonStatus) {
		this.comparisonStatus = comparisonStatus;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public String getScheduledDay() {
		return scheduledDay;
	}
	public void setScheduledDay(String scheduledDay) {
		this.scheduledDay = scheduledDay;
	}
	public boolean isReccurrence() {
		return reccurrence;
	}
	public void setReccurrence(boolean reccurrence) {
		this.reccurrence = reccurrence;
	}
	public String getPendingForThisWeek() {
		return pendingForThisWeek;
	}
	public void setPendingForThisWeek(String pendingForThisWeek) {
		this.pendingForThisWeek = pendingForThisWeek;
	}
	public String getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEnvironmentCategory() {
		return environmentCategory;
	}

	public void setEnvironmentCategory(String environmentCategory) {
		this.environmentCategory = environmentCategory;
	}

	public List<ExecutionStatus> getAction() {
		return action;
	}

	public void setAction(List<ExecutionStatus> action) {
		this.action = action;
	}

	public String getSuitName() {
		return suitName;
	}

	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}
	public List<ScheduleScript> getScheduleScriptList() {
		return scheduleScriptList;
	}

	public void setScheduleScriptList(List<ScheduleScript> scheduleScriptList) {
		this.scheduleScriptList = scheduleScriptList;
	}
	public boolean isTransactionTesting() {
		return transactionTesting;
	}

	public void setTransactionTesting(boolean transactionTesting) {
		this.transactionTesting = transactionTesting;
	}

	@Override
	public String toString() {
		return "ScheduleStatus [scheduleId=" + scheduleId
				+ ", scheduleExecutionId=" + scheduleExecutionId
				+ ", crawlStartTime=" + crawlStartTime + ", crawlEndTime="
				+ crawlEndTime + ", crawlStatus=" + crawlStatus
				+ ", lastUpdatedTime=" + lastUpdatedTime
				+ ", processedUrlCount=" + processedUrlCount
				+ ", pendingUrlCount=" + pendingUrlCount
				+ ", reportsStartTime=" + reportsStartTime
				+ ", reportsEndTime=" + reportsEndTime
				+ ", reportsPendingUrlCount=" + reportsPendingUrlCount
				+ ", reportsProcessedUrlCount=" + reportsProcessedUrlCount
				+ ", reportsStatus=" + reportsStatus + ", comparisonStartTime="
				+ comparisonStartTime + ", comparisonEndTime="
				+ comparisonEndTime + ", comparisonPendingUrlCount="
				+ comparisonPendingUrlCount + ", comparisonProcessedUrlCount="
				+ comparisonProcessedUrlCount + ", comparisonStatus="
				+ comparisonStatus + ", scheduledBy=" + scheduledBy
				+ ", startTime=" + startTime + ", scheduledDate="
				+ scheduledDate + ", scheduledDay=" + scheduledDay
				+ ", reccurrence=" + reccurrence + ", pendingForThisWeek="
				+ pendingForThisWeek + ", executionStatus=" + executionStatus
				+ ", percentageMatch=" + percentageMatch + ", group=" + group
				+ ", runningTime=" + runningTime + ", applicationName="
				+ applicationName + ", environmentCategory="
				+ environmentCategory + ", action=" + action + ", suitName="
				+ suitName + ", crawledLink=" + crawledLink
				+ ", transactionTesting=" + transactionTesting
				+ ", scheduleScriptList=" + scheduleScriptList
				+ ", analyticsReportAvailable=" + analyticsReportAvailable
				+ ", compareConfig=" + compareConfig + "]";
	}

}
