package com.ensense.insense.core.crawler.model;

public class UiReportsSummary {

	private int currentRunExecutionScheduleExecutionId;
	private int baselineRunExecutionScheduleExecutionId;
	private int currentRunTotalUrls;
	private int urlMatchedWithBaselineCount;
	private int urlNotMatchedWithBaselineCount;
	private int newUrlFoundInCurrentRunCount;
	private int baselineTotalUrlCount;
	private String baselineCrawlRunStartDateTime;
	private String currentCrawlRunStartDateTime;
	private String baselineCrawlRunEndDateTime;
	private String currentCrawlRunEndDateTime;
	
	private String baselineTextCompareRunStartDateTime;
	private String currentTextCompareRunStartDateTime;
	private String baselineTextCompareRunEndDateTime;
	private String currentTextCompareRunEndDateTime;
	
	private String baselineHtmlCompareRunStartDateTime;
	private String currentHtmlCompareRunStartDateTime;
	private String baselineHtmlCompareRunEndDateTime;
	private String currentHtmlCompareRunEndDateTime;
	
	private String baselineImageCompareRunStartDateTime;
	private String currentImageCompareRunStartDateTime;
	private String baselineImageCompareRunEndDateTime;
	private String currentImageCompareRunEndDateTime;
	
	private int nintyAndAbovePercentageMatchCount;
	private int zeroTotwentyPercentageMatchCount;
	private int twentyToSixtyPercentageMatchCount;
	private int sixtyToNintyNinePercentageMatchCount;
	private int matchedCount;
	
	private int failedCount;
	
	private int textComparisonPassedCount;
	private int textComparisonFailedCount;
	private int htmlComparisonPassedCount;
	private int htmlComparisonFailedCount;
	private int imageComparisonPassedCount;
	private int imageComparisonFailedCount;
	
	private boolean comparisonStatusRefId;
	private boolean brokenUrlReportStatusId;
	private boolean analyticsReportStatusId; 
	private boolean textComparison;
	private boolean htmlComparison;
	private boolean imageComparison;
	
	private String baselineAnalyticsReportStartDateTime;
	private String currentAnalyticsReportStartDateTime;
	private String baselineAnalyticsReportEndDateTime;
	private String currentAnalyticsReportEndDateTime;
	
	private String baselineBrokenUrlReportStartDateTime;
	private String currentBrokenUrlReportStartDateTime;
	private String baselineBrokenUrlReportEndDateTime;
	private String currentBrokenUrlReportEndDateTime;
	
	public UiReportsSummary(){
		comparisonStatusRefId = false;
		brokenUrlReportStatusId = false;
		analyticsReportStatusId = false;
		textComparison = false;
		htmlComparison = false;
		imageComparison = false;
	}

	public int getCurrentRunExecutionScheduleExecutionId() {
		return currentRunExecutionScheduleExecutionId;
	}

	public void setCurrentRunExecutionScheduleExecutionId(
			int currentRunExecutionScheduleExecutionId) {
		this.currentRunExecutionScheduleExecutionId = currentRunExecutionScheduleExecutionId;
	}

	public int getBaselineRunExecutionScheduleExecutionId() {
		return baselineRunExecutionScheduleExecutionId;
	}

	public void setBaselineRunExecutionScheduleExecutionId(
			int baselineRunExecutionScheduleExecutionId) {
		this.baselineRunExecutionScheduleExecutionId = baselineRunExecutionScheduleExecutionId;
	}

	public int getCurrentRunTotalUrls() {
		return currentRunTotalUrls;
	}

	public void setCurrentRunTotalUrls(int currentRunTotalUrls) {
		this.currentRunTotalUrls = currentRunTotalUrls;
	}

	public int getUrlMatchedWithBaselineCount() {
		return urlMatchedWithBaselineCount;
	}

	public void setUrlMatchedWithBaselineCount(int urlMatchedWithBaselineCount) {
		this.urlMatchedWithBaselineCount = urlMatchedWithBaselineCount;
	}

	public int getUrlNotMatchedWithBaselineCount() {
		return urlNotMatchedWithBaselineCount;
	}

	public void setUrlNotMatchedWithBaselineCount(int urlNotMatchedWithBaselineCount) {
		this.urlNotMatchedWithBaselineCount = urlNotMatchedWithBaselineCount;
	}

	public int getNewUrlFoundInCurrentRunCount() {
		return newUrlFoundInCurrentRunCount;
	}

	public void setNewUrlFoundInCurrentRunCount(int newUrlFoundInCurrentRunCount) {
		this.newUrlFoundInCurrentRunCount = newUrlFoundInCurrentRunCount;
	}

	public int getBaselineTotalUrlCount() {
		return baselineTotalUrlCount;
	}

	public void setBaselineTotalUrlCount(int baselineTotalUrlCount) {
		this.baselineTotalUrlCount = baselineTotalUrlCount;
	}

	public String getBaselineCrawlRunStartDateTime() {
		return baselineCrawlRunStartDateTime;
	}

	public void setBaselineCrawlRunStartDateTime(
			String baselineCrawlRunStartDateTime) {
		this.baselineCrawlRunStartDateTime = baselineCrawlRunStartDateTime;
	}

	public String getCurrentCrawlRunStartDateTime() {
		return currentCrawlRunStartDateTime;
	}

	public void setCurrentCrawlRunStartDateTime(String currentCrawlRunStartDateTime) {
		this.currentCrawlRunStartDateTime = currentCrawlRunStartDateTime;
	}

	public String getBaselineCrawlRunEndDateTime() {
		return baselineCrawlRunEndDateTime;
	}

	public void setBaselineCrawlRunEndDateTime(String baselineCrawlRunEndDateTime) {
		this.baselineCrawlRunEndDateTime = baselineCrawlRunEndDateTime;
	}

	public String getCurrentCrawlRunEndDateTime() {
		return currentCrawlRunEndDateTime;
	}

	public void setCurrentCrawlRunEndDateTime(String currentCrawlRunEndDateTime) {
		this.currentCrawlRunEndDateTime = currentCrawlRunEndDateTime;
	}

	public String getBaselineTextCompareRunStartDateTime() {
		return baselineTextCompareRunStartDateTime;
	}

	public void setBaselineTextCompareRunStartDateTime(
			String baselineTextCompareRunStartDateTime) {
		this.baselineTextCompareRunStartDateTime = baselineTextCompareRunStartDateTime;
	}

	public String getCurrentTextCompareRunStartDateTime() {
		return currentTextCompareRunStartDateTime;
	}

	public void setCurrentTextCompareRunStartDateTime(
			String currentTextCompareRunStartDateTime) {
		this.currentTextCompareRunStartDateTime = currentTextCompareRunStartDateTime;
	}

	public String getBaselineTextCompareRunEndDateTime() {
		return baselineTextCompareRunEndDateTime;
	}

	public void setBaselineTextCompareRunEndDateTime(
			String baselineTextCompareRunEndDateTime) {
		this.baselineTextCompareRunEndDateTime = baselineTextCompareRunEndDateTime;
	}

	public String getCurrentTextCompareRunEndDateTime() {
		return currentTextCompareRunEndDateTime;
	}

	public void setCurrentTextCompareRunEndDateTime(
			String currentTextCompareRunEndDateTime) {
		this.currentTextCompareRunEndDateTime = currentTextCompareRunEndDateTime;
	}

	public String getBaselineHtmlCompareRunStartDateTime() {
		return baselineHtmlCompareRunStartDateTime;
	}

	public void setBaselineHtmlCompareRunStartDateTime(
			String baselineHtmlCompareRunStartDateTime) {
		this.baselineHtmlCompareRunStartDateTime = baselineHtmlCompareRunStartDateTime;
	}

	public String getCurrentHtmlCompareRunStartDateTime() {
		return currentHtmlCompareRunStartDateTime;
	}

	public void setCurrentHtmlCompareRunStartDateTime(
			String currentHtmlCompareRunStartDateTime) {
		this.currentHtmlCompareRunStartDateTime = currentHtmlCompareRunStartDateTime;
	}

	public String getBaselineHtmlCompareRunEndDateTime() {
		return baselineHtmlCompareRunEndDateTime;
	}

	public void setBaselineHtmlCompareRunEndDateTime(
			String baselineHtmlCompareRunEndDateTime) {
		this.baselineHtmlCompareRunEndDateTime = baselineHtmlCompareRunEndDateTime;
	}

	public String getCurrentHtmlCompareRunEndDateTime() {
		return currentHtmlCompareRunEndDateTime;
	}

	public void setCurrentHtmlCompareRunEndDateTime(
			String currentHtmlCompareRunEndDateTime) {
		this.currentHtmlCompareRunEndDateTime = currentHtmlCompareRunEndDateTime;
	}

	public String getBaselineImageCompareRunStartDateTime() {
		return baselineImageCompareRunStartDateTime;
	}

	public void setBaselineImageCompareRunStartDateTime(
			String baselineImageCompareRunStartDateTime) {
		this.baselineImageCompareRunStartDateTime = baselineImageCompareRunStartDateTime;
	}

	public String getCurrentImageCompareRunStartDateTime() {
		return currentImageCompareRunStartDateTime;
	}

	public void setCurrentImageCompareRunStartDateTime(
			String currentImageCompareRunStartDateTime) {
		this.currentImageCompareRunStartDateTime = currentImageCompareRunStartDateTime;
	}

	public String getBaselineImageCompareRunEndDateTime() {
		return baselineImageCompareRunEndDateTime;
	}

	public void setBaselineImageCompareRunEndDateTime(
			String baselineImageCompareRunEndDateTime) {
		this.baselineImageCompareRunEndDateTime = baselineImageCompareRunEndDateTime;
	}

	public String getCurrentImageCompareRunEndDateTime() {
		return currentImageCompareRunEndDateTime;
	}

	public void setCurrentImageCompareRunEndDateTime(
			String currentImageCompareRunEndDateTime) {
		this.currentImageCompareRunEndDateTime = currentImageCompareRunEndDateTime;
	}

	public int getNintyAndAbovePercentageMatchCount() {
		return nintyAndAbovePercentageMatchCount;
	}

	public void setNintyAndAbovePercentageMatchCount(
			int nintyAndAbovePercentageMatchCount) {
		this.nintyAndAbovePercentageMatchCount = nintyAndAbovePercentageMatchCount;
	}

	public int getZeroTotwentyPercentageMatchCount() {
		return zeroTotwentyPercentageMatchCount;
	}

	public void setZeroTotwentyPercentageMatchCount(
			int zeroTotwentyPercentageMatchCount) {
		this.zeroTotwentyPercentageMatchCount = zeroTotwentyPercentageMatchCount;
	}

	public int getTwentyToSixtyPercentageMatchCount() {
		return twentyToSixtyPercentageMatchCount;
	}

	public void setTwentyToSixtyPercentageMatchCount(
			int twentyToSixtyPercentageMatchCount) {
		this.twentyToSixtyPercentageMatchCount = twentyToSixtyPercentageMatchCount;
	}

	public int getSixtyToNintyNinePercentageMatchCount() {
		return sixtyToNintyNinePercentageMatchCount;
	}

	public void setSixtyToNintyNinePercentageMatchCount(
			int sixtyToNintyNinePercentageMatchCount) {
		this.sixtyToNintyNinePercentageMatchCount = sixtyToNintyNinePercentageMatchCount;
	}

	public int getMatchedCount() {
		return matchedCount;
	}

	public void setMatchedCount(int matchedCount) {
		this.matchedCount = matchedCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public int getTextComparisonPassedCount() {
		return textComparisonPassedCount;
	}

	public void setTextComparisonPassedCount(int textComparisonPassedCount) {
		this.textComparisonPassedCount = textComparisonPassedCount;
	}

	public int getTextComparisonFailedCount() {
		return textComparisonFailedCount;
	}

	public void setTextComparisonFailedCount(int textComparisonFailedCount) {
		this.textComparisonFailedCount = textComparisonFailedCount;
	}

	public int getHtmlComparisonPassedCount() {
		return htmlComparisonPassedCount;
	}

	public void setHtmlComparisonPassedCount(int htmlComparisonPassedCount) {
		this.htmlComparisonPassedCount = htmlComparisonPassedCount;
	}

	public int getHtmlComparisonFailedCount() {
		return htmlComparisonFailedCount;
	}

	public void setHtmlComparisonFailedCount(int htmlComparisonFailedCount) {
		this.htmlComparisonFailedCount = htmlComparisonFailedCount;
	}

	public int getImageComparisonPassedCount() {
		return imageComparisonPassedCount;
	}

	public void setImageComparisonPassedCount(int imageComparisonPassedCount) {
		this.imageComparisonPassedCount = imageComparisonPassedCount;
	}

	public int getImageComparisonFailedCount() {
		return imageComparisonFailedCount;
	}

	public void setImageComparisonFailedCount(int imageComparisonFailedCount) {
		this.imageComparisonFailedCount = imageComparisonFailedCount;
	}

	public boolean isComparisonStatusRefId() {
		return comparisonStatusRefId;
	}

	public void setComparisonStatusRefId(boolean comparisonStatusRefId) {
		this.comparisonStatusRefId = comparisonStatusRefId;
	}

	public boolean isBrokenUrlReportStatusId() {
		return brokenUrlReportStatusId;
	}

	public void setBrokenUrlReportStatusId(boolean brokenUrlReportStatusId) {
		this.brokenUrlReportStatusId = brokenUrlReportStatusId;
	}

	public boolean isAnalyticsReportStatusId() {
		return analyticsReportStatusId;
	}

	public void setAnalyticsReportStatusId(boolean analyticsReportStatusId) {
		this.analyticsReportStatusId = analyticsReportStatusId;
	}

	public boolean isTextComparison() {
		return textComparison;
	}

	public void setTextComparison(boolean textComparison) {
		this.textComparison = textComparison;
	}

	public boolean isHtmlComparison() {
		return htmlComparison;
	}

	public void setHtmlComparison(boolean htmlComparison) {
		this.htmlComparison = htmlComparison;
	}

	public boolean isImageComparison() {
		return imageComparison;
	}

	public void setImageComparison(boolean imageComparison) {
		this.imageComparison = imageComparison;
	}

	public String getBaselineAnalyticsReportStartDateTime() {
		return baselineAnalyticsReportStartDateTime;
	}

	public void setBaselineAnalyticsReportStartDateTime(
			String baselineAnalyticsReportStartDateTime) {
		this.baselineAnalyticsReportStartDateTime = baselineAnalyticsReportStartDateTime;
	}

	public String getCurrentAnalyticsReportStartDateTime() {
		return currentAnalyticsReportStartDateTime;
	}

	public void setCurrentAnalyticsReportStartDateTime(
			String currentAnalyticsReportStartDateTime) {
		this.currentAnalyticsReportStartDateTime = currentAnalyticsReportStartDateTime;
	}

	public String getBaselineAnalyticsReportEndDateTime() {
		return baselineAnalyticsReportEndDateTime;
	}

	public void setBaselineAnalyticsReportEndDateTime(
			String baselineAnalyticsReportEndDateTime) {
		this.baselineAnalyticsReportEndDateTime = baselineAnalyticsReportEndDateTime;
	}

	public String getCurrentAnalyticsReportEndDateTime() {
		return currentAnalyticsReportEndDateTime;
	}

	public void setCurrentAnalyticsReportEndDateTime(
			String currentAnalyticsReportEndDateTime) {
		this.currentAnalyticsReportEndDateTime = currentAnalyticsReportEndDateTime;
	}

	public String getBaselineBrokenUrlReportStartDateTime() {
		return baselineBrokenUrlReportStartDateTime;
	}

	public void setBaselineBrokenUrlReportStartDateTime(
			String baselineBrokenUrlReportStartDateTime) {
		this.baselineBrokenUrlReportStartDateTime = baselineBrokenUrlReportStartDateTime;
	}

	public String getCurrentBrokenUrlReportStartDateTime() {
		return currentBrokenUrlReportStartDateTime;
	}

	public void setCurrentBrokenUrlReportStartDateTime(
			String currentBrokenUrlReportStartDateTime) {
		this.currentBrokenUrlReportStartDateTime = currentBrokenUrlReportStartDateTime;
	}

	public String getBaselineBrokenUrlReportEndDateTime() {
		return baselineBrokenUrlReportEndDateTime;
	}

	public void setBaselineBrokenUrlReportEndDateTime(
			String baselineBrokenUrlReportEndDateTime) {
		this.baselineBrokenUrlReportEndDateTime = baselineBrokenUrlReportEndDateTime;
	}

	public String getCurrentBrokenUrlReportEndDateTime() {
		return currentBrokenUrlReportEndDateTime;
	}

	public void setCurrentBrokenUrlReportEndDateTime(
			String currentBrokenUrlReportEndDateTime) {
		this.currentBrokenUrlReportEndDateTime = currentBrokenUrlReportEndDateTime;
	}

	@Override
	public String toString() {
		return "UiReportsSummary [currentRunExecutionScheduleExecutionId="
				+ currentRunExecutionScheduleExecutionId
				+ ", baselineRunExecutionScheduleExecutionId="
				+ baselineRunExecutionScheduleExecutionId
				+ ", currentRunTotalUrls=" + currentRunTotalUrls
				+ ", urlMatchedWithBaselineCount="
				+ urlMatchedWithBaselineCount
				+ ", urlNotMatchedWithBaselineCount="
				+ urlNotMatchedWithBaselineCount
				+ ", newUrlFoundInCurrentRunCount="
				+ newUrlFoundInCurrentRunCount + ", baselineTotalUrlCount="
				+ baselineTotalUrlCount + ", baselineCrawlRunStartDateTime="
				+ baselineCrawlRunStartDateTime
				+ ", currentCrawlRunStartDateTime="
				+ currentCrawlRunStartDateTime
				+ ", baselineCrawlRunEndDateTime="
				+ baselineCrawlRunEndDateTime + ", currentCrawlRunEndDateTime="
				+ currentCrawlRunEndDateTime
				+ ", baselineTextCompareRunStartDateTime="
				+ baselineTextCompareRunStartDateTime
				+ ", currentTextCompareRunStartDateTime="
				+ currentTextCompareRunStartDateTime
				+ ", baselineTextCompareRunEndDateTime="
				+ baselineTextCompareRunEndDateTime
				+ ", currentTextCompareRunEndDateTime="
				+ currentTextCompareRunEndDateTime
				+ ", baselineHtmlCompareRunStartDateTime="
				+ baselineHtmlCompareRunStartDateTime
				+ ", currentHtmlCompareRunStartDateTime="
				+ currentHtmlCompareRunStartDateTime
				+ ", baselineHtmlCompareRunEndDateTime="
				+ baselineHtmlCompareRunEndDateTime
				+ ", currentHtmlCompareRunEndDateTime="
				+ currentHtmlCompareRunEndDateTime
				+ ", baselineImageCompareRunStartDateTime="
				+ baselineImageCompareRunStartDateTime
				+ ", currentImageCompareRunStartDateTime="
				+ currentImageCompareRunStartDateTime
				+ ", baselineImageCompareRunEndDateTime="
				+ baselineImageCompareRunEndDateTime
				+ ", currentImageCompareRunEndDateTime="
				+ currentImageCompareRunEndDateTime
				+ ", nintyAndAbovePercentageMatchCount="
				+ nintyAndAbovePercentageMatchCount
				+ ", zeroTotwentyPercentageMatchCount="
				+ zeroTotwentyPercentageMatchCount
				+ ", twentyToSixtyPercentageMatchCount="
				+ twentyToSixtyPercentageMatchCount
				+ ", sixtyToNintyNinePercentageMatchCount="
				+ sixtyToNintyNinePercentageMatchCount + ", matchedCount="
				+ matchedCount + ", failedCount=" + failedCount
				+ ", textComparisonPassedCount=" + textComparisonPassedCount
				+ ", textComparisonFailedCount=" + textComparisonFailedCount
				+ ", htmlComparisonPassedCount=" + htmlComparisonPassedCount
				+ ", htmlComparisonFailedCount=" + htmlComparisonFailedCount
				+ ", imageComparisonPassedCount=" + imageComparisonPassedCount
				+ ", imageComparisonFailedCount=" + imageComparisonFailedCount
				+ ", comparisonStatusRefId=" + comparisonStatusRefId
				+ ", brokenUrlReportStatusId=" + brokenUrlReportStatusId
				+ ", analyticsReportStatusId=" + analyticsReportStatusId
				+ ", textComparison=" + textComparison + ", htmlComparison="
				+ htmlComparison + ", imageComparison=" + imageComparison
				+ ", baselineAnalyticsReportStartDateTime="
				+ baselineAnalyticsReportStartDateTime
				+ ", currentAnalyticsReportStartDateTime="
				+ currentAnalyticsReportStartDateTime
				+ ", baselineAnalyticsReportEndDateTime="
				+ baselineAnalyticsReportEndDateTime
				+ ", currentAnalyticsReportEndDateTime="
				+ currentAnalyticsReportEndDateTime
				+ ", baselineBrokenUrlReportStartDateTime="
				+ baselineBrokenUrlReportStartDateTime
				+ ", currentBrokenUrlReportStartDateTime="
				+ currentBrokenUrlReportStartDateTime
				+ ", baselineBrokenUrlReportEndDateTime="
				+ baselineBrokenUrlReportEndDateTime
				+ ", currentBrokenUrlReportEndDateTime="
				+ currentBrokenUrlReportEndDateTime + "]";
	}
	
	
	
}
