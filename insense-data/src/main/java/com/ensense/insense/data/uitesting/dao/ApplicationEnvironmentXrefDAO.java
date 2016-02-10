package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;

public interface ApplicationEnvironmentXrefDAO {

	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(
			int applicationId, int environmentId);

	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory();
}
