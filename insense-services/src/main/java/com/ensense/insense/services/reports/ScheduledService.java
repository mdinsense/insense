package com.ensense.insense.services.reports;

import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.uitesting.entity.Schedule;
import com.ensense.insense.data.uitesting.entity.ScheduleExecution;
import com.ensense.insense.data.uitesting.entity.SuitTextImageXref;
import com.ensense.insense.services.crawler.model.UiReportsSummary;
import com.ensense.insense.data.model.uiadmin.form.schedule.CompareLink;

import java.util.List;
import java.util.Map;


public interface ScheduledService {

	public ScheduleExecutionDetail getPendingOnDemandWebApplicationSchedule();

	public void schedulePendingTestSuits();

	public boolean setCompletion(ScheduleDetails testExecutionStatus,
								 boolean runStatus);
	
	public void findFileSchedularTests() throws Exception;
	
	public ScheduleExecutionDetail getPendingComparison();

	public boolean deleteSchedule(int scheduleId, String scheduleDay);

	//public ScheduleExecution getBaselineScheduleExecution(Integer suitId);

	public ScheduleExecution getScheduleExecution(Integer scheduleExecutionId);

	public boolean updateScheduleExecution(ScheduleExecution scheduleExecution, String updateType);

	public void updateFilePath(List<CompareLink> compareResults);

	public ScheduleExecutionDetail getPendingHtmlComparison();

	public ScheduleExecutionDetail getPendingImageComparison();

	public UiReportsSummary getUiReportSummary(ScheduleExecution scheduleExecution, ScheduleExecution baselineScheduleExecution);

	public List<ScheduleExecutionDetail> getPendingInProgressScheduleDetails();

	public List<ScheduleStatus> getScheduleStatusList(
			List<ScheduleExecutionDetail> scheduleExecutionDetailList);

	public List<PartialText> getPartialTextCompareList(int applicationId,
													   int environmentId);

	public boolean updateCrawlStatus(ScheduleExecution scheduleExecution,
									 String scheduleAction);
	
	public Schedule getSchedule(int scheduleId);
	
	public Map<Integer, ScheduleExecution> getScheduleExecutionMap(String scheduleExecutionIdStr);

	public Map<Integer, Schedule> getScheduleMap(String scheduleId);
	public List<SuitTextImageXref> getTextAndImageDetailsFortheSuitId(int suitId);
	//public List<ArchiveData> getScheduleDetails(UsageReportForm form);
	public List<String> deleteScheduleExecutions(String scheduleExecutionId);
}
