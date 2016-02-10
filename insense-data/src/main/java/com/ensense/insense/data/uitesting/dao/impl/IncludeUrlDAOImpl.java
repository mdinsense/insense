package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.uitesting.dao.IncludeUrlDAO;
import com.cts.mint.uitesting.entity.IncludeUrl;

@Service
public class IncludeUrlDAOImpl implements IncludeUrlDAO {
	private static Logger logger = Logger.getLogger(IncludeUrlDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	/*
	 * Getting IncludeURL table data
	 */
	public List<IncludeUrl> getIncludeURLList() {
		logger.debug("Entry: getIncludeURLList");
		Query query = null;
		List<IncludeUrl> includeUrlList = new ArrayList<IncludeUrl>();
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM IncludeUrl");
			includeUrlList = query.list();
			
			logger.info("includeUrlList :"+includeUrlList);
		} catch (Exception exp) {
			logger.error(exp);
		}

		logger.debug("Exit: getIncludeURLList");
		return includeUrlList;

	}

	@SuppressWarnings("unchecked")
	/*
	 * Getting IncludeURL table data
	 */
	@Override
	public List<IncludeUrl> getIncludeURLList(int applicationId, int environmentId) {
		logger.debug("Entry: getIncludeURLList");
		Query query = null;
		List<IncludeUrl> includeUrlList = new ArrayList<IncludeUrl>();
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM IncludeUrl where applicationId = :applicationId and environmentId = :environmentId");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);
			includeUrlList = query.list();
			
			logger.info("includeUrlList :"+includeUrlList);
		} catch (Exception exp) {
			logger.error(exp);
		}

		logger.debug("Exit: getIncludeURLList");
		return includeUrlList;

	}
	/*
	 * Adding new IncludeURL details into table (non-Javadoc)
	 * 
	 * @see
	 * com.cts.mint.dao.IncludeURLDAO#addIncludeURL(com.cts.mint.reports.entity.
	 * IncludeURL)
	 */
	@Override
	public boolean addIncludeURL(IncludeUrl includeURL) {
		logger.debug("Entry: addIncludeURL");
		boolean result = true;

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(includeURL);
		} catch (Exception exp) {
			logger.error(exp);
			result = false;
		}
		logger.debug("Exit: addIncludeURL");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IncludeUrl getIncludeURL(IncludeUrl includeURL) {
		logger.debug("Entry: getIncludeURL, includeURL->"+includeURL);
		Query query = null;
		IncludeUrl include = new IncludeUrl();
		List<IncludeUrl> includeUrlList = new ArrayList<IncludeUrl>();
		try {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from IncludeUrl where includeUrlId = :includeUrlId");
				query.setParameter("includeUrlId", includeURL.getIncludeUrlId());

			includeUrlList = query.list();
logger.info("includeUrlList :"+includeUrlList);

			if (includeUrlList != null && includeUrlList.size() > 0 ) {
				include = (IncludeUrl) includeUrlList.get(0);
			}
		} catch (Exception exp) {
			logger.error("Exception in getIncludeURL");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));

		}
		logger.debug("Exit: getIncludeURL");
		return include;
	}

	/*
	 * Deleting IncludeURL details from DB
	 */
	@Override
	public boolean deleteIncludeURLDetails(IncludeUrl includeURL) {
		boolean result = false;
		logger.info("Entry: deleteIncludeURLDetails");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("delete from IncludeUrl where includeUrlId = :includeUrlId");
			query.setParameter("includeUrlId", includeURL.getIncludeUrlId());
			int status = query.executeUpdate();
			if ( status == 1){
				result = true;
			}
		} catch (Exception exp) {
			logger.error("Exception in deleteIncludeURLDetails");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
			result = false;
		}
		logger.info("Exit: deleteIncludeURLDetails");
		return result;
	}

}
