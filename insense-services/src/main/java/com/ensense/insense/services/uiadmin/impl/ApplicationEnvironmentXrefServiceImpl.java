package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.ApplicationEnvironmentXrefDAO;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.service.ApplicationEnvironmentXrefService;

@Service
public class ApplicationEnvironmentXrefServiceImpl implements ApplicationEnvironmentXrefService{
	private static Logger logger = Logger.getLogger(ApplicationEnvironmentXrefServiceImpl.class);
	
	@Autowired
	ApplicationEnvironmentXrefDAO applicationEnvironmentXrefDAO;
	
	@Override
	@Transactional
	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(
			int applicationId, int environmentId) {
		return applicationEnvironmentXrefDAO.getAppEnvEnvironmentCategoryXref(applicationId, environmentId);
	}

	@Override
	@Transactional
	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory() {
		logger.debug("Enter and Exit :getAllApplicationEnvironmentCategory");
		return applicationEnvironmentXrefDAO.getAllApplicationEnvironmentCategory();
	}
}
