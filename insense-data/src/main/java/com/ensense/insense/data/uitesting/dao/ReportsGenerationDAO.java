package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;

public interface ReportsGenerationDAO {

	public ScheduleExecutionDetail getPendingReportGeneration();
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXrefList(int suitId);
}
