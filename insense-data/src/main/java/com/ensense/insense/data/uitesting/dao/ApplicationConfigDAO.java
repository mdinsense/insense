package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.uitesting.entity.ApplicationConfig;

import java.util.List;


public interface ApplicationConfigDAO {
	
	boolean saveApplicationConfig(ApplicationConfig applicationConfig);

	ApplicationConfig getApplicationConfig(int applicationId, int environmentId);
	
	ApplicationConfig getApplicationConfig(int configId);
	
	List<ApplicationConfig> getApplicationConfigList();
	
	boolean deleteAppConfiguration(ApplicationConfig applicationConfig);
	
}
