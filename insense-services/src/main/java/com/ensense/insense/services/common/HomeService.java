package com.ensense.insense.services.common;

import com.ensense.insense.data.analytics.model.AnalyticsDetails;
import com.ensense.insense.data.analytics.model.AnalyticsSummaryReportUi;
import com.ensense.insense.data.common.entity.*;
import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.uitesting.entity.*;
import com.ensense.insense.data.webservice.entity.WebserviceSuite;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



public interface HomeService {
	public List<Suit> getMintHomeCreatedByGroup(String userid, int groupid);

	public List<Suit> getMintHomeCreatedByAll();

	public List<Suit> getUiRegressionSuitForGroup(int groupId);

	public Object getUiRegressionSuitForUser(int currentUserId);

	public boolean saveMintConfigProperties(ConfigProperty configProperty)
			throws Exception;

	public List<SolutionType> getAllSolutionTypes();

	public Suit getSavedSuits(int suitId);

	public List<Suit> getSavedSuitsCreatedByAll();

	public List<BrowserType> getBrowserTypes();

	public boolean saveSchedule(Schedule ts);

	public boolean removeSuitDetails(Suit suit);

	public boolean addFeedBack(FeedBack feedBack);

	public List<SuitGroupXref> getManageSuitDetails(int suitId);

	public boolean addManageSuitsGroup(String SuitId,
									   List<SuitGroupXref> suitGroupXre);

	public List<Suit> getManageSuitsForGroup(int groupId);

	public List<Suit> getManageSuitsForGroupUser(int groupId, int userId);

	public List<Suit> getManageSuitsForAllSuits();

	public List<ScheduleStatus> getCompletedScheduleStatus(int suitId, int count);

	public List<ScheduleStatus> getInProgressScheduleStatus(int suitId);

	public List<ScheduleStatus> getFutureSuitScheduleStatus(int suitId);

	public List<ScheduleExecution> getBaselineScheduleExcutionList(int suitId);

	public List<ScheduleExecution> getReportBaselineScheduleExcutionList(
			int suitId);

	public ReportStatus getComparisonReportsList(int scheduleExecutionId);

	public boolean isLoginFromLocal(HttpServletRequest httpRequest);

	public List<AnalyticsSummaryReportUi> getAnalyticsSummaryReportData(
			int scheduleExecutionId) throws Exception;

	public boolean saveUsageReport(UsageReport usageReport) throws Exception;

	public boolean updateUsageReport(UsageReport usageReport) throws Exception;

	public UsageReport getUsageReport(Integer scheduleExecutionId) throws Exception;

	public List<UsageReportResult> getUsageReportList(UsageReportForm form) throws Exception;

	public WebserviceSuite getSavedWsSuits(int webserviceSuiteId);

	public List<WebserviceSetupForm> getSavedWsSuitesParams(
			int webserviceSuiteId);

	public boolean saveScheduleScript(ScheduleScript scheduleScript, ScheduleScriptXref scheduleScriptXref);
	
	//public boolean saveScheduleScriptXref(ScheduleScriptXref scheduleScriptXref);

	public boolean updateScheduleScript(List<ScheduleScriptXref> scheduleScriptList);

	public List<ScheduleScript> getScheduleScript(Integer scheduleId);
	public List<ScheduleExecution> getScheduleExecution(Integer scheduleId);

	public List<IdNamePair> getChartData(Integer criteriaId, String fromDate, String toDate);
	public List<IdNamePair> getBarChartData(Integer criteriaId, Integer id, String fromDate, String toDate, int position);

	public List<SuitTextImageXref> getSuitTextImageXref(int suitId);
	
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXref(int suitId);
	
	public List<AnalyticsDetails> getAnalyticsDetailsReportData(
			int scheduleExecutionId, String detailOrErrorFormate) throws Exception;
}
