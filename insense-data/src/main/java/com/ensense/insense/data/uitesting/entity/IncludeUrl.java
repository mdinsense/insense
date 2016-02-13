package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author 434153
 * 
 */
@Entity
@Table(name = "IncludeUrl", uniqueConstraints = {@UniqueConstraint(columnNames={"environmentId","applicationId","includeUrl"})})
public class IncludeUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "includeUrlId")
	private Integer includeUrlId;

	@Column(name = "environmentId", nullable = false)
	private Integer environmentId;

	@Column(name = "applicationId", nullable = false)
	private Integer applicationId;

	@Column(name = "includeUrl", nullable = false)
	private String includeUrl;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "applicationId", insertable = false, updatable = false, nullable = true, unique = false)
	private Application application;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentId", insertable = false, updatable = false, nullable = true, unique = false)
	private Environment environment;

	public Integer getIncludeUrlId() {
		return includeUrlId;
	}

	public void setIncludeUrlId(Integer includeUrlId) {
		this.includeUrlId = includeUrlId;
	}

	public String getIncludeUrl() {
		return includeUrl;
	}

	public void setIncludeUrl(String includeUrl) {
		this.includeUrl = includeUrl;
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
		return "IncludeUrl [includeUrlId=" + includeUrlId + ", environmentId="
				+ environmentId + ", applicationId=" + applicationId
				+ ", includeUrl=" + includeUrl + ", application=" + application
				+ ", environment=" + environment + "]";
	}

}
