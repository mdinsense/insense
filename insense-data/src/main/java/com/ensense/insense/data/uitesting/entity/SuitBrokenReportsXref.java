package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SuitBrokenReportsXref")
public class SuitBrokenReportsXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "suitBrokenReportsXrefId")
	private int suitBrokenReportsXrefId;

	@Column(name = "suitId")
	private int suitId;
	
	@Column(name="reportName")
	private String reportName;
	
	public int getSuitBrokenReportsXrefId() {
		return suitBrokenReportsXrefId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setSuitBrokenReportsXrefId(int suitBrokenReportsXrefId) {
		this.suitBrokenReportsXrefId = suitBrokenReportsXrefId;
	}

	@Override
	public String toString() {
		return "SuitBrokenReportsXref [suitBrokenReportsXrefId="
				+ suitBrokenReportsXrefId + ", suitId=" + suitId
				+ ", reportName=" + reportName + "]";
	}
	
}