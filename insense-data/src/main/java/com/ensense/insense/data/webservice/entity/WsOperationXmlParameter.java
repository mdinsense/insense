package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WsOperationXmlParameter", uniqueConstraints = {@UniqueConstraint(columnNames={"environmentId","serviceId","operationId","datasetName"})})
public class WsOperationXmlParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "parameterId")
	private Integer parameterId;

	@Column(name = "parameterName")
	private String parameterName;
	
	@Column(name = "datasetName")
	private String datasetName;

	@Lob
	@Column(name = "parameterValue")
	private String parameterValue;

	@Column(name = "serviceId")
	private Integer serviceId;

	@Column(name = "operationId")
	private Integer operationId;

	@Column(name = "environmentId")
	private Integer environmentId;

	@Column(name = "paramType")
	private String paramType;

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
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
	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	
	@Override
	public String toString() {
		return "WsOperationXmlParameter [parameterId=" + parameterId
				+ ", parameterName=" + parameterName + ", parameterValue="
				+ parameterValue + ", serviceId=" + serviceId
				+ ", operationId=" + operationId + ", environmentId="
				+ environmentId + ", paramType=" + paramType + "]";
	}

}
