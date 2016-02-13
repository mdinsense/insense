package com.ensense.insense.services.uiadmin;

import com.ensense.insense.data.uitesting.entity.ApplicationModuleXref;
import com.ensense.insense.data.uitesting.entity.ModuleType;
import com.ensense.insense.data.uitesting.entity.TransactionTestCase;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

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
