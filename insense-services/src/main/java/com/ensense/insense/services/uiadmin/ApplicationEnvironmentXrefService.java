package com.ensense.insense.services.uiadmin;


import com.ensense.insense.data.uitesting.entity.AppEnvEnvironmentCategoryXref;

import java.util.List;


public interface ApplicationEnvironmentXrefService {
	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(int applicationId, int environmentId);
	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory();
}
