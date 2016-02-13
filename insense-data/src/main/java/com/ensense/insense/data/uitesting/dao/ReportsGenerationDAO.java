package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.common.model.ScheduleExecutionDetail;
import com.ensense.insense.data.uitesting.entity.SuitBrokenReportsXref;

import java.util.List;

public interface ReportsGenerationDAO {

	public ScheduleExecutionDetail getPendingReportGeneration();
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXrefList(int suitId);
}
