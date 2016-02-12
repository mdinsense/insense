package com.ensense.insense.data.analytics.common;

import com.ensense.insense.data.analytics.model.BrokenUrlData;
import com.ensense.insense.data.analytics.model.SamePagesBrokenUrlDataStore;
import com.ensense.insense.data.analytics.model.SamePagesDataStore;
import com.ensense.insense.data.analytics.model.WebAnalyticsPageData;
import com.ensense.insense.data.uitesting.model.ErrorType;
import com.ensense.insense.data.uitesting.model.TextImageReportData;
import com.ensense.insense.services.analytics.model.WebAnalyticsTagData;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AnalyticsDataReportingUtils {

	private String excelFileName;
	private String sheetName;
	private static Logger logger = Logger
			.getLogger(AnalyticsDataReportingUtils.class);

	private static final Map<String, Integer> tagTypeCountMap;
	static {
		tagTypeCountMap = new LinkedHashMap<String, Integer>();

		// Analytics Tags
		tagTypeCountMap.put("Gomez", new Integer(0));
		tagTypeCountMap.put("SiteCatalyst", new Integer(0));
		tagTypeCountMap.put("AddThis", new Integer(0));
		tagTypeCountMap.put("DoubleClick", new Integer(0));
		tagTypeCountMap.put("CoreMetrics", new Integer(0));
	}

	private static final Map<String, Integer> pagesForTagTypeCountMap;
	static {
		pagesForTagTypeCountMap = new LinkedHashMap<String, Integer>();

		// Analytics Tags
		tagTypeCountMap.put("Gomez", new Integer(0));
		tagTypeCountMap.put("SiteCatalyst", new Integer(0));
		tagTypeCountMap.put("AddThis", new Integer(0));
		tagTypeCountMap.put("DoubleClick", new Integer(0));
		tagTypeCountMap.put("CoreMetrics", new Integer(0));
	}

	// private static final String noMatchingTagFound="No-SupportedTag-Found";
	// private static final String noMatchingAnlyticsPageFound
	// ="No-Matching-page-Found";

	public AnalyticsDataReportingUtils(String excelFileName, String sheetName) {
		this.excelFileName = excelFileName;
		this.sheetName = sheetName;
	}

	public AnalyticsDataReportingUtils(){
		
	}
	public Map<String, AnalyticsSummaryReportData> writeWebAnalyticsAppsSummaryToExcel(
			List<SamePagesDataStore> samePagesDataStoreList,
			Map<String, BrokenUrlData> brokenUrlDataMap,
			Map<String, Integer> status200Map) {
		// String appName;
		Map<String, AnalyticsSummaryReportData> appsAnalyticsSummaryMap = new LinkedHashMap<String, AnalyticsSummaryReportData>();
		Set<String> noDupAppSet = new HashSet<String>();

		// identify the unique app names;
		for (SamePagesDataStore pds : samePagesDataStoreList) {
			String appNameNotFound = "ApplicationNameNotFound";
			if (pds.getWebAnalyticsPageDataBaseline().getApplicationName() == null) {
				noDupAppSet.add(appNameNotFound);
			} else {
				noDupAppSet.add(pds.getWebAnalyticsPageDataBaseline()
						.getApplicationName());
			}
		}
		logger.info("Total apps found :" + noDupAppSet.size());

		// create a map containing the applications analytics summary data
		for (String appName : noDupAppSet) {
			if (appName != null) {
				logger.info("App Found :" + appName);
				appsAnalyticsSummaryMap.put(
						appName,
						writeWebAnalyticsAppSummaryToExcel(
								samePagesDataStoreList, appName,
								brokenUrlDataMap, status200Map));
			}
		}
		return appsAnalyticsSummaryMap;
	}

	public AnalyticsSummaryReportData writeWebAnalyticsAppSummaryToExcel(
			List<SamePagesDataStore> samePagesDataStoreList,
			String appNameAudited, Map<String, BrokenUrlData> brokenUrlDataMap,
			Map<String, Integer> status200Map) {

		String appName = "";
		int baseLineAppCount = 0;
		int brokenURLCount = 0;
		int status200Count = 0;
		int baseLineTotalPagesCount = 0;
		int testDataTotalPagesCount = 0;

		int baseLinePageWithTagsCount = 0;
		int testDataPageWithTagsCount = 0;

		int baseLineSiteCatalystCount = 0;
		int testDataSiteCatalystCount = 0;
		int baseLinePageHasSCCount = 0;
		int testDataPageHasSCCount = 0;

		int baseLineGomezCount = 0;
		int testDataGomezCount = 0;
		int baseLinePageHasGomezCount = 0;
		int testDataPageHasGomezCount = 0;

		int baseLineAddThisCount = 0;
		int testDataAddThisCount = 0;
		int baseLinePageHasATCount = 0;
		int testDataPageHasATCount = 0;

		int baseLineDoubleClickCount = 0;
		int testDataDoubleClickCount = 0;
		int baseLinePageHasDCCount = 0;
		int testDataPageHasDCCount = 0;

		// google analytics
		int baseLineGoogleAnalyticsCount = 0;
		int testDataGoogleAnalyticsCount = 0;
		int baseLinePageHasGACount = 0;
		int testDataPageHasGACount = 0;

		// core metrics
		int baseLineCoreMetricsCount = 0;
		int testDataCoreMetricsCount = 0;
		int baseLinePageHasCoreMetricsCount = 0;
		int testDataPageHasCoreMetricsCount = 0;

		// opinion lab
		int baseLineOpinionLabCount = 0;
		int testDataOpinionLabCount = 0;
		int baseLinePageHasOLCount = 0;
		int testDataPageHasOLCount = 0;

		// com source
		int baseLineComScoreCount = 0;
		int testDataComScoreCount = 0;
		int baseLinePageHasCSCount = 0;
		int testDataPageHasCSCount = 0;

		// test and target
		int baseLineTestAndTargetCount = 0;
		int testDataTestAndTargetCount = 0;
		int baseLinePageHasTaTCount = 0;
		int testDataPageHasTaTCount = 0;

		int baseLinePagesWithNoSupportedTagsCount = 0;
		int testDataPagesWithNoSupportedTagsCount = 0;

		String baselineTagType;
		String testDataTagType;

		boolean scbFound = false;
		boolean gmbFound = false;
		boolean atbFound = false;
		boolean dcbFound = false;
		boolean gabFound = false;
		boolean cmbFound = false;
		boolean olbFound = false;
		boolean csbFound = false;
		boolean tatbFound = false;

		boolean sctFound = false;
		boolean gmtFound = false;
		boolean attFound = false;
		boolean dctFound = false;
		boolean gatFound = false;
		boolean cmtFound = false;
		boolean oltFound = false;
		boolean cstFound = false;
		boolean tattFound = false;
		BrokenUrlData brokenUrlData = null;

		/*******
		 * For Populating Values into DB with the values of the Summary view
		 * added by 303780
		 * 
		 * @param model
		 * @return
		 */
		Iterator it = status200Map.keySet().iterator();

		while (it.hasNext()) {
			String anAppName;
			anAppName = it.next().toString();
			if (anAppName.equals(appNameAudited)) {
				status200Count = status200Map.get(anAppName);
			}
		}
		for (Map.Entry<String, BrokenUrlData> entry : brokenUrlDataMap
				.entrySet()) {
			brokenUrlData = entry.getValue();
			if (brokenUrlData.getAppName().equals(appNameAudited)) {
				brokenURLCount++;
			}
		}
		/*******
		 * For Populating Values into DB with the values of the Summary view
		 * added by 303780
		 * 
		 * @param model
		 * @return
		 */

		// test the normalizedPage data
		for (SamePagesDataStore pds : samePagesDataStoreList) {
			/*
			 * if(!appName.equals(pds.getWebAnalyticsPageDataBaseline().
			 * getApplicationName())){
			 * if(!appName.equals("NoApplicationNameFound")){
			 * baseLineAppCount++; }
			 * appName=pds.getWebAnalyticsPageDataBaseline(
			 * ).getApplicationName(); }
			 */

			appName = pds.getWebAnalyticsPageDataBaseline()
					.getApplicationName();

			// count only for a specific app (passed from the calling method
			// System.out.println("appName :" + appName);
			// check why sometimes appname becomes null
			if (appName != null && appName.equals(appNameAudited)) {

				// total pages with atleast one supported tag
				// pds.getWebAnalyticsPageDataBaseline().getWebAnalyticsTagDataMap().get(noMatchingTagFound)
				// if(!pds.getWebAnalyticsPageDataBaseline().getPageTitle().equals(noMatchingTagFound)){
				if (pds.getWebAnalyticsPageDataBaseline()
						.getWebAnalyticsTagDataMap()
						.get(WebAnalyticsConstants.noMatchingTagFound) == null) {
					baseLinePageWithTagsCount++;
				} else { // pages having no supported tags
					baseLinePagesWithNoSupportedTagsCount++;
				}

				baseLineTotalPagesCount = baseLinePageWithTagsCount
						+ baseLinePagesWithNoSupportedTagsCount;

				// if(!pds.getWebAnalyticsPageDataTestData().getPageTitle().equals(noMatchingTagFound)){
				if (pds.getWebAnalyticsPageDataTestData()
						.getWebAnalyticsTagDataMap()
						.get(WebAnalyticsConstants.noMatchingTagFound) == null) {
					testDataPageWithTagsCount++;
				} else {
					testDataPagesWithNoSupportedTagsCount++;
				}

				testDataTotalPagesCount = testDataPageWithTagsCount
						+ testDataPagesWithNoSupportedTagsCount;

				scbFound = false;
				gmbFound = false;
				atbFound = false;
				dcbFound = false;
				gabFound = false;
				olbFound = false;
				gabFound = false;
				cmbFound = false;
				tatbFound = false;

				sctFound = false;
				gmtFound = false;
				attFound = false;
				dctFound = false;
				gatFound = false;
				oltFound = false;
				gatFound = false;
				tatbFound = false;

				Map<String, String> tagVariableDataMap;
				WebAnalyticsPageData webAnalyticsPageDataBaseline;
				WebAnalyticsPageData webAnalyticsPageDataTestData;

				webAnalyticsPageDataBaseline = pds
						.getWebAnalyticsPageDataBaseline();
				Iterator<String> baselineTagDataMapIterator = webAnalyticsPageDataBaseline
						.getWebAnalyticsTagDataMap().keySet().iterator();

				Map<String, WebAnalyticsTagData> baselineTagDataMap = webAnalyticsPageDataBaseline
						.getWebAnalyticsTagDataMap();

				String baselineKey;

				// System.out.println("Total Tags in Baseline :"
				// +webAnalyticsPageDataBaseline.getWebAnalyticsTagDataMap().size());
				// System.out.println("Page Title :"+webAnalyticsPageDataBaseline.getPageTitle());

				while (baselineTagDataMapIterator.hasNext()) {
					baselineKey = baselineTagDataMapIterator.next();
					baselineTagType = baselineTagDataMap.get(baselineKey)
							.getTagName();
					// System.out.println("Baseline Tag Data: "+baselineTagType);
					// System.out.println("Baseline Tag Key:" +
					// baselineTagDataMap.get(baselineKey).getTagDataKey());
					Iterator iterator = tagTypeCountMap.keySet().iterator();

					String key = null;

					if (baselineTagType.equals("SiteCatalyst")) {
						baseLineSiteCatalystCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineSiteCatalystCount++));

						if (!scbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasSCCount++;
							scbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("Gomez")) {
						baseLineGomezCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineGomezCount++));
						if (!gmbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasGomezCount++;
							gmbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("AddThis")) {
						baseLineAddThisCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineAddThisCount++));
						if (!atbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasATCount++;
							atbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("DoubleClick")) {
						baseLineDoubleClickCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!dcbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasDCCount++;
							dcbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("GoogleAnalytics")) {
						baseLineGoogleAnalyticsCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!gabFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasGACount++;
							gabFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("CoreMetrics")) {
						baseLineCoreMetricsCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!cmbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasCoreMetricsCount++;
							cmbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("comScore")) {
						baseLineComScoreCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!csbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasCSCount++;
							csbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("AdobeTestAndTarget")) {
						baseLineTestAndTargetCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!tatbFound) { // if page has a tag type atleast
											// once, incremennt the page count
											// for tag presence
							baseLinePageHasTaTCount++;
							tatbFound = true; // found atleast once
						}
					}

					if (baselineTagType.equals("OpinionLab")) {
						baseLineOpinionLabCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!olbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							baseLinePageHasOLCount++;
							olbFound = true; // found atleast once
						}
					}

				}

				webAnalyticsPageDataTestData = pds
						.getWebAnalyticsPageDataTestData();

				Iterator<String> testDataTagDataMapIterator = webAnalyticsPageDataTestData
						.getWebAnalyticsTagDataMap().keySet().iterator();
				Map<String, WebAnalyticsTagData> testDataTagDataMap = webAnalyticsPageDataTestData
						.getWebAnalyticsTagDataMap();

				String testDataKey;

				// System.out.println("Total Tags in TestData :" +
				// webAnalyticsPageDataTestData.getWebAnalyticsTagDataMap().size());
				// System.out.println("Test Data Page Title :"+webAnalyticsPageDataTestData.getPageTitle());
				while (testDataTagDataMapIterator.hasNext()) {
					testDataKey = testDataTagDataMapIterator.next();
					testDataTagType = testDataTagDataMap.get(testDataKey)
							.getTagName();
					// System.out.println("TestData Tag Data: "+testDataTagDataMap.get(testDataKey).getTagType());
					// System.out.println("TestData Tag Key:" +
					// testDataTagDataMap.get(testDataKey).getTagDataKey());
					Iterator iterator1 = tagTypeCountMap.keySet().iterator();

					// while(iterator1.hasNext()) {
					// key=iterator1.next().toString();
					// if(testDataTagType.equals(tagTypeCountMap.get(key))){
					if (testDataTagType.equals("SiteCatalyst")) {
						testDataSiteCatalystCount++;
						// tagTypeCountMap.put(key, new
						// Integer(testDataSiteCatalystCount++));

						if (!sctFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasSCCount++;
							sctFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("Gomez")) {
						testDataGomezCount++;
						// tagTypeCountMap.put(key, new
						// Integer(testDataGomezCount++));
						if (!gmtFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasGomezCount++;
							gmtFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("AddThis")) {
						testDataAddThisCount++;
						// tagTypeCountMap.put(key, new
						// Integer(testDataAddThisCount++));
						if (!attFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasATCount++;
							attFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("DoubleClick")) {
						testDataDoubleClickCount++;
						// tagTypeCountMap.put(key, new
						// Integer(testDataDoubleClickCount++));
						if (!dctFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasDCCount++;
							dctFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("GoogleAnalytics")) {
						testDataGoogleAnalyticsCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!gatFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasGACount++;
							gatFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("CoreMetrics")) {
						testDataCoreMetricsCount++;

						if (!cmtFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasCoreMetricsCount++;
							cmtFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("ComScore")) {
						testDataComScoreCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!csbFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasCSCount++;
							csbFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("TestAndTarget")) {
						testDataTestAndTargetCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!tatbFound) { // if page has a tag type atleast
											// once, incremennt the page count
											// for tag presence
							testDataPageHasTaTCount++;
							tatbFound = true; // found atleast once
						}
					}

					if (testDataTagType.equals("OpinionLab")) {
						testDataOpinionLabCount++;
						// tagTypeCountMap.put(key, new
						// Integer(baseLineDoubleClickCount++));
						if (!oltFound) { // if page has a tag type atleast once,
											// incremennt the page count for tag
											// presence
							testDataPageHasOLCount++;
							oltFound = true; // found atleast once
						}
					}

				}
				// System.out.println("\n");
			} // if appname matches only
		} // end for
			// summary report

		AnalyticsSummaryReportData asrd = new AnalyticsSummaryReportData();

		// System.out.println("Expected Data Stats");
		// System.out.println("Application Audited :" + appNameAudited);
		// System.out.println("baseLineAppCount :"+baseLineAppCount);
		// System.out.println("Total Non-Transactional Pages Audited :"
		// + baseLineTotalPagesCount);
		// System.out.println("Total Pages with No supported Tags :"
		// + baseLinePagesWithNoSupportedTagsCount);

		// System.out.println("Total Pages with SiteCatalyst :"
		// + baseLinePageHasSCCount);
		// System.out.println("Total Pages with Gomez :"
		// + baseLinePageHasGomezCount);
		// System.out.println("Total Pages with AddThis :"
		// + baseLinePageHasATCount);
		// System.out.println("Total Pages with DoubleClick :"
		// + baseLinePageHasDCCount);

		// System.out.println("Total Pages with GoogleAnalytics :"
		// + baseLinePageHasGACount);
		// System.out.println("Total Pages with CoreMetrics :"
		// + baseLinePageHasCoreMetricsCount);
		// System.out.println("Total Pages with OpinionLab :"
		// + baseLinePageHasOLCount);
		// System.out.println("Total Pages with ComScore :"
		// + baseLinePageHasCSCount);
		// System.out.println("Total Pages with TestAndTarget :"
		// + baseLinePageHasTaTCount);

		// System.out.println("Total times SiteCatalyst fired :"
		// + baseLineSiteCatalystCount);
		// System.out.println("Total times Gomez fired :" + baseLineGomezCount);
		// System.out
		// .println("Total times AddThis fired :" + baseLineAddThisCount);
		// System.out.println("Total times DoubleClick fired :"
		// + baseLineDoubleClickCount);
		// System.out.println("Total times GoogleAnalytics fired :"
		// + baseLineGoogleAnalyticsCount);
		// System.out.println("Total times CoreMetrics fired :"
		// + baseLineCoreMetricsCount);
		// System.out.println("Total times OpinionLab fired :"
		// + baseLineOpinionLabCount);
		// System.out.println("Total times ComScore fired :"
		// + baseLineComScoreCount);
		// System.out.println("Total times TestAndTarget fired :"
		// + baseLineTestAndTargetCount);

		/*
		 * //System.out.println("\n"); //System.out.println("Test Data Stats");
		 * //System.out.println("Total Non-Transactional Pages Audited :"+
		 * testDataTotalPagesCount);
		 * System.out.println("Total Pages with SiteCatalyst :"
		 * +testDataPageHasSCCount);
		 * System.out.println("Total Pages with Gomez :"
		 * +testDataPageHasGomezCount);
		 * System.out.println("Total Pages with AddThis :"
		 * +testDataPageHasATCount);
		 * System.out.println("Total Pages with DoubleClick :"
		 * +testDataPageHasDCCount);
		 * System.out.println("Total Pages with no supported Tags :"
		 * +testDataPagesWithNoSupportedTagsCount);
		 * System.out.println("Total times SiteCatalyst fired :"
		 * +testDataSiteCatalystCount);
		 * System.out.println("Total times Gomez fired :"+testDataGomezCount);
		 * System
		 * .out.println("Total times AddThis fired :"+testDataAddThisCount);
		 * System
		 * .out.println("Total times DoubleClick fired :"+testDataDoubleClickCount
		 * );
		 */
		// System.out.println("\n");

		asrd.setStatus200Count(status200Count);
		asrd.setBrokenURLCount(brokenURLCount);
		asrd.setAppNameAudited(appNameAudited);
		asrd.setBaseLinePageCount(baseLinePageWithTagsCount);
		asrd.setTestDataPageCount(testDataPageWithTagsCount);

		asrd.setBaseLineSiteCatalystCount(baseLineSiteCatalystCount);
		asrd.setBaseLineGomezCount(baseLineGomezCount);
		asrd.setBaseLineAddThisCount(baseLineAddThisCount);
		asrd.setBaseLineDoubleClickCount(baseLineDoubleClickCount);
		asrd.setBaseLineCoreMetricsCount(baseLineCoreMetricsCount);
		asrd.setBaseLineGoogleAnalyticsCount(baseLineGoogleAnalyticsCount);

		asrd.setBaseLinePageHasSCCount(baseLinePageHasSCCount);
		asrd.setBaseLinePageHasGomezCount(baseLinePageHasGomezCount);
		asrd.setBaseLinePageHasATCount(baseLinePageHasATCount);
		asrd.setBaseLinePageHasDCCount(baseLinePageHasDCCount);
		asrd.setBaseLinePageHasGACount(baseLinePageHasGACount);
		asrd.setBaseLinePageHasCoreMetricsCount(baseLinePageHasCoreMetricsCount);

		asrd.setTestDataSiteCatalystCount(testDataSiteCatalystCount);
		asrd.setTestDataGomezCount(testDataGomezCount);
		asrd.setTestDataAddThisCount(testDataAddThisCount);
		asrd.setTestDataDoubleClickCount(testDataDoubleClickCount);
		asrd.setTestDataGoogleAnalyticsCount(testDataGoogleAnalyticsCount);
		asrd.setTestDataCoreMetricsCount(testDataCoreMetricsCount);

		asrd.setTestDataPageHasSCCount(testDataPageHasSCCount);
		asrd.setTestDataPageHasGomezCount(testDataPageHasGomezCount);
		asrd.setTestDataPageHasATCount(testDataPageHasATCount);
		asrd.setTestDataPageHasDCCount(testDataPageHasDCCount);
		asrd.setTestDataPageHasCoreMetricsCount(testDataPageHasCoreMetricsCount);
		asrd.setTestDataPageHasCSCount(testDataPageHasCSCount);
		asrd.setTestDataPageHasGACount(testDataPageHasGACount);

		asrd.setBaseLinePagesWithNoSupportedTagsCount(baseLinePagesWithNoSupportedTagsCount);
		asrd.setTestDataPagesWithNoSupportedTagsCount(testDataPagesWithNoSupportedTagsCount);
		asrd.setTestDataComScoreCount(testDataComScoreCount);
		asrd.setTestDataGoogleAnalyticsCount(testDataGoogleAnalyticsCount);
		asrd.setTestDataCoreMetricsCount(testDataCoreMetricsCount);
		asrd.setTestDataAddThisCount(testDataAddThisCount);

		return asrd;
	}

	public void writeBrandingReportDetailsToExcel(
			Map<String, TextImageReportData> brandingReportMap,
			String excelReportFilePath, String tempDirectory) {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		Cell cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18, cell19;

		// wb.setCompressTempFiles(true); // temp files will be gzipped
		System.setProperty("java.io.tmpdir", tempDirectory);
		Sheet sheet1 = wb.createSheet("Branding Report Data");
		Sheet sheet = writeBrandingReportHeader(wb, sheet1);

		Iterator brandingReportIterator = brandingReportMap.keySet().iterator();
		int currentRowNum = 1;
		String key;
		TextImageReportData value;

		while (brandingReportIterator.hasNext()) {
			row = sheet.createRow(currentRowNum++);
			key = brandingReportIterator.next().toString();
			value = brandingReportMap.get(key);

			// column 1 - urlNo
			cell0 = row.createCell(0);
			cell0.setCellValue(value.getUrlNo());

			// column 2 - Appname
			cell1 = row.createCell(1);
			cell1.setCellValue(value.getAppName());

			// insert column here for testDataPageName;

			// column 3 -RefererUrl
			cell2 = row.createCell(2);//
			//cell2.setCellValue(value.getRefererUrl());

			// column 4 -URL Matched
			cell3 = row.createCell(3);//
			cell3.setCellValue(value.getUrlMatched());

			// column 5 - PageTitle
			cell4 = row.createCell(4);//
			cell4.setCellValue(value.getPageTitle());

			// column 6 - ErrorPage
			cell5 = row.createCell(5);//
			cell5.setCellValue(value.getErrorpage());

			// column 7 - BaselineAllImageCount
			cell6 = row.createCell(6);//
			//cell6.setCellValue(value.getBaselineAllImageCount());

			// column 8 - AllImageCount
			cell7 = row.createCell(7);//
			cell7.setCellValue(value.getAllImageCount());

			// column 9 - BaseLineTiaaImageCount
			cell8 = row.createCell(8);//
			//cell8.setCellValue(value.getBaselineTiaaImageCount());

			// column 10 - TiaaImageCount
			cell9 = row.createCell(9);
			cell9.setCellValue(value.getTiaaImageCount());

			// column 11 - BaseLineTiaaImageUrls
			cell10 = row.createCell(10);//
			//cell10.setCellValue(value.getBaselineTiaaImageUrls().toString());

			// column 12 - TiaaImageUrls
			cell11 = row.createCell(11);//
			cell11.setCellValue(value.getTiaaImageUrls().toString());

			// column 13 - BaseLineTiaaCrefStringCount
			cell12 = row.createCell(12);//
			//cell12.setCellValue(value.getBaselineTiaaCrefStringCount());

			// column 14 - TiaaCrefStringCount
			cell13 = row.createCell(13);//
			cell13.setCellValue(value.getTiaaCrefStringCount());

			// column 15 - BaseLineTiaaCount
			cell14 = row.createCell(14);//
			//cell14.setCellValue(value.getBaselineTiaaStringCount());

			// column 16 - TiaaStringCount
			cell15 = row.createCell(15);//
			cell15.setCellValue(value.getTiaaStringCount());

			// column 17 - Textmatch
			cell16 = row.createCell(16);//
			cell16.setCellValue(value.getPercentageTextMatch());

			// column 18 - wordmatch
			cell17 = row.createCell(17);//
			cell17.setCellValue(value.getPercentageWordMatch());

			// column 19 - wordmatch
			cell18 = row.createCell(18);//
			cell18.setCellValue(value.getPercentageSentenceMatch());

			// column 19 - wordmatch
			cell19 = row.createCell(19);//
			cell19.setCellValue(value.getLevenshteinDistance());
		}

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			fileOut.close();
			wb.dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeBrokenUrlAuditDetailsToExcel(List<SamePagesBrokenUrlDataStore> samePagesBrokenUrlDataStoreList, String brokenUrlAuditExcelFilePath, String tempDirectory) {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		boolean writePageloadTimeAttributes = true;

		// wb.setCompressTempFiles(true); // temp files will be gzipped
		System.setProperty("java.io.tmpdir", tempDirectory); // location
																// of
																// temporary
																// files
		Sheet sheet1 = wb.createSheet("Broken Url Audit Data");
		Sheet sheet = writeBrokenUrlAuditDetailsHeader(wb, sheet1); // sheet with
																// header row
	
		int currentRowNum = 1;
		int startVarRowPosition = 0;
		
		for ( SamePagesBrokenUrlDataStore samePagesBrokenUrlDataStore :samePagesBrokenUrlDataStoreList){
			BrokenUrlData currentRunBrokenUrlData = samePagesBrokenUrlDataStore.getCurentRunBrokenUrlData();
			BrokenUrlData baselineBrokenUrlData = samePagesBrokenUrlDataStore.getBaselineBrokenUrlData();
			
			startVarRowPosition = currentRowNum;
			
			for (Map.Entry<String, Long> entry : baselineBrokenUrlData.getPageLoadAttributes().entrySet()) {
				String baselineAttributeName = entry.getKey();
			    long baselineAttributeValue = entry.getValue();
			    
			    long currentRunAttributeValue = currentRunBrokenUrlData.getPageLoadAttributes().get(baselineAttributeName);
			    
			    row = sheet.createRow(currentRowNum++);
				brokenUrlAuditExcelRow(currentRunBrokenUrlData, baselineBrokenUrlData, row, wb, writePageloadTimeAttributes, baselineAttributeName, baselineAttributeValue, currentRunAttributeValue);
			}
			
			for ( int i=0; i < 10; i++ ){
				sheet.addMergedRegion(new CellRangeAddress(
						startVarRowPosition, // first
												// row
												// (0-based)
						currentRowNum - 1, // last row (0-based)
						i, // first column (0-based)
						i // last column (0-based)
				));
			}
		}
		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(brokenUrlAuditExcelFilePath);
			wb.write(fileOut);
			fileOut.close();
			wb.dispose(); // dispose temporary files created
			// System.out.println("Succesfully created Mint Audit Report "
			// + excelReportFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	private Sheet writeBrokenUrlAuditDetailsHeader(SXSSFWorkbook wb,
			Sheet sheet) {
		try{
			CellStyle style = createHeaderCellStyle(wb);
	
			Row row = sheet.createRow(0);
	
			int cellnum = 0;
			int temp;
	
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue("AppName");
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline PageUrl");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Current PageUrl");
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline Page Title");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Current Page Title");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline HttpStatusCode");
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue("Current HttpStatusCode");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline HttpRequestMethod");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline HttpRequestMethod");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Ajax Call Present");
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue("Pageload Attribute Name");
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue("Baseline Times");
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue("Current Run Times");
			cell.setCellStyle(style);
		}catch(Exception e){
			System.out.println("Exception while writing header :"+e);
			e.printStackTrace();
		}
		
		return sheet;
	}

	public void writeBrokenUrlDetailsToExcel(
			Map<String, BrokenUrlData> brokenUrlDataMap,
			String excelReportFilePath, String tempDirectory, boolean generateReportsOnlyForRefererUrl, boolean generatePageLoadTimeAttribute, boolean beautify) {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		

		// wb.setCompressTempFiles(true); // temp files will be gzipped
		System.setProperty("java.io.tmpdir", tempDirectory); // location
																// of
																// temporary
																// files
		Sheet sheet1 = wb.createSheet("Broken Url Data");
		Sheet sheet = writeBrokenUrlDetailsHeader(wb, sheet1); // sheet with
																// header row

		Iterator brokenUrlDatIterator = brokenUrlDataMap.keySet().iterator();
		int currentRowNum = 1;
		String key;
		BrokenUrlData value;
		int startVarRowPosition = 0;
		
		while (brokenUrlDatIterator.hasNext()) {
			
			key = brokenUrlDatIterator.next().toString();
			value = brokenUrlDataMap.get(key);
			// column 0 - appName

			Map<String, Long> pageLoadAttributes = value.getPageLoadAttributes();
			
			if ( generateReportsOnlyForRefererUrl && generatePageLoadTimeAttribute){
				startVarRowPosition = currentRowNum;
				for (Map.Entry<String, Long> entry : pageLoadAttributes.entrySet()) {
				    String attributeName = entry.getKey();
				    long attributeValue = entry.getValue();
				   
				   row = sheet.createRow(currentRowNum++);
				   brokenUrlExcelRow(value, row, wb, generatePageLoadTimeAttribute, attributeName, attributeValue);
				   
				}
				if (beautify) {
					for ( int i=0; i < 13 ; i++ ){
						sheet.addMergedRegion(new CellRangeAddress(
								startVarRowPosition, // first
														// row
														// (0-based)
								currentRowNum - 1, // last row (0-based)
								i, // first column (0-based)
								i // last column (0-based)
						));
					}
				}
			}else{
				row = sheet.createRow(currentRowNum++);
				brokenUrlExcelRow(value, row, wb, generatePageLoadTimeAttribute, null, 0);
			}
		}

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			fileOut.close();
			wb.dispose(); // dispose temporary files created
			// System.out.println("Succesfully created Mint Audit Report "
			// + excelReportFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void brokenUrlAuditExcelRow(BrokenUrlData currentRunBrokenUrlData, BrokenUrlData baselineBrokenUrlData, Row row, SXSSFWorkbook wb, 
			boolean writePageloadTimeAttributes, String baselineAttributeName, long baselineAttributeValue, long currentrunAttributeValue) {
		
		try{
			Cell cell;
			int cellnum = 0;
			CellStyle style_failed_red = createGenericAltCellStyle(wb,
					IndexedColors.RED.getIndex());
			CellStyle style_passed_green = createGenericAltCellStyle(wb,
					IndexedColors.GREEN.getIndex());
			CellStyle style_passed_white = createGenericAltCellStyle(wb,
					IndexedColors.WHITE.getIndex());
			
			CellStyle style = style_passed_white;
			
			/*if (currentRunBrokenUrlData.getStatusCode() < 400 && !currentRunBrokenUrlData.isErrorPage()) {
				style = style_passed_green;
			} else if (currentRunBrokenUrlData.isErrorPage()) {
				style = style_failed_red;
			} else {
				currentRunBrokenUrlData.setErrorPage(true);
				currentRunBrokenUrlData.setErrorType(ErrorType.HTTP_STATUS.toString());
				style = style_failed_red;
			}*/
	
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getAppName());
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineBrokenUrlData.getResourceUrl());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getResourceUrl());
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineBrokenUrlData.getPageTitle());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getPageTitle());
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineBrokenUrlData.getStatusCode());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getStatusCode());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineBrokenUrlData.getRequestMethod());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getRequestMethod());
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentRunBrokenUrlData.getAjaxCallMade());
			cell.setCellStyle(style);
	
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineAttributeName);
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(baselineAttributeValue);
			cell.setCellStyle(style);
			
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentrunAttributeValue);
			cell.setCellStyle(style);
		}catch(Exception e){
			System.out.println("Exception while writing row :"+e);
			e.printStackTrace();
		}
	}
	
	private void brokenUrlExcelRow(BrokenUrlData value, Row row, SXSSFWorkbook wb, boolean writePageloadTimeAttributes, String attributeName, long attributeValue) {
		Cell cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10, cell11, cell12, cell13, cell14;
		CellStyle style_failed_red = createGenericAltCellStyle(wb,
				IndexedColors.RED.getIndex());
		CellStyle style_passed_green = createGenericAltCellStyle(wb,
				IndexedColors.GREEN.getIndex());
		CellStyle style;
		
		if (value.getStatusCode() < 400 && !value.isErrorPage()) {
			style = style_passed_green;
		} else if (value.isErrorPage()) {
			style = style_failed_red;
		} else {
			value.setErrorPage(true);
			value.setErrorType(ErrorType.HTTP_STATUS.toString());
			style = style_failed_red;
		}

		cell0 = row.createCell(0);
		cell0.setCellValue(value.getAppName());
		cell0.setCellStyle(style);

		// column 1 -PageName/Title
		cell1 = row.createCell(1);
		cell1.setCellValue(value.getResourceUrl());
		cell1.setCellStyle(style);

		// column 2 - errorPage
		cell2 = row.createCell(2);//

		if (value.isErrorPage()) {
			cell2.setCellValue("True");
		} else {
			cell2.setCellValue("False");
		}
		cell2.setCellStyle(style);
		// insert column here for testDataPageName;

		// column 3 -error type
		cell3 = row.createCell(3);//
		cell3.setCellValue(value.getErrorType());
		cell3.setCellStyle(style);

		// column 4 - parentUrl
		cell4 = row.createCell(4);//
		cell4.setCellValue(value.getParentUrl());
		cell4.setCellStyle(style);

		// column 5 - linkName
		cell5 = row.createCell(5);//
		cell5.setCellValue(value.getLinkName());
		cell5.setCellStyle(style);

		// column 6 - navigation path
		cell6 = row.createCell(6);//
		cell6.setCellValue(value.getNavigationPath());
		cell6.setCellStyle(style);

		// column 7 - refererUrl
		cell7 = row.createCell(7);//
		cell7.setCellValue(value.getRefererUrl());
		cell7.setCellStyle(style);

		// column 8 - page title
		cell8 = row.createCell(8);//
		cell8.setCellValue(value.getPageTitle());
		cell8.setCellStyle(style);

		// column 9 -StatusCode
		cell9 = row.createCell(9);//
		cell9.setCellValue(value.getStatusCode());
		cell9.setCellStyle(style);

		// column 10 -StatusText
		cell10 = row.createCell(10);//
		cell10.setCellValue(value.getStatusText());
		cell10.setCellStyle(style);

		// column 3 - baselineHarFileName
		cell11 = row.createCell(11);//
		cell11.setCellValue(value.getRequestMethod());
		cell11.setCellStyle(style);

		cell12 = row.createCell(12);//
		cell12.setCellValue(value.getAjaxCallMade());
		cell12.setCellStyle(style);

		//cell13 = row.createCell(13);//
		//cell13.setCellValue(value.getHarFileName());
		//cell13.setCellStyle(style);
		
		if ( writePageloadTimeAttributes ){
			cell13 = row.createCell(13);//
			cell13.setCellValue(attributeName);
			cell13.setCellStyle(style);
			
			cell13 = row.createCell(14);//
			cell13.setCellValue(attributeValue);
			cell13.setCellStyle(style);
		}
		
	}

	public void writeAuditAndTestDetailsToExcel(
			List<SamePagesDataStore> samePagesDataStoreList,
			String excelReportFilePath, String tempDirectory, boolean beautify,
			String baselineRunDate, String currentRunDate, boolean collectOnlyFailure) {
		// http://poi.apache.org/spreadsheet/how-to.html#sxssf
		// keep 100 rows in memory, exceeding rows will be flushed to disk
		SXSSFWorkbook wb = new SXSSFWorkbook();
		// wb.setCompressTempFiles(true); // temp files will be gzipped
		// System.out.println("AnalyticsDataReportingUtils teampDirectory :"+teampDirectory);
		System.setProperty("java.io.tmpdir", tempDirectory); // location
																// of
																// temporary
																// files

		// Sheet sheet0 = wb.createSheet("Analytics Summary");

		Sheet sheet1 = wb.createSheet("Web Analytics Data");
		Sheet sheet = writeAuditTestDetailsHeader(wb, sheet1, baselineRunDate,
				currentRunDate); // sheet with
		// header row

		// int totalPages=3;

		// int totalTags=3;
		// int totalTagVars=5;

		// int pageRowNum=0;
		// int tagRowNum=0;

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

		// String tagName;

		// String pageTitle="HOME Page";

		String baseLineTagName;// ="baseLineTagName";
		String testDataTagName;// ="testDataTagName";
		String baseLineTagUrl;// ="baseLineTagUrl";
		String testDataTagUrl;// ="testDataTagUrl";
		String baseLineTagVarName;
		String baseLineTagVarValue;
		String testDataTagVarValue;
		String testResult = "Passed";

		Row row;

		Map<String, String> baseLineTagVariableDataMap;
		Map<String, String> testDataTagVariableDataMap;
		WebAnalyticsPageData webAnalyticsPageDataBaseline;
		WebAnalyticsPageData webAnalyticsPageDataTestData;

		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		Map<String, WebAnalyticsTagData> testDataTagDataMap;

		CellStyle style, style_gold, style_light_turquoise, style_failed_red, style_passed_green;
		CellStyle style_hidden;
		Cell cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10, cell11, cell12, cell13, cell14;

		// style=wb.createCellStyle();
		// style_hidden = wb.createCellStyle();
		style_gold = createGenericAltCellStyle(wb,
				IndexedColors.GOLD.getIndex());
		style_light_turquoise = createGenericAltCellStyle(wb,
				IndexedColors.LIGHT_TURQUOISE.getIndex());
		style_failed_red = createGenericAltCellStyle(wb,
				IndexedColors.RED.getIndex());
		style_passed_green = createGenericAltCellStyle(wb,
				IndexedColors.GREEN.getIndex());

		// int totalPages=samePagesDataStoreList.size();

		// for(int k=0 ;k < totalPages;k++){ //page iterator

		int k = 0;
		for (SamePagesDataStore pds : samePagesDataStoreList) {
			
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

			//baselineTagDataMapForSiteCatalyst.put("hi", baselineTagDataMap);
			
			Iterator<String> baselineTagDataMapIterator = baselineTagDataMap
					.keySet().iterator();

			startTagRowPosition = currentTagVarRowNum;
			String baselineKey;
			int j = 0;
			int i = 0;
			// System.out.println("baselineTagDataMap size :" +
			// baselineTagDataMap.size());
			/*
			 * Iterator it1=baselineTagDataMap.keySet().iterator();
			 * while(it1.hasNext()){
			 * System.out.println("tag type :"+baselineTagDataMap
			 * .get(it1.next()).getTagType()); }
			 */

			// for(int j=0 ;j < totalTags;j++){ //tags iterator
			while (baselineTagDataMapIterator.hasNext()) {
				j++;
				baselineKey = baselineTagDataMapIterator.next();
				// tagName="tag"+j;
				baseLineTagName = baselineTagDataMap.get(baselineKey)
						.getTagName();
				testDataTagName = testDataTagDataMap.get(baselineKey)
						.getTagName();

				baseLineTagUrl = baselineTagDataMap.get(baselineKey)
						.getTagUrl();
				testDataTagUrl = testDataTagDataMap.get(baselineKey)
						.getTagUrl();

				// System.out.println("Baseline Tag Data: "+baselineTagDataMap.get(baselineKey).getTagType());
				// System.out.println("Baseline Tag Key:" +
				// baselineTagDataMap.get(baselineKey).getTagDataKey());

				// for each tag, loop through the params
				baseLineTagVariableDataMap = baselineTagDataMap
						.get(baselineKey).getTagVariableData();
				testDataTagVariableDataMap = testDataTagDataMap
						.get(baselineKey).getTagVariableData();

				Iterator tagVariableDataMapIterator = baseLineTagVariableDataMap
						.keySet().iterator();
				// baseLineTagVarName=tagName;
				String baselineTagVariableKey;
				// tagRowNum++;
				startVarRowPosition = currentTagVarRowNum;

				i = 0;
				// for(int i=0;i<totalTagVars;i++){

				// System.out.println("baseLineTagVariableDataMap size :" +
				// baseLineTagVariableDataMap.size());
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
					// baseLineTagVarValue.equals("NoTagVarValueFound") ||
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
					
					
					// Styling start
					// style differently for odd and even pages

					// if even gold, else green
					if (k % 2 == 0) {
						style = style_gold;// createGenericAltCellStyle(wb,IndexedColors.GOLD.getIndex());
						// style.setFillBackgroundColor(IndexedColors.GOLD.getIndex());
						// style.setFillPattern(CellStyle.SPARSE_DOTS);
					} else {
						style = style_light_turquoise;// createGenericAltCellStyle(wb,IndexedColors.LIGHT_TURQUOISE.getIndex());
						// style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
						// style.setFillPattern(CellStyle.SPARSE_DOTS);
					}

					// style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					// style.setBorderBottom( CellStyle.BORDER_MEDIUM);
					// style.setBorderLeft(CellStyle.BORDER_MEDIUM);
					// style.setBorderRight(CellStyle.BORDER_MEDIUM);
					// style.setBorderTop(CellStyle.BORDER_MEDIUM);

					// hidden style
					// if even gold, else green
					/*
					 * if(k % 2 == 0) {
					 * style_hidden.setFillBackgroundColor(IndexedColors
					 * .GOLD.getIndex());
					 * style_hidden.setFillPattern(CellStyle.SPARSE_DOTS); }
					 * else{
					 * style_hidden.setFillBackgroundColor(IndexedColors.GREEN
					 * .getIndex());
					 * style_hidden.setFillPattern(CellStyle.SPARSE_DOTS); }
					 * style_hidden
					 * .setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					 * style_hidden.setBorderBottom( CellStyle.BORDER_MEDIUM);
					 * style_hidden.setBorderLeft(CellStyle.BORDER_MEDIUM);
					 * style_hidden.setBorderRight(CellStyle.BORDER_MEDIUM);
					 * style_hidden.setBorderTop(CellStyle.BORDER_MEDIUM);
					 */
					// style_hidden.setHidden(true);

					// Styling end

					// row = sheet.createRow((short) currentTagVarRowNum++);
					row = sheet.createRow(currentTagVarRowNum++);

					// appname > pageName/Title > pageUrl> baselineHarFileName >
					// testDataHarFileName > baselineTagName > baselineTagUrl >
					// testDataTagName > testDataTagUrl > baselineTagVarName >
					// baselineTagVarValue > testDataTagVarValue > testResult

					// column 0 - appName
					cell0 = row.createCell(0);
					cell0.setCellValue(appName);
					cell0.setCellStyle(style);

					cell1 = row.createCell(1);
					cell1.setCellValue(testAppName);
					cell1.setCellStyle(style);

					// column 1 -PageName/Title
					cell2 = row.createCell(2);
					cell2.setCellValue(baseLinePageTitle);
					cell2.setCellStyle(style);

					// insert column here for testDataPageName;

					// column 2 -PageUrl
					cell3 = row.createCell(3);//
					cell3.setCellValue(baseLinePageUrl);
					cell3.setCellStyle(style);

					// column 2 -PageUrl
					cell4 = row.createCell(4);//
					cell4.setCellValue(testDataPageUrl);
					cell4.setCellStyle(style);

					// column 3 - baselineHarFileName
					if (beautify) {
						cell5 = row.createCell(5);//
						cell5.setCellValue(baseLineHarFileName);
						cell5.setCellStyle(style);
					}

					// column 4 - testData HarFileName
					if (beautify) {
						cell6 = row.createCell(6);//
						cell6.setCellValue(testDataHarFileName);
						cell6.setCellStyle(style);
					}
					// column 5 - baselineTagName

					cell7 = row.createCell(7);
					cell7.setCellValue(baseLineTagName);
					// cell3.setCellValue("v"+i);
					cell7.setCellStyle(style);

					// column 6 - testDataTagName

					cell8 = row.createCell(8);
					cell8.setCellValue(testDataTagName);
					cell8.setCellStyle(style);

					// column 7 -baseLineTagUrl

					if (beautify) {
						cell9 = row.createCell(9);
						cell9.setCellValue(baseLineTagUrl);
						cell9.setCellStyle(style);
					}
					// column 8 -testDataTagUrl
					if (beautify) {
						cell10 = row.createCell(10);
						cell10.setCellValue(testDataTagUrl);
						cell10.setCellStyle(style);
					}
					// column 9 -baseLineTagVarName same as testDataTagVarName

					cell11 = row.createCell(11);
					// baseLineTagVarName="v"+i;
					cell11.setCellValue(baseLineTagVarName);
					cell11.setCellStyle(style);

					// column 10 -baseLineTagVarName same as testDataTagVarName

					cell12 = row.createCell(12);
					// baseLineTagVarValue="v"+i+"Value";
					cell12.setCellValue(baseLineTagVarValue);
					cell12.setCellStyle(style);

					// column 11 -baseLineTagVarName same as testDataTagVarName

					cell13 = row.createCell(13);
					// testDataTagVarValue="v"+i+"Value";
					cell13.setCellValue(testDataTagVarValue);
					cell13.setCellStyle(style);

					// column 12 -baseLineTagVarName same as testDataTagVarName

					cell14 = row.createCell(14);

					cell14.setCellValue(testResult);
					
				}

				/*
				 * int startVarRowPositionNormForZero=0;
				 * if(startVarRowPosition!=0){
				 * startVarRowPositionNormForZero=startVarRowPosition+1; }
				 */
				// System.out.println("TagMerge :startVarRowPosition :"+(startVarRowPosition));
				// System.out.println("TagMerge :currentTagVarRowNum :"+(currentTagVarRowNum-1));
				// System.out.println("\n");
				if ( !collectOnlyFailure || i > 0 ) {
					if (beautify) {
						// column 7 -merge baseLineTagUrl
						sheet.addMergedRegion(new CellRangeAddress(
								startVarRowPosition, // first
														// row
														// (0-based)
								currentTagVarRowNum - 1, // last row (0-based)
								7, // first column (0-based)
								7 // last column (0-based)
						));
	
						// column 8 -merge testDataTagUrl
						sheet.addMergedRegion(new CellRangeAddress(
								startVarRowPosition, // first
														// row
														// (0-based)
								currentTagVarRowNum - 1, // last row (0-based)
								8, // first column (0-based)
								8 // last column (0-based)
						));
						
						// column 9 -merge testDataTagUrl
						sheet.addMergedRegion(new CellRangeAddress(
								startVarRowPosition, // first
														// row
														// (0-based)
								currentTagVarRowNum - 1, // last row (0-based)
								9, // first column (0-based)
								9 // last column (0-based)
						));
						
						// column 10 -merge testDataTagUrl
						sheet.addMergedRegion(new CellRangeAddress(
								startVarRowPosition, // first
														// row
														// (0-based)
								currentTagVarRowNum - 1, // last row (0-based)
								10, // first column (0-based)
								10 // last column (0-based)
						));
					}
				}
			}

			// System.out.println("PageMerge :startVarRowPosition :"+(startTagRowPosition));
			// System.out.println("PageMerge :currentTagVarRowNum :"+(currentTagVarRowNum-1));
			// System.out.println("\n");
			// column 0 -merge appName
			
			if ( !collectOnlyFailure || i > 0 ) {
				if (beautify) {
					sheet.addMergedRegion(new CellRangeAddress(startTagRowPosition, // first
																					// row
																					// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							0, // first column (0-based)
							0 // last column (0-based)
					));
	
					// column 1 -merge pageName
					sheet.addMergedRegion(new CellRangeAddress(startTagRowPosition, // first
																					// row
																					// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							1, // first column (0-based)
							1 // last column (0-based)
					));
	
					// column 2 -merge pageUrl
					sheet.addMergedRegion(new CellRangeAddress(startTagRowPosition, // first
																					// row
																					// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							2, // first column (0-based)
							2 // last column (0-based)
					));
	
					// column 3 -merge baseline HarName
					sheet.addMergedRegion(new CellRangeAddress(startTagRowPosition, // first
																					// row
																					// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							3, // first column (0-based)
							3 // last column (0-based)
					));
	
					// column 4 -merge testData Har Name
					sheet.addMergedRegion(new CellRangeAddress(startTagRowPosition, // first
																					// row
																					// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							4, // first column (0-based)
							4 // last column (0-based)
					));
					// column 5 -merge baseLineTagName - tag type
					sheet.addMergedRegion(new CellRangeAddress(
							startTagRowPosition, // first
													// row
													// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							5, // first column (0-based)
							5 // last column (0-based)
					));

					// column 6 -merge testDataTagName -tag type
					sheet.addMergedRegion(new CellRangeAddress(
							startTagRowPosition, // first
													// row
													// (0-based)
							currentTagVarRowNum - 1, // last row (0-based)
							6, // first column (0-based)
							6 // last column (0-based)
					));
				}
				// System.out.println("written data to Excel page# " + k);
				// flush the rows to disk
				try {
					((SXSSFSheet) sheet).flushRows(300);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/*
		 * 
		 * 
		 * 
		 * 
		 * sheet.addMergedRegion(new CellRangeAddress( 0, //first row (0-based)
		 * 4, //last row (0-based) 0, //first column (0-based) 0 //last column
		 * (0-based) ));
		 * 
		 * 
		 * 
		 * 
		 * // row1.createCell(2).setCellValue("v2"); //
		 * row1.createCell(3).setCellValue("v2Value"); /*
		 * sheet.createRow(2).createCell(2).setCellValue("v3");
		 * sheet.createRow(2).createCell(3).setCellValue("v3Value");
		 * 
		 * sheet.createRow(3).createCell(2).setCellValue("v4");
		 * sheet.createRow(3).createCell(3).setCellValue("v4Value");
		 */

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			fileOut.close();
			wb.dispose(); // dispose temporary files created
			// System.out.println("Succesfully created Mint Audit Report "
			// + excelReportFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// alternate the colors for alternate pages
	private CellStyle createGenericAltCellStyle(SXSSFWorkbook wb, short color) {
		CellStyle style = null;

		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());

		style = wb.createCellStyle();
		style.setFillForegroundColor(color);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);

		return style;
	}

	private CellStyle createHeaderCellStyle(SXSSFWorkbook wb) {

		CellStyle style = wb.createCellStyle();

		// style.setFillBackgroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		// style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		// style.setFillPattern(CellStyle.SPARSE_DOTS);
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);

		return style;
	}

	private Sheet writeBrandingReportHeader(SXSSFWorkbook wb, Sheet sheet) {
		CellStyle style = createHeaderCellStyle(wb);

		Row row = sheet.createRow(0);

		int cellnum = 0;
		int temp;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("URL No");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("AppName");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("RefererUrl");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("URL Matched");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PageTitle");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ErrorPage");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("BaselineAllImageCount");
		cell.setCellStyle(style);

		/*
		 * cell = row.createCell(cellnum++); cell.setCellValue("None");
		 * cell.setCellStyle(style);
		 * 
		 * cell = row.createCell(cellnum++); cell.setCellValue("None");
		 * cell.setCellStyle(style);
		 * 
		 * cell = row.createCell(cellnum++); cell.setCellValue("None");
		 * cell.setCellStyle(style);
		 */
		cell = row.createCell(cellnum++);
		cell.setCellValue("AllImageCount");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("BaseLineTiaaImageCount");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("TiaaImageCount");
		cell.setCellStyle(style);

		// adding 4 columns for tiaa-cref count

		cell = row.createCell(cellnum++);
		cell.setCellValue("BaseLineTiaaImageUrls");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("TiaaImageUrls");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("BaseLineTiaaCrefStringCount");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("TiaaCrefStringCount");
		cell.setCellStyle(style);

		// ending

		cell = row.createCell(cellnum++);
		cell.setCellValue("BaseLineTiaaCount");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("TiaaStringCount");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PercentageTextMatched");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PercentageWordMatched");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PercentageSentenceMatched");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("LevenshteinDistance");
		cell.setCellStyle(style);

		/*
		 * cell = row.createCell(cellnum++); cell.setCellValue("TiaaCrefUrls");
		 * cell.setCellStyle(style);
		 * 
		 * cell = row.createCell(cellnum++);
		 * cell.setCellValue("BaseLineTiaaCrefStringInContentCount");
		 * cell.setCellStyle(style);
		 * 
		 * cell = row.createCell(cellnum++);
		 * cell.setCellValue("TiaaCrefStringInContentCount");
		 * cell.setCellStyle(style);
		 */
		return sheet;
	}

	private Sheet writeBrokenUrlDetailsHeader(SXSSFWorkbook wb, Sheet sheet) {
		CellStyle style = createHeaderCellStyle(wb);

		Row row = sheet.createRow(0);

		int cellnum = 0;
		int temp;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("AppName");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PageUrl");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("IsErrorPage");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ErrorType");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ParentUrl");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("LinkName");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("NavigationPath");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("RefererUrl");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("RefererPageTitle");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("HttpStatusCode");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("HttpStatusText");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("HttpRequestMethod");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Ajax Call Present");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Pageload Attribute Name");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Pageload Attribute Times");
		cell.setCellStyle(style);
		
		return sheet;
	}

	private Sheet writeAuditTestDetailsHeader(SXSSFWorkbook wb, Sheet sheet,
			String baselineRunDate, String currentRunDate) {

		CellStyle style = createHeaderCellStyle(wb);

		Row row = sheet.createRow(0);

		int cellnum = 0;
		int temp;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("Baseline AppName");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Current Run AppName");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("PageTitle");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Baseline PageUrl");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Current Run PageUrl");
		cell.setCellStyle(style);

		temp = cellnum++;
		cell = row.createCell(temp);
		cell.setCellValue("ExpectedDataFile");
		cell.setCellStyle(style);
		sheet.setColumnHidden(temp, true); // hide this column

		temp = cellnum++;
		cell = row.createCell(temp);
		cell.setCellValue("ActualDataFile");
		cell.setCellStyle(style);
		sheet.setColumnHidden(temp, true); // hide this column

		cell = row.createCell(cellnum++);
		cell.setCellValue("ExpectedTagName" + "(" + baselineRunDate + ")");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ActualTagName" + "(" + currentRunDate + ")");
		cell.setCellStyle(style);

		temp = cellnum++;
		sheet.setColumnHidden(temp, true); // hide this column
		cell = row.createCell(temp);
		cell.setCellValue("ExpectedTagUrl" + "(" + baselineRunDate + ")");
		cell.setCellStyle(style);

		temp = cellnum++;
		sheet.setColumnHidden(temp, true); // hide this column
		cell = row.createCell(temp);
		cell.setCellValue("ActualTagUrl" + "(" + currentRunDate + ")");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ExpectedTagDataName" + "(" + baselineRunDate + ")");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ExpectedTagDataValue" + "(" + baselineRunDate + ")");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("ActualTagDataValue" + "(" + currentRunDate + ")");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("TestResult");
		cell.setCellStyle(style);

		return sheet;
	}
	
}
