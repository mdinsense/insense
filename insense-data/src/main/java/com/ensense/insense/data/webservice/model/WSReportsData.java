package com.ensense.insense.data.webservice.model;

public class WSReportsData {
	
	private String service;
	private String operation;
	private String executionDate;	
	private String baselineDate;
	private String requestXML;
	private String responseXML;
	private String baselineRequestXML;
	private String baselineResponseXML;
	private String differences;
	private String matched;
	private int resultsId;
	private int reportsId;
	private int wsBaselineScheduleId;
	private int webserviceParameterSetId;
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}
	public String getBaselineDate() {
		return baselineDate;
	}
	public void setBaselineDate(String baselineDate) {
		this.baselineDate = baselineDate;
	}
	public String getRequestXML() {
		return requestXML;
	}
	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}
	public String getResponseXML() {
		return responseXML;
	}
	public void setResponseXML(String responseXML) {
		this.responseXML = responseXML;
	}
	public String getBaselineRequestXML() {
		return baselineRequestXML;
	}
	public void setBaselineRequestXML(String baselineRequestXML) {
		this.baselineRequestXML = baselineRequestXML;
	}
	public String getBaselineResponseXML() {
		return baselineResponseXML;
	}
	public void setBaselineResponseXML(String baselineResponseXML) {
		this.baselineResponseXML = baselineResponseXML;
	}

	public String getDifferences() {
		return differences;
	}
	public void setDifferences(String differences) {
		this.differences = differences;
	}
	public String getMatched() {
		return matched;
	}
	public void setMatched(String matched) {
		this.matched = matched;
	}
	
	public int getResultsId() {
		return resultsId;
	}
	public void setResultsId(int resultsId) {
		this.resultsId = resultsId;
	}
	public int getReportsId() {
		return reportsId;
	}
	public void setReportsId(int reportsId) {
		this.reportsId = reportsId;
	}
	
	public int getWsBaselineScheduleId() {
		return wsBaselineScheduleId;
	}
	public void setWsBaselineScheduleId(int wsBaselineScheduleId) {
		this.wsBaselineScheduleId = wsBaselineScheduleId;
	}
	public int getWebserviceParameterSetId() {
		return webserviceParameterSetId;
	}
	public void setWebserviceParameterSetId(int webserviceParameterSetId) {
		this.webserviceParameterSetId = webserviceParameterSetId;
	}
	@Override
	public String toString() {
		return "WSReportsData [service=" + service + ", operation=" + operation
				+ ", executionDate=" + executionDate + ", baselineDate="
				+ baselineDate + ", requestXML=" + requestXML
				+ ", responseXML=" + responseXML + ", baselineRequestXML="
				+ baselineRequestXML + ", baselineResponseXML="
				+ baselineResponseXML + ", differences=" + differences
				+ ", matched=" + matched + ", resultsId=" + resultsId
				+ ", reportsId=" + reportsId + ", wsBaselineScheduleId="
				+ wsBaselineScheduleId + ", webserviceParameterSetId="
				+ webserviceParameterSetId + "]";
	}
}
