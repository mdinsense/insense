package com.ensense.insense.core.analytics.model;


import com.ensense.insense.core.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData;

/**
 * The class for the detailed_view_table database table based on newer xml structure.
 *  This has all columns and the unmarshalled xml column
 * 
 */
public class DetailedViewContents {

private int scheduleId;
	
	private String applicationName;
	
	private String pageURL;
	
	private String pageTitle;
	
	private String harLogFileName;
	
	private int detailedViewId;
	
	private DetailedViewWebAnalyticsTagData webAnalyticsTagData;

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

	public DetailedViewWebAnalyticsTagData getWebAnalyticsTagData() {
		return webAnalyticsTagData;
	}

	public void setWebAnalyticsTagData(DetailedViewWebAnalyticsTagData webAnalyticsTagData) {
		this.webAnalyticsTagData = webAnalyticsTagData;
	}

	
	
	public int getDetailedViewId() {
		return detailedViewId;
	}

	public void setDetailedViewId(int detailedViewId) {
		this.detailedViewId = detailedViewId;
	}

	@Override
	public String toString() {
		return "DetailedViewContents [scheduleId=" + scheduleId
				+ ", applicationName=" + applicationName + ", pageURL="
				+ pageURL + ", pageTitle=" + pageTitle + ", harLogFileName="
				+ harLogFileName + ", detailedViewId =" + detailedViewId + ", webAnalyticsTagData="
				+ webAnalyticsTagData + "]";
	}

	
}
