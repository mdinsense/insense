package com.ensense.insense.data.webservice.model;

public class WSSuiteDetails {
	
	private int webserviceSuiteId;
	private int paramSetId;
	private String serviceName;
	private String operationName;
	private String parameterName;
	private String parameterValue;
	private String inputType;
	private String datasetName;
	
	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	public int getWebserviceSuiteId() {
		return webserviceSuiteId;
	}
	public void setWebserviceSuiteId(int webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
	}
	public int getParamSetId() {
		return paramSetId;
	}
	public void setParamSetId(int paramSetId) {
		this.paramSetId = paramSetId;
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
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	@Override
	public String toString() {
		return "WSSuiteDetails [webserviceSuiteId=" + webserviceSuiteId
				+ ", paramSetId=" + paramSetId + ", serviceName=" + serviceName
				+ ", operationName=" + operationName + ", parameterName="
				+ parameterName + ", parameterValue=" + parameterValue
				+ ", inputType=" + inputType + ", datasetName=" + datasetName
				+ "]";
	}
}
