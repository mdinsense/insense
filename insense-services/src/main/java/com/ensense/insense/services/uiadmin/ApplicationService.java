package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.uitesting.entity.Application;

public interface ApplicationService {

	public boolean addApplication(Application application);

	public List<Application> getAllApplicationDetails();

	public Application getApplication(int applicationId);
	
	public boolean isApplicationNameExists(String applicationName);

	public List<Application> getMappedApplicationDetails();

	public boolean deleteApplication(Application application);

	public List<ApplicationGroupReference> getApplicationListForGroup(int groupId);
	
	public boolean isApplicationEnvironmentXrefExists(int applicationId,
													  int environmentId);
	
	public boolean saveApplicationGroupReference(
			ApplicationGroupReference applicationGroupReference)  throws Exception;
	
	public Application getApplicationForGroup(int applicationId);
	
	public boolean deleteApplicationGroupReference(int groupId);
}
