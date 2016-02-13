package com.ensense.insense.data.uitesting.entity.mintv4;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TestSuitIncludeExcludeXref")
public class TestSuitIncludeExcludeXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "includeExcludeXrefId")
	private int includeExcludeXrefId;

	@Column(name = "suitId")
	private int suitId;
	
	@Column(name = "includeOrExcludeUrl")
	private String includeOrExcludeUrl;

	@Column(name = "includeUrl")
	private boolean includeUrl;
	
	@Column(name = "excludeUrl")
	private boolean excludeUrl;

	public int getIncludeExcludeXrefId() {
		return includeExcludeXrefId;
	}

	public void setIncludeExcludeXrefId(int includeExcludeXrefId) {
		this.includeExcludeXrefId = includeExcludeXrefId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getIncludeOrExcludeUrl() {
		return includeOrExcludeUrl;
	}

	public void setIncludeOrExcludeUrl(String includeOrExcludeUrl) {
		this.includeOrExcludeUrl = includeOrExcludeUrl;
	}

	public boolean isIncludeUrl() {
		return includeUrl;
	}

	public void setIncludeUrl(boolean includeUrl) {
		this.includeUrl = includeUrl;
	}

	public boolean isExcludeUrl() {
		return excludeUrl;
	}

	public void setExcludeUrl(boolean excludeUrl) {
		this.excludeUrl = excludeUrl;
	}
}