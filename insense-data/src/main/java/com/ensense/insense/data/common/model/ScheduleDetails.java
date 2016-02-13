/**
 * 
 */
package com.ensense.insense.data.common.model;

import com.ensense.insense.data.uitesting.entity.HtmlReportsConfig;
import com.ensense.insense.data.uitesting.entity.ScheduleScriptXref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 361494
 *
 */
public class ScheduleDetails implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int executionId;
	private String applicationName;
    private String environmentName;
    private String environmentCategory;
	private String url;
    private String loginId;
    private String password;
    private String securityAnswer;
    private String baseUrl;
    private boolean rsaEnabled;
    private String snapShotPath;
    private boolean testRunExecutionStatus;
    private int testScheduleId;
    private String error;
    private List<Link> navigationList;
    private int testLoginId;
    private String testFilePath;
    private String testFileName;
    private String navigationXmlFileContent;
	private int testExecutionStatusRefId;
    private int testCaseId;
    private boolean publicSite;
    private String harReportsPath;
    private String reportsPath;
    private boolean behindTheScene;
	private String dataCenter;
    
	private String browserType;
	private boolean regressionTesting;
	private boolean analyticsTesting;
	private int stopAfter;
	private boolean akamaiTesting;
	private Properties akamaiMapping;
	private Properties proxyAuthentication;
	private String akamaiUrl;
	private String directory;
	
	private int applicationId;
	private int environmentId;
	
	private String resultsBaseDirectory;
	
	private int noOfThreads;
	private String serializePath;
	
	private boolean emailOnCompletion;   
	private String emailIds;   
	private boolean emailOnFailure;  
	private boolean processChildUrls;
	private boolean robotClicking;
	private boolean brokenUrlReport;
	private int maxWaitTime;
	private int browserRestartCount;
	private String startTime;
	private String endTime;
	private boolean brandingUrlReport;
	private String userId;
	private int baselineScheduleId;
	private boolean staticUrlTesting;
	private ArrayList<String> staticUrlList;
	private String brandingReportsPath;
	private String executionTimeStamp;
	private boolean textCompare;
	private boolean htmlCompare;
	
	private String textFilePath;
	private String errorStackTrace;
	private String mobProxyHost;
	private String mobProxyPort;
	private String upstreamProxyHost;
	private String upstreamProxyPort;
	private List<String> includeUrl;
	private List<String> excludeUrl;
	
	private List<String> includeModuleUrlPatternList;
	private List<String> testModuleUrlPatternList;
	
	private boolean useBMPProxy;
	private MintProxyServer proxyServer;
	private boolean screenCompare;
	private String loginScriptDirectory;
	private String fireBugPath;
	private String netExportPath;
	private List<String> tiaaImageNameList;
	private List<String> textSearchList;
	private List<PartialText> partialTextList;
	private String crawlStatusDirectory;
	private int runStaus;
	private int urlLevel;
	private boolean transactionTesting;
	private List<ScheduleScriptXref> scheduleScriptList;
	private List<HtmlReportsConfig> htmlReportsConfigList;
	private String textImageDirectory;
	private CrawlConfig baseLineCrawlConfig;
	private boolean scrollPage;
	private int waitTime;
	
	public List<ScheduleScriptXref> getScheduleScriptList() {
		return scheduleScriptList;
	}
	public void setScheduleScriptList(List<ScheduleScriptXref> list) {
		this.scheduleScriptList = list;
	}
	public String getTextFilePath() {
		return textFilePath;
	}
	public void setTextFilePath(String textFilePath) {
		this.textFilePath = textFilePath;
	}
	public boolean isTextCompare() {
		return textCompare;
	}
	public void setTextCompare(boolean textCompare) {
		this.textCompare = textCompare;
	}
	public boolean isHtmlCompare() {
		return htmlCompare;
	}
	public void setHtmlCompare(boolean htmlCompare) {
		this.htmlCompare = htmlCompare;
	}
	public boolean isScreenCompare() {
		return screenCompare;
	}
	public void setScreenCompare(boolean screenCompare) {
		this.screenCompare = screenCompare;
	}

    public String getEnvironmentCategory() {
		return environmentCategory;
	}
	public void setEnvironmentCategory(String environmentCategory) {
		this.environmentCategory = environmentCategory;
	}

    public String getExecutionTimeStamp() {
		return executionTimeStamp;
	}
	public void setExecutionTimeStamp(String executionTimeStamp) {
		this.executionTimeStamp = executionTimeStamp;
	}
	public String getNavigationXmlFileContent() {
		return navigationXmlFileContent;
	}
	public void setNavigationXmlFileContent(String navigationXmlFileContent) {
		this.navigationXmlFileContent = navigationXmlFileContent;
	}
	public int getExecutionId() {
		return executionId;
	}
	public void setExecutionId(int executionId) {
		this.executionId = executionId;
	}
    public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public boolean isRsaEnabled() {
		return rsaEnabled;
	}
	public void setRsaEnabled(boolean rsaEnabled) {
		this.rsaEnabled = rsaEnabled;
	}
	public String getSnapShotPath() {
		return snapShotPath;
	}
	public void setSnapShotPath(String snapShotPath) {
		this.snapShotPath = snapShotPath;
	}
	public boolean isTestRunExecutionStatus() {
		return testRunExecutionStatus;
	}
	public void setTestRunExecutionStatus(boolean testRunExecutionStatus) {
		this.testRunExecutionStatus = testRunExecutionStatus;
	}
	public int getTestExecutionStatusRefId() {
		return testExecutionStatusRefId;
	}
	public void setTestExecutionStatusRefId(int testExecutionStatusRefId) {
		this.testExecutionStatusRefId = testExecutionStatusRefId;
	}
	
	public int getTestScheduleId() {
		return testScheduleId;
	}
	public void setTestScheduleId(int testScheduleId) {
		this.testScheduleId = testScheduleId;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public List<Link> getNavigationList() {
		return navigationList;
	}
	public void setNavigationList(List<Link> navigationList) {
		this.navigationList = navigationList;
	}
	
	
	public int getTestLoginId() {
		return testLoginId;
	}
	public void setTestLoginId(int testLoginId) {
		this.testLoginId = testLoginId;
	}
	
	public String getTestFilePath() {
		return testFilePath;
	}
	public void setTestFilePath(String testFilePath) {
		this.testFilePath = testFilePath;
	}
	public String getTestFileName() {
		return testFileName;
	}
	public void setTestFileName(String testFileName) {
		this.testFileName = testFileName;
	}
	
	
	public int getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(int testCaseId) {
		this.testCaseId = testCaseId;
	}
	
	public boolean isPublicSite() {
		return publicSite;
	}
	public void setPublicSite(boolean publicSite) {
		this.publicSite = publicSite;
	}
	
	
	public String getHarReportsPath() {
		return harReportsPath;
	}
	public void setHarReportsPath(String harReportsPath) {
		this.harReportsPath = harReportsPath;
	}
	
	public boolean isBehindTheScene()
	{
		return behindTheScene;
	}

	public void setBehindTheScene(boolean behindTheScene)
	{
		this.behindTheScene = behindTheScene;
	}

	public String getDataCenter()
	{
		return dataCenter;
	}

	public void setDataCenter(String dataCenter)
	{
		this.dataCenter = dataCenter;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public boolean isRegressionTesting() {
		return regressionTesting;
	}
	public void setRegressionTesting(boolean regressionTesting) {
		this.regressionTesting = regressionTesting;
	}
	public boolean isAnalyticsTesting() {
		return analyticsTesting;
	}
	public void setAnalyticsTesting(boolean analyticsTesting) {
		this.analyticsTesting = analyticsTesting;
	}
	public int getStopAfter() {
		return stopAfter;
	}
	public void setStopAfter(int stopAfter) {
		this.stopAfter = stopAfter;
	}
	
	public boolean isAkamaiTesting() {
		return akamaiTesting;
	}
	public void setAkamaiTesting(boolean akamaiTesting) {
		this.akamaiTesting = akamaiTesting;
	}
	
	public Properties getAkamaiMapping() {
		return akamaiMapping;
	}
	public void setAkamaiMapping(Properties akamaiMapping) {
		this.akamaiMapping = akamaiMapping;
	}
	
	public Properties getProxyAuthentication() {
		return proxyAuthentication;
	}
	public void setProxyAuthentication(Properties proxyAuthentication) {
		this.proxyAuthentication = proxyAuthentication;
	}
	
	public String getAkamaiUrl() {
		return akamaiUrl;
	}
	public void setAkamaiUrl(String akamaiUrl) {
		this.akamaiUrl = akamaiUrl;
	}
	
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public int getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}
	
	public String getResultsBaseDirectory() {
		return resultsBaseDirectory;
	}
	public void setResultsBaseDirectory(String resultsBaseDirectory) {
		this.resultsBaseDirectory = resultsBaseDirectory;
	}
	public int getNoOfThreads() {
		return noOfThreads;
	}
	public void setNoOfThreads(int noOfThreads) {
		this.noOfThreads = noOfThreads;
	}
	
	public String getSerializePath() {
		return serializePath;
	}
	public void setSerializePath(String serializePath) {
		this.serializePath = serializePath;
	}
	
	public boolean isEmailOnCompletion() {
		return emailOnCompletion;
	}
	public void setEmailOnCompletion(boolean emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}
	public String getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}
	public boolean isEmailOnFailure() {
		return emailOnFailure;
	}
	public void setEmailOnFailure(boolean emailOnFailure) {
		this.emailOnFailure = emailOnFailure;
	}
	public boolean isProcessChildUrls() {
		return processChildUrls;
	}
	public void setProcessChildUrls(boolean processChildUrls) {
		this.processChildUrls = processChildUrls;
	}
	public boolean isRobotClicking() {
		return robotClicking;
	}
	public void setRobotClicking(boolean robotClicking) {
		this.robotClicking = robotClicking;
	}
	public boolean isBrokenUrlReport() {
		return brokenUrlReport;
	}
	public void setBrokenUrlReport(boolean brokenUrlReport) {
		this.brokenUrlReport = brokenUrlReport;
	}
	public int getMaxWaitTime() {
		return maxWaitTime;
	}
	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	public int getBrowserRestartCount() {
		return browserRestartCount;
	}
	public void setBrowserRestartCount(int browserRestartCount) {
		this.browserRestartCount = browserRestartCount;
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
	public boolean isBrandingUrlReport() {
		return brandingUrlReport;
	}
	public void setBrandingUrlReport(boolean brandingUrlReport) {
		this.brandingUrlReport = brandingUrlReport;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getBaselineScheduleId() {
		return baselineScheduleId;
	}
	public void setBaselineScheduleId(int baselineScheduleId) {
		this.baselineScheduleId = baselineScheduleId;
	}
	public boolean isStaticUrlTesting() {
		return staticUrlTesting;
	}
	public void setStaticUrlTesting(boolean staticUrlTesting) {
		this.staticUrlTesting = staticUrlTesting;
	}
	public ArrayList<String> getStaticUrlList() {
		return staticUrlList;
	}
	public void setStaticUrlList(ArrayList<String> staticUrlList) {
		this.staticUrlList = staticUrlList;
	}
	public String getBrandingReportsPath() {
		return brandingReportsPath;
	}
	public void setBrandingReportsPath(String brandingReportsPath) {
		this.brandingReportsPath = brandingReportsPath;
	}
	public List<String> getTiaaImageNameList() {
		return tiaaImageNameList;
	}
	public void setTiaaImageNameList(List<String> tiaaImageNameList) {
		this.tiaaImageNameList = tiaaImageNameList;
	}
	
	public String getErrorStackTrace() {
		return errorStackTrace;
	}
	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}
	public String getMobProxyHost() {
		return mobProxyHost;
	}
	public void setMobProxyHost(String mobProxyHost) {
		this.mobProxyHost = mobProxyHost;
	}
	public String getMobProxyPort() {
		return mobProxyPort;
	}
	public void setMobProxyPort(String mobProxyPort) {
		this.mobProxyPort = mobProxyPort;
	}
	public String getUpstreamProxyHost() {
		return upstreamProxyHost;
	}
	public void setUpstreamProxyHost(String upstreamProxyHost) {
		this.upstreamProxyHost = upstreamProxyHost;
	}
	public String getUpstreamProxyPort() {
		return upstreamProxyPort;
	}
	public void setUpstreamProxyPort(String upstreamProxyPort) {
		this.upstreamProxyPort = upstreamProxyPort;
	}

	public List<String> getIncludeModuleUrlPatternList() {
		return includeModuleUrlPatternList;
	}
	public void setIncludeModuleUrlPatternList(
			List<String> includeModuleUrlPatternList) {
		this.includeModuleUrlPatternList = includeModuleUrlPatternList;
	}
	public List<String> getTestModuleUrlPatternList() {
		return testModuleUrlPatternList;
	}
	public void setTestModuleUrlPatternList(List<String> testModuleUrlPatternList) {
		this.testModuleUrlPatternList = testModuleUrlPatternList;
	}
	public List<String> getIncludeUrl() {
		return includeUrl;
	}
	public void setIncludeUrl(List<String> includeUrl) {
		this.includeUrl = includeUrl;
	}
	public List<String> getExcludeUrl() {
		return excludeUrl;
	}
	public void setExcludeUrl(List<String> excludeUrl) {
		this.excludeUrl = excludeUrl;
	}
	public boolean isUseBMPProxy() {
		return useBMPProxy;
	}
	public void setUseBMPProxy(boolean useBMPProxy) {
		this.useBMPProxy = useBMPProxy;
	}
	
	public MintProxyServer getProxyServer() {
		return proxyServer;
	}
	public void setProxyServer(MintProxyServer proxyServer) {
		this.proxyServer = proxyServer;
	}
	public String getLoginScriptDirectory() {
		return loginScriptDirectory;
	}
	public void setLoginScriptDirectory(String loginScriptDirectory) {
		this.loginScriptDirectory = loginScriptDirectory;
	}
	public String getFireBugPath() {
		return fireBugPath;
	}
	public void setFireBugPath(String fireBugPath) {
		this.fireBugPath = fireBugPath;
	}
	public String getNetExportPath() {
		return netExportPath;
	}
	public void setNetExportPath(String netExportPath) {
		this.netExportPath = netExportPath;
	}
	public String getReportsPath() {
		return reportsPath;
	}
	public void setReportsPath(String reportsPath) {
		this.reportsPath = reportsPath;
	}
	public List<PartialText> getPartialTextList() {
		if(null == partialTextList) {
			partialTextList = new ArrayList<PartialText>();
		}
		return partialTextList;
	}
	public void setPartialTextList(List<PartialText> partialTextList) {
		this.partialTextList = partialTextList;
	}
	public String getCrawlStatusDirectory() {
		return crawlStatusDirectory;
	}
	public void setCrawlStatusDirectory(String crawlStatusDirectory) {
		this.crawlStatusDirectory = crawlStatusDirectory;
	}
	
	public int getRunStaus() {
		return runStaus;
	}
	public void setRunStaus(int runStaus) {
		this.runStaus = runStaus;
	}
	
	public int getUrlLevel() {
		return urlLevel;
	}
	public void setUrlLevel(int urlLevel) {
		this.urlLevel = urlLevel;
	}
	public boolean isTransactionTesting() {
		return transactionTesting;
	}
	public void setTransactionTesting(boolean transactionTesting) {
		this.transactionTesting = transactionTesting;
	}
	public List<String> getTextSearchList() {
		return textSearchList;
	}
	public void setTextSearchList(List<String> textSearchList) {
		this.textSearchList = textSearchList;
	}
	public String getTextImageDirectory() {
		return textImageDirectory;
	}
	public void setTextImageDirectory(String textImageDirectory) {
		this.textImageDirectory = textImageDirectory;
	}
	public List<HtmlReportsConfig> getHtmlReportsConfigList() {
		return htmlReportsConfigList;
	}
	public void setHtmlReportsConfigList(List<HtmlReportsConfig> htmlReportsConfigList) {
		this.htmlReportsConfigList = htmlReportsConfigList;
	}
	public CrawlConfig getBaseLineCrawlConfig() {
		return baseLineCrawlConfig;
	}
	public void setBaseLineCrawlConfig(CrawlConfig baseLineCrawlConfig) {
		this.baseLineCrawlConfig = baseLineCrawlConfig;
	}
	
	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public boolean isScrollPage() {
		return scrollPage;
	}

	public void setScrollPage(boolean scrollPage) {
		this.scrollPage = scrollPage;
	}
	@Override
	public String toString() {
		return "ScheduleDetails [executionId=" + executionId
				+ ", applicationName=" + applicationName + ", environmentName="
				+ environmentName + ", environmentCategory="
				+ environmentCategory + ", url=" + url + ", loginId=" + loginId
				+ ", password=" + password + ", securityAnswer="
				+ securityAnswer + ", baseUrl=" + baseUrl + ", rsaEnabled="
				+ rsaEnabled + ", snapShotPath=" + snapShotPath
				+ ", testRunExecutionStatus=" + testRunExecutionStatus
				+ ", testScheduleId=" + testScheduleId + ", error=" + error
				+ ", navigationList=" + navigationList + ", testLoginId="
				+ testLoginId + ", testFilePath=" + testFilePath
				+ ", testFileName=" + testFileName
				+ ", navigationXmlFileContent=" + navigationXmlFileContent
				+ ", testExecutionStatusRefId=" + testExecutionStatusRefId
				+ ", testCaseId=" + testCaseId + ", publicSite=" + publicSite
				+ ", harReportsPath=" + harReportsPath + ", reportsPath="
				+ reportsPath + ", behindTheScene=" + behindTheScene
				+ ", dataCenter=" + dataCenter + ", browserType=" + browserType
				+ ", regressionTesting=" + regressionTesting
				+ ", analyticsTesting=" + analyticsTesting + ", stopAfter="
				+ stopAfter + ", akamaiTesting=" + akamaiTesting
				+ ", akamaiMapping=" + akamaiMapping + ", proxyAuthentication="
				+ proxyAuthentication + ", akamaiUrl=" + akamaiUrl
				+ ", directory=" + directory + ", applicationId="
				+ applicationId + ", environmentId=" + environmentId
				+ ", resultsBaseDirectory=" + resultsBaseDirectory
				+ ", noOfThreads=" + noOfThreads + ", serializePath="
				+ serializePath + ", emailOnCompletion=" + emailOnCompletion
				+ ", emailIds=" + emailIds + ", emailOnFailure="
				+ emailOnFailure + ", processChildUrls=" + processChildUrls
				+ ", robotClicking=" + robotClicking + ", brokenUrlReport="
				+ brokenUrlReport + ", maxWaitTime=" + maxWaitTime
				+ ", browserRestartCount=" + browserRestartCount
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", brandingUrlReport=" + brandingUrlReport + ", userId="
				+ userId + ", baselineScheduleId=" + baselineScheduleId
				+ ", staticUrlTesting=" + staticUrlTesting + ", staticUrlList="
				+ staticUrlList + ", brandingReportsPath="
				+ brandingReportsPath + ", executionTimeStamp="
				+ executionTimeStamp + ", textCompare=" + textCompare
				+ ", htmlCompare=" + htmlCompare + ", textFilePath="
				+ textFilePath + ", errorStackTrace=" + errorStackTrace
				+ ", mobProxyHost=" + mobProxyHost + ", mobProxyPort="
				+ mobProxyPort + ", upstreamProxyHost=" + upstreamProxyHost
				+ ", upstreamProxyPort=" + upstreamProxyPort + ", includeUrl="
				+ includeUrl + ", excludeUrl=" + excludeUrl
				+ ", includeModuleUrlPatternList="
				+ includeModuleUrlPatternList + ", testModuleUrlPatternList="
				+ testModuleUrlPatternList + ", useBMPProxy=" + useBMPProxy
				+ ", proxyServer=" + proxyServer + ", screenCompare="
				+ screenCompare + ", loginScriptDirectory="
				+ loginScriptDirectory + ", fireBugPath=" + fireBugPath
				+ ", netExportPath=" + netExportPath + ", tiaaImageNameList="
				+ tiaaImageNameList + ", textSearchList=" + textSearchList
				+ ", partialTextList=" + partialTextList
				+ ", crawlStatusDirectory=" + crawlStatusDirectory
				+ ", runStaus=" + runStaus + ", urlLevel=" + urlLevel
				+ ", transactionTesting=" + transactionTesting
				+ ", scheduleScriptList=" + scheduleScriptList
				+ ", htmlReportsConfigList=" + htmlReportsConfigList
				+ ", textImageDirectory=" + textImageDirectory
				+ ", baseLineCrawlConfig=" + baseLineCrawlConfig
				+ ", scrollPage=" + scrollPage + ", waitTime=" + waitTime + "]";
	}
	
}
