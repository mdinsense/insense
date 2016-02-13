package com.ensense.insense.data.uitesting.dao.impl;

import com.ensense.insense.data.uitesting.dao.ExcludeURLDAO;
import com.ensense.insense.data.uitesting.entity.AnalyticsExcludeLink;
import com.ensense.insense.data.uitesting.entity.ExcludeLinkType;
import com.ensense.insense.data.uitesting.entity.ExcludeUrl;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcludeURLDAOImpl implements ExcludeURLDAO {
	private static Logger logger = Logger.getLogger(ExcludeURLDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	/*
	 * Getting ExcludeURL table data
	 */
	public List<ExcludeUrl> getExcludeURLList() {
		logger.info("Entry: getExcludeURLList");
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM ExcludeUrl");
			
		} catch (Exception exp) {
			logger.error(exp);
		}
		logger.info("Exit: getExcludeURLList");
		return query.list();
	}

	/*
	 * Adding new ExcludeURL details into table (non-Javadoc)
	 * 
	 * @see
	 * com.cts.mint.dao.ExcludeURLDAO#addExcludeURL(com.cts.mint.reports.entity.ExcludeURL
	 * )
	 */
	@Override
	public boolean addExcludeURL(ExcludeUrl excludeUrl) {
		logger.info("Entry: addExcludeURL");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(excludeUrl);
		} catch (Exception exp) {
			result = false;
			logger.error(exp);
		}
		logger.info("Exit: addExcludeURL");
		return result;
	}

	@Override
	public ExcludeUrl getExcludeUrl(ExcludeUrl excludeUrl) {
		logger.info("Entry: getExcludeURL");
		Query query = null;
		ExcludeUrl exclude = null;
		try {

			if (excludeUrl.getExcludeUrl() != null) {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from ExcludeUrl where environmentId = :environmentId and applicationId = :applicationId and excludeUrl = :excludeUrl");
				query.setParameter("excludeUrl", excludeUrl.getExcludeUrl());
				query.setParameter("environmentId",
						excludeUrl.getApplicationId());
				query.setParameter("applicationId",
						excludeUrl.getEnvironmentId());
			} else {
				query = sessionFactory.getCurrentSession().createQuery(
						"from ExcludeUrl where excludeUrl =:excludeUrl");
				query.setParameter("excludeUrl", excludeUrl.getExcludeUrl());
			}

			if (query.list() != null && query.list().size() > 0
					&& query.list().get(0) != null) {
				exclude = (ExcludeUrl) query.list().get(0);
			}
		} catch (Exception exp) {
			logger.error(exp);

		}
		logger.info("Exit: getExcludeUrl");
		return exclude;
	}

	/*
	 * Deleting ExcludeURL details from DB
	 */
	@Override
	public boolean deleteExcludeURLDetails(ExcludeUrl excludeURL) {
		boolean result = false;
		logger.info("Entry: deleteExcludeURLDetails");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("delete from ExcludeUrl where excludeUrlId = :excludeUrlId");
			query.setParameter("excludeUrlId", excludeURL.getExcludeUrlId());
			int status = query.executeUpdate();
			if ( status == 1){
				result = true;
			}
		} catch (Exception exp) {
			result = false;
			logger.error(exp);
		}
		logger.info("Exit: deleteExcludeURLDetails");
		return result;
	}

	@Override
	public ExcludeUrl getExcludeURLBO(ExcludeUrl excludeUrl) {
		logger.info("Entry: getExcludeURLBO");
		Query query = null;
		ExcludeUrl exclude = null;
		try {

			if (excludeUrl.getExcludeUrl() != null) {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from ExcludeUrl where excludeUrlId = :excludeUrlId");
				query.setParameter("excludeUrlId", excludeUrl.getExcludeUrlId());
				query.setParameter("environmentId",
						excludeUrl.getApplicationId());
				query.setParameter("applicationId",
						excludeUrl.getEnvironmentId());
			} else {
				query = sessionFactory.getCurrentSession().createQuery(
						"from ExcludeUrl where excludeUrlId =:excludeId");
				query.setParameter("excludeId", excludeUrl.getExcludeUrlId());
			}

			if (query.list() != null && query.list().size() > 0
					&& query.list().get(0) != null) {
				exclude = (ExcludeUrl) query.list().get(0);
			}
		} catch (Exception exp) {
			logger.error(exp);

		}
		logger.info("Exit: getExcludeURLBO");
		return exclude;
	}

	@Override
	public List<ExcludeUrl> getExcludeURLList(Integer applicationId,
			Integer environmentId) {
		logger.info("Entry: getExcludeURLList");
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM ExcludeUrl where applicationId = :applicationId and environmentId = :environmentId");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);
		} catch (Exception exp) {
			logger.error("Exception in getExcludeURLList");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: getExcludeURLList");
		return query.list();
	}
	
	@Override
	public List<ExcludeLinkType> getExcludeLinkType(){
		logger.info("Entry: getExcludeLinkType");
		List<ExcludeLinkType> excludeLinkTypeList = new ArrayList<ExcludeLinkType>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM ExcludeLinkType");
			excludeLinkTypeList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getExcludeLinkType");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: getExcludeLinkType");
		return excludeLinkTypeList;
	}
	
	@Override
	public boolean addExcludeLink(AnalyticsExcludeLink analyticsExcludeLink){
		logger.info("Entry: addExcludeLink");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(analyticsExcludeLink);
		} catch (Exception e) {
			result = false;
			logger.error("Exception in addExcludeLink");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: addExcludeLink");
		return result;
	}
	
	@Override
	public AnalyticsExcludeLink getAnalyticsExcludeSetupModule(AnalyticsExcludeLink analyticsExcludeLink){
		logger.info("Entry: getAnalyticsExcludeSetupModule");
		Query query = null;
		AnalyticsExcludeLink analyticsExclude = null;
		try {

			
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from AnalyticsExcludeLink where analyticsExcludeLinkId =:analyticsExcludeLinkId");
				query.setParameter("analyticsExcludeLinkId", analyticsExcludeLink.getAnalyticsExcludeLinkId());
			
			if (query.list() != null && query.list().size() > 0
					&& query.list().get(0) != null) {
				analyticsExclude = (AnalyticsExcludeLink) query.list().get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getAnalyticsExcludeSetupModule");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));

		}
		logger.info("Exit: getAnalyticsExcludeSetupModule");
		return analyticsExclude;
	}
	
	@Override
	public boolean deleteAnalyticsExcludeSetup(AnalyticsExcludeLink analyticsExcludeLink){
		boolean result = false;
		logger.info("Entry: deleteAnalyticsExcludeSetup");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("delete from AnalyticsExcludeLink where analyticsExcludeLinkId =:analyticsExcludeLinkId");
			query.setParameter("analyticsExcludeLinkId", analyticsExcludeLink.getAnalyticsExcludeLinkId());
			int status = query.executeUpdate();
			if ( status == 1){
				result = true;
			}
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteAnalyticsExcludeSetup");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: deleteAnalyticsExcludeSetup");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalyticsExcludeLink> getAnalyticsExcludeLinksetup(){
		logger.info("Entry: getAnalyticsExcludeLinksetup");
		List<AnalyticsExcludeLink> analyticsExcludeLinkList = new ArrayList<AnalyticsExcludeLink>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM AnalyticsExcludeLink");
			analyticsExcludeLinkList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getAnalyticsExcludeLinksetup");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: getAnalyticsExcludeLinksetup");
		return analyticsExcludeLinkList;
	}
}
