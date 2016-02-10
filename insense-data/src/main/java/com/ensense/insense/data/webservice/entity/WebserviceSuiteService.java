package com.ensense.insense.data.webservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WebserviceSuiteService")
public class WebserviceSuiteService {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="webserviceSuiteServiceId")
	private int webserviceSuiteServiceId;

	@Column(name="webserviceSuiteId")
	private int webserviceSuiteId;
	
	@Column(name="serviceId")
	private int serviceId;
	
	@Column(name="operationId")
	private int operationId;
	
	@Column(name="inputType")
	private String inputType;
	
	
	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	

	public int getWebserviceSuiteServiceId() {
		return webserviceSuiteServiceId;
	}

	public void setWebserviceSuiteServiceId(int webserviceSuiteServiceId) {
		this.webserviceSuiteServiceId = webserviceSuiteServiceId;
	}

	public int getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(int webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
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
	
	

}
