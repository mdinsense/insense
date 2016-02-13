package com.ensense.insense.services.reports.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.dao.TestResultsDAO;
import com.cts.mint.reports.service.TestResultsService;
import com.cts.mint.uitesting.entity.Results;

@Service
public class TestResultsServiceImpl implements TestResultsService {

	@Autowired 
	TestResultsDAO testResultsDao;
	
	@Override
	@Transactional
	public boolean insertTestResults(Results testResult) {
		return testResultsDao.insertTestResults(testResult);
	}

	@Override
	@Transactional
	public Results retrieveTestResult(int reportdId) {
		return testResultsDao.retrieveTestResult(reportdId);
	}

	@Override
	public Results retrieveBaselineTestResult(int testResultId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Results retrieveSelectedTestResultIdDetails(Results testResult) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
