package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TransactionTestCase")
public class TransactionTestCase implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "testCaseId")
	private int testCaseId;

	@Column(name = "applicationModuleXrefId")
	private int applicationModuleXrefId;

	@Column(name = "transactionName")
	private String transactionName;
	
	@Column(name = "testCasePath")
	private String testCasePath;
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  
	
	public int getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(int testCaseId) {
		this.testCaseId = testCaseId;
	}

	public int getApplicationModuleXrefId() {
		return applicationModuleXrefId;
	}

	public void setApplicationModuleXrefId(int applicationModuleXrefId) {
		this.applicationModuleXrefId = applicationModuleXrefId;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public String getTestCasePath() {
		return testCasePath;
	}

	public void setTestCasePath(String testCasePath) {
		this.testCasePath = testCasePath;
	}
	
	@Override
	public String toString() {
		return "TransactionTestCase [testCaseId=" + testCaseId
				+ ", applicationModuleXrefId=" + applicationModuleXrefId
				+ ", transactionName=" + transactionName + ", testCasePath="
				+ testCasePath + "]";
	}
}
