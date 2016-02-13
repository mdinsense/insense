package com.ensense.insense.services.uiadmin.mintv4;

import com.ensense.insense.data.uiadmin.form.schedule.mintv4.TestSuitDetails;
import com.ensense.insense.services.uiadmin.form.schedule.mintv4.TestSuitForm;

import java.util.List;


public interface TestSuitService {
	public boolean saveSuit(TestSuitForm suitForm);
	public TestSuitDetails getTestSuitDetails(int suitId);
	public List<TestSuitDetails> getAllTestSuitDetails();
}
