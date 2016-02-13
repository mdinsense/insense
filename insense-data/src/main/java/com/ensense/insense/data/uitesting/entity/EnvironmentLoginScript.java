package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the EnvironmentLoginScript database table.
 * 
 */
@Entity
@Table(name = "EnvironmentLoginScript")
public class EnvironmentLoginScript implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "environmentLoginScriptId")
	private Integer environmentLoginScriptId;

	@Column(name = "environmentId", nullable = false)
	private Integer environmentId;

	@Column(name = "sourceFileName", nullable = false)
	private String sourceFileName;

	@Column(name = "sourcefolderPath", nullable = false)
	private String sourcefolderPath;

	/**
	 * @return the environmentLoginScriptId
	 */
	public Integer getEnvironmentLoginScriptId() {
		return environmentLoginScriptId;
	}

	/**
	 * @param environmentLoginScriptId
	 *            the environmentLoginScriptId to set
	 */
	public void setEnvironmentLoginScriptId(Integer environmentLoginScriptId) {
		this.environmentLoginScriptId = environmentLoginScriptId;
	}

	/**
	 * @return the environmentId
	 */
	public Integer getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId
	 *            the environmentId to set
	 */
	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return the sourceFileName
	 */
	public String getSourceFileName() {
		return sourceFileName;
	}

	/**
	 * @param sourceFileName
	 *            the sourceFileName to set
	 */
	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/**
	 * @return the sourcefolderPath
	 */
	public String getSourcefolderPath() {
		return sourcefolderPath;
	}

	/**
	 * @param sourcefolderPath
	 *            the sourcefolderPath to set
	 */
	public void setSourcefolderPath(String sourcefolderPath) {
		this.sourcefolderPath = sourcefolderPath;
	}

}
