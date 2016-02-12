package com.ensense.insense.services.uiadmin.impl;

import com.ensense.insense.data.common.entity.EnvironmentCategoryGroupXref;
import com.ensense.insense.data.uitesting.dao.EnvironmentDAO;
import com.ensense.insense.data.uitesting.entity.*;
import com.ensense.insense.services.uiadmin.EnvironmentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {
	private static Logger logger = Logger
			.getLogger(EnvironmentServiceImpl.class);

	@Autowired
	EnvironmentDAO environmentDAO;

	@Override
	@Transactional
	public List<EnvironmentCategoryGroupXref> getEnvironmentListForGroup(
			int groupId) {
		logger.debug("Enter and Exit :getEnvironmentListForGroup");
		return environmentDAO.getEnvironmentListForGroup(groupId);
	}

	@Override
	@Transactional
	public List<Environment> getAllEnvironmentDetails() {
		logger.debug("Enter and Exit :getAllEnvironmentDetails");
		return environmentDAO.getAllEnvironmentDetails();
	}

	@Override
	@Transactional
	public boolean addEnvironment(Environment environment) {
		logger.debug("Enter and Exit :addEnvironment");
		return environmentDAO.addEnvironment(environment);
	}

	@Override
	@Transactional
	public boolean addEnvironmentLoginScript(
			EnvironmentLoginScript environmentLoginScript) {
		logger.debug("Enter and Exit :addEnvironmentLoginScript");
		return environmentDAO.addEnvironmentLoginScript(environmentLoginScript);
	}

	@Override
	@Transactional
	public boolean deleteEnvironmentLoginScript(int environmentId) {
		logger.debug("Enter and Exit :deleteEnvironmentLoginScript");
		return environmentDAO.deleteEnvironmentLoginScript(environmentId);
	}

	@Override
	@Transactional
	public EnvironmentLoginScript getEnvironmentLoginScript(int environmentId) {
		logger.debug("Enter and Exit :getEnvironmentLoginScript");
		return environmentDAO.getEnvironmentLoginScript(environmentId);
	}

	@Override
	@Transactional
	public List<EnvironmentLoginScript> getEnvironmentScriptListDetails() {
		logger.debug("Enter and Exit :getEnvironmentScriptListDetails");
		return environmentDAO.getEnvironmentScriptListDetails();
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryForEnvironment(
			int environmentId) {
		logger.debug("Enter and Exit :getEnvironment");
		return environmentDAO
				.getEnvironmentCategoryForEnvironment(environmentId);
	}

	@Override
	@Transactional
	public boolean isEnvironmentExists(String environmentName) {
		logger.debug("Enter and Exit :isEnvironmentExists");
		return environmentDAO.isEnvironmentExists(environmentName);
	}

	@Override
	@Transactional
	public List<Environment> getMappedEnvironmentList() {
		logger.debug("Enter and Exit :getMappedEnvironmentList");
		return environmentDAO.getMappedEnvironmentList();
	}

	@Override
	@Transactional
	public boolean deleteEnvironment(UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Enter and Exit :deleteEnvironment");
		return environmentDAO.deleteEnvironment(uiTestingSetupForm);
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryList() {
		logger.debug("Enter and Exit :getEnvironmentCategoryList");
		return environmentDAO.getEnvironmentCategoryList();
	}
	
	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryListWithoutAll(){
		logger.debug("Enter and Exit :getEnvironmentCategoryListWithoutAll");
		return environmentDAO.getEnvironmentCategoryListWithoutAll();
	}
	@Override
	@Transactional
	public boolean addEnvironmentCategoryXref(
			AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref) {
		logger.debug("Enter and Exit :addEnvironmentCategoryXref");
		return environmentDAO
				.addEnvironmentCategoryXref(appEnvEnvironmentCategoryXref);
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryName(int environmentId) {
		logger.debug("Enter and Exit :getEnvironmentCategoryName");
		return environmentDAO.getEnvironmentCategoryName(environmentId);
	}

	@Override
	@Transactional
	public List<Environment> getEnvironmentsByCategory(
			int environmentCategoryId, int applicationId) {
		logger.debug("Enter and Exit :getEnvironmentsByCategory");
		return environmentDAO.getEnvironmentsByCategory(environmentCategoryId,
				applicationId);
	}

	@Override
	@Transactional
	public List<Environment> getSecureEnvironmentsByCategory(
			int environmentCategoryId, int applicationId) {
		logger.debug("Enter and Exit :getSecureEnvironmentsByCategory");
		return environmentDAO.getSecureEnvironmentsByCategory(
				environmentCategoryId, applicationId);
	}

	@Override
	@Transactional
	public List<Environment> getEnvironmentListForApplicationId(
			int applicationId) {
		logger.debug("Entry And Exit : getEnvironmentListForApplicationId");
		return environmentDAO.getEnvironmentListForApplicationId(applicationId);
	}
	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategory(int applicationId) {
		logger.debug("Entry And Exit : getEnvironmentCategory");
		return environmentDAO.getEnvironmentCategory(applicationId);
	}

	@Override
	@Transactional
	public boolean isMachesWithExistingEnvironmentAndEnvrionmentCategory(
			int applicationId, int environmentId, int environmentCategoryId) {
		logger.debug("Entry And Exit : isEnvironmentCategoryAlreadyExistsForApplication");
		return environmentDAO
				.isMachesWithExistingEnvironmentAndEnvrionmentCategory(
						applicationId, environmentId, environmentCategoryId);
	}

	@Override
	@Transactional
	public Environment getEnvironment(int environmentId) {
		logger.debug("Enter and Exit :getEnvironmentsByCategory");
		return environmentDAO.getEnvironment(environmentId);
	}

	@Override
	@Transactional
	public boolean saveEnvironmentGroupReference(
			EnvironmentCategoryGroupXref environmentCategoryGroupXref) throws Exception {
		logger.debug("Enter and Exit :saveEnvironmentGroupReference");
		return environmentDAO.saveEnvironmentGroupReference(environmentCategoryGroupXref);
	}
	
	@Override
	@Transactional
	public boolean deleteEnvironmentGroupReference(int groupId){
		logger.debug("Enter and Exit :deleteEnvironmentGroupReference");
		return environmentDAO.deleteEnvironmentGroupReference(groupId);
	}
	
	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryForGroup(
			int environmentId){
		logger.debug("Enter and Exit :getEnvironmentCategoryForGroup");
		return environmentDAO.getEnvironmentCategoryForGroup(environmentId);
	}

	@Override
	@Transactional
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRef(
			int applicationId, int groupId) {
		logger.debug("Enter and Exit :getEnvironmentCategoryGroupRef");
		return environmentDAO.getEnvironmentCategoryGroupRef(applicationId,groupId);
	}

	@Override
	@Transactional
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRefByGroupId(
			int groupId) {
		logger.debug("Enter and Exit :getEnvironmentCategoryGroupRefByGroupId");
		return environmentDAO.getEnvironmentCategoryGroupRefByGroupId(groupId );
	}

	@Override
	@Transactional
	public boolean addEnvEnvironmentCategoryXref(
			EnvEnvironmentCategoryXref envEnvironmentCategoryXref) {
		logger.debug("Enter and Exit :addEnvEnvironmentCategoryXref");
		return environmentDAO.addEnvEnvironmentCategoryXref(envEnvironmentCategoryXref);
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryById(
			int environmentCategoryId) {
		logger.debug("Enter and Exit :getEnvironmentCategoryById");
		return environmentDAO.getEnvironmentCategoryById(environmentCategoryId);
	}
}
