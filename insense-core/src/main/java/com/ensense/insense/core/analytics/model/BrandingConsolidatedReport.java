package com.ensense.insense.core.analytics.model;

public class BrandingConsolidatedReport {
	private TextImageReportData baselineBrandingReportData;
	private TextImageReportData currentRunBrandingReportData;
	private String key;
	
	public TextImageReportData getBaselineBrandingReportData() {
		return baselineBrandingReportData;
	}
	public void setBaselineBrandingReportData(
			TextImageReportData baselineBrandingReportData) {
		this.baselineBrandingReportData = baselineBrandingReportData;
	}
	public TextImageReportData getCurrentRunBrandingReportData() {
		return currentRunBrandingReportData;
	}
	public void setCurrentRunBrandingReportData(
			TextImageReportData currentRunBrandingReportData) {
		this.currentRunBrandingReportData = currentRunBrandingReportData;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
