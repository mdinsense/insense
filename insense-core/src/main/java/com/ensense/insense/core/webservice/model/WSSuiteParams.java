package com.ensense.insense.core.webservice.model;

public class WSSuiteParams {

	private int serviceId;
	private int environmentId;
	private int operationId;
	
	private String serviceName;
	private int parameterSetId;
	private String environmentName;
	private String operationName;
	private String datasetName;
	private WsDataset wsDataset;
	
	
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}
	public int getOperationId() {
		return operationId;
	}
	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setWsDataset(WsDataset wsDataset) {
		this.wsDataset = wsDataset;
	}
	public WsDataset getWsDataset() {
		return wsDataset;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datasetName == null) ? 0 : datasetName.hashCode());
		result = prime * result
				+ ((environmentName == null) ? 0 : environmentName.hashCode());
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result
				+ ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result
				+ ((wsDataset == null) ? 0 : wsDataset.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WSSuiteParams other = (WSSuiteParams) obj;
		boolean result = true;
		if (operationName == null) {
			if (other.operationName != null)
				result =  false;
		} else if (!operationName.equals(other.operationName))
			result = false;
		if (serviceName == null) {
			if (other.serviceName != null)
				result = false;
		} else if (!serviceName.equals(other.serviceName))
			result = false;
		
		if (wsDataset == null) {
			if (other.wsDataset != null)
				result = false;
		} else if (!wsDataset.equals(other.wsDataset))
			result = false;
		
		return result;
	}
	public void setParameterSetId(int parameterSetId) {
		this.parameterSetId = parameterSetId;
	}
	public int getParameterSetId() {
		return parameterSetId;
	}
	
	

}
