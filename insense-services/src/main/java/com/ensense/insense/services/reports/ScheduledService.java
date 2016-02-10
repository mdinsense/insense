package com.ensense.insense.services.reports;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cts.mint.common.model.PartialText;
import com.cts.mint.common.model.ScheduleStatus;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.crawler.model.UiReportsSummary;
import com.cts.mint.miscellaneous.model.ArchiveData;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.CompareLink;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;


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
	public List<ArchiveData> getScheduleDetails(UsageReportForm form);
	public List<String> deleteScheduleExecutions(String scheduleExecutionId);
}
