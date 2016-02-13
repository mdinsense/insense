package com.ensense.insense.services.reports;

import com.cts.mint.uitesting.entity.Results;

public interface TestResultsService {

	public boolean insertTestResults(Results testResult);
	public Results retrieveTestResult(int testResultId);
	public Results retrieveBaselineTestResult(int testResultId);
	public Results retrieveSelectedTestResultIdDetails(Results testResult);
	
}
