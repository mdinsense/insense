package com.ensense.insense.data.uitesting.dao.mintv4;

import com.ensense.insense.data.uiadmin.form.schedule.mintv4.TestSuitDetails;
import com.ensense.insense.data.uitesting.entity.mintv4.*;

import java.util.List;

public interface TestSuitDAO {
	public boolean saveSuit(TestSuit TestSuit);
	public boolean saveEnvironment(TestEnvironment testEnvironment);
	public boolean saveLoginDetails(TestLoginDetail testLoginDetail);
	public boolean saveTestSuitBrowserConfig(TestSuitBrowserConfig testSuitBrowserConfig);
	public boolean saveTestSuitTextImageXref(TestSuitTextImageXref suitGroup);
	public boolean saveTestSuitTagTextXref(TestSuitRemoveTagOrTextXref removeTagOrText);
	public boolean saveTestSuitIncludeExcludeXref(TestSuitIncludeExcludeXref includeExclude);
	public boolean saveTestSuitCompareConfigXref(TestSuitCompareConfigXref testSuitCompareConfigXref);
	public TestSuitDetails getTestSuitDetails(int suitId);
	public List<TestSuitDetails> getAllTestSuitDetails();
}
