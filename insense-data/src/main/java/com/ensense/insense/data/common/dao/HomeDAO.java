package com.ensense.insense.data.common.dao;

import java.util.Date;
import java.util.List;

import com.cts.mint.common.entity.BrowserType;
import com.cts.mint.common.entity.FeedBack;
import com.cts.mint.common.entity.MintProperties;
import com.cts.mint.common.entity.SolutionType;
import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.entity.UsageReport;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.ScheduleScript;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.model.WebserviceSetupForm;

public interface HomeDAO {
	public List<Suit> getUiRegressionSuitForGroup(int groupId);

	public List<Suit> getUiRegressionSuitForUser(int userId);
	
	public boolean saveMintProperty(MintProperties mintProperties) throws Exception;

	public List<SolutionType> getAllSolutionTypes();

	public boolean deleteAllMintPropertuies(MintProperties mintProperties)
			throws Exception;

	public Suit getSavedSuits(int testSuitId);

	public List<Suit> getSavedSuitsCreatedByAll();
	public List<BrowserType> getBrowserTypes();

	public boolean saveSchedule(Schedule ts);

	public boolean removeSuitDetails(Suit suit);
	
	public boolean addFeedBack(FeedBack feedBack);
	
	public List<SuitGroupXref> getManageSuitDetails(int suitId);
	
	public boolean deleteSuitGroupXref(int suitId);
	
	public boolean addSuitGroupXref(SuitGroupXref suitGroup); 
	
	public List<Suit> getManageSuitsForGroup(int groupId);
	
	public List<Suit> getManageSuitsForGroupUser(int groupId, int userId);

	public boolean isUserHaveAccessToSuit(int groupId, int suitId);

	public Suit getSuitByName(String suitName);
	
	public List<Suit> getManageSuitsForAllSuits();

	public List<ScheduleExecutionDetail> getCompletedScheduleDetailsForSuit(int suitId);

	public List<ScheduleExecution> getInProgressScheduleDetailsForSuit(
			int suitId);

	public List<Schedule> getFutureScheduleDetailsForSuit(int suitId);

	public List<ScheduleExecution> getBaselineScheduleExcutionList(int suitId);
	
	public List<ScheduleExecution> getReportBaselineScheduleExcutionList(int suitId);

	public boolean saveUsageReport(UsageReport usageReport) throws Exception;

	public boolean updateUsageReport(UsageReport usageReport) throws Exception;
	
	public UsageReport getUsageReport(int referenceId) throws Exception;

	public List<UsageReport> getUsageReportList(UsageReportForm form, Date fromDate, Date toDate) throws Exception;

	public WebserviceSuite getSavedWsSuits(int webserviceSuiteId);

	public List<WebserviceSetupForm> getSavedWsSuitesParams(
			int webserviceSuiteId);

	public boolean saveScheduleScript(ScheduleScript scheduleScript);
	
	public boolean saveScheduleScriptXref(ScheduleScriptXref scheduleScriptXref);

	public boolean updateScheduleScript(ScheduleScript scheduleScript);

	public List<ScheduleScript> getScheduleScript(Integer scheduleId);

	public List<ScheduleExecution> getScheduleExecution(Integer scheduleId);

	
	
	public List<Object[]> getChartData(String criteria, Integer criteriaId, Date fromDate, Date toDate);

	public List<Object[]> getBarChartData(String reqCriteria,
										  Integer reqCriteriaId, Date fromDate, Date toDate, String criteria, Integer id);
}
