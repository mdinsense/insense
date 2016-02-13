package com.ensense.insense.data.uitesting.dao.impl;


import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.data.uitesting.dao.ApplicationModuleDAO;
import com.ensense.insense.data.uitesting.entity.ApplicationModuleXref;
import com.ensense.insense.data.uitesting.entity.ModuleType;
import com.ensense.insense.data.uitesting.entity.TransactionTestCase;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationModuleDAOImpl implements ApplicationModuleDAO {
	private static Logger logger = Logger
			.getLogger(ApplicationModuleDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationModuleXref> getApplicationModuleList() {
		logger.info("Entry: getApplicationModuleList");
		Criteria criteria = null;
		List<ApplicationModuleXref> applicationModuleList = new ArrayList<ApplicationModuleXref>();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(
					ApplicationModuleXref.class);
			applicationModuleList = criteria.setResultTransformer(
					Criteria.DISTINCT_ROOT_ENTITY).list();

		} catch (Exception exp) {
			logger.error(exp);
		}

		logger.info("Exit: getApplicationModuleList");
		return applicationModuleList;
	}

	@Override
	public boolean addApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: addApplicationModule, applicationModuleXref->"
				+ applicationModuleXref);
		boolean result = true;

		try {
			sessionFactory.getCurrentSession().save(applicationModuleXref);
		} catch (Exception exp) {
			logger.error(exp);
			result = false;
		}
		logger.info("Exit: addApplicationModule");
		return result;
	}

	@Override
	public ApplicationModuleXref getApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		Query query = null;
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ApplicationModuleXref where applicationModuleXrefId =:applicationModuleXrefId");
			query.setParameter("applicationModuleXrefId",
					applicationModuleXref.getApplicationModuleXrefId());
			@SuppressWarnings("unchecked")
			List<ApplicationModuleXref> applicationModuleList = query.list();
			if (applicationModuleList != null
					&& applicationModuleList.size() > 0
					&& applicationModuleList.get(0) != null) {
				applicationModuleXref = (ApplicationModuleXref) query.list()
						.get(0);
			}
		} catch (Exception exp) {
			logger.error(exp);

		}
		logger.info("Exit: getIncludeURLBO");
		return applicationModuleXref;
	}

	@Override
	public boolean updateApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		logger.debug("Entry: updateApplicationModule");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(
					applicationModuleXref);
		} catch (Exception e) {
			result = false;
			logger.error("Exception in updateApplicationModule.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: updateApplicationModule");
		return result;
	}

	@Override
	public boolean deleteApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: deleteApplicationModule");
		boolean result = false;
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Delete from TransactionTestCase where applicationModuleXrefId=:applicationModuleXrefId");
			query.setParameter("applicationModuleXrefId", applicationModuleXref.getApplicationModuleXrefId());
			query.executeUpdate();
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Delete from ApplicationModuleXref where applicationModuleXrefId=:applicationModuleXrefId");
			query.setParameter("applicationModuleXrefId", applicationModuleXref.getApplicationModuleXrefId());
			query.executeUpdate();
			result = true;
		} catch (Exception exp) {
			logger.error(exp);
			result = false;
		}
		logger.info("Exit: deleteApplicationModule");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ModuleType> getModuleTypeList() {
		logger.debug("Entry: getModuleTypeList");
		Query query = null;
		List<ModuleType> moduleTypeList = new ArrayList<ModuleType>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from ModuleType order by moduleTypeId");
			moduleTypeList = query.list();

		} catch (Exception exp) {
			logger.error("Exception in getModuleTypeList");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}

		logger.debug("Exit: getModuleTypeList");
		return moduleTypeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationModuleXref> getModuleListForApplicationAndCategory(
			int applicationId, int categoryId) {
		logger.debug("Entry: getModuleListForApplicationAndCategory");
		Query query = null;
		List<ApplicationModuleXref> applicationModuleList = new ArrayList<ApplicationModuleXref>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select amx From ApplicationModuleXref amx, AppEnvEnvironmentCategoryXref ecx where amx.applicationId = :applicationId and amx.environmentId = ecx.environmentId and "
									+ "amx.applicationId = ecx.applicationId and ( ecx.environmentCategoryId =:environmentCategoryId OR ecx.environmentCategoryId =:environmentCategoryIdALL ) "
									+ "order by ecx.environmentCategoryId");

			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentCategoryId", categoryId);
			query.setParameter("environmentCategoryIdALL",
					Constants.EnvironmentCategoryENUM.ALL.getEnvironmentCategoryId());
			applicationModuleList = query.list();
		} catch (Exception exp) {
			logger.error("Exception in getModuleTypeList");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}

		logger.debug("Exit: getModuleListForApplicationAndCategory");
		return applicationModuleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationModuleXref> getModulesForSuit(String modules) {
		logger.debug("Entry: getModulesForSuit");
		Query query = null;
		List<ApplicationModuleXref> applicationModuleList = new ArrayList<ApplicationModuleXref>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery("select apx from ApplicationModuleXref apx where apx.applicationModuleXrefId in ("+modules+")");
			applicationModuleList = query.list();
		} catch (Exception exp) {
			logger.error("Exception in getModulesForSuit");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}

		logger.debug("Exit: getModulesForSuit");
		return applicationModuleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean confirmModuleIdForALL(
			ApplicationModuleXref applicationModuleXref) {
		logger.debug("Entry: confirmModuleIdForALL");
		Query query = null;
		boolean isModuleIdExists = false;
		List<ApplicationModuleXref> applicationModuleList = new ArrayList<ApplicationModuleXref>();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select amx from ApplicationModuleXref amx, AppEnvEnvironmentCategoryXref ecx "
									+ " where amx.environmentId = ecx.environmentId "
									+ " and amx.applicationId = ecx.applicationId "
									+ " and amx.applicationId =:applicationId "
									+ " and amx.environmentId =:environmentId "
									+ " and amx.applicationModuleXrefId =:applicationModuleXrefId "
									+ " and ecx.environmentCategoryId =:environmentCategoryId");

			query.setParameter("applicationId",
					applicationModuleXref.getApplicationId());
			query.setParameter("environmentId",
					applicationModuleXref.getEnvironmentId());
			query.setParameter("applicationModuleXrefId",
					applicationModuleXref.getApplicationModuleXrefId());
			query.setParameter("environmentCategoryId",
					Constants.EnvironmentCategoryENUM.ALL.getEnvironmentCategoryId());

			applicationModuleList = query.list();
			if (applicationModuleList != null
					&& applicationModuleList.size() > 0) {
				isModuleIdExists = true;
			}
		} catch (Exception e) {
			logger.error("Exception in confirmModuleIdForALL");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));

		}
		logger.info("Exit: confirmModuleIdForALL");
		return isModuleIdExists;
	}

	@Override
	public boolean saveTransactionTestCase(TransactionTestCase testCase) {
		logger.info("Entry: saveTransactionTestCase, testCase->" + testCase);
		boolean result = true;

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCase);
			sessionFactory.getCurrentSession().flush();
		} catch (Exception exp) {
			logger.error(exp);
			result = false;
		}
		logger.info("Exit: saveTransactionTestCase");
		return result;
	}
	

	@Override
	public TransactionTestCase getTransactionTestCase(int testCaseId) {
		logger.info("Entry: getTransactionTestCase, testCase->" + testCaseId);
		boolean result = true;
        Query query = null;
        List<TransactionTestCase> testCaseList = null;
        TransactionTestCase testCase = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from TransactionTestCase where testCaseId=:testCaseId");
			query.setParameter("testCaseId", testCaseId);
			testCaseList = query.list();
			if(testCaseList != null && testCaseList.size() > 0) {
				testCase = testCaseList.get(0);
			}
		} catch (Exception exp) {
			logger.error(exp);
		}
		logger.info("Exit: getTransactionTestCase");
		return testCase;
	}

	@Override
	public boolean deleteTestCaseByTestCaseId(int testCaseId) {
		logger.info("Entry: deleteTestCaseByTestCaseId");
		boolean result = false;
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Delete from TransactionTestCase where testCaseId=:testCaseId");
			query.setParameter("testCaseId", testCaseId);
			query.executeUpdate();
			result = true;
		} catch (Exception exp) {
			logger.error(exp);
			result = false;
		}
		logger.info("Exit: deleteTestCaseByTestCaseId");
		return result;
	}
}
