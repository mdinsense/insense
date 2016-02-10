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
 * The persistent class for the TestEnvironment database table.
 * 
 */
@Entity
@Table(name = "TestEnvironment", uniqueConstraints = {@UniqueConstraint(columnNames={"environmentName"})})
public class TestEnvironment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "environmentId")
	private Integer environmentId;
	
	@Column(name = "suitId")
	private int suitId;

	@Column(name = "environmentName", nullable = false)
	private String environmentName;
	
	@Column(name = "loginOrHomeUrl", nullable = false)
	private String loginOrHomeUrl;
	
	@Column(name = "secureSite", nullable = false)
	private boolean secureSite;
	
	@Column(name = "staticUrl", nullable = false)
	private boolean staticUrl;
	
	@Column(name = "loginScriptPath", nullable = true)
	private String loginScriptPath;
	
	@Column(name = "staticUrlFilePath", nullable = true)
	private String staticUrlFilePath;
	
	@Column(name = "urlColumnPosition", nullable = true)
	private int urlColumnPosition;

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getLoginOrHomeUrl() {
		return loginOrHomeUrl;
	}

	public void setLoginOrHomeUrl(String loginOrHomeUrl) {
		this.loginOrHomeUrl = loginOrHomeUrl;
	}

	public boolean isSecureSite() {
		return secureSite;
	}

	public void setSecureSite(boolean secureSite) {
		this.secureSite = secureSite;
	}

	public boolean isStaticUrl() {
		return staticUrl;
	}

	public void setStaticUrl(boolean staticUrl) {
		this.staticUrl = staticUrl;
	}

	public String getLoginScriptPath() {
		return loginScriptPath;
	}

	public void setLoginScriptPath(String loginScriptPath) {
		this.loginScriptPath = loginScriptPath;
	}

	public String getStaticUrlFilePath() {
		return staticUrlFilePath;
	}

	public void setStaticUrlFilePath(String staticUrlFilePath) {
		this.staticUrlFilePath = staticUrlFilePath;
	}

	public int getUrlColumnPosition() {
		return urlColumnPosition;
	}

	public void setUrlColumnPosition(int urlColumnPosition) {
		this.urlColumnPosition = urlColumnPosition;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	@Override
	public String toString() {
		return "TestEnvironment [environmentId=" + environmentId
				+ ", environmentName=" + environmentName + ", loginOrHomeUrl="
				+ loginOrHomeUrl + ", secureSite=" + secureSite
				+ ", staticUrl=" + staticUrl + ", loginScriptPath="
				+ loginScriptPath + ", staticUrlFilePath=" + staticUrlFilePath
				+ ", urlColumnPosition=" + urlColumnPosition + "]";
	}
	
}