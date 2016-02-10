package com.ensense.insense.data.webservice.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the ws_test_execution_status_table database table.
 * added by 303731
 */
@Entity
@Table(name="WSExecutionStatus")
public class WSExecutionStatus{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="wSExecutionStatusId")
	private int wsExecutionStatusId;

	@Column(name="wsScheduleId")
	private int wsScheduleId;	
	
	@Column(name="webserviceSuiteId")
	private Integer webserviceSuiteId;
	
	@Column(name="serviceId")
	private int serviceId;
	
	@Column(name="operationId")
	private int operationId;
	
	@Column(name="parameterSetId")
	private int parameterSetId;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="createdTime")
	private Time createdTime;

	@Column(name="executionStatusRefId")
	private int executionStatusRefId;
	
	@Column(name="executionEndTime")
	private Date executionEndTime;
	
	@Column(name="inputType")
	private String inputType;

	public int getWsExecutionStatusId() {
		return wsExecutionStatusId;
	}

	public void setWsExecutionStatusId(int wsExecutionStatusId) {
		this.wsExecutionStatusId = wsExecutionStatusId;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public Integer getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(Integer webserviceSuiteId) {
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

	public int getParameterSetId() {
		return parameterSetId;
	}

	public void setParameterSetId(int parameterSetId) {
		this.parameterSetId = parameterSetId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Time getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Time createdTime) {
		this.createdTime = createdTime;
	}

	public int getExecutionStatusRefId() {
		return executionStatusRefId;
	}

	public void setExecutionStatusRefId(int executionStatusRefId) {
		this.executionStatusRefId = executionStatusRefId;
	}

	public Date getExecutionEndTime() {
		return executionEndTime;
	}

	public void setExecutionEndTime(Date executionEndTime) {
		this.executionEndTime = executionEndTime;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	@Override
	public String toString() {
		return "WSExecutionStatus [wsExecutionStatusId=" + wsExecutionStatusId
				+ ", wsScheduleId=" + wsScheduleId + ", webserviceSuiteId="
				+ webserviceSuiteId + ", serviceId=" + serviceId
				+ ", operationId=" + operationId + ", parameterSetId="
				+ parameterSetId + ", createdDate=" + createdDate
				+ ", createdTime=" + createdTime + ", executionStatusRefId="
				+ executionStatusRefId + ", executionEndTime="
				+ executionEndTime + ", inputType=" + inputType + "]";
	}
}