package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the application database table.
 * 
 */
/**
 * @author manni
 *
 */
@Entity
@Table(name = "Application", uniqueConstraints = {@UniqueConstraint(columnNames={"applicationName"})})
public class Application implements Serializable {

	public Application() {
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "applicationId")
	private Integer applicationId;

	@Column(name = "applicationDescription")
	private String applicationDescription;

	@Column(name = "applicationName", nullable = false)
	private String applicationName;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeCreated")
	private Date dateTimeCreated;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeModified")
	private Date dateTimeModified;

	/*
	@OneToMany
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "applicationId", insertable = false, updatable = false, nullable = true, unique = false)
	List<AppEnvEnvironmentCategoryXref> environmentCategoryXref;
*/
	/**
	 * @return the applicationId
	 */
	public Integer getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the applicationDescription
	 */
	public String getApplicationDescription() {
		return applicationDescription;
	}

	/**
	 * @param applicationDescription the applicationDescription to set
	 */
	public void setApplicationDescription(String applicationDescription) {
		this.applicationDescription = applicationDescription;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the dateTimeCreated
	 */
	public Date getDateTimeCreated() {
		return dateTimeCreated;
	}

	/**
	 * @param dateTimeCreated the dateTimeCreated to set
	 */
	public void setDateTimeCreated(Date dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	/**
	 * @return the dateTimeModified
	 */
	public Date getDateTimeModified() {
		return dateTimeModified;
	}

	/**
	 * @param dateTimeModified the dateTimeModified to set
	 */
	public void setDateTimeModified(Date dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}

/*	public List<AppEnvEnvironmentCategoryXref> getEnvironmentCategoryXref() {
		return environmentCategoryXref;
	}

	public void setEnvironmentCategoryXref(
			List<AppEnvEnvironmentCategoryXref> environmentCategoryXref) {
		this.environmentCategoryXref = environmentCategoryXref;
	}*/

	@Override
	public String toString() {
		return "Application [applicationId=" + applicationId
				+ ", applicationDescription=" + applicationDescription
				+ ", applicationName=" + applicationName + ", dateTimeCreated="
				+ dateTimeCreated + ", dateTimeModified=" + dateTimeModified
				+ "]";
	}

}