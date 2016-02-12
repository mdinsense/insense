package com.ensense.insense.data.common.dao.impl;

import com.ensense.insense.data.common.dao.HomeDAO;
import com.ensense.insense.data.common.entity.*;
import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.ScheduleExecutionDetail;
import com.ensense.insense.data.common.model.UsageReportConstants;
import com.ensense.insense.data.common.model.UsageReportForm;
import com.ensense.insense.data.common.util.DateTimeUtil;
import com.ensense.insense.data.uitesting.entity.*;
import com.ensense.insense.data.webservice.entity.WebserviceSuite;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class HomeDAOImpl implements HomeDAO {
	private static Logger logger = Logger.getLogger(HomeDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getUiRegressionSuitForGroup(int groupId) {
		logger.debug("Entry: getUiRegressionSuitForGroup");
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
			// "from Suit where userId in "
			// + "(select userId from Users where groupId = :groupId)");
					" select a from Suit a,SuitGroupXref b "
							+ " where b.suitId = a.suitId "
							+ " and b.groupId = :groupId"
							+ " and a.privateSuit = :privateSuit");
			query.setParameter("groupId", groupId);
			query.setParameter("privateSuit", false);

		} catch (Exception e) {
			logger.error("Exception in getUiRegressionSuitForGroup :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUiRegressionSuitForGroup");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getUiRegressionSuitForUser(int userId) {
		logger.debug("Entry: getUiRegressionSuitForUser");
		Query query = null;
		List<Suit> UiRegressionSuitList = new ArrayList<Suit>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select a from Suit a,SuitGroupXref b "
									+ " where b.suitId = a.suitId "
									+ " and b.groupId = (select groupId from Users where userId=:userId)"
									+ " and a.privateSuit = :privateSuit "
									+ " and a.userId = :userId order by a.suitId desc");
			query.setParameter("privateSuit", true);
			query.setParameter("userId", userId);
			UiRegressionSuitList = query.list();
		} catch (Exception exp) {
			logger.error("Exception in getUiRegressionSuitForUser :" + exp);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: getUiRegressionSuitForUser");

		return UiRegressionSuitList;
	}

	@Override
	public boolean saveMintProperty(MintProperties mintProperties)
			throws Exception {
		logger.debug("Entry: saveMintProperty");
		boolean isSaved = false;
		sessionFactory.getCurrentSession().save(mintProperties);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		isSaved = true;
		logger.debug("Exit: saveMintProperty");
		return isSaved;
	}

	@Override
	public boolean saveUsageReport(UsageReport usageReport) throws Exception {
		logger.debug("Entry: saveUsageReport");
		boolean isSaved = false;
		sessionFactory.getCurrentSession().save(usageReport);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		isSaved = true;
		logger.debug("Exit: saveUsageReport");
		return isSaved;
	}

	@Override
	public boolean updateUsageReport(UsageReport usageReport) throws Exception {
		logger.debug("Entry: updateUsageReport");
		boolean isSaved = false;
		sessionFactory.getCurrentSession().update(usageReport);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		isSaved = true;
		logger.debug("Exit: updateUsageReport");
		return isSaved;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UsageReport getUsageReport(int referenceId) throws Exception {
		logger.debug("Entry: getUsageReport");
		Query query = null;
		UsageReport usageReport = new UsageReport();
		List<UsageReport> usageReportList = new ArrayList<UsageReport>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from UsageReport where referenceId = :referenceId");
			query.setParameter("referenceId", referenceId);
			usageReportList = query.list();
			if (usageReportList != null && usageReportList.size() > 0) {
				usageReport = usageReportList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getUsageReport :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUsageReport");

		return usageReport;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsageReport> getUsageReportList(UsageReportForm usageReport,
			Date fromDate, Date toDate) throws Exception {
		logger.debug("Entry: getUsageReportList");
		Query query = null;
		List<UsageReport> usageReports = new ArrayList<UsageReport>();
		try {
			String queryString = "from UsageReport ";
			String where = this.addSearchFilters(usageReport, fromDate, toDate);
			queryString += where;
			queryString += "order by startDateTime desc";
			query = sessionFactory.getCurrentSession().createQuery(queryString);
			if (fromDate != null && toDate != null) {
				query.setTimestamp("startDateTime",
						DateTimeUtil.getStartOfTheDay(fromDate));
				query.setTimestamp("endDateTime",
						DateTimeUtil.getEndOfTheDay(toDate));
			}
			usageReports = query.list();
		} catch (Exception e) {
			logger.error("Exception in getUsageReport :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUsageReportList");
		return usageReports;
	}

	private String addSearchFilters(UsageReportForm usageReport, Date fromDate,
			Date toDate) {
		String queryString = "where ";
		boolean haveFilter = false;
		if (usageReport != null) {
			if (usageReport.getSolutionType() != null
					&& usageReport.getSolutionType().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getSolutionType()))) {
				queryString += " and solutionTypeId in ("
						+ this.getParams(usageReport.getSolutionType()) + ") ";
				haveFilter = true;
			}
			if (usageReport.getEnvironmentCategoryId() != null
					&& usageReport.getEnvironmentCategoryId().length > 0
					&& !StringUtils.containsAny(
							UsageReportConstants.ALL_ENVIRONMENT, Arrays
									.toString(usageReport
											.getEnvironmentCategoryId()))) {
				queryString += " and environmentId in ("
						+ this.getParams(usageReport.getEnvironmentCategoryId())
						+ ") ";
				haveFilter = true;
			}
			if (usageReport.getGroupName() != null
					&& usageReport.getGroupName().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getGroupName()))) {
				queryString += " and groupId in ("
						+ this.getParams(usageReport.getGroupName()) + ") ";
				haveFilter = true;
			}
			if (usageReport.getUsers() != null
					&& usageReport.getUsers().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getUsers()))) {
				queryString += " and userId in ("
						+ this.getParams(usageReport.getUsers()) + ") ";
				haveFilter = true;
			}
			if (usageReport.getFunctionality() != null
					&& usageReport.getFunctionality().length > 0
					&& !StringUtils.containsAny(UsageReportConstants.ALL,
							Arrays.toString(usageReport.getFunctionality()))) {
				queryString += " and functionalityTypeId in ("
						+ this.getParams(usageReport.getFunctionality()) + ")";
				haveFilter = true;
			}
		}
		if (fromDate != null && toDate != null) {
			haveFilter = true;
			queryString += " and startDateTime >=:startDateTime and endDateTime <=:endDateTime";
		}
		queryString = queryString.replace("where  and", "where");
		if (!haveFilter) {
			queryString = "";
		}
		return queryString;
	}

	private String getParams(String[] str) {
		return Arrays.toString(str).replace("[", "").replace("]", "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SolutionType> getAllSolutionTypes() {
		logger.debug("Entry: getAllSolutionTypes");
		Criteria criteria = null;
		List<SolutionType> solutionTypeList = new ArrayList<SolutionType>();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(
					SolutionType.class);
			solutionTypeList = criteria.list();
		} catch (Exception e) {
			logger.error("Exception in getAllSolutionTypes :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getAllSolutionTypes");
		return solutionTypeList;
	}

	@Override
	public boolean deleteAllMintPropertuies(MintProperties mintProperties)
			throws Exception {
		logger.debug("Entry: deleteAllMintPropertuies");
		boolean isDeleted = false;
		String stringQuery = "DELETE FROM MintProperties";
		Query query = sessionFactory.getCurrentSession().createQuery(
				stringQuery);
		query.executeUpdate();
		isDeleted = true;
		logger.debug("Exit: deleteAllMintPropertuies");
		return isDeleted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BrowserType> getBrowserTypes() {

		logger.debug("Entry: getBrowserTypes");
		Query query = null;
		List<BrowserType> browserTypeList = new ArrayList<BrowserType>();

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"FROM BrowserType order by browserTypeId");
			browserTypeList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getBrowserTypes :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getBrowserTypes");
		return browserTypeList;
	}

	@Override
	public Suit getSavedSuits(int suitId) {
		logger.debug("Entry: getSavedSuits, suitId->"+suitId);
		Query query = null;

		Suit suits = new Suit();
		List<Suit> suitList = new ArrayList<Suit>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Suit where suitId =:suitId ");

			query.setParameter("suitId", suitId);
			suitList = query.list();
			
			if ( null != suitList && suitList.size() > 0 ){
				suits = (Suit)query.list().get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getSavedSuits :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSavedSuits");
		return suits;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getSavedSuitsCreatedByAll() {
		logger.debug("Entry: getMintHomeOndemand");
		Query query = null;
		List<Suit> suites = new ArrayList<Suit>();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Suit");
			suites = query.list();
		} catch (Exception e) {
			logger.error("Exception in getSavedSuitsCreatedByAll :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getMintHomeOndemand");
		return suites;
	}

	@Override
	public boolean saveSchedule(Schedule ts) {
		logger.debug("Entry: saveSchedule");
		boolean isSaved = false;
		try {
			sessionFactory.getCurrentSession().save(ts);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			isSaved = true;
		} catch (Exception e) {
			logger.error("Exception in saveSchedule");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSchedule");

		return isSaved;
	}

	@Override
	public boolean removeSuitDetails(Suit suit) {
		logger.debug("Entry: removeSuitDetails");
		// boolean result = true;
		boolean isDeleted = true;
		Query query;
		List<ScheduleScriptXref> scriptXrefList = null;
		String scriptIds = "";
		try {
			/*
			 * sessionFactory.getCurrentSession().delete( suit);
			 */
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from ScheduleScriptXref " +
							"where scheduleId IN (Select scheduleId from Schedule where suitId =:suitId)");
			query.setParameter("suitId", suit.getSuitId());
			scriptXrefList = query.list();
			for(ScheduleScriptXref sx : scriptXrefList) {
				scriptIds += sx.getScheduleScriptId() + ",";
			}
			if(scriptIds.endsWith(",")) {
				scriptIds = StringUtils.substringBeforeLast(scriptIds, ",");
			}
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ScheduleScriptXref " +
							"where scheduleId IN (Select scheduleId from Schedule where suitId =:suitId)");
			query.setParameter("suitId", suit.getSuitId());
			query.executeUpdate();
			
			if(scriptXrefList != null && scriptXrefList.size() > 0) {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"delete from ScheduleScript " +
								"where scheduleScriptId IN ("+scriptIds+")");
				query.executeUpdate();
			}

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ScheduleExecution where scheduleId IN (Select scheduleId from Schedule where suitId =:suitId)");
			query.setParameter("suitId", suit.getSuitId());
			query.executeUpdate();

			query = sessionFactory.getCurrentSession().createQuery(
					"delete from Schedule where suitId =:suitId");
			query.setParameter("suitId", suit.getSuitId());
			query.executeUpdate();
			
			query = sessionFactory.getCurrentSession().createQuery(
					"delete from SuitTextImageXref where suitId =:suitId");
			query.setParameter("suitId", suit.getSuitId());
			query.executeUpdate();

			query = sessionFactory.getCurrentSession().createQuery(
					"delete from SuitGroupXref where suitId =:suitId");
			query.setParameter("suitId", suit.getSuitId());
			query.executeUpdate();

			sessionFactory.getCurrentSession().delete(suit);
			sessionFactory.getCurrentSession().flush();

			isDeleted = true;

		} catch (Exception e) {
			isDeleted = false;
			logger.error("Exception in removeSuitDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: removeSuitDetails");
		return isDeleted;

	}

	@Override
	public boolean addFeedBack(FeedBack feedBack) {
		logger.debug("Entry: addFeedBack");
		boolean isSaved = false;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(feedBack);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			isSaved = true;
		} catch (Exception e) {
			logger.error("Exception in addFeedBack");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: addFeedBack");

		return isSaved;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuitGroupXref> getManageSuitDetails(int suitId) {
		logger.debug("Entry: getManageSuitDetails");
		Query query = null;
		List<SuitGroupXref> suitGroupXrefList = new ArrayList<SuitGroupXref>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from SuitGroupXref where suitId = :suitId");
			query.setParameter("suitId", suitId);
			suitGroupXrefList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getManageSuitDetails :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getManageSuitDetails");

		return suitGroupXrefList;
	}

	@Override
	public boolean deleteSuitGroupXref(int suitId) {
		logger.debug("Entry: deleteSuitGroupXref");
		boolean result = true;
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"delete from SuitGroupXref where suitId=:suitId");
			query.setParameter("suitId", suitId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteSuitGroupXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteSuitGroupXref");
		return result;
	}

	@Override
	public boolean addSuitGroupXref(SuitGroupXref suitGroup) {
		logger.debug("Entry: addSuitGroupXref");
		boolean isSaved = false;
		try {
			sessionFactory.getCurrentSession().save(suitGroup);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			isSaved = true;
		} catch (Exception e) {
			logger.error("Exception in addSuitGroupXref");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: addSuitGroupXref");

		return isSaved;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getManageSuitsForGroup(int groupId) {
		logger.debug("Entry: getUiRegressionSuitForGroup");
		Query query = null;
		List<Suit> suitList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" select a from Suit a,SuitGroupXref b "
							+ " where b.suitId = a.suitId "
							+ " and b.groupId = :groupId");
			query.setParameter("groupId", groupId);

			suitList = query.list();
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select bb from Suit bb where bb.userId in (select uu.userId from Users uu where uu.groupId = :groupId) "
									+ " and bb.suitId not in (select inn.suitId from SuitGroupXref inn where inn.groupId = :groupId)");

			query.setParameter("groupId", groupId);
			suitList.addAll(query.list());

		} catch (Exception e) {
			logger.error("Exception in getUiRegressionSuitForGroup :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUiRegressionSuitForGroup");

		return suitList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getManageSuitsForGroupUser(int groupId, int userId) {
		logger.debug("Entry: getUiRegressionSuitForGroup");
		List<Suit> suitList = null;
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" select a from Suit a,SuitGroupXref b "
							+ " where b.suitId = a.suitId "
							+ " and b.groupId = :groupId ");
			query.setParameter("groupId", groupId);
			suitList = query.list();
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select a from Suit a where a.suitId not in (select inn.suitId from SuitGroupXref inn) and a.userId = :userId");

			query.setParameter("userId", userId);
			suitList.addAll(query.list());
		} catch (Exception e) {
			logger.error("Exception in getUiRegressionSuitForGroup :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUiRegressionSuitForGroup");

		return suitList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUserHaveAccessToSuit(int groupId, int suitId) {
		boolean haveAccess = false;
		logger.debug("Entry: isUserHaveAccessToSuit");
		Query query = null;
		List<SuitGroupXref> suitGroupList = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from SuitGroupXref where groupId=:groupId and suitId=:suitId");
			query.setParameter("groupId", groupId);
			query.setParameter("suitId", suitId);
			suitGroupList = query.list();
			if (suitGroupList != null && suitGroupList.size() > 0) {
				haveAccess = true;
			}
		} catch (Exception e) {
			logger.error("Exception in isUserHaveAccessToSuit :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isUserHaveAccessToSuit");

		return haveAccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Suit getSuitByName(String suitName) {
		logger.debug("Entry: getSuitByName");
		Query query = null;
		Suit suits = new Suit();
		List<Suit> suitList = new ArrayList<Suit>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Suit where suitName=:suitName ");

			query.setParameter("suitName", suitName);
			suitList = query.list();

			if (null != suitList && suitList.size() > 0) {
				suits = suitList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getSuitByName :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSuitByName");
		return suits;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suit> getManageSuitsForAllSuits() {
		logger.debug("Entry: getManageSuitsForAllSuits");
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Suit order by suitId DESC");

		} catch (Exception e) {
			logger.error("Exception in getUiRegressionSuitForGroup :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getManageSuitsForAllSuits");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleExecutionDetail> getCompletedScheduleDetailsForSuit(
			int suitId) {
		logger.debug("Entry: getCompletedScheduleDetailsForSuit, suitId->"
				+ suitId);

		Query query = null;
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = new ArrayList<ScheduleExecutionDetail>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se, s from Schedule s, ScheduleExecution se where s.scheduleId = se.scheduleId "
									+ "and s.suitId = :suitId and ( (testExecutionStatusRefId = :testExecutionStatusRefIdFail) "
									+ "or (( reportStatusRefId = :reportStatusRefId or reportStatusRefId = :reportStatusRefIdNa or reportStatusRefId = :reportStatusRefIdFail) "
									+ "and ( comparisonStatusRefId = :comparisonStatusRefId or comparisonStatusRefId = :comparisonStatusRefIdNa or comparisonStatusRefId = :comparisonStatusRefIdFail) "
									+ "and ( testExecutionStatusRefId = :testExecutionStatusRefId)))"
									+ "order by se.testExecutionStartTime desc");

			query.setParameter("suitId", suitId);
			query.setParameter("reportStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("comparisonStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("testExecutionStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("testExecutionStatusRefIdFail",
					ExecutionStatus.FAILED.getStatusCode());

			query.setParameter("reportStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("comparisonStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());

			query.setParameter("reportStatusRefIdFail",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("comparisonStatusRefIdFail",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());

			List<Object[]> objList = query.list();

			if (null != objList && objList.size() > 0) {
				for (Object[] object : objList) {
					ScheduleExecutionDetail scheduleExecutionDetail = new ScheduleExecutionDetail();

					scheduleExecutionDetail
							.setScheduleExecution((ScheduleExecution) object[0]);
					scheduleExecutionDetail.setSchedule((Schedule) object[1]);

					scheduleExecutionDetailList.add(scheduleExecutionDetail);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getScheduleDetailsForSuit :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getCompletedScheduleDetailsForSuit");
		return scheduleExecutionDetailList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleExecution> getInProgressScheduleDetailsForSuit(
			int suitId) {
		logger.debug("Entry: getInProgressScheduleDetailsForSuit, suitId->"
				+ suitId);

		Query query = null;
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se from Schedule s, ScheduleExecution se where s.scheduleId = se.scheduleId "
									+ "and s.suitId = :suitId and testExecutionStatusRefId != :testExecutionStatusRefIdInFail and "
									+ "(reportStatusRefId = :reportStatusRefIdPend or reportStatusRefId =:reportStatusRefIdInPro  or "
									+ "comparisonStatusRefId = :comparisonStatusRefIdPend or comparisonStatusRefId = :comparisonStatusRefIdInPro or "
									+ "testExecutionStatusRefId = :testExecutionStatusRefIdPend or testExecutionStatusRefId = :testExecutionStatusRefIdInPro or "
									+ "testExecutionStatusRefId = :testExecutionStatusRefIdRestart) "
									+ "order by se.testExecutionStartTime desc");

			query.setParameter("suitId", suitId);
			query.setParameter("reportStatusRefIdPend",
					ExecutionStatus.PENDING.getStatusCode());
			query.setParameter("reportStatusRefIdInPro",
					ExecutionStatus.IN_PROGRESS.getStatusCode());

			query.setParameter("comparisonStatusRefIdPend",
					ExecutionStatus.PENDING.getStatusCode());
			query.setParameter("comparisonStatusRefIdInPro",
					ExecutionStatus.IN_PROGRESS.getStatusCode());

			query.setParameter("testExecutionStatusRefIdInPro",
					ExecutionStatus.IN_PROGRESS.getStatusCode());
			query.setParameter("testExecutionStatusRefIdPend",
					ExecutionStatus.PENDING.getStatusCode());
			query.setParameter("testExecutionStatusRefIdRestart",
					ExecutionStatus.RESTART.getStatusCode());

			query.setParameter("testExecutionStatusRefIdInFail",
					ExecutionStatus.FAILED.getStatusCode());
			scheduleExecutionList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getScheduleDetailsForSuit :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getInProgressScheduleDetailsForSuit");
		return scheduleExecutionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Schedule> getFutureScheduleDetailsForSuit(int suitId) {
		logger.debug("Entry: getScheduleDetailsForSuit, suitId->" + suitId);
		Query query = null;
		List<Schedule> scheduleExecutionList = new ArrayList<Schedule>();

		try {
			// Fetch OnDemand
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s where "
									+ "s.suitId=:suitId and s.recurrence = false and s.isScheduled = false "
									+ "and s.scheduleId not in (select sx.scheduleId from ScheduleExecution sx) order by s.scheduleId desc");
			query.setParameter("suitId", suitId);
			scheduleExecutionList = query.list();

			// Fetch Scheduled by Date
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s where "
									+ "s.suitId=:suitId and s.recurrence = false and s.isScheduled = true and scheduleType = 'D' "
									+ "and s.scheduleId not in (select sx.scheduleId from ScheduleExecution sx) "
									+ "order by s.scheduleId desc");
			query.setParameter("suitId", suitId);
			scheduleExecutionList.addAll(query.list());

			// Fetch Scheduled by WeekDay, //and s.recurrence = true
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select s from Schedule s where "
									+ "s.suitId=:suitId and s.isScheduled = true and scheduleType = 'W' "
									+ "and s.scheduleId not in (select sx.scheduleId from ScheduleExecution sx) "
									+ "order by s.scheduleId desc");

			query.setParameter("suitId", suitId);

			scheduleExecutionList.addAll(query.list());
		} catch (Exception e) {
			logger.error("Exception in getScheduleDetailsForSuit :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getScheduleDetailsForSuit");
		return scheduleExecutionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleExecution> getBaselineScheduleExcutionList(int suitId) {
		logger.debug("Entry: getBaselineScheduleExcutionList, suitId->"
				+ suitId);

		Query query = null;
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se from Schedule s, ScheduleExecution se where s.scheduleId = se.scheduleId "
									+ "and s.suitId = :suitId and ( reportStatusRefId = :reportStatusRefId or reportStatusRefId = :reportStatusRefIdNa ) "
									+ "and ( comparisonStatusRefId = :comparisonStatusRefId or comparisonStatusRefId = :comparisonStatusRefIdNa) "
									+ "and testExecutionStatusRefId = :testExecutionStatusRefId "
									+ "order by se.testExecutionStartTime desc");

			query.setParameter("suitId", suitId);
			query.setParameter("reportStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("comparisonStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("testExecutionStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());

			query.setParameter("reportStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("comparisonStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());

			scheduleExecutionList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getBaselineScheduleExcutionList :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getBaselineScheduleExcutionList");
		return scheduleExecutionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleExecution> getReportBaselineScheduleExcutionList(
			int suitId) {
		logger.debug("Entry: getReportBaselineScheduleExcutionList, suitId->"
				+ suitId);

		Query query = null;
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select se from Schedule s, ScheduleExecution se where s.scheduleId = se.scheduleId "
									+ "and s.suitId = :suitId and ( reportStatusRefId = :reportStatusRefId or reportStatusRefId = :reportStatusRefIdNa ) "
									+ "and ((comparisonStatusRefId = :comparisonStatusRefId or comparisonStatusRefId = :comparisonStatusRefIdNa) "
									+ "or(se.analyticsReportStatusId = :analyticsReportStatusId or se.analyticsReportStatusId = :analyticsReportStatusIdNa) "
									+ "or(se.brokenUrlReportStatusId = :brokenUrlReportStatusId or se.brokenUrlReportStatusId = :brokenUrlReportStatusIdNa)) "
									+ "and testExecutionStatusRefId = :testExecutionStatusRefId "
									+ "and se.testExecutionEndTime IS NOT NULL "
									+ "order by se.testExecutionStartTime desc");

			query.setParameter("suitId", suitId);
			query.setParameter("reportStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("comparisonStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("brokenUrlReportStatusId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("analyticsReportStatusId",
					ExecutionStatus.COMPLETE.getStatusCode());
			query.setParameter("testExecutionStatusRefId",
					ExecutionStatus.COMPLETE.getStatusCode());

			query.setParameter("reportStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("comparisonStatusRefIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("brokenUrlReportStatusIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());
			query.setParameter("analyticsReportStatusIdNa",
					ExecutionStatus.NOT_APPLICABLE.getStatusCode());

			scheduleExecutionList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getReportBaselineScheduleExcutionList :"
					+ e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getReportBaselineScheduleExcutionList");
		return scheduleExecutionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WebserviceSuite getSavedWsSuits(int webserviceSuiteId) {
		WebserviceSuite suit = new WebserviceSuite();
		List<WebserviceSuite> webserviceSuiteList = new ArrayList<WebserviceSuite>();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuite where webserviceSuiteId =:webserviceSuiteId ");

			query.setParameter("webserviceSuiteId", webserviceSuiteId);
			webserviceSuiteList = query.list();
			if (webserviceSuiteList.size() > 0) {
				suit = webserviceSuiteList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getSavedSuits :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSavedSuits");
		return suit;

	}

	/*@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public List<WebserviceSetupForm> getSavedWsSuitesParams(
			int webserviceSuiteId) {
		Query query = null;
		List<WebserviceSetupForm> listWebserviceSetupForm = new ArrayList<WebserviceSetupForm>();
		WebserviceSetupForm webserviceSetupForm = null;
		List<WebserviceSuiteService> listWebserviceSuiteService = new ArrayList<WebserviceSuiteService>();

		List<WebserviceSuiteParamSetTable> listWebserviceSuiteParamSetTable = new ArrayList<WebserviceSuiteParamSetTable>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuiteService where webserviceSuiteId =:webserviceSuiteId ");
			query.setParameter("webserviceSuiteId", webserviceSuiteId);

			String[] params;
			List<String> paramList = null;
			Integer param = 0;
			listWebserviceSuiteService = query.list();
			for (WebserviceSuiteService webserviceSuiteService : listWebserviceSuiteService) {
				webserviceSetupForm = new WebserviceSetupForm();
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WebserviceSuiteParamSetTable where webserviceSuiteServiceId =:webserviceSuiteServiceId ");
				query.setParameter("webserviceSuiteServiceId",
						webserviceSuiteService.getWebserviceSuiteServiceId());
				listWebserviceSuiteParamSetTable = query.list();
				paramList = new ArrayList<String>();
				for (WebserviceSuiteParamSetTable webserviceSuiteParamSetTable : listWebserviceSuiteParamSetTable) {
					paramList.add(Integer.toString(webserviceSuiteParamSetTable
							.getParameterSetId()));
				}

				params = new String[paramList.size()];
				params = paramList.toArray(params);

				// params = (String[]) paramList.toArray();
				webserviceSetupForm.setParams(params);
				webserviceSetupForm.setServiceId(webserviceSuiteService
						.getServiceId());
				webserviceSetupForm.setOperationId(webserviceSuiteService
						.getOperationId());
				webserviceSetupForm.setReqInputType(webserviceSuiteService
						.getInputType());
				listWebserviceSetupForm.add(webserviceSetupForm);
			}
		} catch (Exception e) {
			logger.error("Exception in getSavedWsSuitesParams :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return listWebserviceSetupForm;
	}
*/
	@Override
	public boolean saveScheduleScript(ScheduleScript scheduleScript) {
		logger.debug("Entry: saveScheduleScript");
		boolean isSaved = false;
		sessionFactory.getCurrentSession().save(scheduleScript);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		isSaved = true;
		logger.debug("Exit: saveScheduleScript");
		return isSaved;
	}

	@Override
	public boolean saveScheduleScriptXref(ScheduleScriptXref scheduleScriptXref) {
		logger.debug("Entry: saveScheduleScriptXref");
		boolean isSaved = false;
		sessionFactory.getCurrentSession().save(scheduleScriptXref);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		isSaved = true;
		logger.debug("Exit: saveScheduleScriptXref");
		return isSaved;
	}

	@Override
	public boolean updateScheduleScript(ScheduleScript scheduleScript) {
		logger.debug("Entry: updateScheduleScript");
		sessionFactory.getCurrentSession().update(scheduleScript);
		logger.debug("Exit: updateScheduleScript");
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleScript> getScheduleScript(Integer scheduleExecutionId) {
		logger.debug("Entry: getScheduleScript");
		Query query = null;
		List<ScheduleScript> scheduleScriptList = new ArrayList<ScheduleScript>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"From ScheduleScript where scheduleScriptId in (select scheduleScriptId from ScheduleScriptXref where scheduleExecutionId =:scheduleExecutionId)");
			query.setParameter("scheduleExecutionId", scheduleExecutionId);
			scheduleScriptList = query.list();
		} catch (Exception e) {
			logger.error("Exception while getting ScheduleScript detail for the scheduleExecutionId id :"
					+ scheduleExecutionId);
		}
		logger.debug("Exit: getScheduleScript");
		return scheduleScriptList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleExecution> getScheduleExecution(Integer scheduleId) {
		logger.debug("Entry: getScheduleExecution");
		Query query = null;
		List<ScheduleExecution> scheduleExecutionList = new ArrayList<ScheduleExecution>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ScheduleExecution where scheduleId =:scheduleId order by scheduleId desc");
			query.setParameter("scheduleId", scheduleId);

			scheduleExecutionList = query.list();
		} catch (Exception e) {
			logger.error("Exception while getting ScheduleScript detail for the schedule id :"
					+ scheduleId);
		}
		logger.debug("Exit: getScheduleExecution");

		return scheduleExecutionList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getChartData(String critearia, Integer criteriaId, Date fromDate, Date toDate) {
		logger.debug("Entry: getChartData");
		Query query = null;
		List<Object[]> objList = new ArrayList<Object[]>();
		try {
			
			String qryString ="";
			if(fromDate!=null && toDate!=null) {
					qryString = "select "+critearia+", count("+critearia+") from UsageReport" +
					" where startDateTime >=:startDateTime and endDateTime <=:endDateTime  group by "+critearia; 
					query = sessionFactory.getCurrentSession().createQuery(qryString);
					query.setTimestamp("startDateTime", DateTimeUtil.getStartOfTheDay(fromDate));
					query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(toDate));
			} else if(fromDate==null && toDate!=null) {
					qryString = "select "+critearia+", count("+critearia+") from UsageReport" +
					" where endDateTime <=:endDateTime  group by "+critearia; 
					query = sessionFactory.getCurrentSession().createQuery(qryString);
					query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(toDate));
				
			} else if(fromDate!=null && toDate==null) {
					qryString = "select "+critearia+", count("+critearia+") from UsageReport" +
					" where startDateTime >=:startDateTime and endDateTime <=:endDateTime  group by "+critearia; 
					query = sessionFactory.getCurrentSession().createQuery(qryString);
					query.setTimestamp("startDateTime", DateTimeUtil.getStartOfTheDay(fromDate));
					query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(new Date()));
			} else {
				qryString = "select "+critearia+", count("+critearia+") from UsageReport" +
				" group by "+critearia; 
				query = sessionFactory.getCurrentSession().createQuery(qryString);
				
			}
			//query.setParameter("critearia", critearia);
			objList = query.list();
		}catch(Exception e){
			logger.error("Exception in getChartData :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getChartData");
		
		return objList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getBarChartData(String reportCriteria, Integer reportCriteriaId, Date fromDate, Date toDate, String criteria,
			Integer criteriaId) {
		logger.debug("Entry: getBarChartData");
		Query query = null;
		List<Object[]> objList = new ArrayList<Object[]>();
		try {
			String qryString = "";
			
			if(fromDate!=null && toDate!=null) {
						qryString = "select "+reportCriteria+", count("+reportCriteria+") from UsageReport" +
						" where startDateTime >=:startDateTime and endDateTime <=:endDateTime " +
						" and "+criteria+" = "+ criteriaId +" group by "+reportCriteria; 
						query = sessionFactory.getCurrentSession().createQuery(qryString);
						query.setTimestamp("startDateTime", DateTimeUtil.getStartOfTheDay(fromDate));
						query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(toDate));
			} else if(fromDate==null && toDate!=null) {
						qryString = "select "+reportCriteria+", count("+reportCriteria+") from UsageReport" +
						" where endDateTime <=:endDateTime " +
						" and "+criteria+" = "+ criteriaId +" group by "+reportCriteria; 
						query = sessionFactory.getCurrentSession().createQuery(qryString);
						query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(toDate));
			} else if(fromDate!=null && toDate==null) {
						qryString = "select "+reportCriteria+", count("+reportCriteria+") from UsageReport" +
						" where startDateTime >=:startDateTime and endDateTime <=:endDateTime " +
						" and "+criteria+" = "+ criteriaId +" group by "+reportCriteria; 
						query = sessionFactory.getCurrentSession().createQuery(qryString);
						query.setTimestamp("startDateTime", DateTimeUtil.getStartOfTheDay(fromDate));
						query.setTimestamp("endDateTime", DateTimeUtil.getEndOfTheDay(new Date()));
			} else {
				qryString = "select "+reportCriteria+", count("+reportCriteria+") from UsageReport" +
				" where "+criteria+" = "+ criteriaId +" group by "+reportCriteria; 
				query = sessionFactory.getCurrentSession().createQuery(qryString);
			}
			objList = query.list();
		}catch(Exception e){
			logger.error("Exception in getBarChartData :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getBarChartData");
		
		return objList;
	}
}
