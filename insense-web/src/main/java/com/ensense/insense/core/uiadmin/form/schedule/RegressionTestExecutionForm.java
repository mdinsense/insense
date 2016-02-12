package com.ensense.insense.core.uiadmin.form.schedule;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.cts.mint.uitesting.model.mintv4.TestSuitDetails;


public class RegressionTestExecutionForm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String suitId;
	private String selectedSuitId;
	private String application;
	private String environment;
	private String[] skipUrls;
	private String[] crawlUrls;
	private String testlogin;
	private String[] testcaseName;
	private String analyticsTesting;
	private String regressionTesting;
	private String browserType;
	private Integer stopAfter;
	private String akamaiTesting;
	private Integer moduleId;
	private String reportDates;
	private Integer baselineScheduleExecutionId;
	private Integer scheduleExecutionId;
	private String scheduleAction;
	private String schDateTime;
	private String processChildUrls;
	private String robotClicking;
	private String brokenUrlReport;
	private String maxtime;
	private String threads;
	private String browserRestart;
	private String emailOnFailure;
	private String emailOnCompletion;
	private String emailAddr;
	private String brandingUrlReport;
	private String recurrence;
	private String scheduledDate;
	private String recurrenceidweekly1;
	private String recurrenceidweekly2;
	private String recurrenceidweekly3;
	private String recurrenceidweekly4;
	private String recurrenceidweekly5;
	private String recurrenceidweekly6;
	private String recurrenceidweekly7;
	private String SaveDemandData;
	private String suitname;
	private String checkedSuitName;
	private String staticUrlTesting;
	private String moduleAll;
	private String textCompare;
	private String htmlCompare;
	private String screenCompare;
	private String CreatedbyGRP;
	private String CreatedbyAL;
	private String executedDatesId;
	private String dataCenter;
	private String uiSuitsCategory;
	private int solutionType;
	private String scheduleType;
	private Integer setupTabNumber;
	private String[] groupsId;
	private String[] assignGroupId;
	private String groupId;
	private boolean behindTheScene;
	private String editOrViewMode;
	private String executeBy;
	private Integer refreshtext;
	private String allSched;
	private String completedSched;
	private String currentSched;
	private String futureSched;
	private String suitType;
	private String notes;
	private String webserviceSuiteId;
	private Integer urlLevel;
	private String emailRecepients;
	private int wsResultsId;
	private int wsScheduleId;
	private Integer[] testCases; 
	private Integer currentPageNo;
	private Integer reportTabNumber;
	private String analyticTagName;
	private String tagName;
	private String detailOrError;
	private List<TestSuitDetails> testSuitDetails;
	
	public List<TestSuitDetails> getTestSuitDetails() {
		return testSuitDetails;
	}

	public void setTestSuitDetails(List<TestSuitDetails> testSuitDetails) {
		this.testSuitDetails = testSuitDetails;
	}

	public String getDetailOrError() {
		return detailOrError;
	}

	public void setDetailOrError(String detailOrError) {
		this.detailOrError = detailOrError;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	List<String> tagNameDataList;
	

	public List<String> getTagNameDataList() {
		return tagNameDataList;
	}

	public void setTagNameDataList(List<String> tagNameDataList) {
		this.tagNameDataList = tagNameDataList;
	}

	public String getAnalyticTagName() {
		return analyticTagName;
	}

	public void setAnalyticTagName(String analyticTagName) {
		this.analyticTagName = analyticTagName;
	}

	public Integer getReportTabNumber() {
		return reportTabNumber;
	}

	public void setReportTabNumber(Integer reportTabNumber) {
		this.reportTabNumber = reportTabNumber;
	}

	public Integer getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public String getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(String webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
	}

	
	public RegressionTestExecutionForm(){
		refreshtext = 10;	
		currentPageNo = 1;
		reportTabNumber =1;
	}

	public String getSuitType() {
		return suitType;
	}

	
	public int getSolutionType() {
		return solutionType;
	}

	public void setSolutionType(int solutionType) {
		this.solutionType = solutionType;
	}

	public void setSuitType(String suitType) {
		this.suitType = suitType;
	}

	public String getSuitId() {
		return suitId;
	}
	public void setSuitId(String suitId) {
		this.suitId = suitId;
	}
	public String getSelectedSuitId() {
		return selectedSuitId;
	}
	public void setSelectedSuitId(String selectedSuitId) {
		this.selectedSuitId = selectedSuitId;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String[] getSkipUrls() {
		return skipUrls;
	}
	public void setSkipUrls(String[] skipUrls) {
		this.skipUrls = skipUrls;
	}
	public String[] getCrawlUrls() {
		return crawlUrls;
	}
	public void setCrawlUrls(String[] crawlUrls) {
		this.crawlUrls = crawlUrls;
	}
	public String getTestlogin() {
		return testlogin;
	}
	public void setTestlogin(String testlogin) {
		this.testlogin = testlogin;
	}
	public String[] getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String[] testcaseName) {
		this.testcaseName = testcaseName;
	}
	public String getAnalyticsTesting() {
		return analyticsTesting;
	}
	public void setAnalyticsTesting(String analyticsTesting) {
		this.analyticsTesting = analyticsTesting;
	}
	public String getRegressionTesting() {
		return regressionTesting;
	}
	public void setRegressionTesting(String regressionTesting) {
		this.regressionTesting = regressionTesting;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public Integer getStopAfter() {
		return stopAfter;
	}
	public void setStopAfter(Integer stopAfter) {
		this.stopAfter = stopAfter;
	}
	public String getAkamaiTesting() {
		return akamaiTesting;
	}
	public void setAkamaiTesting(String akamaiTesting) {
		this.akamaiTesting = akamaiTesting;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getReportDates() {
		return reportDates;
	}
	public void setReportDates(String reportDates) {
		this.reportDates = reportDates;
	}
	public Integer getBaselineScheduleExecutionId() {
		return baselineScheduleExecutionId;
	}
	public void setBaselineScheduleExecutionId(Integer baselineScheduleExecutionId) {
		this.baselineScheduleExecutionId = baselineScheduleExecutionId;
	}
	public Integer getScheduleExecutionId() {
		return scheduleExecutionId;
	}
	public void setScheduleExecutionId(Integer scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}
	public String getSchDateTime() {
		return schDateTime;
	}
	public void setSchDateTime(String schDateTime) {
		this.schDateTime = schDateTime;
	}
	public String getProcessChildUrls() {
		return processChildUrls;
	}
	public void setProcessChildUrls(String processChildUrls) {
		this.processChildUrls = processChildUrls;
	}
	public String getRobotClicking() {
		return robotClicking;
	}
	public void setRobotClicking(String robotClicking) {
		this.robotClicking = robotClicking;
	}
	public String getBrokenUrlReport() {
		return brokenUrlReport;
	}
	public void setBrokenUrlReport(String brokenUrlReport) {
		this.brokenUrlReport = brokenUrlReport;
	}
	public String getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(String maxtime) {
		this.maxtime = maxtime;
	}
	public String getThreads() {
		return threads;
	}
	public void setThreads(String threads) {
		this.threads = threads;
	}
	public String getBrowserRestart() {
		return browserRestart;
	}
	public void setBrowserRestart(String browserRestart) {
		this.browserRestart = browserRestart;
	}
	public String getEmailOnFailure() {
		return emailOnFailure;
	}
	public void setEmailOnFailure(String emailOnFailure) {
		this.emailOnFailure = emailOnFailure;
	}
	public String getEmailOnCompletion() {
		return emailOnCompletion;
	}
	public void setEmailOnCompletion(String emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getBrandingUrlReport() {
		return brandingUrlReport;
	}
	public void setBrandingUrlReport(String brandingUrlReport) {
		this.brandingUrlReport = brandingUrlReport;
	}
	public String getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}
	public String getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public String getRecurrenceidweekly1() {
		return recurrenceidweekly1;
	}
	public void setRecurrenceidweekly1(String recurrenceidweekly1) {
		this.recurrenceidweekly1 = recurrenceidweekly1;
	}
	public String getRecurrenceidweekly2() {
		return recurrenceidweekly2;
	}
	public void setRecurrenceidweekly2(String recurrenceidweekly2) {
		this.recurrenceidweekly2 = recurrenceidweekly2;
	}
	public String getRecurrenceidweekly3() {
		return recurrenceidweekly3;
	}
	public void setRecurrenceidweekly3(String recurrenceidweekly3) {
		this.recurrenceidweekly3 = recurrenceidweekly3;
	}
	public String getRecurrenceidweekly4() {
		return recurrenceidweekly4;
	}
	public void setRecurrenceidweekly4(String recurrenceidweekly4) {
		this.recurrenceidweekly4 = recurrenceidweekly4;
	}
	public String getRecurrenceidweekly5() {
		return recurrenceidweekly5;
	}
	public void setRecurrenceidweekly5(String recurrenceidweekly5) {
		this.recurrenceidweekly5 = recurrenceidweekly5;
	}
	public String getRecurrenceidweekly6() {
		return recurrenceidweekly6;
	}
	public void setRecurrenceidweekly6(String recurrenceidweekly6) {
		this.recurrenceidweekly6 = recurrenceidweekly6;
	}
	public String getRecurrenceidweekly7() {
		return recurrenceidweekly7;
	}
	public void setRecurrenceidweekly7(String recurrenceidweekly7) {
		this.recurrenceidweekly7 = recurrenceidweekly7;
	}
	public String getSaveDemandData() {
		return SaveDemandData;
	}
	public void setSaveDemandData(String saveDemandData) {
		SaveDemandData = saveDemandData;
	}
	public String getSuitname() {
		return suitname;
	}
	public void setSuitname(String suitname) {
		this.suitname = suitname;
	}
	public String getCheckedSuitName() {
		return checkedSuitName;
	}
	public void setCheckedSuitName(String checkedSuitName) {
		this.checkedSuitName = checkedSuitName;
	}
	public String getStaticUrlTesting() {
		return staticUrlTesting;
	}
	public void setStaticUrlTesting(String staticUrlTesting) {
		this.staticUrlTesting = staticUrlTesting;
	}
	public String getModuleAll() {
		return moduleAll;
	}
	public void setModuleAll(String moduleAll) {
		this.moduleAll = moduleAll;
	}
	public String getTextCompare() {
		return textCompare;
	}
	public void setTextCompare(String textCompare) {
		this.textCompare = textCompare;
	}
	public String getHtmlCompare() {
		return htmlCompare;
	}
	public void setHtmlCompare(String htmlCompare) {
		this.htmlCompare = htmlCompare;
	}
	public String getScreenCompare() {
		return screenCompare;
	}
	public void setScreenCompare(String screenCompare) {
		this.screenCompare = screenCompare;
	}
	public String getCreatedbyGRP() {
		return CreatedbyGRP;
	}
	public void setCreatedbyGRP(String createdbyGRP) {
		CreatedbyGRP = createdbyGRP;
	}
	public String getCreatedbyAL() {
		return CreatedbyAL;
	}
	public void setCreatedbyAL(String createdbyAL) {
		CreatedbyAL = createdbyAL;
	}
	public String getExecutedDatesId() {
		return executedDatesId;
	}
	public void setExecutedDatesId(String executedDatesId) {
		this.executedDatesId = executedDatesId;
	}
	public String getDataCenter() {
		return dataCenter;
	}
	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}
	public String getUiSuitsCategory() {
		return uiSuitsCategory;
	}
	public void setUiSuitsCategory(String uiSuitsCategory) {
		this.uiSuitsCategory = uiSuitsCategory;
	}
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	public Integer getSetupTabNumber() {
		return setupTabNumber;
	}
	public void setSetupTabNumber(Integer setupTabNumber) {
		this.setupTabNumber = setupTabNumber;
	}
	public String[] getGroupsId() {
		return groupsId;
	}
	public void setGroupsId(String[] groupsId) {
		this.groupsId = groupsId;
	}
	public String[] getAssignGroupId() {
		return assignGroupId;
	}
	public void setAssignGroupId(String[] assignGroupId) {
		this.assignGroupId = assignGroupId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public boolean isBehindTheScene() {
		return behindTheScene;
	}
	public void setBehindTheScene(boolean behindTheScene) {
		this.behindTheScene = behindTheScene;
	}
	public String getEditOrViewMode() {
		return editOrViewMode;
	}
	public void setEditOrViewMode(String editOrViewMode) {
		this.editOrViewMode = editOrViewMode;
	}
	public String getExecuteBy() {
		return executeBy;
	}
	public void setExecuteBy(String executeBy) {
		this.executeBy = executeBy;
	}
	public Integer getRefreshtext() {
		return refreshtext;
	}
	public void setRefreshtext(Integer refreshtext) {
		this.refreshtext = refreshtext;
	}
	public String getAllSched() {
		return allSched;
	}
	public void setAllSched(String allSched) {
		this.allSched = allSched;
	}
	public String getCompletedSched() {
		return completedSched;
	}
	public void setCompletedSched(String completedSched) {
		this.completedSched = completedSched;
	}
	public String getCurrentSched() {
		return currentSched;
	}
	public void setCurrentSched(String currentSched) {
		this.currentSched = currentSched;
	}
	public String getFutureSched() {
		return futureSched;
	}
	public void setFutureSched(String futureSched) {
		this.futureSched = futureSched;
	}
	public String getScheduleAction() {
		return scheduleAction;
	}

	public void setScheduleAction(String scheduleAction) {
		this.scheduleAction = scheduleAction;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getUrlLevel() {
		return urlLevel;
	}

	public void setUrlLevel(Integer urlLevel) {
		this.urlLevel = urlLevel;
	}

	public void setEmailRecepients(String emailRecepients) {
		this.emailRecepients = emailRecepients;
	}

	public String getEmailRecepients() {
		return emailRecepients;
	}

	public void setWsResultsId(int wsResultsId) {
		this.wsResultsId = wsResultsId;
	}

	public int getWsResultsId() {
		return wsResultsId;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	public Integer[] getTestCases() {
		return testCases;
	}

	public void setTestCases(Integer[] testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		return "RegressionTestExecutionForm [suitId=" + suitId
				+ ", selectedSuitId=" + selectedSuitId + ", application="
				+ application + ", environment=" + environment + ", skipUrls="
				+ Arrays.toString(skipUrls) + ", crawlUrls="
				+ Arrays.toString(crawlUrls) + ", testlogin=" + testlogin
				+ ", testcaseName=" + Arrays.toString(testcaseName)
				+ ", analyticsTesting=" + analyticsTesting
				+ ", regressionTesting=" + regressionTesting + ", browserType="
				+ browserType + ", stopAfter=" + stopAfter + ", akamaiTesting="
				+ akamaiTesting + ", moduleId=" + moduleId + ", reportDates="
				+ reportDates + ", baselineScheduleExecutionId="
				+ baselineScheduleExecutionId + ", scheduleExecutionId="
				+ scheduleExecutionId + ", scheduleAction=" + scheduleAction
				+ ", schDateTime=" + schDateTime + ", processChildUrls="
				+ processChildUrls + ", robotClicking=" + robotClicking
				+ ", brokenUrlReport=" + brokenUrlReport + ", maxtime="
				+ maxtime + ", threads=" + threads + ", browserRestart="
				+ browserRestart + ", emailOnFailure=" + emailOnFailure
				+ ", emailOnCompletion=" + emailOnCompletion + ", emailAddr="
				+ emailAddr + ", brandingUrlReport=" + brandingUrlReport
				+ ", recurrence=" + recurrence + ", scheduledDate="
				+ scheduledDate + ", recurrenceidweekly1="
				+ recurrenceidweekly1 + ", recurrenceidweekly2="
				+ recurrenceidweekly2 + ", recurrenceidweekly3="
				+ recurrenceidweekly3 + ", recurrenceidweekly4="
				+ recurrenceidweekly4 + ", recurrenceidweekly5="
				+ recurrenceidweekly5 + ", recurrenceidweekly6="
				+ recurrenceidweekly6 + ", recurrenceidweekly7="
				+ recurrenceidweekly7 + ", SaveDemandData=" + SaveDemandData
				+ ", suitname=" + suitname + ", checkedSuitName="
				+ checkedSuitName + ", staticUrlTesting=" + staticUrlTesting
				+ ", moduleAll=" + moduleAll + ", textCompare=" + textCompare
				+ ", htmlCompare=" + htmlCompare + ", screenCompare="
				+ screenCompare + ", CreatedbyGRP=" + CreatedbyGRP
				+ ", CreatedbyAL=" + CreatedbyAL + ", executedDatesId="
				+ executedDatesId + ", dataCenter=" + dataCenter
				+ ", uiSuitsCategory=" + uiSuitsCategory + ", solutionType="
				+ solutionType + ", scheduleType=" + scheduleType
				+ ", setupTabNumber=" + setupTabNumber + ", groupsId="
				+ Arrays.toString(groupsId) + ", assignGroupId="
				+ Arrays.toString(assignGroupId) + ", groupId=" + groupId
				+ ", behindTheScene=" + behindTheScene + ", editOrViewMode="
				+ editOrViewMode + ", executeBy=" + executeBy
				+ ", refreshtext=" + refreshtext + ", allSched=" + allSched
				+ ", completedSched=" + completedSched + ", currentSched="
				+ currentSched + ", futureSched=" + futureSched + ", suitType="
				+ suitType + ", notes=" + notes + ", webserviceSuiteId="
				+ webserviceSuiteId + ", urlLevel=" + urlLevel
				+ ", emailRecepients=" + emailRecepients + ", wsResultsId="
				+ wsResultsId + ", wsScheduleId=" + wsScheduleId
				+ ", testCases=" + Arrays.toString(testCases)
				+ ", currentPageNo=" + currentPageNo + ", reportTabNumber="
				+ reportTabNumber + ", analyticTagName=" + analyticTagName
				+ ", tagName=" + tagName + ", detailOrError=" + detailOrError
				+ ", testSuitDetails=" + testSuitDetails + ", tagNameDataList="
				+ tagNameDataList + "]";
	}

}
