package com.ensense.insense.data.common.model;

public class BrokenReportSelection {

	private boolean analyticsReport;
	private boolean analyticsErrorReport;
	private boolean brokenLinkAvailable;
	private boolean brokenLinkResourcesAvailable;
	private boolean loadTimeAttributeAvailable;
	private boolean comparisonReport;
	private boolean imageTextReport;
	private boolean allImageZipFile;
	private boolean imageTextBaslineReport;
	
	public boolean isAnalyticsReport() {
		return analyticsReport;
	}
	public boolean isImageTextReport() {
		return imageTextReport;
	}
	public void setImageTextReport(boolean imageTextReport) {
		this.imageTextReport = imageTextReport;
	}
	public boolean isAllImageZipFile() {
		return allImageZipFile;
	}
	public void setAllImageZipFile(boolean allImageZipFile) {
		this.allImageZipFile = allImageZipFile;
	}
	public void setAnalyticsReport(boolean analyticsReport) {
		this.analyticsReport = analyticsReport;
	}
	public boolean isAnalyticsErrorReport() {
		return analyticsErrorReport;
	}
	public void setAnalyticsErrorReport(boolean analyticsErrorReport) {
		this.analyticsErrorReport = analyticsErrorReport;
	}
	public boolean isBrokenLinkAvailable() {
		return brokenLinkAvailable;
	}
	public void setBrokenLinkAvailable(boolean brokenLinkAvailable) {
		this.brokenLinkAvailable = brokenLinkAvailable;
	}
	public boolean isBrokenLinkResourcesAvailable() {
		return brokenLinkResourcesAvailable;
	}
	public void setBrokenLinkResourcesAvailable(boolean brokenLinkResourcesAvailable) {
		this.brokenLinkResourcesAvailable = brokenLinkResourcesAvailable;
	}
	public boolean isLoadTimeAttributeAvailable() {
		return loadTimeAttributeAvailable;
	}
	public void setLoadTimeAttributeAvailable(boolean loadTimeAttributeAvailable) {
		this.loadTimeAttributeAvailable = loadTimeAttributeAvailable;
	}
	public boolean isComparisonReport() {
		return comparisonReport;
	}
	public void setComparisonReport(boolean comparisonReport) {
		this.comparisonReport = comparisonReport;
	}
	
	
	public boolean isImageTextBaslineReport() {
		return imageTextBaslineReport;
	}
	public void setImageTextBaslineReport(boolean imageTextBaslineReport) {
		this.imageTextBaslineReport = imageTextBaslineReport;
	}
	
	@Override
	public String toString() {
		return "BrokenReportSelection [analyticsReport=" + analyticsReport
				+ ", analyticsErrorReport=" + analyticsErrorReport
				+ ", brokenLinkAvailable=" + brokenLinkAvailable
				+ ", brokenLinkResourcesAvailable="
				+ brokenLinkResourcesAvailable
				+ ", loadTimeAttributeAvailable=" + loadTimeAttributeAvailable
				+ ", comparisonReport=" + comparisonReport
				+ ", imageTextReport=" + imageTextReport + ", allImageZipFile="
				+ allImageZipFile + ", imageTextBaslineReport="
				+ imageTextBaslineReport + "]";
	}
	
}
