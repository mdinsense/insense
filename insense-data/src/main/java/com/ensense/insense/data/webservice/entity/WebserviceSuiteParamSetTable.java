package com.ensense.insense.data.webservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WebserviceSuiteParamSetTable")
public class WebserviceSuiteParamSetTable {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="webserviceParameterSetId ")
	private int webserviceParameterSetId ;

	@Column(name="webserviceSuiteServiceId ")
	private int webserviceSuiteServiceId ;
	
	@Column(name="parameterSetId ")
	private int parameterSetId ;

	public int getWebserviceParameterSetId() {
		return webserviceParameterSetId;
	}

	public void setWebserviceParameterSetId(int webserviceParameterSetId) {
		this.webserviceParameterSetId = webserviceParameterSetId;
	}

	public int getWebserviceSuiteServiceId() {
		return webserviceSuiteServiceId;
	}

	public void setWebserviceSuiteServiceId(int webserviceSuiteServiceId) {
		this.webserviceSuiteServiceId = webserviceSuiteServiceId;
	}

	public int getParameterSetId() {
		return parameterSetId;
	}

	public void setParameterSetId(int parameterSetId) {
		this.parameterSetId = parameterSetId;
	}

	
	
}
