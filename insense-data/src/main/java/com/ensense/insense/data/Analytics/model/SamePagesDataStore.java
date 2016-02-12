package com.ensense.insense.data.analytics.model;


//store the the baseline and testdata page object here if key matches.
public class SamePagesDataStore {
	
	/**
	 * @return the webAnalyticsPageDataBaseline
	 */
	public WebAnalyticsPageData getWebAnalyticsPageDataBaseline() {
		return webAnalyticsPageDataBaseline;
	}
	/**
	 * @param webAnalyticsPageDataBaseline the webAnalyticsPageDataBaseline to set
	 */
	public void setWebAnalyticsPageDataBaseline(
			WebAnalyticsPageData webAnalyticsPageDataBaseline) {
		this.webAnalyticsPageDataBaseline = webAnalyticsPageDataBaseline;
	}
	/**
	 * @return the webAnalyticsPageDataTestData
	 */
	public WebAnalyticsPageData getWebAnalyticsPageDataTestData() {
		return webAnalyticsPageDataTestData;
	}
	/**
	 * @param webAnalyticsPageDataTestData the webAnalyticsPageDataTestData to set
	 */
	public void setWebAnalyticsPageDataTestData(
			WebAnalyticsPageData webAnalyticsPageDataTestData) {
		this.webAnalyticsPageDataTestData = webAnalyticsPageDataTestData;
	}
	WebAnalyticsPageData webAnalyticsPageDataBaseline;
	WebAnalyticsPageData webAnalyticsPageDataTestData;
	

}
