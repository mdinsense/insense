package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.uitesting.dao.HtmlReportConfigDAO;
import com.cts.mint.uitesting.entity.HtmlReportsConfig;

@Service
public class HtmlReportConfigDAOImpl implements HtmlReportConfigDAO{
	
	private static Logger logger = Logger
			.getLogger(HtmlReportConfigDAOImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean addHtmlReportsConfig(HtmlReportsConfig htmlReportConfig){
		logger.debug("Entry: addHtmlReportsConfig");
		boolean result = false;
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(htmlReportConfig);	
			result = true;
		}catch (Exception e) {
			logger.error("Exception in addHtmlReportsConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: addHtmlReportsConfig");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HtmlReportsConfig getHtmlReportsConfig(int htmlReportConfigId){
		logger.debug("Entry: getHtmlReportsConfig");
		Query query=null;
		List<HtmlReportsConfig> htmlReportConfigList = new ArrayList<HtmlReportsConfig>();
		HtmlReportsConfig htmlReportsConfig = new HtmlReportsConfig();
		try{
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from HtmlReportsConfig where htmlReportsConfigId=:htmlReportsConfigId");
			query.setParameter("htmlReportsConfigId", htmlReportConfigId);
			htmlReportConfigList = query.list();
			if(htmlReportConfigList != null && htmlReportConfigList.size() > 0) {
				htmlReportsConfig = htmlReportConfigList.get(0);
			}
			
		}catch (Exception e) {
			logger.error("Exception in getHtmlReportsConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getHtmlReportsConfig");
		return htmlReportsConfig;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HtmlReportsConfig> getHtmlReportsConfigForApplicationEnvironment(int applicationId,int environmentId){
		logger.debug("Entry: getHtmlReportsConfig");
		Query query=null;
		List<HtmlReportsConfig> htmlReportConfigList = new ArrayList<HtmlReportsConfig>();
		try{
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from HtmlReportsConfig where applicationId=:applicationId and environmentId=:environmentId");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);
			htmlReportConfigList = query.list();
			
		}catch (Exception e) {
			logger.error("Exception in getHtmlReportsConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getHtmlReportsConfig");
		return htmlReportConfigList;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HtmlReportsConfig> getAllHtmlReportsConfig(){
		logger.debug("Entry: getAllHtmlReportsConfig");
		Query query=null;
		List<HtmlReportsConfig> htmlReportConfigList = new ArrayList<HtmlReportsConfig>();
		try{
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from HtmlReportsConfig order by htmlReportsConfigId");
			htmlReportConfigList = query.list();
			
		}catch (Exception e) {
			logger.error("Exception in getHtmlReportsConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getAllHtmlReportsConfig");
		return htmlReportConfigList;	
	}
	
	@Override
	public boolean deleteHtmlReportsConfig(HtmlReportsConfig htmlReportConfig) {
		logger.debug("Entry: deleteHtmlReportsConfig");
		Query query=null;
		boolean isDeleted = false;
		try {
			query =  sessionFactory
					.getCurrentSession()
					.createQuery("delete from HtmlReportsConfig where htmlreportsconfigid =:htmlreportsconfigid");
			query.setParameter("htmlreportsconfigid", htmlReportConfig.getHtmlReportsConfigId());
			query.executeUpdate();
			isDeleted = true;
		} catch (Exception e) {
			logger.error("Exception in deleteHtmlReportsConfig :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: deleteHtmlReportsConfig");
		return isDeleted;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isHtmlConfigExist(HtmlReportsConfig htmlReportConfig) {
		logger.debug("Entry: isHtmlConfigExist, htmlReportConfig->"+htmlReportConfig);
		Query query=null;
		boolean isExists = false;
		List<HtmlReportsConfig> htmlReportConfigList = new ArrayList<HtmlReportsConfig>();
		String where = "";
		try {
			if(htmlReportConfig.getRemoveTag()) {
				where = "removeTagName=:removeTag";
			} else {
				where = "splitContentName=:splitContentName";
			}
			query =  sessionFactory
					.getCurrentSession()
					.createQuery("from HtmlReportsConfig where applicationId =:applicationId and environmentId=:environmentId and "+where);
			query.setParameter("application", htmlReportConfig.getApplicationId());
			query.setParameter("environment", htmlReportConfig.getEnvironmentId());
			
			if(htmlReportConfig.getRemoveTag()) {
				query.setParameter("removeTag", htmlReportConfig.getRemoveTagName());
			} else {
				query.setParameter("splitContentName", htmlReportConfig.getSplitContentName());
			}
			
			htmlReportConfigList = query.list(); 
			
			if(htmlReportConfigList != null && htmlReportConfigList.size() > 0) {
				isExists = true;
			}

		} catch (Exception e) {
			logger.error("Exception in isHtmlConfigExist :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: isHtmlConfigExist");
		return isExists;
	}
}
