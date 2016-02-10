package com.ensense.insense.data.uitesting.dao.mintv4;

import java.util.List;

import com.cts.mint.uitesting.entity.mintv4.TestEnvironment;
import com.cts.mint.uitesting.entity.mintv4.TestLoginDetail;
import com.cts.mint.uitesting.entity.mintv4.TestSuit;
import com.cts.mint.uitesting.entity.mintv4.TestSuitBrowserConfig;
import com.cts.mint.uitesting.entity.mintv4.TestSuitCompareConfigXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitIncludeExcludeXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitRemoveTagOrTextXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitTextImageXref;
import com.cts.mint.uitesting.model.mintv4.TestSuitDetails;

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
