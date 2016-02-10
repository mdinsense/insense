package com.ensense.insense.data.schedule.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.cts.mint.common.model.PartialText;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.dao.ScheduledDAO;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.HtmlReportsConfig;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;

@Service
public class ScheduledDAOImpl implements ScheduledDAO{
	
	private static Logger logger = Logger.getLogger(ScheduledDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	

	@Autowired
	private MessageSource configProperties; //Added by vijay - for email
	
	private static Cipher dcipher;
	
	@Override
	public boolean setCompletion(ScheduleDetails appConf,
			boolean runStatus){
		logger.debug("Entry : setCompletion");
		boolean result =false;
		
		int executionStatus = ExecutionStatus.COMPLETE.getStatusCode();
		
		if ( runStatus == false ){
			executionStatus = ExecutionStatus.FAILED.getStatusCode();
		}
		try{
			if( appConf!=null ){
				ScheduleDetails tCase = appConf;//Take any one of the object for the execution id.
				Query updateStatus = sessionFactory.getCurrentSession().
				createQuery("Update ScheduleExecution se set testExecutionStatusRefId = :executionStatus " +//TODO Properties for Status id
						" where scheduleExecutionId = :scheduleExecutionId ");
				updateStatus.setParameter("executionStatus", executionStatus);
				updateStatus.setParameter("scheduleExecutionId", tCase.getExecutionId());
				//updateStatus.setParameter("currentTimeStamp", DateTimeUtil.formatDateString(new Date().toString(), Constants.DATE_TIME_FORMAT));
				updateStatus.executeUpdate();
				result = true;	
			}
		}catch(Exception exp){
			logger.warn("Exception in setCompletion method ",exp);
			System.out.println(exp);
			exp.printStackTrace(System.out);
		
		}
		logger.debug("Exit : setCompletion");
		return result;
	}

	@Override
	public List<ScheduleExecutionDetail> getPendingInProgressScheduleDetails() {
		logger.debug("Entry: getPendingInProgressScheduleDetails");
		
		Query query = null;
		List<ScheduleExecutionDetail> scheduleExecutionList = new ArrayList<ScheduleExecutionDetail>();

		ScheduleExecutionDetail scheduleExecutionDetail = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s, aecx from "
									+ "ScheduleExecution se, Schedule s, Suit suit, AppEnvEnvironmentCategoryXref aecx "
									+ "where se.scheduleId = s.scheduleId and ( se.testExecutionStatusRefId = :testExecutionStatusRefIdPend or "
									+ "se.testExecutionStatusRefId = :testExecutionStatusRefIdProce or se.testExecutionStatusRefId = :testExecutionStatusRefIdPause or "
									+ "se.testExecutionStatusRefId = :testExecutionStatusRefIdRestart) and "
									+ "s.suitId = suit.suitId and aecx.applicationId = s.applicationId and aecx.environmentId = s.environmentId and "
									+ "aecx.environmentCategoryId = suit.environmentCategoryId "
									+ "order by se.scheduleExecutionId");
			
			query.setParameter("testExecutionStatusRefIdPend", ExecutionStatus.PENDING.getStatusCode());
			query.setParameter("testExecutionStatusRefIdProce", ExecutionStatus.IN_PROGRESS.getStatusCode());
			query.setParameter("testExecutionStatusRefIdPause", ExecutionStatus.PAUSED.getStatusCode());
			query.setParameter("testExecutionStatusRefIdRestart", ExecutionStatus.RESTART.getStatusCode());
	
			
			@SuppressWarnings("unchecked")
			List<Object[]> objList = query.list();

			for(Object[] object: objList){
				scheduleExecutionDetail = new ScheduleExecutionDetail();
				scheduleExecutionDetail
						.setScheduleExecution((ScheduleExecution) object[0]);

				scheduleExecutionDetail
						.setSchedule((Schedule) object[1]);

				scheduleExecutionDetail.setSuitId(scheduleExecutionDetail.getSchedule().getSuitId());
				scheduleExecutionDetail.setAppEnvEnvironmentCategoryXref((AppEnvEnvironmentCategoryXref) object[2]);
				
				scheduleExecutionList.add(scheduleExecutionDetail);
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingInProgressScheduleDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: getPendingInProgressScheduleDetails");
		return scheduleExecutionList;
	}

	@Override
	public List<PartialText> getPartialTextCompareList(int applicationId, int environmentId) {
		logger.debug("Entry: getPartialTextCompareList, applicationId->"+applicationId+" environmentId->"+environmentId);
		List<HtmlReportsConfig> htmlReportsConfigList = new ArrayList<HtmlReportsConfig>();
		
		List<PartialText> partialTextCompareList = new ArrayList<PartialText>();
		
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select hrc from HtmlReportsConfig hrc where hrc.applicationId = :applicationId and hrc.environmentId = :environmentId and splitByContent = 1");
			
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);
			htmlReportsConfigList = query.list();
			
			if ( null != htmlReportsConfigList && htmlReportsConfigList.size() > 0 ){
				for ( HtmlReportsConfig htmlReportsConfig : htmlReportsConfigList){
					PartialText partialText = new PartialText();
					partialText.setContentName(htmlReportsConfig.getSplitContentName());
					partialText.setHtmlTagPath(htmlReportsConfig.getSplitTagName());
					partialTextCompareList.add(partialText);
				}
			}
		}catch (Exception e) {
			logger.error("Exception in getPartialTextCompareList.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: getPartialTextCompareList");
		return partialTextCompareList;
	}
}
