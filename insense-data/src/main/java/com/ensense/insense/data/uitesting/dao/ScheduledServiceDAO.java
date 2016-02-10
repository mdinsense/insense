package com.ensense.insense.data.uitesting.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;

public interface ScheduledServiceDAO {

	public ScheduleExecutionDetail getPendingOnDemandWebApplicationSchedule();

	public void schedulePendingTestSuits();

	public boolean updateScheduleExecutionAsProcessing(ScheduleExecution scheduleExecution);

	public int getMaxScheduleIdForSuit(int suitId, Date baselineDate);
	
	public ScheduleExecutionDetail getPendingComparison();

	public boolean deleteSchedule(int scheduleId);

	public Schedule getSchedule(int scheduleId);

	//public ScheduleExecution getBaselineScheduleExecution(Integer suitId);

	public ScheduleExecution getScheduleExecution(Integer scheduleExecutionId);

	public boolean updateScheduleExecution(
			ScheduleExecution updateScheduleExecution);

	public ScheduleExecutionDetail getPendingHtmlComparison();

	public ScheduleExecutionDetail getPendingImageComparison();

	public List<SuitTextImageXref> getTextAndImageDetailsFortheSuitId(int suitId);
	public Map<Integer, ScheduleExecution> getScheduleExecutionMap(
			String scheduleExecutionIdStr);

	public Map<Integer, Schedule> getScheduleMap(String scheduleIdStr);

	public List<ScheduleExecutionDetail> getScheduleDetails(UsageReportForm form, Date fromDate, Date toDate);

	public List<String> deleteScheduleExecutions(String scheduleExecutionId);
}
