package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.ReportsGenerationDAO;
import com.cts.mint.uitesting.dao.ScheduledServiceDAO;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.service.ReportsGenerationService;

@Service
public class ReportsGenerationServiceImpl implements ReportsGenerationService{

	@Autowired
	ReportsGenerationDAO reportsGenerationDAO;
	
	@Autowired
	ScheduledServiceDAO scheduledServiceDao;
	
	@Override
	@Transactional
	public ScheduleExecutionDetail getPendingReportGeneration() {
		ScheduleExecutionDetail scheduleExecutionDetail = reportsGenerationDAO.getPendingReportGeneration();
		
		if ( null != scheduleExecutionDetail && scheduleExecutionDetail.getSuitId() > 0 ){
			scheduleExecutionDetail.setSuitBrokenTypeXref(reportsGenerationDAO.getSuitBrokenReportsXrefList(scheduleExecutionDetail.getSuitId()));
		}
		return scheduleExecutionDetail;
	}

	@Override
	@Transactional
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXrefList(int suitId){
		return reportsGenerationDAO.getSuitBrokenReportsXrefList(suitId);
	}
}
