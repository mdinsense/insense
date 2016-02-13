package com.ensense.insense.services.reports;


import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.uitesting.entity.Schedule;


public interface TestScheduleService {
	public Schedule getSchedule(Schedule testSchedule);
	public boolean updateTestSchedule(Schedule testSchedule);
	public boolean updateExecutionSeralizePath(ScheduleDetails appConfig);
	public boolean insertComparisonStart(ScheduleDetails appConfig);
}
