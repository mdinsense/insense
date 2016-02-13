package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.uitesting.entity.AppEnvEnvironmentCategoryXref;

import java.util.List;



public interface ApplicationEnvironmentXrefDAO {

	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref(
			int applicationId, int environmentId);

	public List<AppEnvEnvironmentCategoryXref> getAllApplicationEnvironmentCategory();
}
