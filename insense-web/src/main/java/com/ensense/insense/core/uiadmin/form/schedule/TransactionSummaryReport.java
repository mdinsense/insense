package com.ensense.insense.core.uiadmin.form.schedule;

import java.util.List;

import com.cts.mint.common.model.Link;

public class TransactionSummaryReport {
	private String applicationName;
	private String environmentName;
	private String moduleName;
	private String testcaseName;
	private String executionStartTime;
	private String executionStatus;
	private String errorLog;
	private int scheduleExecutionId; //TODO: change it to scheduleExecutionid
	private int scheduleScriptId;
	private List<Link> screenList;
	private String testcasePath;
	
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
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	public String getExecutionStartTime() {
		return executionStartTime;
	}
	public void setExecutionStartTime(String executionStartTime) {
		this.executionStartTime = executionStartTime;
	}
	public String getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	public int getScheduleExecutionId() {
		return scheduleExecutionId;
	}
	public void setScheduleExecutionId(int scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}
	public int getScheduleScriptId() {
		return scheduleScriptId;
	}
	public void setScheduleScriptId(int scheduleScriptId) {
		this.scheduleScriptId = scheduleScriptId;
	}
	public List<Link> getScreenList() {
		return screenList;
	}
	public void setScreenList(List<Link> screenList) {
		this.screenList = screenList;
	}
	public String getTestcasePath() {
		return testcasePath;
	}
	public void setTestcasePath(String testcasePath) {
		this.testcasePath = testcasePath;
	}
	@Override
	public String toString() {
		return "TransactionSummaryReport [applicationName=" + applicationName
				+ ", environmentName=" + environmentName + ", moduleName="
				+ moduleName + ", testcaseName=" + testcaseName
				+ ", executionStartTime=" + executionStartTime
				+ ", executionStatus=" + executionStatus + ", errorLog="
				+ errorLog + ", scheduleExecutionId=" + scheduleExecutionId
				+ ", scheduleScriptId=" + scheduleScriptId + ", screenList="
				+ screenList + ", testcasePath=" + testcasePath + "]";
	}
}
