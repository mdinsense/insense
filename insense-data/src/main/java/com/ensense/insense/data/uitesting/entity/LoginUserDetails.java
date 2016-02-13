package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * The persistent class for the LoginUserDetails database table.
 * 
 */
@Entity
@Table(name = "LoginUserDetails", uniqueConstraints = {@UniqueConstraint(columnNames={"applicationId", "environmentId", "loginId"})})

public class LoginUserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "loginUserDetailId")
	private Integer loginUserDetailId;

	@Column(name = "environmentId", nullable = false)
	private Integer environmentId;

	@Column(name = "applicationId", nullable = false)
	private Integer applicationId;

	@Column(name="loginId", nullable = false)
	private String loginId;
	
	@Column(name="password", nullable = false)
	private String password;
	
	@Column(name="rsaEnabled", nullable = false)
	private boolean rsaEnabled;
	
	@Column(name="secutiryAnswer", nullable = true)
	private String securityAnswer;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeCreated")
	private Date dateTimeCreated;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateTimeModified")
	private Date dateTimeModified;
	
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

	/*@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "loginUserDetailId", insertable = false, updatable = false, nullable = true, unique = false)
	private List<LoginUiElement> loginUiElements = new ArrayList<LoginUiElement>();*/
	
	/**
	 * @return the loginUserDetailId
	 */
	public Integer getLoginUserDetailId() {
		return loginUserDetailId;
	}

	/**
	 * @param loginUserDetailId
	 *            the loginUserDetailId to set
	 */
	public void setLoginUserDetailId(Integer loginUserDetailId) {
		this.loginUserDetailId = loginUserDetailId;
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

	/**
	 * @return the loginUiElements
	 *//*
	public List<LoginUiElement> getLoginUiElements() {
		return loginUiElements;
	}

	public void setLoginUiElements(List<LoginUiElement> loginUiElements) {
		this.loginUiElements = loginUiElements;
	}
*/
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRsaEnabled() {
		return rsaEnabled;
	}

	public void setRsaEnabled(boolean rsaEnabled) {
		this.rsaEnabled = rsaEnabled;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public Date getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(Date dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	public Date getDateTimeModified() {
		return dateTimeModified;
	}

	public void setDateTimeModified(Date dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}

	@Override
	public String toString() {
		return "LoginUserDetails [loginUserDetailId=" + loginUserDetailId
				+ ", environmentId=" + environmentId + ", applicationId="
				+ applicationId + ", loginId=" + loginId + ", rsaEnabled=" + rsaEnabled + ", securityAnswer="
				+ securityAnswer + ", dateTimeCreated=" + dateTimeCreated
				+ ", dateTimeModified=" + dateTimeModified + ", application="
				+ application + ", environment=" + environment + "]";
	}
}
