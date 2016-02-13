package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WebserviceOperations")
public class WebserviceOperations implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="operationId")
	private Integer operationId;

	@Column(name="serviceId")
	private Integer serviceId;
	
	@Column(name="operationName")
	private String operationName;
	
	@Column(name="environmentId")
	private Integer environmentId;	

	@Column(name="methodType")
	private String methodType;	
	
	@Column(name="contentType")
	private String contentType;	
	
	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "WebserviceOperations [operationId=" + operationId + ", serviceId="
				+ serviceId + ", operationName=" + operationName + ", environmentId="
				+ environmentId + ", methodType=" + methodType+ "]";
	}

}
