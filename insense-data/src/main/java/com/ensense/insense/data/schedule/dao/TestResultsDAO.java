package com.ensense.insense.data.schedule.dao;

import com.cts.mint.uitesting.entity.Results;

public interface TestResultsDAO {

	public boolean insertTestResults(Results testResult);
	public Results retrieveTestResult(int testResultId);
	public Results retrieveBaselineTestResult(int testResultId);
	public Results retrieveSelectedTestResultIdDetails(Results testResult);
	
}
