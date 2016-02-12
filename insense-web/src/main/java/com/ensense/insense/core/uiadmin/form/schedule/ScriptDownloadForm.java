package com.ensense.insense.core.uiadmin.form.schedule;

import com.ensense.insense.data.uitesting.entity.TransactionTestCase;

public class ScriptDownloadForm {
	
	private TransactionTestCase testCase;
	private boolean testCaseAvailable;
	private boolean htmlAvailable;

	public TransactionTestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TransactionTestCase testCase) {
		this.testCase = testCase;
	}

	public boolean isTestCaseAvailable() {
		return testCaseAvailable;
	}

	public void setTestCaseAvailable(boolean testCaseAvailable) {
		this.testCaseAvailable = testCaseAvailable;
	}

	public boolean isHtmlAvailable() {
		return htmlAvailable;
	}

	public void setHtmlAvailable(boolean htmlAvailable) {
		this.htmlAvailable = htmlAvailable;
	}
}
