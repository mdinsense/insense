package com.ensense.insense.services.analytics.model;

import java.util.Date;
import java.util.Map;

public class WebAnalyticsTagData {

	// private String applicationName;
	// private String pageTitle;
	// private String pageName;
	private String tagUrl;
	private String tagName;// sitecatalyst, tealeaf, gomez, opinionlab, CoreMetrics
							// floodlight, etc
	private Map<String, String> tagVariableData; // contains variable and value
													// pairs, copied from Har
													// Pages query parameter
													// name, value
	// private String refererUrl;
	// identifier for storing the tag data in a map
	private String tagDataKey;
	// private String readFromFile;
	private Date startedDateTime;

	// no-arg constructor for initializing
	public WebAnalyticsTagData() {
		// super();
	}

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

	/*
	 * public String getApplicationName() { return applicationName; }
	 * 
	 * public void setApplicationName(String applicationName) {
	 * this.applicationName = applicationName; }
	 * 
	 * public String getPageTitle() { return pageTitle; }
	 * 
	 * 
	 * public void setPageTitle(String pageTitle) { this.pageTitle = pageTitle;
	 * } public String getPageName() { return pageName; } public void
	 * setPageName(String pageName) { this.pageName = pageName; }
	 */
	public String getTagUrl() {
		return tagUrl;
	}

	public void setTagUrl(String requestUrl) {
		this.tagUrl = requestUrl;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagType) {
		this.tagName = tagType;
	}

	public Map<String, String> getTagVariableData() {
		return tagVariableData;
	}

	public void setTagVariableData(Map<String, String> tagVariableData) {
		this.tagVariableData = tagVariableData;
	}

	/*
	 * public String getRefererUrl() { return refererUrl; } public void
	 * setRefererUrl(String refererUrl) { this.refererUrl = refererUrl; }
	 */

	public void setTagDataKey(String tagDataKey) {
		this.tagDataKey = tagDataKey;
	}

	public String getTagDataKey() {
		return tagDataKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tagDataKey == null) ? 0 : tagDataKey.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
		result = prime * result + ((tagUrl == null) ? 0 : tagUrl.hashCode());
		result = prime * result
				+ ((tagVariableData == null) ? 0 : tagVariableData.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebAnalyticsTagData other = (WebAnalyticsTagData) obj;
		if (tagDataKey == null) {
			if (other.tagDataKey != null)
				return false;
		} else if (!tagDataKey.equals(other.tagDataKey))
			return false;
		if (tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!tagName.equals(other.tagName))
			return false;
		if (tagUrl == null) {
			if (other.tagUrl != null)
				return false;
		} else if (!tagUrl.equals(other.tagUrl))
			return false;
		if (tagVariableData == null) {
			if (other.tagVariableData != null)
				return false;
		} else if (!tagVariableData.equals(other.tagVariableData))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebAnalyticsTagData [tagUrl=" + tagUrl + ", tagType=" + tagName
				+ ", tagVariableData=" + tagVariableData + ", tagDataKey="
				+ tagDataKey + "]";
	}

}
