package com.ensense.insense.data.analytics.common;

import com.ensense.insense.data.analytics.model.AnalyticSummaryDetails;
import com.ensense.insense.data.analytics.model.AnalyticsDetails;
import com.ensense.insense.data.analytics.model.SamePagesDataStore;
import com.ensense.insense.data.analytics.model.WebAnalyticsPageData;
import com.ensense.insense.data.common.utils.DateTimeUtil;
import com.ensense.insense.data.common.utils.JsonReaderWriter;
import com.ensense.insense.data.common.utils.UiTestingConstants;
import com.ensense.insense.data.analytics.model.WebAnalyticsTagData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

;

public class AnalyticsReportsSerializeUtils {
	
	private static Logger logger = Logger
			.getLogger(AnalyticsReportsSerializeUtils.class);

	public static boolean writeAnalyticsSummaryDetails(
			List<SamePagesDataStore> samePagesDataStoreList,
			String detailedReportsPath, String errorReportsPath ,boolean collectOnlyFailure) {

		boolean isObjCreated = false;
		AnalyticSummaryDetails analyticSummaryDetails = new AnalyticSummaryDetails();
		Map<String, AnalyticsDetails> eachAnalyticsDetails = new HashMap<String, AnalyticsDetails>();

		int currentTagVarRowNum = 1;
		int startTagRowPosition = 0;
		int startVarRowPosition = 0;
		// page details
		String appName;
		String testAppName;
		String baseLinePageTitle; // pageTitle
		String testDataPageTitle;
		String baseLinePageUrl;
		String testDataPageUrl;

		String baseLineHarFileName;// ="Baseline Har File";
		String testDataHarFileName;// ="testData Har File";

		String baseLineTagName;// ="baseLineTagName";
		String testDataTagName;// ="testDataTagName";
		String baseLineTagUrl;// ="baseLineTagUrl";
		String testDataTagUrl;// ="testDataTagUrl";
		String baseLineTagVarName;
		String baseLineTagVarValue;
		String testDataTagVarValue;
		String testResult = "Passed";

		Map<String, String> baseLineTagVariableDataMap;
		Map<String, String> testDataTagVariableDataMap;
		WebAnalyticsPageData webAnalyticsPageDataBaseline;
		WebAnalyticsPageData webAnalyticsPageDataTestData;

		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		Map<String, WebAnalyticsTagData> testDataTagDataMap;

		
		int k = 0;
		for (SamePagesDataStore pds : samePagesDataStoreList) {
			AnalyticsDetails analyticDetails = new AnalyticsDetails();
			k++; // "page"+k;

			webAnalyticsPageDataBaseline = pds
					.getWebAnalyticsPageDataBaseline();
			webAnalyticsPageDataTestData = pds
					.getWebAnalyticsPageDataTestData();

			appName = webAnalyticsPageDataBaseline.getApplicationName();
			testAppName = webAnalyticsPageDataTestData.getApplicationName();

			baseLinePageTitle = webAnalyticsPageDataBaseline.getPageTitle();
			testDataPageTitle = webAnalyticsPageDataTestData.getPageTitle();

			baseLinePageUrl = webAnalyticsPageDataBaseline.getPageUrl();
			testDataPageUrl = webAnalyticsPageDataTestData.getPageUrl();

			baseLineHarFileName = webAnalyticsPageDataBaseline
					.getHarLogFileName();
			testDataHarFileName = webAnalyticsPageDataTestData
					.getHarLogFileName();

			baselineTagDataMap = webAnalyticsPageDataBaseline
					.getWebAnalyticsTagDataMap();
			testDataTagDataMap = webAnalyticsPageDataTestData
					.getWebAnalyticsTagDataMap();

			analyticDetails.setBaselineTagDataMap(baselineTagDataMap);
			analyticDetails.setCurrentTagDataMap(testDataTagDataMap);
			
			Iterator<String> baselineTagDataMapIterator = baselineTagDataMap
					.keySet().iterator();

			startTagRowPosition = currentTagVarRowNum;
			String baselineKey;
			int j = 0;
			int i = 0;

			while (baselineTagDataMapIterator.hasNext()) {
				j++;
				baselineKey = baselineTagDataMapIterator.next();
				baseLineTagName = baselineTagDataMap.get(baselineKey)
						.getTagName();
				testDataTagName = testDataTagDataMap.get(baselineKey)
						.getTagName();

				baseLineTagUrl = baselineTagDataMap.get(baselineKey)
						.getTagUrl();
				testDataTagUrl = testDataTagDataMap.get(baselineKey)
						.getTagUrl();

				// for each tag, loop through the params
				baseLineTagVariableDataMap = baselineTagDataMap
						.get(baselineKey).getTagVariableData();
				testDataTagVariableDataMap = testDataTagDataMap
						.get(baselineKey).getTagVariableData();

				Iterator tagVariableDataMapIterator = baseLineTagVariableDataMap
						.keySet().iterator();

				String baselineTagVariableKey;
				startVarRowPosition = currentTagVarRowNum;

				i = 0;
				while (tagVariableDataMapIterator.hasNext()) {


					baselineTagVariableKey = tagVariableDataMapIterator.next()
							.toString();

					baseLineTagVarName = baselineTagVariableKey;
					baseLineTagVarValue = baseLineTagVariableDataMap
							.get(baselineTagVariableKey);
					testDataTagVarValue = testDataTagVariableDataMap
							.get(baselineTagVariableKey);
					;

					if (baseLineTagName.equals("SiteCatalyst")
							&& baseLineTagVarName.equals("pageName")) {
						if (baseLineTagVarValue.startsWith(":")
								|| baseLineTagVarValue.endsWith(":")
								|| baseLineTagVarValue.equals("null")
								|| baseLineTagVarValue.equals("undefined")
								|| baseLineTagVarValue.equals(null)
								|| baseLineTagVarValue
										.equals("NoTagVarValueFound")
								|| baseLineTagVarValue.equals("")) {
							testResult = "Failed";
						} else {
							testResult = (baseLineTagVarValue
									.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
									: "Failed";
						}

					} // validate pageName

					else if ((baseLineTagVarValue.equals("undefined")
							|| baseLineTagVarValue.equals("null") ||
					baseLineTagVarValue.equals(""))) {
						testResult = "Failed";
					} // other sitecatalyst variables

					else {
						testResult = (baseLineTagVarValue
								.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
								: "Failed";
					} // all other tags and their variables.

					// this is to accomodate tags which dont have variables.
					// dont mark the results failed instead
					// nottagvariablevaluefound
					if (baseLineTagVarValue.toLowerCase().equals(
							WebAnalyticsConstants.noTagVariableValueFound)
							&& testDataTagVarValue
									.toLowerCase()
									.equals(WebAnalyticsConstants.noTagVariableValueFound)) {

						testResult = WebAnalyticsConstants.noTagVariableValueFound;// WebAnalyticsConstants.notApplicable;
					}

					if ( collectOnlyFailure && testResult.equals("Passed")) {
						continue;
					}
					
					i++;
					
					analyticDetails.setBaseLineAppName(appName);
					analyticDetails.setBaseLinePageUrl(baseLinePageUrl);
					analyticDetails.setCurrentRunAppName(testAppName);
					analyticDetails.setCurrentRunPageUrl(testDataPageUrl);
					analyticDetails.setPageTitle(baseLinePageTitle);
					
					//analyticDetails.setBaseLineHarFileName(baseLineHarFileName);
					//analyticDetails.setTestDataHarFileName(testDataHarFileName);
					
					analyticDetails.setExpectedTagName(baseLineTagName);
					analyticDetails.setActualTagName(testDataTagName);
					
					//analyticDetails.setBaseLineTagUrl(baseLineTagUrl);
					//analyticDetails.setTestDataTagUrl(testDataTagUrl);
					
					analyticDetails.setExpectedTagVarName(baseLineTagVarName);
					analyticDetails.setExpectedTagVarValue(baseLineTagVarValue);
					analyticDetails.setActualTagVarValue(testDataTagVarValue);
					analyticDetails.setTestResult(testResult);
					
					if(baseLinePageUrl == null){
						baseLinePageUrl = "valueNVL";
					}
					eachAnalyticsDetails.put(baseLinePageUrl, analyticDetails);
					
					analyticSummaryDetails.setEachAnalyticsDetails(eachAnalyticsDetails);
				}

			}
		}
		
		
		try {
			
			if(detailedReportsPath !=null && detailedReportsPath.length() > 0){
				logger.debug("Entry: AnalyticSummary - Detailed Objects");
				writeAnalyticSummaryDetailOrErrorObjects(analyticSummaryDetails, detailedReportsPath+File.separator + UiTestingConstants.ANALYTICS_SUMMARY_DETAILS_REPORTS);
				logger.debug("Exit: AnalyticSummary - Detailed Objects");
			}
			
			if(errorReportsPath !=null && errorReportsPath.length() > 0){
				logger.debug("Entry: AnalyticSummary - Error Objects");
				writeAnalyticSummaryDetailOrErrorObjects(analyticSummaryDetails, errorReportsPath+File.separator + UiTestingConstants.ANALYTICS_SUMMARY_ERROR_DETAILS_REPORTS);
				logger.debug("Exit: AnalyticSummary - Error Objects");
			}
			
			isObjCreated = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception while writing AnalyticSummaryDetailOrError Object...");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
				
		return isObjCreated;
	}
	
	public static boolean writeAnalyticSummaryDetailOrErrorObjects(AnalyticSummaryDetails analyticSummaryDetails, String objPath) {
		logger.debug("Entry: writeAnalyticSummaryDetailOrErrorObjects");
		boolean status = true;
		
		try{
			analyticSummaryDetails.setLastRecordedTime(DateTimeUtil.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));

			JsonReaderWriter<AnalyticSummaryDetails> jsonReaderWriter = new JsonReaderWriter<AnalyticSummaryDetails>();
			jsonReaderWriter.writeJsonObjectToFile(analyticSummaryDetails, objPath);
		}catch(Exception e){
			status = false;
			logger.error("Exception while writing AnalyticSummaryDetailOrError Object from file :"+objPath);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: writeAnalyticSummaryDetailOrErrorObjects");
		return status;
	}
}
