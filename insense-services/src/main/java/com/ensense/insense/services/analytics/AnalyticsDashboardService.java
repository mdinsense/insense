
package com.ensense.insense.services.analytics;

import java.util.List;

import com.cts.mint.analytics.model.AnalyticsAuditSummaryReportDates;
import com.cts.mint.analytics.model.DetailedViewContents;
import com.cts.mint.generated.jaxb.analytics.summarydashboard.SummaryDashBoardData;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Reports;
import com.cts.mint.uitesting.entity.Results;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.transaction.entity.TestCaseFileUpload;
import com.cts.mint.analytics.entity.AnalyticsAuditSummary;


/**
 * @author 361494
 * 
 */
public interface AnalyticsDashboardService {	
	
	public SummaryDashBoardData getDashBoardDetailsDefault(LoginUserDetails loginUser);
	public List<DetailedViewContents> getDetailedViewData(String pageURLdefault, LoginUserDetails loginUser, Reports forGeneratedDate);
	//added on 6 - 11 - 2014  	
	public List<DetailedViewContents> getDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Reports forGeneratedDate);
	public List<String> getRelatedPageURLs(String searchKey, String applicationName);
	public List<String> getRelatedPageURLs(String searchKey, Results testResult);
	public boolean populateSummaryView(String baselineHarReportsPath, String testHarReportsPath, Integer scheduleId, ScheduleDetails appConfig, String excelFilePath, String sheetName, String tempDirectory, int excelMaxRowCount, boolean generateReportsOnlyForRefererUrl, String tagSignatureJsonFilePath);
	public boolean populateDetailedView(String testHarReportsPath, Integer scheduleId, ScheduleDetails appConfig, String tagSignatureJsonFilePath);
	
	//Added on 06/18/2014 by 424596 by 424596
	public SummaryDashBoardData getDashBoardDetailsBasedOnDropdown(AnalyticsAuditSummary forGeneratedDate);
	//public SummaryDashBoardData getDashBoardDetailsBasedOnDropdownPublic(TestLoginUser loginUser, AnalyticsAuditSummary forGeneratedDate); 
	//public SummaryDashBoardData getDashBoardDetailsBasedOnDropdownTxn(TestCaseFileUpload testCaseFileUpload,AnalyticsAuditSummary forGeneratedDate);
	
	//modified on 6 - 19 - 2014 by 424596
	public List<DetailedViewContents> getDetailedViewDataUserSearchBased(String pageURLSelected, LoginUserDetails loginUser, Results forGeneratedDate);
	public List<DetailedViewContents> getDetailedViewDataUserSearchBasedtc(String pageURLSelected, TestCaseFileUpload testcase, Results forGeneratedDate);
	public List<DetailedViewContents> getDetailedViewDataUserSearchBasedPublic(String pageURLSelected,
																			   Reports forGeneratedDate, Integer applicationId, Integer environmentId);
	
	//Added on 06/19/2014 by 424596
	public List<DetailedViewContents> getTestResultDetailedViewData(String pageURLdefault, LoginUserDetails loginUser, Results forGeneratedDate);
	public List<DetailedViewContents> getTestResultDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Results forGeneratedDate);
	public List<DetailedViewContents> getTestResultDetailedViewDataUserSearchBasedPublic(
			String pageURLSelected, Results forGeneratedDate, Integer applicationId, Integer environmentId);
	
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDates(LoginUserDetails testLoginUser);
	public String getDefaultPageUrl(int testScheduleId);
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryPublicSiteReportsDates(LoginUserDetails testLoginUser);
	public AnalyticsAuditSummary getAnalyticsAuditSummaryForAnalyticsAuditId(AnalyticsAuditSummary analyticsAuditDetails);
	
	public String getDetailedviewxml(int detailedViewId);
	public int getUpdateDetailedviewxml(int detailedViewId, String updatedetailvalue);
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDatesForTransaction(int applicationId, int environmentId, int testCaseId);
}








