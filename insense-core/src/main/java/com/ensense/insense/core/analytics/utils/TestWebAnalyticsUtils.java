package com.ensense.insense.core.analytics.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestWebAnalyticsUtils {

	String baseLineHarDirPath;
	String testHarDirPath;
	WebAnalyticsUtils wau;
	String excelFilePath;
	AnalyticsDataReportingUtils excelWriter;
	String sheetName;
	
	  @Before
	  public void setUp() throws Exception {
		  System.out.println("Test Begin >>>>>");
		  
		  	sheetName = "AnalyticsReport_PublicSite";
			baseLineHarDirPath="C:\\Dhinakar\\Project\\Mint\\MINT_SETUP\\HarReportsDirectory\\HarReportsDirectory\\PublicSite";
			testHarDirPath="C:\\Dhinakar\\Project\\Mint\\MINT_SETUP\\HarReportsDirectory\\HarReportsDirectory\\PublicSite\\public5";
			excelFilePath = testHarDirPath + "\\AnalyticsReport_PublicSite5.xlsx";
			excelWriter = new AnalyticsDataReportingUtils(excelFilePath, sheetName);
							
			
			wau= new WebAnalyticsUtils();

	  }
	 
	  //@Test
	 public void testGetWebAnalyticsSiteInventory(){
		 //wau.getWebAnalyticsSiteInventory(baseLineHarDirPath);
	 }
	  
	  @Test
	  public void testGenerateAuditAndTestExcelReport() throws Exception {
		  boolean collectOnlyFailure = false;
		  wau.generateAuditAndTestExcelReport(wau, baseLineHarDirPath, testHarDirPath, excelFilePath, excelWriter, "applicationName", "c:\\temp", "C:\\projects\\PortalCommonsV8A\\mintToolsV2\\mintCommonsJAR\\resources\\tag-signatures.json", collectOnlyFailure);
	  }

	  
	//@Test
	  public void testGetFunctionalGroupStats() throws Exception {
			List<Map<String,Integer>> functionalGroupStatsList=	wau.getFunctionalGroupStats(wau.getWebAnlyticsPageDataFromHarLogs(baseLineHarDirPath, "ApplicaitonName", "C:\\projects\\PortalCommonsV8A\\mintToolsV2\\mintCommonsJAR\\resources\\tag-signatures.json"),"http://intranet.ops.tiaa-cref.org",2);
			//verify
			int i=0;
			for(Map map:functionalGroupStatsList ){
			Iterator it=	map.keySet().iterator();
		i++;
			System.out.print(i +" >> ");
			while(it.hasNext()){
				String key=it.next().toString();
				int value= ((Integer)map.get(key)).intValue();
			System.out.print(key+"("+value+")" +" : ");
			
			}
			System.out.println("\n");
			
			}

	  }
	  
	  @Test
	  public void testTest() throws Exception{
		  System.out.println("Test End >>>>>>>>>>");
	  }
	  
	  @After
	  public void tearDown() throws Exception {
		  wau=null;
	  }
	


}
