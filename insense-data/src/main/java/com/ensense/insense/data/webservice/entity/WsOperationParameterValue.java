package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;
import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "WsOperationParameterValue")
public class WsOperationParameterValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WsParameterAndSetId paramSetId;
	
	@Column(name = "environmentId")
	private Integer environmentId;
	
	@Lob
	@Column(name = "parameterValue")
	private Clob parameterValue;
	
	@Column(name = "datasetName")
	private String datasetName;

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public WsParameterAndSetId getParamSetId() {
		return paramSetId;
	}

	public void setParamSetId(WsParameterAndSetId paramSetId) {
		this.paramSetId = paramSetId;
	}

	public Clob getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Clob parameterValue) {
		this.parameterValue = parameterValue;
	}

	

	@Override
	public String toString() {
		return "WsOperationParameterValue [paramSetId=" + paramSetId
				+ ", parameterValue=" + parameterValue + "]";
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

}
