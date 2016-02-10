package com.ensense.insense.services.reports;

import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.model.ScheduleDetails;

public interface TestScheduleService {
	public Schedule getSchedule(Schedule testSchedule);
	public boolean updateTestSchedule(Schedule testSchedule);
	public boolean updateExecutionSeralizePath(ScheduleDetails appConfig);
	public boolean insertComparisonStart(ScheduleDetails appConfig);
}
