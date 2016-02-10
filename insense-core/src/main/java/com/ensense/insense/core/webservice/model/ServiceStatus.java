package com.ensense.insense.core.webservice.model;

public class ServiceStatus {
	private String serviceName;
	private String status;
	private String htmlSource;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHtmlSource() {
		return htmlSource;
	}
	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}
	
	@Override
	public String toString() {
		return "Service [serviceName=" + serviceName + ", status=" + status
				+ ", htmlSource=" + htmlSource + "]";
	}
	
}
