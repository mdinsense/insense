package com.ensense.insense.core.webservice.model;

import java.util.List;

public class ServiceDetails {

	private List<ServiceStatus> service;

	public List<ServiceStatus> getService() {
		return service;
	}

	public void setService(List<ServiceStatus> service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return "ServiceDetails [service=" + service + "]";
	}
	
}
