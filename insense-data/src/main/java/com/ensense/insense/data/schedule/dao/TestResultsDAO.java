package com.ensense.insense.data.schedule.dao;

import com.ensense.insense.data.uitesting.entity.Results;

public interface TestResultsDAO {

	public boolean insertTestResults(Results testResult);
	public Results retrieveTestResult(int testResultId);
	public Results retrieveBaselineTestResult(int testResultId);
	public Results retrieveSelectedTestResultIdDetails(Results testResult);
	
}
