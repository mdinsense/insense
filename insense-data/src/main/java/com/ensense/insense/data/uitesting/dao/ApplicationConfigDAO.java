package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.ApplicationConfig;

public interface ApplicationConfigDAO {
	
	boolean saveApplicationConfig(ApplicationConfig applicationConfig);

	ApplicationConfig getApplicationConfig(int applicationId, int environmentId);
	
	ApplicationConfig getApplicationConfig(int configId);
	
	List<ApplicationConfig> getApplicationConfigList();
	
	boolean deleteAppConfiguration(ApplicationConfig applicationConfig);
	
}
