package com.ensense.insense.core.analytics.model;


public class AnalyticsData {

	private String applicationName;
	private String pageTitle;
	private String pageName;
	private String url;
	private String tagName;
	
	private String tagType;
	private String tagValue;
	private boolean baseline;
	private String testStatus;
	private String refererUrl;
	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName.trim();
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		if ( tagType != null )
		this.tagType = tagType.trim();
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		if ( tagValue !=null )
		this.tagValue = tagValue.trim();
	}
	
	public boolean isBaseline() {
		return baseline;
	}

	public void setBaseline(boolean baseline) {
		this.baseline = baseline;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		if ( testStatus != null )
			this.testStatus = testStatus.trim();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName.trim();
	}

	
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	
	public String getRefererUrl() {
		return refererUrl;
	}

	public void setRefererUrl(String refererUrl) {
		this.refererUrl = refererUrl;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (!(o instanceof AnalyticsData)) {
        	return false;
        }
        AnalyticsData key = (AnalyticsData) o;
        return pageTitle == key.getPageTitle() && url == key.getUrl() && tagName == key.getTagName();
    }

    @Override
    public int hashCode() {
        return getPageTitle().hashCode() + getUrl().hashCode() + getTagName().hashCode();
    }

	@Override
	public String toString() {
		return "AnalyticsData [applicationName=" + applicationName
				+ ", pageTitle=" + pageTitle + ", pageName=" + pageName
				+ ", url=" + url + ", tagName=" + tagName + ", tagType="
				+ tagType + ", tagValue=" + tagValue + ", baseline=" + baseline
				+ ", testStatus=" + testStatus + ", refererUrl=" + refererUrl
				+ "]";
	}

}
