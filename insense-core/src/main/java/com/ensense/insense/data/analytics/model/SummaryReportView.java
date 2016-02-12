package com.ensense.insense.data.analytics.model;

public class SummaryReportView {

	String modifiedContent;
	String addOrDeleteContent;
	String impactedUrls;
	String linkToTextComparisonReport;
	int urlNo;
	String navigationPath;
	public String getModifiedContent() {
		return modifiedContent;
	}
	public void setModifiedContent(String modifiedContent) {
		this.modifiedContent = modifiedContent;
	}
	public String getAddOrDeleteContent() {
		return addOrDeleteContent;
	}
	public void setAddOrDeleteContent(String addOrDeleteContent) {
		this.addOrDeleteContent = addOrDeleteContent;
	}
	public String getImpactedUrls() {
		return impactedUrls;
	}
	public void setImpactedUrls(String impactedUrls) {
		this.impactedUrls = impactedUrls;
	}
	public String getLinkToTextComparisonReport() {
		return linkToTextComparisonReport;
	}
	public void setLinkToTextComparisonReport(String linkToTextComparisonReport) {
		this.linkToTextComparisonReport = linkToTextComparisonReport;
	}
	public int getUrlNo() {
		return urlNo;
	}
	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}
	public String getNavigationPath() {
		return navigationPath;
	}
	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}
	@Override
	public String toString() {
		return "SummaryReportView [modifiedContent=" + modifiedContent
				+ ", addOrDeleteContent=" + addOrDeleteContent
				+ ", impactedUrls=" + impactedUrls
				+ ", linkToTextComparisonReport=" + linkToTextComparisonReport
				+ ", urlNo=" + urlNo + ", navigationPath=" + navigationPath
				+ "]";
	}
}
