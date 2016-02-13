package com.ensense.insense.data.uitesting.dao.impl;

import com.ensense.insense.data.uitesting.dao.ApplicationConfigDAO;
import com.ensense.insense.data.uitesting.entity.ApplicationConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationConfigDAOImpl implements ApplicationConfigDAO {

	private static Logger logger = Logger
			.getLogger(ApplicationConfigDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * save Application Config details in DB
	 */
	public boolean saveApplicationConfig(ApplicationConfig applicationConfig) {
		boolean result = false;
		logger.debug("Entry: saveApplicationConfig");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(applicationConfig);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveApplicationConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: saveApplicationConfig");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApplicationConfig getApplicationConfig(int applicationId,
			int environmentId) {
		logger.debug("Entry: getApplicationConfig");
		List<ApplicationConfig> applicationConfigList = new ArrayList<ApplicationConfig>();
		ApplicationConfig applicationConfig = new ApplicationConfig();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ApplicationConfig where applicationId = :applicationId and environmentId = :environemntId");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environemntId", environmentId);
			applicationConfigList = query.list();

			if (applicationConfigList != null
					&& applicationConfigList.size() > 0) {
				applicationConfig = applicationConfigList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getApplicationConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}

		logger.debug("Exit: getApplicationConfig");
		return applicationConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationConfig> getApplicationConfigList() {
		Query query = null;
		List<ApplicationConfig> applicationConfigList = new ArrayList<ApplicationConfig>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from ApplicationConfig order by configId");
			applicationConfigList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getApplicationConfigList :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getApplicationConfig");
		return applicationConfigList;
	}

	@Override
	public boolean deleteAppConfiguration(ApplicationConfig applicationConfig) {
		logger.debug("Entry: deleteAppConfiguration");
		boolean isDeleted = false;
		try {
			sessionFactory.getCurrentSession().delete(applicationConfig);
			isDeleted = true;
		} catch (Exception e) {
			logger.error("Exception in deleteAppConfiguration :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: deleteAppConfiguration");
		return isDeleted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApplicationConfig getApplicationConfig(int configId) {
		logger.debug("Entry: getApplicationConfig");
		List<ApplicationConfig> applicationConfigList = new ArrayList<ApplicationConfig>();
		ApplicationConfig applicationConfig = new ApplicationConfig();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ApplicationConfig where configId = :configId");
			query.setParameter("configId", configId);
			applicationConfigList = query.list();

			if (applicationConfigList != null
					&& applicationConfigList.size() > 0) {
				applicationConfig = applicationConfigList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getApplicationConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}

		logger.debug("Exit: getApplicationConfig");
		return applicationConfig;
	}
}
