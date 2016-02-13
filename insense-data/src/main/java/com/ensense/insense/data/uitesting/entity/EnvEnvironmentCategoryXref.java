package com.ensense.insense.data.uitesting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="EnvEnvironmentCategoryXref" , uniqueConstraints = {@UniqueConstraint(columnNames={"environmentId"})})
public class EnvEnvironmentCategoryXref {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="envEnvironmentCategoryXrefId")
	private int envEnvironmentCategoryXrefId;
	
	@Column(name = "environmentCategoryId")
	private int environmentCategoryId;
	
	@Column(name = "environmentId")
	private int environmentId;

	public int getEnvEnvironmentCategoryXrefId() {
		return envEnvironmentCategoryXrefId;
	}

	public void setEnvEnvironmentCategoryXrefId(int envEnvironmentCategoryXrefId) {
		this.envEnvironmentCategoryXrefId = envEnvironmentCategoryXrefId;
	}

	public int getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(int environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	@Override
	public String toString() {
		return "EnvEnvironmentCategoryXref [envEnvironmentCategoryXrefId="
				+ envEnvironmentCategoryXrefId + ", environmentCategoryId="
				+ environmentCategoryId + ", environmentId=" + environmentId
				+ "]";
	}
}
