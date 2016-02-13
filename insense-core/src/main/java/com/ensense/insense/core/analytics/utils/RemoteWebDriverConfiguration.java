package com.ensense.insense.core.analytics.utils;

public class RemoteWebDriverConfiguration {

	private String operatingSystem;
	private String browserType;
	private String browserVersion;
	private String sauceLabUserId;
	private String sauceLabAccessKey;
	private String sauceLabRemoteUrl;
	
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getSauceLabUserId() {
		return sauceLabUserId;
	}
	public void setSauceLabUserId(String sauceLabUserId) {
		this.sauceLabUserId = sauceLabUserId;
	}
	public String getSauceLabAccessKey() {
		return sauceLabAccessKey;
	}
	public void setSauceLabAccessKey(String sauceLabAccessKey) {
		this.sauceLabAccessKey = sauceLabAccessKey;
	}
	public String getSauceLabRemoteUrl() {
		return sauceLabRemoteUrl;
	}
	public void setSauceLabRemoteUrl(String sauceLabRemoteUrl) {
		this.sauceLabRemoteUrl = sauceLabRemoteUrl;
	}
	
	@Override
	public String toString() {
		return "Configuration [operatingSystem=" + operatingSystem
				+ ", browserType=" + browserType + ", browserVersion="
				+ browserVersion + ", sauceLabUserId=" + sauceLabUserId
				+ ", sauceLabAccessKey=" + sauceLabAccessKey
				+ ", sauceLabRemoteUrl=" + sauceLabRemoteUrl + "]";
	}

}
