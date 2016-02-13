package com.ensense.insense.data.schedule.dao.impl;

import com.ensense.insense.data.schedule.dao.TestResultsDAO;
import com.ensense.insense.data.uitesting.entity.Results;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TestResultsDAOImpl implements TestResultsDAO {
	private static Logger logger = Logger.getLogger(TestResultsDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean insertTestResults(Results testResult) {
		logger.debug("Entry: insertTestResults");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().save(testResult);
		} catch (Exception exp) {
			result = false;
			logger.error(exp);
		}
		logger.debug("Exit: insertTestResults");
		return result;
	}

	@Override
	public Results retrieveTestResult(int reportdId) {
		logger.debug("Entry: retrieveTestResult");
		List<Results> resultsList = new ArrayList<Results>();
		Results testResult = new Results();
		
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from Results where testResultId in (select testResultId from Reports where testReportsId = :reportsId)");
			query.setParameter("reportsId", reportdId);

			resultsList = query.list();

			if ( resultsList.size() > 0 ){
				testResult = resultsList.get(0);
			}

		} catch (Exception exp) {
			logger.error(exp);
		}
		logger.debug("Exit: retrieveTestResult");
		return testResult;
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
