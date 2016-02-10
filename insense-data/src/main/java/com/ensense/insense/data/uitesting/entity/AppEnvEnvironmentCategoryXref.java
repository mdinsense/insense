package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="AppEnvEnvironmentCategoryXref", uniqueConstraints = {@UniqueConstraint(columnNames={"environmentId","applicationId"})})
public class AppEnvEnvironmentCategoryXref implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="environmentCategoryXrefId")
	private int environmentCategoryXrefId;
	
	@Column(name = "environmentCategoryId")
	private int environmentCategoryId;
	
	@Column(name = "environmentId")
	private int environmentId;

	@Column(name = "applicationId")
	private int applicationId;
	
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
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private List<LoginUserDetails> loginUserDetailsList;
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private List<IncludeUrl> includeUrlList;
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private List<ExcludeUrl> excludeUrlList;
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private List<ApplicationModuleXref> applicationModuleXrefList;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private ApplicationConfig applicationConfig;
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns ({
        @JoinColumn(name="applicationId", referencedColumnName="applicationId", insertable=false, updatable=false),
        @JoinColumn(name="environmentId", referencedColumnName="environmentId", insertable=false, updatable=false)
    })
	private List<HtmlReportsConfig> htmlReportsConfigList;
	
	
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

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public int getEnvironmentCategoryXrefId() {
		return environmentCategoryXrefId;
	}

	public void setEnvironmentCategoryXrefId(int environmentCategoryXrefId) {
		this.environmentCategoryXrefId = environmentCategoryXrefId;
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

	public List<LoginUserDetails> getLoginUserDetailsList() {
		return loginUserDetailsList;
	}

	public void setLoginUserDetailsList(List<LoginUserDetails> loginUserDetailsList) {
		this.loginUserDetailsList = loginUserDetailsList;
	}

	public List<IncludeUrl> getIncludeUrlList() {
		return includeUrlList;
	}

	public void setIncludeUrlList(List<IncludeUrl> includeUrlList) {
		this.includeUrlList = includeUrlList;
	}

	public List<ExcludeUrl> getExcludeUrlList() {
		return excludeUrlList;
	}

	public void setExcludeUrlList(List<ExcludeUrl> excludeUrlList) {
		this.excludeUrlList = excludeUrlList;
	}

	public List<ApplicationModuleXref> getApplicationModuleXrefList() {
		return applicationModuleXrefList;
	}

	public void setApplicationModuleXrefList(
			List<ApplicationModuleXref> applicationModuleXrefList) {
		this.applicationModuleXrefList = applicationModuleXrefList;
	}

	
	public ApplicationConfig getApplicationConfig() {
		return applicationConfig;
	}

	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	public List<HtmlReportsConfig> getHtmlReportsConfigList() {
		return htmlReportsConfigList;
	}

	public void setHtmlReportsConfigList(
			List<HtmlReportsConfig> htmlReportsConfigList) {
		this.htmlReportsConfigList = htmlReportsConfigList;
	}

	@Override
	public String toString() {
		return "AppEnvEnvironmentCategoryXref [environmentCategoryXrefId="
				+ environmentCategoryXrefId + ", environmentCategoryId="
				+ environmentCategoryId + ", environmentId=" + environmentId
				+ ", applicationId=" + applicationId + ", application="
				+ application + ", environment=" + environment
				+ ", loginUserDetailsList=" + loginUserDetailsList
				+ ", includeUrlList=" + includeUrlList + ", excludeUrlList="
				+ excludeUrlList + ", applicationModuleXrefList="
				+ applicationModuleXrefList + ", applicationConfig="
				+ applicationConfig + ", htmlReportsConfigList="
				+ htmlReportsConfigList + "]";
	}

}
