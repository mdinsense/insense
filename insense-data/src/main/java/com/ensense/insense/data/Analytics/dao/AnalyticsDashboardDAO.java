package com.ensense.insense.data.analytics.dao;

import java.util.List;

import com.cts.mint.analytics.entity.AnalyticsAuditSummary;
import com.cts.mint.analytics.entity.DashboardsHomeDetails;
import com.cts.mint.analytics.entity.DetailedView;
import com.cts.mint.analytics.model.AnalyticsAuditSummaryReportDates;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Reports;
import com.cts.mint.uitesting.entity.Results;
import com.cts.mint.uitesting.transaction.entity.TestCaseFileUpload;


public interface AnalyticsDashboardDAO {


	public DashboardsHomeDetails getDashBoardHomeDataDefault(LoginUserDetails loginUser);
	//public DetailedViewDetails getDetailedViewDataFromDB();
	public List<DetailedView> getDetailedViewDataFromDB(String pageURLdefault, LoginUserDetails loginUser, Reports forGeneratedDate);
	public List<DetailedView> getDetailedViewDataUserSearchBased(String pageURLSelected, LoginUserDetails loginUser, Results forGeneratedDate) ;
		
	
	public List<String> getRelatedPageURLsfromDB(String searchKey, String applicationName);
	public List<String> getRelatedPageURLs(String searchKey, Results testResult);
	public Boolean insertRequestParams(String reqParameter, String operationName);
	public Boolean insertServiceNames(String serviceName);
	public Boolean insertOperationNames(String operationName, String serviceName);
	
	//added on 6 - 11- 2014 	
	public List<DetailedView> getDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Reports forGeneratedDate);
	
	//added on 6 - 12- 2014  	
	public List<DetailedView> getDetailedViewDataUserSearchBasedtc(String pageURLSelected, TestCaseFileUpload testcase, Results forGeneratedDate) ;
	public List<DetailedView> getDetailedViewDataUserSearchBasedPublic(String pageURLSelected, Reports forGeneratedDate, Integer applicationId,
																	   Integer environmentId);
	
	public AnalyticsAuditSummary getDashBoardHomeDataForUserChosen(AnalyticsAuditSummary forGeneratedDate);
	//public AnalyticsAuditSummary getDashBoardHomeDataForUserChosenPublic(TestLoginUser loginUser, AnalyticsAuditSummary forGeneratedDate );
	//public AnalyticsAuditSummary getDashBoardHomeDataForUserChosenTestCase(TestCaseFileUpload testCaseFileUpload, AnalyticsAuditSummary forGeneratedDate ); 
	
	// Added on 06/19/2014 by Sravanthi
	public List<DetailedView> getTestResultDetailedViewDataFromDB(String pageURLdefault, LoginUserDetails loginUser, Results forGeneratedDate);
	public List<DetailedView> getTestResultDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Results forGeneratedDate);
	public List<DetailedView> getTestResultDetailedViewDataUserSearchBasedPublic(String pageURLSelected, Results forGeneratedDate,
																				 Integer applicationId, Integer environmentId);
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDates(LoginUserDetails testLoginUser);
	public String getDefaultPageUrl(int testScheduleId);
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryPublicSiteReportsDates(LoginUserDetails testLoginUser);
	
	public String getDetailedviewxml(int detailedViewId);
    public int getUpdateDetailedviewxml(int detailedViewId, String updatedetailvalue);
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDatesForTransaction(int testCaseId, int applicationId, int environmentId);


}
