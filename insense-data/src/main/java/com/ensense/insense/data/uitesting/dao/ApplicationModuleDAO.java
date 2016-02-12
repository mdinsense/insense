package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.uitesting.entity.ApplicationModuleXref;
import com.ensense.insense.data.uitesting.entity.ModuleType;
import com.ensense.insense.data.uitesting.entity.TransactionTestCase;

import java.util.List;


public interface ApplicationModuleDAO {

	List<ApplicationModuleXref> getApplicationModuleList();

	boolean addApplicationModule(ApplicationModuleXref applicationModuleXref);

	ApplicationModuleXref getApplicationModule(ApplicationModuleXref applicationModuleXref);

	boolean updateApplicationModule(ApplicationModuleXref applicationModuleXref);

	boolean deleteApplicationModule(ApplicationModuleXref applicationModuleXref);

	List<ModuleType> getModuleTypeList();

	List<ApplicationModuleXref> getModuleListForApplicationAndCategory(
			int applicationId, int categoryId);

	boolean confirmModuleIdForALL(ApplicationModuleXref applicationModuleXref);

	boolean saveTransactionTestCase(TransactionTestCase testCase);

	boolean deleteTestCaseByTestCaseId(int testCaseId);

	List<ApplicationModuleXref> getModulesForSuit(String modules);

	TransactionTestCase getTransactionTestCase(int testCaseId);
}
