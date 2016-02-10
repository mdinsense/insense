package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "ApplicationConfig")
public class ApplicationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "configId")
	private Integer configId;

	@Column(name = "applicationId")
	private Integer applicationId;

	@Column(name = "environmentId")
	private Integer environmentId;

	@Column(name = "browserTimeout")
	private Integer browserTimeout;

	@Column(name = "noOfBrowsers")
	private Integer noOfBrowsers;

	@Column(name = "browserRestart")
	private Integer browserRestart;

	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "applicationId", insertable = false, updatable = false, nullable = true, unique = false)
	private Application application;

	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentId", insertable = false, updatable = false, nullable = true, unique = false)
	private Environment environment;

	public ApplicationConfig() {
		this.browserTimeout = 60;
		this.noOfBrowsers = 1;
		this.browserRestart = 200;
	}

	/**
	 * @return the configId
	 */
	public Integer getConfigId() {
		return configId;
	}

	/**
	 * @param configId
	 *            the configId to set
	 */
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	/**
	 * @return the applicationId
	 */
	public Integer getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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
	 * @return the browserTimeout
	 */
	public Integer getBrowserTimeout() {
		return browserTimeout;
	}

	/**
	 * @param browserTimeout
	 *            the browserTimeout to set
	 */
	public void setBrowserTimeout(Integer browserTimeout) {
		this.browserTimeout = browserTimeout;
	}

	/**
	 * @return the noOfBrowsers
	 */
	public Integer getNoOfBrowsers() {
		return noOfBrowsers;
	}

	/**
	 * @param noOfBrowsers
	 *            the noOfBrowsers to set
	 */
	public void setNoOfBrowsers(Integer noOfBrowsers) {
		this.noOfBrowsers = noOfBrowsers;
	}

	/**
	 * @return the browserRestart
	 */
	public Integer getBrowserRestart() {
		return browserRestart;
	}

	/**
	 * @param browserRestart
	 *            the browserRestart to set
	 */
	public void setBrowserRestart(Integer browserRestart) {
		this.browserRestart = browserRestart;
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "ApplicationConfig [configId=" + configId + ", applicationId="
				+ applicationId + ", environmentId=" + environmentId
				+ ", browserTimeout=" + browserTimeout + ", noOfBrowsers="
				+ noOfBrowsers + ", browserRestart=" + browserRestart
				+ ", application=" + application + ", environment="
				+ environment + "]";
	}

	
}
