package com.ensense.insense.data.uitesting.dao.impl.mintv4;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.utils.Constants;
import com.cts.mint.uitesting.dao.mintv4.TestSuitDAO;
import com.cts.mint.uitesting.entity.mintv4.TestEnvironment;
import com.cts.mint.uitesting.entity.mintv4.TestLoginDetail;
import com.cts.mint.uitesting.entity.mintv4.TestSuit;
import com.cts.mint.uitesting.entity.mintv4.TestSuitBrowserConfig;
import com.cts.mint.uitesting.entity.mintv4.TestSuitCompareConfigXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitIncludeExcludeXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitRemoveTagOrTextXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitTextImageXref;
import com.cts.mint.uitesting.model.mintv4.TestSuitDetails;

@Service
public class TestSuitDAOImpl implements TestSuitDAO {

	private static Logger logger = Logger.getLogger(TestSuitDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveSuit(TestSuit TestSuit) {
		boolean result = false;
		logger.debug("Entry: saveSuit");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(TestSuit);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in addApplication.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSuit");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveEnvironment(TestEnvironment testEnvironment) {
		boolean result = false;
		logger.debug("Entry: saveEnvironment");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testEnvironment);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in addApplication.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveEnvironment");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveLoginDetails(TestLoginDetail testLoginDetail) {
		boolean result = false;
		logger.debug("Entry: saveLoginDetails");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testLoginDetail);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveLoginDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveLoginDetails");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTestSuitBrowserConfig(
			TestSuitBrowserConfig testSuitBrowserConfig) {
		boolean result = false;
		logger.debug("Entry: saveTestSuitBrowserConfig");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testSuitBrowserConfig);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTestSuitBrowserConfig.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveTestSuitBrowserConfig");
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTestSuitTextImageXref(TestSuitTextImageXref suitTextImage) {
		boolean result = false;
		logger.debug("Entry: saveTestSuitTextImageXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(suitTextImage);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTestSuitTextImageXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveTestSuitTextImageXref");
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTestSuitTagTextXref(TestSuitRemoveTagOrTextXref removeTagOrText){
		boolean result = false;
		logger.debug("Entry: saveTestSuitTagTextXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(removeTagOrText);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTestSuitTagTextXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveTestSuitTagTextXref");
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTestSuitIncludeExcludeXref(TestSuitIncludeExcludeXref includeExclude){
		boolean result = false;
		logger.debug("Entry: saveTestSuitIncludeExcludeXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(includeExclude);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTestSuitIncludeExcludeXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveTestSuitIncludeExcludeXref");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTestSuitCompareConfigXref(
			TestSuitCompareConfigXref testSuitCompareConfigXref) {
		boolean result = false;
		logger.debug("Entry: saveTestSuitCompareConfigXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testSuitCompareConfigXref);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTestSuitCompareConfigXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveTestSuitCompareConfigXref");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TestSuitDetails getTestSuitDetails(int suitId) {
		logger.debug("Entry: getTestSuitDetails");
		Query query = null;
		TestSuitDetails testSuitDetails = new TestSuitDetails();
		
        try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select a, b, c, d from "
									+ "TestSuit a, TestEnvironment b, TestLoginDetail c, TestSuitBrowserConfig d "
									+ "where a.suitId=b.suitId and a.suitId=c.suitId and a.suitId=d.suitId and a.suitId=:suitId");
			query.setParameter("suitId",suitId);
			
			List<Object[]> objList = query.list();
			
			if (null != objList && objList.size() > 0) {
				testSuitDetails.setTestSuit((TestSuit)objList.get(0)[0]);
				testSuitDetails.setTestEnvironment((TestEnvironment)objList.get(0)[1]);
				testSuitDetails.setTestLoginDetail((TestLoginDetail)objList.get(0)[2]);
				testSuitDetails.setTestSuitBrowserConfig((TestSuitBrowserConfig)objList.get(0)[3]);
			}
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from TestSuitIncludeExcludeXref where suitId=:suitId");
			query.setParameter("suitId",suitId);
			testSuitDetails.setTestSuitIncludeExcludeXrefList(query.list());
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from TestSuitRemoveTagOrTextXref where suitId=:suitId");
			query.setParameter("suitId",suitId);
			testSuitDetails.setTestSuitRemoveTagOrTextXrefList(query.list());
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from TestSuitTextImageXref where suitId=:suitId");
			query.setParameter("suitId",suitId);
			testSuitDetails.setTestSuitTextImageXrefList(query.list());
		
        } catch (Exception e) {
			logger.error("Exception in saveTestSuitCompareConfigXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
        
		logger.debug("Exit: getTestSuitDetails");
		return testSuitDetails;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<TestSuitDetails> getAllTestSuitDetails() {
		logger.debug("Entry: getAllTestSuitDetails");
		Query query = null;
		List<TestSuitDetails> testSuitDetailsList = new ArrayList<TestSuitDetails>();
		TestSuitDetails testSuitDetails = null;
		
        try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
						/*	"select a, b, c, d from "
									+ "TestSuit a, TestEnvironment b, TestLoginDetail c, TestSuitBrowserConfig d "
									+ "where a.suitId=b.suitId and a.suitId=c.suitId and a.suitId=d.suitId");*/
			
			"select a, b, d from "
			+ "TestSuit a, TestEnvironment b, TestSuitBrowserConfig d "
			+ "where a.suitId=b.suitId and a.suitId=d.suitId");
			
			List<Object[]> objList = query.list();
			
			if (null != objList && objList.size() > 0) {
				for(Object[] obj : objList) {
					testSuitDetails = new TestSuitDetails();
					testSuitDetails.setTestSuit((TestSuit)obj[0]);
					testSuitDetails.setTestEnvironment((TestEnvironment)obj[1]);
					//testSuitDetails.setTestLoginDetail((TestLoginDetail)obj[2]);
					//testSuitDetails.setTestSuitBrowserConfig((TestSuitBrowserConfig)obj[3]);
					testSuitDetails.setTestSuitBrowserConfig((TestSuitBrowserConfig)obj[2]);
					
					query = sessionFactory
							.getCurrentSession()
							.createQuery("from TestSuitIncludeExcludeXref where suitId=:suitId");
					query.setParameter("suitId",testSuitDetails.getTestSuit().getSuitId());
					testSuitDetails.setTestSuitIncludeExcludeXrefList(query.list());
					
					query = sessionFactory
							.getCurrentSession()
							.createQuery("from TestSuitRemoveTagOrTextXref where suitId=:suitId");
					query.setParameter("suitId",testSuitDetails.getTestSuit().getSuitId());
					testSuitDetails.setTestSuitRemoveTagOrTextXrefList(query.list());
					
					query = sessionFactory
							.getCurrentSession()
							.createQuery("from TestSuitTextImageXref where suitId=:suitId");
					query.setParameter("suitId",testSuitDetails.getTestSuit().getSuitId());
					testSuitDetails.setTestSuitTextImageXrefList(query.list());
					testSuitDetails.setSuitType(Constants.MINTV4);
					testSuitDetailsList.add(testSuitDetails);
				}
			}
			
			
		
        } catch (Exception e) {
			logger.error("Exception in getAllTestSuitDetails :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
        
		logger.debug("Exit: getAllTestSuitDetails");
		return testSuitDetailsList;
	}
		
	

}
