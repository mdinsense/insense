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
 * The persistent class for the test_schedule_table database table.
 * 
 */
@Entity
@Table(name = "TestSuit", uniqueConstraints = {@UniqueConstraint(columnNames={"suitName"})})
public class TestSuit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "suitId")
	private Integer suitId;

	@Column(name = "suitName", nullable = false)
	private String suitName;
	
	@Column(name = "applicationName", nullable = false)
	private String applicationName;
	

	public Integer getSuitId() {
		return suitId;
	}

	public void setSuitId(Integer suitId) {
		this.suitId = suitId;
	}

	public String getSuitName() {
		return suitName;
	}

	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
}