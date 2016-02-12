package com.ensense.insense.data.analytics.model;

import com.ensense.insense.services.analytics.model.WebAnalyticsTagData;

import java.util.Date;
import java.util.Map;

//app->page(per app)->tags(perpage)
public class WebAnalyticsPageData {
	private String applicationName;
	private String pageTitle;
	private String harLogFileName;
	private String pageRef; // page id from har log
	private String pageUrl;
	// private WebAnalyticsTagData webAnalyticsTagData;
	// List<WebAnalyticsTagData> webAnalyticsTagDataList; //list of all tag data
	// on the page
	Map<String, WebAnalyticsTagData> webAnalyticsTagDataMap;
	// will assign the last entries datetime
	private Date startedDateTime;

	/**
	 * @return the startedDateTime
	 */
	public Date getStartedDateTime() {
		return startedDateTime;
	}

	/**
	 * @param startedDateTime
	 *            the startedDateTime to set
	 */
	public void setStartedDateTime(Date startedDateTime) {
		this.startedDateTime = startedDateTime;
	}

	/**
	 * @return the pageUrl
	 */
	public String getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param pageUrl
	 *            the pageUrl to set
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the harLogFileName
	 */
	public String getHarLogFileName() {
		return harLogFileName;
	}

	/**
	 * @param harLogFileName
	 *            the harLogFileName to set
	 */
	public void setHarLogFileName(String harLogFileName) {
		this.harLogFileName = harLogFileName;
	}

	/**
	 * @return the pageRef
	 */
	public String getPageRef() {
		return pageRef;
	}

	/**
	 * @param pageRef
	 *            the pageRef to set
	 */
	public void setPageRef(String pageRef) {
		this.pageRef = pageRef;
	}

	/**
	 * @return the webAnalyticsTagDataMap
	 */
	public Map<String, WebAnalyticsTagData> getWebAnalyticsTagDataMap() {
		return webAnalyticsTagDataMap;
	}

	/**
	 * @param webAnalyticsTagDataMap
	 *            the webAnalyticsTagDataMap to set
	 */
	public void setWebAnalyticsTagDataMap(
			Map<String, WebAnalyticsTagData> webAnalyticsTagDataMap) {
		this.webAnalyticsTagDataMap = webAnalyticsTagDataMap;
	}

	@Override
	public String toString() {
		return "WebAnalyticsPageData [applicationName=" + applicationName
				+ ", pageTitle=" + pageTitle + ", harLogFileName="
				+ harLogFileName + ", pageRef=" + pageRef + ", pageUrl="
				+ pageUrl + ", webAnalyticsTagDataMap="
				+ webAnalyticsTagDataMap + ", startedDateTime="
				+ startedDateTime + "]";
	}

}
