package com.ensense.insense.data.uitesting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "EnvironmentCategory")
public class EnvironmentCategory {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="environmentCategoryId")
	private int environmentCategoryId;
	
	@Column(name = "environmentCategoryName", nullable = false)
	private String environmentCategoryName;

	public int getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(int environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public String getEnvironmentCategoryName() {
		return environmentCategoryName;
	}

	public void setEnvironmentCategoryName(String environmentCategoryName) {
		this.environmentCategoryName = environmentCategoryName;
	}

	@Override
	public String toString() {
		return "EnvironmentCategory [environmentCategoryId="
				+ environmentCategoryId + ", environmentCategoryName="
				+ environmentCategoryName + "]";
	}
	
}
