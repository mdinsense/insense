package com.ensense.insense.data.miscellaneous.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="ClearCache")
public class ClearCache {
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "clearCacheId")
	private int clearCacheId;
	
	@Column(name = "applicationName")
	private String applicationName;
	
	@Column(name = "environmentName")
	private String environmentName;

	@Column(name = "scriptPath")
	private String scriptPath;
	
	public int getClearCacheId() {
		return clearCacheId;
	}

	public void setClearCacheId(int clearCacheId) {
		this.clearCacheId = clearCacheId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	@Override
	public String toString() {
		return "ClearCache [clearCacheId=" + clearCacheId
				+ ", applicationName=" + applicationName + ", environmentName="
				+ environmentName + ", scriptPath=" + scriptPath + "]";
	}
}
