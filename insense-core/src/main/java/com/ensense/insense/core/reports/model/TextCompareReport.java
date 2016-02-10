package com.ensense.insense.core.reports.model;

public class TextCompareReport {

	private int urlNo;
	private String url;
	private boolean isUrlfound;
	private int percentagematch;
	private String pageTitle;
	private String linkName;
	private boolean isNewUrl;
	private String navigationPath;
	private String parentUrl;
	
	public int getUrlNo() {
		return urlNo;
	}
	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isUrlfound() {
		return isUrlfound;
	}
	public void setUrlfound(boolean isUrlfound) {
		this.isUrlfound = isUrlfound;
	}
	public int getPercentagematch() {
		return percentagematch;
	}
	public void setPercentagematch(int percentagematch) {
		this.percentagematch = percentagematch;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public boolean isNewUrl() {
		return isNewUrl;
	}
	public void setNewUrl(boolean isNewUrl) {
		this.isNewUrl = isNewUrl;
	}
	
	public String getNavigationPath() {
		return navigationPath;
	}
	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	@Override
	public String toString() {
		return "TextCompareReport [urlNo=" + urlNo + ", url=" + url
				+ ", isUrlfound=" + isUrlfound + ", percentagematch="
				+ percentagematch + ", pageTitle=" + pageTitle + ", linkName="
				+ linkName + ", isNewUrl=" + isNewUrl + ", navigationPath="
				+ navigationPath + ", parentUrl=" + parentUrl + "]";
	}
}
