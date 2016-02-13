package com.ensense.insense.core.webservice.model;

public class ServiceUrl {

	String esbName;
	String environment;
	String serviceName;
	String esbPingUrl;
	String htmlSource;
	boolean serviceFound;
	boolean status;
	
	public String getEsbName() {
		return esbName;
	}
	public void setEsbName(String esbName) {
		this.esbName = esbName;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getEsbPingUrl() {
		return esbPingUrl;
	}
	public void setEsbPingUrl(String esbPingUrl) {
		this.esbPingUrl = esbPingUrl;
	}
	public boolean isServiceFound() {
		return serviceFound;
	}
	public void setServiceFound(boolean serviceFound) {
		this.serviceFound = serviceFound;
	}
	public String getHtmlSource() {
		return htmlSource;
	}
	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ServiceUrl [esbName=" + esbName + ", environment="
				+ environment + ", serviceName=" + serviceName
				+ ", esbPingUrl=" + esbPingUrl + ", htmlSource=" + htmlSource
				+ ", serviceFound=" + serviceFound + ", status=" + status + "]";
	}
}
