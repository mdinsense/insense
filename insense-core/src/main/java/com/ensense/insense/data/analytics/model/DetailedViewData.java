

package com.ensense.insense.data.analytics.model;

/**
 * The class for the detailed_view_table database table. This has all columns and the unmarshalled xml column
 * 
 */

public class DetailedViewData {
	
	private int scheduleId;
	
	private String applicationName;
	
	private String pageURL;
	
	private String pageTitle;
	
	private String harLogFileName;
	
	private WebAnalyticsTagData webAnalyticsPageDataList;
	
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getPageURL() {
		return pageURL;
	}
	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getHarLogFileName() {
		return harLogFileName;
	}
	public void setHarLogFileName(String harLogFileName) {
		this.harLogFileName = harLogFileName;
	}
	public WebAnalyticsTagData getWebAnalyticsPageDataList() {
		return webAnalyticsPageDataList;
	}
	public void setWebAnalyticsPageDataList(WebAnalyticsTagData webAnalyticsPageDataList) {
		this.webAnalyticsPageDataList = webAnalyticsPageDataList;
	}
	
	
	
 
}
