package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.ExcludeURLDAO;
import com.cts.mint.uitesting.entity.AnalyticsExcludeLink;
import com.cts.mint.uitesting.entity.ExcludeLinkType;
import com.cts.mint.uitesting.entity.ExcludeUrl;
import com.cts.mint.uitesting.service.ExcludeURLService;


@Service
public class ExcludeURLServiceImpl implements  ExcludeURLService {
	private static Logger logger = Logger.getLogger(ExcludeURLServiceImpl.class);
	
	@Autowired
	ExcludeURLDAO excludeURLDAO;
	/*Calling ExcludeURLDAOImpl class to get ExcludeURLList
	 * 	
	 */	
	@Override
	@Transactional
	public List<ExcludeUrl> getExcludeURLList() {
		logger.info("Entry: service:getExcludeURLList");
		
		return excludeURLDAO.getExcludeURLList();
	}
	/*Calling ExcludeURLDAOImpl class to add new ExcludeURL to the List
	 * 	
	 */	
	@Override
	@Transactional
	public boolean addExcludeURL(ExcludeUrl excludeUrl) {
		logger.info("Entry: service:addExcludeURL");
		return excludeURLDAO.addExcludeURL(excludeUrl);
	}
	
	@Override
	@Transactional
	public ExcludeUrl getExcludeURLBO(ExcludeUrl excludeUrl) {
		logger.info("Entry: service:getExcludeURLBO");
		return excludeURLDAO.getExcludeURLBO(excludeUrl);
	}
	
	@Override
	@Transactional
	public boolean deleteExcludeURLDetails(ExcludeUrl excludeUrl) {
		logger.info("Entry: service:deleteExcludeURLDetails");
		return excludeURLDAO.deleteExcludeURLDetails(excludeUrl);
	}
	@Override
	@Transactional
	public List<ExcludeUrl> getExcludeURLList(Integer applicationId,
			Integer environmentId) {
		logger.info("Entry and Exit: getExcludeURLList");
		return excludeURLDAO.getExcludeURLList(applicationId, environmentId);
	}
	
	@Override
	@Transactional
	public List<ExcludeLinkType> getExcludeLinkType(){
		logger.info("Entry and Exit: getExcludeLinkType");
		return excludeURLDAO.getExcludeLinkType();
	}
	
	@Override
	@Transactional
	public boolean addExcludeLink(AnalyticsExcludeLink analyticsExcludeLink){
		logger.info("Entry and Exit: addExcludeLink");
		return excludeURLDAO.addExcludeLink(analyticsExcludeLink);
	}
	
	@Override
	@Transactional
	public AnalyticsExcludeLink getAnalyticsExcludeSetupModule(AnalyticsExcludeLink analyticsExcludeLink){
		logger.info("Entry and Exit: getAnalyticsExcludeSetupModule");
		return excludeURLDAO.getAnalyticsExcludeSetupModule(analyticsExcludeLink);
	}
	
	@Override
	@Transactional
	public boolean deleteAnalyticsExcludeSetup(AnalyticsExcludeLink analyticsExcludeLink){
		logger.info("Entry and Exit: deleteAnalyticsExcludeSetup");
		return excludeURLDAO.deleteAnalyticsExcludeSetup(analyticsExcludeLink);	
	}
	
	@Override
	@Transactional
	public List<AnalyticsExcludeLink> getAnalyticsExcludeLinksetup(){
		logger.info("Entry and Exit: getAnalyticsExcludeLinksetup");
		return excludeURLDAO.getAnalyticsExcludeLinksetup();
	}
}





