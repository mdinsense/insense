package com.ensense.insense.data.analytics.dao;

import com.ensense.insense.data.analytics.entity.AnalyticsAuditSummary;
import com.ensense.insense.data.analytics.entity.DashboardsHomeDetails;
import com.ensense.insense.data.analytics.entity.DetailedView;
import com.ensense.insense.data.common.model.AnalyticsAuditSummaryReportDates;
import com.ensense.insense.data.transaction.entity.TestCaseFileUpload;
import com.ensense.insense.data.uitesting.entity.LoginUserDetails;
import com.ensense.insense.data.uitesting.entity.Reports;
import com.ensense.insense.data.uitesting.entity.Results;

import java.util.List;

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
