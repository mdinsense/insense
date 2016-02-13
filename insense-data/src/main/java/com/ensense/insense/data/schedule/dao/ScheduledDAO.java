package com.ensense.insense.data.schedule.dao;

import com.ensense.insense.data.common.model.PartialText;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.model.ScheduleExecutionDetail;

import java.util.List;


public interface ScheduledDAO {
	public boolean setCompletion(ScheduleDetails appConf, boolean runStatus);

	public List<ScheduleExecutionDetail> getPendingInProgressScheduleDetails();

	public List<PartialText> getPartialTextCompareList(int applicationId, int environmentId);

}
