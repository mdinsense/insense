package com.ensense.insense.data.analytics.dao.impl;

import com.ensense.insense.data.analytics.dao.AnalyticsAuditSummaryTableDAO;
import com.ensense.insense.data.analytics.entity.AnalyticsAuditSummary;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class AnalyticsAuditSummaryTableDAOImpl implements
		AnalyticsAuditSummaryTableDAO {

	private static Logger logger = Logger.getLogger(AnalyticsAuditSummaryTableDAO.class);
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public boolean populateSummaryView(AnalyticsAuditSummary analyticsAuditSummary) {

		/*
		String sql = "INSERT INTO analytics_audit_summary_table"
				+ "(test_schedule_id,analytics_audit_xml , transaction_testcase_flag ) VALUES (?, ?,?)";

		DataSource dataSource = new DataSource();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource.getDataSource());

		jdbcTemplate.update(sql,
				new Object[] { analyticsAuditSummary.getTestscheduleid(),
						analyticsAuditSummary.getAnalyticsAuditXml(),
						analyticsAuditSummary.getTransactiontestflag() });
		*/
		
		boolean result = false;
		logger.info("Entry: populateSummaryView");
		try{
			sessionFactory.getCurrentSession().save(analyticsAuditSummary);
		}catch(HibernateException exp){
			logger.error(exp,new HibernateException(exp)); //TODO implement custom exception.
		}catch(Exception exp){
			logger.error(exp,new Exception(exp)); //TODO implement custom exception.
		}
		logger.info("Exit: populateSummaryView");
		return result;

	}
}
