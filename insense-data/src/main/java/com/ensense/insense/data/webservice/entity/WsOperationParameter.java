package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;
import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WsOperationParameter")
public class WsOperationParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "parameterId")
	private Integer parameterId;

	@Column(name = "parameterName")
	private String parameterName;

	@Column(name = "serviceId")
	private Integer serviceId;

	@Column(name = "operationId")
	private Integer operationId;

	@Column(name = "paramType")
	private String paramType;

	@Lob
	@Column(name = "sampleXML")
	private Clob sampleXML;

	public Clob getSampleXML() {
		return sampleXML;
	}

	public void setSampleXML(Clob sampleXML) {
		this.sampleXML = sampleXML;
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

	@Override
	public String toString() {
		return "WebserviceOperationParameter [parameterId=" + parameterId
				+ ", parameterName=" + parameterName + ", serviceId="
				+ serviceId + ", operationId=" + operationId + ", paramType="
				+ paramType + ", sampleXML=" + sampleXML + "]";
	}

}
