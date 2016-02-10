package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the ScheduleScript database table.
 * 
 */
@Entity
@Table(name = "ScheduleScript")
public class ScheduleScript implements Serializable {

	public ScheduleScript() {
	}

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "scheduleScriptId")
	private Integer scheduleScriptId;
	
	@Column(name = "testCaseId")
	private Integer testCaseId;
	
	@Column(name = "testCaseStatusId")
	private Integer testCaseStatusId;

	@Column(name = "errorMessage", nullable = true)
	private String errorMessage;

	@Column(name = "executionLog", nullable = true)
	private String executionLog;
	
	@Column(name="executionStartDate")
	private Date executionStartDate;
	
	@Column(name="executionStatus")
	private Integer executionStatus;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "testCaseId", insertable = false, updatable = false, nullable = true, unique = false)
	private TransactionTestCase transactionTestCase;

	public Integer getScheduleScriptId() {
		return scheduleScriptId;
	}

	public void setScheduleScriptId(Integer scheduleScriptId) {
		this.scheduleScriptId = scheduleScriptId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Integer getTestCaseStatusId() {
		return testCaseStatusId;
	}

	public void setTestCaseStatusId(Integer testCaseStatusId) {
		this.testCaseStatusId = testCaseStatusId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public TransactionTestCase getTransactionTestCase() {
		return transactionTestCase;
	}

	public void setTransactionTestCase(TransactionTestCase transactionTestCase) {
		this.transactionTestCase = transactionTestCase;
	}

	public String getExecutionLog() {
		return executionLog;
	}

	public void setExecutionLog(String executionLog) {
		this.executionLog = executionLog;
	}

	public Date getExecutionStartDate() {
		return executionStartDate;
	}

	public void setExecutionStartDate(Date executionStartDate) {
		this.executionStartDate = executionStartDate;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}

	@Override
	public String toString() {
		return "ScheduleScript [scheduleScriptId=" + scheduleScriptId
				+", testCaseId=" + testCaseId
				+ ", testCaseStatusId=" + testCaseStatusId + ", errorMessage="
				+ errorMessage + ", executionLog=" + executionLog
				+ ", executionStartDate=" + executionStartDate
				+ ", executionStatus=" + executionStatus
				+ ", transactionTestCase=" + transactionTestCase + "]";
	}

}