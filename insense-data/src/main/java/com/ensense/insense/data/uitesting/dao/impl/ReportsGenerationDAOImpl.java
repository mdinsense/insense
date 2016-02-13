package com.ensense.insense.data.uitesting.dao.impl;

import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.ScheduleExecutionDetail;
import com.ensense.insense.data.uitesting.dao.ReportsGenerationDAO;
import com.ensense.insense.data.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.ensense.insense.data.uitesting.entity.Schedule;
import com.ensense.insense.data.uitesting.entity.ScheduleExecution;
import com.ensense.insense.data.uitesting.entity.SuitBrokenReportsXref;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReportsGenerationDAOImpl implements ReportsGenerationDAO {
	private static Logger logger = Logger
			.getLogger(ReportsGenerationDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ScheduleExecutionDetail getPendingReportGeneration() {
		ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();

		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s, aecx from "
									+ "ScheduleExecution se, Schedule s, Suit suit, AppEnvEnvironmentCategoryXref aecx "
									+ "where se.scheduleId = s.scheduleId and se.testExecutionStatusRefId = :testExecutionStatusRefId and "
									+ "se.reportStatusRefId = :reportStatusRefId and s.suitId = suit.suitId and aecx.applicationId = s.applicationId and aecx.environmentId = s.environmentId "
									+ "order by se.testExecutionStartTime");

			query.setParameter("testExecutionStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("reportStatusRefId",
					ExecutionStatus.PENDING.getStatusCode());

			@SuppressWarnings("unchecked")
			List<Object[]> objList = query.list();

			if (null != objList && objList.size() > 0) {
				scheduleExecutionDetail
						.setScheduleExecution((ScheduleExecution) objList
								.get(0)[0]);
				scheduleExecutionDetail
						.setSchedule((Schedule) objList.get(0)[1]);

				scheduleExecutionDetail.setSuitId(scheduleExecutionDetail
						.getSchedule().getSuitId());
				scheduleExecutionDetail
						.setAppEnvEnvironmentCategoryXref((AppEnvEnvironmentCategoryXref) objList
								.get(0)[2]);
				logger.info("New Schedule found for Reports Generation, Schedule Id :"
						+ scheduleExecutionDetail.getSchedule().getScheduleId()
						+ ", Schedule Execution Id :"
						+ scheduleExecutionDetail.getScheduleExecution()
								.getScheduleExecutionId());
			}
		} catch (Exception e) {
			logger.error("Exception in getPendingBrokenLinkReport.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return scheduleExecutionDetail;
	}

	@Override
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXrefList(int suitId) {
		logger.debug("Entry: getSuitBrokenReportsXrefList, suitId->"+suitId);
		Query query = null;
		List<SuitBrokenReportsXref> suitBrokenReportsXrefList = new ArrayList<SuitBrokenReportsXref>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from SuitBrokenReportsXref " + " where suitId=:suitId");
			query.setParameter("suitId", suitId);
			suitBrokenReportsXrefList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getSuitBrokenReportsXrefList :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSuitBrokenReportsXrefList, suitBrokenReportsXrefList->"+suitBrokenReportsXrefList);
		return suitBrokenReportsXrefList;
	}

}
