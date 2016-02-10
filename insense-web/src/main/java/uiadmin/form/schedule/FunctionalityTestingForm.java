package uiadmin.form.schedule;

import java.util.Arrays;


public class FunctionalityTestingForm {
	private Integer suitId;
	private String suitName;
	private Integer applicationId;
	private Integer environmentCategoryId;
	private String loginId;
	private Integer browserType;
	private boolean textCompare;
	private boolean htmlCompare;
	private boolean screenCompare;
	private String[] modules;
	private String entireApplication;
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
	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}
	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
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
	public String getEntireApplication() {
		return entireApplication;
	}
	public void setEntireApplication(String entireApplication) {
		this.entireApplication = entireApplication;
	}
	@Override
	public String toString() {
		return "FunctionalityTestingForm [suitId=" + suitId + ", suitName="
				+ suitName + ", applicationId=" + applicationId
				+ ", environmentCategoryId=" + environmentCategoryId
				+ ", loginId=" + loginId + ", browserType=" + browserType
				+ ", textCompare=" + textCompare + ", htmlCompare="
				+ htmlCompare + ", screenCompare=" + screenCompare
				+ ", modules=" + Arrays.toString(modules)
				+ ", entireApplication=" + entireApplication + "]";
	} 
	
	
}
