package com.ensense.insense.data.webservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WSReports")
public class WSReports {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "wsReportsId")
	private int wsReportsId;

	@Column(name = "generatedDateTime")
	private Date generatedDateTime;

	@Lob
	@Column(name = "comparisonResultsContent")
	private String comparisonResultsContent;

	@Column(name = "wsScheduleId")
	private int wsScheduleId;

	@Column(name = "wsBaselineScheduleId")
	private int wsBaselineScheduleId;

	@Column(name = "operationId")
	private Integer operationId;

	@Column(name = "parameterSetId")
	private Integer parameterSetId;

	@Column(name = "webserviceSuiteId")
	private int webserviceSuiteId;

	@Column(name = "executionStatusRefId")
	private int executionStatusRefId;

	@Column(name = "wsBaselineId")
	private int wsBaselineId;

	@Column(name = "matchedWithBaseline")
	private boolean matchedWithBaseline;
	
	public int getWsReportsId() {
		return wsReportsId;
	}

	public void setWsReportsId(int wsReportsId) {
		this.wsReportsId = wsReportsId;
	}

	public Date getGeneratedDateTime() {
		return generatedDateTime;
	}

	public void setGeneratedDateTime(Date generatedDateTime) {
		this.generatedDateTime = generatedDateTime;
	}

	public String getComparisonResultsContent() {
		return comparisonResultsContent;
	}

	public void setComparisonResultsContent(String comparisonResultsContent) {
		this.comparisonResultsContent = comparisonResultsContent;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public int getWsBaselineScheduleId() {
		return wsBaselineScheduleId;
	}

	public void setWsBaselineScheduleId(int wsBaselineScheduleId) {
		this.wsBaselineScheduleId = wsBaselineScheduleId;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public Integer getParameterSetId() {
		return parameterSetId;
	}

	public void setParameterSetId(Integer parameterSetId) {
		this.parameterSetId = parameterSetId;
	}

	public int getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(int webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
	}

	public int getExecutionStatusRefId() {
		return executionStatusRefId;
	}

	public void setExecutionStatusRefId(int executionStatusRefId) {
		this.executionStatusRefId = executionStatusRefId;
	}

	public int getWsBaselineId() {
		return wsBaselineId;
	}

	public void setWsBaselineId(int wsBaselineId) {
		this.wsBaselineId = wsBaselineId;
	}

	public boolean isMatchedWithBaseline() {
		return matchedWithBaseline;
	}

	public void setMatchedWithBaseline(boolean matchedWithBaseline) {
		this.matchedWithBaseline = matchedWithBaseline;
	}

	@Override
	public String toString() {
		return "WSReports [wsReportsId=" + wsReportsId + ", generatedDateTime="
				+ generatedDateTime + ", comparisonResultsContent="
				+ comparisonResultsContent + ", wsScheduleId=" + wsScheduleId
				+ ", wsBaselineScheduleId=" + wsBaselineScheduleId
				+ ", operationId=" + operationId + ", parameterSetId="
				+ parameterSetId + ", webserviceSuiteId=" + webserviceSuiteId
				+ ", executionStatusRefId=" + executionStatusRefId
				+ ", wsBaselineId=" + wsBaselineId + ", matchedWithBaseline="
				+ matchedWithBaseline + "]";
	}
}