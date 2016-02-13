package com.ensense.insense.data.analytics.common;

import com.ensense.insense.data.analytics.model.*;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.utils.DateTimeUtil;
import com.ensense.insense.data.common.utils.JsonReaderWriter;
import com.ensense.insense.data.common.utils.UiTestingConstants;
import com.ensense.insense.core.analytics.detailedview.DetailedViewWebAnalyticsTagData;

import java.util.*;

public class TestWebAnalytic {
	public static void main(String args[]){
		generateAnalyticsReports();
		//generateBrokenUrleport();
	}
	
	private static void generateAnalyticsReports() {
		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		
		Map<String, WebAnalyticsPageData> baseLinePageDataMap = null;
		
		String currentRunHarPath = "C:\\Dhinakar\\Project\\Mint\\2016\\Analytics\\Feb\\PublicBeta\\temp";
		String baselineHarPath = "C:\\Dhinakar\\Project\\Mint\\2016\\Analytics\\Feb\\PublicBeta\\temp";
		String homePath = "C:\\Dhinakar\\temp1";
		String applicationName = "PublicSite_PRE_PROD";
		String analyticsExcelFilePath = "C:\\Dhinakar\\Project\\Mint\\2016\\Analytics\\Feb\\PublicBeta\\temp\\" + applicationName + DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		String analyticsFailureExcelFilePath = "C:\\Dhinakar\\Project\\Mint\\2016\\Analytics\\Feb\\PublicBeta\\temp\\" + applicationName + "_FAILURE_" + DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		
		System.out.println("*** Started Reading Current Run HAR reports for Analytics Audit. ****");
		Map<String, WebAnalyticsPageData> testPageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(currentRunHarPath, applicationName, "C:\\Projects\\PortalCommonsV8A\\mintToolsV3\\mintCommonsJAR\\resources\\tag-signatures.json");
		
		try{
			JsonReaderWriter<Map<String, WebAnalyticsPageData>> jsonReaderWriter = new JsonReaderWriter<Map<String, WebAnalyticsPageData>>();
			jsonReaderWriter.writeJsonObjectToFile(testPageDataMap, homePath + "current_" + UiTestingConstants.ANALYTICS_PAGE_DATA_MAP);
			System.out.println("Completed writing serialized object :"+homePath + "current_"+ UiTestingConstants.ANALYTICS_PAGE_DATA_MAP);
		}catch(Exception e){
			System.out.println(UiTestingConstants.ANALYTICS_PAGE_DATA_MAP + ", is not available yet for the ScheduleExecutionId.");
		}
		
		
		System.out.println("*** Completed Reading Current Run HAR reports for Analytics Audit. ****");

		System.out.println("*** Started Summary report generation ***");
		
		System.out.println("*** completed Summary Reprot Generation ***");
		
		System.out.println("*** Started Reading Baseline HAR reports for Analytics Audit. ****");
		
		//TODO baseline
		baseLinePageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(baselineHarPath, applicationName, "C:\\Projects\\PortalCommonsV8A\\mintToolsV3\\mintCommonsJAR\\resources\\tag-signatures.json");
		
		//baseLinePageDataMap = testPageDataMap;
		System.out.println("*** Completed Reading Baseline HAR reports for Analytics Audit. ****");

		try{
			JsonReaderWriter<Map<String, WebAnalyticsPageData>> jsonReaderWriter = new JsonReaderWriter<Map<String, WebAnalyticsPageData>>();
			jsonReaderWriter.writeJsonObjectToFile(baseLinePageDataMap, homePath + "baseline_"+ UiTestingConstants.ANALYTICS_PAGE_DATA_MAP);
			System.out.println("Completed writing serialized object :"+homePath + "baseline_"+ UiTestingConstants.ANALYTICS_PAGE_DATA_MAP);
		}catch(Exception e){
			System.out.println(homePath + "baseline_"+ UiTestingConstants.ANALYTICS_PAGE_DATA_MAP + ", Unable to write.");
		}
		
		List<SamePagesDataStore> samePagesDataStoreList = wau
				.normalizePageDataMaps(baseLinePageDataMap, testPageDataMap);

		AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(
				analyticsExcelFilePath, "Sheet");
		
		boolean collectOnlyFailureUrls = false;
		boolean beautify = true;
		excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
				analyticsExcelFilePath, "C:\\Dhinakar\\temp1",beautify, "2015-12-24_10:01:00", "2015-12-24_10:01:00", collectOnlyFailureUrls);
		
		collectOnlyFailureUrls = true;
		excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
				analyticsFailureExcelFilePath, "C:\\Dhinakar\\temp1",beautify, "2015-12-24_10:01:00", "2015-12-24_10:01:00", collectOnlyFailureUrls);
		
		System.out.println("Generation completed. file path :"+analyticsExcelFilePath);
		
		//generateAnalyticsSummaryView(testPageDataMap, baseLinePageDataMap);
		
	}
	
	private static void generateBrokenUrleport(){
		System.out.println("*** Started Broken URL reprot generation. ***");
		WebAnalyticsUtils wau= new WebAnalyticsUtils();
		AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils("C:\\Dhinakar\\temp\\Analytics\\IFA_PROD_FIX_TIMEMonitor\\IFA_current\\IFA\\shift1\\IFA_PROD_FIX_BrokenUrlReport.xlsx","BrokenUrlData");

		
		String harDirPath = "C:\\Dhinakar\\temp\\Analytics\\IFA_PROD_FIX_TIMEMonitor\\IFA_current\\IFA\\shift1";
		String baselineHarDirPath = "C:\\Dhinakar\\temp\\Analytics\\IFA_PROD_FIX_TIMEMonitor\\IFA_Baseline\\IFA\\shift1";
		String applicationName = "IFA_PROD_FIX";
		String navigationListPath = "";
		boolean generatePageloadAttributes = true;
		boolean generateReportsOnlyForRefererUrl = true;
		boolean beautify = true;
		boolean collectAllUrls = true;
		List<Link> navigationList = new ArrayList<Link>();
		String tempDirectory = "C:\\Dhinakar\\temp1";
		
		String brokenUrlExcelFilePath = "C:\\Dhinakar\\temp\\Analytics\\IFA_PROD_FIX_TIMEMonitor\\" + applicationName + DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		String brokenUrlAuditExcelFilePath = brokenUrlExcelFilePath;
		
		Map<String, BrokenUrlData> brokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
		Map<String, BrokenUrlData> baselineBrokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
		
		brokenUrlDataMap = wau.getBrokenUrlDataFromHarLogs(harDirPath, applicationName, collectAllUrls, navigationList, generateReportsOnlyForRefererUrl);
		
		System.out.println("brokenUrlDataMap Count :"+brokenUrlDataMap.size());
		
		baselineBrokenUrlDataMap = wau.getBrokenUrlDataFromHarLogs(baselineHarDirPath, applicationName, collectAllUrls, navigationList, generateReportsOnlyForRefererUrl);
		
		System.out.println("baselineBrokenUrlDataMap Count :"+baselineBrokenUrlDataMap.size());
		
		List<SamePagesBrokenUrlDataStore> samePagesBrokenUrlDataStore = wau.normalizeBrokenUrlDataMap(brokenUrlDataMap, baselineBrokenUrlDataMap);
		
		System.out.println("samePagesBrokenUrlDataStore :"+samePagesBrokenUrlDataStore.size());
		
		AnalyticsDataReportingUtils adru = new AnalyticsDataReportingUtils();
		
		adru.writeBrokenUrlAuditDetailsToExcel(samePagesBrokenUrlDataStore, brokenUrlAuditExcelFilePath, tempDirectory);
		
		/*
		new WebAnalyticsUtils().generateBrokenUrlReport(wau, "C:\\Tools\\mint\\Application\\ResultsDirectory\\Planfocus_PLANFOCUS_PROD_SSO_2015-Dec-11_03-05-23\\firefox\\HarReportsDirectory\\Planfocus\\plantiaa4", 
				"Publicsite_ST4", brokenUrlExcelFilePath, excelWriter, "UD", true, "C:\\Dhinakar\\temp1", 
				new ArrayList<Link>(), generateReportsOnlyForRefererUrl, generatePageloadAttributes, beautify);
		
		*/
		/*CrawlConfig crawlconfig = null;
		
		try{
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			crawlconfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), "C:\\Tools\\mint\\Application\\ResultsDirectory\\Planfocus_PLANFOCUS_PROD_SSO_2015-Dec-11_03-05-23\\\\serialize\\" + UiTestingConstants.CRAWL_CONFIG);
		}catch(Exception e){
			System.out.println(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId.");
		}
		
		String brokenUrlReportFilePath = "C:\\Tools\\mint\\Application\\ResultsDirectory\\Planfocus_PLANFOCUS_PROD_SSO_2015-Dec-11_03-05-23\\\\Reports\\PlanfocusBrokenUrlReport_"+DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		generateReportsOnlyForRefererUrl = true;
		generatePageloadAttributes = true;
		
		wau.generateBrokenUrlReport(wau, "C:\\Tools\\mint\\Application\\ResultsDirectory\\Planfocus_PLANFOCUS_PROD_SSO_2015-Dec-11_03-05-23\\firefox\\HarReportsDirectory\\Planfocus\\plantiaa4", "PublicSite", brokenUrlReportFilePath, excelWriter, "PlanfocusBrokenUrlReport", true, "C:\\Dhinakar\\temp1", 
				crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl, generatePageloadAttributes);
				*/
		System.out.println("Broken URL report path :"+brokenUrlExcelFilePath);
		System.out.println("*** Completed Broken URL reprot generation. ***");
	}
	
	private static AnalyticsSummaryReport generateAnalyticsSummaryView(
			Map<String, WebAnalyticsPageData> pageDataMap, Map<String, WebAnalyticsPageData> baseLinePageDataMap) {
		System.out.println("Total Url Count :"+pageDataMap.size());
		AnalyticsSummaryReport analyticsSummaryReport = new AnalyticsSummaryReport();
		
		Iterator urlIterator = pageDataMap.keySet().iterator();
		String urlkey;

		while (urlIterator.hasNext()) {
			urlkey = urlIterator.next().toString();
		
			WebAnalyticsPageData pageData = pageDataMap
					.get(urlkey);

			WebAnalyticsPageData baselinePageData = baseLinePageDataMap
					.get(urlkey);

			//Adding all the URLs
			analyticsSummaryReport.getAllUrls().add(pageData.getPageUrl());

			Map<String, WebAnalyticsTagData> tagData = pageData
					.getWebAnalyticsTagDataMap();
			
			Map<String, WebAnalyticsTagData> baselineTagData = baselinePageData
					.getWebAnalyticsTagDataMap();
			
			Iterator tagDataIterator = tagData.keySet().iterator();
			
			String tagDataKey;
			while (tagDataIterator.hasNext()) {
				tagDataKey = tagDataIterator.next().toString();

				WebAnalyticsTagData webAnalyticsTagData = tagData.get(tagDataKey);

				WebAnalyticsTagData webAnalyticsBaselineTagData = baselineTagData.get(tagDataKey);
				
				if ( null == analyticsSummaryReport.getEachAnalyticsTagMap().get(webAnalyticsTagData.getTagName()) ){
					AnalyticsTagDetail analyticsTagDetail = new AnalyticsTagDetail();
					
					analyticsSummaryReport.getEachAnalyticsTagMap().put(webAnalyticsTagData.getTagName(), analyticsTagDetail);
				}
				
				AnalyticsTagDetail analyticsTagDetail = analyticsSummaryReport.getEachAnalyticsTagMap().get(webAnalyticsTagData.getTagName());
				
				//Adding to set so don't take duplicates
				analyticsTagDetail.getTagPresentUrl().add(pageData.getPageUrl());
				
				
				DetailedViewWebAnalyticsTagData.DetailedViewTags entrylist = new DetailedViewWebAnalyticsTagData.DetailedViewTags();
				entrylist.setTagtype(webAnalyticsTagData.getTagName());
				entrylist.setTagUrl(webAnalyticsTagData.getTagUrl() );
				entrylist.setTagDataKey(webAnalyticsTagData.getTagDataKey());
				entrylist.setStartedDateTime(webAnalyticsTagData.getStartedDateTime());
				
				Map<String, String> tagname = webAnalyticsTagData.getTagVariableData();
				Iterator it1 = tagname.keySet().iterator();
				while (it1.hasNext()) {
					
					String data=it1.next().toString();
					
					DetailedViewWebAnalyticsTagData.DetailedViewTags.TagVariablesData tagVarNameValue = new DetailedViewWebAnalyticsTagData.DetailedViewTags.TagVariablesData();
					tagVarNameValue.setName(data);
					tagVarNameValue.setValue(tagname.get(data));
					
					entrylist.getTagVariablesData().add(tagVarNameValue);
					
					if(tagname.equals("SiteCatalyst") && tagVarNameValue.getName().equals("pageName")){
						if(tagVarNameValue.getValue().startsWith(":") 
								|| tagVarNameValue.getValue().endsWith(":") 
								|| tagVarNameValue.getValue().equals("null") 
								|| tagVarNameValue.getValue().equals("undefined")
								|| tagVarNameValue.getValue().equals(null)
								|| tagVarNameValue.getValue().equals("NoTagVarValueFound")
								|| tagVarNameValue.getValue().equals("")){
							analyticsTagDetail.getUrlHasErrorTag().add(pageData.getPageUrl());
						}
						
					} else if(
							tagVarNameValue.getValue().equals("undefined") ||
							tagVarNameValue.getValue().equals("null") ||
							//baseLineTagVarValue.equals("NoTagVarValueFound") ||
							tagVarNameValue.getValue().equals("")){
						analyticsTagDetail.getUrlHasErrorTag().add(pageData.getPageUrl());
					}
					
				}
			}
		}
		
		return analyticsSummaryReport;
		
	}


}
