package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.uitesting.dao.ApplicationDAO;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Schedule;

@Service
public class ApplicationDAOImpl implements ApplicationDAO {

	private static Logger logger = Logger.getLogger(ApplicationDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * save application details to application table
	 */
	@Override
	public boolean addApplication(Application application) {

		boolean result = false;
		logger.debug("Entry: addApplication");

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(application);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in addApplication.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: addApplication");
		return result;
	}

	/**
	 * get all application records from application table
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Application> getAllApplicationDetails() {

		logger.debug("Entry: getAllApplicationDetails");
		List<Application> applicationList = new ArrayList<Application>();
		Query query = null;

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Application order by applicationId");

			applicationList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getAllApplicationDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getAllApplicationDetails");
		return applicationList;

	}

	/**
	 * get the application record based on application id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Application getApplication(int applicationId) {
		logger.debug("Entry: getApplication");
		Application applicationReturn = new Application();
		List<Application> applicationList = new ArrayList<Application>();

		String sqlQuery = "";
		try {

			sqlQuery = "from Application where applicationId = :applicationId";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("applicationId", applicationId);
			applicationList = query.list();
			if (null != applicationList && applicationList.size() > 0) {
				applicationReturn = applicationList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getApplication :" + e);
		}

		logger.debug("Exit: getApplication");
		return applicationReturn;
	}

	/**
	 * checks the application record based on application name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isApplicationNameExists(String applicationName) {
		logger.debug("Entry: isApplicationNameExists");

		boolean isApplicationNameExists = false;
		List<Application> applicationList = new ArrayList<Application>();
		String sqlQuery = "";

		try {
			sqlQuery = "from Application where applicationName = :applicationName";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("applicationName", applicationName);
			applicationList = query.list();
			if (null != applicationList && applicationList.size() > 0) {
				isApplicationNameExists = true;
			}

		} catch (Exception exception) {
			logger.error("Exception in getApplication :" + exception);
		}

		logger.debug("Exit: isApplicationNameExists");
		return isApplicationNameExists;
	}

	/**
	 * get applications that are mapped to environment
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Application> getMappedApplicationList() {

		logger.debug("Entry: getMappedApplicationList");
		Query query = null;
		List<Application> applicationList = new ArrayList<Application>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select application from Application application, AppEnvEnvironmentCategoryXref envRef"
									+ " where envRef.applicationId = application.applicationId");

			applicationList = query.list();

		} catch (Exception exception) {
			logger.error("Exception in getMappedApplicationList :" + exception);
		}

		logger.debug("Exit: getMappedApplicationList");
		return applicationList;
	}


	/**
	 * delete application and the configuration for the application
	 */
	@Override
	public boolean deleteApplication(Application application) {

		logger.debug("Entry: deleteApplication");
		boolean isDeleted = false;
		int applicationId = application.getApplicationId();
		Query query = null;

		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ExcludeUrl where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from IncludeUrl where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from LoginUiElement where loginUserDetailId in ("
									+ "select loginUserDetailId from LoginUserDetails where applicationId =:applicationId)");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from LoginUserDetails where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ApplicationConfig where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from AppEnvEnvironmentCategoryXref where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ApplicationGroupReference where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from Application where applicationId =:applicationId");
			query.setParameter("applicationId", applicationId);
			query.executeUpdate();

			isDeleted = true;

		} catch (Exception exception) {
			logger.error("Exception in deleteApplication :" + exception);
		}

		logger.debug("Exit: deleteApplication");
		return isDeleted;
	}

	/**
	 * get list of application mapped to the given group id
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ApplicationGroupReference> getApplicationListForGroup(
			int groupId) {

		/*List<ApplicationGroupReference> applicationGroupReferenceTableList = new ArrayList<ApplicationGroupReference>();
		Criteria criteria = null;
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(
					ApplicationGroupReference.class);

			applicationGroupReferenceTableList = criteria.list();
		} catch (Exception e) {
			logger.error("Exception in getUserApplicationList :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return applicationGroupReferenceTableList;*/
		logger.debug("Entry: getApplicationListForGroup, groupId->"+groupId);
		List<ApplicationGroupReference> applicationGroupReferenceTableList = new ArrayList<ApplicationGroupReference>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from ApplicationGroupReference where groupId = :groupId");
			query.setParameter("groupId", groupId);
			
			applicationGroupReferenceTableList = query.list();
		}catch (Exception e) {
			logger.error("Exception in addApplication :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getApplicationListForGroup");

		return applicationGroupReferenceTableList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isApplicationEnvironmentXrefExists(int applicationId,
			int environmentId) {
		logger.debug("Entry: isApplicationEnvironmentXrefExists");
		Query query = null;
		List<AppEnvEnvironmentCategoryXref> environmentCategoryXrefList = new ArrayList<AppEnvEnvironmentCategoryXref>();
		boolean isExists = false;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from AppEnvEnvironmentCategoryXref where environmentId =:environmentId and applicationId = :applicationId");
			query.setParameter("environmentId", environmentId);
			query.setParameter("applicationId", applicationId);
			environmentCategoryXrefList = query.list();
			if (environmentCategoryXrefList != null
					&& environmentCategoryXrefList.size() > 0) {
				isExists = true;
			}

		} catch (Exception e) {
			logger.error("Exception in isEnvironmentExists :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isApplicationEnvironmentXrefExists");
		return isExists;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveApplicationGroupReference(ApplicationGroupReference applicationGroupReference) throws Exception {
		logger.debug("Exit: saveApplicationGroupReference");
		boolean saved = false;
		
			sessionFactory.getCurrentSession().save(applicationGroupReference);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			saved = true;
		
		logger.debug("Exit: saveApplicationGroupReference");
		return saved;
	}
	
	@SuppressWarnings("unchecked")
	public Application getApplicationForGroup(int applicationId) {
		logger.debug("Entry: getEnvironmentCategoryName");
		Query query = null;
		List<Application> applicationList = new ArrayList<Application>();
		Application application = new Application();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select app from Application as app, ApplicationGroupReference as env "
									+ " where env.applicationId = app.applicationId "
									+ " and app.applicationId =:applicationId ");
									
			query.setParameter("applicationId", applicationId);
			applicationList = query.list();
			if (applicationList != null
					&& applicationList.size() > 0) {
				application = applicationList.get(0);
			}
		} catch (Exception exception) {
			logger.error("Exception in getEnvironmentListForApplicationId :"
					+ exception);
		}
		logger.debug("Exit: getEnvironmentCategoryName");
		return application;
	}
	
	@Override
	public boolean deleteApplicationGroupReference(int groupId) {
		logger.debug("Entry: deleteApplicationGroupReference");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ApplicationGroupReference where groupId=:groupId");
			query.setParameter("groupId", groupId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteApplicationGroupReference :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteApplicationGroupReference");
		return result;
	}

	@Override
	public Map<Integer, Application> getApplicationMap(String applicationIdStr) {
		logger.debug("Entry: getApplicationMap");
		
		List<Application> applicationList = new ArrayList<Application>();
		Map<Integer, Application> applicationMap = new HashMap<Integer, Application>();
		String sqlQuery = "";
		try {
			sqlQuery = "from Application where applicationId in (" +applicationIdStr+")";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			//query.setParameter("applicationId", applicationId);
			applicationList = query.list();
			if (null != applicationList && applicationList.size() > 0) {
				for(Application application : applicationList) {
					applicationMap.put(application.getApplicationId(), application);
				}
			}

		} catch (Exception e) {
			logger.error("Exception in getApplication :" + e);
		}
		logger.debug("Exit: getApplicationMap");
		return applicationMap;
	}
}
