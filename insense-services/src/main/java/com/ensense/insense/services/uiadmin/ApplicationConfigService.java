package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.ApplicationConfig;

public interface ApplicationConfigService {

	boolean saveApplicationConfig(ApplicationConfig applicationConfig);

	ApplicationConfig getApplicationConfig(int applicationId, int environmentId);
	
	ApplicationConfig getApplicationConfig(int configId);
	
	List<ApplicationConfig> getApplicationConfigList();
	
	boolean deleteAppConfiguration(ApplicationConfig applicationConfig);
}
