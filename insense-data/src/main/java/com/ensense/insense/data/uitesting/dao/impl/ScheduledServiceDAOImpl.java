package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.common.utils.Constants.SolutionType;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.Constants.UsageReportConstants;
import com.cts.mint.uitesting.dao.ScheduledServiceDAO;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.MintConstants;

@Service
public class ScheduledServiceDAOImpl implements ScheduledServiceDAO {
	private static Logger logger = Logger
			.getLogger(ScheduledServiceDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public ScheduleExecutionDetail getPendingComparison() {
		ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s, aecx from "
									+ "ScheduleExecution se, Schedule s, Suit suit, AppEnvEnvironmentCategoryXref aecx "
									+ "where se.scheduleId = s.scheduleId and ( se.testExecutionStatusRefId = :testExecutionStatusRefId or se.testExecutionStatusRefId = :testExecutionStatusRefIdCompl ) and "
									+ "(se.textComparisonStatusId = :textComparisonStatusId) and "
									+ "s.suitId = suit.suitId and aecx.applicationId = s.applicationId and aecx.environmentId = s.environmentId and "
									+ "aecx.environmentCategoryId = suit.environmentCategoryId "
									+ "order by se.testExecutionStartTime");
			
			query.setParameter("testExecutionStatusRefId", ExecutionStatus.IN_PROGRESS.getStatusCode());
			query.setParameter("testExecutionStatusRefIdCompl", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("textComparisonStatusId", ExecutionStatus.PENDING.getStatusCode());
			//query.setParameter("htmlComparisonStatusId", ExecutionStatus.PENDING.getStatusCode());
			//query.setParameter("imageComparisonStatusId", ExecutionStatus.PENDING.getStatusCode());
			
			List<Object[]> objList = query.list();

			if (null != objList && objList.size() > 0) {
				scheduleExecutionDetail
						.setScheduleExecution((ScheduleExecution) objList
								.get(0)[0]);
				scheduleExecutionDetail
						.setSchedule((Schedule) objList.get(0)[1]);

				scheduleExecutionDetail.setSuitId(scheduleExecutionDetail.getSchedule().getSuitId());
				scheduleExecutionDetail.setAppEnvEnvironmentCategoryXref((AppEnvEnvironmentCategoryXref) objList.get(0)[2]);
				logger.info("New Schedule found for TEXT Comparison :"
						+ scheduleExecutionDetail);
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingComparison, while quering StatusExecution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return scheduleExecutionDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ScheduleExecutionDetail getPendingOnDemandWebApplicationSchedule() {
		ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();
		boolean isRestart = false;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s, Suit suit where s.suitId = suit.suitId and scheduleId in "
									+ "(select scheduleId from ScheduleExecution where testExecutionStatusRefId = :testExecutionStatusRefId)) order by scheduleId");

			query.setParameter("testExecutionStatusRefId", ExecutionStatus.RESTART.getStatusCode());
			List<Schedule> scheduleList = query.list();

			if ( null == scheduleList || scheduleList.size() < 1 ){
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"select s from Schedule s, Suit suit where s.suitId = suit.suitId and scheduleId in "
										+ "(select scheduleId from ScheduleExecution where testExecutionStatusRefId = :testExecutionStatusRefId)) order by scheduleId");

				query.setParameter("testExecutionStatusRefId", ExecutionStatus.PENDING.getStatusCode());
				scheduleList = query.list();
			}else{
				isRestart = true;
			}
		
			if (null != scheduleList && scheduleList.size() > 0) {
				
				if ( isRestart ){
					logger.info("Fount Web Application for Restart, schedule:"
							+ scheduleList);
				}else{
					logger.info("Fount new Web Application to run, schedule:"
						+ scheduleList);
				}
				
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"select aeecx, se, s from AppEnvEnvironmentCategoryXref aeecx, "
										+ "ScheduleExecution se, Schedule s "
										+ "where se.scheduleId = s.scheduleId and se.testExecutionStatusRefId = :testExecutionStatusRefId and "
										+ "aeecx.environmentId = s.environmentId and aeecx.applicationId = s.applicationId and "
										+ "aeecx.applicationId = :applicationId and aeecx.environmentId = :environmentId");

				query.setParameter("applicationId", scheduleList.get(0)
						.getApplicationId());
				query.setParameter("environmentId", scheduleList.get(0)
						.getEnvironmentId());
				if ( isRestart){
					query.setParameter("testExecutionStatusRefId", ExecutionStatus.RESTART.getStatusCode());
				}else{
					query.setParameter("testExecutionStatusRefId", ExecutionStatus.PENDING.getStatusCode());
				}
				
				List<Object[]> objList = query.list();
				
				if (null != objList && objList.size() > 0) {
					scheduleExecutionDetail.setSuitId(scheduleList.get(0)
							.getSuitId());
					scheduleExecutionDetail
							.setAppEnvEnvironmentCategoryXref((AppEnvEnvironmentCategoryXref) objList
									.get(0)[0]);
					scheduleExecutionDetail
							.setScheduleExecution((ScheduleExecution) objList
									.get(0)[1]);
					scheduleExecutionDetail.setSchedule((Schedule) objList
							.get(0)[2]);
					logger.info("scheduleExecutionDetail :"+scheduleExecutionDetail);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingOnDemandWebApplicationSchedule, while quering StatusExecution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		return scheduleExecutionDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void schedulePendingTestSuits() {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		try {
			// TODO:
			// Find the tasks that are in in progress status. Read the
			// CrawlConfig Object, if last updated time is less than current
			// time, continue executing that process.

			// find all the Ondemand testcases
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s, Suit suit where s.suitId = suit.suitId and scheduleId not in (select scheduleId from ScheduleExecution) "
									+ "and s.recurrence = false and s.isScheduled = false and s.solutionTypeId = 1 order by scheduleId");
			scheduleList = query.list();

			if (null != scheduleList && scheduleList.size() > 0) {
				Schedule schedule = scheduleList.get(0);
				try {
					scheduleTestcaseForExecution(schedule);
				} catch (Exception e) {
					logger.error("Exception while scheduling the testcase, Schedule details :"
							+ schedule);
					logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
				}

				return;
			}

			// Find all Scheduled testcases by date.
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s where scheduleId not in (select scheduleId from ScheduleExecution) "
									+ "and s.recurrence = false and s.isScheduled = true and scheduleType = 'D' " +
									" and s.recurrenceDateWise <=:currentDateTime order by scheduleId");
			
			//query.setTimestamp("currentDateTimeMaxSeconds",DateTimeUtil.addSeconds(new Date(), 59));
			query.setTimestamp("currentDateTime",new Date());
			
			scheduleList = query.list();

			if (null != scheduleList && scheduleList.size() > 0) {
				Schedule schedule = scheduleList.get(0);
				try {
					scheduleTestcaseForExecution(schedule);
				} catch (Exception e) {
					logger.error("Exception while scheduling the testcase, Schedule details :"
							+ schedule);
				}

				return;
			}
			
			// Find all Scheduled testcases by WeekDay no recurrence.
						
			  query = sessionFactory .getCurrentSession() .createQuery(
					  		  " select s from Schedule s where scheduleId not in (select scheduleId from ScheduleExecution) " +
							  " and s.recurrence = false and s.isScheduled = true and scheduleType = 'W' " +
							  " and s.scheduleDDay =:scheduleDDayName" +
							  " and extract(HOUR from s.startDate) <= :currentHours " +
							  " and extract(MINUTE from s.startDate) <= :currentMinutes " +
							  " order by scheduleId");
			  query.setParameter("scheduleDDayName", String.valueOf(MintConstants.DAYS[new Date().getDay()]));
			  query.setParameter("currentHours", new Date().getHours());
			  query.setParameter("currentMinutes", new Date().getMinutes());
			  scheduleList = query.list();
			 

				if (null != scheduleList && scheduleList.size() > 0) {
					Schedule schedule = scheduleList.get(0);
					try {
						scheduleTestcaseForExecution(schedule);
					} catch (Exception e) {
						logger.error("Exception while scheduling the testcase, Schedule details :"
								+ schedule);
						logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
					}

					return;
				}
			
			// Find all Scheduled testcases by WeekDay with recurrence.
			
			  query = sessionFactory .getCurrentSession() .createQuery(
					  		  " select s from Schedule s where scheduleId not in (select scheduleId from ScheduleExecution " +
					  		  " where dateCreated between :CurrentDateStart and :CurrentDateEnd) " +
							  " and s.recurrence = true and s.isScheduled = true and scheduleType = 'W' " +
							  " and s.scheduleDDay =:scheduleDDayName " +
							  " and extract(HOUR from s.startDate) <= :currentHours " +
							  " and extract(MINUTE from s.startDate) <= :currentMinutes) " +
							  " order by scheduleId"); 
			  
			  query.setTimestamp("CurrentDateStart", DateTimeUtil.getStartOfTheDay(new Date()));
			  query.setTimestamp("CurrentDateEnd", DateTimeUtil.getEndOfTheDay(new Date()));
			  query.setParameter("scheduleDDayName", String.valueOf(MintConstants.DAYS[new Date().getDay()]));
			  query.setParameter("currentHours", new Date().getHours());
			  query.setParameter("currentMinutes",new Date().getMinutes());
			  
			  scheduleList = query.list();
			 

				if (null != scheduleList && scheduleList.size() > 0) {
					Schedule schedule = scheduleList.get(0);
					try {
						scheduleTestcaseForExecution(schedule);
					} catch (Exception e) {
						logger.error("Exception while scheduling the testcase, Schedule details :"
								+ schedule);
						logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
					}

					return;
				}	
			 
			// Find all Scheduled testcases by WeekDay no recurrence.
			/*
			 * query = sessionFactory .getCurrentSession() .createQuery(
			 * "select s from Schedule s where " +
			 * "s.recurrence = false and s.isScheduled = true and scheduleType = 'W' order by scheduleId"
			 * ); scheduleList = query.list();
			 */

		} catch (Exception e) {
			logger.error("Exception in findOndemandTests");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
	}

	private void scheduleTestcaseForExecution(Schedule schedule)
			throws Exception {
		if (null != schedule) {
			ScheduleExecution scheduleExecution = new ScheduleExecution();

			logger.info("Found Schedule for Execution, schedule :" + schedule);
			
			scheduleExecution
					.setBrokenUrlReportStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setAnalyticsReportStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			
			scheduleExecution
					.setTextComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setHtmlComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setImageComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			if (null != schedule.getBaselineScheduleExecutionId() && schedule.getBaselineScheduleExecutionId() == 0) {
				scheduleExecution.setBaselineScheduleExecutionId(schedule.getBaselineScheduleExecutionId());
				scheduleExecution
						.setComparisonStatusRefId(ExecutionStatus.NOT_APPLICABLE
								.getStatusCode());
				scheduleExecution
						.setReportStatusRefId(ExecutionStatus.NOT_APPLICABLE
								.getStatusCode());
			}else if ( null != schedule.getBaselineScheduleExecutionId() && schedule.getBaselineScheduleExecutionId() == -1 ){
				scheduleExecution.setBaselineScheduleExecutionId(getBaselineScheduleExecutionId(schedule.getSuitId()));
			}else{
				scheduleExecution.setBaselineScheduleExecutionId(schedule.getBaselineScheduleExecutionId());
			}

			logger.info("Baseline set, scheduleExecution :"+scheduleExecution);
			scheduleExecution.setComparisonStatusRefId(ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			scheduleExecution.setScheduleId(schedule.getScheduleId());
			
			
			if (schedule.isRegressionTesting() && !schedule.isTransactionTestcase()) {
				if (!schedule.isTransactionTestcase()) {
					scheduleExecution.setComparisonStatusRefId(ExecutionStatus.PENDING.getStatusCode());
				}
				if (schedule.isTextCompare()) {

					if (scheduleExecution.getBaselineScheduleExecutionId() > 0) {
						scheduleExecution
								.setTextComparisonStatusId(ExecutionStatus.PENDING
										.getStatusCode());
					} else {
						scheduleExecution
								.setTextComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
										.getStatusCode());
					}
				}

				if (schedule.isHtmlCompare()) {
					if (scheduleExecution.getBaselineScheduleExecutionId() > 0) {
						scheduleExecution
								.setHtmlComparisonStatusId(ExecutionStatus.PENDING
										.getStatusCode());
					} else {
						scheduleExecution
								.setHtmlComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
										.getStatusCode());
					}
				}

				if (schedule.isScreenCompare()) {
					if (scheduleExecution.getBaselineScheduleExecutionId() > 0) {
						scheduleExecution
								.setImageComparisonStatusId(ExecutionStatus.PENDING
										.getStatusCode());
					} else {
						scheduleExecution
								.setImageComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
										.getStatusCode());
					}
				}
			}

			//Reprots Generation
			scheduleExecution.setReportStatusRefId(ExecutionStatus.NOT_APPLICABLE
					.getStatusCode());
			
			if (schedule.isAnalyticsTesting()) {
				scheduleExecution
						.setAnalyticsReportStatusId(ExecutionStatus.PENDING
								.getStatusCode());
				scheduleExecution.setReportStatusRefId(ExecutionStatus.PENDING
						.getStatusCode());
			}else {
				scheduleExecution
				.setAnalyticsReportStatusId(ExecutionStatus.NOT_APPLICABLE
						.getStatusCode());
			}
			if (schedule.isBrokenUrlReport()) {
				scheduleExecution
						.setBrokenUrlReportStatusId(ExecutionStatus.PENDING
								.getStatusCode());
				scheduleExecution.setReportStatusRefId(ExecutionStatus.PENDING
						.getStatusCode());
			}else{
				scheduleExecution
				.setBrokenUrlReportStatusId(ExecutionStatus.NOT_APPLICABLE
						.getStatusCode());
			}
			if (schedule.isBrandingUrlReport()) {
				scheduleExecution
						.setTextOrImageReportStatusId(ExecutionStatus.PENDING
								.getStatusCode());
				scheduleExecution.setReportStatusRefId(ExecutionStatus.PENDING
						.getStatusCode());
			}else {
				scheduleExecution
				.setTextOrImageReportStatusId(ExecutionStatus.NOT_APPLICABLE
						.getStatusCode());
			}

			scheduleExecution.setDateCreated(new Date());
			scheduleExecution
					.setTestExecutionStatusRefId(ExecutionStatus.PENDING
							.getStatusCode());
			logger.info("Adding new Schduele for Execution. scheduleExecution ->"
					+ scheduleExecution);
			sessionFactory.getCurrentSession().saveOrUpdate(scheduleExecution);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			if(schedule.isTransactionTestcase()) {
				logger.info("Adding transaction testcase schedule."
						+ scheduleExecution);
				String sqlQueryInner = " Update ScheduleScriptXref set scheduleExecutionId=:scheduleExecutionId "
									 + " where scheduleId =:scheduleId ";
				
				Query query = sessionFactory.getCurrentSession().createQuery(
						sqlQueryInner);
				query.setParameter("scheduleExecutionId", scheduleExecution.getScheduleExecutionId());
				query.setParameter("scheduleId", scheduleExecution.getScheduleId());
				query.executeUpdate();
			}
		}

	}

	private Integer getBaselineScheduleExecutionId(
			Integer suitId) {
		logger.debug("Entry: getBaselineScheduleExecutionId, suitId->"+suitId);
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();
		int scheduleExecutionId = 0; 
		String sqlQuery = "";
		try {

			sqlQuery = "select se from ScheduleExecution se, Schedule s, Suit suit "
						+ "where se.scheduleId = s.scheduleId and se.testExecutionStatusRefId = :testExecutionStatusRefId and s.suitId = :suitId and s.suitId = suit.suitId and "
						+ "(se.textComparisonStatusId = :textComparisonStatusId or se.textComparisonStatusId = :textComparisonStatusIdPend "
						+ "or se.htmlComparisonStatusId = :htmlComparisonStatusId or se.htmlComparisonStatusId = :htmlComparisonStatusIdPend "
						+ "or se.imageComparisonStatusId = :imageComparisonStatusId or se.imageComparisonStatusId = :imageComparisonStatusIdPend) "
						+ "order by se.testExecutionStartTime desc";
			
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("suitId", suitId);
			query.setParameter("testExecutionStatusRefId", ExecutionStatus.COMPLETE.getStatusCode());
			
			query.setParameter("textComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("textComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			query.setParameter("htmlComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("htmlComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			query.setParameter("imageComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("imageComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			scheduleExecutionList = query.list();
			
			logger.info("scheduleExecutionList for baseline:"+scheduleExecutionList);
			logger.info("scheduleExecutionList for baseline size:"+scheduleExecutionList.size());
			if (null != scheduleExecutionList && scheduleExecutionList.size() > 0) {
				scheduleExecution = scheduleExecutionList.get(0);
				scheduleExecutionId = scheduleExecution.getScheduleExecutionId();
			}

		} catch (Exception e) {
			logger.error("Exception in getBaselineScheduleExecutionId. BaselinescheduleExecutionId ->"+scheduleExecutionId);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: getBaselineScheduleExecutionId");
		
		return scheduleExecutionId;
	}

	@Override
	public boolean updateScheduleExecutionAsProcessing(
			ScheduleExecution scheduleExecution) {
		logger.debug("Entry :updateScheduleExecutionAsProcessing");
		boolean result = false;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(scheduleExecution);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in updateScheduleExecutionAsProcessing");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit :updateScheduleExecutionAsProcessing");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getMaxScheduleIdForSuit(int suitId, Date baselineDate) {
		logger.debug("Entry :getMaxScheduleIdForSuit");
		int scheduleId = 0;
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select schd from Schedule schd, ScheduleExecution sxe where schd.suitId=:suitId and schd.scheduleId in "
									+ "(select scheduleId from ScheduleExecution where testExecutionStatusRefId=3 and " +
									"testExecutionStartTime > :baseLinedateFrom and testExecutionStartTime < :baseLinedateTo) order by schd.scheduleId desc");
			query.setParameter("suitId", suitId);
			query.setTimestamp("baseLinedateFrom", DateTimeUtil.getStartOfTheDay(baselineDate));
			query.setTimestamp("baseLinedateTo", DateTimeUtil.getEndOfTheDay(baselineDate));
			scheduleList = query.list();
			if (null != scheduleList && scheduleList.size() > 0) {
				scheduleId = scheduleList.get(0).getScheduleId();
			}
		} catch (Exception e) {
			logger.error("Exception in findOndemandTests");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit :getMaxScheduleIdForSuit");
		return scheduleId;
	}

	@Override
	public boolean deleteSchedule(int scheduleId) {
		logger.debug("Entry :deleteSchedule");
		boolean isDelete = false;
		Query query;
		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"delete from Schedule where scheduleId =:scheduleId");
			query.setParameter("scheduleId", scheduleId);
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ScheduleExecution where scheduleId =:scheduleId");
			query.setParameter("scheduleId", scheduleId);
			query.executeUpdate();

			isDelete = true;

		} catch (Exception e) {
			logger.error("Exception in deleteSchedule");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit :deleteSchedule");
		return isDelete;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Schedule getSchedule(int scheduleId) {
		logger.debug("Entry: getSchedule");
		Schedule schedule = new Schedule();
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		String sqlQuery = "";
		try {

			sqlQuery = "from Schedule where scheduleId = :scheduleId";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("scheduleId", scheduleId);
			scheduleList = query.list();
			if (null != scheduleList && scheduleList.size() > 0) {
				schedule = scheduleList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getSchedule :" + e);
		}

		logger.debug("Exit: getSchedule");
		return schedule;
	}

/*	@SuppressWarnings("unchecked")
	@Override
	public ScheduleExecution getBaselineScheduleExecution(Integer suitId) {
		logger.debug("Entry: getBaselineScheduleExecution");
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();
		
		String sqlQuery = "";
		try {

			sqlQuery = "select se from ScheduleExecution se, Schedule s "
						+ "where se.scheduleId = s.scheduleId and se.testExecutionStatusRefId = :testExecutionStatusRefId and s.suitId = :suitId and "
						+ "(se.textComparisonStatusId = :textComparisonStatusId or se.textComparisonStatusId = :textComparisonStatusIdPend "
						+ "or se.htmlComparisonStatusId = :htmlComparisonStatusId or se.htmlComparisonStatusId = :htmlComparisonStatusIdPend "
						+ "or se.imageComparisonStatusId = :imageComparisonStatusId or se.imageComparisonStatusId = :imageComparisonStatusIdPend) "
						+ "order by se.testExecutionStartTime desc";
			
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("suitId", suitId);
			
			query.setParameter("testExecutionStatusRefId", ExecutionStatus.COMPLETE.getStatusCode());
			
			query.setParameter("textComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("textComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			query.setParameter("htmlComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("htmlComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			query.setParameter("imageComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("imageComparisonStatusIdPend", ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			
			scheduleExecutionList = query.list();
			if (null != scheduleExecutionList && scheduleExecutionList.size() > 0) {
				scheduleExecution = scheduleExecutionList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getBaselineScheduleExecution.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: getBaselineScheduleExecution");
		
		return scheduleExecution;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public ScheduleExecution getScheduleExecution(Integer scheduleExecutionId) {
		logger.debug("Entry: getScheduleExecution");
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();
		
		Query query = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery(
					"select se from ScheduleExecution se where scheduleExecutionId = :scheduleExecutionId");
			query.setParameter("scheduleExecutionId", scheduleExecutionId);
			
			scheduleExecutionList = query.list();
			
			if ( null != scheduleExecutionList && scheduleExecutionList.size() > 0 ){
				scheduleExecution = scheduleExecutionList.get(0);
			}
		}catch (Exception e) {
			logger.error("Exception in getBaselineScheduleExecution.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getScheduleExecution");
		
		return scheduleExecution;
	}

	@Override
	public boolean updateScheduleExecution(
			ScheduleExecution scheduleExecution) {
		logger.debug("Entry :updateScheduleExecution, ->scheduleExecution"+scheduleExecution);
		boolean result = false;
		try {
			scheduleExecution.setDateModified(new Date());
			sessionFactory.getCurrentSession().saveOrUpdate(scheduleExecution);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in updateScheduleExecution");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit :updateScheduleExecution");
		return result;
	}

	@Override
	public ScheduleExecutionDetail getPendingHtmlComparison() {
		ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s from "
									+ "ScheduleExecution se, Schedule s "
									+ "where se.scheduleId = s.scheduleId and ( se.testExecutionStatusRefId = :testExecutionStatusRefId ) and "
									+ "(se.textComparisonStatusId = :textComparisonStatusId or se.textComparisonStatusId = :textComparisonStatusIdFail) and (se.htmlComparisonStatusId = :htmlComparisonStatusId) "
									+ "order by se.testExecutionStartTime");
			
			query.setParameter("testExecutionStatusRefId", ExecutionStatus.COMPLETE.getStatusCode());
			
			query.setParameter("textComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("textComparisonStatusIdFail", ExecutionStatus.FAILED.getStatusCode());
			
			query.setParameter("htmlComparisonStatusId", ExecutionStatus.PENDING.getStatusCode());
			
			List<Object[]> objList = query.list();

			if (null != objList && objList.size() > 0) {
				scheduleExecutionDetail
						.setScheduleExecution((ScheduleExecution) objList
								.get(0)[0]);
				scheduleExecutionDetail
						.setSchedule((Schedule) objList.get(0)[1]);

				scheduleExecutionDetail.setSuitId(scheduleExecutionDetail.getSchedule().getSuitId());
				logger.info("New Schedule found for HTML Comparison :"
						+ scheduleExecutionDetail);
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingHtmlComparison, while quering StatusExecution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return scheduleExecutionDetail;
	}

	@Override
	public ScheduleExecutionDetail getPendingImageComparison() {
		ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s from "
									+ "ScheduleExecution se, Schedule s "
									+ "where se.scheduleId = s.scheduleId and ( se.testExecutionStatusRefId = :testExecutionStatusRefId ) and "
									+ "(se.textComparisonStatusId = :textComparisonStatusId ) and (se.htmlComparisonStatusId = :htmlComparisonStatusId or se.htmlComparisonStatusId = :htmlComparisonStatusIdFail )"
									+ "and (se.imageComparisonStatusId = :imageComparisonStatusId) "
									+ "order by se.testExecutionStartTime");
			
			query.setParameter("testExecutionStatusRefId", ExecutionStatus.COMPLETE.getStatusCode());
			
			query.setParameter("textComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			
			query.setParameter("htmlComparisonStatusId", ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("htmlComparisonStatusIdFail", ExecutionStatus.FAILED.getStatusCode());
			query.setParameter("imageComparisonStatusId", ExecutionStatus.PENDING.getStatusCode());
			
			List<Object[]> objList = query.list();

			if (null != objList && objList.size() > 0) {
				scheduleExecutionDetail
						.setScheduleExecution((ScheduleExecution) objList
								.get(0)[0]);
				scheduleExecutionDetail
						.setSchedule((Schedule) objList.get(0)[1]);

				scheduleExecutionDetail.setSuitId(scheduleExecutionDetail.getSchedule().getSuitId());
				logger.info("New Schedule found for Image Comparison :"
						+ scheduleExecutionDetail);
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingImageComparison, while quering StatusExecution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return scheduleExecutionDetail;
	}

	@Override
	public Map<Integer, ScheduleExecution> getScheduleExecutionMap(
			String scheduleExecutionIdStr) {
		logger.debug("Entry: getScheduleExecution");
	
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();
		Map<Integer, ScheduleExecution> scheduleExecutionMap = new HashMap<Integer, ScheduleExecution>();
		Query query = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery(
					"select se from ScheduleExecution se where scheduleExecutionId in(" +scheduleExecutionIdStr+")");
			scheduleExecutionList = query.list();
			
			if ( null != scheduleExecutionList && scheduleExecutionList.size() > 0 ){
				//scheduleExecution = scheduleExecutionList.get(0);
				for(ScheduleExecution scheduleExecution : scheduleExecutionList) {
					scheduleExecutionMap.put(scheduleExecution.getScheduleExecutionId(), scheduleExecution);
				}
			}
		}catch (Exception e) {
			logger.error("Exception in getBaselineScheduleExecution.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getScheduleExecution");
		
		return scheduleExecutionMap;
	}

	@Override
	public Map<Integer, Schedule> getScheduleMap(String scheduleIdStr) {
		logger.debug("Entry: getScheduleMap");
		//Schedule schedule = new Schedule();
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		Map<Integer, Schedule> scheduleMap = new HashMap<Integer, Schedule>();
		String sqlQuery = "";
		try {
			sqlQuery = "from Schedule where scheduleId in (" + scheduleIdStr +")";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
		//	query.setParameter("scheduleId", scheduleId);
			scheduleList = query.list();
			if (null != scheduleList && scheduleList.size() > 0) {
			//	schedule = scheduleList.get(0);
				for(Schedule schedule : scheduleList) {
					scheduleMap.put(schedule.getScheduleId(), schedule);
				}
			}

		} catch (Exception e) {
			logger.error("Exception in getSchedule :" + e);
		}

		logger.debug("Exit: getSchedule");
		return scheduleMap;
	}

	@Override
	public List<SuitTextImageXref> getTextAndImageDetailsFortheSuitId(int suitId) {
		logger.debug("Entry : getTextAndImageDetailsFortheSuitId");
		List<SuitTextImageXref> suitTextImageXrefList = new ArrayList<SuitTextImageXref>();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery("from SuitTextImageXref where suitId = :suitId");
			query.setParameter("suitId", suitId);
			suitTextImageXrefList = query.list();
		}catch(Exception e){
			logger.error("Exception in getTextAndImageDetailsFortheSuitId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit : getTextAndImageDetailsFortheSuitId");
		return suitTextImageXrefList;
	}
	
	@Override
	public List<ScheduleExecutionDetail> getScheduleDetails(UsageReportForm usageReport, Date fromDate, Date toDate){
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = new ArrayList<ScheduleExecutionDetail>();
		ScheduleExecutionDetail scheduleExecutionDetail = null;
		Query query = null;
		String sqlQuery ="";
		try {
			
			sqlQuery = "select se, s from ScheduleExecution se, Schedule s, Suit suit, Users user ";
			String where = this.addSearchFilters(usageReport, fromDate, toDate);
			sqlQuery += where;
			sqlQuery += "order by se.testExecutionStartTime desc";
			 query = sessionFactory.getCurrentSession().createQuery(sqlQuery);
			 
			 if (fromDate != null && toDate != null) {
					query.setTimestamp("startDateTime",
							DateTimeUtil.getStartOfTheDay(fromDate));
					query.setTimestamp("endDateTime",
							DateTimeUtil.getEndOfTheDay(toDate));
				}
			 List<Object[]> objList = query.list();

				if (null != objList && objList.size() > 0) {
					for(Object[] obj: objList){
						scheduleExecutionDetail = new ScheduleExecutionDetail();
						scheduleExecutionDetail.setScheduleExecution((ScheduleExecution) obj[0]);
						scheduleExecutionDetail.setSchedule((Schedule) obj[1]);
						scheduleExecutionDetail.setSuitId(scheduleExecutionDetail.getSchedule().getSuitId());
						scheduleExecutionDetailList.add(scheduleExecutionDetail);
					}
				}
		} catch(Exception e) {
			
		}
		return scheduleExecutionDetailList;
	} 
	
	private String addSearchFilters(UsageReportForm usageReport, Date fromDate,
			Date toDate) {
		String sqlQuery = "where se.scheduleId = s.scheduleId and s.suitId = suit.suitId  and user.userId = suit.userId ";
	

		if (usageReport.getSolutionType() != null
				&& usageReport.getSolutionType().length > 0
				&& Integer.parseInt(usageReport.getSolutionType()[0]) == SolutionType.UI_TESTING
				.getSolutionTypeId()) {

			if (usageReport.getEnvironmentCategoryId() != null
					&& usageReport.getEnvironmentCategoryId().length > 0
					&& !StringUtils.containsAny(
							UsageReportConstants.ALL_ENVIRONMENT, Arrays
							.toString(usageReport
									.getEnvironmentCategoryId()))) {
				sqlQuery += " and suit.environmentCategoryId in ("
					+ this.getParams(usageReport.getEnvironmentCategoryId())
					+ ") ";
				
			}

			if (usageReport.getGroupName() != null
					&& usageReport.getGroupName().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getGroupName()))) {
				sqlQuery += " and user in ("
					+ this.getParams(usageReport.getGroupName()) + ") ";
				
			}

			if (usageReport.getUsers() != null
					&& usageReport.getUsers().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getUsers()))) {
				sqlQuery += " and suit.userId in ("
					+ this.getParams(usageReport.getUsers()) + ") ";
				
			}

			if (usageReport.getFunctionality() != null
					&& usageReport.getFunctionality().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getFunctionality()))) {
				sqlQuery += " and suit.functionalityTypeId in ("
					+ this.getParams(usageReport.getFunctionality()) + ")";
				
			}
			if (fromDate != null && toDate != null) {
				
				sqlQuery += " and se.testExecutionStartTime >=:startDateTime and setestExecutionEndTime <=:endDateTime";

			}

		}
		sqlQuery = sqlQuery.replace("where  and", "where");
		
		return sqlQuery;
	}

	private String getParams(String[] str) {
		return Arrays.toString(str).replace("[", "").replace("]", "");
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<String> deleteScheduleExecutions(String scheduleExecutionId) {
		logger.debug("Entry :deleteScheduleExecutions");
		boolean isDelete = false;
		
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		List<String> directoriesTobeDeleted = new ArrayList();
		Schedule schedule = new Schedule();
		String sqlQuery ="";
		Query query = null;
		try {
			
			// Checking whether the schedule is recurrrence
			sqlQuery = "select se, s from ScheduleExecution se, Schedule s where scheduleExecutionId in (" +scheduleExecutionId + 
					" ) and se.scheduleId = s.scheduleId";
			query = sessionFactory.getCurrentSession().createQuery(sqlQuery);
			//	query.setParameter("scheduleExecutionId", scheduleExecutionId);
			
			 List<Object[]> objList = query.list();
			 	
			 	//Deleting ScheduleExecution table
				query = sessionFactory.getCurrentSession()
				.createQuery("delete from ScheduleExecution where scheduleExecutionId in ("+ scheduleExecutionId+")");;
				//	query.setParameter("scheduleExecutionId", scheduleExecutionId);
				query.executeUpdate();
				isDelete = true;
				
				//Deleting Results table
				query = sessionFactory.getCurrentSession()
				.createQuery("delete from Results where scheduleExecutionId in ("+ scheduleExecutionId+")");;
				//	query.setParameter("scheduleExecutionId", scheduleExecutionId);
				query.executeUpdate();
				isDelete = true;
				
				//Deleting Reports table
				query = sessionFactory.getCurrentSession()
				.createQuery("delete from Reports where scheduleExecutionId in ("+ scheduleExecutionId+")");;
				//	query.setParameter("scheduleExecutionId", scheduleExecutionId);
				query.executeUpdate();
				isDelete = true;
			 
				if (null != objList && objList.size() > 0) {
					for(Object[] obj: objList){
						scheduleExecution = (ScheduleExecution) obj[0];
						schedule = (Schedule) obj[1];
						
						//If not recurrence and the scheduleExecution is dleted, deleting the schedule 	
						if(!schedule.isRecurrence()){	
							query = sessionFactory.getCurrentSession()
							.createQuery("delete from Schedule where scheduleId  = "+ scheduleExecution.getScheduleId());
							//	query.setParameter("scheduleExecutionId", scheduleExecutionId);
							query.executeUpdate();
							isDelete = true;
						}
						if(isDelete) {
							directoriesTobeDeleted.add(scheduleExecution.getCrawlStatusDirectory().replace("serialize", ""));
						} 
					}
				}
				
		} catch (Exception e) {
			logger.error("Exception in deleteScheduleExecutions");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit :deleteSchedule");
		return directoriesTobeDeleted;
	}
	
}
