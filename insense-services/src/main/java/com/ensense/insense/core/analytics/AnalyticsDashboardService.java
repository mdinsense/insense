
package com.ensense.insense.core.analytics;

import com.ensense.insense.core.analytics.model.AnalyticsAuditSummaryReportDates;
import com.ensense.insense.core.analytics.model.DetailedViewContents;
import com.ensense.insense.core.generated.jaxb.analytics.summarydashboard.SummaryDashBoardData;
import com.ensense.insense.data.analytics.entity.AnalyticsAuditSummary;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.transaction.entity.TestCaseFileUpload;
import com.ensense.insense.data.uitesting.entity.LoginUserDetails;
import com.ensense.insense.data.uitesting.entity.Reports;
import com.ensense.insense.data.uitesting.entity.Results;

import java.util.List;


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








