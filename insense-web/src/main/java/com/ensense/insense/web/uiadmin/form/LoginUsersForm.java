package com.ensense.insense.web.uiadmin.form;

import com.cts.mint.uitesting.model.UiTestingSetupForm;

public class LoginUsersForm extends UiTestingSetupForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String application;
	private String environment;
	private String elememntsArray;
	private String testLoginId;

	/**
	 * @return the elememntsArray
	 */
	public String getElememntsArray() {
		return elememntsArray;
	}

	/**
	 * @param elememntsArray
	 *            the elememntsArray to set
	 */
	public void setElememntsArray(String elememntsArray) {
		this.elememntsArray = elememntsArray;
	}

	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the testLoginId
	 */
	public String getTestLoginId() {
		return testLoginId;
	}

	/**
	 * @param testLoginId
	 *            the testLoginId to set
	 */
	public void setTestLoginId(String testLoginId) {
		this.testLoginId = testLoginId;
	}

}
