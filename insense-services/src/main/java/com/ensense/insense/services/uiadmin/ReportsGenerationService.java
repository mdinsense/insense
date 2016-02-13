package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;

public interface ReportsGenerationService {
	public ScheduleExecutionDetail getPendingReportGeneration();
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXrefList(int suitId);
}
