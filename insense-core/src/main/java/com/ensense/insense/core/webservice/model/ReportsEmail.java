package com.ensense.insense.core.webservice.model;

import java.util.Date;

public class ReportsEmail {
	String webserviceSuitName;
	String serviceName;
	String operationName;
	String environmentName;
	String matchedwithBasedline;
	String dataSetName;
	Date currentRunExecutionTime;
	Date baseLineRunExecutionTime;
	
	public String getWebserviceSuitName() {
		return webserviceSuitName;
	}
	public void setWebserviceSuitName(String webserviceSuitName) {
		this.webserviceSuitName = webserviceSuitName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getMatchedwithBasedline() {
		return matchedwithBasedline;
	}
	public void setMatchedwithBasedline(String matchedwithBasedline) {
		this.matchedwithBasedline = matchedwithBasedline;
	}
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	public Date getCurrentRunExecutionTime() {
		return currentRunExecutionTime;
	}
	public void setCurrentRunExecutionTime(Date currentRunExecutionTime) {
		this.currentRunExecutionTime = currentRunExecutionTime;
	}
	public Date getBaseLineRunExecutionTime() {
		return baseLineRunExecutionTime;
	}
	public void setBaseLineRunExecutionTime(Date baseLineRunExecutionTime) {
		this.baseLineRunExecutionTime = baseLineRunExecutionTime;
	}
	@Override
	public String toString() {
		return "ReportsEmail [webserviceSuitName=" + webserviceSuitName
				+ ", serviceName=" + serviceName + ", operationName="
				+ operationName + ", environmentName=" + environmentName
				+ ", matchedwithBasedline=" + matchedwithBasedline
				+ ", dataSetName=" + dataSetName + ", currentRunExecutionTime="
				+ currentRunExecutionTime + ", baseLineRunExecutionTime="
				+ baseLineRunExecutionTime + "]";
	}
}
