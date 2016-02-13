package com.ensense.insense.data.common.model;

import com.ensense.insense.data.common.utils.WebDriverListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CrawlConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7764462250551392784L;
	private List<String> excludeUrl;
	private List<String> includeUrl;
	private String removeUrlPattern;
	private boolean robotEnabled;
	private String jqueryFilePath;
	private int stopAfterUrlCount;
	private CrawlStatus crawlStatus;
	private int sessionRestart;
	
	private ScheduleDetails appConfig;
	private WebDriver driver;
	private WebDriverListener eventListener;
	
	private int pageCount;
	private int linkCount;
	
	private List<String> errorpageIdentifiers;
	
	private boolean processChildUrls;
	private String lastRecordedTime;
	private String startTime;
	private String endTime;
	private String userId;
	private ExecutionStatus executionAction;
	private List<UrlFormElement> urlFormElementList = new ArrayList<UrlFormElement>();
	
	public List<String> getExcludeUrl() {
		return excludeUrl;
	}
	public void setExcludeUrl(List<String> excludeUrl) {
		this.excludeUrl = excludeUrl;
	}
	public List<String> getIncludeUrl() {
		return includeUrl;
	}
	public void setIncludeUrl(List<String> includeUrl) {
		this.includeUrl = includeUrl;
	}
	public String getRemoveUrlPattern() {
		return removeUrlPattern;
	}
	public void setRemoveUrlPattern(String removeUrlPattern) {
		this.removeUrlPattern = removeUrlPattern;
	}
	public boolean isRobotEnabled() {
		return robotEnabled;
	}
	public void setRobotEnabled(boolean robotEnabled) {
		this.robotEnabled = robotEnabled;
	}
	public String getJqueryFilePath() {
		return jqueryFilePath;
	}
	public void setJqueryFilePath(String jqueryFilePath) {
		this.jqueryFilePath = jqueryFilePath;
	}
	public int getStopAfterUrlCount() {
		return stopAfterUrlCount;
	}
	public void setStopAfterUrlCount(int stopAfterUrlCount) {
		this.stopAfterUrlCount = stopAfterUrlCount;
	}
	public synchronized CrawlStatus getCrawlStatus() {
		if ( null == crawlStatus ){
			crawlStatus = new CrawlStatus();
		}
		return crawlStatus;
	}
	public void setCrawlStatus(CrawlStatus crawlStatus) {
		this.crawlStatus = crawlStatus;
	}
	public int getSessionRestart() {
		return sessionRestart;
	}
	public void setSessionRestart(int sessionRestart) {
		this.sessionRestart = sessionRestart;
	}
	public ScheduleDetails getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(ScheduleDetails appConfig) {
		this.appConfig = appConfig;
	}
	public WebDriver getDriver() {
		return driver;
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	public WebDriverListener getEventListener() {
		return eventListener;
	}
	public void setEventListener(WebDriverListener eventListener) {
		this.eventListener = eventListener;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public synchronized void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public synchronized void incrementPageCount(){
		this.pageCount++;
	}
	public int getLinkCount() {
		return linkCount;
	}
	public void setLinkCount(int linkCount) {
		this.linkCount = linkCount;
	}
	
	public List<String> getErrorpageIdentifiers() {
		return errorpageIdentifiers;
	}
	public void setErrorpageIdentifiers(List<String> errorpageIdentifiers) {
		this.errorpageIdentifiers = errorpageIdentifiers;
	}
	public boolean isProcessChildUrls() {
		return processChildUrls;
	}
	public void setProcessChildUrls(boolean processChildUrls) {
		this.processChildUrls = processChildUrls;
	}
	public String getLastRecordedTime() {
		return lastRecordedTime;
	}
	public void setLastRecordedTime(String lastRecordedTime) {
		this.lastRecordedTime = lastRecordedTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ExecutionStatus getExecutionAction() {
		return executionAction;
	}
	public void setExecutionAction(ExecutionStatus executionAction) {
		this.executionAction = executionAction;
	}
	
		public void setUrlFormElementList(List<UrlFormElement> urlFormElementList) {
		this.urlFormElementList = urlFormElementList;
	}
	public List<UrlFormElement> getUrlFormElementList() {
		return urlFormElementList;
	}
	
	@Override
	public String toString() {
		return "CrawlConfig [excludeUrl=" + excludeUrl + ", includeUrl="
				+ includeUrl + ", removeUrlPattern=" + removeUrlPattern
				+ ", robotEnabled=" + robotEnabled + ", jqueryFilePath="
				+ jqueryFilePath + ", stopAfterUrlCount=" + stopAfterUrlCount
				+ ", crawlStatus=" + crawlStatus + ", sessionRestart="
				+ sessionRestart + ", appConfig=" + appConfig + ", driver="
				+ driver + ", eventListener=" + eventListener + ", pageCount="
				+ pageCount + ", linkCount=" + linkCount
				+ ", errorpageIdentifiers=" + errorpageIdentifiers
				+ ", processChildUrls=" + processChildUrls
				+ ", lastRecordedTime=" + lastRecordedTime + ", startTime="
				+ startTime + ", endTime=" + endTime
				+ ", userId=" + userId
				+ "]";
	}

}
