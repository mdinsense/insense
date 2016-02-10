package com.ensense.insense.services.uiadmin;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.ModuleType;
import com.cts.mint.uitesting.entity.TransactionTestCase;


public interface ApplicationModuleService {

	public List<ApplicationModuleXref> getApplicationModuleList();
	
	public boolean addApplicationModule(ApplicationModuleXref applicationModuleXref);
	
	public ApplicationModuleXref getApplicationModule(ApplicationModuleXref applicationModuleXref);

	public boolean updateApplicationModule(ApplicationModuleXref applicationModuleXref);

	public boolean deleteApplicationModule(ApplicationModuleXref applicationModuleXref);
	
	public List<ModuleType> getModuleTypeList();
	
	public List<ApplicationModuleXref> getModuleListForApplicationAndCategory(
			int applicationId, int categoryId);
	
	public boolean confirmModuleIdForALL(ApplicationModuleXref applicationModuleXref);

	public boolean deleteTestCaseByTestCaseId(int testCaseId, String filePathToDelete);

	List<ApplicationModuleXref> getTransactionalModulesForSuit(int suitId);
	
	TransactionTestCase getTransactionTestCase(int testCaseId);
	
	public boolean updateTransactionTestCase(TransactionTestCase transactionTestCase, CommonsMultipartFile javafile, CommonsMultipartFile htmlfile);
}
