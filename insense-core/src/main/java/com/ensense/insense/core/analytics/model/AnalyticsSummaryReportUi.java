package com.ensense.insense.core.analytics.model;

public class AnalyticsSummaryReportUi {
	private int totalUrl;
	private String tagName;
	private int tagPresentUrlCount;
	private int tagNotPresentUrlCount;
	private int tagHasErrorUrlCount;
	public int getTotalUrl() {
		return totalUrl;
	}
	public void setTotalUrl(int totalUrl) {
		this.totalUrl = totalUrl;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public int getTagPresentUrlCount() {
		return tagPresentUrlCount;
	}
	public void setTagPresentUrlCount(int tagPresentUrlCount) {
		this.tagPresentUrlCount = tagPresentUrlCount;
	}
	public int getTagNotPresentUrlCount() {
		return tagNotPresentUrlCount;
	}
	public void setTagNotPresentUrlCount(int tagNotPresentUrlCount) {
		this.tagNotPresentUrlCount = tagNotPresentUrlCount;
	}
	public int getTagHasErrorUrlCount() {
		return tagHasErrorUrlCount;
	}
	public void setTagHasErrorUrlCount(int tagHasErrorUrlCount) {
		this.tagHasErrorUrlCount = tagHasErrorUrlCount;
	}
	@Override
	public String toString() {
		return "AnalyticsSummaryReportUi [totalUrl=" + totalUrl + ", tagName="
				+ tagName + ", tagPresentUrlCount=" + tagPresentUrlCount
				+ ", tagNotPresentUrlCount=" + tagNotPresentUrlCount
				+ ", tagHasErrorUrlCount=" + tagHasErrorUrlCount + "]";
	}
}
