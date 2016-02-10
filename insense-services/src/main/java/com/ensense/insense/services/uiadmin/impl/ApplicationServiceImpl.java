package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.uitesting.dao.ApplicationDAO;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	private static Logger logger = Logger
			.getLogger(ApplicationServiceImpl.class);

	@Autowired
	ApplicationDAO applicationDAO;

	@Override
	@Transactional
	public boolean addApplication(Application application) {
		logger.debug("Entry And Exit : addApplication");
		return applicationDAO.addApplication(application);
	}

	@Override
	@Transactional
	public List<Application> getAllApplicationDetails() {
		logger.debug("Entry And Exit : getApplicaitonList");
		return applicationDAO.getAllApplicationDetails();
	}

	@Override
	@Transactional
	public Application getApplication(int applicationId) {
		logger.debug("Entry And Exit : getApplication");
		return applicationDAO.getApplication(applicationId);
	}
	
	@Override
	@Transactional
	public boolean isApplicationNameExists(String applicationName) {
		logger.debug("Entry And Exit : isApplicationNameExists");
		return applicationDAO.isApplicationNameExists(applicationName);
	}

	@Override
	@Transactional
	public List<Application> getMappedApplicationDetails() {
		logger.debug("Entry And Exit : getMappedApplicationDetails");
		return applicationDAO.getMappedApplicationList();
	}

	@Override
	@Transactional
	public boolean deleteApplication(Application application) {
		logger.debug("Entry And Exit : deleteApplication");
		return applicationDAO.deleteApplication(application);
	}

	@Override
	@Transactional
	public List<ApplicationGroupReference> getApplicationListForGroup(
			int groupId) {
		logger.debug("Entry And Exit : getApplicationListForGroup");
		return applicationDAO.getApplicationListForGroup(groupId);
	}

	@Override
	@Transactional
	public boolean isApplicationEnvironmentXrefExists(int applicationId,
			int environmentId) {
		logger.debug("Entry And Exit : isApplicationEnvironmentXrefExists");
		return applicationDAO.isApplicationEnvironmentXrefExists(applicationId, environmentId);
	}

	@Override
	@Transactional
	public boolean saveApplicationGroupReference(
			ApplicationGroupReference applicationGroupReference)  throws Exception{
		logger.debug("Entry And Exit : saveApplicationGroupReference");
		return applicationDAO.saveApplicationGroupReference(applicationGroupReference);
	}
	
	@Override
	@Transactional
	public Application getApplicationForGroup(int applicationId){
		logger.debug("Entry And Exit : getApplicationForGroup");
		return applicationDAO.getApplicationForGroup(applicationId);
	}
	
	@Override
	@Transactional
	public boolean deleteApplicationGroupReference(int groupId){
		logger.debug("Entry And Exit : deleteApplicationGroupReference");
		return applicationDAO.deleteApplicationGroupReference(groupId);
	}
}
