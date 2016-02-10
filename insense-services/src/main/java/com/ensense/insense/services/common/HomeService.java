package com.ensense.insense.services.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cts.mint.analytics.model.AnalyticsDetails;
import com.cts.mint.analytics.model.AnalyticsSummaryReportUi;
import com.cts.mint.common.entity.BrowserType;
import com.cts.mint.common.entity.FeedBack;
import com.cts.mint.common.entity.SolutionType;
import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.entity.UsageReport;
import com.cts.mint.common.model.ConfigProperty;
import com.cts.mint.common.model.IdNamePair;
import com.cts.mint.common.model.ReportStatus;
import com.cts.mint.common.model.ScheduleStatus;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.common.model.UsageReportResult;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.ScheduleScript;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.model.WebserviceSetupForm;

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
