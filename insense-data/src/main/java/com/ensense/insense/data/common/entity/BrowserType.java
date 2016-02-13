package com.ensense.insense.data.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BrowserType", uniqueConstraints = {@UniqueConstraint(columnNames={"browserName"})})
public class BrowserType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "browserTypeId")
	private Integer browserTypeId;

	@Column(name = "browserName")
	private String browserName;

	public Integer getBrowserTypeId() {
		return browserTypeId;
	}

	public void setBrowserTypeId(Integer browserTypeId) {
		this.browserTypeId = browserTypeId;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	@Override
	public String toString() {
		return "BrowserType [browserTypeId=" + browserTypeId + ", browserName="
				+ browserName + "]";
	}
	
}
