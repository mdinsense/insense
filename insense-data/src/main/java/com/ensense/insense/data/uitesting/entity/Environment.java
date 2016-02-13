package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the Environment database table.
 * 
 */
@Entity
@Table(name = "Environment", uniqueConstraints = {@UniqueConstraint(columnNames={"environmentName"})})
public class Environment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "environmentId")
	private Integer environmentId;

	@Column(name = "environmentName", nullable = false)
	private String environmentName;

	@Column(name = "secureSite")
	private Boolean secureSite;

	@Column(name = "loginOrHomeUrl", nullable = false)
	private String loginOrHomeUrl;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeCreated")
	private Date dateTimeCreated;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeModified")
	private Date dateTimeModified;
	
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentId",  referencedColumnName = "environmentId", insertable = false, updatable = false, nullable = true, unique = false)
	private EnvironmentLoginScript environmentLoginScript;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinTable(name = "EnvEnvironmentCategoryXref", 
	        joinColumns = @JoinColumn(name = "environmentId", referencedColumnName = "environmentId"),
	        inverseJoinColumns = @JoinColumn(name = "environmentCategoryId"))
	private EnvironmentCategory environmentCategory;
	
/*	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentId", insertable = false, updatable = false, nullable = true, unique = false)
	private List<AppEnvEnvironmentCategoryXref> environmentCategoryXref;*/
	
	/**
	 * @return the environmentId
	 */
	public Integer getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}

	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	/**
	 * @return the secureSite
	 */
	public Boolean getSecureSite() {
		return secureSite;
	}

	/**
	 * @param secureSite the secureSite to set
	 */
	public void setSecureSite(Boolean secureSite) {
		this.secureSite = secureSite;
	}

	/**
	 * @return the loginOrHomeUrl
	 */
	public String getLoginOrHomeUrl() {
		return loginOrHomeUrl;
	}

	/**
	 * @param loginOrHomeUrl the loginOrHomeUrl to set
	 */
	public void setLoginOrHomeUrl(String loginOrHomeUrl) {
		this.loginOrHomeUrl = loginOrHomeUrl;
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

	/**
	 * @return the environmentLoginScript
	 */
	public EnvironmentLoginScript getEnvironmentLoginScript() {
		return environmentLoginScript;
	}

	/**
	 * @param environmentLoginScript the environmentLoginScript to set
	 */
	public void setEnvironmentLoginScript(
			EnvironmentLoginScript environmentLoginScript) {
		this.environmentLoginScript = environmentLoginScript;
	}

	public EnvironmentCategory getEnvironmentCategory() {
		return environmentCategory;
	}

	public void setEnvironmentCategory(EnvironmentCategory environmentCategory) {
		this.environmentCategory = environmentCategory;
	}

	@Override
	public String toString() {
		return "Environment [environmentId=" + environmentId
				+ ", environmentName=" + environmentName + ", secureSite="
				+ secureSite + ", loginOrHomeUrl=" + loginOrHomeUrl
				+ ", dateTimeCreated=" + dateTimeCreated
				+ ", dateTimeModified=" + dateTimeModified
				+ ", environmentLoginScript=" + environmentLoginScript
				+ ", environmentCategory=" + environmentCategory + "]";
	}
	
}