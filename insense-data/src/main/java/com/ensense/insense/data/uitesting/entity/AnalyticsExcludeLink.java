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
@Table(name = "AnalyticsExcludeLink")
public class AnalyticsExcludeLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "analyticsExcludeLinkId")
	private Integer analyticsExcludeLinkId;

	@Column(name = "applicationId", nullable = false)
	private Integer applicationId;
	
	@Column(name = "environmentId", nullable = false)
	private Integer environmentId;

	@Column(name = "excludeLinktypeId", nullable = false)
	private Integer excludeLinktypeId;
	
	@Column(name = "excludeLink", nullable = false)
	private String excludeLink;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "applicationId", insertable = false, updatable = false, nullable = true, unique = false)
	private Application application;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentId", insertable = false, updatable = false, nullable = true, unique = false)
	private Environment environment;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "excludeLinktypeId", insertable = false, updatable = false, nullable = true, unique = false)
	private ExcludeLinkType excludeLinkTypevalue;

	public Integer getAnalyticsExcludeLinkId() {
		return analyticsExcludeLinkId;
	}

	public void setAnalyticsExcludeLinkId(Integer analyticsExcludeLinkId) {
		this.analyticsExcludeLinkId = analyticsExcludeLinkId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public Integer getExcludeLinktypeId() {
		return excludeLinktypeId;
	}

	public void setExcludeLinktypeId(Integer excludeLinktypeId) {
		this.excludeLinktypeId = excludeLinktypeId;
	}

	public String getExcludeLink() {
		return excludeLink;
	}

	public void setExcludeLink(String excludeLink) {
		this.excludeLink = excludeLink;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public ExcludeLinkType getExcludeLinkTypevalue() {
		return excludeLinkTypevalue;
	}

	public void setExcludeLinkTypevalue(ExcludeLinkType excludeLinkTypevalue) {
		this.excludeLinkTypevalue = excludeLinkTypevalue;
	}

	@Override
	public String toString() {
		return "AnalyticsExcludeLink [analyticsExcludeLinkId="
				+ analyticsExcludeLinkId + ", applicationId=" + applicationId
				+ ", environmentId=" + environmentId + ", excludeLinktypeId="
				+ excludeLinktypeId + ", excludeLink=" + excludeLink
				+ ", application=" + application + ", environment="
				+ environment + ", excludeLinkTypevalue="
				+ excludeLinkTypevalue + "]";
	}
	
}
