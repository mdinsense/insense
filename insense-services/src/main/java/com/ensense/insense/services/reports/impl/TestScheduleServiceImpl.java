package com.ensense.insense.services.reports.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.dao.TestScheduleDAO;
import com.cts.mint.reports.service.TestScheduleService;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.model.ScheduleDetails;

@Service
public class TestScheduleServiceImpl implements TestScheduleService {
	private static Logger logger = Logger
			.getLogger(TestScheduleServiceImpl.class);

	@Autowired
	private TestScheduleDAO testScheduleDAO;

	/*
	 * Calling testScheduleDAOImpl class to get scheduleDetails
	 */
	@Override
	@Transactional
	public Schedule getSchedule(Schedule testSchedule) {
		logger.info("Entry: service:getSchedule");
		return testScheduleDAO.getSchedule(testSchedule);
	}
	

	@Override
	@Transactional
	public boolean updateTestSchedule(Schedule testSchedule) {
		logger.info("Entry: updateTestSchedule");
		return testScheduleDAO.updateTestSchedule(testSchedule);
	}
	
	
	@Override
	@Transactional
	public boolean updateExecutionSeralizePath(ScheduleDetails appConfig) {
		logger.info("Entry: updateExecutionSeralizePath");
		return testScheduleDAO.updateExecutionSeralizePath(appConfig);
	}


	@Override
	@Transactional
	public boolean insertComparisonStart(ScheduleDetails appConfig) {
		logger.info("Entry and Exit: insertComparisonStart");
		return testScheduleDAO.insertComparisonStart(appConfig);
	}
	
}
