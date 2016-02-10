package com.ensense.insense.core.analytics.model;

import java.util.Map;

public class BrokenUrlData {

	int statusCode;
	String statusText;
	String resourceUrl; //url which is broken
	String appName;
	String refererUrl;
	String pageTitle;
	String requestMethod;
	String harFileName;
	String parentUrl;
	String navigationPath;
	boolean errorPage;
	String errorType;
	String linkName;
	String ajaxCallMade;
	Map<String, Long> pageLoadAttributes;
	
	/**
	 * @return the statusText
	 */
	public String getStatusText() {
		return statusText;
	}
	/**
	 * @param statusText the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	/**
	 * @return the harFileName
	 */
	public String getHarFileName() {
		return harFileName;
	}
	/**
	 * @param harFileName the harFileName to set
	 */
	public void setHarFileName(String harFileName) {
		this.harFileName = harFileName;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return requestMethod;
	}
	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	/**
	 * @return the resourceUrl
	 */
	public String getResourceUrl() {
		return resourceUrl;
	}
	/**
	 * @param resourceUrl the resourceUrl to set
	 */
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the refererUrl
	 */
	public String getRefererUrl() {
		return refererUrl;
	}
	/**
	 * @param refererUrl the refererUrl to set
	 */
	public void setRefererUrl(String refererUrl) {
		this.refererUrl = refererUrl;
	}
	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}
	/**
	 * @param pageTitle the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	public String getNavigationPath() {
		return navigationPath;
	}
	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}
	public boolean isErrorPage() {
		return errorPage;
	}
	public void setErrorPage(boolean errorPage) {
		this.errorPage = errorPage;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getAjaxCallMade() {
		return ajaxCallMade;
	}
	public void setAjaxCallMade(String ajaxCallMade) {
		this.ajaxCallMade = ajaxCallMade;
	}

	public Map<String, Long> getPageLoadAttributes() {
		return pageLoadAttributes;
	}
	public void setPageLoadAttributes(Map<String, Long> pageLoadAttributes) {
		this.pageLoadAttributes = pageLoadAttributes;
	}
	@Override
	public String toString() {
		return "BrokenUrlData [statusCode=" + statusCode + ", statusText="
				+ statusText + ", resourceUrl=" + resourceUrl + ", appName="
				+ appName + ", refererUrl=" + refererUrl + ", pageTitle="
				+ pageTitle + ", requestMethod=" + requestMethod
				+ ", harFileName=" + harFileName + ", parentUrl=" + parentUrl
				+ ", navigationPath=" + navigationPath + ", errorPage="
				+ errorPage + ", errorType=" + errorType + ", linkName="
				+ linkName + ", ajaxCallMade=" + ajaxCallMade
				+ ", pageLoadAttributes=" + pageLoadAttributes + "]";
	}
}
