package com.ensense.insense.services.uiadmin;

import com.ensense.insense.data.common.entity.EnvironmentCategoryGroupXref;
import com.ensense.insense.data.uitesting.entity.*;
import com.ensense.insense.data.uitesting.model.UiTestingSetupForm;

import java.util.List;


public interface EnvironmentService {

	public List<EnvironmentCategoryGroupXref> getEnvironmentListForGroup(
			int groupId);

	public List<Environment> getAllEnvironmentDetails();

	public boolean addEnvironment(Environment environment);

	public boolean addEnvironmentLoginScript(
			EnvironmentLoginScript environmentLoginScript);

	public boolean deleteEnvironmentLoginScript(int environmentId);

	public EnvironmentLoginScript getEnvironmentLoginScript(int environmentId);

	public List<EnvironmentLoginScript> getEnvironmentScriptListDetails();

	public EnvironmentCategory getEnvironmentCategoryForEnvironment(
			int environmentId);

	public boolean isEnvironmentExists(String environmentName);

	public List<Environment> getMappedEnvironmentList();

	public List<EnvironmentCategory> getEnvironmentCategoryList();
	
	public List<EnvironmentCategory> getEnvironmentCategoryListWithoutAll();

	public boolean addEnvironmentCategoryXref(
			AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref);

	public EnvironmentCategory getEnvironmentCategoryName(int environmentId);

	public List<Environment> getEnvironmentsByCategory(
			int environmentCategoryId, int applicationId);

	public List<Environment> getSecureEnvironmentsByCategory(
			int environmentCategoryId, int applicationId);

	public List<Environment> getEnvironmentListForApplicationId(
			int applicationId);

	public boolean deleteEnvironment(UiTestingSetupForm uiTestingSetupForm);

	public List<EnvironmentCategory> getEnvironmentCategory(int applicationId);

	public boolean isMachesWithExistingEnvironmentAndEnvrionmentCategory(
			int applicationId, int environmentId, int environmentCategoryId);

	public Environment getEnvironment(int environmentId);

	public boolean saveEnvironmentGroupReference(
			EnvironmentCategoryGroupXref environmentCategoryGroupXref) throws Exception ;
	
	public boolean deleteEnvironmentGroupReference(int groupId);
	
	public EnvironmentCategory getEnvironmentCategoryForGroup(
			int environmentId);
	
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRef(
			int applicationId, int groupId);
	
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRefByGroupId(int groupId);

	public boolean addEnvEnvironmentCategoryXref(
			EnvEnvironmentCategoryXref envEnvironmentCategoryXref);
	
	public EnvironmentCategory getEnvironmentCategoryById(int environmentCategoryId);
}
