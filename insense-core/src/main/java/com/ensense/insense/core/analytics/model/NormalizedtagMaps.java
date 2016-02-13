package com.ensense.insense.core.analytics.model;

import java.util.List;
import java.util.Map;

public class NormalizedtagMaps {

	
	protected Map<String, List<TagVarNameValueDetailed>> baseLineMap;

	protected Map<String, List<TagVarNameValueDetailed>> testDataMap;
	
	protected Map<String, List<TagVarNameValueDetailed>> TestResultMap;

	public Map<String, List<TagVarNameValueDetailed>> getBaseLineMap() {
		return baseLineMap;
	}

	public void setBaseLineMap(
			Map<String, List<TagVarNameValueDetailed>> baseLineMap) {
		this.baseLineMap = baseLineMap;
	}

	public Map<String, List<TagVarNameValueDetailed>> getTestDataMap() {
		return testDataMap;
	}

	public void setTestDataMap(
			Map<String, List<TagVarNameValueDetailed>> testDataMap) {
		this.testDataMap = testDataMap;
	}

	public Map<String, List<TagVarNameValueDetailed>> getTestResultMap() {
		return TestResultMap;
	}

	public void setTestResultMap(
			Map<String, List<TagVarNameValueDetailed>> testResultMap) {
		TestResultMap = testResultMap;
	} 
	
	
}
