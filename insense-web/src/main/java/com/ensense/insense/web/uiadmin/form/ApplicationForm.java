package com.ensense.insense.web.uiadmin.form;


public class ApplicationForm {
	private String applicationName;
	private String applicationDescription;
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the applicationDescription
	 */
	public String getApplicationDescription() {
		return applicationDescription;
	}
	/**
	 * @param applicationDescription the applicationDescription to set
	 */
	public void setApplicationDescription(String applicationDescription) {
		this.applicationDescription = applicationDescription;
	}
	@Override
	public String toString() {
		return "ApplicationForm [applicationName=" + applicationName
				+ ", applicationDescription=" + applicationDescription
				+ "]";
	}
	
}
