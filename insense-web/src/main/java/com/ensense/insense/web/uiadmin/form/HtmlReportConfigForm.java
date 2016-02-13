package com.ensense.insense.web.uiadmin.form;

public class HtmlReportConfigForm {
	private String applicationId;
	private String[] environmentId;
	private String removeTagOrSplitTag;
	private String removeTagName;
	private String splitTagName;
	private String splitContentName;
	private String configId; 
	
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String[] getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(String[] environmentId) {
		this.environmentId = environmentId;
	}
	public String getRemoveTagOrSplitTag() {
		return removeTagOrSplitTag;
	}
	public void setRemoveTagOrSplitTag(String removeTagOrSplitTag) {
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
	
	
}
