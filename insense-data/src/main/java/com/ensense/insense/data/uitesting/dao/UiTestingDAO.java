package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.common.entity.SuitGroupXref;
import com.ensense.insense.data.uitesting.entity.Suit;
import com.ensense.insense.data.uitesting.entity.SuitBrokenReportsXref;
import com.ensense.insense.data.uitesting.entity.SuitTextImageXref;

import java.util.ArrayList;
import java.util.List;


public interface UiTestingDAO {

	public boolean saveFunctionalityTestingSuit(Suit suit);
	
	public boolean saveSuitGroupXref(SuitGroupXref suitGroup);

	public List<String> getBatchFileData();
	
	public boolean saveTextOrImageSuit(Suit suit);
	
	public boolean saveSuitTextImageXref(SuitTextImageXref suitTextImage);
	
	public List<SuitTextImageXref> getSuitTextImageXref(int suitId);
	
	public boolean deleteSuitTextImageXref(int suitId); 
	
	public boolean saveSuitBrokenReportsXref(ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList);
	
	public boolean deleteSuitBrokenReportsXref(int suitId);
	
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXref(int suitId);
	
	public boolean getCountSuitTextImageXref(int suitId);
}
