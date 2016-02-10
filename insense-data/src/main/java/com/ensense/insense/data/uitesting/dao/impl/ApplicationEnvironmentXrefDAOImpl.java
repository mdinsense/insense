package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.uitesting.dao.ApplicationEnvironmentXrefDAO;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;

@Service
public class ApplicationEnvironmentXrefDAOImpl implements
		ApplicationEnvironmentXrefDAO {
	private static Logger logger = Logger
			.getLogger(ApplicationEnvironmentXrefDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(
			int applicationId, int environmentId) {
		logger.debug("Entry :getAppEnvEnvironmentCategoryXref");
		List<AppEnvEnvironmentCategoryXref> appEnvEnvironmentCategoryXrefList = new ArrayList<AppEnvEnvironmentCategoryXref>();
		AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref = new AppEnvEnvironmentCategoryXref();
		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from AppEnvEnvironmentCategoryXref where applicationId = :applicationId and environmentId = :environmentId");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);

			appEnvEnvironmentCategoryXrefList = query.list();

			if (null != appEnvEnvironmentCategoryXrefList
					&& appEnvEnvironmentCategoryXrefList.size() > 0) {
				appEnvEnvironmentCategoryXref = appEnvEnvironmentCategoryXrefList
						.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getAppEnvEnvironmentCategoryXref");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit :getAppEnvEnvironmentCategoryXref");
		return appEnvEnvironmentCategoryXref;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory() {
		logger.debug("Entry: getAllApplicationEnvironmentCategory");
		Query query = null;
		List<AppEnvEnvironmentCategoryXref> applicationEnvironmentCategoryList = new ArrayList<AppEnvEnvironmentCategoryXref>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select envxref from AppEnvEnvironmentCategoryXref envxref");
			applicationEnvironmentCategoryList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getAllApplicationEnvironmentCategory :"
					+ e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getAllApplicationEnvironmentCategory");
		return applicationEnvironmentCategoryList;
	}

}
