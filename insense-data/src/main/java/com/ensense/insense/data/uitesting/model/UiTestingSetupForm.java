package com.ensense.insense.data.uitesting.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.Serializable;


public class UiTestingSetupForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer setupTabNumber;
	private Integer applicationId;
	private String applicationName;
	private String applicationDescription;
	private Integer environmentId;
	private String environmentName;
	private String  loginOrHomeUrl;
	private Boolean secureSite;
	private Boolean saveEnvironment;
	private Integer environmentCategoryId;
	private String loginScriptPath;
	private String includeUrl;
	private String excludeUrl;
	private Integer includeUrlId;
	private Integer excludeUrlId;
	private CommonsMultipartFile loginScriptFile;
	private Integer configId;
	private Integer browserTimeout;
	private Integer noOfBrowsers;
	private Integer browserRestart;
	private Boolean isPageLoad;
	private String elementsArray;
	private String testLoginId;
	private Integer isEnvironmentEdit;
	private Integer removeTagOrSplitTag;
	private String removeTagName;
	private String splitTagName;
	private String removeTextContent;
	private String splitContentName;
	private String moduleName;
	private String includeUrlPattern;
	private String testUrlPattern;
	private String[] environments;
	private Integer categoryId;
	private int applicationModuleXrefId;
	private Integer[] environmentIds;
	private Integer loginUserId;
	private Integer moduleTypeId;
	private CommonsMultipartFile staticUrlFile;
	private CommonsMultipartFile[] testCaseFile;
	private CommonsMultipartFile[] testHtmlFile;
	private String[] testCaseName;
	private Integer columnPosition;
	private String startUrlPattern;
	private String loginId;
	private String password;
	private String securityAnswer;
	private boolean rsaEnabled;
	private String[] systemIncludeUrls;
	private String[] systemExcludeUrls;
	private Integer analyticsExcludeLinkId;
	private Integer excludeLinkType;
	private String excludeLink;
	private String[] systemExcludeLink;
	private Integer oldTestCaseId;
	
	public UiTestingSetupForm() {
		this.isEnvironmentEdit = 0;
		this.browserTimeout = 60;
		this.noOfBrowsers = 1;
		this.browserRestart = 200;
	}
	public Integer getAnalyticsExcludeLinkId() {
		return analyticsExcludeLinkId;
	}
	public void setAnalyticsExcludeLinkId(Integer analyticsExcludeLinkId) {
		this.analyticsExcludeLinkId = analyticsExcludeLinkId;
	}
	public Integer getExcludeLinkType() {
		return excludeLinkType;
	}
	public void setExcludeLinkType(Integer excludeLinkType) {
		this.excludeLinkType = excludeLinkType;
	}
	public String getExcludeLink() {
		return excludeLink;
	}
	public void setExcludeLink(String excludeLink) {
		this.excludeLink = excludeLink;
	}
	public String[] getSystemExcludeLink() {
		return systemExcludeLink;
	}
	public void setSystemExcludeLink(String[] systemExcludeLink) {
		this.systemExcludeLink = systemExcludeLink;
	}
	public String[] getSystemIncludeUrls() {
		return systemIncludeUrls;
	}
	public void setSystemIncludeUrls(String[] systemIncludeUrls) {
		this.systemIncludeUrls = systemIncludeUrls;
	}
	public String[] getSystemExcludeUrls() {
		return systemExcludeUrls;
	}
	public void setSystemExcludeUrls(String[] systemExcludeUrls) {
		this.systemExcludeUrls = systemExcludeUrls;
	}
	public Integer getSetupTabNumber() {
		return setupTabNumber;
	}
	public void setSetupTabNumber(Integer setupTabNumber) {
		this.setupTabNumber = setupTabNumber;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationDescription() {
		return applicationDescription;
	}
	public void setApplicationDescription(String applicationDescription) {
		this.applicationDescription = applicationDescription;
	}
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
	public Boolean getSecureSite() {
		return secureSite;
	}
	public void setSecureSite(Boolean secureSite) {
		this.secureSite = secureSite;
	}
	public Boolean getSaveEnvironment() {
		return saveEnvironment;
	}
	public void setSaveEnvironment(Boolean saveEnvironment) {
		this.saveEnvironment = saveEnvironment;
	}
	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}
	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}
	public String getLoginScriptPath() {
		return loginScriptPath;
	}
	public void setLoginScriptPath(String loginScriptPath) {
		this.loginScriptPath = loginScriptPath;
	}
	public String getIncludeUrl() {
		return includeUrl;
	}
	public void setIncludeUrl(String includeUrl) {
		this.includeUrl = includeUrl;
	}
	public String getExcludeUrl() {
		return excludeUrl;
	}
	public void setExcludeUrl(String excludeUrl) {
		this.excludeUrl = excludeUrl;
	}
	public Integer getIncludeUrlId() {
		return includeUrlId;
	}
	public void setIncludeUrlId(Integer includeUrlId) {
		this.includeUrlId = includeUrlId;
	}
	public Integer getExcludeUrlId() {
		return excludeUrlId;
	}
	public void setExcludeUrlId(Integer excludeUrlId) {
		this.excludeUrlId = excludeUrlId;
	}
	public CommonsMultipartFile getLoginScriptFile() {
		return loginScriptFile;
	}
	public void setLoginScriptFile(CommonsMultipartFile loginScriptFile) {
		this.loginScriptFile = loginScriptFile;
	}
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public Integer getBrowserTimeout() {
		return browserTimeout;
	}
	public void setBrowserTimeout(Integer browserTimeout) {
		this.browserTimeout = browserTimeout;
	}
	public Integer getNoOfBrowsers() {
		return noOfBrowsers;
	}
	public void setNoOfBrowsers(Integer noOfBrowsers) {
		this.noOfBrowsers = noOfBrowsers;
	}
	public Integer getBrowserRestart() {
		return browserRestart;
	}
	public void setBrowserRestart(Integer browserRestart) {
		this.browserRestart = browserRestart;
	}
	public Boolean getIsPageLoad() {
		return isPageLoad;
	}
	public void setIsPageLoad(Boolean isPageLoad) {
		this.isPageLoad = isPageLoad;
	}
	public String getElementsArray() {
		return elementsArray;
	}
	public void setElementsArray(String elementsArray) {
		this.elementsArray = elementsArray;
	}
	public String getTestLoginId() {
		return testLoginId;
	}
	public void setTestLoginId(String testLoginId) {
		this.testLoginId = testLoginId;
	}
	public Integer getIsEnvironmentEdit() {
		return isEnvironmentEdit;
	}
	public void setIsEnvironmentEdit(Integer isEnvironmentEdit) {
		this.isEnvironmentEdit = isEnvironmentEdit;
	}
	public Integer getRemoveTagOrSplitTag() {
		return removeTagOrSplitTag;
	}
	public void setRemoveTagOrSplitTag(Integer removeTagOrSplitTag) {
		this.removeTagOrSplitTag = removeTagOrSplitTag;
	}
	public String getRemoveTagName() {
		return removeTagName;
	}
	public void setRemoveTagName(String removeTagName) {
		this.removeTagName = removeTagName;
	}
	public String getSplitTagName() {
		return splitTagName;
	}
	public void setSplitTagName(String splitTagName) {
		this.splitTagName = splitTagName;
	}
	public String getSplitContentName() {
		return splitContentName;
	}
	public void setSplitContentName(String splitContentName) {
		this.splitContentName = splitContentName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getIncludeUrlPattern() {
		return includeUrlPattern;
	}
	public void setIncludeUrlPattern(String includeUrlPattern) {
		this.includeUrlPattern = includeUrlPattern;
	}
	public String getTestUrlPattern() {
		return testUrlPattern;
	}
	public void setTestUrlPattern(String testUrlPattern) {
		this.testUrlPattern = testUrlPattern;
	}
	public String[] getEnvironments() {
		return environments;
	}
	public void setEnvironments(String[] environments) {
		this.environments = environments;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public int getApplicationModuleXrefId() {
		return applicationModuleXrefId;
	}
	public void setApplicationModuleXrefId(int applicationModuleXrefId) {
		this.applicationModuleXrefId = applicationModuleXrefId;
	}
	public Integer[] getEnvironmentIds() {
		return environmentIds;
	}
	public void setEnvironmentIds(Integer[] environmentIds) {
		this.environmentIds = environmentIds;
	}
	public Integer getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(Integer loginUserId) {
		this.loginUserId = loginUserId;
	}
	public Integer getModuleTypeId() {
		return moduleTypeId;
	}
	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}
	public CommonsMultipartFile getStaticUrlFile() {
		return staticUrlFile;
	}
	public void setStaticUrlFile(CommonsMultipartFile staticUrlFile) {
		this.staticUrlFile = staticUrlFile;
	}
	public Integer getColumnPosition() {
		return columnPosition;
	}
	public void setColumnPosition(Integer columnPosition) {
		this.columnPosition = columnPosition;
	}
	public String getStartUrlPattern() {
		return startUrlPattern;
	}
	public void setStartUrlPattern(String startUrlPattern) {
		this.startUrlPattern = startUrlPattern;
	}
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
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public boolean isRsaEnabled() {
		return rsaEnabled;
	}
	public void setRsaEnabled(boolean rsaEnabled) {
		this.rsaEnabled = rsaEnabled;
	}
	public CommonsMultipartFile[] getTestCaseFile() {
		return testCaseFile;
	}
	public void setTestCaseFile(CommonsMultipartFile[] testCaseFile) {
		this.testCaseFile = testCaseFile;
	}
	public String[] getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String[] testCaseName) {
		this.testCaseName = testCaseName;
	}
	public Integer getOldTestCaseId() {
		return oldTestCaseId;
	}
	public void setOldTestCaseId(Integer oldTestCaseId) {
		this.oldTestCaseId = oldTestCaseId;
	}
	public CommonsMultipartFile[] getTestHtmlFile() {
		return testHtmlFile;
	}
	public void setTestHtmlFile(CommonsMultipartFile[] testHtmlFile) {
		this.testHtmlFile = testHtmlFile;
	}
	public void setRemoveTextContent(String removeTextContent) {
		this.removeTextContent = removeTextContent;
	}
	public String getRemoveTextContent() {
		return removeTextContent;
	}
}
