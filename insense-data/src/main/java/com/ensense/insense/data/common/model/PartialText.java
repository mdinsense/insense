package com.ensense.insense.data.common.model;

public class PartialText {
	private String contentName;
	private String htmlTagPath;;
	private String baseLineStatus;
	private String status;
	private int percentageMatch;
	private int percentageBaselineMatch;
	private String partialTextPath;
	
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getHtmlTagPath() {
		return htmlTagPath;
	}
	public void setHtmlTagPath(String htmlTagPath) {
		this.htmlTagPath = htmlTagPath;
	}
	public String getBaseLineStatus() {
		return baseLineStatus;
	}
	public void setBaseLineStatus(String baseLineStatus) {
		this.baseLineStatus = baseLineStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPercentageMatch() {
		return percentageMatch;
	}
	public void setPercentageMatch(int percentageMatch) {
		this.percentageMatch = percentageMatch;
	}
	public int getPercentageBaselineMatch() {
		return percentageBaselineMatch;
	}
	public void setPercentageBaselineMatch(int percentageBaselineMatch) {
		this.percentageBaselineMatch = percentageBaselineMatch;
	}
	
	public String getPartialTextPath() {
		return partialTextPath;
	}
	public void setPartialTextPath(String partialTextPath) {
		this.partialTextPath = partialTextPath;
	}
	@Override
	public String toString() {
		return "PartialText [contentName=" + contentName + ", htmlTagPath="
				+ htmlTagPath + ", baseLineStatus=" + baseLineStatus
				+ ", status=" + status + ", percentageMatch=" + percentageMatch
				+ ", percentageBaselineMatch=" + percentageBaselineMatch
				+ ", partialTextPath=" + partialTextPath + "]";
	}
}
