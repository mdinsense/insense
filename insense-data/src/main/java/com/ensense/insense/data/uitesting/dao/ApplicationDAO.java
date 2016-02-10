package com.ensense.insense.data.uitesting.dao;

import java.util.List;
import java.util.Map;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.uitesting.entity.Application;

public interface ApplicationDAO {

	public boolean addApplication(Application application);

	public List<Application> getAllApplicationDetails();

	public Application getApplication(int applicationId);

	public boolean isApplicationNameExists(String applicationName);

	public List<Application> getMappedApplicationList();

	public boolean deleteApplication(Application application);

	public List<ApplicationGroupReference> getApplicationListForGroup(
			int groupId);

	public boolean isApplicationEnvironmentXrefExists(int applicationId,
													  int environmentId);

	public boolean saveApplicationGroupReference(
			ApplicationGroupReference applicationGroupReference)  throws Exception;
	
	public Application getApplicationForGroup(int applicationId);
	
	public boolean deleteApplicationGroupReference(int groupId);

	public Map<Integer, Application> getApplicationMap(
			String applicationId);
}
