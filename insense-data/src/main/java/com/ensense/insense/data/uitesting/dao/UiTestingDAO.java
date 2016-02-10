package com.ensense.insense.data.uitesting.dao;

import java.util.ArrayList;
import java.util.List;

import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;

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
