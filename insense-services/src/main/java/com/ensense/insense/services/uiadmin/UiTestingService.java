package com.ensense.insense.services.uiadmin;

import com.ensense.insense.data.common.entity.SuitGroupXref;
import com.ensense.insense.data.uitesting.entity.Suit;
import com.ensense.insense.data.uitesting.entity.SuitBrokenReportsXref;
import com.ensense.insense.data.uitesting.entity.SuitTextImageXref;

import java.util.ArrayList;
import java.util.List;



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
