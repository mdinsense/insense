package com.ensense.insense.services.uiadmin;

import java.util.ArrayList;
import java.util.List;

import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;

public interface UiTestingService {

	public boolean saveFunctionalityTestingSuit(Suit suit);
	
	public boolean saveSuitGroupXref(SuitGroupXref suitGroup);

	public boolean syncBatchFileData();
	
	public boolean saveTextOrImageSuit(Suit suit);
	
	public boolean saveSuitTextImageXref(SuitTextImageXref suitGroup);
	
	public List<SuitTextImageXref> getSuitTextImageXref(Integer suitId);
	
	public boolean deleteSuitTextImageXref(int suitId);
	
	public boolean saveSuitBrokenReportsXref(ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList);
	
	public boolean deleteSuitBrokenReportsXref(int suitId);
}
