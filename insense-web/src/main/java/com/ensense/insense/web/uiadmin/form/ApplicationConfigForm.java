package com.ensense.insense.web.uiadmin.form;



public class ApplicationConfigForm  {

	private String configId;
	private String browserTimeout;
	private String noOfBrowsers;
	private String browserRestart;
	private String isPageLoad;

	/**
	 * @return the isPageLoad
	 */
	public String getIsPageLoad() {
		return isPageLoad;
	}

	/**
	 * @param isPageLoad the isPageLoad to set
	 */
	public void setIsPageLoad(String isPageLoad) {
		this.isPageLoad = isPageLoad;
	}

	/**
	 * @return the configId
	 */
	public String getConfigId() {
		return configId;
	}

	/**
	 * @param configId
	 *            the configId to set
	 */
	public void setConfigId(String configId) {
		this.configId = configId;
	}

	/**
	 * @return the browserTimeout
	 */
	public String getBrowserTimeout() {
		return browserTimeout;
	}

	/**
	 * @param browserTimeout
	 *            the browserTimeout to set
	 */
	public void setBrowserTimeout(String browserTimeout) {
		this.browserTimeout = browserTimeout;
	}

	/**
	 * @return the noOfBrowsers
	 */
	public String getNoOfBrowsers() {
		return noOfBrowsers;
	}

	/**
	 * @param noOfBrowsers
	 *            the noOfBrowsers to set
	 */
	public void setNoOfBrowsers(String noOfBrowsers) {
		this.noOfBrowsers = noOfBrowsers;
	}

	/**
	 * @return the browserRestart
	 */
	public String getBrowserRestart() {
		return browserRestart;
	}

	/**
	 * @param browserRestart
	 *            the browserRestart to set
	 */
	public void setBrowserRestart(String browserRestart) {
		this.browserRestart = browserRestart;
	}

}
