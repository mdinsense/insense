package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.ApplicationConfigDAO;
import com.cts.mint.uitesting.entity.ApplicationConfig;
import com.cts.mint.uitesting.service.ApplicationConfigService;

@Service
public class ApplicationConfigServiceImpl implements ApplicationConfigService {
	private static Logger logger = Logger
			.getLogger(ApplicationConfigServiceImpl.class);
	@Autowired
	private ApplicationConfigDAO applicationConfigDAO;

	@Override
	@Transactional
	public boolean saveApplicationConfig(ApplicationConfig applicationConfig) {
		logger.debug("Entry And Exit: saveApplicationConfig");
		return applicationConfigDAO.saveApplicationConfig(applicationConfig);
	}

	@Override
	@Transactional
	public ApplicationConfig getApplicationConfig(int applicationId,
			int environmentId) {
		logger.debug("Entry And Exit: getApplicationConfig");
		return applicationConfigDAO.getApplicationConfig(applicationId,
				environmentId);
	}

	@Override
	@Transactional
	public List<ApplicationConfig> getApplicationConfigList() {
		logger.debug("Entry And Exit: getApplicationConfigList");
		return applicationConfigDAO.getApplicationConfigList();
	}

	@Override
	@Transactional
	public boolean deleteAppConfiguration(ApplicationConfig applicationConfig) {
		logger.debug("Entry And Exit: deleteAppConfiguration");
		return applicationConfigDAO.deleteAppConfiguration(applicationConfig);
	}

	@Override
	@Transactional
	public ApplicationConfig getApplicationConfig(int configId) {
		logger.debug("Entry And Exit: getApplicationConfig");
		return applicationConfigDAO.getApplicationConfig(configId);
	}
}
