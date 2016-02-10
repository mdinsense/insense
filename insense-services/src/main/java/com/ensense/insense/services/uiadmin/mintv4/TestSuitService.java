package com.ensense.insense.services.uiadmin.mintv4;

import java.util.List;

import com.cts.mint.uitesting.model.mintv4.TestSuitDetails;
import com.cts.mint.uitesting.model.mintv4.TestSuitForm;

public interface TestSuitService {
	public boolean saveSuit(TestSuitForm suitForm);
	public TestSuitDetails getTestSuitDetails(int suitId);
	public List<TestSuitDetails> getAllTestSuitDetails();
}
