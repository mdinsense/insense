package com.ensense.insense.core.analytics.utils;

public class AnalyticsSummaryReportData {
	
	public int getBaseLineTotalPagesCount() {
		return baseLineTotalPagesCount;
	}
	public void setBaseLineTotalPagesCount(int baseLineTotalPagesCount) {
		this.baseLineTotalPagesCount = baseLineTotalPagesCount;
	}
	String appNameAudited;
	
	int baseLinePageCount=0;
	@Tags(value="Total Pages")
	int testDataPageCount=0;
	@Tags(value="Site Catalyst")
		int testDataPageHasSCCount=0;
	
	@Tags(value="Times SiteCatalyst fired")
	int testDataSiteCatalystCount=0;
	
	@Tags(value="Google Analytics")
	int testDataPageHasGACount=0;
	
	@Tags(value="Core Metrics")
	int testDataPageHasCoreMetricsCount=0;
	
	@Tags(value="Times GoogleAnalytics fired")
	int testDataGoogleAnalyticsCount=0;
	
	@Tags(value="Times Core Metrics fired")
	int testDataCoreMetricsCount=0;
	
	@Tags(value="Comscore")
	int testDataPageHasCSCount=0;
	
	@Tags(value="Times comscore fired")
	int testDataComScoreCount=0;
	
	@Tags(value="Times Gomez fired")
	int testDataPageHasGomezCount=0;
	
	@Tags(value="Gomez")
	int testDataGomezCount=0;
	
	@Tags(value="Add This")
	int testDataPageHasATCount=0;
	
	@Tags(value="Times Add This fired")
	int testDataAddThisCount=0;
	
	@Tags(value="Double Click")
	int testDataPageHasDCCount=0;
	
	@Tags(value="Times Double Click fired")
	int testDataDoubleClickCount=0;
	
	@Tags(value="Broken links")
	int brokenURLCount=0;
	
	@Tags(value="Status 200 -ok")
	int status200Count=0;
	
	
	public int getTestDataPageHasCSCount() {
		return testDataPageHasCSCount;
	}
	public void setTestDataPageHasCSCount(int testDataPageHasCSCount) {
		this.testDataPageHasCSCount = testDataPageHasCSCount;
	}
	public int getTestDataPageHasGACount() {
		return testDataPageHasGACount;
	}
	public void setTestDataPageHasGACount(int testDataPageHasGACount) {
		this.testDataPageHasGACount = testDataPageHasGACount;
	}
	int baseLineSiteCatalystCount=0;
	
	
	
	
	@Tags(value="Trans pages audited") /// temporary value
	int baseLinePageHasSCCount=0;
	

	
	
	public int getTestDataGoogleAnalyticsCount() {
		return testDataGoogleAnalyticsCount;
	}
	public void setTestDataGoogleAnalyticsCount(int testDataGoogleAnalyticsCount) {
		this.testDataGoogleAnalyticsCount = testDataGoogleAnalyticsCount;
	}
	public int getTestDataComScoreCount() {
		return testDataComScoreCount;
	}
	public void setTestDataComScoreCount(int testDataComScoreCount) {
		this.testDataComScoreCount = testDataComScoreCount;
	}

	
	int baseLineGomezCount=0;
	
	
	int baseLinePageHasGomezCount=0;
	
	
	
	

	public int getBrokenURLCount() {
		return brokenURLCount;
	}
	public void setBrokenURLCount(int brokenURLCount) {
		this.brokenURLCount = brokenURLCount;
	}
	public int getStatus200Count() {
		return status200Count;
	}
	public void setStatus200Count(int status200Count) {
		this.status200Count = status200Count;
	}
	
	
	int baseLineAddThisCount=0;
	//int testDataAddThisCount=0;
	
	@Tags(value="non-trans Pages audited")	
	int baseLineTotalPagesCount=0;
	
	int baseLinePageHasATCount=0;
//	int testDataPageHasATCount=0;
	
	int baseLineDoubleClickCount=0;
	int baseLineGoogleAnalyticsCount=0;
//	int testDataDoubleClickCount=0;
	int baseLinePageHasDCCount=0;
	int baseLinePageHasGACount=0;
//	int testDataPageHasDCCount=0;
	int baseLineCoreMetricsCount=0;
	int baseLinePageHasCoreMetricsCount=0;
	
	int baseLinePagesWithNoSupportedTagsCount=0;
	int testDataPagesWithNoSupportedTagsCount=0;
	
	/**
	 * @return the appNameAudited
	 */
	public String getAppNameAudited() {
		return appNameAudited;
	}
	/**
	 * @param appNameAudited the appNameAudited to set
	 */
	public void setAppNameAudited(String appNameAudited) {
		this.appNameAudited = appNameAudited;
	}
	/**
	 * @return the baseLinePageCount
	 */
	public int getBaseLinePageCount() {
		return baseLinePageCount;
	}
	/**
	 * @param baseLinePageCount the baseLinePageCount to set
	 */
	public void setBaseLinePageCount(int baseLinePageCount) {
		this.baseLinePageCount = baseLinePageCount;
	}
	/**
	 * @return the testDataPageCount
	 */
	public int getTestDataPageCount() {
		return testDataPageCount;
	}
	/**
	 * @param testDataPageCount the testDataPageCount to set
	 */
	public void setTestDataPageCount(int testDataPageCount) {
		this.testDataPageCount = testDataPageCount;
	}
	/**
	 * @return the baseLineSiteCatalystCount
	 */
	public int getBaseLineSiteCatalystCount() {
		return baseLineSiteCatalystCount;
	}
	/**
	 * @param baseLineSiteCatalystCount the baseLineSiteCatalystCount to set
	 */
	public void setBaseLineSiteCatalystCount(int baseLineSiteCatalystCount) {
		this.baseLineSiteCatalystCount = baseLineSiteCatalystCount;
	}
	/**
	 * @return the testDataSiteCatalystCount
	 */
	public int getTestDataSiteCatalystCount() {
		return testDataSiteCatalystCount;
	}
	/**
	 * @param testDataSiteCatalystCount the testDataSiteCatalystCount to set
	 */
	public void setTestDataSiteCatalystCount(int testDataSiteCatalystCount) {
		this.testDataSiteCatalystCount = testDataSiteCatalystCount;
	}
	/**
	 * @return the baseLinePageHasSCCount
	 */
	public int getBaseLinePageHasSCCount() {
		return baseLinePageHasSCCount;
	}
	/**
	 * @param baseLinePageHasSCCount the baseLinePageHasSCCount to set
	 */
	public void setBaseLinePageHasSCCount(int baseLinePageHasSCCount) {
		this.baseLinePageHasSCCount = baseLinePageHasSCCount;
	}
	/**
	 * @return the testDataPageHasSCCount
	 */
	public int getTestDataPageHasSCCount() {
		return testDataPageHasSCCount;
	}
	/**
	 * @param testDataPageHasSCCount the testDataPageHasSCCount to set
	 */
	public void setTestDataPageHasSCCount(int testDataPageHasSCCount) {
		this.testDataPageHasSCCount = testDataPageHasSCCount;
	}
	/**
	 * @return the baseLineGomezCount
	 */
	public int getBaseLineGomezCount() {
		return baseLineGomezCount;
	}
	/**
	 * @param baseLineGomezCount the baseLineGomezCount to set
	 */
	public void setBaseLineGomezCount(int baseLineGomezCount) {
		this.baseLineGomezCount = baseLineGomezCount;
	}
	/**
	 * @return the testDataGomezCount
	 */
	public int getTestDataGomezCount() {
		return testDataGomezCount;
	}
	/**
	 * @param testDataGomezCount the testDataGomezCount to set
	 */
	public void setTestDataGomezCount(int testDataGomezCount) {
		this.testDataGomezCount = testDataGomezCount;
	}
	/**
	 * @return the baseLinePageHasGomezCount
	 */
	public int getBaseLinePageHasGomezCount() {
		return baseLinePageHasGomezCount;
	}
	/**
	 * @param baseLinePageHasGomezCount the baseLinePageHasGomezCount to set
	 */
	public void setBaseLinePageHasGomezCount(int baseLinePageHasGomezCount) {
		this.baseLinePageHasGomezCount = baseLinePageHasGomezCount;
	}
	/**
	 * @return the testDataPageHasGomezCount
	 */
	public int getTestDataPageHasGomezCount() {
		return testDataPageHasGomezCount;
	}
	/**
	 * @param testDataPageHasGomezCount the testDataPageHasGomezCount to set
	 */
	public void setTestDataPageHasGomezCount(int testDataPageHasGomezCount) {
		this.testDataPageHasGomezCount = testDataPageHasGomezCount;
	}
	/**
	 * @return the baseLineAddThisCount
	 */
	public int getBaseLineAddThisCount() {
		return baseLineAddThisCount;
	}
	/**
	 * @param baseLineAddThisCount the baseLineAddThisCount to set
	 */
	public void setBaseLineAddThisCount(int baseLineAddThisCount) {
		this.baseLineAddThisCount = baseLineAddThisCount;
	}
	/**
	 * @return the testDataAddThisCount
	 */
	public int getTestDataAddThisCount() {
		return testDataAddThisCount;
	}
	/**
	 * @param testDataAddThisCount the testDataAddThisCount to set
	 */
	public void setTestDataAddThisCount(int testDataAddThisCount) {
		this.testDataAddThisCount = testDataAddThisCount;
	}
	/**
	 * @return the baseLinePageHasATCount
	 */
	public int getBaseLinePageHasATCount() {
		return baseLinePageHasATCount;
	}
	/**
	 * @param baseLinePageHasATCount the baseLinePageHasATCount to set
	 */
	public void setBaseLinePageHasATCount(int baseLinePageHasATCount) {
		this.baseLinePageHasATCount = baseLinePageHasATCount;
	}
	/**
	 * @return the testDataPageHasATCount
	 */
	public int getTestDataPageHasATCount() {
		return testDataPageHasATCount;
	}
	/**
	 * @param testDataPageHasATCount the testDataPageHasATCount to set
	 */
	public void setTestDataPageHasATCount(int testDataPageHasATCount) {
		this.testDataPageHasATCount = testDataPageHasATCount;
	}
	/**
	 * @return the baseLineDoubleClickCount
	 */
	public int getBaseLineDoubleClickCount() {
		return baseLineDoubleClickCount;
	}
	/**
	 * @param baseLineDoubleClickCount the baseLineDoubleClickCount to set
	 */
	public void setBaseLineDoubleClickCount(int baseLineDoubleClickCount) {
		this.baseLineDoubleClickCount = baseLineDoubleClickCount;
	}
	/**
	 * @return the testDataDoubleClickCount
	 */
	public int getTestDataDoubleClickCount() {
		return testDataDoubleClickCount;
	}
	/**
	 * @param testDataDoubleClickCount the testDataDoubleClickCount to set
	 */
	public void setTestDataDoubleClickCount(int testDataDoubleClickCount) {
		this.testDataDoubleClickCount = testDataDoubleClickCount;
	}
	/**
	 * @return the baseLinePageHasDCCount
	 */
	public int getBaseLinePageHasDCCount() {
		return baseLinePageHasDCCount;
	}
	/**
	 * @param baseLinePageHasDCCount the baseLinePageHasDCCount to set
	 */
	public void setBaseLinePageHasDCCount(int baseLinePageHasDCCount) {
		this.baseLinePageHasDCCount = baseLinePageHasDCCount;
	}
	
	
	public int getBaseLineGoogleAnalyticsCount() {
		return baseLineGoogleAnalyticsCount;
	}
	public void setBaseLineGoogleAnalyticsCount(int baseLineGoogleAnalyticsCount) {
		this.baseLineGoogleAnalyticsCount = baseLineGoogleAnalyticsCount;
	}

	public int getBaseLinePageHasGACount() {
		return baseLinePageHasGACount;
	}
	public void setBaseLinePageHasGACount(int baseLinePageHasGACount) {
		this.baseLinePageHasGACount = baseLinePageHasGACount;
	}
	/**
	 * @return the testDataPageHasDCCount
	 */
	public int getTestDataPageHasDCCount() {
		return testDataPageHasDCCount;
	}
	/**
	 * @param testDataPageHasDCCount the testDataPageHasDCCount to set
	 */
	public void setTestDataPageHasDCCount(int testDataPageHasDCCount) {
		this.testDataPageHasDCCount = testDataPageHasDCCount;
	}
	/**
	 * @return the baseLinePagesWithNoSupportedTagsCount
	 */
	public int getBaseLinePagesWithNoSupportedTagsCount() {
		return baseLinePagesWithNoSupportedTagsCount;
	}
	/**
	 * @param baseLinePagesWithNoSupportedTagsCount the baseLinePagesWithNoSupportedTagsCount to set
	 */
	public void setBaseLinePagesWithNoSupportedTagsCount(
			int baseLinePagesWithNoSupportedTagsCount) {
		this.baseLinePagesWithNoSupportedTagsCount = baseLinePagesWithNoSupportedTagsCount;
	}
	/**
	 * @return the testDataPagesWithNoSupportedTagsCount
	 */
	public int getTestDataPagesWithNoSupportedTagsCount() {
		return testDataPagesWithNoSupportedTagsCount;
	}
	/**
	 * @param testDataPagesWithNoSupportedTagsCount the testDataPagesWithNoSupportedTagsCount to set
	 */
	public void setTestDataPagesWithNoSupportedTagsCount(
			int testDataPagesWithNoSupportedTagsCount) {
		this.testDataPagesWithNoSupportedTagsCount = testDataPagesWithNoSupportedTagsCount;
	}
	
	
	public int getTestDataPageHasCoreMetricsCount() {
		return testDataPageHasCoreMetricsCount;
	}
	public void setTestDataPageHasCoreMetricsCount(
			int testDataPageHasCoreMetricsCount) {
		this.testDataPageHasCoreMetricsCount = testDataPageHasCoreMetricsCount;
	}
	public int getTestDataCoreMetricsCount() {
		return testDataCoreMetricsCount;
	}
	public void setTestDataCoreMetricsCount(int testDataCoreMetricsCount) {
		this.testDataCoreMetricsCount = testDataCoreMetricsCount;
	}
	public int getBaseLineCoreMetricsCount() {
		return baseLineCoreMetricsCount;
	}
	public void setBaseLineCoreMetricsCount(int baseLineCoreMetricsCount) {
		this.baseLineCoreMetricsCount = baseLineCoreMetricsCount;
	}
	public int getBaseLinePageHasCoreMetricsCount() {
		return baseLinePageHasCoreMetricsCount;
	}
	
	public void setBaseLinePageHasCoreMetricsCount(
			int baseLinePageHasCoreMetricsCount) {
		this.baseLinePageHasCoreMetricsCount = baseLinePageHasCoreMetricsCount;
	}
	@Override
	public String toString() {
		return "AnalyticsSummaryReportData [appNameAudited=" + appNameAudited
				+ ", baseLinePageCount=" + baseLinePageCount
				+ ", testDataPageCount=" + testDataPageCount
				+ ", testDataPageHasSCCount=" + testDataPageHasSCCount
				+ ", testDataSiteCatalystCount=" + testDataSiteCatalystCount
				+ ", testDataPageHasGACount=" + testDataPageHasGACount
				+ ", testDataPageHasCoreMetricsCount="
				+ testDataPageHasCoreMetricsCount
				+ ", testDataGoogleAnalyticsCount="
				+ testDataGoogleAnalyticsCount + ", testDataCoreMetricsCount="
				+ testDataCoreMetricsCount + ", testDataPageHasCSCount="
				+ testDataPageHasCSCount + ", testDataComScoreCount="
				+ testDataComScoreCount + ", testDataPageHasGomezCount="
				+ testDataPageHasGomezCount + ", testDataGomezCount="
				+ testDataGomezCount + ", testDataPageHasATCount="
				+ testDataPageHasATCount + ", testDataAddThisCount="
				+ testDataAddThisCount + ", testDataPageHasDCCount="
				+ testDataPageHasDCCount + ", testDataDoubleClickCount="
				+ testDataDoubleClickCount + ", brokenURLCount="
				+ brokenURLCount + ", status200Count=" + status200Count
				+ ", baseLineSiteCatalystCount=" + baseLineSiteCatalystCount
				+ ", baseLinePageHasSCCount=" + baseLinePageHasSCCount
				+ ", baseLineGomezCount=" + baseLineGomezCount
				+ ", baseLinePageHasGomezCount=" + baseLinePageHasGomezCount
				+ ", baseLineAddThisCount=" + baseLineAddThisCount
				+ ", baseLineTotalPagesCount=" + baseLineTotalPagesCount
				+ ", baseLinePageHasATCount=" + baseLinePageHasATCount
				+ ", baseLineDoubleClickCount=" + baseLineDoubleClickCount
				+ ", baseLinePageHasDCCount=" + baseLinePageHasDCCount
				+ ", baseLineCoreMetricsCount=" + baseLineCoreMetricsCount
				+ ", baseLinePageHasCoreMetricsCount="
				+ baseLinePageHasCoreMetricsCount
				+ ", baseLinePagesWithNoSupportedTagsCount="
				+ baseLinePagesWithNoSupportedTagsCount
				+ ", testDataPagesWithNoSupportedTagsCount="
				+ testDataPagesWithNoSupportedTagsCount + "]";
	}
	
}
