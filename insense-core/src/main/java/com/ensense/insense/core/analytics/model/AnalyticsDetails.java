/**
 * 
 */
package com.ensense.insense.core.analytics.model;

import java.io.Serializable;
import java.util.Map;

import com.cts.mint.common.model.Link;
import com.cts.mint.common.model.PartialText;
import com.cts.mint.crawler.MintProxyServer;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;

/**
 * @author 361494
 *
 */
public class AnalyticsDetails implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer scheduleExecutionId;
	private String baseLineAppName;
	private String currentRunAppName;
	private String pageTitle; // pageTitle
	private String baseLinePageUrl;
	private String currentRunPageUrl;
	private String expectedTagName;
	private String actualTagName;
	private String expectedTagVarName;
	private String expectedTagVarValue;
	private String actualTagVarValue;
	private String testResult;
	private String lastRecordedTime;
	
	Map<String, WebAnalyticsTagData> baselineTagDataMap;
	Map<String, WebAnalyticsTagData> CurrentTagDataMap;
	public Integer getScheduleExecutionId() {
		return scheduleExecutionId;
	}
	public void setScheduleExecutionId(Integer scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}
	public String getBaseLineAppName() {
		return baseLineAppName;
	}
	public void setBaseLineAppName(String baseLineAppName) {
		this.baseLineAppName = baseLineAppName;
	}
	public String getCurrentRunAppName() {
		return currentRunAppName;
	}
	public void setCurrentRunAppName(String currentRunAppName) {
		this.currentRunAppName = currentRunAppName;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getBaseLinePageUrl() {
		return baseLinePageUrl;
	}
	public void setBaseLinePageUrl(String baseLinePageUrl) {
		this.baseLinePageUrl = baseLinePageUrl;
	}
	public String getCurrentRunPageUrl() {
		return currentRunPageUrl;
	}
	public void setCurrentRunPageUrl(String currentRunPageUrl) {
		this.currentRunPageUrl = currentRunPageUrl;
	}
	public String getExpectedTagName() {
		return expectedTagName;
	}
	public void setExpectedTagName(String expectedTagName) {
		this.expectedTagName = expectedTagName;
	}
	public String getActualTagName() {
		return actualTagName;
	}
	public void setActualTagName(String actualTagName) {
		this.actualTagName = actualTagName;
	}
	public String getExpectedTagVarName() {
		return expectedTagVarName;
	}
	public void setExpectedTagVarName(String expectedTagVarName) {
		this.expectedTagVarName = expectedTagVarName;
	}
	public String getExpectedTagVarValue() {
		return expectedTagVarValue;
	}
	public void setExpectedTagVarValue(String expectedTagVarValue) {
		this.expectedTagVarValue = expectedTagVarValue;
	}
	public String getActualTagVarValue() {
		return actualTagVarValue;
	}
	public void setActualTagVarValue(String actualTagVarValue) {
		this.actualTagVarValue = actualTagVarValue;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getLastRecordedTime() {
		return lastRecordedTime;
	}
	public void setLastRecordedTime(String lastRecordedTime) {
		this.lastRecordedTime = lastRecordedTime;
	}
	public Map<String, WebAnalyticsTagData> getBaselineTagDataMap() {
		return baselineTagDataMap;
	}
	public void setBaselineTagDataMap(
			Map<String, WebAnalyticsTagData> baselineTagDataMap) {
		this.baselineTagDataMap = baselineTagDataMap;
	}
	public Map<String, WebAnalyticsTagData> getCurrentTagDataMap() {
		return CurrentTagDataMap;
	}
	public void setCurrentTagDataMap(
			Map<String, WebAnalyticsTagData> currentTagDataMap) {
		CurrentTagDataMap = currentTagDataMap;
	}
	@Override
	public String toString() {
		return "AnalyticsDetails [scheduleExecutionId=" + scheduleExecutionId
				+ ", baseLineAppName=" + baseLineAppName
				+ ", currentRunAppName=" + currentRunAppName + ", pageTitle="
				+ pageTitle + ", baseLinePageUrl=" + baseLinePageUrl
				+ ", currentRunPageUrl=" + currentRunPageUrl
				+ ", expectedTagName=" + expectedTagName + ", actualTagName="
				+ actualTagName + ", expectedTagVarName=" + expectedTagVarName
				+ ", expectedTagVarValue=" + expectedTagVarValue
				+ ", actualTagVarValue=" + actualTagVarValue + ", testResult="
				+ testResult + ", lastRecordedTime=" + lastRecordedTime
				+ ", baselineTagDataMap=" + baselineTagDataMap
				+ ", CurrentTagDataMap=" + CurrentTagDataMap + "]";
	}
	
}
