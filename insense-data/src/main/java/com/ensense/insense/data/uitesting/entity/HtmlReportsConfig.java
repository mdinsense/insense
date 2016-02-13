package com.ensense.insense.data.uitesting.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the test_schedule_table database table.
 * 
 */
@Entity
@Table(name="HtmlReportsConfig")
public class HtmlReportsConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="htmlReportsConfigId")
	private Integer htmlReportsConfigId;
	
	@Column(name="removeTag")
	private Boolean removeTag ;
	
	@Column(name="splitByContent")
	private Boolean splitByContent ;
	
	@Column(name="removeText")
	private Boolean removeText ;
	
	@Column(name = "removeTagName")
	private String removeTagName;
	
	@Column(name = "removeTextContent")
	private String removeTextContent;
	
	@Column(name = "splitTagName")
	private String splitTagName;
	
	@Column(name = "splitContentName")
	private String splitContentName;
	 
	@Column(name = "applicationId")
	private Integer applicationId;
	
	@Column(name = "environmentId")
	private Integer environmentId;
	
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

	/**
	 * @return the htmlReportsConfigId
	 */
	public Integer getHtmlReportsConfigId() {
		return htmlReportsConfigId;
	}

	/**
	 * @param htmlReportsConfigId the htmlReportsConfigId to set
	 */
	public void setHtmlReportsConfigId(Integer htmlReportsConfigId) {
		this.htmlReportsConfigId = htmlReportsConfigId;
	}

	/**
	 * @return the removeTag
	 */
	public Boolean getRemoveTag() {
		return removeTag;
	}

	/**
	 * @param removeTag the removeTag to set
	 */
	public void setRemoveTag(Boolean removeTag) {
		this.removeTag = removeTag;
	}

	/**
	 * @return the splitByContent
	 */
	public Boolean getSplitByContent() {
		return splitByContent;
	}

	/**
	 * @param splitByContent the splitByContent to set
	 */
	public void setSplitByContent(Boolean splitByContent) {
		this.splitByContent = splitByContent;
	}

	/**
	 * @return the removeTagName
	 */
	public String getRemoveTagName() {
		return removeTagName;
	}

	/**
	 * @param removeTagName the removeTagName to set
	 */
	public void setRemoveTagName(String removeTagName) {
		this.removeTagName = removeTagName;
	}

	/**
	 * @return the splitTagName
	 */
	public String getSplitTagName() {
		return splitTagName;
	}

	/**
	 * @param splitTagName the splitTagName to set
	 */
	public void setSplitTagName(String splitTagName) {
		this.splitTagName = splitTagName;
	}

	/**
	 * @return the splitContentName
	 */
	public String getSplitContentName() {
		return splitContentName;
	}

	/**
	 * @param splitContentName the splitContentName to set
	 */
	public void setSplitContentName(String splitContentName) {
		this.splitContentName = splitContentName;
	}

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
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
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
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	
	public void setRemoveText(Boolean removeText) {
		this.removeText = removeText;
	}

	public Boolean getRemoveText() {
		return removeText;
	}
	
	public void setRemoveTextContent(String removeTextContent) {
		this.removeTextContent = removeTextContent;
	}

	public String getRemoveTextContent() {
		return removeTextContent;
	}

	@Override
	public String toString() {
		return "HtmlReportsConfig [htmlReportsConfigId=" + htmlReportsConfigId
				+ ", removeTag=" + removeTag + ", splitByContent="
				+ splitByContent + ", removeText=" + removeText
				+ ", removeTagName=" + removeTagName + ", removeTextContent="
				+ removeTextContent + ", splitTagName=" + splitTagName
				+ ", splitContentName=" + splitContentName + ", applicationId="
				+ applicationId + ", environmentId=" + environmentId
				+ ", application=" + application + ", environment="
				+ environment + "]";
	}
	
}