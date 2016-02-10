package uiadmin.form.schedule;

import java.util.Arrays;


public class TestingForm {
	private Integer suitId;
	private String suitName;
	private Integer applicationId;
	private Integer environmentId;
	private Integer environmentCategoryId;
	private Integer loginId;
	private Integer browserType;
	private boolean textCompare;
	private boolean htmlCompare;
	private boolean screenCompare;
	private String[] modules;
	private String[] transactionModules;
	private String entireApplication;
	private boolean smartUser;
	private boolean processUrl;
	private String suitType;
	private boolean privateSuit;
	private String editOrViewMode;
	private Integer urlLevel;
	private String transactionOrCrawlerType;
	private boolean findText;
	private boolean findImage;
	private String[] textName;
	private String[] imageName;
	private boolean brokenLinks;
	private boolean brokenLinksResources;
	private boolean loadAttributes;
	private boolean scrollPage;
	private Integer waitTime;
	
	
	public TestingForm(){
		urlLevel = 0;
		waitTime = 3100;
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

	public Integer getBrowserType() {
		return browserType;
	}

	public void setBrowserType(Integer browserType) {
		this.browserType = browserType;
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

	public String[] getModules() {
		return modules;
	}

	public void setModules(String[] modules) {
		this.modules = modules;
	}

	public String[] getTransactionModules() {
		return transactionModules;
	}

	public void setTransactionModules(String[] transactionModules) {
		this.transactionModules = transactionModules;
	}

	public String getEntireApplication() {
		return entireApplication;
	}

	public void setEntireApplication(String entireApplication) {
		this.entireApplication = entireApplication;
	}

	public boolean isSmartUser() {
		return smartUser;
	}

	public void setSmartUser(boolean smartUser) {
		this.smartUser = smartUser;
	}

	public boolean isProcessUrl() {
		return processUrl;
	}

	public void setProcessUrl(boolean processUrl) {
		this.processUrl = processUrl;
	}

	public String getSuitType() {
		return suitType;
	}

	public void setSuitType(String suitType) {
		this.suitType = suitType;
	}

	public boolean isPrivateSuit() {
		return privateSuit;
	}

	public void setPrivateSuit(boolean privateSuit) {
		this.privateSuit = privateSuit;
	}

	public String getEditOrViewMode() {
		return editOrViewMode;
	}

	public void setEditOrViewMode(String editOrViewMode) {
		this.editOrViewMode = editOrViewMode;
	}

	public Integer getUrlLevel() {
		return urlLevel;
	}

	public void setUrlLevel(Integer urlLevel) {
		this.urlLevel = urlLevel;
	}

	public String getTransactionOrCrawlerType() {
		return transactionOrCrawlerType;
	}

	public void setTransactionOrCrawlerType(String transactionOrCrawlerType) {
		this.transactionOrCrawlerType = transactionOrCrawlerType;
	}

	public boolean isFindText() {
		return findText;
	}

	public void setFindText(boolean findText) {
		this.findText = findText;
	}

	public boolean isFindImage() {
		return findImage;
	}

	public void setFindImage(boolean findImage) {
		this.findImage = findImage;
	}

	public String[] getTextName() {
		return textName;
	}

	public void setTextName(String[] textName) {
		this.textName = textName;
	}

	public String[] getImageName() {
		return imageName;
	}

	public void setImageName(String[] imageName) {
		this.imageName = imageName;
	}

	public boolean isBrokenLinks() {
		return brokenLinks;
	}

	public void setBrokenLinks(boolean brokenLinks) {
		this.brokenLinks = brokenLinks;
	}

	public boolean isBrokenLinksResources() {
		return brokenLinksResources;
	}

	public void setBrokenLinksResources(boolean brokenLinksResources) {
		this.brokenLinksResources = brokenLinksResources;
	}

	public boolean isLoadAttributes() {
		return loadAttributes;
	}

	public void setLoadAttributes(boolean loadAttributes) {
		this.loadAttributes = loadAttributes;
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
		return "TestingForm [suitId=" + suitId + ", suitName=" + suitName
				+ ", applicationId=" + applicationId + ", environmentId="
				+ environmentId + ", environmentCategoryId="
				+ environmentCategoryId + ", loginId=" + loginId
				+ ", browserType=" + browserType + ", textCompare="
				+ textCompare + ", htmlCompare=" + htmlCompare
				+ ", screenCompare=" + screenCompare + ", modules="
				+ Arrays.toString(modules) + ", transactionModules="
				+ Arrays.toString(transactionModules) + ", entireApplication="
				+ entireApplication + ", smartUser=" + smartUser
				+ ", processUrl=" + processUrl + ", suitType=" + suitType
				+ ", privateSuit=" + privateSuit + ", editOrViewMode="
				+ editOrViewMode + ", urlLevel=" + urlLevel
				+ ", transactionOrCrawlerType=" + transactionOrCrawlerType
				+ ", findText=" + findText + ", findImage=" + findImage
				+ ", textName=" + Arrays.toString(textName) + ", imageName="
				+ Arrays.toString(imageName) + ", brokenLinks=" + brokenLinks
				+ ", brokenLinksResources=" + brokenLinksResources
				+ ", loadAttributes=" + loadAttributes + ", scrollPage="
				+ scrollPage + ", waitTime=" + waitTime + "]";
	}


	

}
