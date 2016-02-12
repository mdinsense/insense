package com.ensense.insense.core.uiadmin.form.schedule.mintv4;

import java.util.List;

import com.cts.mint.uitesting.entity.mintv4.TestEnvironment;
import com.cts.mint.uitesting.entity.mintv4.TestLoginDetail;
import com.cts.mint.uitesting.entity.mintv4.TestSuit;
import com.cts.mint.uitesting.entity.mintv4.TestSuitBrowserConfig;
import com.cts.mint.uitesting.entity.mintv4.TestSuitCompareConfigXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitIncludeExcludeXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitRemoveTagOrTextXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitTextImageXref;

public class TestSuitDetails {
	
	private TestSuit testSuit;
	private TestEnvironment testEnvironment;
	private TestLoginDetail testLoginDetail;
	private TestSuitBrowserConfig testSuitBrowserConfig;
	private TestSuitCompareConfigXref testSuitCompareConfigXref;
	private List<TestSuitIncludeExcludeXref> testSuitIncludeExcludeXrefList;
	private List<TestSuitRemoveTagOrTextXref> testSuitRemoveTagOrTextXrefList;
	private List<TestSuitTextImageXref> testSuitTextImageXrefList;
	private String suitType;
	
	public TestSuitCompareConfigXref getTestSuitCompareConfigXref() {
		return testSuitCompareConfigXref;
	}
	public void setTestSuitCompareConfigXref(
			TestSuitCompareConfigXref testSuitCompareConfigXref) {
		this.testSuitCompareConfigXref = testSuitCompareConfigXref;
	}
	public String getSuitType() {
		return suitType;
	}
	public void setSuitType(String suitType) {
		this.suitType = suitType;
	}
	public TestSuit getTestSuit() {
		return testSuit;
	}
	public void setTestSuit(TestSuit testSuit) {
		this.testSuit = testSuit;
	}
	public TestEnvironment getTestEnvironment() {
		return testEnvironment;
	}
	public void setTestEnvironment(TestEnvironment testEnvironment) {
		this.testEnvironment = testEnvironment;
	}
	public TestLoginDetail getTestLoginDetail() {
		return testLoginDetail;
	}
	public void setTestLoginDetail(TestLoginDetail testLoginDetail) {
		this.testLoginDetail = testLoginDetail;
	}
	public TestSuitBrowserConfig getTestSuitBrowserConfig() {
		return testSuitBrowserConfig;
	}
	public void setTestSuitBrowserConfig(TestSuitBrowserConfig testSuitBrowserConfig) {
		this.testSuitBrowserConfig = testSuitBrowserConfig;
	}
	public List<TestSuitIncludeExcludeXref> getTestSuitIncludeExcludeXrefList() {
		return testSuitIncludeExcludeXrefList;
	}
	public void setTestSuitIncludeExcludeXrefList(
			List<TestSuitIncludeExcludeXref> testSuitIncludeExcludeXrefList) {
		this.testSuitIncludeExcludeXrefList = testSuitIncludeExcludeXrefList;
	}
	public List<TestSuitRemoveTagOrTextXref> getTestSuitRemoveTagOrTextXrefList() {
		return testSuitRemoveTagOrTextXrefList;
	}
	public void setTestSuitRemoveTagOrTextXrefList(
			List<TestSuitRemoveTagOrTextXref> testSuitRemoveTagOrTextXrefList) {
		this.testSuitRemoveTagOrTextXrefList = testSuitRemoveTagOrTextXrefList;
	}
	public List<TestSuitTextImageXref> getTestSuitTextImageXrefList() {
		return testSuitTextImageXrefList;
	}
	public void setTestSuitTextImageXrefList(
			List<TestSuitTextImageXref> testSuitTextImageXrefList) {
		this.testSuitTextImageXrefList = testSuitTextImageXrefList;
	}

}
