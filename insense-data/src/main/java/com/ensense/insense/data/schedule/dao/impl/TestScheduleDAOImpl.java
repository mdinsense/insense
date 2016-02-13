package com.ensense.insense.data.schedule.dao.impl;

import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.schedule.dao.TestScheduleDAO;
import com.ensense.insense.data.uitesting.entity.Schedule;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestScheduleDAOImpl implements TestScheduleDAO {
	private static Logger logger = Logger.getLogger(TestScheduleDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * Getting scheduling data from TestSchedule table
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Schedule getSchedule(Schedule testSchedule) {
		logger.info("Entry: getSchedule");
		Schedule ts = null;
		try {
			Query query = this.sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Schedule where scheduleId = :scheduleId ");
			query.setParameter("scheduleId",
					testSchedule.getScheduleId());
			ts = (Schedule) query.list().get(0);

		} catch (Exception exp) {
			logger.error("Exception in getSchedule.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: getSchedule");
		return ts;

	}
	
	/*
	 * deleting scheduling data from TestSchedule table
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateTestSchedule(Schedule testSchedule) {
		logger.info("Entry: updateTestSchedule");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().update(testSchedule);
		} catch (Exception exp) {
			result = false;
			logger.error("Exception in updateTestSchedule.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: updateTestSchedule");
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateExecutionSeralizePath(ScheduleDetails appConfig) {
		logger.info("Entry: updateExecutionSeralizePath");
		Query updateStatus=null;
		boolean result = false;
		try{
			updateStatus = sessionFactory.getCurrentSession().
					createQuery(" Update ScheduleExecution schStatus set crawlStatusDirectory = :crawlStatusDirectory, reportsDirectory = :reportsDirectory, " //TODO Properties for crawlStatusDirectory
								+ "harReportsDirectory = :harReportsDirectory "
								+ "where scheduleExecutionId = :scheduleExecutionId and scheduleId =:scheduleId ");
					updateStatus.setParameter("crawlStatusDirectory", appConfig.getSerializePath());
					updateStatus.setParameter("reportsDirectory", appConfig.getReportsPath());
					updateStatus.setParameter("harReportsDirectory", appConfig.getHarReportsPath());
					updateStatus.setParameter("scheduleExecutionId",appConfig.getExecutionId());
					updateStatus.setParameter("scheduleId",appConfig.getTestScheduleId());
					updateStatus.executeUpdate();
					result = true;
		}catch(Exception exp){
			logger.error("Exception in updateExecutionSeralizePath.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
			exp.printStackTrace();
		}
		
		logger.info("Exit: updateExecutionSeralizePath");
		return result;
	}

	@Override
	public boolean insertComparisonStart(ScheduleDetails appConfig) {
		logger.info("Entry: insertComparisonStart");
		boolean result = false;
		Query updateStatus = null;
		try{
			StringBuffer sb = new StringBuffer("Update ScheduleExecution schStatus set ");
			if ( appConfig.isTextCompare() ){
				sb.append("textComparisonStatusId = " + ExecutionStatus.PENDING.getStatusCode() +",");
			}
			if ( appConfig.isHtmlCompare() ){
				sb.append("htmlComparisonStatusId = " + ExecutionStatus.PENDING.getStatusCode() +",");
			}
			if ( appConfig.isScreenCompare() ){
				sb.append("imageComparisonStatusId = " + ExecutionStatus.PENDING.getStatusCode() +",");
			}
			sb.deleteCharAt(sb.length()-1);
			
			sb.append(" where scheduleExecutionId = :scheduleExecutionId and scheduleId = :scheduleId");
			updateStatus = sessionFactory.getCurrentSession().
					createQuery(sb.toString());
					
					updateStatus.setParameter("scheduleExecutionId",appConfig.getExecutionId());
					updateStatus.setParameter("scheduleId",appConfig.getTestScheduleId());
					updateStatus.executeUpdate();
					result = true;
		}catch(Exception exp){
			logger.error(exp);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		
		logger.info("Exit: insertComparisonStart");
		return result;
	}
	
}
