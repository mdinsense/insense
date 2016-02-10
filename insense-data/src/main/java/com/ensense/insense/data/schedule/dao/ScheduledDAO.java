package com.ensense.insense.data.schedule.dao;

import java.util.List;

import com.cts.mint.common.model.PartialText;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;

public interface ScheduledDAO {
	public boolean setCompletion(ScheduleDetails appConf, boolean runStatus);

	public List<ScheduleExecutionDetail> getPendingInProgressScheduleDetails();

	public List<PartialText> getPartialTextCompareList(int applicationId, int environmentId);

}
