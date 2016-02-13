package com.ensense.insense.data.analytics.dao.impl;

import com.ensense.insense.data.analytics.common.DataSource;
import com.ensense.insense.data.analytics.dao.AnalyticsDashboardDAO;
import com.ensense.insense.data.analytics.entity.AnalyticsAuditSummary;
import com.ensense.insense.data.analytics.entity.DashboardsHomeDetails;
import com.ensense.insense.data.analytics.entity.DetailedView;
import com.ensense.insense.data.common.model.AnalyticsAuditSummaryReportDates;
import com.ensense.insense.data.transaction.entity.TestCaseFileUpload;
import com.ensense.insense.data.uitesting.entity.LoginUserDetails;
import com.ensense.insense.data.uitesting.entity.Reports;
import com.ensense.insense.data.uitesting.entity.Results;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class AnalyticsDashboardDAOImpl implements AnalyticsDashboardDAO {

	private static Logger logger = Logger
			.getLogger(AnalyticsDashboardDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	/*
	 * added by 303741 dao which returns data for dashboard for IWC - production
	 * which is the default chart
	 */
	@Override
	public DashboardsHomeDetails getDashBoardHomeDataDefault(
			LoginUserDetails loginUser) {

		logger.info("Entry getDashBoardHomeDataDefaults in DAO");
		List<DashboardsHomeDetails> dashboardsHomeDetailsList = new ArrayList<DashboardsHomeDetails>();
		DashboardsHomeDetails dashboardsHomeDetails = new DashboardsHomeDetails();
		Query query = null;

		try {
			// application ID is 1 for IWCand environment ID is 5 for Production
			// - in DB
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from DashboardsHomeDetails aa "
									+ " where aa.testScheduleId in ("
									+ " select ts.testScheduleId "
									+ "  from TestSchedule ts "
									+ "  where ts.applicationId=:applicationId and ts.environmentId=:environmentId"
									+ "  and ts. testScheduleId in("
									+ "     select tr. testScheduleId"
									+ "     from  Results  tr "
									+ " 	   where  tr. generatedDate in("
									+ " 			  select  tr. generatedDate from  Results  tr where tr.testScheduleId in ("
									+ " 					  select ts. testScheduleId from TestSchedule ts  where ts.applicationId=:applicationId and ts.environmentId=:environmentId)"
									+ "						and tr.testLoginId in (select lt.testLoginId from TestLoginUser lt where lt.applicationId=:applicationId and lt.environmentId=:environmentId)"
									+ " 			  order by  tr. generatedDate desc"
									+ " )))");

			query.setParameter("applicationId", loginUser.getApplicationId());
			query.setParameter("environmentId", loginUser.getEnvironmentId());

			logger.info("query value in DAO - Default: " + query);
			/*
			 * Pick only the latest report. As generated date is ordered in
			 * descending, first row will be the latest.
			 */
			if (query != null && query.list() != null
					&& query.list().size() > 0) {

				dashboardsHomeDetails = (DashboardsHomeDetails) query.list()
						.get(0);
				logger.info("testschedule id "
						+ dashboardsHomeDetails.getTestScheduleId());
				// logger.info("xml"
				// + dashboardsHomeDetails.getAuditAnalyticsXml());
				dashboardsHomeDetailsList.add(dashboardsHomeDetails);

			}
		} catch (Exception exp) {
			logger.error(exp);
		}
		return dashboardsHomeDetails;

	}

	/*
	 * added by 303741 dao returns data for dashboard for the application,
	 * environment, login ID and date chosen by user from the dropdown
	 * 
	 * @Override public DashboardsHomeDetails
	 * getDashBoardHomeDataForUserChosen(TestLoginUser loginUser, Reports
	 * forGeneratedDate ) {
	 * 
	 * logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
	 * DashboardsHomeDetails dashboardsHomeDetails = new
	 * DashboardsHomeDetails(); List<DashboardsHomeDetails>
	 * dashboardsHomeDetailsList = new ArrayList<DashboardsHomeDetails>(); Query
	 * query = null;
	 * 
	 * try{
	 * 
	 * query = sessionFactory.getCurrentSession(). createQuery(
	 * " from DashboardsHomeDetails aa where aa. testScheduleId in ("
	 * +"    select ts. testScheduleId from TestSchedule ts"
	 * +"    where ts. applicationId=:applicationId "
	 * +" and environmentId= :environmentId" +" and ts. testScheduleId in(" +
	 * "    select tr. testScheduleId from Results tr where tr.testLoginId=:testLoginId and date(tr. generatedDate) = (select date(r.generatedDate) from Reports r where test_reports_id= :testReportsId)))"
	 * );
	 * 
	 * query.setParameter("applicationId",loginUser.getApplicationId());
	 * query.setParameter("environmentId",loginUser.getEnvironmentId());
	 * query.setParameter("testLoginId",loginUser.getTestLoginId());
	 * query.setParameter("testReportsId", forGeneratedDate.getTestReportsId());
	 * 
	 * 
	 * logger.info("query value in DAO - Dropdown based: "+query);
	 * 
	 * if ( query != null && query.list() != null && query.list().size() > 0 ){
	 * 
	 * 
	 * dashboardsHomeDetails =(DashboardsHomeDetails)query.list().get(0);
	 * logger.info("xml"+dashboardsHomeDetails.getAuditAnalyticsXml());
	 * dashboardsHomeDetailsList.add(dashboardsHomeDetails);
	 * 
	 * } } catch(Exception exp){ logger.error(exp); } return
	 * dashboardsHomeDetails;
	 * 
	 * }
	 */

	/*
	 * added by 303741 dao returns data for detailed view
	 */
	/*
	 * @Override public DetailedViewDetails getDetailedViewDataFromDB() {
	 * 
	 * logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
	 * DetailedViewDetails detailedViewDetails = new DetailedViewDetails();
	 * Query query = null;
	 * 
	 * try{ query = sessionFactory.getCurrentSession(). createQuery(
	 * " from DetailedViewDetails " );
	 * 
	 * 
	 * 
	 * logger.info("query value in DAO - Dropdown based: "+query.list());
	 * 
	 * if ( query != null && query.list() != null && query.list().size() > 0 ){
	 * 
	 * detailedViewDetails =(DetailedViewDetails)query.list().get(0);
	 * 
	 * } } catch(Exception exp){ logger.error(exp); } return
	 * detailedViewDetails;
	 * 
	 * }
	 */

	/*
	 * added by 303741 dao returns data for detailed view for the application,
	 * environment, login ID and date chosen by user from the dropdown for
	 * default home_page url ---login ID based
	 */
	@Override
	public List<DetailedView> getDetailedViewDataFromDB(String pageURLdefault,
														LoginUserDetails loginUser, Reports forGeneratedDate) {

		logger.info("Entry getDetailedViewDataFromDB in DAO");
		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from DetailedView dv where dv.scheduleId in "
									+ "(select ts.testScheduleId from TestSchedule ts"
									+ "	where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from Results tr where"
									+ " tr.testLoginId =:testLoginId and date(tr.generatedDate)="
									+ "	(select date(r.generatedDate) from Reports r where test_reports_id=:testReportsId))))"
									+ " and dv.applicationName in "
									+ "(select applicationName from Application where applicationId= :applicationId ) and dv.pageURL LIKE :homePage");

			query.setParameter("applicationId", loginUser.getApplicationId());
			query.setParameter("environmentId", loginUser.getEnvironmentId());
			query.setParameter("testLoginId", loginUser.getLoginId());
			query.setParameter("testReportsId",
					forGeneratedDate.getReportsId());
			query.setParameter("homePage", "%" + pageURLdefault + "%");

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	/*
	 * added by 303741 - date- 6-11-2014 dao returns data for detailed view for
	 * the application, environment, test case ID and date chosen by user from
	 * the dropdown for default home_page url
	 */
	@Override
	public List<DetailedView> getDetailedViewDataForTestCase(
			String pageURLdefault, TestCaseFileUpload testcase,
			Reports forGeneratedDate) {

		logger.info("Entry getDetailedViewDataForTestCase in DAO");
		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from DetailedView dv where dv.scheduleId in "
									+ "(select ts.testScheduleId from TestSchedule ts"
									+ "	where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from Results tr where"
									+ " tr.testCaseId =:testCaseId and date(tr.generatedDate)="
									+ "	(select date(r.generatedDate) from Reports r where test_reports_id=:testReportsId))))"
									+ " and dv.applicationName in "
									+ "(select applicationName from Application where applicationId= :applicationId ) and dv.pageURL LIKE :homePage");

			query.setParameter("applicationId", testcase.getApplicationId());
			query.setParameter("environmentId", testcase.getEnvironmentId());
			query.setParameter("testCaseId", testcase.getTestCaseId());
			query.setParameter("testReportsId",
					forGeneratedDate.getReportsId());
			query.setParameter("homePage", "%" + pageURLdefault + "%");

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	/*
	 * added by 303741 dao returns data for detailed view for the application,
	 * environment, login ID and date chosen by user from the dropdown for page
	 * url chosen by the user
	 */
	@Override
	public List<DetailedView> getDetailedViewDataUserSearchBased(
			String pageURLSelected, LoginUserDetails loginUser,
			Results forGeneratedDate) {

		logger.info("Entry getDetailedViewDataUserSearchBased in DAO");

		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from DetailedView dv where dv.scheduleId in "
									+ "  (select ts.testScheduleId from TestSchedule ts"
									+ "	 where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from Results tr where"
									+ "  tr.testLoginId =:testLoginId and tr.resultsId=:resultsId))) "
									+ " and dv.pageURL=:pageURL");

			query.setParameter("applicationId", loginUser.getApplicationId());
			query.setParameter("environmentId", loginUser.getEnvironmentId());
			query.setParameter("testLoginId", loginUser.getLoginId());
			query.setParameter("resultsId",
					forGeneratedDate.getResultId());
			query.setParameter("pageURL", pageURLSelected);

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	/*
	 * added by 303741 - added on 6/12/2014 dao returns data for detailed view
	 * for the application, environment, test case and date chosen by user from
	 * the dropdown for page url chosen by the user
	 */
	@Override
	public List<DetailedView> getDetailedViewDataUserSearchBasedtc(
			String pageURLSelected, TestCaseFileUpload testcase,
			Results forGeneratedDate) {

		logger.info("Entry getDetailedViewDataUserSearchBasedtc in DAO");

		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from DetailedView dv where dv.scheduleId in "
									+ "  (select ts.testScheduleId from TestSchedule ts"
									+ "	 where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from Results tr where"
									+ "  tr.testCaseId =:testCaseId and tr.resultsId=:resultsId))) "
									+ " and dv.pageURL=:pageURL");

			query.setParameter("applicationId", testcase.getApplicationId());
			query.setParameter("environmentId", testcase.getEnvironmentId());
			query.setParameter("testCaseId", testcase.getTestCaseId());
			query.setParameter("resultsId",
					forGeneratedDate.getResultId());
			query.setParameter("pageURL", pageURLSelected);

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	/*
	 * added by 303741 - added on 6/12/2014 dao returns data for detailed view
	 * for the application, environment, test case and date chosen by user from
	 * the dropdown for page url chosen by the user
	 */
	@Override
	public List<DetailedView> getDetailedViewDataUserSearchBasedPublic(
			String pageURLSelected, Reports forGeneratedDate,
			Integer applicationId, Integer environmentId) {

		logger.info("Entry getDetailedViewDataUserSearchBasedPublic in DAO");

		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from DetailedView dv where dv.scheduleId in "
									+ "  (select ts.testScheduleId from TestSchedule ts"
									+ "	 where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from Results tr where"
									+ "  date(tr.generatedDate)="
									+ "	(select date(r.generatedDate) from Reports r where r.testReportsId=:testReportsId)))) "
									+ " and dv.pageURL=:pageURL");

			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentId", environmentId);
			query.setParameter("testReportsId",
					forGeneratedDate.getReportsId());
			query.setParameter("pageURL", pageURLSelected);

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	public List<String> getRelatedPageURLs(String searchKey,
			Results testResult) {
		logger.info("Entry getRelatedPageURLsfromDB in DAO, testResult->"
				+ testResult);
		Query query = null;
		List<String> pageURLs = new ArrayList<String>();
		String manager = "manager";
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select distinct pageURL from DetailedView where (pageURL LIKE :searchKey OR webAnalyticsPageDataXml LIKE %<tagurl>:searchKey<tagurl>%) and scheduleId =:testScheduleId");
			/*
			 * "select distinct pageURL from DetailedViewDetails where pageURL   LIKE :searchKey or  pageURL   LIKE :manager "
			 * +"  and applicationName =:applicationName");
			 */

			// where pageURL LIKE :searchKey
			query.setParameter("searchKey", "%" + searchKey + "%");
			query.setParameter("searchKey", "%" + searchKey + "%");
			query.setParameter("testScheduleId", testResult.getScheduleId());
			// query.setParameter("manager","%" + manager + "%");
			logger.info("getRelatedPageURLsfromDB: " + query.list());
			pageURLs = query.list();

		} catch (Exception exp) {
			logger.error("*****************************************************"
					+ exp);
			exp.printStackTrace();
			logger.error(exp);
		}
		return pageURLs;
	}

	@Override
	public List<String> getRelatedPageURLsfromDB(String searchKey,
			String applicationName) {
		logger.info("Entry getRelatedPageURLsfromDB in DAO");
		Query query = null;
		List<String> pageURLs = new ArrayList<String>();
		String manager = "manager";
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select distinct pageURL from DetailedView where pageURL LIKE :searchKey and applicationName =:applicationName");
			/*
			 * "select distinct pageURL from DetailedViewDetails where pageURL   LIKE :searchKey or  pageURL   LIKE :manager "
			 * +"  and applicationName =:applicationName");
			 */

			// where pageURL LIKE :searchKey
			query.setParameter("searchKey", "%" + searchKey + "%");
			query.setParameter("applicationName", applicationName);
			// query.setParameter("manager","%" + manager + "%");
			logger.info("getRelatedPageURLsfromDB: " + query.list());
			pageURLs = query.list();

		} catch (Exception exp) {
			logger.error("*****************************************************"
					+ exp);
			exp.printStackTrace();
			logger.error(exp);
		}
		return pageURLs;
	}

	@Override
	public Boolean insertServiceNames(String serviceName) {
		Boolean result = false;
		try {

			logger.info("insertServiceNames daoimpl");
			System.out.println("insertServiceNames daoimpl: ");
			String sql = "INSERT INTO test_services"
					+ "(service_name ) VALUES (?)";

			DataSource dataSource = new DataSource();
			JdbcTemplate jdbcTemplate = new JdbcTemplate(
					dataSource.getDataSource());
			jdbcTemplate.update(sql, new Object[] { serviceName });
			result = true;

		} catch (Exception exp) {
			logger.error("Exception occured in insertServiceNames ", exp);
			exp.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean insertOperationNames(String operationName, String serviceName) {
		Boolean result = false;
		try {

			logger.info("insertOperationNames daoimpl");
			System.out.println("insertOperationNames daoimpl: ");
			DataSource dataSource = new DataSource();
			JdbcTemplate jdbcTemplate = new JdbcTemplate(
					dataSource.getDataSource());

			int serviceID = jdbcTemplate
					.queryForInt(
							"select service_id from test_services where service_name= ?",
							new Object[] { serviceName });

			System.out.println("serviceID is: " + serviceID);

			String sql = "INSERT INTO test_services_operation"
					+ "(service_id,operation_name ) VALUES (?,?)";

			jdbcTemplate.update(sql, new Object[] { serviceID, operationName });
			result = true;

		} catch (Exception exp) {
			logger.error("Exception occured in insertOperationNames ", exp);
			exp.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean insertRequestParams(String reqParameter, String operationName) {
		Boolean result = false;
		try {

			logger.info("insertRquestParams daoimpl");
			System.out.println("insertRquestParams daoimpl: ");

			String oper_name = operationName;
			String sql = "INSERT INTO operation_request_parameters_table"
					+ "(service_id,operation_id , parameter_name ) VALUES (?, ?,?)";

			DataSource dataSource = new DataSource();

			JdbcTemplate jdbcTemplate = new JdbcTemplate(
					dataSource.getDataSource());

			int operID = jdbcTemplate
					.queryForInt(
							"select operation_id from test_services_operation where operation_name= ?",
							new Object[] { oper_name });
			int serviceID = jdbcTemplate
					.queryForInt(
							"select service_id from test_services_operation where operation_name= ?",
							new Object[] { oper_name });

			System.out.println("operID is: " + operID);
			System.out.println("serviceID is: " + serviceID);

			jdbcTemplate.update(sql, new Object[] { serviceID, operID,
					reqParameter });
			result = true;
		} catch (Exception exp) {
			logger.error("Exception occured in insertRquestParams ", exp);
			exp.printStackTrace();
		}
		return result;
	}

	@Override
	public AnalyticsAuditSummary getDashBoardHomeDataForUserChosen(
			AnalyticsAuditSummary analyticsAuditSummary) {

		logger.info("Entry getDashBoardHomeDataForUserChosen in DAO");
		AnalyticsAuditSummary analyticsAuditSummaryDetails = new AnalyticsAuditSummary();

		Query query = null;

		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from AnalyticsAuditSummary aa where aa.analyticsAuditSummaryId = :analyticsAuditSummaryId");
			query.setParameter("analyticsAuditSummaryId",
					analyticsAuditSummary.getAnalyticsAuditSummaryId());

			logger.info("analyticsAuditSummary.getAnalyticsAuditSummaryId() :"
					+ analyticsAuditSummary.getAnalyticsAuditSummaryId());
			logger.info("query value in DAO - Dropdown based User Choosen1: "
					+ query);

			if (query != null && query.list() != null
					&& query.list().size() > 0) {
				analyticsAuditSummaryDetails = (AnalyticsAuditSummary) query
						.list().get(0);
				logger.info("xml :"
						+ analyticsAuditSummaryDetails.getAnalyticsAuditXml());

			}
		} catch (Exception exp) {
			logger.error(exp);
		}
		return analyticsAuditSummaryDetails;

	}

	/*
	 * @Override public DashboardsHomeDetails
	 * getDashBoardHomeDataForUserChosenPublic( TestLoginUser loginUser,
	 * AnalyticsAuditSummary analyticsAuditSummary) {
	 * 
	 * logger.info("Entry: getDashBoardHomeDataForUserChosenPublic");
	 * logger.info("loginUser :"+loginUser);
	 * 
	 * DashboardsHomeDetails dashboardsHomeDetails = new
	 * DashboardsHomeDetails(); List<DashboardsHomeDetails>
	 * dashboardsHomeDetailsList = new ArrayList<DashboardsHomeDetails>(); Query
	 * query = null;
	 * 
	 * try { query = sessionFactory .getCurrentSession() .createQuery(
	 * " from DashboardsHomeDetails aa where aa.testScheduleId = :analyticsAuditSummaryId"
	 * ); query.setParameter("analyticsAuditSummaryId",
	 * analyticsAuditSummary.getAnalyticsAuditSummaryId());
	 * 
	 * logger.info("***** DAOIMPL Public Site1 ***: " + query);
	 * 
	 * if (query != null && query.list() != null && query.list().size() > 0) {
	 * dashboardsHomeDetails = (DashboardsHomeDetails) query.list() .get(0);
	 * logger.info("xml" + dashboardsHomeDetails.getAuditAnalyticsXml());
	 * dashboardsHomeDetailsList.add(dashboardsHomeDetails); }
	 * 
	 * logger.info("Exit: getDashBoardHomeDataForUserChosenPublic"); } catch
	 * (Exception exp) { logger.error(exp); } return dashboardsHomeDetails;
	 * 
	 * }
	 * 
	 * @Override public DashboardsHomeDetails
	 * getDashBoardHomeDataForUserChosenTestCase( TestCaseFileUpload testUser,
	 * AnalyticsAuditSummary analyticsAuditSummary) {
	 * 
	 * logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
	 * logger.info("selectedAuditSummary :"+analyticsAuditSummary);
	 * 
	 * DashboardsHomeDetails dashboardsHomeDetails = new
	 * DashboardsHomeDetails(); List<DashboardsHomeDetails>
	 * dashboardsHomeDetailsList = new ArrayList<DashboardsHomeDetails>(); Query
	 * query = null;
	 * 
	 * try {
	 * 
	 * query = sessionFactory .getCurrentSession() .createQuery(
	 * "from DashboardsHomeDetails aa where aa.testScheduleId = :analyticsAuditSummaryId"
	 * ); query.setParameter("analyticsAuditSummaryId",
	 * analyticsAuditSummary.getAnalyticsAuditSummaryId());
	 * 
	 * logger.info("query value in DAO - Dropdown based Test Case Name1 ***: " +
	 * query);
	 * logger.info("query value in DAO - Dropdown based Test Case Name size ***: "
	 * + query.list().size()); if (query != null && query.list() != null &&
	 * query.list().size() > 0) { dashboardsHomeDetails =
	 * (DashboardsHomeDetails) query.list() .get(0); logger.info("xml" +
	 * dashboardsHomeDetails.getAuditAnalyticsXml());
	 * dashboardsHomeDetailsList.add(dashboardsHomeDetails);
	 * 
	 * } } catch (Exception exp) { logger.error(exp); } return
	 * dashboardsHomeDetails;
	 * 
	 * }
	 */

	/*
	 * added by 303741 dao returns data for detailed view for the application,
	 * environment, login ID and date chosen by user from the dropdown for
	 * default home_page url ---login ID based
	 */
	@Override
	public List<DetailedView> getTestResultDetailedViewDataFromDB(
			String pageURLdefault, LoginUserDetails loginUser,
			Results forGeneratedDate) {

		logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
		Query query = null;

		try {

			/*
			 * select * from detailed_view_table dv where dv.schedule_id
			 * in(select ts.test_schedule_id from test_schedule_table ts where
			 * ts.application_id=1 and ts.environment_id=4 and
			 * ts.test_schedule_id in (select tr.test_schedule_id from
			 * test_results_table tr where tr.test_login_id=5 and
			 * tr.test_result_id=3)) and dv.application_name in(select
			 * application_name from applications_table where application_id=1)
			 * and dv.page_url Like '%Home%'
			 */

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from DetailedView dv where dv.scheduleId in"
									+ " (select ts.testScheduleId from TestSchedule ts"
									+ "	where ts.applicationId=:applicationId and ts.environmentId=:environmentId and ts.testScheduleId in"
									+ " (select tr.testScheduleId from TestResult tr where "
									+ " tr.testLoginId =:testLoginId and tr.testResultId=:testResultId)) and dv.applicationName in "
									+ " (select applicationName from Application where applicationId= :applicationId) and dv.pageURL LIKE :homePage");

			query.setParameter("applicationId", loginUser.getApplicationId());
			query.setParameter("environmentId", loginUser.getEnvironmentId());
			query.setParameter("testLoginId", loginUser.getLoginId());
			query.setParameter("testResultId",
					forGeneratedDate.getResultId());
			query.setParameter("homePage", "%" + pageURLdefault + "%");

			logger.info("query value in DAO - Dropdown based: " + query.list());
		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	/*
	 * added by 303741 - date- 6-11-2014 dao returns data for detailed view for
	 * the application, environment, test case ID and date chosen by user from
	 * the dropdown for default home_page url
	 */
	@Override
	public List<DetailedView> getTestResultDetailedViewDataForTestCase(
			String pageURLdefault, TestCaseFileUpload testcase,
			Results forGeneratedDate) {

		logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from DetailedView dv where dv.scheduleId in "
									+ "(select ts.testScheduleId from TestSchedule ts"
									+ "	where ts.applicationId=:applicationId and ts.environmentId=:environmentId and "
									+ "	(ts.testScheduleId in (select tr.testScheduleId from TestResult tr where"
									+ " tr.testCaseId =:testCaseId and tr.testResultId=:testResultId)))"
									+ " and dv.applicationName in "
									+ "(select applicationName from Application where applicationId= :applicationId ) and dv.pageURL LIKE :homePage");

			query.setParameter("applicationId", testcase.getApplicationId());
			query.setParameter("environmentId", testcase.getEnvironmentId());
			query.setParameter("testCaseId", testcase.getTestCaseId());
			query.setParameter("testResultId",
					forGeneratedDate.getResultId());
			query.setParameter("homePage", "%" + pageURLdefault + "%");

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	@Override
	public String getDefaultPageUrl(int testScheduleId) {
		Query query = null;
		String defaultUrl = "";
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from DetailedView dv where dv.scheduleId = :scheduleId");

			query.setParameter("scheduleId", testScheduleId);

			List<DetailedView> defaultUrlList = query.list();

			if (null != defaultUrlList && defaultUrlList.size() > 0) {
				defaultUrl = defaultUrlList.get(0).getPageURL();
			}
		} catch (Exception exp) {
			logger.error(exp);
			exp.printStackTrace();
		}
		logger.info("defaultUrl :" + defaultUrl);
		return defaultUrl;
	}

	/*
	 * added by 303741 - added on 6/12/2014 dao returns data for detailed view
	 * for the application, environment, test case and date chosen by user from
	 * the dropdown for page url chosen by the user
	 */
	@Override
	public List<DetailedView> getTestResultDetailedViewDataUserSearchBasedPublic(
			String pageURLSelected, Results forGeneratedDate,
			Integer applicationId, Integer environmentId) {

		logger.info("Entry getDashBoardHomeDataForUserChosens in DAO, forGeneratedDate->"
				+ forGeneratedDate);
		logger.info("pageURLSelected :" + pageURLSelected);

		Query query = null;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from DetailedView dv where dv.scheduleId = :scheduleId and dv.pageURL=:pageURL");
			// query.setParameter("applicationId", applicationId);
			// query.setParameter("environmentId", environmentId);
			query.setParameter("scheduleId",
					forGeneratedDate.getScheduleId());
			query.setParameter("pageURL", pageURLSelected);

			logger.info("query value in DAO - Dropdown based: " + query.list());

		} catch (Exception exp) {
			logger.error(exp);
		}
		return query.list();

	}

	// for comparing xmls
	/*
	 * @Override public List<DetailedViewDetails> getXMLDataFromDB() {
	 * 
	 * logger.info("Entry getDashBoardHomeDataForUserChosens in DAO");
	 * System.out.println("Entry getDashBoardHomeDataForUserChosens in DAO");
	 * Query query = null;
	 * 
	 * try{
	 * 
	 * System.out.println("sessionFactory::::"+sessionFactory); query =
	 * sessionFactory.getCurrentSession(). createQuery(
	 * " from DetailedViewDetails " );
	 * 
	 * System.out.println("compareXMLs class-getXMLDataFromDB....query "+query);
	 * logger.info("compareXMLs class-getXMLDataFromDB "+query.list());
	 * System.out.println("compareXMLs class-getXMLDataFromDB "+query.list()); }
	 * catch(Exception exp){ logger.error(exp); exp.printStackTrace(); } return
	 * query.list();
	 * 
	 * }
	 */

	@Override
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryPublicSiteReportsDates(
			LoginUserDetails testLoginUser) {
		logger.info("Entry: retrieveAnalyticsSummaryPublicSiteReportsDates, TestLoginUser->"
				+ testLoginUser);

		List<AnalyticsAuditSummaryReportDates> auditSummaryReportsList = new ArrayList<AnalyticsAuditSummaryReportDates>();
		List<AnalyticsAuditSummary> auditSummaryList = new ArrayList<AnalyticsAuditSummary>();

		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from AnalyticsAuditSummary where analyticsAuditSummaryId in ( "
								+ "select a.analyticsAuditSummaryId from AnalyticsAuditSummary a, TestSchedule ts where a.testScheduleId = ts.testScheduleId "
								+ "and ts.applicationId = :applicationId and ts.environmentId = :environmentId ) order by reportGeneratedDate desc");

		query.setParameter("applicationId", testLoginUser.getApplicationId());
		query.setParameter("environmentId", testLoginUser.getEnvironmentId());

		auditSummaryList = query.list();
		logger.info("auditSummaryList size :" + auditSummaryList.size());

		for (AnalyticsAuditSummary analyticsAuditSummary : auditSummaryList) {
			AnalyticsAuditSummaryReportDates analyticsAuditSummaryReportDates = new AnalyticsAuditSummaryReportDates();
			analyticsAuditSummaryReportDates
					.setAnalyticsAuditSummaryId(analyticsAuditSummary
							.getAnalyticsAuditSummaryId());
			analyticsAuditSummaryReportDates
					.setReportGeneratedDate(analyticsAuditSummary
							.getReportGeneratedDate());
			logger.info("analyticsAuditSummaryReportDates :"
					+ analyticsAuditSummaryReportDates);
			auditSummaryReportsList.add(analyticsAuditSummaryReportDates);
		}

		logger.info("Exit: retrieveAnalyticsSummaryPublicSiteReportsDates, AnalyticsAuditSummaryReportDatesList->"
				+ auditSummaryReportsList);
		return auditSummaryReportsList;
	}

	@Override
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDates(
			LoginUserDetails testLoginUser) {
		logger.info("Entry: retrieveAnalyticsSummaryReportsDates");
		logger.info("testLoginUser :" + testLoginUser);
		List<AnalyticsAuditSummaryReportDates> auditSummaryReportsList = new ArrayList<AnalyticsAuditSummaryReportDates>();
		List<AnalyticsAuditSummary> auditSummaryList = new ArrayList<AnalyticsAuditSummary>();

		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from AnalyticsAuditSummary where analyticsAuditSummaryId in ( "
								+ "select a.analyticsAuditSummaryId from AnalyticsAuditSummary a, TestScheduleLoginUser tslu, TestSchedule ts where a.testScheduleId = ts.testScheduleId and "
								+ "ts.testScheduleId = tslu.testScheduleId and tslu.testLoginId = :testLoginId "
								+ "and ts.applicationId = :applicationId and ts.environmentId = :environmentId )order by reportGeneratedDate desc");

		query.setParameter("testLoginId", testLoginUser.getLoginId());
		query.setParameter("applicationId", testLoginUser.getApplicationId());
		query.setParameter("environmentId", testLoginUser.getEnvironmentId());

		auditSummaryList = query.list();
		logger.info("auditSummaryList size :" + auditSummaryList.size());

		for (AnalyticsAuditSummary analyticsAuditSummary : auditSummaryList) {
			AnalyticsAuditSummaryReportDates analyticsAuditSummaryReportDates = new AnalyticsAuditSummaryReportDates();
			analyticsAuditSummaryReportDates
					.setAnalyticsAuditSummaryId(analyticsAuditSummary
							.getAnalyticsAuditSummaryId());
			analyticsAuditSummaryReportDates
					.setScheduleId(analyticsAuditSummary.getScheduleId());
			analyticsAuditSummaryReportDates
					.setReportGeneratedDate(analyticsAuditSummary
							.getReportGeneratedDate());
			logger.info("analyticsAuditSummaryReportDates :"
					+ analyticsAuditSummaryReportDates);
			auditSummaryReportsList.add(analyticsAuditSummaryReportDates);
		}

		logger.info("Exit: retrieveAnalyticsSummaryReportsDates, AnalyticsAuditSummaryReportDatesList->"
				+ auditSummaryReportsList);
		return auditSummaryReportsList;
	}
	
	@Override
    public String getDetailedviewxml(int detailedViewId) {
        Query query = null;
        String defaultUrl = "";
        try {
               query = sessionFactory.getCurrentSession().createQuery(
                            "from DetailedView dv where dv.detailedViewId = :detailedViewId");

               query.setParameter("detailedViewId", detailedViewId);

               List<DetailedView> defaultUrlList = query.list();

               if (null != defaultUrlList && defaultUrlList.size() > 0) {
                     defaultUrl = defaultUrlList.get(0).getWebAnalyticsPageDataXml();
               }
        } catch (Exception exp) {
               logger.error(exp);
               exp.printStackTrace();
        }
        logger.info("defaultUrl :" + defaultUrl);
        return defaultUrl;
 }
 
 
 @Override
 public int getUpdateDetailedviewxml(int detailedViewId,String updatedetailvalue){
        // TODO Auto-generated method stub
                     logger.info("Entry: updateApplicationDetails");
                     Query query = null ;
                     try{
                            query = sessionFactory.getCurrentSession().createQuery(" update DetailedView set " +
                                          " webanalyics_pagedata_xml = :webAnalyticsPageDataXml " +
                                          " where detailed_view_id= :detailedViewId");
                            query.setParameter("webAnalyticsPageDataXml",updatedetailvalue);
                            query.setParameter("detailedViewId",detailedViewId);
                            query.executeUpdate();
                     }
                     catch (Exception exp) {
                            
                            logger.error(exp);
                     }
                     logger.info("Entry: updateApplicationDetails");
                     return        query.executeUpdate();
 }

@Override
public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDatesForTransaction(int applicationId, int environmentId, int testCaseId) {
	logger.info("Entry: retrieveAnalyticsSummaryReportsDatesForTransaction");
	logger.info("testCaseId :" + testCaseId);
	logger.info("applicationId :" + applicationId);
	logger.info("environmentId :" + environmentId);
	
	List<AnalyticsAuditSummaryReportDates> auditSummaryReportsList = new ArrayList<AnalyticsAuditSummaryReportDates>();
	List<AnalyticsAuditSummary> auditSummaryList = new ArrayList<AnalyticsAuditSummary>();
	
	Query query = sessionFactory
	.getCurrentSession()
	.createQuery("from AnalyticsAuditSummary where analyticsAuditSummaryId in (select a.analyticsAuditSummaryId from AnalyticsAuditSummary a, TestcaseTransaction tt, TestSchedule ts where a.testScheduleId = ts.testScheduleId and "
					+ "ts.testScheduleId = tt.testScheduleId and tt.testCaseId = :testCaseId "
					+ "and ts.applicationId = :applicationId and ts.environmentId = :environmentId) order by reportGeneratedDate desc");

	query.setParameter("testCaseId", testCaseId);
	query.setParameter("applicationId", applicationId);
	query.setParameter("environmentId", environmentId);

	auditSummaryList = query.list();
	logger.info("auditSummaryList size :" + auditSummaryList.size());

	for (AnalyticsAuditSummary analyticsAuditSummary : auditSummaryList) {
		AnalyticsAuditSummaryReportDates analyticsAuditSummaryReportDates = new AnalyticsAuditSummaryReportDates();
		analyticsAuditSummaryReportDates
				.setAnalyticsAuditSummaryId(analyticsAuditSummary
						.getAnalyticsAuditSummaryId());
		analyticsAuditSummaryReportDates
				.setScheduleId(analyticsAuditSummary.getScheduleId());
		analyticsAuditSummaryReportDates
				.setReportGeneratedDate(analyticsAuditSummary
						.getReportGeneratedDate());
		logger.info("analyticsAuditSummaryReportDates :"
				+ analyticsAuditSummaryReportDates);
		auditSummaryReportsList.add(analyticsAuditSummaryReportDates);
	}

	logger.info("Exit: retrieveAnalyticsSummaryReportsDatesForTransaction, AnalyticsAuditSummaryReportDatesList->"
			+ auditSummaryReportsList);
	return auditSummaryReportsList;
}

}
