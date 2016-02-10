package com.ensense.insense.data.uitesting.entity.mintv4;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the TestSuitBrowserConfig database table.
 * 
 */
@Entity
@Table(name = "TestSuitBrowserConfig", uniqueConstraints = {@UniqueConstraint(columnNames={"suitId"})})
public class TestSuitBrowserConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "TestSuitBrowserConfigId")
	private Integer TestSuitBrowserConfigId;
	
	@Column(name = "suitId", nullable = false)
	private Integer suitId;

	@Column(name = "browserTypeId", nullable = false)
	private Integer browserTypeId;

	public Integer getTestSuitBrowserConfigId() {
		return TestSuitBrowserConfigId;
	}

	public void setTestSuitBrowserConfigId(Integer testSuitBrowserConfigId) {
		TestSuitBrowserConfigId = testSuitBrowserConfigId;
	}

	public Integer getSuitId() {
		return suitId;
	}

	public void setSuitId(Integer suitId) {
		this.suitId = suitId;
	}

	public Integer getBrowserTypeId() {
		return browserTypeId;
	}

	public void setBrowserTypeId(Integer browserTypeId) {
		this.browserTypeId = browserTypeId;
	}

	@Override
	public String toString() {
		return "TestSuitBrowserConfig [TestSuitBrowserConfigId="
				+ TestSuitBrowserConfigId + ", suitId=" + suitId
				+ ", browserTypeId=" + browserTypeId + "]";
	}

}