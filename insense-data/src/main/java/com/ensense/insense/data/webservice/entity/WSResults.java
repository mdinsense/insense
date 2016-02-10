package com.ensense.insense.data.webservice.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WSResults")
public class WSResults{

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsResultsId")
	private int wsResultsId;
	
	@Column(name="wsExecutionStatusId")
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
	
	@Lob
	@Column(name="resultXml")
	private String resultXml;
	
	@Column(name="createdDateTime")
	private Date createdDateTime;
	
	@Column(name="executionStatusRefId")
	private int executionStatusRefId;

	@Lob
	@Column(name="requestXml")
	private String requestXml;

	public int getWsResultsId() {
		return wsResultsId;
	}

	public void setWsResultsId(int wsResultsId) {
		this.wsResultsId = wsResultsId;
	}

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

	public String getResultXml() {
		return resultXml;
	}

	public void setResultXml(String resultXml) {
		this.resultXml = resultXml;
	}

	
	public int getExecutionStatusRefId() {
		return executionStatusRefId;
	}

	public void setExecutionStatusRefId(int executionStatusRefId) {
		this.executionStatusRefId = executionStatusRefId;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	@Override
	public String toString() {
		return "WSResults [wsResultsId=" + wsResultsId
				+ ", wsExecutionStatusId=" + wsExecutionStatusId
				+ ", wsScheduleId=" + wsScheduleId + ", webserviceSuiteId="
				+ webserviceSuiteId + ", serviceId=" + serviceId
				+ ", operationId=" + operationId + ", parameterSetId="
				+ parameterSetId + ", resultXml=" + "resultXml"
				+ ", createdDateTime=" + createdDateTime
				+ ", executionStatusRefId=" + executionStatusRefId
				+ ", requestXml=" + "requestXml" + "]";
	}
}