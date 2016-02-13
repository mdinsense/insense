package com.ensense.insense.data.webservice.model;

public class WsSuite {
	
	public WsSuite() {
		
	}
	public WsSuite(int serviceId, int operationId, int environmentId,
			int paramset, String xmlInput, String rawInput) {
		super();
		this.serviceId = serviceId;
		this.operationId = operationId;
		this.environmentId = environmentId;
		this.paramset = paramset;
		this.xmlInput = xmlInput;
		this.rawInput = rawInput;
	}
	private int serviceId;
	private int operationId;
	private int environmentId;
	private int paramset;
	private String xmlInput;
	private String rawInput;
	
	public String getXmlInput() {
		return xmlInput;
	}
	public void setXmlInput(String xmlInput) {
		this.xmlInput = xmlInput;
	}
	public String getRawInput() {
		return rawInput;
	}
	public void setRawInput(String rawInput) {
		this.rawInput = rawInput;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getOperationId() {
		return operationId;
	}
	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}
	public int getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}
	public int getParamset() {
		return paramset;
	}
	public void setParamset(int paramset) {
		this.paramset = paramset;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + environmentId;
		result = prime * result + operationId;
		result = prime * result + paramset;
		result = prime * result + serviceId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WsSuite other = (WsSuite) obj;
		if (environmentId != other.environmentId)
			return false;
		if (operationId != other.operationId)
			return false;
		if (paramset != other.paramset)
			return false;
		if (serviceId != other.serviceId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WsSuite [serviceId=" + serviceId + ", operationId="
				+ operationId + ", environmentId=" + environmentId
				+ ", paramset=" + paramset + "]";
	}

}
