package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;

public interface ApplicationEnvironmentXrefService {
	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(int applicationId, int environmentId);
	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory();
}
