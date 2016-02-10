package com.ensense.insense.core.webservice.model;

import java.sql.Date;

public class PingTestStatus {

	private String serviceName;
	private Date runDate;
	private String environment;
	private boolean status;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "PingTestStatus [serviceName=" + serviceName + ", runDate="
				+ runDate + ", environment=" + environment + ", status="
				+ status + "]";
	}

}
