package com.ensense.insense.data.uitesting.entity.mintv4;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TestSuitCompareConfigXref")
public class TestSuitCompareConfigXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "testSuitCompareConfigId")
	private int testSuitCompareConfigId;

	@Column(name = "suitId")
	private int suitId;

	@Column(name = "textCompare")
	private boolean textCompare;

	@Column(name = "htmlCompare")
	private boolean htmlCompare;

	@Column(name = "screenCompare")
	private boolean screenCompare;

	public int getTestSuitCompareConfigId() {
		return testSuitCompareConfigId;
	}

	public void setTestSuitCompareConfigId(int testSuitCompareConfigId) {
		this.testSuitCompareConfigId = testSuitCompareConfigId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public boolean isTextCompare() {
		return textCompare;
	}

	public void setTextCompare(boolean textCompare) {
		this.textCompare = textCompare;
	}

	public boolean isHtmlCompare() {
		return htmlCompare;
	}

	public void setHtmlCompare(boolean htmlCompare) {
		this.htmlCompare = htmlCompare;
	}

	public boolean isScreenCompare() {
		return screenCompare;
	}

	public void setScreenCompare(boolean screenCompare) {
		this.screenCompare = screenCompare;
	}

}