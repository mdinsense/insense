package com.ensense.insense.data.uitesting.entity;

import com.ensense.insense.data.common.entity.Users;
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
@Table(name = "Suit", uniqueConstraints = {@UniqueConstraint(columnNames={"suitName"})})
public class Suit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "suitId")
	private Integer suitId;

	@Column(name = "suitName", nullable = false)
	private String suitName;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "applicationId", nullable = false)
	private Integer applicationId;

	@Column(name = "environmentId", nullable = false)
	private Integer environmentId;
	
	@Column(name = "environmentCategoryId", nullable = false)
	private Integer environmentCategoryId;

	@Column(name = "loginId ")
	private Integer loginId;

	@Column(name = "compareBaselineScheduleId")
	private Integer compareBaselineScheduleId;

	@Column(name = "browserTypeId", nullable = false)
	private Integer browserTypeId;
	
	@Column(name = "solutionTypeId", nullable = false)
	private Integer solutionTypeId;

	@Column(name = "analyticsTesting")
	private boolean analyticsTesting;

	@Column(name = "robotClicking", nullable = false)
	private boolean robotClicking;

	@Column(name = "textComparisonReport")
	private boolean textComparisonReport;

	@Column(name = "brokenUrlReport")
	private boolean brokenUrlReport;

	@Column(name = "processChildUrls")
	private boolean processChildUrls;
	
	@Column(name="regressionTesting")
	private boolean regressionTesting;

	@Column(name = "stopAfter")
	private Integer stopAfter;

	@Column(name = "akamaiTesting")
	private boolean akamaiTesting;

	@Column(name = "emailOnCompletion")
	private boolean emailOnCompletion;

	@Column(name = "emailOnFailure")
	private boolean emailOnFailure;

	@Column(name = "emailIds")
	private String emailIds;

	@Column(name = "maxWaitTime")
	private String maxWaitTime;

	@Column(name = "noOfThreads")
	private String noOfThreads;

	@Column(name = "browserRestartCount")
	private String browserRestartCount;
	
	@Column(name = "userId")
	private int userId;

	@Column(name = "recordStatus")
	private String recordStatus;
	
	@Column(name="staticUrlTesting")
	private boolean staticUrlTesting;
	
	@Column(name="moduleId")
	private String moduleId;
	
	@Column(name="textCompare")
	private boolean textCompare;
	
	@Column(name="htmlCompare")
	private boolean htmlCompare;
	
	@Column(name="screenCompare")
	private boolean screenCompare;
	
	@Column(name="privateSuit")
	private boolean privateSuit;
	
	@Column(name = "urlLevel", columnDefinition = "int default 0")
	private int urlLevel;
	
	@Column(name="transactional", columnDefinition = "boolean default false")
	private boolean transactional;
	
	@Column(name = "functionalityTypeId", nullable=false)
	private int functionalityTypeId;
	
	@Column(name = "scrollPage", columnDefinition = "boolean default false")
	private boolean scrollPage;
	
	@Column(name = "waitTime")
	private Integer waitTime;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "userId", insertable = false, updatable = false, nullable = true, unique = false)
	private Users users;

	public int getUrlLevel() {
		return urlLevel;
	}

	public void setUrlLevel(int urlLevel) {
		this.urlLevel = urlLevel;
	}

	public Integer getSuitId() {
		return suitId;
	}

	public void setSuitId(Integer suitId) {
		this.suitId = suitId;
	}

	public String getSuitName() {
		return suitName;
	}

	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public Integer getCompareBaselineScheduleId() {
		return compareBaselineScheduleId;
	}

	public void setCompareBaselineScheduleId(Integer compareBaselineScheduleId) {
		this.compareBaselineScheduleId = compareBaselineScheduleId;
	}

	public Integer getBrowserTypeId() {
		return browserTypeId;
	}

	public void setBrowserTypeId(Integer browserTypeId) {
		this.browserTypeId = browserTypeId;
	}

	public Integer getSolutionTypeId() {
		return solutionTypeId;
	}

	public void setSolutionTypeId(Integer solutionTypeId) {
		this.solutionTypeId = solutionTypeId;
	}

	public boolean isAnalyticsTesting() {
		return analyticsTesting;
	}

	public void setAnalyticsTesting(boolean analyticsTesting) {
		this.analyticsTesting = analyticsTesting;
	}

	public boolean isRobotClicking() {
		return robotClicking;
	}

	public void setRobotClicking(boolean robotClicking) {
		this.robotClicking = robotClicking;
	}

	public boolean isTextComparisonReport() {
		return textComparisonReport;
	}

	public void setTextComparisonReport(boolean textComparisonReport) {
		this.textComparisonReport = textComparisonReport;
	}

	public boolean isBrokenUrlReport() {
		return brokenUrlReport;
	}

	public void setBrokenUrlReport(boolean brokenUrlReport) {
		this.brokenUrlReport = brokenUrlReport;
	}

	public boolean isProcessChildUrls() {
		return processChildUrls;
	}

	public void setProcessChildUrls(boolean processChildUrls) {
		this.processChildUrls = processChildUrls;
	}

	public boolean isRegressionTesting() {
		return regressionTesting;
	}

	public void setRegressionTesting(boolean regressionTesting) {
		this.regressionTesting = regressionTesting;
	}

	public Integer getStopAfter() {
		return stopAfter;
	}

	public void setStopAfter(Integer stopAfter) {
		this.stopAfter = stopAfter;
	}

	public boolean isAkamaiTesting() {
		return akamaiTesting;
	}

	public void setAkamaiTesting(boolean akamaiTesting) {
		this.akamaiTesting = akamaiTesting;
	}

	public boolean isEmailOnCompletion() {
		return emailOnCompletion;
	}

	public void setEmailOnCompletion(boolean emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}

	public boolean isEmailOnFailure() {
		return emailOnFailure;
	}

	public void setEmailOnFailure(boolean emailOnFailure) {
		this.emailOnFailure = emailOnFailure;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public String getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(String maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public String getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(String noOfThreads) {
		this.noOfThreads = noOfThreads;
	}

	public String getBrowserRestartCount() {
		return browserRestartCount;
	}

	public void setBrowserRestartCount(String browserRestartCount) {
		this.browserRestartCount = browserRestartCount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public boolean isStaticUrlTesting() {
		return staticUrlTesting;
	}

	public void setStaticUrlTesting(boolean staticUrlTesting) {
		this.staticUrlTesting = staticUrlTesting;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public boolean isTextCompare() {
		return textCompare;
	}

	public void setTextCompare(boolean textCompare) {
		this.textCompare = textCompare;
	}

	public boolean isHtmlCompare() {
		return htmlCompare;
	}

	public void setHtmlCompare(boolean htmlCompare) {
		this.htmlCompare = htmlCompare;
	}

	public boolean isScreenCompare() {
		return screenCompare;
	}

	public void setScreenCompare(boolean screenCompare) {
		this.screenCompare = screenCompare;
	}

	public boolean isPrivateSuit() {
		return privateSuit;
	}

	public void setPrivateSuit(boolean privateSuit) {
		this.privateSuit = privateSuit;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public boolean isTransactionTestcase() {
		return transactional;
	}

	public void setTransactionTestcase(boolean transactional) {
		this.transactional = transactional;
	}

	public void setFunctionalityTypeId(int functionalityTypeId) {
		this.functionalityTypeId = functionalityTypeId;
	}

	public int getFunctionalityTypeId() {
		return functionalityTypeId;
	}

	public boolean isScrollPage() {
		return scrollPage;
	}

	public void setScrollPage(boolean scrollPage) {
		this.scrollPage = scrollPage;
	}

	public Integer getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public String toString() {
		return "Suit [suitId=" + suitId + ", suitName=" + suitName + ", type="
				+ type + ", applicationId=" + applicationId
				+ ", environmentId=" + environmentId
				+ ", environmentCategoryId=" + environmentCategoryId
				+ ", loginId=" + loginId + ", compareBaselineScheduleId="
				+ compareBaselineScheduleId + ", browserTypeId="
				+ browserTypeId + ", solutionTypeId=" + solutionTypeId
				+ ", analyticsTesting=" + analyticsTesting + ", robotClicking="
				+ robotClicking + ", textComparisonReport="
				+ textComparisonReport + ", brokenUrlReport=" + brokenUrlReport
				+ ", processChildUrls=" + processChildUrls
				+ ", regressionTesting=" + regressionTesting + ", stopAfter="
				+ stopAfter + ", akamaiTesting=" + akamaiTesting
				+ ", emailOnCompletion=" + emailOnCompletion
				+ ", emailOnFailure=" + emailOnFailure + ", emailIds="
				+ emailIds + ", maxWaitTime=" + maxWaitTime + ", noOfThreads="
				+ noOfThreads + ", browserRestartCount=" + browserRestartCount
				+ ", userId=" + userId + ", recordStatus=" + recordStatus
				+ ", staticUrlTesting=" + staticUrlTesting + ", moduleId="
				+ moduleId + ", textCompare=" + textCompare + ", htmlCompare="
				+ htmlCompare + ", screenCompare=" + screenCompare
				+ ", privateSuit=" + privateSuit + ", urlLevel=" + urlLevel
				+ ", transactional=" + transactional + ", functionalityTypeId="
				+ functionalityTypeId + ", scrollPage=" + scrollPage
				+ ", waitTime=" + waitTime + ", users=" + users + "]";
	}
	
	
	

}