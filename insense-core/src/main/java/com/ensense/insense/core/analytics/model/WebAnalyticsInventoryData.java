package com.ensense.insense.core.analytics.model;

import java.util.Set;

public class WebAnalyticsInventoryData {
	 @Override
	public String toString() {
		return "WebAnalyticsInventoryData [pageUrl=" + pageUrl
				+ ", responseCode=" + responseCode + ", pageName=" + pageName
				+ ", pageTitle=" + pageTitle + ", appName=" + appName
				+ ", appType=" + appType + ", tagsList=" + tagsSet + "]";
	}
	String pageUrl;
	 int responseCode;//!=200 or redirected to error page
	 String pageName;//  //sitecatalyst pageName variable
	 String pageTitle;
	 String appName;
	 String appType; //Atom/portal
	 Set<String> tagsSet;
	 String tagUrl;
	 String tagName;
	 String tagType;
	 long tagSize;

	 
	public long getTagSize() {
		return tagSize;
	}
	public void setTagSize(long tagSize) {
		this.tagSize = tagSize;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagType() {
		return tagType;
	}
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	public String getTagUrl() {
		return tagUrl;
	}
	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}
	public Set<String> getTagsList() {
		return tagsSet;
	}
	public void setTagsList(Set<String> tagsList) {
		this.tagsSet = tagsList;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}

	 
	 

}
